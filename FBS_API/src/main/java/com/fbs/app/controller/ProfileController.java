package com.fbs.app.controller;

import com.fbs.app.model.UserModel;
import com.fbs.app.repository.UserRepository;
import com.fbs.app.service.ProfileService;
import com.fbs.app.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.Map;

@RestController
@RequestMapping("/api/fbs")
public class ProfileController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProfileService profileService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(@RequestHeader("Authorization") String authHeader) {

        return ResponseEntity.ok(profileService.getProfile(authHeader));

    }


    // ============== UPDATE PROFILE (name, phone) ===============
    @PutMapping("/profile/update")
    public ResponseEntity<?> updateProfile(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody UserModel updatedUser) {

        if (authHeader == null) {
            throw new RuntimeException("Authorization header missing!");
        }
        System.out.println("controller===========>>>>" +authHeader);

        return
                ResponseEntity.ok(profileService.updateProfile(authHeader, updatedUser));
    }


    // ============== CHANGE PASSWORD ===============
    @PutMapping("/profile/change-password")
    public ResponseEntity<?> changePassword(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody Map<String, String> reqBody) {

        return ResponseEntity.ok(profileService.changePassword(authHeader, reqBody));

    }


    // ============== UPLOAD PROFILE IMAGE ===============
    @PostMapping("/profile/upload-image")
    public ResponseEntity<?> uploadImage(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam("file") MultipartFile file) throws Exception {

        return ResponseEntity.ok(profileService.uploadProfileImage(authHeader, file));

    }

}

