package com.personal.community.post.repository;

import com.personal.community.common.CommunityEnum;
import com.personal.community.domain.post.entity.Post;
import com.personal.community.domain.post.repository.PostRepository;
import com.personal.community.domain.user.entity.User;
import com.personal.community.domain.user.repository.UserRepository;
import com.personal.community.post.PostTest;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@DataJpaTest
public class PostRepositoryTest extends PostTest {

    @Autowired
    PostRepository postRepository;

    @Autowired
    UserRepository userRepository;

    @AfterEach
    void clear() {
        postRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("데이터 저장 테스트")
    void save(){
        //given
        User user = this.createUserForTest();
        User savedUser = userRepository.save(user);
        Post post = this.createPostForTest(null, savedUser);

        //when
        Post savedPost = postRepository.save(post);

        //then
        Optional<Post> optionalPost = postRepository.findById(savedPost.getId());
        Assertions.assertThat(optionalPost.isPresent()).isTrue();
    }

    @Test
    @DisplayName("데이터 삭제 테스트")
    void delete(){
        //given
        User user = this.createUserForTest();
        Post post = this.createPostForTest(null, user);
        Post savedPost = postRepository.save(post);

        //when
        postRepository.delete(savedPost);

        //then
        Optional<Post> optionalPost = postRepository.findById(savedPost.getId());
        Assertions.assertThat(optionalPost.isEmpty()).isTrue();
    }
}
