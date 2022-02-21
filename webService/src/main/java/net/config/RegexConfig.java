package net.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @author Takanashi
 * @since 2021/12/13
 */

@Configuration
@PropertySource(value = "classpath:regex.properties", encoding = "UTF-8")
public class RegexConfig {

    @Value("${regex.nameRegex}")
    private String nameRegex;

    @Value("${regex.passwordRegex}")
    private String passwordRegex;

    public String getNameRegex() {
        return nameRegex;
    }

    public String getPasswordRegex() {
        return passwordRegex;
    }
}
