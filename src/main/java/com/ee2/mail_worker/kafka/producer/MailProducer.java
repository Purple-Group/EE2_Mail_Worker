package com.ee2.mail_worker.kafka.producer;

import com.easybackend.shared.DTO.rest.account_processor.reply.TrustAccountReplyDTO;
import com.easybackend.shared.kafka.KafkaTopics;
import com.easybackend.shared.kafka.messages.TrustAccountUpdateMessageDTO;
import com.ee2.mail_worker.dto.reply.UserVo;
import com.ee2.mail_worker.dto.request.EmailByTemplateByEmailRequestDto;
import com.ee2.mail_worker.dto.request.EmailByTemplateByUserIDRequestDto;
import com.ee2.mail_worker.kafka.messages.KafkaMailMessage;
import com.ee2.mail_worker.kafka.messages.MailMessage;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;

@Service
@Slf4j
public class MailProducer {
    Gson gson = new Gson();

    SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Transactional(transactionManager = "kafkaTransactionManager")
    public void sendMailMessage(KafkaMailMessage mailMessage){
        String KEY = mailMessage.getOBID()+"";
        String json = gson.toJson(mailMessage);
        kafkaTemplate.send(KafkaTopics.TOPIC_MAIL_MQ, KEY,json);
        log.info("Sent Mail: " + mailMessage.getMessageTemplate() + " UserID: " + mailMessage.getTargetID());
    }
    // @TrackExecutionTime
    @Transactional(transactionManager = "kafkaTransactionManager")
    public void sendMailMessage(EmailByTemplateByUserIDRequestDto emailByTemplateByUserIDRequestDto, UserVo userVo){
        KafkaMailMessage messageDTO = new KafkaMailMessage();
        messageDTO.setTargetID(userVo.getUserId() + ":" + userVo.getCustomerCode());
        messageDTO.setSourceID(userVo.getSubSystem());
        messageDTO.setSessionOriginPlatformID(String.valueOf(emailByTemplateByUserIDRequestDto.getSourceOriginPlatformID()));
        messageDTO.setEmailAddress(userVo.getEmailAddress());
        messageDTO.setOBID(String.valueOf(userVo.getSubSystemID()));
        messageDTO.setD2Sub("");
        messageDTO.setTLoc(userVo.getFirstName() + " " + userVo.getLastName());
        messageDTO.setSubject(emailByTemplateByUserIDRequestDto.getSubject());
        messageDTO.setMessageTemplate(emailByTemplateByUserIDRequestDto.getTemplate());
        messageDTO.setTradeMailXml(emailByTemplateByUserIDRequestDto.getMailVo());
        this.sendMailMessage(messageDTO);
    }

    @Transactional(transactionManager = "kafkaTransactionManager")
    public void sendMailMessage(EmailByTemplateByEmailRequestDto emailByTemplateByEmailRequestDto, UserVo userVo){
        KafkaMailMessage messageDTO = new KafkaMailMessage();
        messageDTO.setTargetID(userVo.getUserId() + ":" + userVo.getCustomerCode());
        messageDTO.setSourceID(userVo.getSubSystem());
        messageDTO.setSessionOriginPlatformID(String.valueOf(emailByTemplateByEmailRequestDto.getSourceOriginPlatformID()));
        messageDTO.setEmailAddress(userVo.getEmailAddress());
        messageDTO.setOBID(String.valueOf(userVo.getSubSystemID()));
        messageDTO.setD2Sub("");
        messageDTO.setTLoc(userVo.getFirstName() + " " + userVo.getLastName());
        messageDTO.setSubject(emailByTemplateByEmailRequestDto.getSubject());
        messageDTO.setMessageTemplate(emailByTemplateByEmailRequestDto.getTemplate());
        messageDTO.setTradeMailXml(emailByTemplateByEmailRequestDto.getMailVo());
        this.sendMailMessage(messageDTO);
    }
}
