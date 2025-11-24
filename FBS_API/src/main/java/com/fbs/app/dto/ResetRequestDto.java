package com.fbs.app.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ResetRequestDto {
    private String email;

    private String otp;

    private String newpassword;
}
