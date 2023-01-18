package com.personal.community.post.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.personal.community.common.CommunityEnum;
import com.personal.community.common.MapStruct;
import com.personal.community.common.MapStructImpl;
import com.personal.community.domain.post.contorller.PostController;
import com.personal.community.domain.post.dto.RequestPostDto;
import com.personal.community.domain.post.dto.ResponsePostDto;
import com.personal.community.domain.post.entity.Post;
import com.personal.community.domain.post.service.PostService;
import com.personal.community.domain.user.entity.User;
import com.personal.community.domain.user.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;


@ActiveProfiles("test")
@AutoConfigureMockMvc
@WebMvcTest(PostController.class)
public class PostControllerTest {

    final String baseUrl = "/api/v1/posts";
    @Autowired
    MockMvc mvc;

    @MockBean
    PostService postService;

    @MockBean
    UserService userService;
    @Autowired
    ObjectMapper objectMapper;

    @TestConfiguration
    static class TestConfig {
        @Bean
        MapStruct getMapper() {
            return new MapStructImpl();
        }
    }

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
        Post post = createPostForTest(user);
        ResponsePostDto.PostDto postDto = new ResponsePostDto.PostDto();

        given(postService.findById(any())).willReturn(post);



        //when
        ResultActions result = mvc.perform(get(baseUrl + "/{postId}", post.getId())
                                                   .contentType("application/json;charset=UTF-8"));

        //then
        result.andExpect(status().isOk()).andDo(print());
    }

    private Post createPostForTest(User user){
        return Post.builder()
                .id(1L)
                .title("title")
                .author("author")
                .content("content")
                .type(CommunityEnum.PostType.FREE_BOARD)
                .user(user)
                .build();
    }

    private User createUserForTest(){
        return User.builder()
                .id(1L)
                .email("malamut10@naver.com")
                .password("password")
                .nickname("nickname")
                .build();
    }
}
