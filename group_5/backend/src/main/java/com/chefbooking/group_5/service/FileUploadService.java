package com.chefbooking.group_5.service;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

public interface FileUploadService {
    /**
     * Tải file lên cloud
     * @param file File được gửi từ client
     * @return Đường dẫn URL an toàn của file
     * @throws IOException
     */
    String uploadFile(MultipartFile file) throws IOException;
}