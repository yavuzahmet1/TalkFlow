package com.yavuzahmet.talkflow.message;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.io.File.separator;

@Service
@Slf4j
public class FileService {

    @Value("${application.file.uploads.media-output-path}")
    private String fileUploadPath;

    public String saveFile(@NonNull MultipartFile sourceFile, @NonNull String senderId) {
        final String fileUploadSubPath = "users" + separator + senderId;
        return uploadFile(sourceFile,fileUploadSubPath);

    }

    public String uploadFile(@NonNull MultipartFile sourceFile, @NonNull String fileUploadSubPath) {
        final String finalUploadPath = fileUploadPath + separator + fileUploadSubPath;
        File targetFolder = new File(finalUploadPath);

        if (!targetFolder.exists()) {
            boolean folderCreated = targetFolder.mkdirs();
            if (!folderCreated) {
                log.error("Could not create the directory {}.", targetFolder);
                return null;
            }
        }

        final  String fileExtension=getFileExtension(sourceFile.getOriginalFilename());

        String targetFilePath =finalUploadPath+separator+System.currentTimeMillis()+"."+fileExtension;

        Path targetPath = Paths.get(targetFilePath);

        try {

            Files.write(targetPath,sourceFile.getBytes());

            log.info("File uploaded successfully.{}",targetPath);

            return targetFilePath;

        }catch(IOException e){

            log.error("Could not save file. {}",targetPath,e);

        }

        return null;

    }

    private String getFileExtension(String fileName) {

        if (fileName == null||fileName.isEmpty()) {

            return "";
        }

        int lastDotIndex = fileName.lastIndexOf('.');

        if (lastDotIndex == -1) {

            return "";
        }

        return fileName.substring(lastDotIndex + 1).toLowerCase();
    }
}
