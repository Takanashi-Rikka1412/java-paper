package net.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @author Takanashi
 * @since 2021/12/15
 */

@Configuration
@PropertySource(value = "classpath:time.properties", encoding = "UTF-8")
public class TimeConfig {

    @Value("${time.pattern}")
    private String pattern;

    public String getPattern() {
        return pattern;
    }
}
