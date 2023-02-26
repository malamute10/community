package com.personal.community.domain.user.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = 1733380279L;

    public static final QUser user = new QUser("user");

    public final ListPath<com.personal.community.domain.post.entity.Comment, com.personal.community.domain.post.entity.QComment> comments = this.<com.personal.community.domain.post.entity.Comment, com.personal.community.domain.post.entity.QComment>createList("comments", com.personal.community.domain.post.entity.Comment.class, com.personal.community.domain.post.entity.QComment.class, PathInits.DIRECT2);

    public final DateTimePath<java.time.LocalDateTime> createdDate = createDateTime("createdDate", java.time.LocalDateTime.class);

    public final StringPath email = createString("email");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final DateTimePath<java.time.LocalDateTime> lastLoginDate = createDateTime("lastLoginDate", java.time.LocalDateTime.class);

    public final StringPath nickname = createString("nickname");

    public final StringPath password = createString("password");

    public final ListPath<com.personal.community.domain.post.entity.Post, com.personal.community.domain.post.entity.QPost> postList = this.<com.personal.community.domain.post.entity.Post, com.personal.community.domain.post.entity.QPost>createList("postList", com.personal.community.domain.post.entity.Post.class, com.personal.community.domain.post.entity.QPost.class, PathInits.DIRECT2);

    public final ListPath<com.personal.community.domain.post.entity.Post, com.personal.community.domain.post.entity.QPost> scrapList = this.<com.personal.community.domain.post.entity.Post, com.personal.community.domain.post.entity.QPost>createList("scrapList", com.personal.community.domain.post.entity.Post.class, com.personal.community.domain.post.entity.QPost.class, PathInits.DIRECT2);

    public final EnumPath<com.personal.community.common.CommunityEnum.UserRole> userRole = createEnum("userRole", com.personal.community.common.CommunityEnum.UserRole.class);

    public QUser(String variable) {
        super(User.class, forVariable(variable));
    }

    public QUser(Path<? extends User> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUser(PathMetadata metadata) {
        super(User.class, metadata);
    }

}

