package com.personal.community.post;

import com.personal.community.common.CommunityEnum;
import com.personal.community.domain.post.entity.Post;
import com.personal.community.domain.user.entity.User;

public class PostTest {
    protected Post createPostForTest(Long postId, User user){
        return Post.builder()
                .id(postId)
                .title("title")
                .author("author")
                .content("content")
                .type(CommunityEnum.PostType.FREE_BOARD)
                .user(user)
                .build();
    }

    protected User createUserForTest(){
        return User.builder()
                .email("malamut10@naver.com")
                .password("password")
                .nickname("nickname")
                .build();
    }
}
