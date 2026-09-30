import 'api_client.dart';
import 'package:flutter/foundation.dart';

class BackendUser {
  const BackendUser({required this.login, required this.email, this.fullName = ''});

  final String login;
  final String email;
  final String fullName;
  bool get emailVerified => true;
}

class BackendAuthService {
  BackendAuthService._();

  static final BackendAuthService instance = BackendAuthService._();
  final ApiClient _api = ApiClient.instance;
  final ValueNotifier<BackendUser?> session = ValueNotifier<BackendUser?>(null);
  BackendUser? _currentUser;

  BackendUser? get currentUser => _currentUser;

  Future<void> initialize() async {
    await _api.loadToken();
    if (!_api.hasToken) return;
    try {
      await reloadCurrentUser();
    } catch (_) {
      await signOut();
    }
  }

  Future<void> signIn({required String email, required String password}) async {
    final response = await _api.post('/authenticate', {
      'username': email.trim(),
      'password': password,
    });
    await _api.setToken(response['id_token'].toString());
    await reloadCurrentUser();
  }

  Future<void> register({required String email, required String password, required String fullName}) async {
    final parts = fullName.trim().split(RegExp(r'\s+'));
    await _api.post('/register', {
      'login': email.trim(),
      'email': email.trim(),
      'password': password,
      'firstName': parts.first,
      'lastName': parts.length > 1 ? parts.sublist(1).join(' ') : '',
      'langKey': 'en',
    });
    await signIn(email: email, password: password);
  }

  Future<void> reloadCurrentUser() async {
    final response = await _api.get('/account');
    _currentUser = BackendUser(
      login: response['login'].toString(),
      email: (response['email'] ?? response['login']).toString(),
      fullName: '${response['firstName'] ?? ''} ${response['lastName'] ?? ''}'.trim(),
    );
    session.value = _currentUser;
  }

  Future<void> sendPasswordResetEmail(String email) async {
    await _api.postValue('/account/reset-password/init', email.trim());
  }

  Future<void> signOut() async {
    await _api.clearToken();
    _currentUser = null;
    session.value = null;
  }
}