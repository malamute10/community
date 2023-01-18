package com.personal.community.post.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.personal.community.common.CommunityEnum;
import com.personal.community.common.MapStruct;
import com.personal.community.domain.post.dto.RequestPostDto;
import com.personal.community.domain.post.entity.Post;
import com.personal.community.domain.post.repository.PostRepository;
import com.personal.community.domain.post.service.PostService;
import com.personal.community.domain.user.entity.User;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class PostServiceTest {

    @Mock
    PostRepository postRepository;

    @InjectMocks
    PostService postService;

    @Spy
    MapStruct mapper = Mappers.getMapper(MapStruct.class);

    @Test
    @DisplayName("저장 테스트")
    void save() {
        //given
        User user = createUserForTest();
        Post post = createPostForTest(user);
        given(postRepository.save(any())).willReturn(post);

        RequestPostDto.CreatePostDto createPostDto = new RequestPostDto.CreatePostDto();
        createPostDto.setTitle("title");
        createPostDto.setAuthor("author");
        createPostDto.setContent("content");
        createPostDto.setType(CommunityEnum.PostType.FREE_BOARD);

        //when
        postService.createPost(createPostDto, user);

        //then
    }

    @Test
    @DisplayName("조회 테스트")
    void find() {
        //given
        User user = createUserForTest();
        Post post = createPostForTest(user);
        given(postRepository.findById(any())).willReturn(Optional.ofNullable(post));

        //when
        Post result = postService.findById(post.getId());

        //then
        assertThat(result.getId()).isEqualTo(post.getId());
        assertThat(result.getView()).isEqualTo(1);
    }

    private Post createPostForTest(User user){
        return Post.builder()
                .id(1L)
                .title("title")
                .author("author")
                .content("content")
                .type(CommunityEnum.PostType.FREE_BOARD)
                .user(user)
                .build();
    }

    private User createUserForTest(){
        return User.builder()
                .email("malamut10@naver.com")
                .password("password")
                .nickname("nickname")
                .build();
    }
}
