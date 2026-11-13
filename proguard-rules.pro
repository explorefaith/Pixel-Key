# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-keepclasseswithmembernames,includedescriptorclasses class * {
    native <methods>;
}

-keepclasseswithmembers class * {
    native <methods>;
}
# Keep classes and methods that have the @UsedForTesting annotation
-keep @com.explorefaith.pixelkey.annotations.UsedForTesting class *
-keepclassmembers class * {
    @com.explorefaith.pixelkey.annotations.UsedForTesting *;
}

# Keep classes and methods that have the @ExternallyReferenced annotation
-keep @com.explorefaith.pixelkey.annotations.ExternallyReferenced class *
-keepclassmembers class * {
    @com.explorefaith.pixelkey.annotations.ExternallyReferenced *;
}

# Keep native methods
-keepclassmembers class * {
    native <methods>;
}

# Keep classes that are used as a parameter type of methods that are also marked as keep
# to preserve changing those methods' signature.
-keep class com.explorefaith.pixelkey.latin.AssetFileAddress
-keep class com.explorefaith.pixelkey.latin.Dictionary
-keep class com.explorefaith.pixelkey.latin.NgramContext
-keep class com.explorefaith.pixelkey.latin.makedict.ProbabilityInfo
-keep class com.explorefaith.pixelkey.keyboard.KeyboardLayout { *; }

-keep class org.tensorflow.lite.Interpreter** { *; }
-keep class org.futo.pocketfft.** { *; }

-dontobfuscate
-optimizations !code/allocation/variable