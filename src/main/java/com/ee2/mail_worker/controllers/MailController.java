package com.ee2.mail_worker.controllers;

import com.ee2.mail_worker.dto.request.EmailByTemplateByEmailRequestDto;
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
public class MailController {
    private final MailService mailService;


    @GetMapping(value = "/test", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public String getTrustAccountValue() {
        return "Hello";
    }

    @PostMapping(value = "/emailByTemlateByUserID")
    public void EmailByTemlateByUserID(@RequestHeader(value = "sessionOriginPlatformID", required = false) String sessionOriginPlatformID,
                                       @RequestBody EmailByTemplateByUserIDRequestDto emailByTemplateByUserIDRequestDto) throws MailRequestException {

        if (sessionOriginPlatformID != null) {
            try {
                Integer sourceOriginPlatformID = Integer.valueOf(sessionOriginPlatformID);
                emailByTemplateByUserIDRequestDto.setSourceOriginPlatformID(sourceOriginPlatformID);
            } catch (NumberFormatException e) {
                //Do nothing
            }

            mailService.sendMailMessage(emailByTemplateByUserIDRequestDto);
        } else {

            mailService.sendMailMessage(emailByTemplateByUserIDRequestDto);
        }

    }

    @PostMapping(value = "/emailByTemlateByEmail")
    public void EmailByTemlateByEmail(@RequestHeader(value = "sessionOriginPlatformID", required = false) String sessionOriginPlatformID,
                                       @RequestBody EmailByTemplateByEmailRequestDto emailByTemplateByEmailRequestDto) throws MailRequestException {

        if (sessionOriginPlatformID != null) {
            try {
                Integer sourceOriginPlatformID = Integer.valueOf(sessionOriginPlatformID);
                emailByTemplateByEmailRequestDto.setSourceOriginPlatformID(sourceOriginPlatformID);
            } catch (NumberFormatException e) {
                //Do nothing
            }

            mailService.sendMailMessageByEmail(emailByTemplateByEmailRequestDto);
        } else {

            mailService.sendMailMessageByEmail(emailByTemplateByEmailRequestDto);
        }

    }
}