package com.ee2.mail_worker.kafka.producer;

import com.easybackend.shared.DTO.rest.account_processor.reply.TrustAccountReplyDTO;
import com.easybackend.shared.kafka.KafkaTopics;
import com.easybackend.shared.kafka.messages.TrustAccountUpdateMessageDTO;
import com.ee2.mail_worker.dto.request.EmailByTemplateByUserIDRequestDto;
import com.ee2.mail_worker.kafka.messages.KafkaMailMessage;
import com.ee2.mail_worker.kafka.messages.MailMessage;
import com.google.gson.Gson;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;

@Service
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
    }
    // @TrackExecutionTime
    @Transactional(transactionManager = "kafkaTransactionManager")
    public void sendMailMessage(EmailByTemplateByUserIDRequestDto emailByTemplateByUserIDRequestDto){
        KafkaMailMessage messageDTO = new KafkaMailMessage();
       messageDTO.setOBID(String.valueOf(emailByTemplateByUserIDRequestDto.getUserID()));
       messageDTO.setSubject(emailByTemplateByUserIDRequestDto.getSubject());
       messageDTO.setTradeMailXml(emailByTemplateByUserIDRequestDto.getMailVo());
        this.sendMailMessage(messageDTO);
    }
}
