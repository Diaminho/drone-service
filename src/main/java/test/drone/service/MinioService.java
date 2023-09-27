package test.drone.service;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import test.drone.exception.CustomMinioException;

import java.util.Base64;

/**
 * Service to interact with MinIO
 */
@Service
public class MinioService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MinioService.class);

    @Value("${minio.bucket-name}")
    private String bucketName;
    private final MinioClient minioClient;

    public MinioService(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    /**
     * Download file from MinIO and transforms its content to base64 string
     * @param url link to file
     * @return base64 file content
     */
    public String downloadFileAsBase64(String url) {
        var parsedUrl = url.split("/");
        var fileName = parsedUrl[parsedUrl.length - 1];
        var getObjectArgs = GetObjectArgs
                .builder()
                .bucket(bucketName)
                .object(fileName)
                .build();

        try {
            var response = minioClient.getObject(getObjectArgs);
            var bytes = response.readAllBytes();

            // for now return base64 content to parse on the client side
            return Base64
                    .getEncoder()
                    .encodeToString(bytes);
        } catch (Exception e) {
            LOGGER.error("Cannot load object from MinIO. bucketName: {}; name: {}", bucketName, fileName, e);
            throw new CustomMinioException("Cannot load file");
        }
    }
}
