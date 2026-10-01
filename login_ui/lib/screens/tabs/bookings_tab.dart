import 'package:flutter/material.dart';

import '../../models/booking_item.dart';
import '../../services/booking_service.dart';

class BookingsTab extends StatefulWidget {
  final VoidCallback onExploreServices;

  const BookingsTab({super.key, required this.onExploreServices});

  @override
  State<BookingsTab> createState() => _BookingsTabState();
}

class _BookingsTabState extends State<BookingsTab> {
  bool _isLoading = true;

  @override
  void initState() {
    super.initState();
    BookingService().addListener(_onBookingsChanged);
    _loadBookings();
  }

  @override
  void dispose() {
    BookingService().removeListener(_onBookingsChanged);
    super.dispose();
  }

  void _onBookingsChanged() {
    if (mounted) setState(() {});
  }

  Future<void> _loadBookings() async {
    try {
      await BookingService().loadForCurrentUser();
    } finally {
      if (mounted) setState(() => _isLoading = false);
    }
  }

  Color _getStatusColor(BookingStatus s) {
    switch (s) {
      case BookingStatus.confirmed:
        return const Color(0xFF2563EB);
      case BookingStatus.inProgress:
        return const Color(0xFFD97706);
      case BookingStatus.completed:
        return const Color(0xFF10B981);
      case BookingStatus.cancelled:
        return const Color(0xFFDC2626);
    }
  }

