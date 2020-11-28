package cn.itcast.jd.util.image;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class StringUtils {
    public static boolean isBlank(String str) {
        return str == null || str.isEmpty();
    }

    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    /**
     * @param str 要被逗号（不区分中英文）切割的字符串
     * @return 字符串数组
     */
    public static List<String> commaSplit(String str) {
        List<String> list = new ArrayList<>();
        if (isBlank(str)) {
            return list;
        }
        str = str.replace("，", ",");
        str = str.replace(" ", "");
        String[] strArray = str.split(",");
        for (String item : strArray) {
            if (isNotBlank(item)) {
                list.add(item);
            }
        }
        return list;
    }

    /**
     * 获取文件路径中的文件名
     *
     * @param filePath 文件路径
     */
    public static String getFileName(String filePath) {
        String[] strArray = filePath.split(File.separator.equals("\\") ? "\\\\" : File.separator);
        return strArray[strArray.length - 1];
    }
}