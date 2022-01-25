package com.ee2.mail_worker.kafka.messages;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KafkaMailMessage {
    private String targetID;
    private String sourceID;
    private String subsystem;
    private String sessionOriginPlatformID;
    private String emailAddress;
    private String OBID;
    private String TLoc;
    private String D2Sub;
    private String subject;
    private String messageTemplate;
    private String tradeMailXml;
}
