LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := pizzaJNI
LOCAL_SRC_FILES := pizzaJNI.c

include $(BUILD_SHARED_LIBRARY)
