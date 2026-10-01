import 'package:flutter/material.dart';

import '../services/auth_service.dart';
import '../widgets/auth_text_field.dart';
import '../widgets/mobile_frame.dart';

class RegisterScreen extends StatefulWidget {
  const RegisterScreen({super.key});

  @override
  State<RegisterScreen> createState() => _RegisterScreenState();
}

class _RegisterScreenState extends State<RegisterScreen> {
  final _formKey = GlobalKey<FormState>();
  final _name = TextEditingController();
  final _email = TextEditingController();
  final _password = TextEditingController();
  final _confirm = TextEditingController();
  bool _acceptedTerms = false;
  bool _isLoading = false;

  @override
  void dispose() {
    _name.dispose();
    _email.dispose();
    _password.dispose();
    _confirm.dispose();
    super.dispose();
  }

  Future<void> _submit() async {
    if (!(_formKey.currentState?.validate() ?? false)) return;
    if (!_acceptedTerms) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Please accept the terms to continue')),
      );
      return;
    }

    setState(() => _isLoading = true);
    try {
      await AuthService().signUpWithEmailAndPassword(
        email: _email.text.trim(),
        password: _password.text,
        displayName: _name.text.trim(),
      );
      if (!mounted) return;
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(
          content: Text(
            'Account created. Verification email sent to ${_email.text.trim()}.',
          ),
        ),
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
        setState(() => _isLoading = false);
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    return MobileFrame(
      child: Scaffold(
        appBar: AppBar(
          backgroundColor: Colors.transparent,
          title: const Text('Create account'),
        ),
        body: SafeArea(
          child: Center(
            child: SingleChildScrollView(
              padding: const EdgeInsets.symmetric(horizontal: 24, vertical: 8),
              child: Form(
                key: _formKey,
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.stretch,
                  children: [
                    Text(
                      'Join us',
                      style: Theme.of(context).textTheme.headlineMedium?.copyWith(
                            fontWeight: FontWeight.w700,
                          ),
                    ),
                    const SizedBox(height: 8),
                    Text(
                      'Create an account to get started.',
                      style: Theme.of(context).textTheme.bodyLarge?.copyWith(
                          ),
                    ),
                    const SizedBox(height: 28),
                    AuthTextField(
                      label: 'Full name',
                      controller: _name,
                      textInputAction: TextInputAction.next,
                      prefixIcon: Icons.person_outline,
                      autofillHints: const [AutofillHints.name],
                      validator: (value) {
                        if ((value ?? '').trim().length < 2) return 'Enter your name';
                        return null;
                      },
                    ),
                    const SizedBox(height: 14),
                    AuthTextField(
                      label: 'Email',
                      controller: _email,
                      keyboardType: TextInputType.emailAddress,
                      textInputAction: TextInputAction.next,
                      prefixIcon: Icons.mail_outline,
                      autofillHints: const [AutofillHints.email],
                      validator: (value) {
                        final email = value?.trim() ?? '';
                        if (email.isEmpty) return 'Enter your email';
                        if (!RegExp(r'^[^@]+@[^@]+\.[^@]+$').hasMatch(email)) {
                          return 'Enter a valid email';
                        }
                        return null;
                      },
                    ),
                    const SizedBox(height: 14),
                    AuthTextField(
                      label: 'Password',
                      controller: _password,
                      obscureText: true,
                      textInputAction: TextInputAction.next,
                      prefixIcon: Icons.lock_outline,
                      autofillHints: const [AutofillHints.newPassword],
                      validator: (value) {
                        if ((value ?? '').isEmpty) return 'Enter your password';
                        if ((value ?? '').length < 6) return 'Use at least 6 characters';
                        return null;
                      },
                    ),
                    const SizedBox(height: 14),
                    AuthTextField(
                      label: 'Confirm password',
                      controller: _confirm,
                      obscureText: true,
                      textInputAction: TextInputAction.done,
                      prefixIcon: Icons.lock_outline,
                      validator: (value) {
                        if (value != _password.text) return 'Passwords do not match';
                        return null;
                      },
                    ),
                    const SizedBox(height: 8),
                    CheckboxListTile(
                      contentPadding: EdgeInsets.zero,
                      controlAffinity: ListTileControlAffinity.leading,
                      value: _acceptedTerms,
                      onChanged: (value) => setState(() => _acceptedTerms = value ?? false),
                      title: const Text('I agree to the Terms and Privacy Policy'),
                    ),
                    const SizedBox(height: 8),
                    FilledButton(
                      onPressed: _isLoading ? null : _submit,
                      child: _isLoading
                          ? const SizedBox(
                              height: 20,
                              width: 20,
                              child: CircularProgressIndicator(
                                strokeWidth: 2,
                                color: Colors.white,
                              ),
                            )
                          : const Text('Create account'),
                    ),
                    const SizedBox(height: 16),
                    Wrap(
                      alignment: WrapAlignment.center,
                      crossAxisAlignment: WrapCrossAlignment.center,
                      children: [
                        const Text('Already have an account?'),
                        TextButton(
                          onPressed: () => Navigator.of(context).pop(),
                          child: const Text('Log in'),
                        ),
                      ],
                    ),
                  ],
                ),
              ),
            ),
          ),
        ),
      ),
    );
  }
}
