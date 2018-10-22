LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

SOURCE_PATH := libjpeg-turbo-1.5.2


# libjpeg_la_SOURCES from Makefile.am
LOCAL_SRC_FILES += \
	$(SOURCE_PATH)/jcapimin.c \
	$(SOURCE_PATH)/jcapistd.c \
	$(SOURCE_PATH)/jccoefct.c \
	$(SOURCE_PATH)/jccolor.c \
	$(SOURCE_PATH)/jcdctmgr.c \
	$(SOURCE_PATH)/jchuff.c \
	$(SOURCE_PATH)/jcinit.c \
	$(SOURCE_PATH)/jcmainct.c \
	$(SOURCE_PATH)/jcmarker.c \
	$(SOURCE_PATH)/jcmaster.c \
	$(SOURCE_PATH)/jcomapi.c \
	$(SOURCE_PATH)/jcparam.c \
	$(SOURCE_PATH)/jcphuff.c \
	$(SOURCE_PATH)/jcprepct.c \
	$(SOURCE_PATH)/jcsample.c \
	$(SOURCE_PATH)/jctrans.c \
	$(SOURCE_PATH)/jdapimin.c \
	$(SOURCE_PATH)/jdapistd.c \
	$(SOURCE_PATH)/jdatadst.c \
	$(SOURCE_PATH)/jdatasrc.c \
	$(SOURCE_PATH)/jdcoefct.c \
	$(SOURCE_PATH)/jdcolor.c \
	$(SOURCE_PATH)/jddctmgr.c \
	$(SOURCE_PATH)/jdhuff.c \
	$(SOURCE_PATH)/jdinput.c \
	$(SOURCE_PATH)/jdmainct.c \
	$(SOURCE_PATH)/jdmarker.c \
	$(SOURCE_PATH)/jdmaster.c \
	$(SOURCE_PATH)/jdmerge.c \
	$(SOURCE_PATH)/jdphuff.c \
	$(SOURCE_PATH)/jdpostct.c \
	$(SOURCE_PATH)/jdsample.c \
	$(SOURCE_PATH)/jdtrans.c \
	$(SOURCE_PATH)/jerror.c \
	$(SOURCE_PATH)/jfdctflt.c \
	$(SOURCE_PATH)/jfdctfst.c \
	$(SOURCE_PATH)/jfdctint.c \
	$(SOURCE_PATH)/jidctflt.c \
	$(SOURCE_PATH)/jidctfst.c \
	$(SOURCE_PATH)/jidctint.c \
	$(SOURCE_PATH)/jidctred.c \
	$(SOURCE_PATH)/jquant1.c \
	$(SOURCE_PATH)/jquant2.c \
	$(SOURCE_PATH)/jutils.c \
	$(SOURCE_PATH)/jmemmgr.c \
	$(SOURCE_PATH)/jmemnobs.c \

# if WITH_ARITH_ENC from Makefile.am
LOCAL_SRC_FILES += \
	$(SOURCE_PATH)/jaricom.c \
	$(SOURCE_PATH)/jcarith.c \
	$(SOURCE_PATH)/jdarith.c \

# libturbojpeg_la_SOURCES from Makefile.am
LOCAL_SRC_FILES += \
	$(SOURCE_PATH)/turbojpeg.c \
	$(SOURCE_PATH)/transupp.c \
	$(SOURCE_PATH)/jdatadst-tj.c \
	$(SOURCE_PATH)/jdatasrc-tj.c \

ifeq ($(TARGET_ARCH_ABI),armeabi)
LOCAL_SRC_FILES += $(SOURCE_PATH)/jsimd_none.c

LOCAL_CFLAGS += \
	-DSIZEOF_SIZE_T=4 \

else ifeq ($(TARGET_ARCH_ABI),armeabi-v7a)
# ARM v7 NEON
LOCAL_ARM_NEON := true
LOCAL_CFLAGS += -D__ARM_HAVE_NEON__

LOCAL_SRC_FILES += \
	$(SOURCE_PATH)/simd/jsimd_arm.c \
	$(SOURCE_PATH)/simd/jsimd_arm_neon.S \

LOCAL_CFLAGS += \
	-DSIZEOF_SIZE_T=4 \

else ifeq ($(TARGET_ARCH_ABI),arm64-v8a)
# ARM v8 64-bit NEON
LOCAL_ARM_NEON := true
LOCAL_CFLAGS += -D__ARM_HAVE_NEON__

LOCAL_SRC_FILES += \
	$(SOURCE_PATH)/simd/jsimd_arm64.c \
	$(SOURCE_PATH)/simd/jsimd_arm64_neon.S \

LOCAL_CFLAGS += \
	-DSIZEOF_SIZE_T=8 \

