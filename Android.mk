LOCAL_PATH:= $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE_TAGS := optional

LOCAL_STATIC_JAVA_LIBRARIES := android-support-v4 android-support-v13

LOCAL_SRC_FILES := $(call all-subdir-java-files)

LOCAL_PACKAGE_NAME := AgingTest
LOCAL_CERTIFICATE := platform
LOCAL_DEX_PREOPT:= false

include $(BUILD_PACKAGE)
