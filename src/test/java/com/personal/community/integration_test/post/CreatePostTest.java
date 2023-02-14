package com.personal.community.integration_test.post;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.personal.community.common.CommunityEnum.PostType;
import com.personal.community.domain.post.dto.RequestPostDto.CreatePostDto;
import com.personal.community.domain.post.service.PostService;
import com.personal.community.domain.user.entity.User;
import com.personal.community.integration_test.IntegrationTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

public class CreatePostTest extends IntegrationTest {

    @Autowired
    PostService postService;

    Logger log = LoggerFactory.getLogger(Logger.class);

    @AfterEach
    void clear() {
        deleteAllRepository();
    }

    @Test
    @DisplayName("게시글 작성 테스트")
    void createPost() throws Exception {
        //given
        String email = "malamute10@naver.com";
        String password = "password";
        User user = createUserForTest(email, password);
        User savedUser = userRepository.save(user);
        String accessToken = getToken(email, password);

        CreatePostDto createPostDto = new CreatePostDto();
        createPostDto.setTitle("title");
        createPostDto.setAuthor(user.getNickname());
        createPostDto.setContent("content");
        createPostDto.setType(PostType.FREE_BOARD);

        //when
        ResultActions resultAction = mvc.perform(post(postBaseUrl + "/create")
                                                         .param("userId", String.valueOf(savedUser.getId()))
                                                         .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                                                         .contentType(MediaType.APPLICATION_JSON)
                                                         .content(objectMapper.writeValueAsString(createPostDto)));
        //then
        resultAction.andExpect(status().isOk())
                .andDo(print())
                .andDo(
                        restDocs.document(
                                requestFields(
                                    fieldWithPath("title").description("String"),
                                    fieldWithPath("author").description("String"),
                                    fieldWithPath("content").description("String"),
                                    fieldWithPath("type").description("link:../enum/PostType.html[게시물 타입, role=\"popup\"]")
                        )));
    }
}
