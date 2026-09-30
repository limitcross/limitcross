import '../models/user_profile.dart';
import 'api_client.dart';

class UserProfileService {
  UserProfileService._();

  static final UserProfileService instance = UserProfileService._();

  Future<void> createUserProfile({
    required String uid,
    required String phone,
    required String fullName,
    required String email,
  }) async {
    await ApiClient.instance.put('/users/me', {
      'phone': phone,
      'fullName': fullName,
      'email': email,
    });
  }

  Future<UserProfile?> getProfile(String uid) async {
    final response = await ApiClient.instance.get('/users/me');
    return UserProfile.fromMap(uid, response as Map<String, dynamic>);
  }

  Future<void> updateLastLogin(String uid) async {
    return;
  }

  Future<void> signOutAndResetSession() async {
    await ApiClient.instance.clearToken();
  }
}
