#include "trojan_jni.h"
#include <string>
#include <thread>
#include <android/log.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <unistd.h>
#include <fcntl.h>
#include <errno.h>

#define LOG_TAG "TrojanJNI"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)

static bool is_running = false;
static std::string config_string;
static std::thread proxy_thread;

// 添加 JNI 全局引用
static JavaVM* g_vm = nullptr;
static jclass g_service_class = nullptr;
static jobject g_service_object = nullptr;
static jmethodID g_protect_socket_method = nullptr;

// 保护 socket 的函数
void protect_socket(int fd) {
    JNIEnv* env;
    if (g_vm->AttachCurrentThread(&env, nullptr) == JNI_OK) {
        env->CallVoidMethod(g_service_object, g_protect_socket_method, fd);
        if (env->ExceptionCheck()) {
            env->ExceptionDescribe();
            env->ExceptionClear();
        }
    }
}

// 代理线程函数
void proxy_worker() {
    LOGI("Proxy worker started");
    
    // 创建本地 SOCKS5 服务器
    int server_fd = socket(AF_INET, SOCK_STREAM, 0);
    if (server_fd < 0) {
        LOGE("Failed to create socket: %s", strerror(errno));
        return;
    }
    
    // 保护 socket
    protect_socket(server_fd);
    
    // 设置地址重用
    int opt = 1;
    if (setsockopt(server_fd, SOL_SOCKET, SO_REUSEADDR, &opt, sizeof(opt)) < 0) {
        LOGE("Failed to set SO_REUSEADDR: %s", strerror(errno));
        close(server_fd);
        return;
    }
    
    // 绑定地址
    struct sockaddr_in addr;
    addr.sin_family = AF_INET;
    addr.sin_addr.s_addr = inet_addr("127.0.0.1");
    addr.sin_port = htons(1080);
    
    if (bind(server_fd, (struct sockaddr *)&addr, sizeof(addr)) < 0) {
        LOGE("Failed to bind: %s", strerror(errno));
        close(server_fd);
        return;
    }
    
    // 开始监听
    if (listen(server_fd, SOMAXCONN) < 0) {
        LOGE("Failed to listen: %s", strerror(errno));
        close(server_fd);
        return;
    }
    
    LOGI("SOCKS5 server listening on 127.0.0.1:1080");
    
    while (is_running) {
        // 接受连接
        struct sockaddr_in client_addr;
        socklen_t client_len = sizeof(client_addr);
        int client_fd = accept(server_fd, (struct sockaddr *)&client_addr, &client_len);
        
        if (client_fd < 0) {
            if (errno != EINTR) {
                LOGE("Failed to accept: %s", strerror(errno));
            }
            continue;
        }
        
        // TODO: 处理 SOCKS5 协议并转发流量
        LOGI("New client connected");
        close(client_fd);
    }
    
    close(server_fd);
    LOGI("Proxy worker stopped");
}

extern "C" {
    JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM* vm, void* reserved) {
        LOGI("JNI_OnLoad called");
        g_vm = vm;
        
        JNIEnv* env;
        if (vm->GetEnv(reinterpret_cast<void**>(&env), JNI_VERSION_1_6) != JNI_OK) {
            return JNI_ERR;
        }
        
        return JNI_VERSION_1_6;
    }

    JNIEXPORT jint JNICALL
    Java_com_example_proxyclient_service_TrojanService_initTrojan(
        JNIEnv* env, jobject thiz, jstring config) {
        
        // 保存 service 对象的全局引用
        if (g_service_object == nullptr) {
            g_service_class = static_cast<jclass>(env->NewGlobalRef(env->GetObjectClass(thiz)));
            g_service_object = env->NewGlobalRef(thiz);
            g_protect_socket_method = env->GetMethodID(g_service_class, "protectSocket", "(I)V");
        }
        
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
        
        config_string = config_str;
        env->ReleaseStringUTFChars(config, config_str);
        
        LOGI("Trojan initialized with config: %s", config_string.c_str());
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
            is_running = true;
            proxy_thread = std::thread(proxy_worker);
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
            is_running = false;
            if (proxy_thread.joinable()) {
                proxy_thread.join();
            }
            LOGI("Trojan stopped");
        }
    }
} 