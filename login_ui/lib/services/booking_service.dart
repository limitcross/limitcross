import 'package:flutter/material.dart';

import '../models/booking_item.dart';
import '../models/service_item.dart';
import 'api_client.dart';

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
    final response = await ApiClient.instance.get('/bookings') as List<dynamic>;
    _bookings
      ..clear()
      ..addAll(response.map((item) => BookingItem.fromApi(item as Map<String, dynamic>)));
    notifyListeners();
  }

  Future<BookingItem> createBooking({
    required ServiceItem service,
    required DateTime date,
    required String timeSlot,
    required String address,
  }) async {
    final response = await ApiClient.instance.post('/bookings', {
      'serviceId': service.id,
      'date': date.toIso8601String().split('T').first,
      'timeSlot': timeSlot,
      'address': address.trim(),
    }) as Map<String, dynamic>;
    final booking = BookingItem.fromApi(response);
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

    await ApiClient.instance.post('/bookings/$id/cancel');
    _bookings[index] = _bookings[index].copyWith(
      status: BookingStatus.cancelled,
    );
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

    await ApiClient.instance.patch('/bookings/$id', {
      'date': date.toIso8601String().split('T').first,
      'timeSlot': timeSlot,
    });
    _bookings[index] = booking.copyWith(date: date, timeSlot: timeSlot);
    notifyListeners();
  }
}
