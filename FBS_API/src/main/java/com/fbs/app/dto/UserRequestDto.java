package com.fbs.app.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserRequestDto {

    private Long id;
    @NotNull
    private String userName;
    @NotNull
    private String email;
    @NotNull
    private String password;
    @NotNull
    private String userType;
    @NotNull
    private Integer mobileNo;

    private String gender;

    private String familyId;

    private Integer status;

    private String dateOfBirth;

    private String createDate;

    private String updateDate;

    private String clientIp;
}
