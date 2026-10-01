import 'package:flutter/material.dart';

import '../services/auth_service.dart';
import '../widgets/auth_text_field.dart';
import '../widgets/mobile_frame.dart';
import 'register_screen.dart';

class LoginScreen extends StatefulWidget {
  const LoginScreen({super.key});

  @override
  State<LoginScreen> createState() => _LoginScreenState();
}

class _LoginScreenState extends State<LoginScreen> {
  final _formKey = GlobalKey<FormState>();
  final _email = TextEditingController();
  final _password = TextEditingController();
  bool _rememberMe = true;
  bool _isLoading = false;

  @override
  void dispose() {
    _email.dispose();
    _password.dispose();
    super.dispose();
  }

  Future<void> _submit() async {
    if (!(_formKey.currentState?.validate() ?? false)) return;

    setState(() => _isLoading = true);
    try {
      await AuthService().signInWithEmailAndPassword(
        email: _email.text.trim(),
        password: _password.text,
      );
      if (!mounted) return;
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Successfully logged in!')),
      );
    } catch (e) {
      if (!mounted) return;
      final message = AuthService.getErrorMessage(e);
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(
          content: Text(message),
          backgroundColor: Theme.of(context).colorScheme.error,
        ),
      );
    } finally {
      if (mounted) {
        setState(() => _isLoading = false);
      }
    }
  }

  Future<void> _forgotPassword() async {
    final resetEmailController = TextEditingController(text: _email.text.trim());
    final dialogFormKey = GlobalKey<FormState>();

    final confirmed = await showDialog<bool>(
      context: context,
      builder: (ctx) => AlertDialog(
        title: const Text('Reset Password'),
        content: Form(
          key: dialogFormKey,
          child: Column(
            mainAxisSize: MainAxisSize.min,
            children: [
              const Text('Enter your account email to receive a password reset link:'),
              const SizedBox(height: 12),
              TextFormField(
                controller: resetEmailController,
                keyboardType: TextInputType.emailAddress,
                decoration: const InputDecoration(labelText: 'Email'),
                validator: (v) {
                  final email = v?.trim() ?? '';
                  if (email.isEmpty) return 'Enter your email';
                  if (!RegExp(r'^[^@]+@[^@]+\.[^@]+$').hasMatch(email)) {
                    return 'Enter a valid email';
                  }
                  return null;
                },
              ),
            ],
          ),
        ),
        actions: [
          TextButton(
            onPressed: () => Navigator.of(ctx).pop(false),
            child: const Text('Cancel'),
          ),
          FilledButton(
            onPressed: () {
              if (dialogFormKey.currentState?.validate() ?? false) {
                Navigator.of(ctx).pop(true);
              }
            },
            child: const Text('Send Link'),
          ),
        ],
      ),
    );

    if (confirmed == true && resetEmailController.text.trim().isNotEmpty) {
      try {
        await AuthService().sendPasswordResetEmail(resetEmailController.text.trim());
        if (!mounted) return;
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text('Password reset link sent to ${resetEmailController.text.trim()}')),
        );
      } catch (e) {
        if (!mounted) return;
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(
            content: Text(AuthService.getErrorMessage(e)),
            backgroundColor: Theme.of(context).colorScheme.error,
          ),
        );
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    final colors = Theme.of(context).colorScheme;

    return MobileFrame(
      child: Scaffold(
        body: SafeArea(
          child: Center(
            child: SingleChildScrollView(
              padding: const EdgeInsets.symmetric(horizontal: 24, vertical: 16),
              child: Form(
                key: _formKey,
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.stretch,
                  children: [
                    const SizedBox(height: 12),
                    Align(
                      alignment: Alignment.centerLeft,
                      child: Container(
                        height: 60,
                        width: 60,
                        decoration: BoxDecoration(
                          color: colors.primaryContainer,
                          borderRadius: BorderRadius.circular(18),
                        ),
                        child: Icon(Icons.lock_open_rounded, color: colors.primary, size: 30),
                      ),
                    ),
                    const SizedBox(height: 28),
                    Text(
                      'Welcome back',
                      style: Theme.of(context).textTheme.headlineMedium?.copyWith(
                            fontWeight: FontWeight.w700,
                          ),
                    ),
                    const SizedBox(height: 8),
                    Text(
                      'Sign in to continue to your account.',
                      style: Theme.of(context).textTheme.bodyLarge?.copyWith(
                          ),
                    ),
                    const SizedBox(height: 32),
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
                      textInputAction: TextInputAction.done,
                      prefixIcon: Icons.lock_outline,
                      autofillHints: const [AutofillHints.password],
                      validator: (value) {
                        if ((value ?? '').isEmpty) return 'Enter your password';
                        if ((value ?? '').length < 6) return 'Use at least 6 characters';
                        return null;
                      },
                    ),
                    const SizedBox(height: 8),
                    Wrap(
                      alignment: WrapAlignment.spaceBetween,
                      crossAxisAlignment: WrapCrossAlignment.center,
                      children: [
                        InkWell(
                          onTap: () => setState(() => _rememberMe = !_rememberMe),
                          child: Row(
                            mainAxisSize: MainAxisSize.min,
                            children: [
                              Checkbox(
                                visualDensity: VisualDensity.compact,
                                materialTapTargetSize: MaterialTapTargetSize.shrinkWrap,
                                value: _rememberMe,
                                onChanged: (value) => setState(() => _rememberMe = value ?? false),
                              ),
                              const Text('Remember me'),
                            ],
                          ),
                        ),
                        TextButton(
                          onPressed: _forgotPassword,
                          child: const Text('Forgot password?'),
                        ),
                      ],
                    ),
                    const SizedBox(height: 12),
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
                          : const Text('Log in'),
                    ),
                    const SizedBox(height: 24),
                    Row(
                      children: [
                        const Expanded(child: Divider()),
                        Padding(
                          padding: const EdgeInsets.symmetric(horizontal: 12),
                          child: Text(
                            'or',
                            style: TextStyle(color: colors.outline),
                          ),
                        ),
                        const Expanded(child: Divider()),
                      ],
                    ),
                    const SizedBox(height: 16),
                    OutlinedButton.icon(
                      onPressed: () {
                        ScaffoldMessenger.of(context).showSnackBar(
                          const SnackBar(
                            content: Text(
                              'Google Sign-In requires configuring OAuth in the Firebase Console.',
                            ),
                          ),
                        );
                      },
                      icon: const Icon(Icons.g_mobiledata, size: 28),
                      label: const Text('Continue with Google'),
                      style: OutlinedButton.styleFrom(
                        minimumSize: const Size.fromHeight(52),
                        backgroundColor: Colors.white,
                        shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(14)),
                      ),
                    ),
                    const SizedBox(height: 20),
                    Wrap(
                      alignment: WrapAlignment.center,
                      crossAxisAlignment: WrapCrossAlignment.center,
                      children: [
                        const Text("Don't have an account?"),
                        TextButton(
                          onPressed: () {
                            Navigator.of(context).push(
                              MaterialPageRoute<void>(
                                builder: (_) => const RegisterScreen(),
                              ),
                            );
                          },
                          child: const Text('Register'),
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
