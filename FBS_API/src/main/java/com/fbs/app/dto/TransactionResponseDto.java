package com.fbs.app.dto;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TransactionResponseDto {
    private Long id;

    private String transactionType;

    private Integer userId;

    private String expenseType;

    private String fundType;

    private Integer amount;

    private String purpose;

    private String transactionFile;

    private Integer status;

    private String createDate;

    private String updateDate;

    private String clientIp;
}
