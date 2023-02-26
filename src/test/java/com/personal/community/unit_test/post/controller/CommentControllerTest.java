package com.personal.community.unit_test.post.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.personal.community.config.jwt.TokenService;
import com.personal.community.config.security.SecurityConfig;
import com.personal.community.config.security.UserDetailsServiceImpl;
import com.personal.community.domain.post.contorller.CommentController;
import com.personal.community.domain.post.service.CommentService;
import com.personal.community.domain.post.service.PostService;
import com.personal.community.domain.user.entity.User;
import com.personal.community.domain.user.repository.UserRepository;
import com.personal.community.domain.user.service.UserService;
import com.personal.community.unit_test.post.PostTest;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.filter.OncePerRequestFilter;


@ActiveProfiles("test")
@Import({SecurityConfig.class, UserDetailsServiceImpl.class})
@AutoConfigureMockMvc
@ContextConfiguration
@WebMvcTest(controllers = CommentController.class)
public class CommentControllerTest extends PostTest {

    final String baseUrl = "/api/v1/comments";
    @Autowired
    MockMvc mvc;
    @MockBean
    UserService userService;
    @MockBean
    CommentService commentService;
    @MockBean
    PostService postService;
    @MockBean
    UserRepository userRepository;
    @MockBean
    TokenService tokenService;
    @Autowired
    ObjectMapper objectMapper;

    @Test
    @WithMockUser(value = "malamute10@naver.com", roles = "USER")
    @DisplayName("댓글 삭제 컨트롤러 테스트")
    void deleteComment() throws Exception {
        //given
        User user = createUserForTest();
        String token = "token";
        given(tokenService.getUsername(token)).willReturn(user.getEmail());
        given(tokenService.getUserRole(token)).willReturn(user.getUserRole().name());
        given(userRepository.findByEmail(user.getEmail())).willReturn(Optional.of(user));

        //when
        ResultActions result = mvc.perform(delete(baseUrl + "/{commentId}", 1L)
                                                   .header(HttpHeaders.AUTHORIZATION, token)
                                                   .contentType("application/json;charset=UTF-8"));

        //then
        result.andExpect(status().isOk()).andDo(print());
    }
}
