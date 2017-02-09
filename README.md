# AgingTest
由于是在Eclipse中开发，无法使用 android:sharedUserId="android.uid.system" ， 
导致目前存在无法正常使用rebootTest。
但是可以在Android 源码环境中，通过Android.mk文件进行编译，此时编译过的apk是可以正常使用所有功能的。

主要功能：
------
（1）Video测试；<br>
（2）3D动画测试；<br>
（3）Audio测试；<br>
（4）LCD测试；<br>
（5）2D动画测试；<br>
（6）录像测试（前摄和后摄交替开启录像30分钟的测试）；<br>
（7）重启手机的测试（需要系统权限）；<br>
以上测试项，默认测试均为30分钟，可以在开始测试前更改测试时间和测试项。
