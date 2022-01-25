package com.ee2.mail_worker.controllers;

import com.ee2.mail_worker.dto.request.EmailByTemplateByUserIDRequestDto;
import com.ee2.mail_worker.exceptions.MailRequestException;
import com.ee2.mail_worker.services.MailService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/mail")
@AllArgsConstructor
public class MainServiceController {
    private final MailService mailService;


    @GetMapping(value = "/test", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public String getTrustAccountValue() {
        return "Hello";
    }

@PostMapping( value = "/emailByTemlateByUserID")
public void EmailByTemlateByUserID( @RequestHeader(value="sessionID",  required = false) String sessionID,
        @RequestBody EmailByTemplateByUserIDRequestDto emailByTemplateByUserIDRequestDto) throws MailRequestException {

        if(sessionID != null){
            try{
                Integer sourceOriginPlatformID= Integer.valueOf(sessionID);
                emailByTemplateByUserIDRequestDto.setSourceOriginPlatformID(sourceOriginPlatformID);
                mailService.sendMailMessage(emailByTemplateByUserIDRequestDto);
            }catch (NumberFormatException e){
                log.error("SessionID: "+ sessionID+ " is not a vallid number");
            }
        }else{

        mailService.sendMailMessage(emailByTemplateByUserIDRequestDto);
        }



}

}