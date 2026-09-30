# Limitcross Facility

Flutter (Dart) home services app with login, registration, bookings, and service discovery.

## Firebase authentication

Email registration and login use Firebase Authentication. Configure the Firebase
project before testing real accounts:

1. Install the FlutterFire CLI and authenticate with Firebase.
2. Run `flutterfire configure` from the project root.
3. Select the Firebase project and the platforms you support.
4. In Firebase Console, open **Authentication > Sign-in method** and enable **Email/Password**.
5. Run `flutter pub get` and start the app.

The generated `lib/firebase_options.dart` must contain the real project options.
Do not commit private service-account keys or admin credentials to the Flutter app.

Bookings are stored in Cloud Firestore under `users/{uid}/bookings/{bookingId}`.
Deploy the included `firestore.rules` file with the Firebase CLI before using
real accounts. The rules restrict every booking read and write to its owner.

## Run locally

Add Flutter to your PATH if needed:

```bash
export PATH="$HOME/flutter/bin:$PATH"
cd ~/login_ui
```

**Chrome (works on this machine — no Android SDK required):**

```bash
flutter run -d chrome
```

In Chrome DevTools, toggle the device toolbar and pick a Pixel-sized phone to preview the Android layout.

**Android (after installing Android Studio / SDK + an emulator or device):**

```bash
flutter doctor
flutter run -d android
```
