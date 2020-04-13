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
private:
  serial::Serial serial;
  std::mutex serialPortMutex;
};

SerialPort serialPort;

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
  (JNIEnv *env, jclass, jstring portName, jint baud) {
    const jchar* portNameP = (env)->GetStringChars(portName, 0);
    LOG_INFO("Open serial port:%s", (char*)portNameP);
    return serialPort.open(string((char*)portNameP), baud);
}

/*
 * Class:     yizheneng_Driver_SerialPort
 * Method:    isOpened
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_yizheneng_Driver_SerialPort_isOpened
  (JNIEnv *, jclass) {
	return serialPort.isOpened();
}

/*
 * Class:     yizheneng_Driver_SerialPort
 * Method:    close
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_yizheneng_Driver_SerialPort_close
  (JNIEnv *, jclass) {
    serialPort.close();
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
      LOG_ERROR("Open serial port exception:%d", e.getErrorNumber());
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
  std::lock_guard<std::mutex> lk(serialPortMutex);
  return serial.write((uint8_t*)data, length);
}

int SerialPort::readData(uint8_t* dataBuf, int16_t bufSize)
{
  std::lock_guard<std::mutex> lk(serialPortMutex);
  return serial.read(dataBuf, bufSize);
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
