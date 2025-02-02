cmake_minimum_required(VERSION 3.22.1)

project(trojan)

# 设置预编译库路径
set(PREBUILT_DIR ${CMAKE_CURRENT_SOURCE_DIR}/../jniLibs/${ANDROID_ABI})
set(OPENSSL_INCLUDE_DIR ${CMAKE_CURRENT_SOURCE_DIR}/include)

# 添加预编译的 OpenSSL 库
add_library(ssl SHARED IMPORTED)
set_target_properties(ssl PROPERTIES IMPORTED_LOCATION ${PREBUILT_DIR}/libssl_1_1.so)

add_library(crypto SHARED IMPORTED)
set_target_properties(crypto PROPERTIES IMPORTED_LOCATION ${PREBUILT_DIR}/libcrypto_1_1.so)

# 添加源文件
add_library(${PROJECT_NAME} SHARED
    trojan_jni.cpp
)

# 添加头文件路径
target_include_directories(${PROJECT_NAME} PRIVATE
    ${OPENSSL_INCLUDE_DIR}
)

# 查找系统库
find_library(log-lib log)

# 链接库
target_link_libraries(${PROJECT_NAME} 
    ssl
    crypto
    ${log-lib}
)

# 设置编译选项
target_compile_options(${PROJECT_NAME} PRIVATE
    -Wall
    -Werror
    -std=c++17
) 