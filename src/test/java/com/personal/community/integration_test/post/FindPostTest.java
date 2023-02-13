package com.personal.community.integration_test.post;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.beneathPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.personal.community.common.CommunityEnum;
import com.personal.community.common.CommunityEnum.PostType;
import com.personal.community.domain.post.dto.RequestPostDto.CreatePostDto;
import com.personal.community.domain.post.entity.Post;
import com.personal.community.domain.post.service.PostService;
import com.personal.community.domain.user.entity.User;
import com.personal.community.integration_test.IntegrationTest;
import java.time.LocalDateTime;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

public class FindPostTest extends IntegrationTest {

    @Autowired
    PostService postService;

    @AfterEach
    void clear() {
        postRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("게시글 조회 테스트")
    void findPost() throws Exception {
        //given
        String email = "malamute10@naver.com";
        String password = "password";
        User user = createUserForTest(email, password);
        User savedUser = userRepository.save(user);

        Post post = Post.builder()
                .title("post Title")
                .author(savedUser.getNickname())
                .content("post Content")
                .user(savedUser)
                .type(PostType.FREE_BOARD)
                .build();
        postRepository.save(post);

        //when
        ResultActions resultAction = mvc.perform(get(postBaseUrl + "/{postId}", 1L)
                                                         .contentType(MediaType.APPLICATION_JSON));
        //then
        resultAction.andExpect(status().isOk())
                .andDo(print())
                .andDo(
                        restDocs.document(
                                pathParameters(
                                        parameterWithName("postId").description("게시물 번호")
                                ),
                                responseFields(
                                    fieldWithPath("id").description("게시물 번호"),
                                    fieldWithPath("title").description("제목"),
                                    fieldWithPath("author").description("작성자"),
                                    fieldWithPath("content").description("내용"),
                                    fieldWithPath("view").description("조회수"),
                                    fieldWithPath("createdDate").description("작성일자"),
                                    fieldWithPath("type").description("link:../enum/PostType.html[게시물 타입, role=\"popup\"]")
                        )));
    }
}
