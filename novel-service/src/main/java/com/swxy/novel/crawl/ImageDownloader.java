package com.swxy.crawl;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

import static com.swxy.crawl.Utils.getMD5Hash;


public class ImageDownloader {
    private static final Logger logger = LoggerFactory.getLogger(ImageDownloader.class);

    public static void downloadImage(Document novelPage, String saveDir) {
        Element imgElement = novelPage.select("div:nth-child(1) > img").first();
        if (imgElement != null) {
            String imgUrl = imgElement.absUrl("src");
            String uniqueFileName = getMD5Hash(imgUrl.getBytes()) + ".jpg";
            String savePath = saveDir + Utils.generateUniqueFileName(saveDir, uniqueFileName);
            downloadImage(imgUrl, savePath);
        } else {
            logger.warn("未找到图片元素。");
        }
    }

    private static void downloadImage(String imgUrl, String savePath) {
        try (InputStream in = new URL(imgUrl).openStream()) {
            byte[] imageBytes = in.readAllBytes(); // 读取所有字节
            String imgHash = getMD5Hash(imageBytes); // 计算图片的 MD5 哈希值

            // 检查目标路径是否已存在
            if (Files.exists(Paths.get(savePath))) {
                // 如果存在，计算已存在文件的哈希值
                byte[] existingImageBytes = Files.readAllBytes(Paths.get(savePath));
                String existingImgHash = getMD5Hash(existingImageBytes);

                // 如果哈希相同，则不下载
                if (imgHash.equals(existingImgHash)) {
                    logger.info("图片已存在，跳过下载: {}", savePath);
                    return;
                }
            }

            // 如果文件不存在或哈希不同，则保存文件
            Files.write(Paths.get(savePath), imageBytes);
            logger.info("图片下载成功: {}", savePath);
        } catch (MalformedURLException e) {
            logger.error("无效的图片URL: {}", imgUrl);
        } catch (IOException e) {
            logger.error("下载图片时发生错误: {}", e.getMessage());
        }
}
}

