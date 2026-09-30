import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:limitcross_facility/main.dart';
import 'package:limitcross_facility/models/booking_item.dart';
import 'package:limitcross_facility/models/service_item.dart';
import 'package:limitcross_facility/screens/limitcross_facility_main_screen.dart';
import 'package:limitcross_facility/screens/service_detail_screen.dart';
import 'package:limitcross_facility/screens/support_screen.dart';
import 'package:limitcross_facility/screens/tabs/services_tab.dart';
import 'package:limitcross_facility/services/booking_service.dart';

void main() {
  Future<void> pumpApp(WidgetTester tester) async {
    tester.view.physicalSize = const Size(390, 844);
    tester.view.devicePixelRatio = 1.0;
    addTearDown(tester.view.resetPhysicalSize);
    addTearDown(tester.view.resetDevicePixelRatio);
    await tester.pumpWidget(const LimitcrossFacilityApp());
  }

  testWidgets('Login screen shows authenticated sign-in controls', (
    WidgetTester tester,
  ) async {
    await pumpApp(tester);

    expect(find.text('Welcome back'), findsOneWidget);
    expect(find.text('Log in'), findsOneWidget);
    expect(find.text('Continue with Google'), findsOneWidget);
    expect(find.text('Register'), findsOneWidget);
    expect(
      find.text('Explore Limitcross Facility Services (Guest Mode)'),
      findsNothing,
    );
  });

  testWidgets('Register screen opens from login', (WidgetTester tester) async {
    await pumpApp(tester);

    await tester.tap(find.text('Register'));
    await tester.pumpAndSettle();

    expect(find.text('Create account'), findsWidgets);
    expect(find.text('Join us'), findsOneWidget);
  });

  test(
    'ServiceItem contains all 32+ Limitcross Facility services requested',
    () {
      final services = ServiceItem.allServices;
      expect(services.length, greaterThanOrEqualTo(30));

      // Home Maintenance
      expect(services.any((s) => s.title == 'AC Service & Repair'), isTrue);
      expect(services.any((s) => s.title == 'Plumbing'), isTrue);
      expect(services.any((s) => s.title == 'Electrician'), isTrue);
      expect(services.any((s) => s.title == 'Carpentry'), isTrue);
      expect(services.any((s) => s.title == 'Painting & Wall Décor'), isTrue);
      expect(services.any((s) => s.title == 'Appliance Repair'), isTrue);

      // Cleaning
      expect(services.any((s) => s.title == 'Home Deep Cleaning'), isTrue);
      expect(services.any((s) => s.title == 'Bathroom Cleaning'), isTrue);
      expect(services.any((s) => s.title == 'Sofa & Carpet Clean'), isTrue);
      expect(services.any((s) => s.title == 'Pest Control'), isTrue);
      expect(services.any((s) => s.title == 'Water Tank Cleaning'), isTrue);
      expect(services.any((s) => s.title == 'Car Cleaning'), isTrue);

      // Beauty & Wellness (Women)
      expect(services.any((s) => s.title == 'Salon at Home'), isTrue);
      expect(services.any((s) => s.title == 'Massage Therapy'), isTrue);
      expect(services.any((s) => s.title == 'Skincare & Facial'), isTrue);
      expect(services.any((s) => s.title == 'Haircut & Style'), isTrue);
      expect(services.any((s) => s.title == 'Mehendi'), isTrue);
      expect(services.any((s) => s.title == 'Pedicure & Manicure'), isTrue);

      // Men's Grooming
      expect(services.any((s) => s.title == 'Haircut at Home'), isTrue);
      expect(services.any((s) => s.title == 'Shave & Beard Grooming'), isTrue);
      expect(services.any((s) => s.title == "Men's Massage"), isTrue);
      expect(services.any((s) => s.title == "Men's Facial"), isTrue);

      // On-Demand Home Help
      expect(services.any((s) => s.title == 'Cook at Home'), isTrue);
      expect(services.any((s) => s.title == 'Babysitter / Nanny'), isTrue);
      expect(services.any((s) => s.title == 'Maid / Home Help'), isTrue);
      expect(services.any((s) => s.title == 'Elder Care'), isTrue);

      // Health at Home
      expect(services.any((s) => s.title == 'Physiotherapy'), isTrue);
      expect(services.any((s) => s.title == 'Personal Trainer'), isTrue);
      expect(services.any((s) => s.title == 'Doctor Visit'), isTrue);
      expect(services.any((s) => s.title == 'Lab Tests at Home'), isTrue);

      // Native Products
      expect(services.any((s) => s.title == 'Water Purifier (RO)'), isTrue);
      expect(services.any((s) => s.title == 'Smart Door Lock'), isTrue);
    },
  );

  testWidgets(
    'LimitcrossFacilityMainScreen renders services and allows tab switching',
    (WidgetTester tester) async {
      tester.view.physicalSize = const Size(390, 844);
      tester.view.devicePixelRatio = 1.0;
      addTearDown(tester.view.resetPhysicalSize);
      addTearDown(tester.view.resetDevicePixelRatio);

      await tester.pumpWidget(
        MaterialApp(home: LimitcrossFacilityMainScreen(onSignOut: () {})),
      );
      await tester.pumpAndSettle();

      // Verify header is visible
      expect(find.text('Limitcross Facility'), findsOneWidget);

      // Switch to Bookings Tab
      await tester.tap(find.byIcon(Icons.calendar_today_outlined));
      await tester.pumpAndSettle();

      expect(find.text('My Bookings'), findsOneWidget);

      // Switch to Account Tab
      await tester.tap(find.byIcon(Icons.person_outline));
      await tester.pumpAndSettle();

      expect(find.text('Account & Settings'), findsOneWidget);
      expect(find.text('Sign out from Account'), findsOneWidget);
    },
  );

  testWidgets('Service detail screen renders booking information', (
    WidgetTester tester,
  ) async {
    await tester.pumpWidget(
      MaterialApp(
        home: ServiceDetailScreen(service: ServiceItem.allServices.first),
      ),
    );

    expect(find.text('Service details'), findsOneWidget);
    expect(find.text('What is included'), findsOneWidget);
    expect(find.textContaining('Request service'), findsOneWidget);

    await tester.drag(find.byType(Scrollable).first, const Offset(0, -500));
    await tester.pump();
    expect(find.text('How it works'), findsOneWidget);
  });

  testWidgets('Services carousel has four swipeable category slides', (
    WidgetTester tester,
  ) async {
    tester.view.physicalSize = const Size(390, 844);
    tester.view.devicePixelRatio = 1;
    addTearDown(tester.view.resetPhysicalSize);
    addTearDown(tester.view.resetDevicePixelRatio);
    await tester.pumpWidget(
      MaterialApp(
        home: ServicesTab(onNavigateToBookings: () {}),
      ),
    );
    await tester.pumpAndSettle();

    expect(find.text('Repairs, right at home.'), findsOneWidget);
    expect(find.bySemanticsLabel('Show slide 4 of 4'), findsOneWidget);

    await tester.drag(find.byType(PageView).first, const Offset(-260, 0));
    await tester.pumpAndSettle();
    expect(find.text('A cleaner start.'), findsOneWidget);

    await tester.tap(
      find.widgetWithText(FilledButton, 'Explore services').at(1),
    );
    await tester.pumpAndSettle();
    expect(find.text('Cleaning'), findsWidgets);
  });

  testWidgets('Service browse-to-booking flow guards unauthenticated requests', (WidgetTester tester) async {
    tester.view.physicalSize = const Size(390, 844);
    tester.view.devicePixelRatio = 1;
    addTearDown(tester.view.resetPhysicalSize);
    addTearDown(tester.view.resetDevicePixelRatio);
    await tester.pumpWidget(MaterialApp(
      home: ServicesTab(onNavigateToBookings: () {}),
    ));
    await tester.pumpAndSettle();

    await tester.ensureVisible(find.text('Details').first);
    await tester.tap(find.text('Details').first);
    await tester.pumpAndSettle();
    expect(find.text('Service details'), findsOneWidget);

    await tester.tap(find.textContaining('Request service').first);
    await tester.pumpAndSettle();
    expect(find.text('Select Date'), findsOneWidget);
    expect(find.text('Service Address'), findsOneWidget);

    await tester.ensureVisible(find.text('Send booking request'));
    await tester.pumpAndSettle();
    await tester.tap(find.text('Send booking request'));
    await tester.pumpAndSettle();

    expect(
      find.text('Please sign in before booking a service.'),
      findsOneWidget,
    );
  });

  test('New bookings remain requests until the service confirms them', () {
    final booking = BookingItem(
      id: 'booking-test',
      serviceId: 'hm-ac',
      serviceTitle: 'AC Service & Repair',
      emoji: '❄️',
      price: 400,
      date: DateTime.now().add(const Duration(days: 2)),
      timeSlot: '10:00 AM - 12:00 PM',
      address: 'Test address, Delhi',
    );

    expect(booking.status, BookingStatus.requested);
    expect(booking.status.label, 'Request sent');
    expect(booking.assignedProName, 'Not assigned yet');
    expect(booking.totalPrice, 449);
  });
  testWidgets('Support screen renders searchable help topics', (
    WidgetTester tester,
  ) async {
    await tester.pumpWidget(const MaterialApp(home: SupportScreen()));

    expect(find.text('How can we help?'), findsOneWidget);
    expect(find.text('Browse help topics'), findsOneWidget);
    expect(find.text('Manage a booking'), findsOneWidget);
  });

  test(
    'BookingService requires authentication before writing bookings',
    () async {
      final service = BookingService();
      final initialCount = service.bookings.length;

      expect(initialCount, equals(0));

      await expectLater(
        service.createBooking(
          service: ServiceItem.allServices.first,
          date: DateTime.now().add(const Duration(days: 2)),
          timeSlot: '01:00 PM - 03:00 PM',
          address: 'Test Apt 101',
        ),
        throwsA(isA<StateError>()),
      );
      expect(service.bookings, isEmpty);
    },
  );
}
