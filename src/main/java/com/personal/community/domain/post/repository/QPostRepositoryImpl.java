package com.personal.community.domain.post.repository;

import com.personal.community.common.CommunityEnum.SearchTarget;
import com.personal.community.domain.post.entity.Post;
import com.personal.community.domain.post.entity.QPost;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class QPostRepositoryImpl implements QPostRepository{

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Post> findAllBySearchAndPaging(SearchTarget searchTarget, String searchText, Pageable pageable) {
        QPost qPost = new QPost("post");

        JPAQuery<Post> query = queryFactory.selectFrom(qPost);

        if(searchTarget == SearchTarget.TITLE || searchTarget == SearchTarget.TITLE_CONTENT) {
            query = query.where(qPost.title.like("%" + searchText + "%"));
        }
        if(searchTarget == SearchTarget.CONTENT || searchTarget == SearchTarget.TITLE_CONTENT) {
            query = query.where(qPost.content.like("%" + searchText + "%"));
        }
        if(searchTarget == SearchTarget.AUTHOR) {
            query = query.where(qPost.author.like("%" + searchText + "%"));
        }

        Long totalCount = this.getTotalCount(query);

        List<Post> postList = query.orderBy(qPost.createdDate.desc()).offset(pageable.getOffset())
                .limit(pageable.getPageSize()).fetch();


        return new PageImpl<>(postList, pageable, totalCount);
    }

    private Long getTotalCount(JPAQuery query) {
        return (long) query.fetch().size();
    }
}
