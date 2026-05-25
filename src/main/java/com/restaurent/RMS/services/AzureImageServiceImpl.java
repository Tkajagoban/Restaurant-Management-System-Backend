package com.restaurent.RMS.services;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.sas.BlobSasPermission;
import com.azure.storage.blob.sas.BlobServiceSasSignatureValues;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AzureImageServiceImpl implements AzureImageService {

    private final BlobServiceClient blobServiceClient;

    @Value("${azure.storage.container-name}")
    private String containerName;

    @Override
    public String uploadImage(MultipartFile file) {
        try {
            BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);

            // Create container if it doesn't exist
            if (!containerClient.exists()) {
                containerClient.create();
            }

            // Sanitize filename to remove spaces/special chars
            String originalFilename = file.getOriginalFilename();
            String sanitizedFilename = originalFilename != null ? originalFilename.replaceAll("\\s+", "-") : "file";
            String fileName = UUID.randomUUID() + "-" + sanitizedFilename;

            BlobClient blobClient = containerClient.getBlobClient(fileName);

            // Upload blob (overwrite if exists)
            blobClient.upload(file.getInputStream(), file.getSize(), true);

            // Generate SAS token valid for 24 hours (adjust as needed)
            BlobSasPermission permission = new BlobSasPermission().setReadPermission(true);
            OffsetDateTime expiryTime = OffsetDateTime.now().plusHours(24);
            BlobServiceSasSignatureValues sasValues = new BlobServiceSasSignatureValues(expiryTime, permission);

            String sasToken = blobClient.generateSas(sasValues);

            // Return full URL with SAS token
            return blobClient.getBlobUrl() + "?" + sasToken;

        } catch (IOException e) {
            throw new RuntimeException("Azure Blob upload failed", e);
        }
    }
}
