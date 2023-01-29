package com.personal.community.domain.post.dto;

import com.personal.community.common.CommunityEnum;
import com.personal.community.common.Paging;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;
import org.springframework.data.domain.Pageable;

public class ResponsePostDto {

    @Data
    public static class PostDtoList {
        private List<PostDto> postDtoList;
        private Paging paging;

        public static PostDtoList ofCreate(List<PostDto> postDtoList, Paging paging){
            PostDtoList list = new PostDtoList();
            list.postDtoList = postDtoList;
            list.paging = paging;
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
