package com.xinxian.generator.framework.utils;

import java.io.*;
import java.util.List;

/**
 * @ClassName FileUtils
 * @Description 文件工具类
 * @Author lmy
 * @Date 2023/1/6 22:35
 */
public class FileUtils {
    public static void writeList2File(List<String> lines, String filePath) {
        BufferedWriter bw = null;
        try {
            File file = new File(filePath);
            // 判断文件是否存在
            createNewFile(file);
            // 遍历写入
            bw = new BufferedWriter(new FileWriter(file));
            for (String line : lines) {
                bw.write(line + "\n");
            }
            bw.flush();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                bw.close();
            } catch (IOException ioEx) {
                throw new RuntimeException(ioEx);
            }
        }
    }

    public static void writeText2File(String text, String filePath) {
        BufferedWriter bw = null;
        try {
            File file = new File(filePath);
            // 判断文件是否存在
            createNewFile(file);
            // 遍历写入
            bw = new BufferedWriter(new FileWriter(file));
            bw.write(text + "\n");
            bw.flush();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (bw != null) {
                    bw.close();
                }
            } catch (IOException ioEx) {
                throw new RuntimeException(ioEx);
            }
        }
    }

    public static String readFileByChars(String fileName) {
        File file = new File(fileName);
        Reader reader = null;
        StringBuffer sb = new StringBuffer();
        try {
            // 一次读多个字符
            char[] tempchars = new char[30];
            int charread = 0;
            reader = new InputStreamReader(new FileInputStream(fileName));
            // 读入多个字符到字符数组中，charread为一次读取字符数
            while ((charread = reader.read(tempchars)) != -1) {
                // 同样屏蔽掉\r不显示
                if ((charread == tempchars.length)
                        && (tempchars[tempchars.length - 1] != '\r')) {
                    sb.append(tempchars);
                } else {
                    for (int i = 0; i < charread; i++) {
                        if (tempchars[i] == '\r') {
                            continue;
                        } else {
                            sb.append(tempchars[i]);
                        }
                    }
                }
            }
            return sb.toString();
        } catch (Exception e1) {
            e1.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        return null;
    }

    public static void createNewFile(File file) throws IOException {
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        if (!file.exists()) {
            file.createNewFile();
        }
    }
}
