# PixelKey ⌨️ — A Modern, Privacy-First Android Keyboard

**PixelKey** is a modern, high-performance, privacy-first Android virtual keyboard crafted with a clean Bauhaus-inspired design language. Designed for people who value minimalism, tactile beauty, and absolute privacy, PixelKey combines an elegant matte palette, refined typography, dynamic per-key accent popups, and an advanced multilingual typing suite.

Typing never touches the internet. Zero cloud logging. Zero tracking. Just pure, elevated typing.

---

## 🌟 Why PixelKey?

### 🎨 1. Bauhaus-Inspired Design Language
- **Matte Palette**: Handcrafted matte tones designed for high legibility and OLED battery conservation
- **Dynamic Accent Popups**: Long-presses and keystroke bubble previews dynamically inherit the tint of the underlying functional key
- **Refined Typography**: Clean, readable fonts across key labels, suggestions and settings

### 🌍 2. Multilingual Typing Suite
- **Bangla**: Avro-style phonetic, Probhat and Jatiyo layouts
- **English**: Full QWERTY with next-word prediction and autocorrect
- **Banglish**: Million-scale romanized Bengali suggestions on the English keyboard — the full Bangla vocabulary (454k words → 1.5M typed forms) as completions, plus a 930-word daily-use list with typo correction (bhalu → bhalo) and next-word bigrams (kemon → acho)
- **Hindi**: Devanagari InScript layout
- **Spanish**: Layout with ñ, accents and ¿¡ punctuation
- **Chinese**: Pinyin input with a candidate bar
- **Arabic**: Proper RTL layout with correct letter shaping
- **One-Tap Language Toggle**: Switch languages instantly via the globe key

### 🔢 3. Streamlined 3-Page Symbol & Number Architecture
- **Page 1 (Alphabet)**: Clean language layout
- **Page 2 (Numbers & Standard Symbols)**: Number row, arithmetic signs, and common punctuation
- **Page 3 (Extended Symbols & Math)**: Advanced math symbols, brackets, and typography

### 🔒 4. Offline & Private
- **No network use for typing**: Typing, prediction and dictionary lookups are fully on-device. The manifest requests `INTERNET` only for the optional update check; it is never used while you type
- **On-Device Only**: Your passwords, messages, bank details, and personal dictionaries never leave your local storage
- **No Telemetry**: No trackers, no analytics SDKs

### ⚡ 5. Power Features
- **Haptic Engine**: Crisp, tactile vibration feedback
- **Clipboard Management**: Pinned clipboard history with dedup, auto-clear of unpinned items after 1 hour, sensitive-clip protection, and a paste chip that appears right after you copy
- **Text Shortcuts**: Save snippets like `/mail` → your full email; type the shortcut, press space, done
- **Incognito Mode**: No word learning and no clipboard history while enabled
- **Smart Suggestions**: Per-app dismiss (stays dismissed), no candidates on password/number/email/URL fields, and nothing shown when the model is not confident
- **Grapheme-Aware Backspace**: Bangla conjuncts (ক্ষ), emoji ZWJ sequences and surrogate pairs delete as one unit
- **Customizable Dimensions**: Adjustable keyboard height, one-handed mode, and split tablet layouts
- **Gesture Typing**: Smooth glide trace input with real-time word formulation
- **Voice Typing**: Fully on-device speech recognition (optional microphone permission)

---

## 📸 Layout Preview

```
+-------------------------------------------------------+
|  [ Q ] [ W ] [ E ] [ R ] [ T ] [ Y ] [ U ] [ I ] [ O ] [ P ]  |
|   [ A ] [ S ] [ D ] [ F ] [ G ] [ H ] [ J ] [ K ] [ L ]   |
|    [^]   [ Z ] [ X ] [ C ] [ V ] [ B ] [ N ] [ M ]   [x]   |
|   [?123]   [  🌐  ]   [     Space Bar     ]   [ . ]  [ ↵ ]   |
+-------------------------------------------------------+
```

---

## 🛠️ Tech Stack & Architecture

- **Operating System:** Android 7.0 (Nougat, API 24) through Android 15 (targets API 34)
- **Architecture:** Android Open Source Project (AOSP) LatinIME foundation with modern Jetpack Compose UIX
- **Native Core:** C++ JNI Native Input Engine (`libjni_latinime.so`, pocketfft, ggml)
- **Languages:** Java, Kotlin, C++
- **Build System:** Gradle 8.2 & Android Gradle Plugin 8.2

---

## 🚀 Building from Source

### Prerequisites
- Android Studio Ladybug / Koala or CLI tools
- Java Development Kit (JDK) 17+
- Android SDK 34 & NDK (`r25c` or newer)

### Build Steps
```bash
# Clone the repository
git clone https://github.com/explorefaith/Pixel-Key.git
cd Pixel-Key

# Build the release APKs
./gradlew assembleStableRelease

# Outputs are located in:
# build/outputs/apk/stable/release/
```

---

## 🐛 Bug Reporting & Feedback

If you encounter any bug, glitch, or layout suggestion, reach out:

- **WhatsApp:** [Chat with the developer](https://wa.me/8801610709657)
- **Issue Tracker:** [GitHub Issues](https://github.com/explorefaith/Pixel-Key/issues)

When reporting a bug, please include:
1. Device model
2. Android version
3. Steps to reproduce the issue

---

## 👨‍💻 Developer & Author

- **Developer & Designer:** **Eyasin Arafat**
- **GitHub:** [@explorefaith](https://github.com/explorefaith)
- **Instagram:** [@explorearafat](https://www.instagram.com/explorearafat)
- **WhatsApp:** [+880 1610 709657](https://wa.me/8801610709657)

---

## 🎙️ Voice Typing and Microphone Permission

If your voice typing is not responding or the microphone does not detect audio, Android may have restricted microphone access by default. Follow these steps to grant permission:

1. **Quick Method:**
   - On your home screen or app drawer, **long-press the PixelKey app icon**.
   - Tap **App info** (the ⓘ icon).
   - Go to **Permissions** -> **Microphone**.
   - Select **"Allow only while using the app"**.

2. **From Android Settings:**
   - Open your phone's **Settings** app.
   - Navigate to **Apps** -> **PixelKey**.
   - Tap **Permissions** -> **Microphone** -> Select **Allow**.

*Note: PixelKey processes microphone audio entirely on-device. Audio is never recorded, stored, or sent to any server.*

## 📄 License & Legal Notice

PixelKey is released under the **Apache License 2.0**.

```text
Copyright (c) 2026 Eyasin Arafat

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0
```

See [LICENSE.md](LICENSE.md) and [NOTICE](NOTICE) for complete third-party licenses and credits. PixelKey builds upon the work of the AOSP LatinIME, FUTO Keyboard, and prior community fork projects — their upstream licenses are preserved with attribution.
