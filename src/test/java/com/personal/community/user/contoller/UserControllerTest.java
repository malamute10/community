package com.personal.community.user.contoller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.personal.community.common.CommunityEnum;
import com.personal.community.common.MapStruct;
import com.personal.community.common.MapStructImpl;
import com.personal.community.config.jwt.TokenService;
import com.personal.community.config.security.SecurityConfig;
import com.personal.community.domain.post.dto.ResponseCommentDto;
import com.personal.community.domain.post.dto.ResponseCommentDto.CommentDto;
import com.personal.community.domain.post.dto.ResponseCommentDto.CommentListDto;
import com.personal.community.domain.post.entity.Comment;
import com.personal.community.domain.post.entity.Post;
import com.personal.community.domain.post.service.CommentService;
import com.personal.community.domain.post.service.PostService;
import com.personal.community.domain.user.controller.UserController;
import com.personal.community.domain.user.dto.RequestUserDto;
import com.personal.community.domain.user.dto.ResponseUserDto;
import com.personal.community.domain.user.dto.ResponseUserDto.UserInfoDto;
import com.personal.community.domain.user.entity.User;
import com.personal.community.domain.user.service.UserService;
import com.personal.community.user.UserTest;
import java.time.LocalDateTime;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.filter.OncePerRequestFilter;

@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = UserController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = OncePerRequestFilter.class)})
public class UserControllerTest extends UserTest {

    @MockBean
    UserService userService;
    @MockBean
    PostService postService;
    @MockBean
    TokenService tokenService;
    @Autowired
    MockMvc mvc;
    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    CommentService commentService;
    Logger log = LoggerFactory.getLogger(LoggerFactory.class);


    private final String baseUrl = "/api/v1/users";

    @Test
    @DisplayName("회원가입 컨트롤러 테스트")
    @WithMockUser(roles = "USER")
    void signup() throws Exception {
        //given
        RequestUserDto.UserSignupDto userSignupDto = new RequestUserDto.UserSignupDto();
        userSignupDto.setEmail("email@gmail.com");
        userSignupDto.setNickname("nickname");
        userSignupDto.setPassword("password12");
        userSignupDto.setConfirmPassword("password12");

        //when
        ResultActions result = mvc.perform(post(baseUrl + "/signup")
                                                   .contentType(MediaType.APPLICATION_JSON)
                                                    .content(objectMapper.writeValueAsString(userSignupDto)));

        //then
        result.andExpect(status().isOk()).andDo(print());
    }

    @Test
    @DisplayName("로그인 컨트롤러 테스트")
    @WithMockUser(roles = "USER")
    void signin() throws Exception {
        //given
        RequestUserDto.UserSigninDto userSigninDto = new RequestUserDto.UserSigninDto();
        userSigninDto.setEmail("malamute10@naver.com");
        userSigninDto.setPassword("password");

        ResponseUserDto.SigninUserDto signinUserDto = new ResponseUserDto.SigninUserDto();
        signinUserDto.setId(1L);
        signinUserDto.setEmail("malamute10@naver.com");
        signinUserDto.setNickname("malamute10");
        signinUserDto.setUserRole(CommunityEnum.UserRole.USER);

        given(userService.signin(any(), any())).willReturn(signinUserDto);
        given(tokenService.generateAccessToken(any(), any())).willReturn("accessToken");

        //when
        ResultActions result = mvc.perform(post(baseUrl + "/signin").contentType(MediaType.APPLICATION_JSON)
                                                    .content(objectMapper.writeValueAsBytes(userSigninDto)));

        //then
        result.andExpect(status().isOk()).andDo(print());
    }

    @Test
    @DisplayName("내 정보 조회 컨트롤러 테스트")
    void getInfo() throws Exception{
        //given
        UserInfoDto userInfoDto = new UserInfoDto();
        userInfoDto.setId(1L);
        userInfoDto.setEmail("malamute10@naver.com");
        userInfoDto.setNickname("nickname");
        userInfoDto.setCreatedDate(LocalDateTime.now());
        userInfoDto.setLastLoginDate(LocalDateTime.now());

        given(userService.getUserInfo(1L)).willReturn(userInfoDto);

        //when
        ResultActions result =
                mvc.perform(get(baseUrl + "/{userId}", 1L)
                                                   .contentType(MediaType.APPLICATION_JSON));
        //then
        result.andExpect(status().isOk()).andDo(print());
    }

    @Test
    @DisplayName("작성 댓글목록 조회 컨트롤러 테스트")
    void getMyComments() throws Exception {
        //given
        User user = createUserForTest();
        given(userService.findUserById(user.getId())).willReturn(user);
        given(commentService.findAllByUser(user)).willReturn(createCommentList(user));

        //when
        ResultActions result = mvc.perform(get(baseUrl + "/{userId}/comments", 1L)
                                                   .contentType(MediaType.APPLICATION_JSON));
        //then
        result.andExpect(status().isOk()).andDo(print());
    }

    private CommentListDto createCommentList(User user){
        CommentDto commentDto1 = new CommentDto();
        commentDto1.setAuthor(user.getNickname());
        commentDto1.setContent("comment1");
        commentDto1.setCreatedDate(LocalDateTime.now());
        commentDto1.setId(1L);

        CommentDto commentDto2 = new CommentDto();
        commentDto2.setAuthor(user.getNickname());
        commentDto2.setContent("comment2");
        commentDto2.setCreatedDate(LocalDateTime.now());
        commentDto2.setId(2L);

        return CommentListDto.ofCreate(List.of(commentDto1, commentDto2));
    }

    @Test
    @DisplayName("스크랩 추가 컨트롤러 테스트")
    void addScrap() throws Exception {
        //given
        User user = createUserForTest();
        Post post = createPostForTest(1L, user);

        given(postService.findById(post.getId())).willReturn(post);

        //when
        ResultActions result = mvc.perform(post(baseUrl + "/{userId}/scraps/{postId}", 1L, 1L)
                                                   .contentType(MediaType.APPLICATION_JSON));
        //then
        result.andExpect(status().isOk()).andDo(print());
    }
}
