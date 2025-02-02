cmake_minimum_required(VERSION 3.10.2)

project(trojan)

# 设置 OpenSSL 路径
set(OPENSSL_ROOT_DIR ${CMAKE_CURRENT_SOURCE_DIR}/deps/openssl)
set(OPENSSL_INCLUDE_DIR ${OPENSSL_ROOT_DIR}/include)

# 查找 Android 日志库
find_library(log-lib log)

# 添加 JNI 库
add_library(trojan-jni SHARED
    trojan_jni.cpp
)

target_include_directories(trojan-jni PRIVATE
    ${CMAKE_CURRENT_SOURCE_DIR}
    ${OPENSSL_INCLUDE_DIR}
)

target_link_libraries(trojan-jni
    ${log-lib}
) 