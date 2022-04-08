package com.ee2.mail_worker.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Data
@ApiModel(description = "Email Template by Email request DTO")
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailByTemplateByEmailRequestDto extends SourceOriginPlatformRequestDTO {
    @ApiModelProperty(required = true, notes = "The email of the client the mail will be sent to", example = "joe@easequities.io")
    private String email;
    @ApiModelProperty(required = true, notes = "The Subsystem ID of the client the mail will be sent to, if not sent then the default will be used", example = "2")
    private Integer subsystemId;
    @ApiModelProperty(required = true, notes = "The template to be used for the mail being sent", example = "Welcome.xml")
    private String template;
    @ApiModelProperty(required = true, notes = "The Mail subject for the email", example = "Welcome to Easy Equities")
    private String subject; //comments
    @ApiModelProperty(required = true, notes = "The Xml mail value object to be used", example = "<Some-XML><userID>1234<userID/><Some-XML/>")
    private String mailVo; //comments

}
