package com.personal.community.post.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import com.personal.community.common.MapStruct;
import com.personal.community.domain.post.dto.RequestCommentDto;
import com.personal.community.domain.post.dto.ResponseCommentDto;
import com.personal.community.domain.post.entity.Comment;
import com.personal.community.domain.post.entity.Post;
import com.personal.community.domain.post.repository.CommentRepository;
import com.personal.community.domain.post.service.CommentService;
import com.personal.community.domain.user.entity.User;
import com.personal.community.post.PostTest;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class CommentServiceTest extends PostTest {

    @Mock
    CommentRepository commentRepository;
    @InjectMocks
    CommentService commentService;
    @Spy
    MapStruct mapper = Mappers.getMapper(MapStruct.class);

    @Test
    @DisplayName("작성 댓글 조회 서비스 테스트")
    void getCommentList() {
        //given
        User user = createUserForTest();
        Post post = createPostForTest(1L, user);
        List<Comment> commentList = List.of(
                createCommentForTest(1L, user.getNickname(), user, post, "content1", null),
                createCommentForTest(2L, user.getNickname(), user, post, "content2", null));

        given(commentRepository.findAllByUser(user)).willReturn(commentList);

        //when
        ResponseCommentDto.CommentListDto commentListDto = commentService.findAllByUser(user);

        //then
        assertThat(commentListDto.getCommentList().size()).isEqualTo(2);
    }

    @Test
    @DisplayName("댓글 작성 서비스 테스트")
    void createComment() {
        //given
        User user = createUserForTest();
        Post post = createPostForTest(1L, user);

        RequestCommentDto.CreateCommentDto createCommentDto = new RequestCommentDto.CreateCommentDto();
        createCommentDto.setComment("comment");

        //when
        commentService.createComment(createCommentDto, post, user, null);

        //then
    }

    @Test
    @DisplayName("대댓글 작성 서비스 테스트")
    void createComment2() {
        //given
        User user = createUserForTest();
        Post post = createPostForTest(1L, user);
        Comment parentComment = createCommentForTest(1L, user.getNickname(), user, post, "comment", null);

        RequestCommentDto.CreateCommentDto createCommentDto = new RequestCommentDto.CreateCommentDto();
        createCommentDto.setComment("comment");

        //when
        commentService.createComment(createCommentDto, post, user, parentComment);

        //then
    }

    @Test
    @DisplayName("댓글 삭제 서비스 테스트")
    void deleteComment() {
        //given
        User user = createUserForTest();
        Post post = createPostForTest(1L, user);
        Comment comment = createCommentForTest(1L, user.getNickname(), user, post, "comment", null);
        given(commentRepository.findById(1L)).willReturn(Optional.of(comment));
        //when
        commentService.deleteById(1L, user);

        //then
    }

}
