import 'package:firebase_auth/firebase_auth.dart';
import 'package:firebase_core/firebase_core.dart';
import 'package:flutter/foundation.dart';
import 'package:google_sign_in/google_sign_in.dart';

class AuthService {
  FirebaseAuth? get _auth {
    try {
      if (Firebase.apps.isNotEmpty) {
        return FirebaseAuth.instance;
      }
    } catch (_) {}
    return null;
  }

  /// Stream of authentication and user profile changes.
  Stream<User?> get userChanges => _auth?.userChanges() ?? const Stream.empty();

  /// Currently logged in user, or null if none
  User? get currentUser => _auth?.currentUser;

  /// Sign in with email and password
  Future<UserCredential> signInWithEmailAndPassword({
    required String email,
    required String password,
  }) async {
    final auth = _auth;
    if (auth == null) {
      throw Exception('Firebase is not initialized. Please configure credentials in lib/firebase_options.dart');
    }
    return await auth.signInWithEmailAndPassword(
      email: email.trim(),
      password: password,
    );
  }

  /// Sign in with a Google account.
  Future<UserCredential> signInWithGoogle() async {
    final auth = _auth;
    if (auth == null) {
      throw Exception('Firebase is not initialized. Please configure credentials in lib/firebase_options.dart');
    }

    if (kIsWeb) {
      return auth.signInWithPopup(GoogleAuthProvider());
    }

    final googleSignIn = GoogleSignIn.instance;
    await googleSignIn.initialize();
    final googleUser = await googleSignIn.authenticate();
    final googleAuth = googleUser.authentication;
    final credential = GoogleAuthProvider.credential(
      idToken: googleAuth.idToken,
    );

    return auth.signInWithCredential(credential);
  }

  /// Register a new account with email and password, and optionally set display name
  Future<UserCredential> signUpWithEmailAndPassword({
    required String email,
    required String password,
    String? displayName,
  }) async {
    final auth = _auth;
    if (auth == null) {
      throw Exception('Firebase is not initialized. Please configure credentials in lib/firebase_options.dart');
    }
    final credential = await auth.createUserWithEmailAndPassword(
      email: email.trim(),
      password: password,
    );

    if (displayName != null && displayName.trim().isNotEmpty) {
      await credential.user?.updateDisplayName(displayName.trim());
      await credential.user?.reload();
    }

    await credential.user?.sendEmailVerification();

    return credential;
  }

  Future<void> sendPhoneOtp({
    required String phoneNumber,
    required void Function(String verificationId, int? resendToken) codeSent,
    required void Function(FirebaseAuthException e) verificationFailed,
  }) async {
    final auth = _auth;
    if (auth == null) {
      throw Exception('Firebase is not initialized. Please configure credentials in lib/firebase_options.dart');
    }

    await auth.verifyPhoneNumber(
      phoneNumber: phoneNumber,
      verificationCompleted: (PhoneAuthCredential credential) async {
        await auth.signInWithCredential(credential);
      },
      verificationFailed: (FirebaseAuthException e) {
        verificationFailed(e);
      },
      codeSent: (String verificationId, int? resendToken) {
        codeSent(verificationId, resendToken);
      },
      codeAutoRetrievalTimeout: (String verificationId) {},
    );
  }

  Future<UserCredential> verifyPhoneOtp({
    required String verificationId,
    required String smsCode,
  }) async {
    final auth = _auth;
    if (auth == null) {
      throw Exception('Firebase is not initialized. Please configure credentials in lib/firebase_options.dart');
    }

    final credential = PhoneAuthProvider.credential(
      verificationId: verificationId,
      smsCode: smsCode,
    );

    return await auth.signInWithCredential(credential);
  }

  /// Send password reset email
  Future<void> sendPasswordResetEmail(String email) async {
    final auth = _auth;
    if (auth == null) {
      throw Exception('Firebase is not initialized. Please configure credentials in lib/firebase_options.dart');
    }
    await auth.sendPasswordResetEmail(email: email.trim());
  }

  Future<void> sendVerificationEmail() async {
    final user = _auth?.currentUser;
    if (user == null) {
      throw Exception('You must be signed in to request email verification.');
    }
    await user.sendEmailVerification();
  }

  Future<void> reloadCurrentUser() async {
    final user = _auth?.currentUser;
    if (user == null) {
      throw Exception('You must be signed in to check email verification.');
    }
    await user.reload();
  }

  /// Sign out the current user
  Future<void> signOut() async {
    await _auth?.signOut();
  }

  /// Converts FirebaseAuthException error codes into friendly user messages
  static String getErrorMessage(dynamic error) {
    if (error is FirebaseAuthException) {
      switch (error.code) {
        case 'user-not-found':
          return 'No user found with this email.';
        case 'wrong-password':
          return 'Incorrect password. Please try again.';
        case 'invalid-credential':
          return 'Invalid email or password.';
        case 'email-already-in-use':
          return 'An account already exists with this email address.';
        case 'weak-password':
          return 'The password is too weak. Please choose a stronger password.';
        case 'invalid-email':
          return 'The email address is invalid.';
        case 'user-disabled':
          return 'This user account has been disabled.';
        case 'too-many-requests':
          return 'Too many failed attempts. Please try again later.';
        case 'operation-not-allowed':
          return 'Email/password sign-in is not enabled in Firebase Console.';
        case 'network-request-failed':
          return 'Network error. Please check your internet connection.';
        case 'invalid-phone-number':
          return 'The phone number is invalid.';
        case 'quota-exceeded':
          return 'SMS quota exceeded. Please try again later.';
        case 'captcha-check-failed':
          return 'Captcha verification failed. Please try again.';
        case 'code-expired':
          return 'The verification code has expired. Please request a new one.';
        default:
          return error.message ?? 'An authentication error occurred (${error.code}).';
      }
    }
    return error.toString();
  }
}
