package net.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @author Takanashi
 * @since 2021/12/15
 */

@Configuration
@PropertySource(value = "classpath:hotRank.properties", encoding = "UTF-8")
public class HotRankConfig {

    @Value("${hotRank.viewDivRatio}")
    private float viewDivRatio;

    @Value("${hotRank.commentRatio}")
    private float commentRatio;

    @Value("${hotRank.timeGravity}")
    private float timeGravity;

    public float getViewDivRatio() {
        return viewDivRatio;
    }

    public float getCommentRatio() {
        return commentRatio;
    }

    public float getTimeGravity() {
        return timeGravity;
    }
}
