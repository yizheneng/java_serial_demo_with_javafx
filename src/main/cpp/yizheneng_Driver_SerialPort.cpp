#include "yizheneng_Driver_SerialPort.h"
#include "serial.h"

JNIEXPORT jobjectArray JNICALL Java_yizheneng_Driver_SerialPort_listPorts
  (JNIEnv *env, jclass) {
    std::vector<serial::PortInfo> portsInfo = serial::list_ports();

    jobjectArray resultMsg = env->NewObjectArray(portsInfo.size(), env->FindClass("java/lang/String"), env->NewStringUTF(""));

    for(int i = 0; i < portsInfo.size(); i++) {
        env->SetObjectArrayElement(resultMsg, i, env->NewStringUTF(portsInfo[i].port.c_str()));
    }

    return resultMsg;
}

/*
 * Class:     yizheneng_Driver_SerialPort
 * Method:    open
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_yizheneng_Driver_SerialPort_open
  (JNIEnv *, jclass) {
	return true;
}

/*
 * Class:     yizheneng_Driver_SerialPort
 * Method:    isOpened
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_yizheneng_Driver_SerialPort_isOpened
  (JNIEnv *, jclass) {
	return true;
}

/*
 * Class:     yizheneng_Driver_SerialPort
 * Method:    close
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_yizheneng_Driver_SerialPort_close
  (JNIEnv *, jclass) {

}

/*
 * Class:     yizheneng_Driver_SerialPort
 * Method:    readData
 * Signature: ()[B
 */
JNIEXPORT jbyteArray JNICALL Java_yizheneng_Driver_SerialPort_readData
  (JNIEnv *, jclass) {
	jbyteArray result;
	return result;
}

/*
 * Class:     yizheneng_Driver_SerialPort
 * Method:    send
 * Signature: ([B)V
 */
JNIEXPORT void JNICALL Java_yizheneng_Driver_SerialPort_send
  (JNIEnv *, jclass, jbyteArray) {

}