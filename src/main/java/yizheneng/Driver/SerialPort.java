package yizheneng.Driver;

public class SerialPort {
    public native static String[] listPorts();

    public native static boolean open();

    public native static boolean isOpened();

    public native static void close();

    public native static byte[] readData();

    public native static void send(byte[] data);
}
