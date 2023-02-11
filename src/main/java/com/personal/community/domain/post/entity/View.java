package com.personal.community.domain.post.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;

@Getter
@Entity
public class View {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_id", referencedColumnName = "id")
    private Post post;

    private String ip;

    private LocalDate viewedDate;

    public static View ofCreate(Post post, String ip) {
        View view = new View();
        view.post = post;
        view.ip = ip;
        return view;
    }

    public void updateViewDate() {
        this.viewedDate = LocalDate.now();
    }
}
