package com.personal.community.user;

import com.personal.community.common.CommunityEnum;
import com.personal.community.domain.post.entity.Comment;
import com.personal.community.domain.post.entity.Post;
import com.personal.community.domain.user.entity.User;

public class UserTest {
    protected User createUserForTest(){
        return User.builder()
                .id(1L)
                .email("malamute10@naver.com")
                .password("password")
                .nickname("nickname")
                .build();
    }

    protected Comment createCommentForTest(Long id, String author, User user, Post post, String content, Comment parentComment) {
        return Comment.builder()
                .id(id)
                .author(author)
                .user(user)
                .post(post)
                .content(content)
                .parentComment(parentComment)
                .build();
    }

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
}
