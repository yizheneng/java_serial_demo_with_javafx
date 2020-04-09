#include "yizheneng_Utils_JniLogger.h"

JNIEXPORT void JNICALL Java_yizheneng_Utils_JniLogger_loggerToJni
  (JNIEnv *env, jclass, jint level, jstring log) {
  const jchar* jcstr = (env)->GetStringChars(log, 0);
  Logger::instance()->logToFile((LoggerLevel)level, (const char *)jcstr, "java", 0);
}
