# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
#fqcn.of.javascript.interface.for.webview
# class:
-keepclassmembers class inc.pir.buyexpress.WebAppInterface{
   public *;
}
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-verbose
-dontnote
-dontwarn
#-dontshrink
#-dontoptimize
# keep the class and specified members from being removed or renamed
-keep class inc.pir.buyexpress.MainActivity { void onCreate(android.os.Bundle); }
-keepattributes !LocalVariableTable,!LocalVariableTypeTable
# Also keep - Enumerations. Keep the special static methods that are required in
# enumeration classes.
-keepclassmembers enum  * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
# Keep serializable classes & fields
-keep class ** extends java.io.Serializable {
    <fields>;
}
# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
