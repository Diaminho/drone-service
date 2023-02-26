package test.drone.service;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.Base64;

@Service
@Slf4j
@RequiredArgsConstructor
public class MinioService {
    @Value("${minio.bucket-name}")
    private String bucketName;
    private final MinioClient minioClient;

    public String downloadFile(String url) {
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
            log.error("Cannot load object from minio. bucketName: {}; name: {}", bucketName, fileName, e);
            throw new RuntimeException("Cannot load file");
        }
    }
}
