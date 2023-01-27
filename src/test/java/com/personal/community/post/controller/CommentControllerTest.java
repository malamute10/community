package com.personal.community.post.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.personal.community.config.security.SecurityConfig;
import com.personal.community.domain.post.contorller.CommentController;
import com.personal.community.domain.post.service.CommentService;
import com.personal.community.domain.user.service.UserService;
import com.personal.community.post.PostTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.filter.OncePerRequestFilter;


@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = CommentController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = OncePerRequestFilter.class)})
public class CommentControllerTest extends PostTest {

    final String baseUrl = "/api/v1/comments";
    @Autowired
    MockMvc mvc;
    @MockBean
    UserService userService;
    @MockBean
    CommentService commentService;
    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("댓글 삭제 컨트롤러 테스트")
    void deleteComment() throws Exception {
        //given

        //when
        ResultActions result = mvc.perform(delete(baseUrl + "/{commentId}/users/{userId}", 1L, 1L)
                                                   .contentType("application/json;charset=UTF-8"));

        //then
        result.andExpect(status().isOk()).andDo(print());
    }
}
