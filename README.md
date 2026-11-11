# YouTube Fullscreen WebView Android App (Kotlin + Jetpack Compose)

This is a production-ready Android project built to your exact specifications.

## Key Features
- **Single Activity**: MainActivity.kt hosting Compose AndroidView.
- **Immediate Load**: Loads https://m.youtube.com instantly on launch.
- **JavaScript Enabled**: settings.javaScriptEnabled = true
- **DOM Storage Enabled**: settings.domStorageEnabled = true
- **Internal WebViewClient**: Keeps clicks inside the app; never opens system browser.
- **INTERNET Permission**: Declared in AndroidManifest.xml.
- **100% Fullscreen**: No toolbar, no address bar, Modifier.fillMaxSize().
- **minSdk = 24**: Targets Android 7.0 Nougat and newer.
- **Back Handler**: WebView canGoBack navigation, exits cleanly when at start.

## How to Run in Android Studio
1. Unzip this folder.
2. In Android Studio, click **Open** and select this directory.
3. Wait for Gradle sync to complete.
4. Press **Run** (Shift+F10) on a real device or emulator.
