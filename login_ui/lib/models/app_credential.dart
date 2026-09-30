class AppCredential {
  const AppCredential({
    required this.email,
    required this.appName,
    required this.appPassword,
    required this.updatedAt,
  });

  final String email;
  final String appName;
  final String appPassword;
  final DateTime updatedAt;

  factory AppCredential.fromMap(Map<String, dynamic> map) {
    return AppCredential(
      email: (map['email'] ?? '').toString(),
      appName: (map['appName'] ?? '').toString(),
      appPassword: (map['appPassword'] ?? '').toString(),
      updatedAt: (map['updatedAt'] as DateTime?) ?? DateTime.now(),
    );
  }

  Map<String, dynamic> toMap() {
    return {
      'email': email,
      'appName': appName,
      'appPassword': appPassword,
      'updatedAt': updatedAt,
    };
  }
}
