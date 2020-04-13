/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class yizheneng_Driver_SerialPort */

#ifndef _Included_yizheneng_Driver_SerialPort
#define _Included_yizheneng_Driver_SerialPort
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     yizheneng_Driver_SerialPort
 * Method:    listPorts
 * Signature: ()[Ljava/lang/String;
 */
JNIEXPORT jobjectArray JNICALL Java_yizheneng_Driver_SerialPort_listPorts
  (JNIEnv *, jclass);

/*
 * Class:     yizheneng_Driver_SerialPort
 * Method:    open
 * Signature: (Ljava/lang/String;I)Z
 */
JNIEXPORT jboolean JNICALL Java_yizheneng_Driver_SerialPort_open
  (JNIEnv *, jclass, jstring, jint);

/*
 * Class:     yizheneng_Driver_SerialPort
 * Method:    isOpened
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_yizheneng_Driver_SerialPort_isOpened
  (JNIEnv *, jclass);

/*
 * Class:     yizheneng_Driver_SerialPort
 * Method:    close
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_yizheneng_Driver_SerialPort_close
  (JNIEnv *, jclass);

/*
 * Class:     yizheneng_Driver_SerialPort
 * Method:    readData
 * Signature: ()[B
 */
JNIEXPORT jbyteArray JNICALL Java_yizheneng_Driver_SerialPort_readData
  (JNIEnv *, jclass);

/*
 * Class:     yizheneng_Driver_SerialPort
 * Method:    send
 * Signature: ([B)V
 */
JNIEXPORT void JNICALL Java_yizheneng_Driver_SerialPort_send
  (JNIEnv *, jclass, jbyteArray);

#ifdef __cplusplus
}
#endif
#endif
