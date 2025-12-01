package app.config;

import app.utils.CustomCacheResolver;
import com.github.benmanes.caffeine.cache.Caffeine;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CacheConfig {

    @Value("${cache-config.initial-capacity}")
    private int initialCapacity;

    @Value("${cache-config.maximum-size}")
    private int maximumSize;

    @Value("${cache-config.expiration-minutes}")
    private int expirationMinutes;

    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();

        cacheManager.setCaffeine(
            Caffeine
                .newBuilder()
                .initialCapacity(initialCapacity)
                .maximumSize(maximumSize)
                .expireAfterWrite(expirationMinutes, TimeUnit.MINUTES)
        );

        return cacheManager;
    }

    @Bean
    public CustomCacheResolver customCacheResolver(CacheManager cacheManager) {
        return new CustomCacheResolver(cacheManager);
    }
}
