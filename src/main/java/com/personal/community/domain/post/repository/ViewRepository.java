package com.personal.community.domain.post.repository;

import com.personal.community.domain.post.entity.Post;
import com.personal.community.domain.post.entity.View;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ViewRepository extends JpaRepository<View, Long> {

    Optional<View> findByPostAndIp(Post post, String ip);
}
