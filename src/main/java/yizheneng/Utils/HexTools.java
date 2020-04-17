package yizheneng.Utils;

public class HexTools {
//    public byte[] stringToHex(String data) {
//        data.toCharArray()
//    }

    public static String bytesToHexString(byte[] bArray)
    {
        int length = bArray.length;
        StringBuffer sb = new StringBuffer(length);
        String sTemp;
        for (int i = 0; i < length; i++)
        {
            sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2)
                sb.append(0);
            sb.append(sTemp.toUpperCase() + " ");
        }
        return sb.toString();
    }

//    public static byte[] hexStringToByte(String hex)
//    {
//        int len = (hex.length() / 2);
//        byte[] result = new byte[len];
//        char[] achar = hex.toCharArray();
//
//        for (int i = 0; i < len; i++)
//        {
//            int pos = i * 2;
//            result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
//        }
//        return result;
//
//    }
}
