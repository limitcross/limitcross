import 'package:flutter/material.dart';

import '../widgets/mobile_frame.dart';
import 'tabs/bookings_tab.dart';
import 'tabs/profile_tab.dart';
import 'tabs/services_tab.dart';

class LimitcrossFacilityMainScreen extends StatefulWidget {
  final VoidCallback onSignOut;

  const LimitcrossFacilityMainScreen({super.key, required this.onSignOut});

  @override
  State<LimitcrossFacilityMainScreen> createState() =>
      _LimitcrossFacilityMainScreenState();
}

class _LimitcrossFacilityMainScreenState
    extends State<LimitcrossFacilityMainScreen> {
  int _currentIndex = 0;

  void _navigateToTab(int index) {
    setState(() => _currentIndex = index);
  }

  @override
  Widget build(BuildContext context) {
    final tabs = [
      ServicesTab(
        onNavigateToBookings: () => _navigateToTab(1),
      ),
      BookingsTab(
        onExploreServices: () => _navigateToTab(0),
      ),
      ProfileTab(
        onSignOut: widget.onSignOut,
      ),
    ];

    return MobileFrame(
      child: Scaffold(
        body: IndexedStack(
          index: _currentIndex,
          children: tabs,
        ),
        bottomNavigationBar: BottomNavigationBar(
          currentIndex: _currentIndex,
          onTap: (index) => setState(() => _currentIndex = index),
          type: BottomNavigationBarType.fixed,
          backgroundColor: Colors.white,
          selectedItemColor: Theme.of(context).colorScheme.primary,
          unselectedItemColor: Theme.of(context).colorScheme.outline,
          selectedLabelStyle: const TextStyle(fontWeight: FontWeight.bold, fontSize: 11),
          unselectedLabelStyle: const TextStyle(fontWeight: FontWeight.w500, fontSize: 11),
          elevation: 0,
          items: const [
            BottomNavigationBarItem(
              icon: Icon(Icons.home_repair_service_outlined),
              activeIcon: Icon(Icons.home_repair_service),
              label: 'Services',
            ),
            BottomNavigationBarItem(
              icon: Icon(Icons.calendar_today_outlined),
              activeIcon: Icon(Icons.calendar_today),
              label: 'Bookings',
            ),
            BottomNavigationBarItem(
              icon: Icon(Icons.person_outline),
              activeIcon: Icon(Icons.person),
              label: 'Account',
            ),
          ],
        ),
      ),
    );
  }
}