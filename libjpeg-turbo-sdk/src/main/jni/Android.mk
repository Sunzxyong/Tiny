LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

# LOCAL_MODULE := libjpeg-turbo
# LOCAL_SRC_FILES := lib/$(TARGET_ARCH_ABI)/libjpeg-turbo.so
LOCAL_MODULE := libjpeg-turbo-static
LOCAL_SRC_FILES := lib/$(TARGET_ARCH_ABI)/libjpeg-turbo-static.a

# include $(PREBUILT_SHARED_LIBRARY)
include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE    := tiny
# LOCAL_SHARED_LIBRARIES := libjpeg-turbo
LOCAL_STATIC_LIBRARIES := libjpeg-turbo-static
LOCAL_SRC_FILES := com_zxy_libjpegturbo_JpegTurboCompressor.c
LOCAL_LDLIBS := -ljnigraphics -llog
LOCAL_C_INCLUDES := $(LOCAL_PATH) \
                    $(LOCAL_PATH)/include \

include $(BUILD_SHARED_LIBRARY)