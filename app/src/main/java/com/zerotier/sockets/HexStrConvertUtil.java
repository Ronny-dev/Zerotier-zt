/*
 * 16进制与字符串相互转换的一些静态方法
 */
package com.zerotier.sockets;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

public class HexStrConvertUtil {

    /**
     * 字符串转换成16进制文字列的方法
     *
     * @param str
     * @return
     */
    public static String toHex(String str) {
        String hexString = "0123456789ABCDEF";
        byte[] bytes = null;
        try {
            bytes = str.getBytes("ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            System.out.println("byte数组转16进制出错！");
        }
        StringBuilder hex = new StringBuilder(bytes.length * 2);
        for (int i = 0; i < bytes.length; i++) {
            hex.append(hexString.charAt((bytes[i] & 0xf0) >> 4));  // 作用同 n / 16
            hex.append(hexString.charAt((bytes[i] & 0x0f) >> 0));  // 作用同 n
            hex.append(' ');  //中间用空格隔开
        }
        return hex.toString();
    }

    /**
     * byte 转换为string显示
     * 例如：0xaa 0xbb -> aabb显示。
     *
     * @param array
     * @return
     */
    public static String toHex(byte[] array) {
        if(array == null){
            return "";
        }
        String hexString = "0123456789ABCDEF";
        StringBuilder hex = new StringBuilder(array.length * 2);
        for (int i = 0; i < array.length; i++) {
            hex.append(hexString.charAt((array[i] & 0xf0) >> 4));  // 作用同 n / 16
            hex.append(hexString.charAt((array[i] & 0x0f) >> 0));  // 作用同 n
        }
        return hex.toString();
    }

    public static String toHex(short[] array) {
        if(array == null){
            return "";
        }
        String hexString = "0123456789ABCDEF";
        StringBuilder hex = new StringBuilder(array.length * 2);
        for (int i = 0; i < array.length; i++) {
            hex.append(hexString.charAt((array[i] & 0xf0) >> 4));  // 作用同 n / 16
            hex.append(hexString.charAt((array[i] & 0x0f) >> 0));  // 作用同 n
        }
        return hex.toString();
    }

    /**
     * byte 转换为string显示（带分隔符）
     * 例如：0xaa 0xbb -> startCheck + "sep" + bb显示。
     *
     * @param array
     * @return
     */
    public static String toHex(byte[] array, String sep) {
        String hexString = "0123456789ABCDEF";
        StringBuilder hex = new StringBuilder(array.length * 2);
        for (int i = 0; i < array.length; i++) {
            hex.append(hexString.charAt((array[i] & 0xf0) >> 4));  // 作用同 n / 16
            hex.append(hexString.charAt((array[i] & 0x0f) >> 0));  // 作用同 n
            hex.append(sep);
        }
        return hex.toString();
    }

    /**
     * @param array
     * @param len
     * @return
     */
    public static String toHex(byte[] array, int len) {    //带长度的byte数组
        String hexString = "0123456789ABCDEF";
        StringBuilder hex = new StringBuilder(len * 2);
        for (int i = 0; i < len; i++) {
            hex.append(hexString.charAt((array[i] & 0xf0) >> 4));  // 作用同 n / 16
            hex.append(hexString.charAt((array[i] & 0x0f) >> 0));  // 作用同 n
        }
        return hex.toString();
    }

    /**
     * 将指定字符串src，以每两个字符分割转换为16进制形式
     * 如："2B44EFD9" --> byte[]{0x2B, 0x44, 0xEF, 0xD9}
     *
     * @param src
     * @return
     */
    public static byte[] HexString2Bytes(String src) {
        byte[] ret = new byte[src.length() / 2];
        byte[] tmp = src.getBytes();
        for (int i = 0; i < src.length() / 2; i++) {
            ret[i] = uniteBytes(tmp[i * 2], tmp[i * 2 + 1]);
        }
        return ret;
    }

    /**
     * 将两个ASCII字符合成一个字节；
     * 如："EF"--> 0xEF
     *
     * @param src0 byte
     * @param src1 byte
     * @return byte
     **/
    public static byte uniteBytes(byte src0, byte src1) {
        byte _b0 = Byte.decode("0x" + new String(new byte[]{src0})).byteValue();
        _b0 = (byte) (_b0 << 4);
        byte _b1 = Byte.decode("0x" + new String(new byte[]{src1})).byteValue();
        byte ret = (byte) (_b0 ^ _b1);
        return ret;
    }

    public static byte[] mergebytearray(byte[] b1, byte[] b2, byte[] b3) {//合并3个byte数组
        byte[] result = new byte[b1.length + b2.length + b3.length];
        System.arraycopy(b1, 0, result, 0, b1.length);
        System.arraycopy(b2, 0, result, b1.length, b2.length);
        System.arraycopy(b3, 0, result, b1.length + b2.length, b3.length);
        return result;
    }

    public static byte[] mergebytearray(byte[] b1, byte[] b2, byte[] b3, byte[] b4) {//合并4个byte数组
        byte[] result = new byte[b1.length + b2.length + b3.length + b4.length];
        System.arraycopy(b1, 0, result, 0, b1.length);
        System.arraycopy(b2, 0, result, b1.length, b2.length);
        System.arraycopy(b3, 0, result, b1.length + b2.length, b3.length);
        System.arraycopy(b4, 0, result, b1.length + b2.length + b3.length, b4.length);
        return result;
    }

    /**
     * 字节数组转换成十六进制字符串
     * 例如：0x1a 0x1c -> "1a1c"
     *
     * @param bArray
     * @return HexString
     */
    public static final String bytesToHexString(byte[] bArray) {
        StringBuffer sb = new StringBuffer(bArray.length);
        String sTemp;
        for (int i = 0; i < bArray.length; i++) {
            sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2)
                sb.append(0);
            sb.append(sTemp.toLowerCase());//转成小写
        }
        return sb.toString();
    }

