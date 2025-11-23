package com.fbs.app.dto;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString

public class TransactionRequestDto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String transactionType;

    private Integer userId;
    @NotNull
    private String expenseType;
    @NotNull
    private String fundType;
    @NotNull
    private Integer amount;

    private String purpose;

    private String transactionFile;

    private Integer status;

    private LocalDateTime createDate;

    private LocalDateTime updateDate;

    private String clientIp;
}
