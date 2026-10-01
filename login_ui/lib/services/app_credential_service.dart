import '../models/app_credential.dart';
import 'api_client.dart';

class AppCredentialService {
  AppCredentialService._();

  static final AppCredentialService instance = AppCredentialService._();

  Future<void> saveCredential({
    required String email,
    required String appName,
    required String appPassword,
  }) async {
    final trimmedEmail = email.trim();
    final trimmedAppName = appName.trim();
    final trimmedPassword = appPassword.trim();

    if (trimmedEmail.isEmpty || trimmedAppName.isEmpty || trimmedPassword.isEmpty) {
      throw ArgumentError('Email, app name, and app password are required.');
    }

    await ApiClient.instance.put(
      '/app-credentials/${Uri.encodeComponent(trimmedAppName)}',
      {
      'email': trimmedEmail,
      'appName': trimmedAppName,
      'appPassword': trimmedPassword,
      },
    );
  }

  Future<AppCredential?> getCredential(String appName) async {
    try {
      final response = await ApiClient.instance.get(
        '/app-credentials/${Uri.encodeComponent(appName.trim())}',
      );
      if (response is! Map<String, dynamic>) return null;
      return AppCredential.fromMap(response);
    } on ApiException catch (error) {
      if (error.statusCode == 404) return null;
      rethrow;
    }
  }

  Future<void> deleteCredential(String appName) async {
    await ApiClient.instance.delete(
      '/app-credentials/${Uri.encodeComponent(appName.trim())}',
    );
  }
}