    /**
     * 将指定字符串src，以每两个字符分割转换为16进制形式
     * 如："2B 44 EF D9" --> byte[]{0x2B, 0x44, 0xEF, 0xD9}
     *
     * @param src String
     * @return byte[]
     **/
    public static byte[] hexStringToByte(String src) {
        src = src.replaceAll(" ", "");// 去掉空格
        byte[] ret = new byte[src.length() / 2];
        byte[] tmp = src.getBytes();
        for (int i = 0; i < src.length() / 2; i++) {
            ret[i] = uniteBytes(tmp[i * 2], tmp[i * 2 + 1]);
        }
        return ret;
    }

    /**
     * 拼接hex和CRC
     *
     * @param b
     * @param crc
     * @return
     */
    public static byte[] hexLinkCRC(byte[] b, int[] crc) {

        byte[] n = new byte[b.length + 2];
        System.arraycopy(b, 0, n, 0, b.length);

        n[b.length] = (byte) crc[0];
        n[b.length + 1] = (byte) crc[1];

        return n;
    }

    public static int hexToInt(String hex) {
        return byteArrayToInt(hexStringToByte(hex));
    }

    public static int byteArrayToInt(byte[] b) {
        if (b.length == 1) {
            return b[0] & 0xFF;
        } else if (b.length == 2) {
            return b[1] & 0xFF |
                    (b[0] & 0xFF) << 8;
        } else if (b.length == 4) {
            return b[3] & 0xFF |
                    (b[2] & 0xFF) << 8 |
                    (b[1] & 0xFF) << 16 |
                    (b[0] & 0xFF) << 24;
        } else {
            return -1;
        }
    }

    public static int byteArrayToInt(short[] b) {
        if (b.length == 1) {
            return b[0] & 0xFF;
        } else if (b.length == 2) {
            return b[1] & 0xFF |
                    (b[0] & 0xFF) << 8;
        } else if (b.length == 4) {
            return b[3] & 0xFF |
                    (b[2] & 0xFF) << 8 |
                    (b[1] & 0xFF) << 16 |
                    (b[0] & 0xFF) << 24;
        } else {
            return -1;
        }
    }

    public static int byteArrayToInt(byte b) {
        return b & 0xFF;
    }

    /**
     * byte数组转int,小端
     *
     * @param bytes
     * @return
     */
    public static int byteArrayToInt_Little(byte[] bytes) {
        int result = 0;
        if (bytes.length == 1) {
            return bytes[0] & 0xFF;
        } else if (bytes.length == 2) {
            return bytes[0] & 0xFF |
                    (bytes[1] & 0xFF) << 8;
        } else if (bytes.length == 4) {
            int a = (bytes[0] & 0xff) << 0;
            int b = (bytes[1] & 0xff) << 8;
            int c = (bytes[2] & 0xff) << 16;
            int d = (bytes[3] & 0xff) << 24;
            result = a | b | c | d;
        }
        return result;
    }

    /**
     * byte数组转有符号int,小端
     * @param bytes
     * @return
     */
    public static int byteArrayToSignInt_Little(byte[] bytes) {
        int result = 0;
        if (bytes.length == 1) {
            return bytes[0] & 0xFF;
        } else if (bytes.length == 2) {
            return bytes[0] & 0xFF |
                    (bytes[1] << 8);
        } else if (bytes.length == 4) {
            int a = bytes[0] & 0xff;
            int b = (bytes[1] & 0xff) << 8;
            int c = (bytes[2] & 0xff) << 16;
            int d = bytes[3]  << 24;
            result = a | b | c | d;
        }
        return result;
    }

    public static int stringToConvertInt(short[] b) {
        if (b.length <= 2) {
            return b[0] & 0xFF |
                    (b[1] & 0xFF) << 8;
        } else if (b.length == 4) {
            return b[0] & 0xFF |
                    (b[1] & 0xFF) << 8 |
                    (b[2] & 0xFF) << 16 |
                    (b[3] & 0xFF) << 24;
        } else {
            return -1;
        }
    }

