package com.example.sharablead.util;

import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.io.IOUtils;
import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.FileItemFactory;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.*;
import java.util.Objects;

public class FileUtil {
    /**
     * 压缩文件
     * @param file
     * @return
     */
    public static MultipartFile compressFile(MultipartFile file) throws IOException {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Thumbnails.of(file.getInputStream())
                .size(200, 200).keepAspectRatio(false)
                .outputQuality(0.5)
                .outputFormat("jpg")
                .toOutputStream(outputStream);
        byte[] bytes = outputStream.toByteArray();
        InputStream inputStream = new ByteArrayInputStream(bytes);
        return new MockMultipartFile(Objects.requireNonNull(file.getOriginalFilename()), file.getOriginalFilename(), file.getContentType(), inputStream);
    }

    public static MultipartFile resizeFile(MultipartFile file, int l, int l1) throws IOException {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Thumbnails.of(file.getInputStream())
                .size(l, l1).keepAspectRatio(false)
                .outputFormat("jpg")
                .toOutputStream(outputStream);
        byte[] bytes = outputStream.toByteArray();
        InputStream inputStream = new ByteArrayInputStream(bytes);
        return new MockMultipartFile(Objects.requireNonNull(file.getOriginalFilename()), file.getOriginalFilename(), file.getContentType(), inputStream);
    }

    public static void main(String args[]) throws IOException {
        File file = new File("C:\\Users\\JC0648\\Desktop\\test.jpg");
        Thumbnails.Builder<File> builder = Thumbnails.of(file);
        builder.outputQuality(0.1).size(1601,2425).toFile("test1.jpg");
    }
}