import 'package:flutter/material.dart';

import '../../models/booking_item.dart';
import '../../services/booking_service.dart';
import '../../theme/app_theme.dart';

class BookingsTab extends StatefulWidget {
  final VoidCallback onExploreServices;

  const BookingsTab({super.key, required this.onExploreServices});

  @override
  State<BookingsTab> createState() => _BookingsTabState();
}

class _BookingsTabState extends State<BookingsTab> {
  bool _isLoading = true;
  String? _loadError;
  static const _timeSlots = [
    '08:00 AM - 10:00 AM',
    '10:00 AM - 12:00 PM',
    '01:00 PM - 03:00 PM',
    '04:00 PM - 06:00 PM',
    '06:30 PM - 08:30 PM',
  ];

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
    setState(() {
      _isLoading = true;
      _loadError = null;
    });
    try {
      await BookingService().loadForCurrentUser();
    } catch (error) {
      _loadError = error.toString();
    } finally {
      if (mounted) setState(() => _isLoading = false);
    }
  }

  Future<void> _reschedule(BookingItem booking) async {
    final firstDate = DateTime.now().add(const Duration(days: 1));
    final initialDate = booking.date.isBefore(firstDate)
        ? firstDate
        : booking.date;
    final date = await showDatePicker(
      context: context,
      initialDate: initialDate,
      firstDate: firstDate,
      lastDate: DateTime.now().add(const Duration(days: 90)),
      helpText: 'Choose a new service date',
    );
    if (date == null || !mounted) return;

    var selectedSlot = _timeSlots.contains(booking.timeSlot)
        ? booking.timeSlot
        : _timeSlots.first;
    final timeSlot = await showDialog<String>(
      context: context,
      builder: (dialogContext) => StatefulBuilder(
        builder: (context, setDialogState) => AlertDialog(
          title: const Text('Choose a time window'),
          content: DropdownButtonFormField<String>(
            initialValue: selectedSlot,
            items: _timeSlots
                .map((slot) => DropdownMenuItem(value: slot, child: Text(slot)))
                .toList(),
            onChanged: (value) {
              if (value != null) {
                setDialogState(() => selectedSlot = value);
              }
            },
          ),
          actions: [
            TextButton(
              onPressed: () => Navigator.of(dialogContext).pop(),
              child: const Text('Keep current slot'),
            ),
            FilledButton(
              onPressed: () => Navigator.of(dialogContext).pop(selectedSlot),
              child: const Text('Update request'),
            ),
          ],
        ),
      ),
    );
    if (timeSlot == null) return;

    try {
      await BookingService().rescheduleBooking(
        id: booking.id,
        date: date,
        timeSlot: timeSlot,
      );
      if (!mounted) return;
      ScaffoldMessenger.of(
        context,
      ).showSnackBar(const SnackBar(content: Text('Booking request updated.')));
    } catch (error) {
      if (!mounted) return;
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(
          content: Text(error.toString()),
          backgroundColor: Theme.of(context).colorScheme.error,
        ),
      );
    }
  }

  Future<void> _cancel(BookingItem booking) async {
    final confirmed = await showDialog<bool>(
      context: context,
      builder: (dialogContext) => AlertDialog(
        title: const Text('Cancel booking request?'),
        content: Text('Cancel your ${booking.serviceTitle} request?'),
        actions: [
          TextButton(
            onPressed: () => Navigator.of(dialogContext).pop(false),
            child: const Text('Keep request'),
          ),
          FilledButton(
            onPressed: () => Navigator.of(dialogContext).pop(true),
            child: const Text('Cancel request'),
          ),
        ],
      ),
    );
    if (confirmed != true) return;

    try {
      await BookingService().cancelBooking(booking.id);
    } catch (error) {
      if (!mounted) return;
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(
          content: Text(error.toString()),
          backgroundColor: Theme.of(context).colorScheme.error,
        ),
      );
    }
  }

  Color _getStatusColor(BookingStatus s) {
    switch (s) {
      case BookingStatus.requested:
        return const Color(0xFFB45309);
      case BookingStatus.confirmed:
        return const Color(0xFF176B57);
      case BookingStatus.inProgress:
        return const Color(0xFFD97706);
      case BookingStatus.completed:
        return const Color(0xFF315C49);
      case BookingStatus.cancelled:
        return const Color(0xFFDC2626);
    }
  }

  @override
  Widget build(BuildContext context) {
    final bookings = BookingService().bookings;

    return Scaffold(
      backgroundColor: AppTheme.surface,
      appBar: AppBar(
        backgroundColor: Colors.white,
        elevation: 0,
        title: const Text(
          'My Bookings',
          style: TextStyle(
            fontWeight: FontWeight.w800,
            fontSize: 18,
            color: AppTheme.ink,
          ),
        ),
      ),
      body: _isLoading
          ? const Center(child: CircularProgressIndicator())
          : _loadError != null
          ? Center(
              child: Padding(
                padding: const EdgeInsets.all(24),
                child: Column(
                  mainAxisSize: MainAxisSize.min,
                  children: [
                    const Icon(Icons.cloud_off_outlined, size: 40),
                    const SizedBox(height: 12),
                    const Text('Bookings could not be loaded.'),
                    const SizedBox(height: 12),
                    FilledButton.icon(
                      onPressed: _loadBookings,
                      icon: const Icon(Icons.refresh),
                      label: const Text('Try again'),
                    ),
                  ],
                ),
              ),
            )
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
                        color: AppTheme.surface,
                        shape: BoxShape.circle,
                      ),
                      child: const Icon(
                        Icons.calendar_today_outlined,
                        size: 40,
                        color: AppTheme.seed,
                      ),
                    ),
                    const SizedBox(height: 16),
                    const Text(
                      'No Bookings Yet',
                      style: TextStyle(
                        fontSize: 18,
                        fontWeight: FontWeight.bold,
                        color: Color(0xFF0F172A),
                      ),
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
                        backgroundColor: AppTheme.ink,
                        shape: RoundedRectangleBorder(
                          borderRadius: BorderRadius.circular(8),
                        ),
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
                    borderRadius: BorderRadius.circular(8),
                    border: Border.all(color: AppTheme.border),
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
                                  style: const TextStyle(
                                    fontWeight: FontWeight.bold,
                                    fontSize: 15,
                                    color: Color(0xFF0F172A),
                                  ),
                                ),
                                Text(
                                  'ID: ${b.id}',
                                  style: TextStyle(
                                    fontSize: 11,
                                    color: Colors.grey.shade500,
                                    fontFamily: 'monospace',
                                  ),
                                ),
                              ],
                            ),
                          ),
                          Container(
                            padding: const EdgeInsets.symmetric(
                              horizontal: 10,
                              vertical: 4,
                            ),
                            decoration: BoxDecoration(
                              color: statusColor.withValues(alpha: 0.1),
                              borderRadius: BorderRadius.circular(6),
                            ),
                            child: Text(
                              b.status.label,
                              style: TextStyle(
                                fontSize: 12,
                                fontWeight: FontWeight.bold,
                                color: statusColor,
                              ),
                            ),
                          ),
                        ],
                      ),

                      const Divider(height: 20),

                      // Appointment Date & Slot
                      Row(
                        children: [
                          const Icon(
                            Icons.event_outlined,
                            size: 16,
                            color: Color(0xFF64748B),
                          ),
                          const SizedBox(width: 6),
                          Expanded(
                            child: Text(
                              '${b.date.day}/${b.date.month}/${b.date.year} • ${b.timeSlot}',
                              style: const TextStyle(
                                fontWeight: FontWeight.w600,
                                fontSize: 13,
                                color: Color(0xFF334155),
                              ),
                            ),
                          ),
                        ],
                      ),

                      const SizedBox(height: 6),

                      // Address
                      Row(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          const Icon(
                            Icons.location_on_outlined,
                            size: 16,
                            color: Color(0xFF64748B),
                          ),
                          const SizedBox(width: 6),
                          Expanded(
                            child: Text(
                              b.address,
                              style: const TextStyle(
                                fontSize: 12,
                                color: Color(0xFF64748B),
                              ),
                            ),
                          ),
                        ],
                      ),

                      const SizedBox(height: 10),

                      if (b.status == BookingStatus.requested)
                        Container(
                          padding: const EdgeInsets.all(10),
                          decoration: BoxDecoration(
                            color: const Color(0xFFFFFBEB),
                            borderRadius: BorderRadius.circular(8),
                          ),
                          child: const Row(
                            children: [
                              Icon(
                                Icons.hourglass_top,
                                size: 18,
                                color: Color(0xFFB45309),
                              ),
                              SizedBox(width: 8),
                              Expanded(
                                child: Text(
                                  'Request received. Confirmation is pending.',
                                  style: TextStyle(
                                    fontSize: 12,
                                    color: Color(0xFF78350F),
                                  ),
                                ),
                              ),
                            ],
                          ),
                        )
                      else if (b.assignedProName != 'Not assigned yet')
                        Container(
                          padding: const EdgeInsets.all(10),
                          decoration: BoxDecoration(
                            color: const Color(0xFFF8FAFC),
                            borderRadius: BorderRadius.circular(8),
                          ),
                          child: Row(
                            children: [
                              const Icon(
                                Icons.verified_user_rounded,
                                size: 18,
                                color: Color(0xFF0F766E),
                              ),
                              const SizedBox(width: 8),
                              Expanded(child: Text(b.assignedProName)),
                              if (b.proRating > 0) ...[
                                const Icon(
                                  Icons.star_rounded,
                                  size: 15,
                                  color: Colors.amber,
                                ),
                                const SizedBox(width: 2),
                                Text('${b.proRating}'),
                              ],
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
                            style: const TextStyle(
                              fontWeight: FontWeight.w800,
                              fontSize: 16,
                              color: Color(0xFF0F172A),
                            ),
                          ),
                          if (b.status == BookingStatus.requested ||
                              b.status == BookingStatus.confirmed)
                            Wrap(
                              spacing: 4,
                              children: [
                                TextButton.icon(
                                  onPressed: () => _reschedule(b),
                                  icon: const Icon(
                                    Icons.edit_calendar_outlined,
                                    size: 16,
                                  ),
                                  label: const Text('Change time'),
                                ),
                                TextButton.icon(
                                  onPressed: () => _cancel(b),
                                  icon: const Icon(Icons.close, size: 16),
                                  label: const Text('Cancel'),
                                  style: TextButton.styleFrom(
                                    foregroundColor: const Color(0xFFB42318),
                                  ),
                                ),
                              ],
                            )
                          else
                            TextButton.icon(
                              onPressed: () {
                                ScaffoldMessenger.of(context).showSnackBar(
                                  const SnackBar(
                                    content: Text(
                                      'Invoice downloaded to receipts',
                                    ),
                                  ),
                                );
                              },
                              icon: const Icon(
                                Icons.receipt_long_outlined,
                                size: 16,
                              ),
                              label: const Text(
                                'Receipt',
                                style: TextStyle(fontSize: 12),
                              ),
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
