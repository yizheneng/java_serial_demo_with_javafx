package yizheneng.Utils;

public class JniLogger {
    private static final int JNI_LOGGER_LEVEL_DEBUG = 0;
    private static final int JNI_LOGGER_LEVEL_INFO  = 1;
    private static final int JNI_LOGGER_LEVEL_WARN  = 2;
    private static final int JNI_LOGGER_LEVEL_ERROR = 3;

    public static void d (String tag, String msg) {
//        System.out.println("DEBUG:" + tag + "," + msg);
        loggerToJni(JNI_LOGGER_LEVEL_DEBUG, tag + ":" + msg);
    }

    public static void i (String tag,String msg) {
//        System.out.println("INFO:" + tag + "," + msg);
        loggerToJni(JNI_LOGGER_LEVEL_INFO, tag + ":" + msg);
    }

    public static void w (String tag,String msg) {
//        System.out.println("WARN:" + tag + "," + msg);
        loggerToJni(JNI_LOGGER_LEVEL_WARN, tag + ":" + msg);
    }

    public static void e (String tag,String msg) {
//        System.out.println("ERROR:" + tag + "," + msg);
        loggerToJni(JNI_LOGGER_LEVEL_ERROR, tag + ":" + msg);
    }

    public native static void loggerToJni(int level, String log);
}
