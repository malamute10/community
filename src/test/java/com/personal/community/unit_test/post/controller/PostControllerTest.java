package com.personal.community.unit_test.post.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.personal.community.common.CommunityEnum;
import com.personal.community.common.MapStructImpl;
import com.personal.community.config.security.SecurityConfig;
import com.personal.community.domain.post.contorller.PostController;
import com.personal.community.domain.post.dto.RequestCommentDto.CreateCommentDto;
import com.personal.community.domain.post.dto.RequestPostDto;
import com.personal.community.domain.post.entity.Post;
import com.personal.community.domain.post.service.CommentService;
import com.personal.community.domain.post.service.PostService;
import com.personal.community.domain.user.entity.User;
import com.personal.community.domain.user.service.UserService;
import com.personal.community.unit_test.post.PostTest;
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
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.filter.OncePerRequestFilter;


@ActiveProfiles("test")
@Import(MapStructImpl.class)
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = PostController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = OncePerRequestFilter.class)})
public class PostControllerTest extends PostTest {

    final String baseUrl = "/api/v1/posts";
    @Autowired
    MockMvc mvc;
    @MockBean
    PostService postService;
    @MockBean
    UserService userService;
    @MockBean
    CommentService commentService;
    @Autowired
    ObjectMapper objectMapper;
    Logger log = LoggerFactory.getLogger(Logger.class);


    @Test
    @DisplayName("게시물 생성 controller 테스트")
    void test1() throws Exception {
        //given
        RequestPostDto.CreatePostDto req = new RequestPostDto.CreatePostDto();
        req.setTitle("title");
        req.setContent("content");
        req.setAuthor("author");
        req.setType(CommunityEnum.PostType.FREE_BOARD);

        User user = createUserForTest();
        MultiValueMap<String, String> queryParam = new LinkedMultiValueMap<>();
        queryParam.set("userId", String.valueOf(user.getId()));

        given(userService.findUserById(any())).willReturn(user);

        //when
        ResultActions result = mvc.perform(post(baseUrl + "/create?userId=1")
                                                   .contentType("application/json;charset=UTF-8")
                                                    .content(objectMapper.writeValueAsString(req)));

        //then
        result.andExpect(status().isOk());
    }

    @Test
    @DisplayName("게시물 조회 controller 테스트")
    void test2() throws Exception {
        //given
        User user = createUserForTest();
        Post post = createPostForTest(1L, user);

        given(postService.findById(any())).willReturn(post);

        //when
        ResultActions result = mvc.perform(get(baseUrl + "/{postId}", post.getId())
                                                   .contentType("application/json;charset=UTF-8"));

        //then
        result.andExpect(status().isOk()).andDo(print());
    }

    @Test
    @DisplayName("게시물 목록 조회 controller 테스트")
    void test3() throws Exception {
        //given
        User user = createUserForTest();

        List<Post> postList = new ArrayList<>();
        for(long i=1L; i<=5L; i++) {
            postList.add(createPostForTest(i, user));
        }
        PageRequest pageable = PageRequest.of(0, 5);
        Page<Post> postPage = new PageImpl<>(postList, pageable, postList.size());

        given(postService.findAllPagination(pageable)).willReturn(postPage);

        //when
        ResultActions result = mvc.perform(get(baseUrl)
                                                   .contentType("application/json;charset=UTF-8")
                                                   .param("page", "1")
                                                   .param("size", "5"));

        //then
        result.andExpect(status().isOk()).andDo(print());
    }

    @Test
    @DisplayName("댓글 작성 controller 테스트")
    void createComment() throws Exception {
        //given
        User user = createUserForTest();
        Post post = createPostForTest(1L, user);

        CreateCommentDto createCommentDto = new CreateCommentDto();
        createCommentDto.setUserId(1L);
        createCommentDto.setComment("comment");

        given(userService.findUserById(1L)).willReturn(user);
        given(postService.findById(1L)).willReturn(post);

        //when
        ResultActions result = mvc.perform(post(baseUrl + "/{postId}/comments", 1L)
                                                   .contentType(MediaType.APPLICATION_JSON)
                                                   .content(objectMapper.writeValueAsString(createCommentDto)));

        //then
        result.andExpect(status().isOk()).andDo(print());
    }
}
