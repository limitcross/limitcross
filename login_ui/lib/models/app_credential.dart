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
    final rawUpdatedAt = map['updatedAt'];
    final updatedAt = rawUpdatedAt is DateTime
        ? rawUpdatedAt
        : DateTime.tryParse(rawUpdatedAt?.toString() ?? '') ?? DateTime.now();
    return AppCredential(
      email: (map['email'] ?? '').toString(),
      appName: (map['appName'] ?? '').toString(),
      appPassword: (map['appPassword'] ?? '').toString(),
      updatedAt: updatedAt,
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
