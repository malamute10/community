package com.personal.community.domain.post.service;

import com.personal.community.common.MapStruct;
import com.personal.community.domain.post.dto.RequestPostDto;
import com.personal.community.domain.post.entity.Post;
import com.personal.community.domain.post.repository.PostRepository;
import com.personal.community.domain.user.entity.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final MapStruct mapper;

    @Transactional
    public void createPost(RequestPostDto.CreatePostDto createPostDto, User user){
        Post post = mapper.convertDtoToEntity(createPostDto);
        post.addUser(user);

        postRepository.save(post);
    }

    @Transactional
    public Post findById(Long postId){
        Post post = postRepository.findById(postId).orElseThrow(RuntimeException::new);

        post.plusView();
        return post;
    }

    @Transactional(readOnly = false)
    public List<Post> findAll() {
        return postRepository.findAll();
    }
}
