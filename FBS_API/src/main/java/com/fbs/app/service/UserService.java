package com.fbs.app.service;

import com.fbs.app.dto.UserRequestDto;
import com.fbs.app.model.EventModel;
import com.fbs.app.model.UserModel;
import com.fbs.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;



    public String addUser(UserRequestDto userRequestDto){
        String success="SUCCESS";

        UserModel userModel;
        if(Objects.nonNull(userRequestDto.getId()) && Objects.nonNull(userRequestDto.getEmail())){
            userModel = userRepository.findByIdAndEmail(userRequestDto.getId(),userRequestDto.getEmail());
            if (Objects.isNull(userModel)) {
                throw new RuntimeException ("Invalid Signature Id!");
            }

        }
        else {
            userModel = new UserModel();
        }
        userModel.setUserType(userRequestDto.getUserType());
        userModel.setUserName(userRequestDto.getUserName());
        userModel.setEmail(userRequestDto.getEmail());
        userModel.setPassword(userRequestDto.getPassword());
        userModel.setGender(userRequestDto.getGender());
        userModel.setClientIp(userRequestDto.getClientIp());
        userModel.setFamilyId(userRequestDto.getFamilyId());
        userModel.setMobileNo(userRequestDto.getMobileNo());
        userModel.setStatus(userRequestDto.getStatus());

        LocalDateTime localDateTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        userModel.setCreateDate(localDateTime);
        userRepository.save(userModel);

        return success;
    }

    public List<UserModel> readUser(){
        return userRepository.findAll();
    }

    public void delUser(Long id){
        if(!userRepository.existsById((id))) throw new RuntimeException("Transaction not found with this ID:");

        userRepository.deleteById((id));
    }


}