else ifeq ($(TARGET_ARCH_ABI),x86)
# x86 MMX and SSE2
LOCAL_SRC_FILES += \
    $(SOURCE_PATH)/simd/jsimd_i386.c \
    $(SOURCE_PATH)/simd/jccolor-mmx.asm \
    $(SOURCE_PATH)/simd/jccolor-sse2.asm \
    $(SOURCE_PATH)/simd/jcgray-mmx.asm \
    $(SOURCE_PATH)/simd/jchuff-sse2.asm\
    $(SOURCE_PATH)/simd/jcgray-sse2.asm \
    $(SOURCE_PATH)/simd/jcsample-mmx.asm \
    $(SOURCE_PATH)/simd/jfdctflt-3dn.asm \
    $(SOURCE_PATH)/simd/jidctflt-3dn.asm \
    $(SOURCE_PATH)/simd/jquant-3dn.asm \
    $(SOURCE_PATH)/simd/jdcolor-mmx.asm \
    $(SOURCE_PATH)/simd/jdmerge-mmx.asm \
    $(SOURCE_PATH)/simd/jdsample-mmx.asm \
    $(SOURCE_PATH)/simd/jfdctfst-mmx.asm \
    $(SOURCE_PATH)/simd/jfdctint-mmx.asm \
    $(SOURCE_PATH)/simd/jidctfst-mmx.asm \
    $(SOURCE_PATH)/simd/jidctint-mmx.asm \
    $(SOURCE_PATH)/simd/jidctred-mmx.asm \
    $(SOURCE_PATH)/simd/jquant-mmx.asm \
    $(SOURCE_PATH)/simd/jfdctflt-sse.asm \
    $(SOURCE_PATH)/simd/jidctflt-sse.asm \
    $(SOURCE_PATH)/simd/jquant-sse.asm \
    $(SOURCE_PATH)/simd/jcsample-sse2.asm \
    $(SOURCE_PATH)/simd/jdcolor-sse2.asm \
    $(SOURCE_PATH)/simd/jdmerge-sse2.asm \
    $(SOURCE_PATH)/simd/jdsample-sse2.asm \
    $(SOURCE_PATH)/simd/jfdctfst-sse2.asm \
    $(SOURCE_PATH)/simd/jfdctint-sse2.asm \
    $(SOURCE_PATH)/simd/jidctflt-sse2.asm \
    $(SOURCE_PATH)/simd/jidctfst-sse2.asm \
    $(SOURCE_PATH)/simd/jidctint-sse2.asm \
    $(SOURCE_PATH)/simd/jidctred-sse2.asm \
    $(SOURCE_PATH)/simd/jquantf-sse2.asm \
    $(SOURCE_PATH)/simd/jquanti-sse2.asm \
    $(SOURCE_PATH)/simd/jsimdcpu.asm \

LOCAL_CFLAGS += \
     	-DSIZEOF_SIZE_T=4 \

LOCAL_ASMFLAGS += -DPIC -DELF

else ifeq ($(TARGET_ARCH_ABI),x86_64)
# x86-64 SSE2
LOCAL_SRC_FILES += \
	$(SOURCE_PATH)/simd/jsimd_x86_64.c \
	$(SOURCE_PATH)/simd/jfdctflt-sse-64.asm \
	$(SOURCE_PATH)/simd/jccolor-sse2-64.asm \
	$(SOURCE_PATH)/simd/jcgray-sse2-64.asm \
	$(SOURCE_PATH)/simd/jchuff-sse2-64.asm\
	$(SOURCE_PATH)/simd/jcsample-sse2-64.asm \
	$(SOURCE_PATH)/simd/jdcolor-sse2-64.asm \
	$(SOURCE_PATH)/simd/jdmerge-sse2-64.asm \
	$(SOURCE_PATH)/simd/jdsample-sse2-64.asm \
	$(SOURCE_PATH)/simd/jfdctfst-sse2-64.asm \
	$(SOURCE_PATH)/simd/jfdctint-sse2-64.asm \
	$(SOURCE_PATH)/simd/jidctflt-sse2-64.asm \
	$(SOURCE_PATH)/simd/jidctfst-sse2-64.asm \
	$(SOURCE_PATH)/simd/jidctint-sse2-64.asm \
	$(SOURCE_PATH)/simd/jidctred-sse2-64.asm \
	$(SOURCE_PATH)/simd/jquantf-sse2-64.asm \
	$(SOURCE_PATH)/simd/jquanti-sse2-64.asm \

LOCAL_CFLAGS += \
	-DSIZEOF_SIZE_T=8 \

LOCAL_ASMFLAGS += -D__x86_64__ -DPIC -DELF

endif

# Header file path
LOCAL_C_INCLUDES += \
    $(LOCAL_PATH)/include \
	$(LOCAL_PATH)/$(SOURCE_PATH)/simd \
	$(LOCAL_PATH)/$(SOURCE_PATH) \

LOCAL_EXPORT_C_INCLUDES := \
	$(LOCAL_PATH)/$(SOURCE_PATH) \

LOCAL_CFLAGS += -O3 -fstrict-aliasing
LOCAL_CFLAGS += -Wno-unused-parameter

# Build as a static library.
LOCAL_MODULE := libjpeg-turbo-static
include $(BUILD_STATIC_LIBRARY)
# Also build as a shared library.
include $(CLEAR_VARS)
LOCAL_EXPORT_C_INCLUDE_DIRS := $(LOCAL_PATH)/$(SOURCE_PATH)
LOCAL_WHOLE_STATIC_LIBRARIES = libjpeg-turbo-static
LOCAL_MODULE := libjpeg-turbo
include $(BUILD_SHARED_LIBRARY)
