import 'package:flutter/material.dart';

import 'screens/email_verification_screen.dart';
import 'screens/home_screen.dart';
import 'screens/login_screen.dart';
import 'services/backend_auth_service.dart';
import 'theme/app_theme.dart';

void main() async {
  WidgetsFlutterBinding.ensureInitialized();
  await BackendAuthService.instance.initialize();
  runApp(const LimitcrossFacilityApp());
}

class LimitcrossFacilityApp extends StatelessWidget {
  const LimitcrossFacilityApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Limitcross Facility',
      debugShowCheckedModeBanner: false,
      theme: AppTheme.light,
      home: const AuthGate(),
    );
  }
}

class AuthGate extends StatelessWidget {
  const AuthGate({super.key});

  @override
  Widget build(BuildContext context) {
    return ValueListenableBuilder<BackendUser?>(
      valueListenable: BackendAuthService.instance.session,
      builder: (context, user, child) {
        if (user != null) {
          if (!user.emailVerified) {
            return EmailVerificationScreen(email: user.email);
          }
          return const HomeScreen();
        }
        return const LoginScreen();
      },
    );
  }
}
