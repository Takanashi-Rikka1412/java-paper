package net.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.stereotype.Component;

/**
 * @author Takanashi
 * @since 2021/11/30
 */

@Configuration
@PropertySource(value = "classpath:keys.properties", encoding = "UTF-8")
public class KeysConfig {

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
