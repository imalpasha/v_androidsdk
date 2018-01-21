# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in W:\Development\AndroidSDK/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

-dontwarn com.senstation.android.pincast.Pincast

-keep public class com.vav.cn.directhelper.VavDirectGenerator
-keep public class com.vav.cn.listener.OnLoginCallbackSuccess
-keep public class com.vav.cn.listener.OnCloseFragmentListener
-keep public class com.vav.cn.application.ApplicationHelper
-keep public class com.vav.cn.application.ApplicationHelper
-keep public class com.vav.cn.model.ErrorInfo
-keep public class com.vav.cn.listener.LoginCallback
-keep public class com.vav.cn.listener.LogoutCallback
-keep public class com.vav.cn.model.DeploymentType

-keepclassmembers class com.vav.cn.model.DeploymentType {
    *;
}

-keepclassmembers class com.vav.cn.listener.LoginCallback {
     <methods>;
}

-keepclassmembers class com.vav.cn.listener.LogoutCallback {
     <methods>;
}

-keepclassmembers class com.vav.cn.directhelper.VavDirectGenerator {
    public <methods>;
}

-keepclassmembers class com.vav.cn.listener.OnLoginCallbackSuccess {
    public <methods>;
}

-keepclassmembers class com.vav.cn.listener.OnCloseFragmentListener {
    public <methods>;
}

-keepclassmembers class com.vav.cn.application.ApplicationHelper {
    public <methods>;
}

-keepclassmembers class **.R$* {
    public static <fields>;
}

-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-verbose
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
-keepattributes *Annotation*

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService
-keep public class * implements java.io.Serializable
-keep public class * extends android.app.Fragment
-keepattributes EnclosingMethod

-keepattributes Signature

# For using GSON @Expose annotation
-keepattributes *Annotation*

# Gson specific classes
-keep class sun.misc.Unsafe { *; }
#-keep class com.google.gson.stream.** { *; }

# Application classes that will be serialized/deserialized over Gson
-keep class com.vav.cn.server.model.** { *; }
-keep class com.vav.cn.model.** { *; }

-keep class com.senstation.** { *; }


-dontwarn rx.**

-dontwarn okio.**

-dontwarn com.squareup.okhttp.**
-keep class com.squareup.okhttp.** { *; }
-keep interface com.squareup.okhttp.** { *; }

-dontwarn retrofit.**
-dontwarn retrofit.appengine.UrlFetchClient
-keep class retrofit.** { *; }
-keepclasseswithmembers class * {
    @retrofit.http.* <methods>;
}