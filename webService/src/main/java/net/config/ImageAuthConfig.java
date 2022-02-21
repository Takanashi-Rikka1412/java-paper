package net.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;


@Configuration
@PropertySource(value = "classpath:staticConfig/imageAuth.properties", encoding = "UTF-8")
public class ImageAuthConfig {

    @Value("${keys.authKey}")
    private String authKey;

    @Value("${keys.passwordFrontKey}")
    private String passwordFrontKey;

    @Value("${keys.passwordBackKey}")
    private String passwordBackKey;


    public String getAuthKey() {
        return authKey;
    }

    public String getPasswordFrontKey() {
        return passwordFrontKey;
    }

    public String getPasswordBackKey() {
        return passwordBackKey;
    }


}
