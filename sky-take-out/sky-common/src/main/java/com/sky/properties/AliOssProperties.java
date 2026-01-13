package com.sky.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author candong
 */
@Component
@ConfigurationProperties(prefix = "sky.aliyun.oss")
@Data
public class AliOssProperties {

    private String endpoint;
    private String bucketName;
    private String region;

}
