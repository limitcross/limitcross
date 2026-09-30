import 'package:flutter/material.dart';

/// Keeps a phone-width layout when the app is opened in a browser or desktop.
class MobileFrame extends StatelessWidget {
  const MobileFrame({super.key, required this.child});

  final Widget child;

  @override
  Widget build(BuildContext context) {
    return LayoutBuilder(
      builder: (context, constraints) {
        if (constraints.maxWidth <= 480) {
          return child;
        }
        return ColoredBox(
          color: const Color(0xFFE8EEF8),
          child: Center(
            child: ConstrainedBox(
              constraints: const BoxConstraints(maxWidth: 390, maxHeight: 844),
              child: ClipRRect(
                borderRadius: BorderRadius.circular(28),
                child: child,
              ),
            ),
          ),
        );
      },
    );
  }
}
