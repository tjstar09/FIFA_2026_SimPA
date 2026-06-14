# Release Steps for FIFA 2026 SimPA

> Complete these steps to generate a signed release APK ready for Google Play Store.

---

## 1. Generate a Java Keystore

Open a terminal in the project root and run:

```bash
keytool -genkey -v \
  -keystore app/simpa-release.jks \
  -alias simpa \
  -keyalg RSA \
  -keysize 2048 \
  -validity 10000 \
  -storepass YOUR_STORE_PASSWORD \
  -keypass YOUR_KEY_PASSWORD \
  -dname "CN=Tanmay Jain, OU=Dev, O=FIFA SimPA, L=City, S=State, C=IN"
```

> Replace `YOUR_STORE_PASSWORD` and `YOUR_KEY_PASSWORD` with strong passwords.

---

## 2. Configure Keystore in Gradle

Create `app/keystore.properties` (already in `.gitignore`):

```properties
storeFile=simpa-release.jks
storePassword=YOUR_STORE_PASSWORD
keyAlias=simpa
keyPassword=YOUR_KEY_PASSWORD
```

Update `app/build.gradle.kts` — add above the `android {}` block:

```kotlin
android {
    // ... existing config ...

    // Load keystore
    val keystorePropertiesFile = rootProject.file("app/keystore.properties")
    val keystoreProperties = java.util.Properties()
    if (keystorePropertiesFile.exists()) {
        keystoreProperties.load(keystorePropertiesFile.inputStream())
    }

    signingConfigs {
        create("release") {
            storeFile = file(keystoreProperties["storeFile"] as String)
            storePassword = keystoreProperties["storePassword"] as String
            keyAlias = keystoreProperties["keyAlias"] as String
            keyPassword = keystoreProperties["keyPassword"] as String
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release") // <-- CHANGED from debug
        }
        // ... debug block unchanged
    }
}
```

---

## 3. Add API Keys (Optional)

Create `local.properties` in the project root (already in `.gitignore`):

```properties
SPORTMONKS_API_KEY=your_sportmonks_api_key_here
API_FOOTBALL_KEY=your_api_football_key_here
MACHINA_API_KEY=your_machina_api_key_here
```

> The app **works without API keys** using built-in mock data for 48 teams across 12 groups.

---

## 4. Build Release APK

```bash
# On macOS/Linux
./gradlew assembleRelease

# On Windows
gradlew.bat assembleRelease
```

Output APK location: `app/build/outputs/apk/release/app-release.apk`

---

## 5. Verify APK

```bash
# Check the APK is signed
jarsigner -verify -verbose -certs app/build/outputs/apk/release/app-release.apk

# Or use Android Studio: Build → Analyze APK
```

---

## 6. Prepare for Play Store

- **Target SDK**: Already set to 35 (latest)
- **Min SDK**: 26 (Android 8.0 Oreo)
- **App Bundle (AAB)**: Run `./gradlew bundleRelease` for Play Store submission
- **Privacy Policy**: Add a URL in `app/src/main/res/values/strings.xml`
- **App Icon**: Replace `mipmap` resources with a custom 1024×1024 icon

---

## 7. ProGuard / R8 Verification

R8 is enabled for release builds. Verify no critical classes are stripped:

```bash
# Check mapping file
cat app/build/outputs/mapping/release/mapping.txt | grep "com.fifa.simpa"
```

The `app/proguard-rules.pro` file already contains keep rules for:
- Retrofit interfaces
- Kotlin Serialization
- OkHttp/Okio
- Coil image loading
- Coroutines
- All data/domain model classes

---

## Quick Checklist

- [ ] Generate keystore (`simpa-release.jks`)
- [ ] Create `app/keystore.properties`
- [ ] Update `app/build.gradle.kts` signing config
- [ ] (Optional) Add API keys to `local.properties`
- [ ] Run `./gradlew assembleRelease`
- [ ] Verify signed APK exists
- [ ] Test on a physical device