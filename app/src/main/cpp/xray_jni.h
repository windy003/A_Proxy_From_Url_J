#ifndef XRAY_JNI_H
#define XRAY_JNI_H

#include <jni.h>

#ifdef __cplusplus
extern "C" {
#endif

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