package com.personal.community.domain.user.entity;


import com.personal.community.common.CommunityEnum;
import com.personal.community.domain.post.entity.Comment;
import com.personal.community.domain.post.entity.Post;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;


@Getter
@Entity
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nickname;
    @Column
    private CommunityEnum.UserRole userRole;

    @CreatedDate
    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "last_login_date")
    private LocalDateTime lastLoginDate;

    @OneToMany(mappedBy = "id")
    private List<Post> postList;

    @OneToMany(mappedBy = "id")
    private List<Scrap> scrapList = new ArrayList<>();

    @OneToMany(mappedBy = "id", cascade = CascadeType.ALL)
    private List<Comment> comments;

    @Builder
    public User(Long id, String email, String password, String nickname) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.userRole = CommunityEnum.UserRole.USER;
    }

    public static User ofCreate(String email, String password, String nickname) {
        User user = new User();
        user.email = email;
        user.password = password;
        user.nickname = nickname;
        user.userRole = CommunityEnum.UserRole.USER;
        return user;
    }

    public void signup(String encodedPassword) {
        this.password = encodedPassword;
        this.userRole = CommunityEnum.UserRole.USER;
    }

    public void signin() {
        this.lastLoginDate = LocalDateTime.now();
    }

    public void addScrap(Scrap scrap) {
        this.scrapList.add(scrap);
    }
}
