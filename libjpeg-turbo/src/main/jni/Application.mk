LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)
NDK_PROJECT_PATH=$HOME/Library/Android/sdk/ndk-bundle
NDK_TOOLCHAIN_VERSION := 4.9
APP_PLATFORM := android-14
APP_ABI := armeabi,armeabi-v7a,arm64-v8a,x86
APP_OPTIM := release
APP_BUILD_SCRIPT := $(LOCAL_PATH)/Android.mk