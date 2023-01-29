package com.personal.community.domain.post.dto;

import com.personal.community.common.Paging;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

public class ResponseCommentDto {

    @Data
    public static class CommentListDto {
        private List<CommentDto> commentList;
        private Paging paging;

        public static CommentListDto ofCreate(List<CommentDto> commentList, Paging paging) {
            CommentListDto commentListDto = new CommentListDto();
            commentListDto.setCommentList(commentList);
            commentListDto.setPaging(paging);
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
