package com.personal.community.domain.user.controller;

import com.personal.community.domain.user.dto.RequestUserDto;
import com.personal.community.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<Object> signupUser(@RequestBody RequestUserDto.UserSignupDto userSignupDto) {
        userSignupDto.checkPassword();
        userService.signup(userSignupDto);

        return ResponseEntity.ok().build();
    }
}
