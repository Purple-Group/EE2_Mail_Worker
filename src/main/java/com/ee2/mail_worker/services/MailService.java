package com.ee2.mail_worker.services;

import com.ee2.mail_worker.dao.SubSystemDAO;
import com.ee2.mail_worker.dao.UserDAO;
import com.ee2.mail_worker.dao.entities.SubSystemEntity;
import com.ee2.mail_worker.dao.entities.UsersEntity;
import com.ee2.mail_worker.dto.reply.UserDTO;
import com.ee2.mail_worker.dto.reply.UserVo;
import com.ee2.mail_worker.dto.request.EmailByTemplateByEmailRequestDto;
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
    private final UserDAO userDAO;
    private final SubSystemDAO subSystemDAO;


    public void sendMailMessage(EmailByTemplateByUserIDRequestDto emailByTemplateByUserIDRequestDto){

        UsersEntity usersEntity = userDAO.findById(emailByTemplateByUserIDRequestDto.getUserID());
        SubSystemEntity subSystemEntity = subSystemDAO.findById(usersEntity.getSubSystemId());
        String customerCode = (usersEntity.getCustomerCode() != null) ? usersEntity.getCustomerCode() : "null";
        UserDTO userDTO = UserDTO.builder()
                .customerCode(usersEntity.getCustomerCode().trim())
                .email(usersEntity.getEmail().trim())
                .firstName(usersEntity.getFirstName().trim())
                .lastName(usersEntity.getLastName().trim())
                .userID(usersEntity.getUserId())
                .subSystem(subSystemEntity.getSubSystem())
                .subSystemID(subSystemEntity.getSubSystemId())
                .build();

        UserVo userVo = UserVo.builder()
                .userId(userDTO.getUserID())
                .customerCode(customerCode)
                .subSystem(userDTO.getSubSystem())
                .emailAddress(userDTO.getEmail())
                .subSystemID((int) userDTO.getSubSystemID())
                .firstName(userDTO.getFirstName())
                .lastName(userDTO.getLastName())
                .build();

        mailProducer.sendMailMessage(emailByTemplateByUserIDRequestDto, userVo);
    }


    public void sendMailMessageByEmail(EmailByTemplateByEmailRequestDto emailByTemplateByEmailRequestDto) {

        Integer subsystemId = (emailByTemplateByEmailRequestDto.getSubsystemId() != null)? emailByTemplateByEmailRequestDto.getSubsystemId() : 1;
        SubSystemEntity subSystemEntity = subSystemDAO.findById(subsystemId);
        UserDTO userDTO = UserDTO.builder()
                .customerCode("null")
                .email(emailByTemplateByEmailRequestDto.getEmail().trim())
                .firstName("")
                .lastName("")
                .userID(0L)
                .subSystem(subSystemEntity.getSubSystem().trim())
                .subSystemID(subSystemEntity.getSubSystemId())
                .build();

        UserVo userVo = UserVo.builder()
                .userId(userDTO.getUserID())
                .customerCode("null")
                .subSystem(userDTO.getSubSystem())
                .emailAddress(userDTO.getEmail())
                .subSystemID((int) userDTO.getSubSystemID())
                .firstName(userDTO.getFirstName())
                .lastName(userDTO.getLastName())
                .build();

        mailProducer.sendMailMessage(emailByTemplateByEmailRequestDto, userVo);
    }
}
