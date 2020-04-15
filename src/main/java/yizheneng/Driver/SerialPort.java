package yizheneng.Driver;

public class SerialPort {
    public native static String[] listPorts();

    private native static boolean openCPP(String portName, int baud, long pointer);

    private native static boolean isOpenedCPP(long pointer);

    private native static void closeCPP(long pointer);

    private native static byte[] readDataCPP(long pointer);

    private native static void sendCPP(byte[] data, long pointer);

    private native static String getErrorCPP(long pointer);

    private native static long newSerialPortCPP();

    private native static void deleteSerialPortCPP(long pointer);

    private long cSerialPointer = 0;

    public SerialPort() {
        cSerialPointer = newSerialPortCPP();
    }

    public boolean open(String portName, int baud) {
        return openCPP(portName, baud, cSerialPointer);
    }

    public boolean isOpened() {
        return isOpenedCPP(cSerialPointer);
    }

    public void close() {
        closeCPP(cSerialPointer);
    }

    public byte[] readData() {
        return readDataCPP(cSerialPointer);
    }

    public void send(byte[] data) {
        sendCPP(data, cSerialPointer);
    }

    public String getError() {
        return getErrorCPP(cSerialPointer);
    }

    /**
     * After using this method this instance should not be use
     */
    public void delete() {
        deleteSerialPortCPP(cSerialPointer);
        cSerialPointer = 0;
    }
}
