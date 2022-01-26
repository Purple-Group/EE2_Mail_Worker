package com.ee2.mail_worker.dto.reply;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserVo {
    public long userId;
    public String userRole;
    public String firstName;
    public String lastName;
    public String subSystem;
    public int subSystemID;
    private String emailAddress;
    private String contactNumber;
    private String url;
    private String[] alternateEMailAddresses;
    private String customerCode;

    public String getPID() {
        return this.customerCode;
    }
}
