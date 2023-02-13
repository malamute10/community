package com.personal.community.integration_test.post;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.personal.community.common.CommunityEnum.PostType;
import com.personal.community.domain.post.dto.RequestCommentDto.CreateCommentDto;
import com.personal.community.domain.post.entity.Post;
import com.personal.community.domain.post.service.PostService;
import com.personal.community.domain.user.entity.User;
import com.personal.community.integration_test.IntegrationTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

public class CreateComment extends IntegrationTest {

    @Autowired
    PostService postService;

    @AfterEach
    void clear() {
        postRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("댓글 작성 테스트")
    void createComment() throws Exception {
        //given
        String email = "malamute10@naver.com";
        String password = "password";
        User user = createUserForTest(email, password);
        User savedUser = userRepository.save(user);
        String access = getToken(email, password);

        Post post = Post.builder()
                .title("post Title")
                .author(savedUser.getNickname())
                .content("post Content")
                .user(savedUser)
                .type(PostType.FREE_BOARD)
                .build();
        postRepository.save(post);

        CreateCommentDto createCommentDto = new CreateCommentDto();
        createCommentDto.setUserId(savedUser.getId());
        createCommentDto.setComment("댓글");
        createCommentDto.setParentCommentId(null);

        //when
        ResultActions resultAction = mvc.perform(post(postBaseUrl + "/{postId}/comments", 1L)
                                                         .header(HttpHeaders.AUTHORIZATION, "Bearer " + access)
                                                         .contentType(MediaType.APPLICATION_JSON)
                                                         .content(objectMapper.writeValueAsString(createCommentDto)));
        //then
        resultAction.andExpect(status().isOk())
                .andDo(print())
                .andDo(
                        restDocs.document(
                                pathParameters(
                                        parameterWithName("postId").description("게시물 번호")
                                ),
                                requestFields(
                                        fieldWithPath("userId").description("유저 번호"),
                                        fieldWithPath("comment").description("댓글 내용"),
                                        fieldWithPath("parentCommentId").description("대댓글")
                                )));
    }
}
