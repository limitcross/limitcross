class UserProfile {
  const UserProfile({
    required this.uid,
    required this.phone,
    required this.fullName,
    required this.email,
    required this.createdAt,
    required this.lastLoginAt,
  });

  final String uid;
  final String phone;
  final String fullName;
  final String email;
  final DateTime createdAt;
  final DateTime lastLoginAt;

  factory UserProfile.fromMap(String uid, Map<String, dynamic> map) {
    final createdAt = DateTime.tryParse((map['createdAt'] ?? '').toString()) ?? DateTime.now();
    final lastLoginAt = DateTime.tryParse((map['lastLoginAt'] ?? '').toString()) ?? createdAt;

    return UserProfile(
      uid: uid,
      phone: (map['phone'] ?? '').toString(),
      fullName: (map['fullName'] ?? '').toString(),
      email: (map['email'] ?? '').toString(),
      createdAt: createdAt,
      lastLoginAt: lastLoginAt,
    );
  }

  Map<String, dynamic> toMap() {
    return {
      'uid': uid,
      'phone': phone,
      'fullName': fullName,
      'email': email,
      'createdAt': createdAt.toIso8601String(),
      'lastLoginAt': lastLoginAt.toIso8601String(),
    };
  }
}
