package com.fbs.app.controller;

import com.fbs.app.dto.UserRequestDto;
import com.fbs.app.model.UserModel;
import com.fbs.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/fbs")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/add-user")
    public ResponseEntity<String> addUser(@RequestBody UserRequestDto user ){
        userService.addUser(user);
        return ResponseEntity.ok("User Added");
    }

        @GetMapping("/add-user")
        public List<UserModel> getAllUser(){
            return userService.readUser();
        }

        @DeleteMapping("/add-user/{id}")
        public String delUser(@PathVariable Long id){
            userService.delUser(id);
            return "User Deleted";
        }
}


