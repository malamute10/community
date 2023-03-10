package com.personal.community.unit_test.user.contoller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.personal.community.common.CommunityEnum.UserRole;
import com.personal.community.common.MapStruct;
import com.personal.community.common.MapStructImpl;
import com.personal.community.config.jwt.TokenService;
import com.personal.community.config.security.SecurityConfig;
import com.personal.community.domain.post.entity.Comment;
import com.personal.community.domain.post.entity.Post;
import com.personal.community.domain.post.service.CommentService;
import com.personal.community.domain.post.service.PostService;
import com.personal.community.domain.user.controller.UserController;
import com.personal.community.domain.user.dto.RequestUserDto.UserSigninDto;
import com.personal.community.domain.user.dto.RequestUserDto.UserSignupDto;
import com.personal.community.domain.user.dto.ResponseUserDto.SigninUserDto;
import com.personal.community.domain.user.dto.ResponseUserDto.UserInfoDto;
import com.personal.community.domain.user.entity.User;
import com.personal.community.domain.user.service.UserService;
import com.personal.community.unit_test.user.UserTest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.filter.OncePerRequestFilter;

@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
@Import(MapStructImpl.class)
@WebMvcTest(controllers = UserController.class,
        excludeFilters = {
                @Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class),
                @Filter(type = FilterType.ASSIGNABLE_TYPE, classes = OncePerRequestFilter.class)})
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

    @Autowired
    MapStruct mapper;
    @MockBean
    CommentService commentService;

    Logger log = LoggerFactory.getLogger(LoggerFactory.class);


    private final String baseUrl = "/api/v1/users";

    @Test
    @DisplayName("???????????? ???????????? ?????????")
    @WithMockUser(roles = "USER")
    void signup() throws Exception {
        //given
        UserSignupDto userSignupDto = new UserSignupDto();
        userSignupDto.setEmail("email@gmail.com");
        userSignupDto.setNickname("nickname");
        userSignupDto.setPassword("!password12");
        userSignupDto.setConfirmPassword("!password12");

        //when
        ResultActions result = mvc.perform(post(baseUrl + "/signup")
                                                   .contentType(MediaType.APPLICATION_JSON)
                                                    .content(objectMapper.writeValueAsString(userSignupDto)));

        //then
        result.andExpect(status().isOk()).andDo(print());
    }

    @Test
    @DisplayName("????????? ???????????? ?????????")
    @WithMockUser(roles = "USER")
    void signin() throws Exception {
        //given
        UserSigninDto userSigninDto = new UserSigninDto();
        userSigninDto.setEmail("malamute10@naver.com");
        userSigninDto.setPassword("password");

        SigninUserDto signinUserDto = new SigninUserDto();
        signinUserDto.setId(1L);
        signinUserDto.setEmail("malamute10@naver.com");
        signinUserDto.setNickname("malamute10");
        signinUserDto.setUserRole(UserRole.USER);

        given(userService.signin(any(), any())).willReturn(signinUserDto);
        given(tokenService.generateAccessToken(any(), any())).willReturn("accessToken");

        //when
        ResultActions result = mvc.perform(post(baseUrl + "/signin").contentType(MediaType.APPLICATION_JSON)
                                                    .content(objectMapper.writeValueAsBytes(userSigninDto)));

        //then
        result.andExpect(status().isOk()).andDo(print());
    }

    @Test
    @DisplayName("??? ?????? ?????? ???????????? ?????????")
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
    @DisplayName("?????? ???????????? ?????? ???????????? ?????????")
    void getMyComments() throws Exception {
        //given
        User user = createUserForTest();
        Post post = createPostForTest(1L, user);

        List<Comment> commentList = new ArrayList<>();
        for(long i=1L; i<=5L; i++) {
            commentList.add(createCommentForTest(i, user.getNickname(), user, post, "content" + i, null));
        }
        PageRequest pageable = PageRequest.of(0, 5);
        Page<Comment> commentPage = new PageImpl<>(commentList, pageable, commentList.size());


        given(userService.findUserById(user.getId())).willReturn(user);
        given(commentService.findAllByUser(user, pageable)).willReturn(commentPage);

        //when
        ResultActions result = mvc.perform(get(baseUrl + "/{userId}/comments", 1L)
                                                   .contentType(MediaType.APPLICATION_JSON)
                                                   .param("page", "1")
                                                   .param("size", "5"));
        //then
        result.andExpect(status().isOk()).andDo(print());
    }

    @Test
    @DisplayName("????????? ?????? ???????????? ?????????")
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

    @Test
    @DisplayName("????????? ?????? ???????????? ?????????")
    void deleteScrap() throws Exception {
        //given
        User user = createUserForTest();
        Post post = createPostForTest(1L, user);

        given(postService.findById(post.getId())).willReturn(post);

        //when
        ResultActions result = mvc.perform(delete(baseUrl + "/{userId}/scraps/{postId}", 1L, 1L)
                                                   .contentType(MediaType.APPLICATION_JSON));
        //then
        result.andExpect(status().isOk()).andDo(print());
    }

    @Test
    @DisplayName("????????? ?????? ???????????? ?????????")
    void findScrap() throws Exception {
        //given
        User user = createUserForTest();
        Pageable pageable = PageRequest.of(0, 5);

        for(long i=1L; i<=10; i++) {
            user.addScrap(createPostForTest(i, user));
        }
        long startIndex = pageable.getOffset();
        List<Post> scrapList = user.getScrapList();
        List<Post> pagingScraps = scrapList.subList((int) startIndex, (int) Math.min(scrapList.size(),
                                                                              startIndex + pageable.getPageSize()));

        given(userService.findScrapsByUserId(user.getId(), pageable)).willReturn(pagingScraps);

        //when
        ResultActions result = mvc.perform(get(baseUrl + "/{userId}/scraps", 1L)
                                                   .param("page", "1")
                                                   .param("size", "5")
                                                   .contentType(MediaType.APPLICATION_JSON));
        //then
        result.andExpect(status().isOk()).andDo(print());
    }

    @Test
    @DisplayName("????????? ?????? ???????????? ?????????")
    void findMyPosts() throws Exception {
        //given
        User user = createUserForTest();
        Pageable pageable = PageRequest.of(0, 5);

        List<Post> postList = new ArrayList<>();
        for(long i = 1L; i<=5L; i++) {
            Post post = createPostForTest(i, user);
            if(i%2 == 0) {
                post.addComment(createCommentForTest(i, user.getNickname(), user, post, "comment" + i, null));
            }
            postList.add(post);
        }
        Page<Post> postPage = new PageImpl<>(postList, pageable, postList.size());

        given(userService.findUserById(user.getId())).willReturn(user);
        given(postService.findAllByUser(user, pageable)).willReturn(postPage);

        //when
        ResultActions result = mvc.perform(get(baseUrl + "/{userId}/posts", 1L)
                                                   .contentType(MediaType.APPLICATION_JSON)
                                                   .param("page", "1")
                                                   .param("size", "5"));

        //then
        result.andExpect(status().isOk()).andDo(print());
    }
}
