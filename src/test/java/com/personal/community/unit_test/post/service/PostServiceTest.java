package com.personal.community.unit_test.post.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

import com.personal.community.common.CommunityEnum;
import com.personal.community.common.MapStruct;
import com.personal.community.domain.post.dto.RequestPostDto;
import com.personal.community.domain.post.entity.Post;
import com.personal.community.domain.post.entity.View;
import com.personal.community.domain.post.repository.PostRepository;
import com.personal.community.domain.post.repository.ViewRepository;
import com.personal.community.domain.post.service.PostService;
import com.personal.community.domain.post.service.ViewService;
import com.personal.community.domain.user.entity.User;
import com.personal.community.unit_test.post.PostTest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class PostServiceTest extends PostTest {

    @Mock
    PostRepository postRepository;
    @InjectMocks
    PostService postService;
    @Mock
    ViewService viewService;
    @Spy
    MapStruct mapper = Mappers.getMapper(MapStruct.class);

    @Test
    @DisplayName("저장 테스트")
    void save() {
        //given
        User user = createUserForTest();
        Post post = createPostForTest(1L, user);
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
        Post post = createPostForTest(1L, user);
        View view = View.ofCreate(post, "127.0.0.1");
        given(postRepository.findById(any())).willReturn(Optional.ofNullable(post));
        given(viewService.findViewByPost(any(), eq("127.0.0.1"))).willReturn(view);

        //when
        Post result = postService.viewPost(post.getId(), "127.0.0.1");

        //then
        assertThat(result.getId()).isEqualTo(post.getId());
        assertThat(result.getView()).isEqualTo(1);
        assertThat(result.getViews().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("목록 조회 테스트")
    void findAll() {
        //given
        User user = createUserForTest();
        Post post1 = createPostForTest(1l, user);
        Post post2 = createPostForTest(2l, user);
        List<Post> postList = new ArrayList<>();
        postList.add(post1);
        postList.add(post2);

        given(postRepository.findAll()).willReturn(postList);

        //when
        List<Post> resultList = postService.findAll();

        //then
        assertThat(resultList.size()).isEqualTo(2);
    }


}
