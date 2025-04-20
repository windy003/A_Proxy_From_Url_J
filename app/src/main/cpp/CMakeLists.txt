cmake_minimum_required(VERSION 3.10.2)
project(proxyclient)

# 添加日志库
find_library(log-lib log)

# 设置包含目录
include_directories(include)

# 添加源文件
add_library(proxyclient SHARED
    xray_jni.cpp
    utils.cpp
)

# 链接外部库
target_link_libraries(proxyclient
    ${CMAKE_SOURCE_DIR}/../jniLibs/${ANDROID_ABI}/libxray.so
    ${log-lib}
) 