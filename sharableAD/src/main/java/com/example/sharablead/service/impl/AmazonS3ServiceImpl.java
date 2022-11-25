package com.example.sharablead.service.impl;


import com.amazonaws.AmazonServiceException;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.example.sharablead.common.GlobalResponse;
import com.example.sharablead.entity.Ad;
import com.example.sharablead.enums.GlobalResponseEnum;
import com.example.sharablead.enums.LikeStatusEnum;
import com.example.sharablead.response.UploadVO;
import com.example.sharablead.service.AdService;
import com.example.sharablead.service.AmazonS3Service;
import com.example.sharablead.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Amazon S3存储服务实现
 */
@Slf4j
@Service
public class AmazonS3ServiceImpl implements AmazonS3Service {

    /**
     * bucket
     */
    @Value("${amazon.s3.bucket-name}")
    private String bucketName;
    /**
     * accessKey
     */
    @Value("${amazon.s3.access-key}")
    private String accessKey;
    /**
     * secretKey
     */
    @Value("${amazon.s3.secret-key}")
    private String secretKey;
    /**
     * endpoint
     */
    @Value("${amazon.s3.endpoint}")
    private String endpoint;

    @Value("${amazon.s3.region}")
    private String region;

    @Value("${amazon.s3.origin-file-path}")
    private String originFilePath;

    @Value("${amazon.s3.compress-file-path}")
    private String compressFilePath;

    @Value("${amazon.s3.ad-file-path}")
    private String adFilePath;

    @Value("${amazon.s3.video-file-path}")
    private String videoFilePath;

    @Value("${amazon.s3.visit-url-prefix}")
    private String visitUrlPrefix;
    /**
     * aws s3 client
     */
    AmazonS3 s3 = null;

    private static final String IMAGE = "image";

    private static final String VIDEO = "video";

    @Autowired
    private AdService adService;

    @Override
    public GlobalResponse upload(MultipartFile file) {
        if (Objects.isNull(file) || StringUtils.isEmpty(file.getContentType())){
            return GlobalResponse.error(GlobalResponseEnum.ERROR.getCode(), "invalid file");
        }

        if (!file.getContentType().startsWith(IMAGE) && !file.getContentType().startsWith(VIDEO)){
            return GlobalResponse.error(GlobalResponseEnum.ERROR.getCode(), "unsupported file type");
        }

        if (file.getContentType().startsWith(IMAGE) && file.getSize() > 25165824){
            return GlobalResponse.error(GlobalResponseEnum.ERROR.getCode(), "image file is over 3M");
        }

        if (file.getContentType().startsWith(VIDEO) && file.getSize() > 83886080){
            return GlobalResponse.error(GlobalResponseEnum.ERROR.getCode(), "video file is over 10M");
        }
        String tempFileName = UUID.randomUUID().toString().replace("-", "") + "." + FilenameUtils.getExtension(file.getOriginalFilename());
        String contentType = file.getContentType();
        long fileSize = file.getSize();
        String dateDir = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String tempBucketName = bucketName + originFilePath + dateDir;
        String filePath = originFilePath + dateDir + "/" + tempFileName;
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(contentType);
        objectMetadata.setContentLength(fileSize);
        try {

            if (contentType.startsWith(IMAGE)){
                PutObjectResult putObjectResult = s3.putObject(tempBucketName, tempFileName, file.getInputStream(), objectMetadata);
                //图片压缩
                tempBucketName = bucketName + compressFilePath + dateDir;
                ObjectMetadata objectMetadata1 = new ObjectMetadata();
                objectMetadata1.setContentType(contentType);
                MultipartFile compressFile = FileUtil.compressFile(file);
                objectMetadata1.setContentLength(compressFile.getSize());
                PutObjectResult putObjectResult1 = s3.putObject(tempBucketName, tempFileName, compressFile.getInputStream(), objectMetadata1);
            }

            if (contentType.startsWith(VIDEO)){
                tempBucketName = bucketName + videoFilePath + dateDir;
                filePath = videoFilePath + dateDir + "/" + tempFileName;
                PutObjectResult putObjectResult = s3.putObject(tempBucketName, tempFileName, file.getInputStream(), objectMetadata);
            }

        } catch (AmazonServiceException e) {
            return GlobalResponse.error(GlobalResponseEnum.ERROR.getCode(), "amazon Exception");
        } catch (IOException e) {
            return GlobalResponse.error(GlobalResponseEnum.ERROR.getCode(), "IO Exception");
        } catch (Exception e) {
            log.error(e.getMessage());
            return GlobalResponse.error(GlobalResponseEnum.ERROR.getCode(), "file convert Exception");
        }

        UploadVO vo = new UploadVO();
        vo.setOriginUrl(visitUrlPrefix + filePath);
        if (contentType.startsWith(IMAGE)){
            vo.setType(IMAGE);
        }
        if (contentType.startsWith(VIDEO)){
            vo.setType(VIDEO);
        }
        return GlobalResponse.success(vo);
    }

    @Override
    public GlobalResponse uploadAd(MultipartFile file, Long userId) {
        LambdaQueryWrapper<Ad> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Ad::getUserId, userId);
        List<Ad> list = adService.list(lambdaQueryWrapper);

        if (CollectionUtils.isEmpty(list)){
            return GlobalResponse.error(GlobalResponseEnum.ERROR.getCode(), "invalid user");
        }

        if (Objects.isNull(file) || StringUtils.isEmpty(file.getContentType())){
            return GlobalResponse.error(GlobalResponseEnum.ERROR.getCode(), "invalid file");
        }

        if (!file.getContentType().startsWith(IMAGE)){
            return GlobalResponse.error(GlobalResponseEnum.ERROR.getCode(), "unsupported file type");
        }

        if (file.getContentType().startsWith(IMAGE) && file.getSize() > 25165824){
            return GlobalResponse.error(GlobalResponseEnum.ERROR.getCode(), "image file is over 3M");
        }

        String tempFileName = UUID.randomUUID().toString().replace("-", "") + "." + FilenameUtils.getExtension(file.getOriginalFilename());
        String contentType = file.getContentType();
        String dateDir = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String tempBucketName = bucketName + adFilePath + dateDir;
        String filePath = adFilePath + dateDir + "/" + tempFileName;

        if (contentType.startsWith(IMAGE)){
            try {
                MultipartFile resizeFile = FileUtil.resizeFile(file);
                long fileSize = resizeFile.getSize();
                ObjectMetadata objectMetadata = new ObjectMetadata();
                objectMetadata.setContentType(contentType);
                objectMetadata.setContentLength(fileSize);
                PutObjectResult putObjectResult = s3.putObject(tempBucketName, tempFileName, resizeFile.getInputStream(), objectMetadata);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return GlobalResponse.success(visitUrlPrefix + filePath);
    }

    @PostConstruct
    public void init() {

        ClientConfiguration config = new ClientConfiguration();

        AwsClientBuilder.EndpointConfiguration endpointConfig =
                new AwsClientBuilder.EndpointConfiguration(endpoint, region);

        AWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
        AWSCredentialsProvider awsCredentialsProvider = new AWSStaticCredentialsProvider(awsCredentials);

        s3 = AmazonS3Client.builder()
                .withEndpointConfiguration(endpointConfig)
                .withClientConfiguration(config)
                .withCredentials(awsCredentialsProvider)
                .disableChunkedEncoding()
                .withPathStyleAccessEnabled(true)
                .build();
    }

}
