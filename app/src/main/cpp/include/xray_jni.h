#ifndef XRAY_JNI_H
#define XRAY_JNI_H

#include <jni.h>
#include <android/log.h>
#include <string>

#ifdef __cplusplus
extern "C" {
#endif

// 日志宏定义
#define TAG "XrayJNI"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, TAG, __VA_ARGS__)
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, TAG, __VA_ARGS__)

// JNI 方法声明
JNIEXPORT jint JNICALL
Java_com_example_proxyclient_service_XrayService_initXray(
    JNIEnv* env, jobject thiz, jstring config);

JNIEXPORT jint JNICALL
Java_com_example_proxyclient_service_XrayService_startXray(
    JNIEnv* env, jobject thiz);

JNIEXPORT void JNICALL
Java_com_example_proxyclient_service_XrayService_stopXray(
    JNIEnv* env, jobject thiz);

JNIEXPORT jboolean JNICALL
Java_com_example_proxyclient_service_XrayService_isXrayRunning(
    JNIEnv* env, jobject thiz);

#ifdef __cplusplus
}
#endif

#endif // XRAY_JNI_H 