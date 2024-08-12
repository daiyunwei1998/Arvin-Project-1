package com.stylish.stylish.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.stylish.stylish.utlis.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class S3Service {
    private final AmazonS3 amazonS3;
    @Value("${aws.s3.url}")
    private String s3URL;
    @Value("${aws.s3.bucket}")
    private String bucketName;

    @Autowired
    public S3Service(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    private String generateUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }
    public String uploadFile(MultipartFile file) throws IOException {
        String uuid = generateUUID();
        String fileName = uuid +"." + FileUtil.getFileExtension(file);
        // Initialize multipart upload
        InitiateMultipartUploadRequest initRequest = new InitiateMultipartUploadRequest(bucketName, fileName);
        InitiateMultipartUploadResult initResponse = amazonS3.initiateMultipartUpload(initRequest);
        String uploadId = initResponse.getUploadId();

        try (InputStream inputStream = file.getInputStream()) {
            // Upload parts
            byte[] buffer = new byte[5 * 1024 * 1024]; // 5MB buffer
            int partNumber = 1;
            List<PartETag> partETags = new ArrayList<>();
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) > 0) {
                UploadPartRequest uploadRequest = new UploadPartRequest()
                        .withBucketName(bucketName)
                        .withKey(fileName)
                        .withUploadId(uploadId)
                        .withPartNumber(partNumber++)
                        .withInputStream(new ByteArrayInputStream(buffer, 0, bytesRead))
                        .withPartSize(bytesRead);
                partETags.add(amazonS3.uploadPart(uploadRequest).getPartETag());
            }

            // Complete multipart upload
            CompleteMultipartUploadRequest compRequest = new CompleteMultipartUploadRequest(bucketName, fileName, uploadId, partETags);
            amazonS3.completeMultipartUpload(compRequest);
            return s3URL+fileName;
        } catch (Exception e) {
            // Abort multipart upload in case of error
            amazonS3.abortMultipartUpload(new AbortMultipartUploadRequest(bucketName, fileName, uploadId));
            throw e;
        }
    }
}
