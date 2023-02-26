package com.personal.community.integration_test.comment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.personal.community.common.CommunityEnum.PostType;
import com.personal.community.domain.post.dto.RequestCommentDto.CreateCommentDto;
import com.personal.community.domain.post.entity.Comment;
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

public class DeleteComment extends IntegrationTest {

    @Autowired
    PostService postService;

    @AfterEach
    void clear() {
        deleteAllRepository();
    }

    @Test
    @DisplayName("댓글 삭제 테스트")
    void deleteComments() throws Exception {
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
        Post savedPost = postRepository.save(post);

        Comment comment = Comment.builder()
                .author(savedUser.getNickname())
                .user(savedUser)
                .content("댓글")
                .post(savedPost)
                .build();
        log.info("child:{}", comment.getChildComments());
        Comment savedComment = commentRepository.save(comment);
        log.info("savedCommentChild:{}", savedComment.getChildComments());

        //when
        ResultActions resultAction = mvc.perform(delete(commentBaseUrl + "/{commentId}", savedComment.getId())
                                                         .header(HttpHeaders.AUTHORIZATION, "Bearer " + access)
                                                         .contentType(MediaType.APPLICATION_JSON));
        //then
        resultAction.andExpect(status().isOk())
                .andDo(print())
                .andDo(
                        restDocs.document(
                                pathParameters(
                                        parameterWithName("commentId").description("댓글 번호")
                                )));
        log.info("comment:{}", commentRepository.findAll());
        assertThat(commentRepository.findById(savedComment.getId()).isEmpty()).isTrue();
    }
}
