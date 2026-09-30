import 'package:cloud_firestore/cloud_firestore.dart';

enum BookingStatus { requested, confirmed, inProgress, completed, cancelled }

extension BookingStatusExt on BookingStatus {
  String get label {
    switch (this) {
      case BookingStatus.requested:
        return 'Request sent';
      case BookingStatus.confirmed:
        return 'Confirmed';
      case BookingStatus.inProgress:
        return 'In Progress';
      case BookingStatus.completed:
        return 'Completed';
      case BookingStatus.cancelled:
        return 'Cancelled';
    }
  }
}

class BookingItem {
  final String id;
  final String serviceId;
  final String serviceTitle;
  final String emoji;
  final int price;
  final DateTime date;
  final String timeSlot;
  final String address;
  final BookingStatus status;
  final String assignedProName;
  final double proRating;

  BookingItem({
    required this.id,
    required this.serviceId,
    required this.serviceTitle,
    required this.emoji,
    required this.price,
    required this.date,
    required this.timeSlot,
    required this.address,
    this.status = BookingStatus.requested,
    this.assignedProName = 'Not assigned yet',
    this.proRating = 0,
    this.serviceFee = 49,
  });

  final int serviceFee;
  int get totalPrice => price + serviceFee;

  BookingItem copyWith({
    BookingStatus? status,
    DateTime? date,
    String? timeSlot,
  }) {
    return BookingItem(
      id: id,
      serviceId: serviceId,
      serviceTitle: serviceTitle,
      emoji: emoji,
      price: price,
      date: date ?? this.date,
      timeSlot: timeSlot ?? this.timeSlot,
      address: address,
      status: status ?? this.status,
      assignedProName: assignedProName,
      proRating: proRating,
      serviceFee: serviceFee,
    );
  }

  factory BookingItem.fromFirestore(
    DocumentSnapshot<Map<String, dynamic>> snapshot,
  ) {
    final data = snapshot.data() ?? const <String, dynamic>{};
    final rawStatus = data['status'] as String?;
    final status = BookingStatus.values.firstWhere(
      (value) => value.name == rawStatus,
      orElse: () => BookingStatus.confirmed,
    );
    final rawDate = data['date'];
    final date = rawDate is Timestamp ? rawDate.toDate() : DateTime.now();

    return BookingItem(
      id: snapshot.id,
      serviceId: data['serviceId'] as String? ?? '',
      serviceTitle: data['serviceTitle'] as String? ?? 'Service',
      emoji: data['emoji'] as String? ?? '🛠️',
      price: (data['price'] as num?)?.toInt() ?? 0,
      date: date,
      timeSlot: data['timeSlot'] as String? ?? '',
      address: data['address'] as String? ?? '',
      status: status,
      assignedProName: data['assignedProName'] as String? ?? 'Not assigned yet',
      proRating: (data['proRating'] as num?)?.toDouble() ?? 0,
      serviceFee: (data['serviceFee'] as num?)?.toInt() ?? 49,
    );
  }

  factory BookingItem.fromApi(Map<String, dynamic> data) {
    final rawStatus = (data['status'] ?? 'requested').toString();
    final status = BookingStatus.values.firstWhere(
      (value) => value.name == rawStatus.toLowerCase(),
      orElse: () => BookingStatus.requested,
    );
    return BookingItem(
      id: (data['id'] ?? '').toString(),
      serviceId: (data['serviceId'] ?? '').toString(),
      serviceTitle: (data['serviceTitle'] ?? 'Service').toString(),
      emoji: (data['emoji'] ?? '🛠️').toString(),
      price: (data['price'] as num?)?.toInt() ?? 0,
      date: DateTime.parse(data['date'].toString()),
      timeSlot: (data['timeSlot'] ?? '').toString(),
      address: (data['address'] ?? '').toString(),
      status: status,
      assignedProName: (data['assignedProName'] ?? 'Not assigned yet').toString(),
      proRating: (data['proRating'] as num?)?.toDouble() ?? 0,
      serviceFee: (data['serviceFee'] as num?)?.toInt() ?? 49,
    );
  }

  Map<String, dynamic> toFirestore() {
    return {
      'serviceId': serviceId,
      'serviceTitle': serviceTitle,
      'emoji': emoji,
      'price': price,
      'serviceFee': serviceFee,
      'totalPrice': totalPrice,
      'date': Timestamp.fromDate(date),
      'timeSlot': timeSlot,
      'address': address,
      'status': status.name,
      'assignedProName': assignedProName,
      'proRating': proRating,
      'updatedAt': FieldValue.serverTimestamp(),
    };
  }
}
