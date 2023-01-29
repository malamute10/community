package com.personal.community.domain.user.controller;

import com.personal.community.common.MapStruct;
import com.personal.community.config.jwt.TokenService;
import com.personal.community.domain.post.dto.ResponseCommentDto;
import com.personal.community.domain.post.dto.ResponseCommentDto.CommentListDto;
import com.personal.community.domain.post.entity.Post;
import com.personal.community.domain.post.service.CommentService;
import com.personal.community.domain.post.service.PostService;
import com.personal.community.domain.user.dto.RequestUserDto;
import com.personal.community.domain.user.dto.ResponseUserDto;
import com.personal.community.domain.user.dto.ResponseUserDto.ScrapDto;
import com.personal.community.domain.user.dto.ResponseUserDto.ScrapListDto;
import com.personal.community.domain.user.dto.ResponseUserDto.UserInfoDto;
import com.personal.community.domain.user.entity.User;
import com.personal.community.domain.user.service.UserService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final TokenService tokenService;
    private final CommentService commentService;
    private final PostService postService;
    private final MapStruct mapper;

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

    @PostMapping("/{userId}/scraps/{postId}")
    public ResponseEntity<Object> addScrap(@PathVariable Long userId, @PathVariable Long postId) {
        Post post = postService.findById(postId);
        userService.addScrap(userId, post);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{userId}/scraps/{postId}")
    public ResponseEntity<Object> deleteScrap(@PathVariable Long userId, @PathVariable Long postId) {
        Post post = postService.findById(postId);
        userService.deleteScrap(userId, post);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{userId}/scraps")
    public ResponseEntity<ResponseUserDto.ScrapListDto> findScrapList(@PathVariable Long userId,
                                                                      @RequestParam Integer page,
                                                                      @RequestParam Integer size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        List<Post> pagingScraps = userService.findScrapsByUserId(userId, pageable);
        List<ScrapDto> scrapList = mapper.postToScrapDto(pagingScraps);

        return ResponseEntity.ok(ScrapListDto.ofCreate(scrapList));
    }
}
