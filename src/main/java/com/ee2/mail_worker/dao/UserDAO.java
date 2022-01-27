package com.ee2.mail_worker.dao;

import com.ee2.mail_worker.constants.CacheNames;
import com.ee2.mail_worker.dao.entities.UsersEntity;
import com.ee2.mail_worker.dao.repositories.UserRepository;
import com.ee2.mail_worker.exceptions.CannotRetrieveDataFromSourceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
@Slf4j
public class UserDAO {
    @Autowired
    private UserRepository userRepository;
    @Value("${gt247cfd_data_access_api_url}")
    private String sourceUri;
    @Autowired
    RestTemplate restTemplate;

    @Cacheable(value = CacheNames.USER_CACHE, key = "#userId", unless = "#result == null", cacheManager = "cacheManager_ExpireIn1Day")
    public UsersEntity findById(Long userId) {
        //read from local database... if not found then call source and save local
//        Optional<UsersEntity> usersEntity = userRepository.findById(userId);
//        if (!usersEntity.isPresent()) {
//            return getDataFromSource(userId);
//        }
//        return usersEntity.get();
        return getDataFromSource(userId);
    }

    //    @Override
//    @CacheEvict(value = CacheNames.USER_CACHE, key = "#DTO.userId")
    public void save(UsersEntity usersEntity) {
        userRepository.save(usersEntity);
    }


    //    @Override
    public UsersEntity getDataFromSource(Long userId) throws CannotRetrieveDataFromSourceException {
        //get from source
        String fooResourceUrl = sourceUri + "/userEntities/";
        try {
            ResponseEntity<UsersEntity> response = restTemplate.getForEntity(fooResourceUrl + userId, UsersEntity.class);
            UsersEntity usersEntity = new UsersEntity();
            UsersEntity sourceEntity = response.getBody();
            BeanUtils.copyProperties(sourceEntity, usersEntity);
            usersEntity.setUserId(userId);
            // mongoEntity.setId(userId);
            //Leaving this out untill we have way to update user data that changed
//            return userRepository.save(usersEntity);
            log.info("Fetched data from Source for userId: " + userId);
            return usersEntity;
        } catch (RestClientException e) {
            throw new CannotRetrieveDataFromSourceException("Cannot access user data from source api: " + e);
        }
    }
}