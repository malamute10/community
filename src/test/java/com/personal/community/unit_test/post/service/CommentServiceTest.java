package com.personal.community.unit_test.post.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import com.personal.community.common.MapStruct;
import com.personal.community.domain.post.dto.RequestCommentDto;
import com.personal.community.domain.post.entity.Comment;
import com.personal.community.domain.post.entity.Post;
import com.personal.community.domain.post.repository.CommentRepository;
import com.personal.community.domain.post.service.CommentService;
import com.personal.community.domain.user.entity.User;
import com.personal.community.unit_test.post.PostTest;
import java.util.ArrayList;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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
        List<Comment> commentList = new ArrayList<>();
        for(long i=1L; i<=5L; i++) {
            commentList.add(createCommentForTest(i, user.getNickname(), user, post, "content" + i, null));
        }
        PageRequest pageable = PageRequest.of(0, 5);
        Page<Comment> commentPage = new PageImpl<>(commentList, pageable, commentList.size());

        given(commentRepository.findAllByUser(user, pageable)).willReturn(commentPage);

        //when
        Page<Comment> commentPageResult = commentService.findAllByUser(user, pageable);

        //then
        assertThat(commentPageResult.getTotalElements()).isEqualTo(5);
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
