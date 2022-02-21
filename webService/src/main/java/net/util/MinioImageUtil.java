package net.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource(value = "classpath:staticConfig/minio.properties",encoding = "UTF-8")
public class MinioImageUtil {

    /**
     * API调用地址
     */
    @Value("${image.url}")
    private String url;
    /**
     * 连接账号
     */
    @Value("${image.accesskey}")
    private String accessKey;
    /**
     * 连接秘钥
     */
    @Value("${image.secretkey}")
    private String secretKey;

    @Value("${image.bucketname}")
    private String bucketname;

    public String getAccessKey() {
        return accessKey;
    }

    public String getBucketname() {
        return bucketname;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getUrl() {
        return url;
    }

    public void setBucketname(String bucketname) {
        this.bucketname = bucketname;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
