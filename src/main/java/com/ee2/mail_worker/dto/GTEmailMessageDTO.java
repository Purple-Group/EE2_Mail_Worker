package com.ee2.mail_worker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GTEmailMessageDTO {
    private String emailThreadID = "0";
    private String subject;
    private EmailBodyGrpBlockDTO emailBodyGrpBlockDTO;
    private HeaderMessageDTO headerMessageDTO;
    private String emailTyp = "0";
    private AttachmentDTO attachmentDTO = null;
}
