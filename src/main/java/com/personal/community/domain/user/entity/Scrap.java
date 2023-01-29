package com.personal.community.domain.user.entity;


import com.personal.community.domain.post.entity.Post;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;


@Getter
@Entity
public class Scrap {

    @Id
    private Long id;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "postId", referencedColumnName = "id")
    private Post post;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "userId", referencedColumnName = "id")
    private User user;

    public static Scrap ofCreate(User user, Post post) {
        Scrap scrap = new Scrap();
        scrap.user = user;
        scrap.post = post;
        return scrap;
    }
}
