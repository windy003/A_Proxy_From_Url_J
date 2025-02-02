#include "trojan_jni.h"
#include <string>
#include <android/log.h>

#define LOG_TAG "TrojanJNI"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)

static bool is_running = false;

extern "C" {
    JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM* vm, void* reserved) {
        LOGI("JNI_OnLoad called");
        return JNI_VERSION_1_6;
    }
}

JNIEXPORT jint JNICALL
Java_com_example_proxyclient_service_TrojanService_initTrojan(
    JNIEnv* env, jobject thiz, jstring config) {
    
    LOGI("initTrojan called");
    
    if (!config) {
        LOGE("Config is null");
        return -1;
    }
    
    const char* config_str = env->GetStringUTFChars(config, nullptr);
    if (!config_str) {
        LOGE("Failed to get config string");
        return -1;
    }
    
    LOGI("Initializing Trojan with config: %s", config_str);
    env->ReleaseStringUTFChars(config, config_str);
    return 0;
}

JNIEXPORT jint JNICALL
Java_com_example_proxyclient_service_TrojanService_startTrojan(
    JNIEnv* env, jobject thiz) {
    
    LOGI("startTrojan called");
    
    if (is_running) {
        LOGI("Trojan is already running");
        return 0;
    }
    
    try {
        LOGI("Starting Trojan");
        is_running = true;
        return 0;
    } catch (const std::exception& e) {
        LOGE("Exception in startTrojan: %s", e.what());
        return -1;
    } catch (...) {
        LOGE("Unknown exception in startTrojan");
        return -1;
    }
}

JNIEXPORT void JNICALL
Java_com_example_proxyclient_service_TrojanService_stopTrojan(
    JNIEnv* env, jobject thiz) {
    
    LOGI("stopTrojan called");
    
    if (is_running) {
        LOGI("Stopping Trojan");
        is_running = false;
    }
} 