    public static int stringToConvertInt(byte[] b) {
        if (b.length <= 2) {
            return b[0] & 0xFF |
                    (b[1] & 0xFF) << 8;
        } else if (b.length == 4) {
            return b[0] & 0xFF |
                    (b[1] & 0xFF) << 8 |
                    (b[2] & 0xFF) << 16 |
                    (b[3] & 0xFF) << 24;
        } else {
            return -1;
        }
    }

    public static int stringToConvertInt(String s) {
        byte[] b = hexStringToByte(s);
        if (b.length == 2) {
            return b[0] & 0xFF |
                    (b[1] & 0xFF) << 8;
        }
        return -1;
    }

    public static byte[] intToByteArray(int a) {
        return new byte[]{
                (byte) ((a >> 8) & 0xFF),
                (byte) (a & 0xFF)
        };
    }

    /**
     * 反转数组
     *
     * @param data
     * @return
     */
    public static byte[] reverseByteArray(byte[] data) {
        int length = data.length;
        byte[] outputData = new byte[length];
        for (int i = 0; i < length; i++) {
            outputData[length - i - 1] = data[i];
        }
        return outputData;
    }

    /**
     * 获取int数值（小端字节）
     * @param data
     * @param startIndex 开始字节
     * @param endIndex 结束字节
     * @return
     */
    public static int getIntLittleEndian(byte[] data, int startIndex, int endIndex) {
        return HexStrConvertUtil.byteArrayToInt_Little(Arrays.copyOfRange(data, startIndex, endIndex));
    }

    /**
     * 获取有符号int数值（小端字节）
     * @param data
     * @param startIndex 开始字节
     * @param endIndex 结束字节
     * @return
     */
    public static int getIntSignLittleEndian(byte[] data, int startIndex, int endIndex){
        return HexStrConvertUtil.byteArrayToSignInt_Little(Arrays.copyOfRange(data, startIndex, endIndex));
    }

    /**
     * 获取int数值（大端字节）
     * @param data
     * @param startIndex 开始字节
     * @param endIndex 结束字节
     * @return
     */
    public static int getIntBigEndian(byte[] data, int startIndex, int endIndex) {
        return HexStrConvertUtil.byteArrayToInt(Arrays.copyOfRange(data, startIndex, endIndex));
    }

    /**
     * 获取位的数值（从低位开始）
     * @param data
     * @param position 开始位置(从0开始)
     * @return
     */
    public static int getBitValue(byte[] data, int position) {
        int i = byteArrayToInt(data);
        return i >> position & 1;
    }

    /**
     * 获取位的数值（从低位开始）
     *
     * @param data
     * @param startIndex 开始位置(从0开始)
     * @param count      取多少位
     * @return
     */
    public static int getBitValue(byte[] data, int startIndex, int count) {
        int i = byteArrayToInt(data);
        return i >> startIndex & ((int) Math.pow(2, count) - 1);
    }

    public static byte[] intTo32ByteArray(Long a) {
        return new byte[]{
                (byte) ((a >> 24) & 0xFF),
                (byte) ((a >> 16) & 0xFF),
                (byte) ((a >> 8) & 0xFF),
                (byte) (a & 0xFF)
        };
    }

    public static byte[] intTo32ByteArrayReverse(long a) {
        return new byte[]{
                (byte) (a & 0xFF),
                (byte) ((a >> 8) & 0xFF),
                (byte) ((a >> 16) & 0xFF),
                (byte) ((a >> 24) & 0xFF)
        };
    }

    public static byte[] intTo16ByteArray(int a) {
        return new byte[]{
                (byte) ((a >> 8) & 0xFF),
                (byte) (a & 0xFF)
        };
    }

    public static byte[] intTo16ByteArrayReverse(int a) {
        return new byte[]{
                (byte) (a & 0xFF),
                (byte) ((a >> 8) & 0xFF)
        };
    }

    public static byte intTo8ByteArray(int a) {
        return (byte) (a & 0xFF);
    }

    public static short[] intToShortArray(int a) {
        return new short[]{(short) (a >> 8), (short) (a & 255)};
    }

    public static short[] longToShortArray(long a) {
        return new short[]{
                (short) ((a >> 24) & 0xFF),
                (short) ((a >> 16) & 0xFF),
                (short) ((a >> 8) & 0xFF),
                (short) (a & 0xFF)
        };
    }

    public static short[] stringToCharByte(String str) {
        int len = str.length();
        short[] bytes = new short[len];
        for (int i = 0; i < len; i++) {
            bytes[i] = (byte) str.charAt(i);
        }
        return bytes;
    }

    public static String charByteToString(byte[] key) {
        StringBuilder buf = new StringBuilder();
        for (short value : key) {
            if (value != 0)
                buf.append((char) value);
            else
                break;
        }
        return buf.toString();
    }

    public static String charByteToString(short[] key) {
        StringBuilder buf = new StringBuilder();
        for (short value : key) {
            if (value != 0)
                buf.append((char) value);
            else
                break;
        }
        return buf.toString();
    }
}
