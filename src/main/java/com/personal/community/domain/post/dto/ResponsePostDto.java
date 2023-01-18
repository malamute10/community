package com.personal.community.domain.post.dto;

import com.personal.community.common.CommunityEnum;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

public class ResponsePostDto {

    @Data
    public static class PostDtoList {
        List<PostDto> postDtoList;

        public static PostDtoList ofCreate(List<PostDto> postDtoList){
            PostDtoList list = new PostDtoList();
            list.postDtoList = postDtoList;
            return list;
        }
    }

    @Data
    public static class PostDto {
        private Long id;
        private String title;
        private String content;
        private String author;
        private CommunityEnum.PostType type;
        private Long view;
        private LocalDateTime createdDate;
    }
}
