import 'package:flutter/material.dart';

import '../services/backend_auth_service.dart';
import 'limitcross_facility_main_screen.dart';

class HomeScreen extends StatelessWidget {
  const HomeScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return LimitcrossFacilityMainScreen(
      onSignOut: () async {
        await BackendAuthService.instance.signOut();
      },
    );
  }
}
