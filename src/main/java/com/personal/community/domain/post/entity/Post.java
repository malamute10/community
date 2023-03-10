package com.personal.community.domain.post.entity;


import com.personal.community.common.CommunityEnum;
import com.personal.community.domain.user.entity.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@Getter
@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@AllArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Lob
    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String author;

    @Column(name = "type", nullable = false)
    private CommunityEnum.PostType type;

    @Column
    @ColumnDefault(value = "0")
    private Long view;

    @CreatedDate
    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(name = "modified_date")
    private LocalDateTime modifiedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @OneToMany(mappedBy = "id", cascade = CascadeType.REMOVE)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "id", cascade = CascadeType.REMOVE)
    private List<View> views = new ArrayList<>();

    @Builder
    public Post(Long id, String title, String content, String author, CommunityEnum.PostType type, User user) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.author = author;
        this.user = user;
        this.type = type;
        this.view = 0L;
    }

    public void addUser(User user){
        this.user = user;
    }

    public void plusView(){
        this.view++;
    }

    public void addComment(Comment comment) {
        this.comments.add(comment);
    }

    public void addView(View view) {
        this.views.add(view);
    }
}
