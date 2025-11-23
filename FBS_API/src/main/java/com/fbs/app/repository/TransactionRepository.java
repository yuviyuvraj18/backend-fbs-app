package com.fbs.app.repository;

import com.fbs.app.model.TransactionModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionModel,Long> {

     TransactionModel findByIdAndFundType(Long id, String fundType);
     List<TransactionModel> findByTransactionType(String transactionType);
}
