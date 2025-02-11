#ifndef UTILS_H
#define UTILS_H

#include <string>
#include <jni.h>

// 工具函数声明
std::string jstring2string(JNIEnv* env, jstring jStr);
jstring string2jstring(JNIEnv* env, const std::string& str);

#endif // UTILS_H 