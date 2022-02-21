package net.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @author Takanashi
 * @since 2021/12/15
 */

@Configuration
@PropertySource(value = "classpath:homepage.properties", encoding = "UTF-8")
public class HomepageConfig {
    // 初筛选个数
    @Value("${homepage.recommendCount}")
    private int recommendCount;

    // 浏览的tag加入推荐的阈值
    @Value("${homepage.recommendTagViewLimit}")
    private int recommendTagViewLimit;

    // 已浏览过的文章加入推荐的可能性降低
    @Value("${homepage.recommendHaveReadRatio}")
    private float recommendHaveReadRatio;

    public int getRecommendCount() {
        return recommendCount;
    }

    public int getRecommendTagViewLimit() {
        return recommendTagViewLimit;
    }

    public float getRecommendHaveReadRatio() {
        return recommendHaveReadRatio;
    }
}
