package com.fbs.app.repository;

import com.fbs.app.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface  UserRepository extends JpaRepository<UserModel,Long> {

    Optional<UserModel> findByEmail(String email);
    UserModel findByIdAndEmail(Long id ,String email);


}
