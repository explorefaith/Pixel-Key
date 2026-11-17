# PixelKey — Active Issues & Feature Tracking

## Active & Resolved High-Priority Issues

### ISSUE-006: Long-press Cut Deleted the Entire Text Field (Data Loss)
- **Status**: RESOLVED (Build 1.1.0)
- **Description**: `performCut()` always copied and deleted the whole editor buffer — even when the user had only a word selected. Unselected text after the cursor was destroyed.
- **Fix**: `LatinIMELegacy.java` — Cut now removes only the selection when one exists (whole-buffer fallback kept for empty selections), with correct cache reset to the new cursor position.

### ISSUE-007: Backspace Safeguard Broke Double-Space-Period / Autocorrect Revert (All Languages)
- **Status**: RESOLVED (Build 1.1.0)
- **Description**: The ISSUE-003 space-only-delete branch ran before the upstream revert logic, making double-space-period undo, punctuation-swap undo, and "backspace to undo autocorrect" unreachable in every language (e.g. backspace after `". "` deleted only the space and left the period).
- **Fix**: `InputLogic.java` — the safeguard now applies only to the phonetic engines (Avro/Pinyin), yields to double-space-period and punctuation-swap undo, and English keeps the stock AOSP revert flow. The single-character delete for phonetic engines moved after the revert checks.

### ISSUE-008: Stale `mCursorMoved` Flag Swallowed Keystrokes
- **Status**: RESOLVED (Build 1.1.0)
- **Description**: After sliding on backspace/space, a cancelled touch (incoming call, nav gesture) left `mCursorMoved` set; the next tap fired a spurious delete/selection callback and the keystroke was silently dropped.
- **Fix**: `PointerTracker.java` — `mCursorMoved` is now reset on key down and on touch cancel.

### ISSUE-009: Clipboard Shortcuts Missing on Probhat & Jatiyo
- **Status**: RESOLVED (Build 1.1.0)
- **Description**: ISSUE-002 claimed Probhat (শ/ঢ়, চ/ছ, আ/ঋ) and Jatiyo (ো/ৌ, ে/ৈ, র) long-press cut/copy/paste support, but the gate only accepted English + Avro and the key helpers only matched ASCII x/c/v.
- **Fix**: `PointerTracker.java` + `Key.java` — gate now accepts `bengali_probhat`/`bengali_jatiyo`, and the Bengali key codes at the x/c/v positions are recognized. Note: on those keys, long-press now triggers the clipboard action (Ridmik behavior) instead of the more-keys popup, per ISSUE-002's spec.

### ISSUE-010: Release Keystore Was Committed to the Public Repo
- **Status**: RESOLVED (Build 1.1.0)
- **Description**: `java/shared.keystore` was tracked in git and used to sign release APKs — anyone could build a malicious update that installs over the official app.
- **Fix**: New PKCS12 release keystore generated OUTSIDE the repo (see `keystore.properties`, gitignored). `shared.keystore` removed from tracking and disk. IMPORTANT: publish the v1.1.0 release from this key; existing installs must uninstall first (signature change). The old key still exists in git history — run `git filter-repo --path java/shared.keystore --invert-paths` and force-push to purge it.

### ISSUE-011: Other Resolved Maintenance Items (Build 1.1.0)
- Pinyin candidate bar now receives suggestions when the transformer LM is enabled (`LanguageModelFacilitator.kt`, `LatinIME.kt`).
- Space after typing a symbol on pages 2/3 returns to the alphabet for non-Bengali layouts (stock AOSP behavior restored; Bengali pages still latch, ISSUE-001 unchanged).
- `onLongPressed()` failures now release stuck key graphics instead of swallowing silently.
- Hardware-key identifier precedence bug fixed (`deviceId << 32 | keyCode`).
- Gesture-emoji replacement offsets fixed for Avro/Pinyin compositions (`getComposingLength` tracks the converted text length).
- Spanish polish: `values-es/strings-uix.xml` added; language-screen strings extracted to resources (`uix_*`); layout labels translated.
- Website/README no longer claim "no internet permission" (the manifest declares INTERNET for the optional update check); download links updated to v1.1.0.
- Stable crash reports no longer mailed to upstream FUTO — add your own address in `CrashLoggingApplication.kt` (stable flavor) to re-enable email reports.
- Cleanups: dead gradle closures, `enableJetifier`, unused glyph SDK jar + Nothing permission/meta-data, dangling top-level `dictionaries/`, unused tensorflow-lite AARs, orphan keyboard XMLs (`key_space_5kw`, `row_pcqwerty5`), unreachable locale→layout map entries (qwertz/azerty/nordic/hebrew/east_slavic/south_slavic/turkish/Kurdish), orphan `lint-baseline.xml`, deprecated `lintOptions` → `lint`, APK-rename task case mismatch, README build-info accuracy.
- Known limitation (open): Arabic, Hindi and Chinese ship with the 34-byte stub `main.dict` — no built-in suggestions/autocorrect dictionaries for those languages yet. The stable manifest's update-checking service/receiver are kept declared (inert while `UPDATE_CHECKING=false`) because `InstallReceiver` backs the in-app update install flow.

