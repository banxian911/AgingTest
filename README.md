# AgingTest
由于是在Eclipse中开发，无法使用 android:sharedUserId="android.uid.system" ， 
导致目前存在无法正常使用rebootTest和Play3DTest。
但是可以在Android 源码环境中，通过Android.mk文件进行编译，此时编译过的apk是可以正常使用所有功能的。
