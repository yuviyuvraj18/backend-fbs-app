package com.fbs.app.dto;

import lombok.*;

import javax.validation.constraints.NotNull;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserResponseDto {

    private Long id;

    private String userName;

    private String email;

    private String userType;

    private Integer mobileNo;


    private String gender;

    private String familyId;

    private String status;

    private String dateOfBirth;

    private String createDate;

    private String updateDate;

    private String clientIp;
}
