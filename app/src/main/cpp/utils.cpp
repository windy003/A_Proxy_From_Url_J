#include "utils.h"
#include <string>

std::string jstring2string(JNIEnv* env, jstring jStr) {
    if (!jStr) {
        return "";
    }
    
    const char* str = env->GetStringUTFChars(jStr, nullptr);
    if (!str) {
        return "";
    }
    
    std::string result(str);
    env->ReleaseStringUTFChars(jStr, str);
    
    return result;
}

jstring string2jstring(JNIEnv* env, const std::string& str) {
    return env->NewStringUTF(str.c_str());
} 