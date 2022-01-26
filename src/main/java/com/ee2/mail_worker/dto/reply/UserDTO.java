package com.ee2.mail_worker.dto.reply;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
public class UserDTO {

    private long userID;
    private String customerCode;
    private String firstName;
    private String lastName;
    private String email;
    private long subSystemID;
    private String subSystem;
}
