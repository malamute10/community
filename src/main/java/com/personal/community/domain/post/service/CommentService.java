package com.personal.community.domain.post.service;

import com.personal.community.common.MapStruct;
import com.personal.community.config.exception.CommunityException;
import com.personal.community.config.exception.ExceptionEnum;
import com.personal.community.domain.post.dto.RequestCommentDto;
import com.personal.community.domain.post.dto.ResponseCommentDto.CommentDto;
import com.personal.community.domain.post.dto.ResponseCommentDto.CommentListDto;
import com.personal.community.domain.post.entity.Comment;
import com.personal.community.domain.post.entity.Post;
import com.personal.community.domain.post.repository.CommentRepository;
import com.personal.community.domain.user.entity.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final MapStruct mapper;

    public CommentListDto findAllByUser(User user) {
        List<Comment> commentList = commentRepository.findAllByUser(user);
        List<CommentDto> commentDtos = mapper.convertEntityListToDto(commentList);
        return CommentListDto.ofCreate(commentDtos);
    }

    public void createComment(RequestCommentDto.CreateCommentDto createCommentDto, Post post, User user, Comment parentComment){
        Comment comment = Comment.builder().author(user.getNickname())
                .content(createCommentDto.getComment()).user(user).post(post)
                .parentComment(parentComment).build();

        commentRepository.save(comment);
    }

    public Comment findById(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(
                () -> CommunityException.of(ExceptionEnum.NOT_FOUND, "해당 댓글을 찾을 수 없습니다."));
    }
}
