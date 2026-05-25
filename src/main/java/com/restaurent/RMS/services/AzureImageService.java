package com.restaurent.RMS.services;

import org.springframework.web.multipart.MultipartFile;

public interface AzureImageService {
    String uploadImage(MultipartFile file);
}
