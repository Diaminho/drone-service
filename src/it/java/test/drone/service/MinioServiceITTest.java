package test.drone.service;

import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.UploadObjectArgs;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.util.ReflectionUtils;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
class MinioServiceITTest {
    private static final int MINIO_PORT = 9000;
    private static final String MINIO_HOST_TEMPLATE = "http://localhost:%s/";
    private static final String DEFAULT_BUCKET = "test";
    private static final Map<String, String> DEFAULT_ENVS = Map.of(
            "MINIO_ROOT_USER", "test",
            "MINIO_ROOT_PASSWORD", "password"
    );
    private static final String TEST_IMAGE_PATH = "src/it/resources/images/test.jpg";
    private static final String EXPECTED_FILENAME = "1.jpg";

    @Container
    private static final GenericContainer<?> MINIO_CONTAINER = new GenericContainer<>("minio/minio")
            .withEnv(DEFAULT_ENVS)
            .withExposedPorts(MINIO_PORT)
            .withCommand("server /data");

    private static MinioClient MINIO_CLIENT;

    private MinioService minioService;

    @BeforeAll
    static void setUp() throws Exception {
        var client = getMinioClient();

        // bootstrap MinIO
        bootstrapMinio(client);
    }

    @BeforeEach
    public void initService() throws Exception {
        var client = getMinioClient();

        minioService = new MinioService(client);

        // set bucketName
        var field = ReflectionUtils.findField(MinioService.class, "bucketName");
        field.setAccessible(true);
        field.set(minioService, DEFAULT_BUCKET);
    }

    @Test
    void testSimplePutAndGet() throws Exception {
        var testUrl = "http://localhost:9000/medications-images/" + EXPECTED_FILENAME;
        var actual = minioService.downloadFileAsBase64(testUrl);

        var encoder = Base64.getEncoder();
        var expectedPlainBytes = Files.readAllBytes(Paths.get(TEST_IMAGE_PATH));
        var expected = encoder.encodeToString(expectedPlainBytes);

        assertEquals(expected, actual);
    }

    private static void bootstrapMinio(MinioClient client) throws Exception {
        createBucket(client);
        uploadFile(client);
    }

    private static void uploadFile(MinioClient client) throws Exception {
        var testFile = UploadObjectArgs
                .builder()
                .bucket(DEFAULT_BUCKET)
                .filename(TEST_IMAGE_PATH)
                .object(EXPECTED_FILENAME)
                .build();
        client.uploadObject(testFile);
    }

    private static void createBucket(MinioClient client) throws Exception {
        var bucketInfo = MakeBucketArgs.builder().bucket(DEFAULT_BUCKET).build();
        client.makeBucket(bucketInfo);
    }

    @NotNull
    private static MinioClient getMinioClient() {
        if (MINIO_CLIENT == null) {
            var minioUrl = MINIO_CONTAINER.getMappedPort(MINIO_PORT);
            MINIO_CLIENT = MinioClient
                    .builder()
                    .endpoint(String.format(MINIO_HOST_TEMPLATE, minioUrl))
                    .credentials("test", "password")
                    .build();
        }

        return MINIO_CLIENT;
    }
}
