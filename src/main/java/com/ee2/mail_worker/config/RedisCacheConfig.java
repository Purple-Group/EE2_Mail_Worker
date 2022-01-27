package com.ee2.mail_worker.config;

import com.ee2.mail_worker.constants.CacheNames;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import java.time.Duration;


@Configuration
@Slf4j
public class RedisCacheConfig {

    @Bean
    public RedisCacheManager cacheManager_ExpireIn1Day(final RedisConnectionFactory connectionFactory) {
        RedisCacheConfiguration cacheConfiguration = RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofDays(1));
        RedisCacheManager.RedisCacheManagerBuilder builder = RedisCacheManager.builder(connectionFactory).cacheDefaults(cacheConfiguration);
        RedisCacheManager manager = builder.build();
        return manager;
    }

    @CacheEvict(key = "#key", value = CacheNames.USER_CACHE)
    public void evictEE_UserCacheByKey(String key) {
        log.info("EE_UserCache key FLUSHED : " + key);
    }
}
