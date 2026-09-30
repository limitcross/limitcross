import 'dart:convert';

import 'package:http/http.dart' as http;
import 'package:shared_preferences/shared_preferences.dart';

class ApiException implements Exception {
  ApiException(this.message, this.statusCode);

  final String message;
  final int statusCode;

  @override
  String toString() => message;
}

class ApiClient {
  ApiClient._();

  static final ApiClient instance = ApiClient._();
  static const String baseUrl = String.fromEnvironment(
    'API_BASE_URL',
    defaultValue: 'http://localhost:8080/api',
  );
  static const String _tokenKey = 'facility_access_token';

  String? _token;

  bool get hasToken => _token != null && _token!.isNotEmpty;

  Future<void> loadToken() async {
    final preferences = await SharedPreferences.getInstance();
    _token = preferences.getString(_tokenKey);
  }

  Future<void> setToken(String token) async {
    _token = token;
    final preferences = await SharedPreferences.getInstance();
    await preferences.setString(_tokenKey, token);
  }

  Future<void> clearToken() async {
    _token = null;
    final preferences = await SharedPreferences.getInstance();
    await preferences.remove(_tokenKey);
  }

  Future<dynamic> get(String path) => _send('GET', path);
  Future<dynamic> post(String path, [Map<String, dynamic>? body]) => _send('POST', path, body);

  Future<dynamic> postValue(String path, dynamic body) => _sendValue('POST', path, body);
  Future<dynamic> put(String path, Map<String, dynamic> body) => _send('PUT', path, body);
  Future<dynamic> patch(String path, Map<String, dynamic> body) => _send('PATCH', path, body);

  Future<dynamic> _send(String method, String path, [Map<String, dynamic>? body]) async {
    return _sendValue(method, path, body ?? {});
  }

  Future<dynamic> _sendValue(String method, String path, dynamic body) async {
    final headers = <String, String>{'Content-Type': 'application/json'};
    if (_token != null) headers['Authorization'] = 'Bearer $_token';
    final uri = Uri.parse('$baseUrl$path');
    late http.Response response;
    switch (method) {
      case 'GET':
        response = await http.get(uri, headers: headers);
      case 'POST':
        response = await http.post(uri, headers: headers, body: jsonEncode(body));
      case 'PUT':
        response = await http.put(uri, headers: headers, body: jsonEncode(body));
      case 'PATCH':
        response = await http.patch(uri, headers: headers, body: jsonEncode(body));
      default:
        throw ApiException('Unsupported HTTP method', 0);
    }
    dynamic decoded;
    if (response.body.isNotEmpty) {
      try {
        decoded = jsonDecode(response.body);
      } catch (_) {
        decoded = response.body;
      }
    }
    if (response.statusCode < 200 || response.statusCode >= 300) {
      final message = decoded is Map && decoded['detail'] != null
          ? decoded['detail'].toString()
          : 'Request failed (${response.statusCode})';
      throw ApiException(message, response.statusCode);
    }
    return decoded;
  }
}