### ISSUE-001: Symbol Page 2 and Page 3 Two-Way Toggle Connection (User Marked)
- **Status**: RESOLVED & VERIFIED (Build 1.0.2)
- **Reported By**: User (with visual screenshots: `media_1793135938970_8b3cd142.png` & `media_1793135938804_745667f6.png`)
- **Description**:
  - **2nd Page (`kbd_symbols_bengali.xml`)**: Contains Bengali numbers `১ ২ ৩ ৪ ৫ ৬ ৭ ৮ ৯ ০`, currency `৳`, and basic punctuation. The red-marked key above `অকখ` is labeled `= \<`.
  - **3rd Page (`kbd_symbols_shift_bengali.xml`)**: Contains symbols `~ ` | ₹ √ π ÷ × { }`, `⇥ £ $ % ^ ° _ = [ ]`, etc. The red-marked key above `অকখ` is labeled `?১২৩`.
  - **Requirement**: The red-marked key on these two pages MUST act as a direct two-way bridge connecting them:
    - Pressing `= \<` on 2nd Page $\rightarrow$ Immediately switches to 3rd Page (`kbd_symbols_shift_bengali`).
    - Pressing `?১২৩` on 3rd Page $\rightarrow$ Immediately switches back to 2nd Page (`kbd_symbols_bengali`).
    - The bottom-left functional key `অকখ` (or `ABC` in English) remains the dedicated key to return to the 1st page (Alphabet layout).
- **Root Cause & Architectural Resolution**:
  - `PointerTracker.java`: When a modifier key triggers a layout switch on `ACTION_DOWN` (`mLayoutChangedOnDown`), `onUpEventInternal` releases key graphics without sending `detectAndSendKey`, suppressing unintentional double-toggling of the newly revealed opposite key on finger lift.
  - `KeyboardState.java`: Symbol paging switch state is set to `SWITCH_STATE_SYMBOL_BEGIN` (latched/persistent) rather than momentary, preventing sliding input gestures from accidentally reverting symbol pages on release.

---

### ISSUE-002: Ridmik-Parity Long-Press Cut, Copy, and Paste Shortcuts
- **Status**: RESOLVED & VERIFIED (Build 1.0.2)
- **Reported By**: User ("copy by c, cut by x and not working like ridmic.. note them i will give you some more feedback")
- **Description**:
  - Ridmik Keyboard signature feature:
    - Long-press on `C` $\rightarrow$ Copy selected text, or fallback to all text buffer (`beforeCursor + afterCursor`).
    - Long-press on `X` $\rightarrow$ Cut selected text, or fallback to all text buffer (`deleteSurroundingText`).
    - Long-press on `V` $\rightarrow$ Paste from clipboard.
- **Root Cause & Architectural Resolution**:
  - `Key.java`: Added `isCutKey()`, `isCopyKey()`, and `isPasteKey()` to recognize physical X, C, V positions across English QWERTY (`x, c, v`), Bengali Avro (`x, c, v`), Bengali Probhat (`শ/ঢ়, চ/ছ, আ/ঋ`), and Bengali Jatiyo (`ো/ৌ, ে/ৈ, র`), enabling long-press timers for all of them.
  - `PointerTracker.java`: Dispatches `CUSTOM_CODE_CUT`, `CUSTOM_CODE_COPY`, and `CUSTOM_CODE_PASTE` on long-press before any more-keys panels.
  - `LatinIMELegacy.java`: Implemented full buffer fallback logic via `getTextBeforeCursor` + `getTextAfterCursor` and `deleteSurroundingText(lenBefore, lenAfter)` with haptic feedback and Toast notifications ("Copied", "Cut", "Pasted").

---

### ISSUE-005: Caps Lock Synchronization and Glitch Resolution
- **Status**: RESOLVED & VERIFIED (Build 1.0.2)
- **Reported By**: User ("উল্টা কেপস লক করার নিতে গ্লিচ চলে আসছে")
- **Description**: Rapid layout flickering and failure to unshift when triggering Caps Lock via Shift double-tap or long-press.
- **Root Cause & Architectural Resolution**:
  - In `PointerTracker.java`, when `moreKeyCode == Constants.CODE_CAPSLOCK` triggers via long-press auto-more-key, `sListener.onReleaseKey(key.getCode(), false)` is explicitly invoked for the original `CODE_SHIFT` before switching to Caps Lock, preventing `mShiftKeyState` from staying permanently trapped in `STATE_PRESSING`.
  - In `KeyboardState.java`, `mShiftKeyState.onRelease()` is called when setting `CODE_CAPSLOCK`.
  - In `onPressShift()`, if `mAlphabetShiftState.isShiftLocked()` is active, a single tap on Shift cleanly unlocks Caps Lock directly back to unshifted lowercase.

---

### ISSUE-003: Bengali Composing & Backspace Word Deletion Safeguard
- **Status**: IN-REVIEW
- **Description**: Ensure backspacing after space deletes only the space character without erasing the entire preceding Bengali word.

---

### ISSUE-004: Configurable Long-Press Delay Setting
- **Status**: PLANNED
- **Description**: Add a setting in Settings $\rightarrow$ Typing to adjust long-press duration (e.g. 150ms - 400ms).
