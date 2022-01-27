package com.ee2.mail_worker.services;

import com.ee2.mail_worker.config.RedisCacheConfig;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
public class UserService {

    private final RedisCacheConfig redisCacheConfig;

    public void evictUserCacheByKey(String key){
        redisCacheConfig.evictEE_UserCacheByKey(key);
    }
}
