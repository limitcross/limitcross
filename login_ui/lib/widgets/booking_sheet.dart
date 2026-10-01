import 'package:flutter/material.dart';

import '../models/service_item.dart';
import '../services/auth_service.dart';
import '../services/booking_service.dart';

class BookingSheet extends StatefulWidget {
  final ServiceItem service;

  const BookingSheet({super.key, required this.service});

  static Future<bool?> show(BuildContext context, ServiceItem service) {
    return showModalBottomSheet<bool>(
      context: context,
      isScrollControlled: true,
      backgroundColor: Colors.transparent,
      builder: (_) => BookingSheet(service: service),
    );
  }

  @override
  State<BookingSheet> createState() => _BookingSheetState();
}

class _BookingSheetState extends State<BookingSheet> {
  DateTime _selectedDate = DateTime.now().add(const Duration(days: 1));
  String _selectedSlot = '10:00 AM - 12:00 PM';
  final _addressController = TextEditingController();
  bool _isBooking = false;

  final List<String> _slots = [
    '08:00 AM - 10:00 AM',
    '10:00 AM - 12:00 PM',
    '01:00 PM - 03:00 PM',
    '04:00 PM - 06:00 PM',
    '06:30 PM - 08:30 PM',
  ];

  @override
  void dispose() {
    _addressController.dispose();
    super.dispose();
  }

  String _formatDate(DateTime d) {
    final now = DateTime.now();
    if (d.year == now.year && d.month == now.month && d.day == now.day) {
      return 'Today';
    }
    final tomorrow = now.add(const Duration(days: 1));
    if (d.year == tomorrow.year &&
        d.month == tomorrow.month &&
        d.day == tomorrow.day) {
      return 'Tomorrow';
    }
    const months = [
      'Jan',
      'Feb',
      'Mar',
      'Apr',
      'May',
      'Jun',
      'Jul',
      'Aug',
      'Sep',
      'Oct',
      'Nov',
      'Dec',
    ];
    return '${d.day} ${months[d.month - 1]}';
  }

  Future<void> _submitBooking() async {
    if (AuthService().currentUser == null) {
      if (!mounted) return;
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(
          content: Text('Please sign in before booking a service.'),
        ),
      );
      return;
    }

