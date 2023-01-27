package com.personal.community.domain.user.controller;

import com.personal.community.config.jwt.TokenService;
import com.personal.community.domain.post.dto.ResponseCommentDto;
import com.personal.community.domain.post.dto.ResponseCommentDto.CommentListDto;
import com.personal.community.domain.post.service.CommentService;
import com.personal.community.domain.user.dto.RequestUserDto;
import com.personal.community.domain.user.dto.ResponseUserDto;
import com.personal.community.domain.user.dto.ResponseUserDto.UserInfoDto;
import com.personal.community.domain.user.entity.User;
import com.personal.community.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final TokenService tokenService;
    private final CommentService commentService;

    @PostMapping("/signup")
    public ResponseEntity<Object> signupUser(@Valid @RequestBody RequestUserDto.UserSignupDto userSignupDto) {
        userSignupDto.checkPassword();
        userService.signup(userSignupDto);

        return ResponseEntity.ok().build();
    }

    @PostMapping("signin")
    public ResponseEntity<ResponseUserDto.SigninUserDto> signinUser(@RequestBody RequestUserDto.UserSigninDto userSigninDto) {
        ResponseUserDto.SigninUserDto signinUserDto = userService.signin(userSigninDto.getEmail(), userSigninDto.getPassword());

        String accessToken = tokenService.generateAccessToken(signinUserDto.getEmail(), signinUserDto.getUserRole());
        ResponseUserDto.TokenDto tokenDto = ResponseUserDto.TokenDto.ofCreate(accessToken);

        signinUserDto.setToken(tokenDto);

        return ResponseEntity.ok(signinUserDto);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ResponseUserDto.UserInfoDto> getUserInfo(@PathVariable Long userId) {
        UserInfoDto userInfo = userService.getUserInfo(userId);
        return ResponseEntity.ok(userInfo);
    }

    @GetMapping("/{userId}/comments")
    public ResponseEntity<ResponseCommentDto.CommentListDto> getCommentsByUser(@PathVariable Long userId) {
        User user = userService.findUserById(userId);
        CommentListDto commentList = commentService.findAllByUser(user);

        return ResponseEntity.ok(commentList);
    }
}
