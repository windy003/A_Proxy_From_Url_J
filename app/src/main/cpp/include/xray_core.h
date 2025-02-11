#ifndef XRAY_CORE_H
#define XRAY_CORE_H

#ifdef __cplusplus
extern "C" {
#endif

// Xray 核心功能接口声明
int StartXray(const char* config);
void StopXray();
int IsXrayRunning();

#ifdef __cplusplus
}
#endif

#endif // XRAY_CORE_H 