package com.ee2.mail_worker.dto;

import com.ee2.mail_worker.dto.reply.UserVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MailVo {
    private Object valueObject = null;
    private UserVo userVo = null;
    private String subject = null;
    private String identifier = null;
    private String bccAddress = null;
    private String executionDateString = null;
    private String sessionOriginPlatformID = null;
    private long tradingCurrencyTypeId = 0;
    private String formattedDisplayTickSize = null;
    private boolean sendAsSMS = false;
    private boolean sendAsEmail = false;
//    private List<ClientPrefMailVO> clientPrefMailVOList;
}
