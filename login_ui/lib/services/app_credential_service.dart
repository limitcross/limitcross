import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:firebase_auth/firebase_auth.dart';

import '../models/app_credential.dart';

class AppCredentialService {
  AppCredentialService._();

  static final AppCredentialService instance = AppCredentialService._();

  static const String collectionName = 'app_credentials';

  FirebaseFirestore get _db => FirebaseFirestore.instance;

  Future<void> saveCredential({
    required String email,
    required String appName,
    required String appPassword,
  }) async {
    final user = FirebaseAuth.instance.currentUser;
    if (user == null) {
      throw StateError('User must be signed in to save app credentials.');
    }

    final trimmedEmail = email.trim();
    final trimmedAppName = appName.trim();
    final trimmedPassword = appPassword.trim();

    if (trimmedEmail.isEmpty || trimmedAppName.isEmpty || trimmedPassword.isEmpty) {
      throw ArgumentError('Email, app name, and app password are required.');
    }

    await _db.collection(collectionName).doc(trimmedAppName).set({
      'email': trimmedEmail,
      'appName': trimmedAppName,
      'appPassword': trimmedPassword,
      'createdByUid': user.uid,
      'updatedAt': FieldValue.serverTimestamp(),
    }, SetOptions(merge: true));
  }

  Future<AppCredential?> getCredential(String appName) async {
    final doc = await _db.collection(collectionName).doc(appName.trim()).get();
    if (!doc.exists || doc.data() == null) return null;

    final data = doc.data()!;
    return AppCredential(
      email: (data['email'] ?? '').toString(),
      appName: (data['appName'] ?? '').toString(),
      appPassword: (data['appPassword'] ?? '').toString(),
      updatedAt: (data['updatedAt'] as Timestamp?)?.toDate() ?? DateTime.now(),
    );
  }

  Future<void> deleteCredential(String appName) async {
    await _db.collection(collectionName).doc(appName.trim()).delete();
  }
}
