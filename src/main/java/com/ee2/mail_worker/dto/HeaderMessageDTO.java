package com.ee2.mail_worker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class HeaderMessageDTO {
    private String targetID;
    private String sourceID;
    private Date snt;
    private String subsystem = null;
    private String emailAddress = null;
    private String OBID = null;
    private String TLoc;
    private String D2Sub;
}
