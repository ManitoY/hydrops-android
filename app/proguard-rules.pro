# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/shengwei.yi/Documents/android-studio-sdk/sdk/tools/proguard/proguard-android.txt
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
-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-dontoptimize
-verbose
-ignorewarning
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
-dontwarn

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService
-keep public class * extends android.os.IInterface

-keepclasseswithmembers class * {
    native <methods>;
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

-keepclassmembers class * {
    public <init>(org.json.JSONObject);
}

-keep class **.R$*{
	*;
}

-dontwarn cn.jpush.**
-keep class cn.jpush.** { *; }
-keep class com.edu.zwu.hydrops.entity.** { *; }
-keep class com.edu.zwu.hydrops.bmob.** { *; }


-keep class com.google.gson.** { *; }
-keepclassmembers class * implements java.io.Serializable {
    *;
}


-keepclassmembers class com.edu.zwu.hydrops.util.WebViewUtil$*{
    *;
}

-keepattributes Signature
-keepattributes *Annotation*
-keep class sun.misc.Unsafe { *; }

-keep class com.baidu.** { *; }
-keep class com.baidu.platform.** { *; }
-keep class com.baidu.location.** { *; }
-keep class com.baidu.vi.** { *; }
-keep class vi.com.gdi.bgl.android.** { *; }

-keep class com.edu.zwu.hydrops.base.** { *; }
-keepclasseswithmembernames class * {
    @com.edu.zwu.watermonitor.* <fields>;
}

-dontwarn retrofit.**
-keep class retrofit.** { *; }
-keepattributes Signature
-keepattributes Exceptions
-keepattributes EnclosingMethod


-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }

-keep class cn.bmob.v3.** { *; }
-keep class cn.bmob.v3.BmobQuery.** { *; }
-keep class cn.bmob.v3.exception.** { *; }
-keep class cn.bmob.v3.listener.** { *; }
-keep class cn.bmob.v3.datatype.** { *; }


-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}

-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}

-keepclassmembers class ** {
    public void onEvent(**);
}

# LeakCanary
-keep class org.eclipse.mat.** { *; }
-keep class com.squareup.leakcanary.** { *; }

-keep public class * implements com.bumptech.glide.module.GlideModule

-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
    **[] $VALUES;
    public *;
}


-keepattributes SourceFile,LineNumberTable

-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
    long producerIndex;
    long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}