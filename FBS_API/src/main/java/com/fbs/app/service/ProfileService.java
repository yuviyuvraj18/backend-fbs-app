package com.fbs.app.service;

import com.fbs.app.model.UserModel;
import com.fbs.app.repository.UserRepository;
import com.fbs.app.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

@Service
public class ProfileService {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    // =================== GET PROFILE ======================
    public UserModel getProfile(String authHeader) {
        Long userId = extractUserId(authHeader);
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }


    // =================== UPDATE PROFILE ======================
    public String updateProfile(String authHeader, UserModel updatedUser) {

        if (authHeader == null) {
            throw new RuntimeException("Authorization header missing!");
        }

        Long userId = extractUserId(authHeader);

        UserModel user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setUserName(updatedUser.getUserName());
        user.setEmail(updatedUser.getEmail());

        userRepository.save(user);

        return "Profile Updated Successfully";
    }


    // =================== CHANGE PASSWORD ======================
    public String changePassword(String authHeader, Map<String, String> reqBody) {

        Long userId = extractUserId(authHeader);

        UserModel user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String oldPass = reqBody.get("oldPassword");
        String newPass = reqBody.get("newPassword");

        if (!passwordEncoder.matches(oldPass, user.getPassword())) {
            return "Old password incorrect";
        }

        user.setPassword(passwordEncoder.encode(newPass));
        userRepository.save(user);

        return "Password changed successfully";
    }


    // =================== UPLOAD IMAGE ======================
    public String uploadProfileImage(String authHeader, MultipartFile file) throws Exception {
        Long userId = extractUserId(authHeader);

        UserModel user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String folder = "uploads/profile/";
        File dir = new File(folder);
        if (!dir.exists()) dir.mkdirs();

        String fileName = userId + "_" + file.getOriginalFilename();
        Path path = Paths.get(folder + fileName);

        Files.write(path, file.getBytes());

        user.setProfileImage(fileName);
        userRepository.save(user);

        return "Image uploaded successfully";
    }


    // =================== HELPER METHOD ======================
    private Long extractUserId(String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        return jwtUtil.extractUserId(token);
    }
}

