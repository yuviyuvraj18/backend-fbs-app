package com.fbs.app.controller;

import com.fbs.app.dto.UserRequestDto;
import com.fbs.app.model.Role;
import com.fbs.app.model.UserModel;
import com.fbs.app.repository.UserRepository;
import com.fbs.app.service.TokenService;
import com.fbs.app.utils.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    // ============================
    // REGISTER USER
    // ============================
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRequestDto user) {

        System.out.println(user);
        UserModel userModel = new UserModel();

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("User already exists");
        }

        userModel.setUserName(user.getUserName());
        userModel.setEmail(user.getEmail());

        userModel.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        userRepository.saveAndFlush(userModel);

        return ResponseEntity.ok("User registered successfully");
    }

    // ============================
    // LOGIN USER + GENERATE TOKENS
    // ============================
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserRequestDto user) {

        UserModel existing = userRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!new BCryptPasswordEncoder().matches(user.getPassword(), existing.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }

        // AccessToken (short-lived)
        List<String> roleNames = existing.getRoles()
                .stream()
                .map(Role::name)     // convert ENUM to string like "ROLE_ADMIN"
                .collect(Collectors.toList());;

        String accessToken = jwtUtil.generateAccessToken(existing.getEmail() , roleNames );

      //  System.out.println(accessToken);
        // RefreshToken (long-lived)
        String refreshToken = jwtUtil.generateRefreshToken(existing.getEmail());

        return ResponseEntity.ok(
                Map.of(
                        "accessToken", accessToken,
                        "refreshToken", refreshToken
                )
        );
    }


    // ============================
    // REFRESH TOKEN
    // ============================
    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(HttpServletRequest request) {
        String refreshToken = Arrays.stream(request.getCookies())
                .filter(c -> c.getName().equals("refreshToken"))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);


        if (refreshToken == null) {
            return ResponseEntity.status(400).body("Refresh token missing");
        }

        if (!jwtUtil.isTokenValid(refreshToken)) {
            return ResponseEntity.status(401).body("Invalid refresh token");
        }

        String email = jwtUtil.extractEmail(refreshToken);

        UserModel existing = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<String> roleNames = existing.getRoles()
                .stream()
                .map(Role::name)
                .collect(Collectors.toList());

        String newAccessToken = jwtUtil.generateAccessToken(existing.getEmail(), roleNames);

        return ResponseEntity.ok(Map.of("accessToken", newAccessToken));
    }



    private  TokenService tokenService; // service to revoke tokens

    public AuthController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        // 1) Read refresh token from cookie
        Cookie[] cookies = request.getCookies();
        String refreshToken = null;
        if (cookies != null) {
            for (Cookie c : cookies) {
                if ("refreshToken".equals(c.getName())) {
                    refreshToken = c.getValue();
                }
            }
        }

        // 2) If refresh token present -> revoke it server-side (DB/blacklist)
        if (refreshToken != null && !refreshToken.isEmpty()) {
            tokenService.revokeRefreshToken(refreshToken);
        }

        // 3) Clear refresh token cookie on client by setting it with maxAge 0
        Cookie clearCookie = new Cookie("refreshToken", "");
        clearCookie.setHttpOnly(true);
        clearCookie.setPath("/");           // same path as cookie was set
        clearCookie.setMaxAge(0);           // remove cookie
        clearCookie.setSecure(false);       // set true if using HTTPS in prod
        response.addCookie(clearCookie);

        // 4) Return success
        return ResponseEntity.ok(Map.of("message", "Logged out"));
    }


    @PostMapping("/is-token-valid")
    public ResponseEntity<?> checkTokenValidity(HttpServletRequest request) {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.ok(false);  // token missing
        }

        String accessToken = authHeader.substring(7);

        try {

            boolean isValid = jwtUtil.isTokenValid(accessToken);

            return ResponseEntity.ok(isValid);

        } catch (Exception e) {
            return ResponseEntity.ok(false);
        }
    }


}







//import com.fbs.app.model.UserModel;
//import com.fbs.app.repository.UserRepository;
//import com.fbs.app.utils.JwtUtil;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.Map;
//
//@RestController
//@RequestMapping("/api/auth")
//@CrossOrigin(origins = "http://localhost:3000")
//public class AuthController {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private JwtUtil jwtUtil;
//
//    @PostMapping("/register")
//    public ResponseEntity<?> register(@RequestBody UserModel user) {
//        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
//            return ResponseEntity.badRequest().body("User already exists");
//        }
//        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
//        userRepository.save(user);
//        return ResponseEntity.ok("User registered successfully");
//    }
//
//    @PostMapping("/login")
//    public ResponseEntity<?> login(@RequestBody UserModel user) {
//
//
//
//        UserModel existing = userRepository.findByEmail(user.getEmail())
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//
//        if (!new BCryptPasswordEncoder().matches(user.getPassword(), existing.getPassword())) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
//        }
//        System.out.println("test");
//
//
//        String token = jwtUtil.generateAccessToken(existing.getEmail());
//        return ResponseEntity.ok(Map.of("token", token));
//    }
//}
