package com.fbs.app.service;

import com.fbs.app.dto.TransactionRequestDto;
import com.fbs.app.dto.TransactionResponseDto;
import com.fbs.app.model.TransactionModel;
import com.fbs.app.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;



    public String addIncome(TransactionRequestDto transactionRequestDto){
        String success="SUCCESS";

        TransactionModel transactionModel = new TransactionModel();
        if(Objects.nonNull(transactionModel.getId()) && Objects.nonNull(transactionModel.getFundType())){
            transactionModel = transactionRepository.findByIdAndFundType(transactionRequestDto.getId(),transactionRequestDto.getFundType());
            if (Objects.isNull(transactionModel)) {
                throw new RuntimeException ("Invalid Signature Id!");
            }

        }
        else {
            transactionModel = new TransactionModel();
        }
        transactionModel.setTransactionType(transactionRequestDto.getTransactionType());
        transactionModel.setFundType(transactionRequestDto.getFundType());
        transactionModel.setExpenseType(transactionRequestDto.getExpenseType());
        transactionModel.setAmount(transactionRequestDto.getAmount());
        transactionModel.setPurpose(transactionRequestDto.getPurpose());
        transactionModel.setUserId(transactionRequestDto.getUserId());
        LocalDateTime localDateTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        transactionModel.setCreateDate(localDateTime);
        transactionRepository.save(transactionModel);


        return success;
    }

    public List<TransactionResponseDto> readIncome(String type){
        
       List<TransactionModel> transactionList = transactionRepository.findByTransactionType(type);

        List<TransactionResponseDto> responseList = new ArrayList<>();

        for (TransactionModel model : transactionList) {

            TransactionResponseDto responseDto = new TransactionResponseDto();

            if (Objects.nonNull(model.getCreateDate())){

                LocalDateTime cdate = model.getCreateDate();

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

                String formattedDate = cdate.format(formatter);

                responseDto.setCreateDate(formattedDate);
            }

            responseDto.setId(model.getId());
            responseDto.setTransactionType(model.getTransactionType());
            responseDto.setAmount(model.getAmount());
            responseDto.setFundType(model.getFundType());
            responseDto.setExpenseType(model.getExpenseType());
            responseDto.setPurpose(model.getPurpose());
            responseDto.setUserId(model.getUserId());

            responseList.add(responseDto);

        }
        return responseList;
    }

    public void delIncome(Long id){
        if(!transactionRepository.existsById(id)) throw new RuntimeException("Transaction not found with this ID:");

        transactionRepository.deleteById(id);
    }


}

