package com.personal.community.domain.post.service;

import com.personal.community.domain.post.entity.Post;
import com.personal.community.domain.post.entity.View;
import com.personal.community.domain.post.repository.ViewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ViewService {
    private final ViewRepository viewRepository;

    public View findViewByPost(Post post, String ip) {
        return viewRepository.findByPostAndIp(post, ip).orElseGet(
                () -> View.ofCreate(post, ip));
    }
}
