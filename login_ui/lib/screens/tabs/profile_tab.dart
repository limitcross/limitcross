import 'package:firebase_auth/firebase_auth.dart';
import 'package:firebase_core/firebase_core.dart';
import 'package:flutter/material.dart';

import '../../services/auth_service.dart';
import '../../services/booking_service.dart';
import '../support_screen.dart';

class ProfileTab extends StatelessWidget {
  final VoidCallback onSignOut;

  const ProfileTab({super.key, required this.onSignOut});

  @override
  Widget build(BuildContext context) {
    User? user;
    try {
      if (Firebase.apps.isNotEmpty) {
        user = FirebaseAuth.instance.currentUser;
      }
    } catch (_) {}
    final displayName = user?.displayName ?? 'Limitcross Customer';
    final email = user?.email ?? 'guest.user@limitcrossfacility.com';

    return Scaffold(
      backgroundColor: const Color(0xFFF8FAFC),
      appBar: AppBar(
        backgroundColor: Colors.white,
        elevation: 0,
        title: const Text(
          'Account & Settings',
          style: TextStyle(fontWeight: FontWeight.w800, fontSize: 18, color: Color(0xFF0F172A)),
        ),
      ),
      body: ListView(
        padding: const EdgeInsets.all(16),
        children: [
          // Profile Card
          Container(
            padding: const EdgeInsets.all(16),
            decoration: BoxDecoration(
              color: Colors.white,
              borderRadius: BorderRadius.circular(18),
              border: Border.all(color: const Color(0xFFE2E8F0)),
            ),
            child: Row(
              children: [
                CircleAvatar(
                  radius: 28,
                  backgroundColor: const Color(0xFF4F46E5).withValues(alpha: 0.15),
                  child: Text(
                    displayName.isNotEmpty ? displayName[0].toUpperCase() : 'U',
                    style: const TextStyle(fontSize: 22, fontWeight: FontWeight.bold, color: Color(0xFF4F46E5)),
                  ),
                ),
                const SizedBox(width: 14),
                Expanded(
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Text(
                        displayName,
                        style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 16, color: Color(0xFF0F172A)),
                      ),
                      const SizedBox(height: 2),
                      Text(
                        email,
                        style: const TextStyle(fontSize: 12, color: Color(0xFF64748B)),
                      ),
                      const SizedBox(height: 4),
                      Container(
                        padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 2),
                        decoration: BoxDecoration(
                          color: const Color(0xFFFEF3C7),
                          borderRadius: BorderRadius.circular(6),
                        ),
                        child: const Text(
                          '⭐ Limitcross Plus Member (10% Off)',
                          style: TextStyle(fontSize: 10, fontWeight: FontWeight.bold, color: Color(0xFFB45309)),
                        ),
                      ),
                    ],
                  ),
                ),
              ],
            ),
          ),

          const SizedBox(height: 16),

          // City & Location
          Container(
            padding: const EdgeInsets.all(16),
            decoration: BoxDecoration(
              color: Colors.white,
              borderRadius: BorderRadius.circular(16),
              border: Border.all(color: const Color(0xFFE2E8F0)),
            ),
            child: Row(
              children: [
                const Icon(Icons.location_city_rounded, color: Color(0xFF4F46E5)),
                const SizedBox(width: 12),
                Expanded(
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      const Text('Operating City', style: TextStyle(fontSize: 12, color: Color(0xFF64748B))),
                      Text(
                        '${BookingService().currentCity} (51+ Cities Live)',
                        style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 14, color: Color(0xFF0F172A)),
                      ),
                    ],
                  ),
                ),
                TextButton(
                  onPressed: () {
                    showModalBottomSheet<void>(
                      context: context,
                      builder: (ctx) => ListView.builder(
                        itemCount: BookingService.availableCities.length,
                        itemBuilder: (_, i) => ListTile(
                          title: Text(BookingService.availableCities[i]),
                          onTap: () {
                            BookingService().setCity(BookingService.availableCities[i]);
                            Navigator.of(ctx).pop();
                          },
                        ),
                      ),
                    );
                  },
                  child: const Text('Change'),
                ),
              ],
            ),
          ),

          const SizedBox(height: 16),

          // Safety & Quality Assurance
          Container(
            padding: const EdgeInsets.all(16),
            decoration: BoxDecoration(
              gradient: const LinearGradient(
                colors: [Color(0xFF065F46), Color(0xFF047857)],
              ),
              borderRadius: BorderRadius.circular(16),
            ),
            child: const Row(
              children: [
                Icon(Icons.verified_rounded, color: Colors.white, size: 32),
                SizedBox(width: 14),
                Expanded(
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Text(
                        'Limitcross Facility Safety Standard',
                        style: TextStyle(color: Colors.white, fontWeight: FontWeight.bold, fontSize: 14),
                      ),
                      SizedBox(height: 2),
                      Text(
                        '• 59K+ background verified pros\n• Up to ₹10,000 damage cover\n• 30-day service warranty',
                        style: TextStyle(color: Color(0xFFD1FAE5), fontSize: 11),
                      ),
                    ],
                  ),
                ),
              ],
            ),
          ),

          const SizedBox(height: 24),

          Card(
            margin: EdgeInsets.zero,
            elevation: 0,
            shape: RoundedRectangleBorder(
              borderRadius: BorderRadius.circular(16),
              side: const BorderSide(color: Color(0xFFE2E8F0)),
            ),
            child: Column(
              children: [
                ListTile(
                  leading: const Icon(Icons.notifications_none_rounded),
                  title: const Text('Notifications'),
                  subtitle: const Text('Booking updates and offers'),
                  trailing: const Icon(Icons.chevron_right_rounded),
                  onTap: () => ScaffoldMessenger.of(context).showSnackBar(
                    const SnackBar(content: Text('Notifications will connect to Spring Boot APIs')),
                  ),
                ),
                const Divider(height: 1),
                ListTile(
                  leading: const Icon(Icons.help_outline_rounded),
                  title: const Text('Help & support'),
                  subtitle: const Text('Find answers or contact our team'),
                  trailing: const Icon(Icons.chevron_right_rounded),
                  onTap: () => Navigator.of(context).push(
                    MaterialPageRoute<void>(builder: (_) => const SupportScreen()),
                  ),
                ),
              ],
            ),
          ),

          const SizedBox(height: 16),

          // Sign Out Button
          FilledButton.icon(
            onPressed: () async {
              await AuthService().signOut();
              onSignOut();
            },
            icon: const Icon(Icons.logout_rounded, size: 18),
            label: const Text('Sign out from Account'),
            style: FilledButton.styleFrom(
              backgroundColor: const Color(0xFFEF4444),
              foregroundColor: Colors.white,
              minimumSize: const Size.fromHeight(48),
              shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
            ),
          ),

          const SizedBox(height: 16),
          const Center(
            child: Text(
              'Limitcross Facility • v1.0.0 (Flutter & Firebase)',
              style: TextStyle(fontSize: 11, color: Color(0xFF94A3B8)),
            ),
          ),
        ],
      ),
    );
  }
}
