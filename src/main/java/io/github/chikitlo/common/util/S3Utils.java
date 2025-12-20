package io.github.chikitlo.common.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.core.async.AsyncResponseTransformer;
import software.amazon.awssdk.core.client.config.ClientOverrideConfiguration;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.core.retry.RetryMode;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.http.async.SdkAsyncHttpClient;
import software.amazon.awssdk.http.nio.netty.NettyNioAsyncHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.paginators.ListObjectsV2Iterable;
import software.amazon.awssdk.services.s3.paginators.ListObjectsV2Publisher;
import software.amazon.awssdk.transfer.s3.S3TransferManager;
import software.amazon.awssdk.transfer.s3.model.CompletedDirectoryUpload;
import software.amazon.awssdk.transfer.s3.model.DirectoryUpload;
import software.amazon.awssdk.transfer.s3.model.UploadDirectoryRequest;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.zip.GZIPInputStream;

/**
 * Amazon S3 Utils
 *
 * @author Jack Lo
 * @version 1.0
 * @date 2025/12/13 17:55
 */
@Slf4j
public final class S3Utils {
    private String endpoint;
    private String accessKeyId;
    private String secretAccessKey;
    private String region;

    private S3Client s3Client;
    private S3AsyncClient s3AsyncClient;
    private S3TransferManager s3TransferManager;

    private S3Utils() {

    }

    public static S3Utils builder() {
        return new S3Utils();
    }

    public S3Utils endpoint(String endpoint) {
        this.endpoint = endpoint;
        return this;
    }

