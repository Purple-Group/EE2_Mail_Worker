package com.ee2.mail_worker.controllers;

import com.ee2.mail_worker.services.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/users")
@AllArgsConstructor
public class UserDetailsController {

    private final UserService userService;

    @GetMapping(value = "/evictCachedUserDataByUserId", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public String evictUserDetailsByUserId(@RequestParam long userId){

        String key = String.valueOf(userId);
        userService.evictUserCacheByKey(key);

        return "User : " + key + " flushed from Redis cache :";

    }


}
