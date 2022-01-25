package com.ee2.mail_worker.dto.request;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Data
@ApiModel(description = "Email Template by User ID request DTO")
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailByTemplateByUserIDRequestDto extends SourceOriginPlatformRequestDTO{
    @ApiModelProperty(required = true, notes = "The UserID of the client the mail will be sent to", example = "45311")
    private Long userID;
    @ApiModelProperty(required = true, notes = "The template to be used for the mail being sent", example = "Welcome.xml")
    private String template;
    @ApiModelProperty(required = true, notes = "The Mail subject for the email", example = "Welcome to Easy Equities")
    private String subject; //comments
    @ApiModelProperty(required = true, notes = "The Xml mail value object to be used", example = "<Some-XML><userID>1234<userID/><Some-XML/>")
    private String mailVo; //comments

}
