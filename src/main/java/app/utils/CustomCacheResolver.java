package app.utils;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import org.jetbrains.annotations.NotNull;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.interceptor.CacheOperationInvocationContext;
import org.springframework.cache.interceptor.CacheResolver;

public class CustomCacheResolver implements CacheResolver {

    private final CacheManager cacheManager;

    public CustomCacheResolver(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @NotNull @Override
    public Collection<? extends Cache> resolveCaches(
        CacheOperationInvocationContext<?> context
    ) {
        String cacheName = Arrays
            .stream(
                context
                    .getTarget()
                    .getClass()
                    .getAnnotation(CacheConfig.class)
                    .value()
            )
            .iterator()
            .next();

        Cache cache = cacheManager.getCache(cacheName);

        return Collections.singletonList(cache);
    }
}
