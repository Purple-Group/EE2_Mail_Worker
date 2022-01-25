package com.ee2.mail_worker.services;

import com.ee2.mail_worker.dto.request.EmailByTemplateByUserIDRequestDto;
import com.ee2.mail_worker.kafka.producer.MailProducer;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
public class MailService {

    private final MailProducer mailProducer;

    public void sendMailMessage(EmailByTemplateByUserIDRequestDto emailByTemplateByUserIDRequestDto){

        mailProducer.sendMailMessage(emailByTemplateByUserIDRequestDto);
    }


}