    if (_addressController.text.trim().length < 8) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Enter a complete service address.')),
      );
      return;
    }

    setState(() => _isBooking = true);
    try {
      await BookingService().createBooking(
        service: widget.service,
        date: _selectedDate,
        timeSlot: _selectedSlot,
        address: _addressController.text,
      );
      if (!mounted) return;
      Navigator.of(context).pop(true);
    } catch (error) {
      if (!mounted) return;
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(
          content: Text(error.toString()),
          backgroundColor: Theme.of(context).colorScheme.error,
        ),
      );
    } finally {
      if (mounted) setState(() => _isBooking = false);
    }
  }

  @override
  Widget build(BuildContext context) {
    final colors = Theme.of(context).colorScheme;
    final item = widget.service;
    final bottomInset = MediaQuery.of(context).viewInsets.bottom;

    return Container(
      padding: EdgeInsets.only(bottom: bottomInset),
      decoration: const BoxDecoration(
        color: Colors.white,
        borderRadius: BorderRadius.vertical(top: Radius.circular(28)),
      ),
      child: SafeArea(
        top: false,
        child: SingleChildScrollView(
          padding: const EdgeInsets.fromLTRB(20, 12, 20, 24),
          child: Column(
            mainAxisSize: MainAxisSize.min,
            crossAxisAlignment: CrossAxisAlignment.stretch,
            children: [
              Center(
                child: Container(
                  width: 48,
                  height: 5,
                  margin: const EdgeInsets.only(bottom: 16),
                  decoration: BoxDecoration(
                    color: Colors.grey.shade300,
                    borderRadius: BorderRadius.circular(10),
                  ),
                ),
              ),

              // Header
              Row(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Container(
                    width: 56,
                    height: 56,
                    decoration: BoxDecoration(
                      color: item.accentColor.withValues(alpha: 0.12),
                      borderRadius: BorderRadius.circular(16),
                    ),
                    alignment: Alignment.center,
                    child: Text(
                      item.emoji,
                      style: const TextStyle(fontSize: 28),
                    ),
                  ),
                  const SizedBox(width: 14),
                  Expanded(
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        Text(
                          item.title,
                          style: const TextStyle(
                            fontSize: 18,
                            fontWeight: FontWeight.bold,
                            color: Color(0xFF0F172A),
                          ),
                        ),
                        const SizedBox(height: 4),
                        Wrap(
                          spacing: 4,
                          runSpacing: 4,
                          crossAxisAlignment: WrapCrossAlignment.center,
                          children: [
                            const Icon(
                              Icons.star_rounded,
                              size: 18,
                              color: Colors.amber,
                            ),
                            Text(
                              '${item.rating}',
                              style: const TextStyle(
                                fontWeight: FontWeight.w600,
                                fontSize: 12,
                              ),
                            ),
                            Text(
                              '${item.reviewsCount} reviews',
                              style: TextStyle(
                                color: Colors.grey.shade600,
                                fontSize: 11,
                              ),
                            ),
                            Container(
                              padding: const EdgeInsets.symmetric(
                                horizontal: 8,
                                vertical: 3,
                              ),
                              decoration: BoxDecoration(
                                color: Colors.indigo.shade50,
                                borderRadius: BorderRadius.circular(8),
                              ),
                              child: Text(
                                item.duration,
                                style: TextStyle(
                                  fontSize: 11,
                                  fontWeight: FontWeight.w600,
                                  color: Colors.indigo.shade700,
                                ),
                              ),
                            ),
                          ],
                        ),
                      ],
                    ),
                  ),
                ],
              ),

              const SizedBox(height: 14),
              Text(
                item.description,
                style: TextStyle(color: Colors.grey.shade700, fontSize: 13),
              ),

              const SizedBox(height: 12),
              // Highlights
              Wrap(
                spacing: 8,
                runSpacing: 6,
                children: item.highlights.map((h) {
                  return Container(
                    padding: const EdgeInsets.symmetric(
                      horizontal: 10,
                      vertical: 5,
                    ),
                    decoration: BoxDecoration(
                      color: const Color(0xFFF1F5F9),
                      borderRadius: BorderRadius.circular(8),
                    ),
                    child: Row(
                      mainAxisSize: MainAxisSize.min,
                      children: [
                        const Icon(
                          Icons.check_circle,
                          size: 14,
                          color: Color(0xFF10B981),
                        ),
                        const SizedBox(width: 6),
                        Text(
                          h,
                          style: const TextStyle(
                            fontSize: 12,
                            fontWeight: FontWeight.w500,
                            color: Color(0xFF334155),
                          ),
                        ),
                      ],
                    ),
                  );
                }).toList(),
              ),

              const Divider(height: 28),

              // Select Date
              const Text(
                'Select Date',
                style: TextStyle(
                  fontSize: 14,
                  fontWeight: FontWeight.bold,
                  color: Color(0xFF0F172A),
                ),
              ),
              const SizedBox(height: 10),
              SingleChildScrollView(
                scrollDirection: Axis.horizontal,
                child: Row(
                  children: List.generate(7, (index) {
                    final date = DateTime.now().add(Duration(days: index + 1));
                    final isSelected =
                        _selectedDate.day == date.day &&
                        _selectedDate.month == date.month;

                    return Padding(
                      padding: const EdgeInsets.only(right: 10),
                      child: InkWell(
                        onTap: () => setState(() => _selectedDate = date),
                        borderRadius: BorderRadius.circular(12),
                        child: Container(
                          padding: const EdgeInsets.symmetric(
                            horizontal: 16,
                            vertical: 10,
                          ),
                          decoration: BoxDecoration(
                            color: isSelected
                                ? const Color(0xFF0F172A)
                                : const Color(0xFFF8FAFC),
                            border: Border.all(
                              color: isSelected
                                  ? const Color(0xFF0F172A)
                                  : const Color(0xFFE2E8F0),
                            ),
                            borderRadius: BorderRadius.circular(12),
                          ),
                          child: Column(
                            children: [
                              Text(
                                _formatDate(date),
                                style: TextStyle(
                                  fontSize: 12,
                                  fontWeight: FontWeight.bold,
                                  color: isSelected
                                      ? Colors.white
                                      : const Color(0xFF0F172A),
                                ),
                              ),
                              const SizedBox(height: 2),
                              Text(
                                '${date.day}',
                                style: TextStyle(
                                  fontSize: 16,
                                  fontWeight: FontWeight.bold,
                                  color: isSelected
                                      ? Colors.white
                                      : Colors.grey.shade700,
                                ),
                              ),
                            ],
                          ),
                        ),
                      ),
                    );
                  }),
                ),
              ),

              const SizedBox(height: 16),

              // Select Time Slot
              const Text(
                'Select Time Slot',
                style: TextStyle(
                  fontSize: 14,
                  fontWeight: FontWeight.bold,
                  color: Color(0xFF0F172A),
                ),
              ),
              const SizedBox(height: 10),
              Wrap(
                spacing: 8,
                runSpacing: 8,
                children: _slots.map((slot) {
                  final isSelected = _selectedSlot == slot;
                  return ChoiceChip(
                    label: Text(
                      slot,
                      style: TextStyle(
                        fontSize: 12,
                        fontWeight: isSelected
                            ? FontWeight.w600
                            : FontWeight.normal,
                      ),
                    ),
                    selected: isSelected,
                    selectedColor: colors.primary.withValues(alpha: 0.15),
                    labelStyle: TextStyle(
                      color: isSelected
                          ? colors.primary
                          : const Color(0xFF334155),
                    ),
                    onSelected: (selected) {
                      if (selected) setState(() => _selectedSlot = slot);
                    },
                  );
                }).toList(),
              ),

              const SizedBox(height: 16),

              // Address input
              const Text(
                'Service Address',
                style: TextStyle(
                  fontSize: 14,
                  fontWeight: FontWeight.bold,
                  color: Color(0xFF0F172A),
                ),
              ),
              const SizedBox(height: 8),
              TextFormField(
                controller: _addressController,
                style: const TextStyle(fontSize: 13),
                decoration: InputDecoration(
                  prefixIcon: const Icon(Icons.location_on_outlined, size: 20),
                  hintText: 'Enter your flat/house no., street & landmark',
                  contentPadding: const EdgeInsets.symmetric(
                    horizontal: 14,
                    vertical: 12,
                  ),
                  filled: true,
                  fillColor: const Color(0xFFF8FAFC),
                  border: OutlineInputBorder(
                    borderRadius: BorderRadius.circular(12),
                    borderSide: const BorderSide(color: Color(0xFFE2E8F0)),
                  ),
                ),
              ),

              const SizedBox(height: 20),

              // Price Summary Card
              Container(
                padding: const EdgeInsets.all(14),
                decoration: BoxDecoration(
                  color: const Color(0xFFF8FAFC),
                  borderRadius: BorderRadius.circular(14),
                  border: Border.all(color: const Color(0xFFE2E8F0)),
                ),
                child: Column(
                  children: [
                    Row(
                      mainAxisAlignment: MainAxisAlignment.spaceBetween,
                      children: [
                        const Text(
                          'Price Estimate',
                          style: TextStyle(
                            color: Color(0xFF64748B),
                            fontSize: 13,
                          ),
                        ),
                        Text(
                          'From ₹${item.startingPrice}',
                          style: const TextStyle(
                            fontWeight: FontWeight.bold,
                            fontSize: 14,
                          ),
                        ),
                      ],
                    ),
                    const SizedBox(height: 6),
                    const Row(
                      children: [
                        Expanded(
                          child: Text(
                            'Safety & Convenience Fee',
                            style: TextStyle(
                              color: Color(0xFF64748B),
                              fontSize: 12,
                            ),
                          ),
                        ),
                        Text(
                          '₹49',
                          style: TextStyle(
                            fontSize: 12,
                            fontWeight: FontWeight.w500,
                          ),
                        ),
                      ],
                    ),
                    const Divider(height: 16),
                    Row(
                      mainAxisAlignment: MainAxisAlignment.spaceBetween,
                      children: [
                        const Text(
                          'Estimated total',
                          style: TextStyle(
                            fontWeight: FontWeight.bold,
                            fontSize: 14,
                            color: Color(0xFF0F172A),
                          ),
                        ),
                        Text(
                          '₹${item.startingPrice + 49}',
                          style: const TextStyle(
                            fontWeight: FontWeight.bold,
                            fontSize: 17,
                            color: Color(0xFF10B981),
                          ),
                        ),
                      ],
                    ),
                    const SizedBox(height: 8),
                    const Text(
                      'Final scope and price are confirmed with you before work begins.',
                      style: TextStyle(fontSize: 11, color: Color(0xFF64748B)),
                    ),
                  ],
                ),
              ),

              const SizedBox(height: 20),

              Container(
                padding: const EdgeInsets.all(12),
                decoration: BoxDecoration(
                  color: const Color(0xFFECFDF5),
                  borderRadius: BorderRadius.circular(10),
                ),
                child: const Row(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Icon(
                      Icons.info_outline,
                      size: 18,
                      color: Color(0xFF047857),
                    ),
                    SizedBox(width: 8),
                    Expanded(
                      child: Text(
                        'This is a booking request. We will confirm availability before assigning a professional.',
                        style: TextStyle(
                          fontSize: 12,
                          color: Color(0xFF065F46),
                        ),
                      ),
                    ),
                  ],
                ),
              ),

              const SizedBox(height: 14),

              FilledButton(
                onPressed: _isBooking ? null : _submitBooking,
                style: FilledButton.styleFrom(
                  backgroundColor: const Color(0xFF0F172A),
                  minimumSize: const Size.fromHeight(52),
                  shape: RoundedRectangleBorder(
                    borderRadius: BorderRadius.circular(14),
                  ),
                ),
                child: _isBooking
                    ? const SizedBox(
                        height: 20,
                        width: 20,
                        child: CircularProgressIndicator(
                          strokeWidth: 2,
                          color: Colors.white,
                        ),
                      )
                    : const Text(
                        'Send booking request',
                        style: TextStyle(
                          fontSize: 15,
                          fontWeight: FontWeight.bold,
                        ),
                      ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
