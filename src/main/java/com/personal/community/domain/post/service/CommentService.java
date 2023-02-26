package com.personal.community.domain.post.service;

import com.personal.community.common.MapStruct;
import com.personal.community.config.exception.CommunityException;
import com.personal.community.config.exception.ExceptionEnum;
import com.personal.community.domain.post.dto.RequestCommentDto;
import com.personal.community.domain.post.entity.Comment;
import com.personal.community.domain.post.entity.Post;
import com.personal.community.domain.post.repository.CommentRepository;
import com.personal.community.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final MapStruct mapper;

    @Transactional(readOnly = true)
    public Page<Comment> findAllByUser(User user, Pageable pageable) {
        return commentRepository.findAllByUser(user, pageable);
    }

    @Transactional
    public void createComment(RequestCommentDto.CreateCommentDto createCommentDto, Post post, User user, Comment parentComment){
        Comment comment = Comment.builder().author(user.getNickname())
                .content(createCommentDto.getComment()).user(user).post(post)
                .parentComment(parentComment).build();

        commentRepository.save(comment);
    }

    @Transactional(readOnly = true)
    public Comment findById(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(
                () -> CommunityException.of(ExceptionEnum.NOT_FOUND, "해당 댓글을 찾을 수 없습니다."));
    }
    @Transactional
    public void deleteById(Long commentId, User user) {
        Comment comment = this.findById(commentId);
        log.info("comment:{}", comment.getChildComments());
        log.info("comment:{}", comment);
        boolean hasChildComment = comment.delete(user);
        log.info("hasChildComment:{}", hasChildComment);
        if(!hasChildComment) {
            commentRepository.delete(comment);
        }
    }
}
