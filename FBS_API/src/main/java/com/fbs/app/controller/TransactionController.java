package com.fbs.app.controller;

import com.fbs.app.dto.TransactionRequestDto;
import com.fbs.app.dto.TransactionResponseDto;
import com.fbs.app.model.TransactionModel;
import com.fbs.app.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fbs")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/add-funds")
    public ResponseEntity<String> addIncome(@RequestBody TransactionRequestDto income ){
       transactionService.addIncome(income);

       return ResponseEntity.ok("Fund Added");
    }

    @GetMapping("/add-funds")
    public List<TransactionResponseDto> getAllIncomes(@RequestParam String type ){
        return transactionService.readIncome(type);
    }

    @DeleteMapping("/add-funds/{id}")
    public String delIncome(@PathVariable Long id){
        transactionService.delIncome(id);
        return "Transaction Deleted";
    }

}
