import 'package:flutter/material.dart';

import '../models/service_item.dart';
import '../widgets/booking_sheet.dart';
import '../widgets/mobile_frame.dart';

class ServiceDetailScreen extends StatelessWidget {
  const ServiceDetailScreen({
    super.key,
    required this.service,
    this.onBookingCreated,
  });

  final ServiceItem service;
  final VoidCallback? onBookingCreated;

  Future<void> _book(BuildContext context) async {
    final booked = await BookingSheet.show(context, service);
    if (booked == true && context.mounted) {
      onBookingCreated?.call();
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(
          content: Text('${service.title} request sent'),
          action: SnackBarAction(
            label: 'View bookings',
            onPressed: () => Navigator.of(context).pop(),
          ),
        ),
      );
    }
  }

  @override
  Widget build(BuildContext context) {
    final colors = Theme.of(context).colorScheme;

    return MobileFrame(
      child: Scaffold(
        backgroundColor: const Color(0xFFF8FAFC),
        appBar: AppBar(
          title: const Text('Service details'),
          actions: [
            IconButton(
              tooltip: 'Share service',
              onPressed: () {
                ScaffoldMessenger.of(context).showSnackBar(
                  const SnackBar(content: Text('Service link ready to share')),
                );
              },
              icon: const Icon(Icons.ios_share_outlined),
            ),
            const SizedBox(width: 4),
          ],
        ),
        body: ListView(
          padding: const EdgeInsets.fromLTRB(16, 8, 16, 100),
          children: [
            Container(
              padding: const EdgeInsets.all(20),
              decoration: BoxDecoration(
                color: service.accentColor,
                borderRadius: BorderRadius.circular(24),
              ),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Row(
                    children: [
                      Container(
                        width: 64,
                        height: 64,
                        decoration: BoxDecoration(
                          color: Colors.white.withValues(alpha: 0.2),
                          borderRadius: BorderRadius.circular(18),
                        ),
                        alignment: Alignment.center,
                        child: Text(
                          service.emoji,
                          style: const TextStyle(fontSize: 32),
                        ),
                      ),
                      const Spacer(),
                      _RatingPill(rating: service.rating),
                    ],
                  ),
                  const SizedBox(height: 22),
                  Text(
                    service.title,
                    style: const TextStyle(
                      color: Colors.white,
                      fontSize: 26,
                      fontWeight: FontWeight.w800,
                    ),
                  ),
                  const SizedBox(height: 8),
                  Text(
                    service.description,
                    style: TextStyle(
                      color: Colors.white.withValues(alpha: 0.86),
                      fontSize: 14,
                      height: 1.4,
                    ),
                  ),
                ],
              ),
            ),
            const SizedBox(height: 16),
            Row(
              children: [
                _InfoTile(
                  icon: Icons.schedule_outlined,
                  label: service.duration,
                ),
                const SizedBox(width: 10),
                _InfoTile(
                  icon: Icons.payments_outlined,
                  label: 'From ₹${service.startingPrice}',
                ),
                const SizedBox(width: 10),
                _InfoTile(
                  icon: Icons.reviews_outlined,
                  label: '${service.reviewsCount} reviews',
                ),
              ],
            ),
            const SizedBox(height: 24),
            const _SectionTitle(title: 'What is included'),
            const SizedBox(height: 10),
            ...service.highlights.map(
              (highlight) => Padding(
                padding: const EdgeInsets.only(bottom: 10),
                child: Row(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Icon(
                      Icons.check_circle_rounded,
                      color: colors.primary,
                      size: 20,
                    ),
                    const SizedBox(width: 10),
                    Expanded(
                      child: Text(
                        highlight,
                        style: const TextStyle(
                          fontSize: 14,
                          color: Color(0xFF334155),
                        ),
                      ),
                    ),
                  ],
                ),
              ),
            ),
            const SizedBox(height: 14),
            const _SectionTitle(title: 'How it works'),
            const SizedBox(height: 10),
            const _StepRow(
              number: '01',
              title: 'Choose a service window',
              description: 'Select a date, time window and service address.',
            ),
            const _StepRow(
              number: '02',
              title: 'Send your booking request',
              description: 'Your requested estimate and schedule are saved to your account.',
            ),
            const _StepRow(
              number: '03',
              title: 'Follow the request',
              description: 'View, reschedule or cancel eligible requests in My Bookings.',
            ),
            const SizedBox(height: 14),
            const _SectionTitle(title: 'Frequently asked'),
            const SizedBox(height: 8),
            const _FaqTile(
              question: 'Can I reschedule my booking?',
              answer: 'You can change the date and time while a request is awaiting confirmation or confirmed.',
            ),
            const _FaqTile(
              question: 'Where can I see appointment updates?',
              answer: 'Open My Bookings to see the latest request status and scheduled service details.',
            ),
            const _FaqTile(
              question: 'When will I know the final price?',
              answer: 'The amount shown is a starting estimate. Confirm the final scope and price before work begins.',
            ),
          ],
        ),
        bottomNavigationBar: SafeArea(
          minimum: const EdgeInsets.fromLTRB(16, 10, 16, 12),
          child: FilledButton(
            onPressed: () => _book(context),
            child: Text('Request service · from ₹${service.startingPrice}'),
          ),
        ),
      ),
    );
  }
}

