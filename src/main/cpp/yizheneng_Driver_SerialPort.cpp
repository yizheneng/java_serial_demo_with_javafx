#include "yizheneng_Driver_SerialPort.h"
#include "Logger.h"
#include "serial.h"

using namespace std;

class SerialPort
{
public:
    SerialPort();
    ~SerialPort();

    bool open(std::string portName, int baud);

    int sendData(uint8_t* data, int16_t length);

    int readData(uint8_t* dataBuf, int16_t bufSize);

    void close();

    bool isOpened();

    std::string getError() {
        std::lock_guard<std::mutex> lk(serialPortErrorStringMutex);
        return errorString;
    }

private:
    void setError(std::string msg) {
        std::lock_guard<std::mutex> lk(serialPortErrorStringMutex);
        errorString = msg;
    }

    serial::Serial serial;
    std::string errorString;

    std::mutex serialPortMutex;
    std::mutex serialPortErrorStringMutex;
};

SerialPort::SerialPort(/* args */)
{
}

SerialPort::~SerialPort()
{
}

bool SerialPort::open(std::string portName, int baud)
{
  LOG_INFO("Serial port:%s", portName.c_str());

  std::lock_guard<std::mutex> lk(serialPortMutex);

  if(serial.isOpen()) {
    serial.close();
  }

  try {
      serial.setPort(portName.c_str());
      serial.setBaudrate(baud);
      serial::Timeout to = serial::Timeout::simpleTimeout(5);
      serial.setTimeout(to);
      serial.open();
  } catch(serial::IOException& e) {
	  LOG_ERROR("Open serial port exception:%s", e.what());
	  setError(e.what());
      return false;
  }

  if(!serial.isOpen()) {
	  LOG_ERROR("Open serial port error!!");
      return false;
  } else {
	  LOG_INFO("Open serial port succeed!!");
  }

  return true;
}

int SerialPort::sendData(uint8_t* data, int16_t length)
{
    serialPortMutex.lock();
    try {
        int len = serial.write((uint8_t*)data, length);
        serialPortMutex.unlock();
        return len;
    } catch(serial::PortNotOpenedException& e) {
		LOG_ERROR("Send data error, serial not opened!");
		setError(e.what());
    } catch(serial::IOException& e) {
        LOG_ERROR("Send data error, serial port closed!");
        setError(e.what());
    } catch (serial::SerialException& e) {
        LOG_ERROR("Send data error, serial port disconnected, %s", e.what());
        setError(e.what());
    }
    serialPortMutex.unlock();
    close();
    return 0;
}

int SerialPort::readData(uint8_t* dataBuf, int16_t bufSize)
{
    serialPortMutex.lock();
    try {
        int len = serial.read(dataBuf, bufSize);
        serialPortMutex.unlock();
        return len;
    } catch(serial::IOException& e) {
        LOG_ERROR("Receive data error, serial port closed, %s", e.what());
        setError(e.what());
    } catch (serial::SerialException& e) {
        LOG_ERROR("Receive data error, serial port disconnected, %s", e.what());
        setError(e.what());
        return 0;
    }
    serialPortMutex.unlock();
    close();
    return 0;
}

void SerialPort::close()
{
    std::lock_guard<std::mutex> lk(serialPortMutex);
    serial.close();
}

bool SerialPort::isOpened()
{
    std::lock_guard<std::mutex> lk(serialPortMutex);
    return serial.isOpen();
}


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
JNIEXPORT jboolean JNICALL Java_yizheneng_Driver_SerialPort_openCPP
  (JNIEnv *env, jclass, jstring portName, jint baud, jlong pointer) {
    const char* portNameP = (env)->GetStringUTFChars(portName, 0);
    LOG_INFO("Open serial port:%s", portNameP);
    return ((SerialPort*)pointer)->open(string(portNameP), baud);
}

/*
 * Class:     yizheneng_Driver_SerialPort
 * Method:    isOpened
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_yizheneng_Driver_SerialPort_isOpenedCPP
  (JNIEnv *, jclass, jlong pointer) {
	return ((SerialPort*)pointer)->isOpened();
}

/*
 * Class:     yizheneng_Driver_SerialPort
 * Method:    close
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_yizheneng_Driver_SerialPort_closeCPP
  (JNIEnv *, jclass, jlong pointer) {
    ((SerialPort*)pointer)->close();
}

/*
 * Class:     yizheneng_Driver_SerialPort
 * Method:    readData
 * Signature: ()[B
 */
#define RECEIVE_BUF_SIZE 255
uint8_t receiveBuf[RECEIVE_BUF_SIZE];
JNIEXPORT jbyteArray JNICALL Java_yizheneng_Driver_SerialPort_readDataCPP
  (JNIEnv *env, jclass, jlong pointer) {
    int length = ((SerialPort*)pointer)->readData(receiveBuf, RECEIVE_BUF_SIZE);
	jbyteArray result = env->NewByteArray(length);
    env->SetByteArrayRegion(result, 0, length, (const jbyte*)receiveBuf);;
	return result;
}

/*
 * Class:     yizheneng_Driver_SerialPort
 * Method:    send
 * Signature: ([B)V
 */
JNIEXPORT void JNICALL Java_yizheneng_Driver_SerialPort_sendCPP
  (JNIEnv *env, jclass, jbyteArray data, jlong pointer) {
    jsize len = env->GetArrayLength(data);
    jbyte *jbarray = (jbyte *)malloc(len * sizeof(jbyte));
    env->GetByteArrayRegion(data, 0, len, jbarray);

    ((SerialPort*)pointer)->sendData((uint8_t*)jbarray, len);
    free(jbarray);
}

JNIEXPORT jstring JNICALL Java_yizheneng_Driver_SerialPort_getErrorCPP
  (JNIEnv *env, jclass, jlong pointer) {
    return env->NewStringUTF(((SerialPort*)pointer)->getError().c_str());
}

JNIEXPORT jlong JNICALL Java_yizheneng_Driver_SerialPort_newSerialPortCPP
  (JNIEnv *, jclass) {
    return (jlong)(new SerialPort());
}

JNIEXPORT void JNICALL Java_yizheneng_Driver_SerialPort_deleteSerialPortCPP
  (JNIEnv *, jclass, jlong pointer) {
    delete ((SerialPort*)pointer);
}
