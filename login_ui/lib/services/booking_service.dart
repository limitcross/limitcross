import 'package:flutter/material.dart';

import '../models/booking_item.dart';
import '../models/service_item.dart';
import 'api_client.dart';
import 'auth_service.dart';

class BookingService extends ChangeNotifier {
  static final BookingService _instance = BookingService._internal();
  factory BookingService() => _instance;

  BookingService._internal();

  String _currentCity = 'Delhi NCR';
  String get currentCity => _currentCity;

  static const List<String> availableCities = [
    'Delhi NCR',
    'Bengaluru',
    'Mumbai',
    'Hyderabad',
    'Pune',
    'Chennai',
    'Kolkata',
    'Ahmedabad',
    'Jaipur',
    'Dubai (UAE)',
    'Singapore',
  ];

  final List<BookingItem> _bookings = [];
  List<BookingItem> get bookings => List.unmodifiable(_bookings);

  void setCity(String city) {
    if (_currentCity != city) {
      _currentCity = city;
      notifyListeners();
    }
  }

  Future<void> loadForCurrentUser() async {
    if (AuthService().currentUser == null) {
      _bookings.clear();
      notifyListeners();
      return;
    }

    final response = await ApiClient.instance.get('/bookings');
    if (response is! List) {
      throw const FormatException('Invalid bookings response from the server.');
    }
    _bookings
      ..clear()
      ..addAll(
        response.whereType<Map<String, dynamic>>().map(BookingItem.fromMap),
      );
    notifyListeners();
  }

  Future<BookingItem> createBooking({
    required ServiceItem service,
    required DateTime date,
    required String timeSlot,
    required String address,
  }) async {
    if (AuthService().currentUser == null) {
      throw StateError('You must be signed in to create a booking.');
    }

    final response = await ApiClient.instance.post('/bookings', {
      'serviceId': service.id,
      'date': _dateOnly(date),
      'timeSlot': timeSlot,
      'address': address.trim(),
    });
    if (response is! Map<String, dynamic>) {
      throw const FormatException('Invalid booking response from the server.');
    }
    final booking = BookingItem.fromMap(response);
    _bookings.insert(0, booking);
    notifyListeners();
    return booking;
  }

  Future<void> cancelBooking(String id) async {
    final index = _bookings.indexWhere((booking) => booking.id == id);
    if (index == -1) return;
    final booking = _bookings[index];
    if (booking.status != BookingStatus.requested &&
        booking.status != BookingStatus.confirmed) {
      throw StateError('This booking can no longer be cancelled.');
    }

    final response = await ApiClient.instance.post(
      '/bookings/${Uri.encodeComponent(id)}/cancel',
    );
    if (response is Map<String, dynamic>) {
      _bookings[index] = BookingItem.fromMap(response);
    } else {
      _bookings[index] = booking.copyWith(status: BookingStatus.cancelled);
    }
    notifyListeners();
  }

  Future<void> rescheduleBooking({
    required String id,
    required DateTime date,
    required String timeSlot,
  }) async {
    final index = _bookings.indexWhere((booking) => booking.id == id);
    if (index == -1) return;
    final booking = _bookings[index];
    if (booking.status != BookingStatus.requested &&
        booking.status != BookingStatus.confirmed) {
      throw StateError('This booking can no longer be rescheduled.');
    }
    if (!date.isAfter(DateTime.now())) {
      throw ArgumentError('Choose a future date for your service.');
    }

    if (AuthService().currentUser == null) {
      throw StateError('You must be signed in to reschedule a booking.');
    }

    final response = await ApiClient.instance.patch(
      '/bookings/${Uri.encodeComponent(id)}',
      {
      'date': _dateOnly(date),
      'timeSlot': timeSlot,
      },
    );
    _bookings[index] = response is Map<String, dynamic>
        ? BookingItem.fromMap(response)
        : booking.copyWith(date: date, timeSlot: timeSlot);
    notifyListeners();
  }

  static String _dateOnly(DateTime date) =>
      '${date.year.toString().padLeft(4, '0')}-${date.month.toString().padLeft(2, '0')}-${date.day.toString().padLeft(2, '0')}';
}
