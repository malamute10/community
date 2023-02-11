package com.personal.community.domain.post.service;

import com.personal.community.common.CommunityEnum.SearchTarget;
import com.personal.community.common.MapStruct;
import com.personal.community.domain.post.dto.RequestPostDto;
import com.personal.community.domain.post.entity.Post;
import com.personal.community.domain.post.entity.View;
import com.personal.community.domain.post.repository.PostRepository;
import com.personal.community.domain.user.entity.User;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final ViewService viewService;
    private final MapStruct mapper;


    @Transactional
    public void createPost(RequestPostDto.CreatePostDto createPostDto, User user){
        Post post = mapper.createPostDtoToPost(createPostDto);
        post.addUser(user);

        postRepository.save(post);
    }

    @Transactional
    public Post findById(Long postId){
        return postRepository.findById(postId).orElseThrow(RuntimeException::new);
    }

    @Transactional(readOnly = true)
    public List<Post> findAll() {
        return postRepository.findAll();
    }

    @Transactional
    public void deleteById(Long postId) {
        postRepository.deleteById(postId);
    }

    @Transactional(readOnly = true)
    public Page<Post> findAllPagination(SearchTarget searchTarget, String searchText, Pageable pageable) {
        return postRepository.findAllBySearchAndPaging(searchTarget, searchText, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Post> findAllByUser(User user, Pageable pageable) {
        return postRepository.findAllByUserOrderByCreatedDateDesc(user, pageable);
    }

    @Transactional
    public Post viewPost(Long postId, String ip) {
        Post post = this.findById(postId);

        View view = viewService.findViewByPost(post, ip);

        if(view.getViewedDate() == null || !view.getViewedDate().plusDays(1).isAfter(LocalDate.now())){
            post.plusView();
            view.updateViewDate();
            post.addView(view);
        }
        return post;
    }
}
