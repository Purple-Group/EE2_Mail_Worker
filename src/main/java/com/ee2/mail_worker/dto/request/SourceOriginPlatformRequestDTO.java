package com.ee2.mail_worker.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SourceOriginPlatformRequestDTO {
    @ApiModelProperty(required = false, notes = "Yhe source origin of the mail bein sent", example = "19", hidden = true)
    private Integer sourceOriginPlatformID;
}
