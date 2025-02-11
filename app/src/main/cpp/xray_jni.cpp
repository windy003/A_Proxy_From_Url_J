#include "xray_jni.h"
#include "xray_core.h"
#include "utils.h"
#include <string>
#include <thread>
#include <android/log.h>

#define LOG_TAG "XrayJNI"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)

static bool is_running = false;
static std::string config_string;
static std::thread proxy_thread;

// 临时实现，后续需要替换为真实的 Xray 实现
int StartXray(const char* config) {
    LOGI("Starting Xray with config: %s", config);
    return 0;
}

void StopXray() {
    LOGI("Stopping Xray");
}

int IsXrayRunning() {
    return is_running ? 1 : 0;
}

extern "C" {

JNIEXPORT jint JNICALL
Java_com_example_proxyclient_service_XrayService_initXray(
    JNIEnv* env, jobject thiz, jstring config) {
    if (is_running) {
        LOGE("Xray is already running");
        return -1;
    }
    
    config_string = jstring2string(env, config);
    LOGI("Initializing Xray with config: %s", config_string.c_str());
    
    return StartXray(config_string.c_str());
}

JNIEXPORT jint JNICALL
Java_com_example_proxyclient_service_XrayService_startXray(
    JNIEnv* env, jobject thiz) {
    if (is_running) {
        return 0;
    }
    
    is_running = true;
    return 0;
}

JNIEXPORT void JNICALL
Java_com_example_proxyclient_service_XrayService_stopXray(
    JNIEnv* env, jobject thiz) {
    if (!is_running) {
        return;
    }
    
    StopXray();
    is_running = false;
}

JNIEXPORT jboolean JNICALL
Java_com_example_proxyclient_service_XrayService_isXrayRunning(
    JNIEnv* env, jobject thiz) {
    return is_running;
}

} // extern "C" 