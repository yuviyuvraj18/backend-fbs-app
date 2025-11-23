package com.fbs.app.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenRefreshRequestDto {
    private String refreshToken;
}
