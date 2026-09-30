// File generated for FlutterFire integration.
// ignore_for_file: type=lint
import 'package:firebase_core/firebase_core.dart' show FirebaseOptions;
import 'package:flutter/foundation.dart'
    show defaultTargetPlatform, kIsWeb, TargetPlatform;

/// Default [FirebaseOptions] for use with your Firebase apps.
///
/// Example:
/// ```dart
/// import 'firebase_options.dart';
/// // ...
/// await Firebase.initializeApp(
///   options: DefaultFirebaseOptions.currentPlatform,
/// );
/// ```
class DefaultFirebaseOptions {
  static FirebaseOptions get currentPlatform {
    if (kIsWeb) {
      return web;
    }
    switch (defaultTargetPlatform) {
      case TargetPlatform.android:
        return android;
      case TargetPlatform.iOS:
        return ios;
      case TargetPlatform.macOS:
        return macos;
      case TargetPlatform.windows:
        return windows;
      case TargetPlatform.linux:
        throw UnsupportedError(
          'DefaultFirebaseOptions have not been configured for linux.',
        );
      default:
        throw UnsupportedError(
          'DefaultFirebaseOptions are not supported for this platform.',
        );
    }
  }

  /// Helper to check if credentials have been replaced with real project keys.
  static bool get isConfigured {
    try {
      final options = currentPlatform;
      return options.apiKey.isNotEmpty &&
          !options.apiKey.contains('YourApiKeyHere') &&
          options.appId.isNotEmpty &&
          !options.appId.contains('abcdef');
    } on UnsupportedError {
      return false;
    }
  }

  // Project: parking-cb3c5 (npavprashant@gmail.com)

  static const FirebaseOptions web = FirebaseOptions(
    apiKey: 'AIzaSyBe2Mn15sZL4EScPWT3bxZ-5J_tRW2FsyY',
    appId: '1:171365152705:web:afd52a0bee4658a229a140',
    messagingSenderId: '171365152705',
    projectId: 'login-ui-test-2026',
    authDomain: 'login-ui-test-2026.firebaseapp.com',
    storageBucket: 'login-ui-test-2026.firebasestorage.app',
  );

  static const FirebaseOptions android = FirebaseOptions(
    apiKey: 'AIzaSyCR-s5XI5V9HK8H3QcDXyhzLFOLoC5s878',
    appId: '1:171365152705:android:da2a5bf78618602929a140',
    messagingSenderId: '171365152705',
    projectId: 'login-ui-test-2026',
    storageBucket: 'login-ui-test-2026.firebasestorage.app',
  );
  static const FirebaseOptions ios = FirebaseOptions(
    apiKey: 'AIzaSyYourApiKeyHereForiOS',
    appId: '1:1234567890:ios:abcdef123456',
    messagingSenderId: '1234567890',
    projectId: 'parking-cb3c5',
    storageBucket: 'parking-cb3c5.firebasestorage.app',
    iosBundleId: 'com.loginui.loginUi',
  );

  static const FirebaseOptions macos = FirebaseOptions(
    apiKey: 'AIzaSyYourApiKeyHereForMac',
    appId: '1:1234567890:ios:abcdef123456',
    messagingSenderId: '1234567890',
    projectId: 'parking-cb3c5',
    storageBucket: 'parking-cb3c5.firebasestorage.app',
    iosBundleId: 'com.loginui.loginUi',
  );

  static const FirebaseOptions windows = FirebaseOptions(
    apiKey: 'AIzaSyYourApiKeyHereForWindows',
    appId: '1:1234567890:web:abcdef123456',
    messagingSenderId: '1234567890',
    projectId: 'parking-cb3c5',
    storageBucket: 'parking-cb3c5.firebasestorage.app',
  );
}
