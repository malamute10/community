package com.personal.community.domain.post.repository;

import com.personal.community.common.CommunityEnum.SearchTarget;
import com.personal.community.domain.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface QPostRepository {
    Page<Post> findAllBySearchAndPaging(SearchTarget searchTarget, String searchText, Pageable pageable);
}