    public S3Utils accessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
        return this;
    }

    public S3Utils secretAccessKey(String secretAccessKey) {
        this.secretAccessKey = secretAccessKey;
        return this;
    }

    public S3Utils region(String region) {
        this.region = region;
        return this;
    }

    public S3Utils build() {
        validateRequiredParams();
        initClient();
        initAsyncClient();
        initTransferManager();
        return this;
    }

    public S3Client getClient() {
        return s3Client;
    }

    public S3AsyncClient getAsyncClient() {
        return s3AsyncClient;
    }

    public S3TransferManager getTransferManager() {
        return s3TransferManager;
    }

    /**
     * Validate required fields before building clients.
     *
     * @param
     * @return
     * @throws
     * @author Jack Lo
     * @date 2025/12/13 18:05
     */
    private void validateRequiredParams() {
        if (ObjectUtils.isEmpty(endpoint) || ObjectUtils.isEmpty(accessKeyId) || ObjectUtils.isEmpty(secretAccessKey) || ObjectUtils.isEmpty(region)) {
            throw new IllegalArgumentException("Endpoint, Access Key ID, Secret Access Key, Region must be provided");
        }
    }

    /**
     * Initialize S3 Client.
     *
     * @param
     * @return
     * @throws
     * @author Jack Lo
     * @date 2025/12/13 18:06
     */
    private void initClient() {
        if (s3Client == null) {
            s3Client = S3Client.builder()
                    .endpointOverride(URI.create(endpoint))
                    .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKeyId, secretAccessKey)))
                    .region(Region.of(region))
                    .build();
        }
    }

    /**
     * Initialize S3 Async Client.
     *
     * @param
     * @return
     * @throws
     * @author Jack Lo
     * @date 2025/12/13 18:07
     */
    private void initAsyncClient() {
        if (s3AsyncClient == null) {
            SdkAsyncHttpClient httpClient = NettyNioAsyncHttpClient
                    .builder()
                    .maxConcurrency(50)
                    .connectionTimeout(Duration.ofSeconds(60))
                    .readTimeout(Duration.ofSeconds(60))
                    .writeTimeout(Duration.ofSeconds(60))
                    .build();

            ClientOverrideConfiguration overrideConfiguration = ClientOverrideConfiguration
                    .builder()
                    .apiCallTimeout(Duration.ofMinutes(2))
                    .apiCallAttemptTimeout(Duration.ofSeconds(90))
                    .retryStrategy(RetryMode.STANDARD)
                    .build();

            s3AsyncClient = S3AsyncClient
                    .builder()
                    .endpointOverride(URI.create(endpoint))
                    .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKeyId, secretAccessKey)))
                    .region(Region.of(region))
                    .httpClient(httpClient)
                    .overrideConfiguration(overrideConfiguration)
                    .build();
        }
    }

    /**
     * Initialize S3 Transfer Manager.
     *
     * @param
     * @return
     * @throws
     * @author Jack Lo
     * @date 2025/12/20 14:38
     */
    private void initTransferManager() {
        if (s3TransferManager == null) {
            s3TransferManager = S3TransferManager
                    .builder()
                    .s3Client(s3AsyncClient)
                    .build();
        }
    }

    /**
     * Get Buckets.
     *
     * @param
     * @return java.util.List<java.lang.String>
     * @throws
     * @author Jack Lo
     * @date 2025/12/20 14:38
     */
    public List<String> listBuckets() {
        return s3Client
                .listBuckets()
                .buckets()
                .stream()
                .map(Bucket::name)
                .toList();
    }

    /**
     * Get Keys by bucket and Prefix.
     *
     * @param bucket
     * @param prefix
     * @return java.util.List<java.lang.String>
     * @throws
     * @author Jack Lo
     * @date 2025/12/20 14:42
     */
    public List<String> listPrefixKeys(String bucket, String prefix) {
        ListObjectsV2Request listReq = ListObjectsV2Request
                .builder()
                .bucket(bucket)
                .prefix(prefix)
                .maxKeys(1)
                .build();

        ListObjectsV2Iterable listRes = s3Client.listObjectsV2Paginator(listReq);

        return listRes
                .stream()
                .flatMap(item -> item.contents().stream())
                .filter(item -> !prefix.equals(item.key()))
                .map(item -> bucket + "/" + item.key())
                .toList();
    }

    /**
     * Get Objects by bucket and Prefix.
     *
     * @param bucket
     * @param prefix
     * @return java.util.List<software.amazon.awssdk.services.s3.model.S3Object>
     * @throws
     * @author Jack Lo
     * @date 2025/12/20 14:44
     */
    public List<S3Object> listPrefixObjects(String bucket, String prefix) {
        ListObjectsV2Request listReq = ListObjectsV2Request
                .builder()
                .bucket(bucket)
                .prefix(prefix)
                .maxKeys(1)
                .build();

        ListObjectsV2Iterable listRes = s3Client.listObjectsV2Paginator(listReq);

        return listRes
                .stream()
                .flatMap(item -> item.contents().stream())
                .filter(item -> !prefix.equals(item.key()))
                .toList();
    }

    /**
     * Get Objects by bucket and Prefix Async.
     *
     * @param bucket
     * @param prefix
     * @return java.util.concurrent.CompletableFuture<java.lang.Void>
     * @throws
     * @author Jack Lo
     * @date 2025/12/20 14:53
     */
    public CompletableFuture<Void> listPrefixObjectsAsync(String bucket, String prefix) {
        ListObjectsV2Request listReq = ListObjectsV2Request
                .builder()
                .bucket(bucket)
                .prefix(prefix)
                .maxKeys(1)
                .build();

        ListObjectsV2Publisher paginator = s3AsyncClient.listObjectsV2Paginator(listReq);

        CompletableFuture<Void> future = new CompletableFuture<>();
        paginator.subscribe(response -> response.contents().forEach(s3Object -> log.info("Object key: {}", s3Object.key())))
                .thenRun(() -> {
                    log.info("Successfully listed all objects in bucket: {}, prefix: {}", bucket, prefix);
                    future.complete(null);
                })
                .exceptionally(ex -> {
                    future.completeExceptionally(SdkException.create("Failed to list objects", ex));
                    return null;
                });

        return future;
    }

    /**
     * Head Object by Bucket and Key.
     *
     * @param bucket
     * @param key
     * @return software.amazon.awssdk.services.s3.model.HeadObjectResponse
     * @throws
     * @author Jack Lo
     * @date 2025/12/20 14:57
     */
    public HeadObjectResponse headObject(String bucket, String key) {
        HeadObjectRequest headObjectRequest = HeadObjectRequest
                .builder()
                .bucket(bucket)
                .key(key)
                .build();

        return s3Client.headObject(headObjectRequest);
    }

    /**
     * Get Object by Bucket and Key.
     *
     * @param bucket
     * @param key
     * @return software.amazon.awssdk.core.ResponseBytes<software.amazon.awssdk.services.s3.model.GetObjectResponse>
     * @throws
     * @author Jack Lo
     * @date 2025/12/20 14:58
     */
    public ResponseBytes<GetObjectResponse> getObject(String bucket, String key) {
        GetObjectRequest getObjectRequest = GetObjectRequest
                .builder()
                .bucket(bucket)
                .key(key)
                .build();

        return s3Client.getObject(getObjectRequest, ResponseTransformer.toBytes());
    }

    /**
     * Download Object by Bucket and Key.
     *
     * @param bucket
     * @param key
     * @param path
     * @return
     * @throws
     * @author Jack Lo
     * @date 2025/12/20 15:04
     */
    public void getObjectBytes(String bucket, String key, String path) {
        try (OutputStream outputStream = new FileOutputStream(path)) {
            GetObjectRequest getObjectRequest = GetObjectRequest
                    .builder()
                    .bucket(bucket)
                    .key(key)
                    .build();

            ResponseBytes<GetObjectResponse> objectBytes = s3Client.getObject(getObjectRequest, ResponseTransformer.toBytes());
            outputStream.write(objectBytes.asByteArray());
            log.info("Successfully obtained bytes from an S3 object, target path: {}", path);
        } catch (IOException e) {
            log.error("IO error while writing S3 object to file: {}", e.getMessage());
        } catch (S3Exception e) {
            log.error(e.awsErrorDetails().errorMessage());
        }
    }

    /**
     * Download Object by Bucket and Key Async.
     *
     * @param bucket
     * @param key
     * @param path
     * @return java.util.concurrent.CompletableFuture<java.lang.Void>
     * @throws
     * @author Jack Lo
     * @date 2025/12/20 15:11
     */
    public CompletableFuture<Void> getObjectBytesAsync(String bucket, String key, String path) {
        GetObjectRequest getObjectRequest = GetObjectRequest
                .builder()
                .bucket(bucket)
                .key(key)
                .build();
        CompletableFuture<ResponseBytes<GetObjectResponse>> response = s3AsyncClient.getObject(getObjectRequest, AsyncResponseTransformer.toBytes());

        return response.thenAccept(objectBytes -> {
            try {
                Path filePath = Paths.get(path);
                Files.write(filePath, objectBytes.asByteArray());
                log.info("Successfully obtained bytes from an S3 object, target path: {}", path);
            } catch (IOException e) {
                throw SdkException.create("Failed to write data to file", e);
            }
        }).handle((resp, ex) -> {
            if (ex != null) {
                throw SdkException.create("Failed to get object bytes from S3", ex);
            }
            return null;
        });
    }

    /**
     * Copy Object to another Bucket.
     *
     * @param sourceBucket
     * @param sourceKey
     * @param destinationBucket
     * @param destinationKey
     * @return java.util.concurrent.CompletableFuture<java.lang.String>
     * @throws
     * @author Jack Lo
     * @date 2025/12/20 15:16
     */
    public CompletableFuture<String> copyBucketObjectAsync(String sourceBucket, String sourceKey, String destinationBucket, String destinationKey) {
        CopyObjectRequest copyObjectRequest = CopyObjectRequest
                .builder()
                .sourceBucket(sourceBucket)
                .sourceKey(sourceKey)
                .destinationBucket(destinationBucket)
                .destinationKey(destinationKey)
                .build();
        CompletableFuture<CopyObjectResponse> response = s3AsyncClient.copyObject(copyObjectRequest);

        return response.handle((copyRes, ex) -> {
            if (ex != null) {
                throw SdkException.create("An S3 exception occurred during copy", ex);
            }
            log.info("The {}/{} was copied to {}/{}", sourceBucket, sourceKey, destinationBucket, destinationKey);

            return copyRes.copyObjectResult().toString();
        });
    }

    /**
     * Delete Object by Bucket and Key.
     *
     * @param bucket
     * @param key
     * @return java.util.concurrent.CompletableFuture<java.lang.Void>
     * @throws
     * @author Jack Lo
     * @date 2025/12/20 15:18
     */
    public CompletableFuture<Void> deleteObjectFromBucketAsync(String bucket, String key) {
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest
                .builder()
                .bucket(bucket)
                .key(key)
                .build();
        CompletableFuture<DeleteObjectResponse> response = s3AsyncClient.deleteObject(deleteObjectRequest);

        return response.handle((deleteRes, ex) -> {
            if (ex != null) {
                throw SdkException.create("An S3 exception occurred during delete", ex);
            }
            log.info("The {}/{} was deleted", bucket, key);

            return null;
        });
    }

    /**
     * Upload file to S3 Bucket.
     *
     * @param bucket
     * @param key
     * @param requestBody
     * @return software.amazon.awssdk.services.s3.model.PutObjectResponse
     * @throws
     * @author Jack Lo
     * @date 2025/12/20 15:21
     */
    public PutObjectResponse uploadFile(String bucket, String key, RequestBody requestBody) {
        log.info("Uploading {} to {}", key, bucket);
        PutObjectResponse putObjectResponse = s3Client.putObject(request -> request.bucket(bucket).key(key), requestBody);
        log.info("Upload status: {}", putObjectResponse.sdkHttpResponse().isSuccessful());

        return putObjectResponse;
    }

    /**
     * Upload file to S3 Bucket.
     *
     * @param bucket
     * @param key
     * @param requestBody
     * @return java.util.concurrent.CompletableFuture<software.amazon.awssdk.services.s3.model.PutObjectResponse>
     * @throws
     * @author Jack Lo
     * @date 2025/12/20 15:24
     */
    public CompletableFuture<PutObjectResponse> uploadFileAsync(String bucket, String key, AsyncRequestBody requestBody) {
        log.info("Uploading {} to {}", key, bucket);
        CompletableFuture<PutObjectResponse> response = s3AsyncClient.putObject(request -> request.bucket(bucket).key(key), requestBody);

        return response.whenComplete((resp, ex) -> {
            if (ex != null) {
                throw SdkException.create("An S3 exception occurred during upload", ex);
            }
        });
    }

    /**
     * Upload directory to S3 Bucket.
     *
     * @param sourceDirectory
     * @param bucket
     * @param prefix
     * @return java.lang.Integer
     * @throws
     * @author Jack Lo
     * @date 2025/12/20 15:35
     */
    public Integer uploadDirectory(URI sourceDirectory, String bucket, String prefix) {
        DirectoryUpload directoryUpload = s3TransferManager.uploadDirectory(UploadDirectoryRequest.builder().source(Paths.get(sourceDirectory)).bucket(bucket).s3Prefix(prefix).build());
        CompletedDirectoryUpload completedDirectoryUpload = directoryUpload.completionFuture().join();
        completedDirectoryUpload.failedTransfers().forEach(fail -> log.warn("Object {}, failed to transfer", fail.toString()));

        return completedDirectoryUpload.failedTransfers().size();
    }

    /**
     * Decompress Gzip files to S3 Bucket.
     *
     * @param sourceBucket
     * @param sourceKey
     * @param destinationBucket
     * @param destinationPrefix
     * @return
     * @throws IOException
     * @author Jack Lo
     * @date 2025/12/20 15:49
     */
    public void decompressGzipFile(String sourceBucket, String sourceKey, String destinationBucket, String destinationPrefix) throws IOException {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder().bucket(sourceBucket).key(sourceKey).build();
        try (ResponseInputStream<GetObjectResponse> responseInputStream = s3Client.getObject(getObjectRequest, ResponseTransformer.toInputStream());
             GZIPInputStream gzipInputStream = new GZIPInputStream(responseInputStream)) {
            String fileName = sourceKey.substring(sourceKey.lastIndexOf('/')).replace(".gz", "");
            String targetKey = destinationPrefix + fileName;
            PutObjectRequest putObjectRequest = PutObjectRequest.builder().bucket(destinationBucket).key(targetKey).build();
            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(gzipInputStream.readAllBytes()));
            log.info("Successfully uncompressed {} into bucket {}", targetKey, destinationBucket);
        }
    }

    /**
     * Decompress path contains Gzip files to S3 Bucket.
     *
     * @param sourceBucket
     * @param sourcePrefix
     * @param destinationBucket
     * @param destinationPrefix
     * @return
     * @throws
     * @author Jack Lo
     * @date 2025/12/20 15:53
     */
    public void decompressGzipPrefix(String sourceBucket, String sourcePrefix, String destinationBucket, String destinationPrefix) {
        List<S3Object> s3ObjectList = listPrefixObjects(sourceBucket, sourcePrefix);
        s3ObjectList.stream()
                .map(S3Object::key)
                .filter(key -> key.endsWith(".gz"))
                .forEach(sourceKey -> {
                    try {
                        decompressGzipFile(sourceBucket, sourceKey, destinationBucket, destinationPrefix);
                    } catch (IOException e) {
                        log.error(e.getMessage());
                        throw SdkException.create(e.getMessage(), e);
                    }
                });
    }
}