-dontobfuscate
-dontoptimize
-keepattributes *
-keep class app.revanced.** {
  *;
}
-keep class com.google.** {
  *;
}
-keep class org.mozilla.** {
  *;
}
-dontwarn java.awt.**
-dontwarn javax.swing.**
