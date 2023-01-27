package com.personal.community.domain.post.contorller;

import com.personal.community.domain.post.service.CommentService;
import com.personal.community.domain.user.entity.User;
import com.personal.community.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final UserService userService;


    @DeleteMapping("/{commentId}/users/{userId}")
    public ResponseEntity<Object> deleteCommentById(@PathVariable Long commentId,
                                                    @PathVariable Long userId) {
        User user = userService.findUserById(userId);
        commentService.deleteById(commentId, user);
        return ResponseEntity.ok().build();
    }

}
