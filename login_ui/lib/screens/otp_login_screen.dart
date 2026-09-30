import 'package:flutter/material.dart';

import '../services/auth_service.dart';
import '../services/user_profile_service.dart';
import '../widgets/auth_text_field.dart';
import '../widgets/mobile_frame.dart';

class OtpLoginScreen extends StatefulWidget {
  const OtpLoginScreen({super.key});

  @override
  State<OtpLoginScreen> createState() => _OtpLoginScreenState();
}

class _OtpLoginScreenState extends State<OtpLoginScreen> {
  final _phoneController = TextEditingController();
  final _otpController = TextEditingController();
  final _fullNameController = TextEditingController();
  final _emailController = TextEditingController();
  final _formKey = GlobalKey<FormState>();
  bool _isSendingOtp = false;
  bool _isVerifying = false;
  String? _verificationId;

  Future<void> _sendOtp() async {
    if (!(_formKey.currentState?.validate() ?? false)) return;

    setState(() => _isSendingOtp = true);
    try {
      final phone = _phoneController.text.trim();
      await AuthService().sendPhoneOtp(
        phoneNumber: phone,
        codeSent: (verificationId, _) {
          setState(() {
            _verificationId = verificationId;
          });
          if (mounted) {
            ScaffoldMessenger.of(context).showSnackBar(
              const SnackBar(content: Text('OTP sent successfully.')),
            );
          }
        },
        verificationFailed: (e) {
          if (mounted) {
            ScaffoldMessenger.of(context).showSnackBar(
              SnackBar(
                content: Text(AuthService.getErrorMessage(e)),
                backgroundColor: Theme.of(context).colorScheme.error,
              ),
            );
          }
        },
      );
    } catch (e) {
      if (!mounted) return;
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(
          content: Text(AuthService.getErrorMessage(e)),
          backgroundColor: Theme.of(context).colorScheme.error,
        ),
      );
    } finally {
      if (mounted) {
        setState(() => _isSendingOtp = false);
      }
    }
  }

  Future<void> _verifyOtp() async {
    if (_verificationId == null || _otpController.text.trim().isEmpty) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Please request and enter the OTP first.')),
      );
      return;
    }

    setState(() => _isVerifying = true);
    try {
      final credential = await AuthService().verifyPhoneOtp(
        verificationId: _verificationId!,
        smsCode: _otpController.text.trim(),
      );

      final user = credential.user;
      if (user != null) {
        await UserProfileService.instance.createUserProfile(
          uid: user.uid,
          phone: _phoneController.text.trim(),
          fullName: _fullNameController.text.trim().isEmpty ? 'New User' : _fullNameController.text.trim(),
          email: _emailController.text.trim(),
        );
      }

      if (!mounted) return;
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('OTP verified successfully.')),
      );
      Navigator.of(context).pop();
    } catch (e) {
      if (!mounted) return;
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(
          content: Text(AuthService.getErrorMessage(e)),
          backgroundColor: Theme.of(context).colorScheme.error,
        ),
      );
    } finally {
      if (mounted) {
        setState(() => _isVerifying = false);
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    return MobileFrame(
      child: Scaffold(
        appBar: AppBar(
          title: const Text('Phone login'),
        ),
        body: SafeArea(
          child: SingleChildScrollView(
            padding: const EdgeInsets.all(24),
            child: Form(
              key: _formKey,
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.stretch,
                children: [
                  const Text(
                    'Register or sign in with OTP',
                    style: TextStyle(fontSize: 24, fontWeight: FontWeight.bold),
                  ),
                  const SizedBox(height: 20),
                  AuthTextField(
                    label: 'Full name',
                    controller: _fullNameController,
                    prefixIcon: Icons.person_outline,
                    validator: (value) {
                      if ((value ?? '').trim().isEmpty) return 'Enter your name';
                      return null;
                    },
                  ),
                  const SizedBox(height: 14),
                  AuthTextField(
                    label: 'Email (optional)',
                    controller: _emailController,
                    keyboardType: TextInputType.emailAddress,
                    prefixIcon: Icons.email_outlined,
                    validator: (value) {
                      final text = (value ?? '').trim();
                      if (text.isEmpty) return null;
                      if (!RegExp(r'^[^@]+@[^@]+\.[^@]+$').hasMatch(text)) {
                        return 'Enter a valid email';
                      }
                      return null;
                    },
                  ),
                  const SizedBox(height: 14),
                  AuthTextField(
                    label: 'Phone number',
                    controller: _phoneController,
                    keyboardType: TextInputType.phone,
                    prefixIcon: Icons.phone_outlined,
                    validator: (value) {
                      if ((value ?? '').trim().isEmpty) return 'Enter your phone number';
                      return null;
                    },
                  ),
                  const SizedBox(height: 14),
                  FilledButton.icon(
                    onPressed: _isSendingOtp ? null : _sendOtp,
                    icon: const Icon(Icons.send_rounded),
                    label: _isSendingOtp
                        ? const SizedBox(
                            height: 20,
                            width: 20,
                            child: CircularProgressIndicator(strokeWidth: 2, color: Colors.white),
                          )
                        : const Text('Send OTP'),
                  ),
                  const SizedBox(height: 18),
                  AuthTextField(
                    label: 'OTP code',
                    controller: _otpController,
                    keyboardType: TextInputType.number,
                    prefixIcon: Icons.pin,
                    validator: (value) {
                      if ((value ?? '').trim().isEmpty) return 'Enter the OTP';
                      return null;
                    },
                  ),
                  const SizedBox(height: 18),
                  FilledButton.icon(
                    onPressed: _isVerifying ? null : _verifyOtp,
                    icon: const Icon(Icons.verified_user_rounded),
                    label: _isVerifying
                        ? const SizedBox(
                            height: 20,
                            width: 20,
                            child: CircularProgressIndicator(strokeWidth: 2, color: Colors.white),
                          )
                        : const Text('Verify OTP'),
                  ),
                ],
              ),
            ),
          ),
        ),
      ),
    );
  }
}
