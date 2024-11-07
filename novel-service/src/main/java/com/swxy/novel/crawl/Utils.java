package com.swxy.crawl;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Utils {
    public static String getMD5Hash(byte[] input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hashInBytes = md.digest(input);
            StringBuilder sb = new StringBuilder();
            for (byte b : hashInBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static String generateUniqueFileName(String saveDir, String baseName) {
        String uniqueFileName = baseName;
        // 生成新的文件名，通过在文件名后加上序号来避免重复
        String fileNameWithoutExtension = uniqueFileName.substring(0, uniqueFileName.lastIndexOf('.'));
        String fileExtension = uniqueFileName.substring(uniqueFileName.lastIndexOf('.'));
        uniqueFileName = fileNameWithoutExtension  + fileExtension; // 生成唯一文件名
        return uniqueFileName;
    }
}

