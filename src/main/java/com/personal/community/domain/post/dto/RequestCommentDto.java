package com.personal.community.domain.post.dto;

import lombok.Data;

public class RequestCommentDto {

    @Data
    public static class CreateCommentDto {
        private Long userId;
        private String comment;
        private Long parentCommentId;
    }
}
