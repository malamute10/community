package com.personal.community.domain.post.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

public class ResponseCommentDto {

    @Data
    public static class CommentListDto {
        List<CommentDto> commentList;

        public static CommentListDto ofCreate(List<CommentDto> commentList) {
            CommentListDto commentListDto = new CommentListDto();
            commentListDto.setCommentList(commentList);
            return commentListDto;
        }
    }

    @Data
    public static class CommentDto {
        private Long id;
        private String author;
        private String content;
        private LocalDateTime createdDate;
    }
}