  @override
  Widget build(BuildContext context) {
    final bookings = BookingService().bookings;

    return Scaffold(
      backgroundColor: const Color(0xFFF8FAFC),
      appBar: AppBar(
        backgroundColor: Colors.white,
        elevation: 0,
        title: const Text(
          'My Bookings',
          style: TextStyle(fontWeight: FontWeight.w800, fontSize: 18, color: Color(0xFF0F172A)),
        ),
      ),
        body: _isLoading
          ? const Center(child: CircularProgressIndicator())
          : bookings.isEmpty
          ? Center(
              child: Padding(
                padding: const EdgeInsets.all(32),
                child: Column(
                  mainAxisSize: MainAxisSize.min,
                  children: [
                    Container(
                      width: 80,
                      height: 80,
                      decoration: const BoxDecoration(
                        color: Color(0xFFEEF2FF),
                        shape: BoxShape.circle,
                      ),
                      child: const Icon(Icons.calendar_today_outlined, size: 40, color: Color(0xFF4F46E5)),
                    ),
                    const SizedBox(height: 16),
                    const Text(
                      'No Bookings Yet',
                      style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold, color: Color(0xFF0F172A)),
                    ),
                    const SizedBox(height: 8),
                    const Text(
                      'Book expert home maintenance, cleaning, beauty or grooming services with 1-tap.',
                      textAlign: TextAlign.center,
                      style: TextStyle(color: Color(0xFF64748B), fontSize: 13),
                    ),
                    const SizedBox(height: 20),
                    FilledButton(
                      onPressed: widget.onExploreServices,
                      style: FilledButton.styleFrom(
                        backgroundColor: const Color(0xFF0F172A),
                        shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
                      ),
                      child: const Text('Explore Services'),
                    ),
                  ],
                ),
              ),
            )
          : ListView.builder(
              padding: const EdgeInsets.all(16),
              itemCount: bookings.length,
              itemBuilder: (context, index) {
                final b = bookings[index];
                final statusColor = _getStatusColor(b.status);

                return Container(
                  margin: const EdgeInsets.only(bottom: 14),
                  padding: const EdgeInsets.all(16),
                  decoration: BoxDecoration(
                    color: Colors.white,
                    borderRadius: BorderRadius.circular(16),
                    border: Border.all(color: const Color(0xFFE2E8F0)),
                    boxShadow: [
                      BoxShadow(
                        color: Colors.black.withValues(alpha: 0.02),
                        blurRadius: 8,
                        offset: const Offset(0, 2),
                      ),
                    ],
                  ),
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      // Header Row
                      Row(
                        children: [
                          Text(b.emoji, style: const TextStyle(fontSize: 22)),
                          const SizedBox(width: 10),
                          Expanded(
                            child: Column(
                              crossAxisAlignment: CrossAxisAlignment.start,
                              children: [
                                Text(
                                  b.serviceTitle,
                                  style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 15, color: Color(0xFF0F172A)),
                                ),
                                Text(
                                  'ID: ${b.id}',
                                  style: TextStyle(fontSize: 11, color: Colors.grey.shade500, fontFamily: 'monospace'),
                                ),
                              ],
                            ),
                          ),
                          Container(
                            padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 4),
                            decoration: BoxDecoration(
                              color: statusColor.withValues(alpha: 0.1),
                              borderRadius: BorderRadius.circular(8),
                            ),
                            child: Text(
                              b.status.label,
                              style: TextStyle(fontSize: 12, fontWeight: FontWeight.bold, color: statusColor),
                            ),
                          ),
                        ],
                      ),

                      const Divider(height: 20),

                      // Appointment Date & Slot
                      Row(
                        children: [
                          const Icon(Icons.event_outlined, size: 16, color: Color(0xFF64748B)),
                          const SizedBox(width: 6),
                          Expanded(
                            child: Text(
                              '${b.date.day}/${b.date.month}/${b.date.year} • ${b.timeSlot}',
                              style: const TextStyle(fontWeight: FontWeight.w600, fontSize: 13, color: Color(0xFF334155)),
                            ),
                          ),
                        ],
                      ),

                      const SizedBox(height: 6),

                      // Address
                      Row(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          const Icon(Icons.location_on_outlined, size: 16, color: Color(0xFF64748B)),
                          const SizedBox(width: 6),
                          Expanded(
                            child: Text(
                              b.address,
                              style: const TextStyle(fontSize: 12, color: Color(0xFF64748B)),
                            ),
                          ),
                        ],
                      ),

                      const SizedBox(height: 10),

                      // Assigned Pro info
                      Container(
                        padding: const EdgeInsets.all(10),
                        decoration: BoxDecoration(
                          color: const Color(0xFFF8FAFC),
                          borderRadius: BorderRadius.circular(10),
                          border: Border.all(color: const Color(0xFFF1F5F9)),
                        ),
                        child: Row(
                          children: [
                            const Icon(Icons.verified_user_rounded, size: 18, color: Color(0xFF4F46E5)),
                            const SizedBox(width: 8),
                            Expanded(
                              child: Text(
                                b.assignedProName,
                                style: const TextStyle(fontSize: 12, fontWeight: FontWeight.w500, color: Color(0xFF1E293B)),
                              ),
                            ),
                            Row(
                              children: [
                                const Icon(Icons.star_rounded, size: 15, color: Colors.amber),
                                const SizedBox(width: 2),
                                Text('${b.proRating}', style: const TextStyle(fontSize: 11, fontWeight: FontWeight.bold)),
                              ],
                            ),
                          ],
                        ),
                      ),

                      const SizedBox(height: 12),

                      // Bottom actions & price
                      Row(
                        mainAxisAlignment: MainAxisAlignment.spaceBetween,
                        children: [
                          Text(
                            '₹${b.totalPrice}',
                            style: const TextStyle(fontWeight: FontWeight.w800, fontSize: 16, color: Color(0xFF0F172A)),
                          ),
                          if (b.status == BookingStatus.confirmed)
                            OutlinedButton(
                              onPressed: () {
                                showDialog<void>(
                                  context: context,
                                  builder: (ctx) => AlertDialog(
                                    title: const Text('Cancel Booking?'),
                                    content: Text('Are you sure you want to cancel the booking for ${b.serviceTitle}?'),
                                    actions: [
                                      TextButton(onPressed: () => Navigator.of(ctx).pop(), child: const Text('No')),
                                      FilledButton(
                                        onPressed: () {
                                          () async {
                                            try {
                                              await BookingService().cancelBooking(b.id);
                                              if (ctx.mounted) Navigator.of(ctx).pop();
                                            } catch (error) {
                                              if (!ctx.mounted) return;
                                              ScaffoldMessenger.of(context).showSnackBar(
                                                SnackBar(content: Text(error.toString())),
                                              );
                                            }
                                          }();
                                        },
                                        style: FilledButton.styleFrom(backgroundColor: const Color(0xFFDC2626)),
                                        child: const Text('Yes, Cancel'),
                                      ),
                                    ],
                                  ),
                                );
                              },
                              style: OutlinedButton.styleFrom(
                                foregroundColor: const Color(0xFFDC2626),
                                side: const BorderSide(color: Color(0xFFFCA5A5)),
                                shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(8)),
                                padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 4),
                              ),
                              child: const Text('Cancel', style: TextStyle(fontSize: 12)),
                            )
                          else
                            TextButton.icon(
                              onPressed: () {
                                ScaffoldMessenger.of(context).showSnackBar(
                                  const SnackBar(content: Text('Invoice downloaded to receipts')),
                                );
                              },
                              icon: const Icon(Icons.receipt_long_outlined, size: 16),
                              label: const Text('Receipt', style: TextStyle(fontSize: 12)),
                            ),
                        ],
                      ),
                    ],
                  ),
                );
              },
            ),
    );
  }
}
