LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := rotary_encoder
LOCAL_SRC_FILES := rotary_encoder.c

LOCAL_CFLAGS    += -UNDEBUG

LOCAL_LDLIBS    := -ldl -llog

include $(BUILD_SHARED_LIBRARY)