class _RatingPill extends StatelessWidget {
  const _RatingPill({required this.rating});

  final double rating;

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 7),
      decoration: BoxDecoration(
        color: Colors.white.withValues(alpha: 0.18),
        borderRadius: BorderRadius.circular(12),
      ),
      child: Row(
        mainAxisSize: MainAxisSize.min,
        children: [
          const Icon(Icons.star_rounded, color: Colors.white, size: 17),
          const SizedBox(width: 4),
          Text(
            '$rating',
            style: const TextStyle(
              color: Colors.white,
              fontWeight: FontWeight.w700,
            ),
          ),
        ],
      ),
    );
  }
}

class _InfoTile extends StatelessWidget {
  const _InfoTile({required this.icon, required this.label});

  final IconData icon;
  final String label;

  @override
  Widget build(BuildContext context) {
    return Expanded(
      child: Container(
        padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 12),
        decoration: BoxDecoration(
          color: Colors.white,
          borderRadius: BorderRadius.circular(14),
          border: Border.all(color: const Color(0xFFE2E8F0)),
        ),
        child: Column(
          children: [
            Icon(icon, size: 20, color: Theme.of(context).colorScheme.primary),
            const SizedBox(height: 6),
            Text(
              label,
              maxLines: 2,
              textAlign: TextAlign.center,
              overflow: TextOverflow.ellipsis,
              style: const TextStyle(
                fontSize: 11,
                color: Color(0xFF475569),
                fontWeight: FontWeight.w600,
              ),
            ),
          ],
        ),
      ),
    );
  }
}

class _SectionTitle extends StatelessWidget {
  const _SectionTitle({required this.title});

  final String title;

  @override
  Widget build(BuildContext context) {
    return Text(
      title,
      style: Theme.of(context).textTheme.titleMedium
          ?.copyWith(fontWeight: FontWeight.w800),
    );
  }
}

class _StepRow extends StatelessWidget {
  const _StepRow({
    required this.number,
    required this.title,
    required this.description,
  });

  final String number;
  final String title;
  final String description;

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.only(bottom: 12),
      child: Row(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Container(
            width: 34,
            height: 34,
            alignment: Alignment.center,
            decoration: BoxDecoration(
              color: Theme.of(context).colorScheme.primaryContainer,
              borderRadius: BorderRadius.circular(10),
            ),
            child: Text(
              number,
              style: TextStyle(
                fontSize: 11,
                fontWeight: FontWeight.w800,
                color: Theme.of(context).colorScheme.primary,
              ),
            ),
          ),
          const SizedBox(width: 12),
          Expanded(
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Text(
                  title,
                  style: const TextStyle(
                    fontWeight: FontWeight.w700,
                    color: Color(0xFF0F172A),
                  ),
                ),
                const SizedBox(height: 2),
                Text(
                  description,
                  style: const TextStyle(
                    fontSize: 12,
                    color: Color(0xFF64748B),
                  ),
                ),
              ],
            ),
          ),
        ],
      ),
    );
  }
}

class _FaqTile extends StatelessWidget {
  const _FaqTile({required this.question, required this.answer});

  final String question;
  final String answer;

  @override
  Widget build(BuildContext context) {
    return Card(
      margin: const EdgeInsets.only(bottom: 8),
      elevation: 0,
      shape: RoundedRectangleBorder(
        borderRadius: BorderRadius.circular(14),
        side: const BorderSide(color: Color(0xFFE2E8F0)),
      ),
      child: ExpansionTile(
        tilePadding: const EdgeInsets.symmetric(horizontal: 14),
        childrenPadding: const EdgeInsets.fromLTRB(14, 0, 14, 14),
        title: Text(
          question,
          style: const TextStyle(fontSize: 13, fontWeight: FontWeight.w700),
        ),
        children: [
          Align(
            alignment: Alignment.centerLeft,
            child: Text(
              answer,
              style: const TextStyle(
                fontSize: 12,
                color: Color(0xFF64748B),
                height: 1.4,
              ),
            ),
          ),
        ],
      ),
    );
  }
}
