package com.termux.shared.data;

import android.os.Bundle;
import androidx.annotation.Nullable;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class DataUtils {

    /**
     * 最大的安全数据大小限制，用于在进程间或应用间传输数据时防止 TransactionTooLargeException。
     */
    // 100KB
    public static final int TRANSACTION_SIZE_LIMIT_IN_BYTES = 100 * 1024;

    // 十六进制字符数组
    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

    /**
     * 获取被截断的命令输出字符串。
     *
     * @param text 原始文本
     * @param maxLength 最大长度
     * @param fromEnd 是否从末尾截断
     * @param onNewline 截断是否以换行符对齐
     * @param addPrefix 是否添加"(truncated) "前缀
     * @return 截断后的结果
     */
    public static String getTruncatedCommandOutput(String text, int maxLength, boolean fromEnd, boolean onNewline, boolean addPrefix) {
        if (text == null)
            return null;
        String prefix = "(truncated) ";
        if (addPrefix)
            maxLength = maxLength - prefix.length();
        if (maxLength < 0 || text.length() < maxLength)
            return text;
        if (fromEnd) {
            text = text.substring(0, maxLength);
        } else {
            int cutOffIndex = text.length() - maxLength;
            if (onNewline) {
                int nextNewlineIndex = text.indexOf('\n', cutOffIndex);
                if (nextNewlineIndex != -1 && nextNewlineIndex != text.length() - 1) {
                    cutOffIndex = nextNewlineIndex + 1;
                }
            }
            text = text.substring(cutOffIndex);
        }
        if (addPrefix)
            text = prefix + text;
        return text;
    }

    /**
     * 替换字符串数组中每个元素的子字符串。
     *
     * @param array 字符串数组
     * @param find 需要被替换的子字符串
     * @param replace 替换后的子字符串
     */
    public static void replaceSubStringsInStringArrayItems(String[] array, String find, String replace) {
        if (array == null || array.length == 0)
            return;
        for (int i = 0; i < array.length; i++) {
            array[i] = array[i].replace(find, replace);
        }
    }

    /**
     * 从字符串获取 float 类型值。
     *
     * @param value 待解析字符串
     * @param def 默认值（解析失败时返回）
     * @return 解析到的 float 值或默认值
     */
    public static float getFloatFromString(String value, float def) {
        if (value == null)
            return def;
        try {
            return Float.parseFloat(value);
        } catch (Exception e) {
            return def;
        }
    }

    /**
     * 从字符串获取 int 类型值。
     *
     * @param value 待解析字符串
     * @param def 默认值（解析失败时返回）
     * @return 解析到的 int 值或默认值
     */
    public static int getIntFromString(String value, int def) {
        if (value == null)
            return def;
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            return def;
        }
    }

    /**
     * 将 Integer 转换为字符串。
     *
     * @param value 整数对象
     * @param def 默认字符串
     * @return 若 value 为 null，返回 def，否则返回字符串
     */
    public static String getStringFromInteger(Integer value, String def) {
        return (value == null) ? def : String.valueOf((int) value);
    }

    /**
     * 将字节数组转换为十六进制字符串。
     *
     * @param bytes 字节数组
     * @return 十六进制字符串
     */
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }

    /**
     * 从 Bundle 中以字符串形式存储的 int 值。
     *
     * @param bundle Bundle 对象
     * @param key 字段名
     * @param def 默认值
     * @return 解析到的 int 值或默认值
     */
    public static int getIntStoredAsStringFromBundle(Bundle bundle, String key, int def) {
        if (bundle == null)
            return def;
        return getIntFromString(bundle.getString(key, Integer.toString(def)), def);
    }

    /**
     * 将值限制在[min,max]区间。
     *
     * @param value 待限制的值
     * @param min 最小值
     * @param max 最大值
     * @return 限制后的结果
     */
    public static int clamp(int value, int min, int max) {
        return Math.min(Math.max(value, min), max);
    }

    /**
     * 若 value 不在[min,max]区间，则返回默认值，否则返回 value。
     *
     * @param value 检查值
     * @param def 默认值
     * @param min 最小值
     * @param max 最大值
     * @return 检查结果
     */
    public static float rangedOrDefault(float value, float def, float min, float max) {
        if (value < min || value > max)
            return def;
        else
            return value;
    }

    /**
     * 获取对象本身，如果为 null 则返回默认对象。
     *
     * @param object 对象
     * @param def 默认对象
     * @return 本身或默认对象
     */
    public static <T> T getDefaultIfNull(@Nullable T object, @Nullable T def) {
        return (object == null) ? def : object;
    }

    /**
     * 获取字符串本身，如果为 null 或空则返回默认值。
     *
     * @param value 字符串
     * @param def 默认字符串
     * @return 本身或默认字符串
     */
    public static String getDefaultIfUnset(@Nullable String value, String def) {
        return (value == null || value.isEmpty()) ? def : value;
    }

    /**
     * 判断字符串是否为 null 或空字符串。
     *
     * @param string 检查的字符串
     * @return 为 null 或空返回 true，否则返回 false
     */
    public static boolean isNullOrEmpty(String string) {
        return string == null || string.isEmpty();
    }

    /**
     * 获取可序列化对象的序列化后大小。
     *
     * @param object 可序列化对象
     * @return 序列化后的字节数，失败返回 -1，null 返回0
     */
    public static long getSerializedSize(Serializable object) {
        if (object == null)
            return 0;
        try {
            ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteOutputStream);
            objectOutputStream.writeObject(object);
            objectOutputStream.flush();
            objectOutputStream.close();
            return byteOutputStream.toByteArray().length;
        } catch (Exception e) {
            return -1;
        }
    }
}

