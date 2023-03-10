package com.personal.community.domain.post.repository;

import com.personal.community.domain.post.entity.Post;
import com.personal.community.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>, QPostRepository {

    Page<Post> findAllByUserOrderByCreatedDateDesc(User user, Pageable pageable);

    void deleteByIdAndUser(Long postId, User user);
}
