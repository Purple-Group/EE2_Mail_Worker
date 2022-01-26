package com.ee2.mail_worker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailBodyGrpBlockDTO {
    private String XSLT;
    private String value;
}
