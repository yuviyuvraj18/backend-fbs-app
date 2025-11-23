package com.fbs.app.model;

import lombok.*;

import javax.persistence.*;
import java.sql.Date;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "transactions")

public class TransactionModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "transaction_type")
    private String transactionType;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "expense_type")
    private String expenseType;

    @Column(name = "fund_type")
    private String fundType;

    @Column(name = "amount")
    private Integer amount;

    @Column(name = "purpose")
    private String purpose;

    @Column(name = "transaction_file")
    private String transactionFile;

    @Column(name = "status")
    private Integer status;

    @Column(name = "create_date")
    private LocalDateTime createDate;

    @Column(name = "update_date")
    private LocalDateTime updateDate;

    @Column(name = "client_ip")
    private String clientIp;


}
