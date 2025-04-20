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

// Xray操作函数，通过FFI调用Go导出的函数
extern "C" {
    extern int StartXrayWithConfig(const char* config);
    extern void StopXrayInstance();
    extern int CheckXrayStatus();
}

// 实现Xray启动函数
int StartXray(const char* config) {
    LOGI("Starting Xray with config: %s", config);
    return StartXrayWithConfig(config);
}

void StopXray() {
    LOGI("Stopping Xray");
    StopXrayInstance();
}

int IsXrayRunning() {
    return CheckXrayStatus();
}

// JNI接口实现
JNIEXPORT jint JNICALL
Java_com_example_proxyclient_service_XrayService_initXray(JNIEnv *env, jobject thiz, jstring config) {
    // 将Java字符串转为C++字符串
    const char *config_str = env->GetStringUTFChars(config, nullptr);
    if (config_str == nullptr) {
        LOGE("Failed to get config string");
        return -1;
    }
    
    // 保存配置字符串
    config_string = config_str;
    
    // 释放Java字符串
    env->ReleaseStringUTFChars(config, config_str);
    
    LOGI("Xray initialized with config");
    return 0;
}

JNIEXPORT jint JNICALL
Java_com_example_proxyclient_service_XrayService_startXray(JNIEnv *env, jobject thiz) {
    if (is_running) {
        LOGI("Xray is already running");
        return 1;
    }
    
    // 在新线程中启动Xray，避免阻塞主线程
    proxy_thread = std::thread([&]() {
        LOGI("Starting Xray in a new thread");
        int result = StartXray(config_string.c_str());
        if (result != 0) {
            LOGE("Failed to start Xray, error code: %d", result);
            is_running = false;
        } else {
            LOGI("Xray started successfully");
            is_running = true;
        }
    });
    
    // 分离线程，让它在后台运行
    proxy_thread.detach();
    return 0;
}

JNIEXPORT void JNICALL
Java_com_example_proxyclient_service_XrayService_stopXray(JNIEnv *env, jobject thiz) {
    if (!is_running) {
        LOGI("Xray is not running");
        return;
    }
    
    // 停止Xray
    StopXray();
    is_running = false;
    LOGI("Xray stopped");
}

JNIEXPORT jboolean JNICALL
Java_com_example_proxyclient_service_XrayService_isXrayRunning(JNIEnv *env, jobject thiz) {
    // 可以使用导出的CheckXrayStatus函数检查状态
    // 这里我们使用本地变量
    return is_running ? JNI_TRUE : JNI_FALSE;
}

extern "C" {
} // extern "C" 