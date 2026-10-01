import 'package:firebase_auth/firebase_auth.dart';
import 'package:firebase_core/firebase_core.dart';
import 'package:flutter/material.dart';

import 'firebase_options.dart';
import 'screens/home_screen.dart';
import 'screens/login_screen.dart';
import 'services/auth_service.dart';
import 'theme/app_theme.dart';

void main() async {
  WidgetsFlutterBinding.ensureInitialized();

  bool firebaseInitialized = false;
  String? firebaseError;

  try {
    if (DefaultFirebaseOptions.isConfigured) {
      await Firebase.initializeApp(
        options: DefaultFirebaseOptions.currentPlatform,
      );
      firebaseInitialized = true;
    } else {
      // Attempt default platform initialization (e.g. when google-services.json is present)
      try {
        await Firebase.initializeApp();
        firebaseInitialized = true;
      } catch (e) {
        firebaseError = 'Firebase credentials need to be configured in lib/firebase_options.dart';
      }
    }
  } catch (e) {
    firebaseError = e.toString();
  }

  runApp(LimitcrossFacilityApp(
    firebaseInitialized: firebaseInitialized,
    firebaseError: firebaseError,
  ));
}

class LimitcrossFacilityApp extends StatelessWidget {
  const LimitcrossFacilityApp({
    super.key,
    this.firebaseInitialized = false,
    this.firebaseError,
  });

  final bool firebaseInitialized;
  final String? firebaseError;

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Limitcross Facility',
      debugShowCheckedModeBanner: false,
      theme: AppTheme.light,
      home: AuthGate(
        firebaseInitialized: firebaseInitialized,
        firebaseError: firebaseError,
      ),
    );
  }
}

class AuthGate extends StatelessWidget {
  const AuthGate({
    super.key,
    required this.firebaseInitialized,
    this.firebaseError,
  });

  final bool firebaseInitialized;
  final String? firebaseError;

  @override
  Widget build(BuildContext context) {
    if (!firebaseInitialized) {
      return Stack(
        children: [
          const LoginScreen(),
          if (firebaseError != null)
            Positioned(
              top: 0,
              left: 0,
              right: 0,
              child: SafeArea(
                child: Material(
                  color: const Color(0xFFF59E0B),
                  child: Padding(
                    padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 8),
                    child: Row(
                      children: [
                        const Icon(Icons.info_outline, color: Colors.white, size: 18),
                        const SizedBox(width: 8),
                        Expanded(
                          child: Text(
                            firebaseError!,
                            style: const TextStyle(
                              color: Colors.white,
                              fontSize: 12,
                              fontWeight: FontWeight.w500,
                            ),
                          ),
                        ),
                      ],
                    ),
                  ),
                ),
              ),
            ),
        ],
      );
    }

    return StreamBuilder<User?>(
      stream: AuthService().authStateChanges,
      builder: (context, snapshot) {
        if (snapshot.connectionState == ConnectionState.waiting) {
          return const Scaffold(
            body: Center(child: CircularProgressIndicator()),
          );
        }
        if (snapshot.hasData && snapshot.data != null) {
          return const HomeScreen();
        }
        return const LoginScreen();
      },
    );
  }
}
