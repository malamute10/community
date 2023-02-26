package com.personal.community.domain.post.entity;

import com.personal.community.common.CommunityEnum;
import com.personal.community.common.CommunityEnum.CommentStatus;
import com.personal.community.config.exception.CommunityException;
import com.personal.community.config.exception.ExceptionEnum;
import com.personal.community.domain.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Slf4j
@Getter
@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "author")
    private String author;
    @Column(name = "content")
    private String content;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private CommunityEnum.CommentStatus status;

    @CreatedDate
    @Column(name = "created_date")
    private LocalDateTime createdDate;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "post_id", referencedColumnName = "id")
    private Post post;

    @OneToMany(mappedBy = "parentComment")
    private List<Comment> childComments = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "parent_comment_id", referencedColumnName = "id")
    private Comment parentComment;

    @Builder
    public Comment(Long id, String author, User user, Post post, String content, Comment parentComment) {
        this.id = id;
        this.author = author;
        this.user = user;
        this.post = post;
        this.content = content;
        this.parentComment = parentComment;
        this.status = CommentStatus.EXISTS;
    }

    public boolean delete(User user) {
        if(!this.user.getId().equals(user.getId())){
            throw CommunityException.of(ExceptionEnum.UNAUTHORIZED, "해당 댓글을 삭제할 권한이 없습니다.");
        }
        this.user = null;
        this.status = CommentStatus.DELETED;
        return this.childComments.iterator().hasNext();
    }
}
