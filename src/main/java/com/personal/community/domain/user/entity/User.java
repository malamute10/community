package com.personal.community.domain.user.entity;


import com.personal.community.domain.post.entity.Post;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;


@Getter
@Entity
public class User {

    @Id
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nickname;

    @CreatedDate
    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "last_login_date")
    private LocalDateTime lastLoginDate;

    @OneToMany(mappedBy = "id")
    private List<Post> postList;

    @OneToMany(mappedBy = "id")
    private List<Scrap> scrapList;
}
