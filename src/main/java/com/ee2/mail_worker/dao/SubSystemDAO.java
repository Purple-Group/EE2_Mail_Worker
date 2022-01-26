package com.ee2.mail_worker.dao;

import com.ee2.mail_worker.dao.entities.SubSystemEntity;
import com.ee2.mail_worker.dao.entities.UsersEntity;
import com.ee2.mail_worker.dao.repositories.SubSystemRepository;
import com.ee2.mail_worker.dao.repositories.UserRepository;
import com.ee2.mail_worker.exceptions.CannotRetrieveDataFromSourceException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class SubSystemDAO{
    @Autowired
    private SubSystemRepository subSystemRepository;
    @Value("${gt247cfd_data_access_api_url}")
    private String sourceUri;
    @Autowired
    RestTemplate restTemplate;

    //    @Cacheable(value = CacheNames.USER_CACHE, key = "#userId")
    public SubSystemEntity findById(Integer subSystemId) {
        //read from local database... if not found then call source and save local
        Optional<SubSystemEntity> subSystemEntity = subSystemRepository.findById(subSystemId);
        if (!subSystemEntity.isPresent()) {
            return getDataFromSource(subSystemId);
        }
        return subSystemEntity.get();
    }

    //    @Override
//    @CacheEvict(value = CacheNames.USER_CACHE, key = "#DTO.userId")
    public void save(SubSystemEntity subSystemEntity) {
        subSystemRepository.save(subSystemEntity);
    }


    //    @Override
    public SubSystemEntity getDataFromSource(Integer subSystemId) throws CannotRetrieveDataFromSourceException {
        //get from source
        String fooResourceUrl = sourceUri + "/subSystemEntities/";
        try {
            ResponseEntity<SubSystemEntity> response = restTemplate.getForEntity(fooResourceUrl + subSystemId, SubSystemEntity.class);
            SubSystemEntity subSystemEntity = new SubSystemEntity();
            SubSystemEntity sourceEntity = response.getBody();
            BeanUtils.copyProperties(sourceEntity, subSystemEntity);
            subSystemEntity.setSubSystemId(subSystemId);
            // mongoEntity.setId(userId);
            return subSystemRepository.save(subSystemEntity);
        } catch (RestClientException e) {
            throw new CannotRetrieveDataFromSourceException("Cannot access user data from source api: " + e);
        }
    }
}