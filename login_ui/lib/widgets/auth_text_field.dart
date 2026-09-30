import 'package:flutter/material.dart';

class AuthTextField extends StatefulWidget {
  const AuthTextField({
    super.key,
    required this.label,
    required this.controller,
    this.keyboardType,
    this.textInputAction,
    this.obscureText = false,
    this.prefixIcon,
    this.validator,
    this.autofillHints,
  });

  final String label;
  final TextEditingController controller;
  final TextInputType? keyboardType;
  final TextInputAction? textInputAction;
  final bool obscureText;
  final IconData? prefixIcon;
  final String? Function(String?)? validator;
  final Iterable<String>? autofillHints;

  @override
  State<AuthTextField> createState() => _AuthTextFieldState();
}

class _AuthTextFieldState extends State<AuthTextField> {
  late bool _hidden = widget.obscureText;

  @override
  Widget build(BuildContext context) {
    return TextFormField(
      controller: widget.controller,
      keyboardType: widget.keyboardType,
      textInputAction: widget.textInputAction,
      obscureText: _hidden,
      validator: widget.validator,
      autofillHints: widget.autofillHints,
      decoration: InputDecoration(
        labelText: widget.label,
        prefixIcon: widget.prefixIcon == null ? null : Icon(widget.prefixIcon),
        suffixIcon: widget.obscureText
            ? IconButton(
                tooltip: _hidden ? 'Show password' : 'Hide password',
                onPressed: () => setState(() => _hidden = !_hidden),
                icon: Icon(_hidden ? Icons.visibility_outlined : Icons.visibility_off_outlined),
              )
            : null,
      ),
    );
  }
}
