#ifndef TROJAN_JNI_H
#define TROJAN_JNI_H

#include <jni.h>

#ifdef __cplusplus
extern "C" {
#endif

JNIEXPORT jint JNICALL
Java_com_example_proxyclient_service_TrojanService_initTrojan(
    JNIEnv* env, jobject thiz, jstring config);

JNIEXPORT jint JNICALL
Java_com_example_proxyclient_service_TrojanService_startTrojan(
    JNIEnv* env, jobject thiz);

JNIEXPORT void JNICALL
Java_com_example_proxyclient_service_TrojanService_stopTrojan(
    JNIEnv* env, jobject thiz);

#ifdef __cplusplus
}
#endif

#endif // TROJAN_JNI_H 