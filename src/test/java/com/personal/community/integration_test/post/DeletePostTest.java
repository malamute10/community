package com.personal.community.integration_test.post;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.personal.community.common.CommunityEnum.PostType;
import com.personal.community.domain.post.entity.Post;
import com.personal.community.domain.post.service.CommentService;
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

public class DeletePostTest extends IntegrationTest {

    @Autowired
    PostService postService;
    @Autowired
    CommentService commentService;

    @AfterEach
    void clear() {
        deleteAllRepository();
    }

    @Test
    @DisplayName("게시글 삭제 테스트")
    void deletePost() throws Exception {
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
        Post savedPost = postRepository.save(post);

        //when
        ResultActions resultAction = mvc.perform(delete(postBaseUrl + "/{postId}", savedPost.getId())
                                                         .header(HttpHeaders.AUTHORIZATION, getToken(email, password))
                                                         .contentType(MediaType.APPLICATION_JSON));
        //then
        resultAction.andExpect(status().isOk())
                .andDo(print())
                .andDo(
                        restDocs.document(
                                pathParameters(
                                        parameterWithName("postId").description("게시물 번호")
                                )));
    }
}
