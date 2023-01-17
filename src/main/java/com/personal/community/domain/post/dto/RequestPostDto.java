package com.personal.community.domain.post.dto;

import com.personal.community.common.CommunityEnum;
import lombok.Data;

public class RequestPostDto {
    @Data
    public static class CreatePostDto {
        private String title;
        private String author;
        private String content;
        private CommunityEnum.PostType type;
    }
}
