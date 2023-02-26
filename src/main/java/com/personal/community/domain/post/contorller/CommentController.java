package com.personal.community.domain.post.contorller;

import com.personal.community.config.security.UserDetailsImpl;
import com.personal.community.domain.post.dto.RequestCommentDto.CreateCommentDto;
import com.personal.community.domain.post.entity.Comment;
import com.personal.community.domain.post.entity.Post;
import com.personal.community.domain.post.service.CommentService;
import com.personal.community.domain.post.service.PostService;
import com.personal.community.domain.user.entity.User;
import com.personal.community.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final UserService userService;
    private final PostService postService;

    @PostMapping("/posts/{postId}")
    public ResponseEntity<Object> createComment(@PathVariable Long postId,
                                                @RequestBody CreateCommentDto createCommentDto) {
        User user = userService.findUserById(createCommentDto.getUserId());
        Post post = postService.findById(postId);
        Comment comment = null;
        if(createCommentDto.getParentCommentId() != null) {
            comment = commentService.findById(createCommentDto.getParentCommentId());
        }

        commentService.createComment(createCommentDto, post, user, comment);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Object> deleteCommentById(@PathVariable Long commentId,
                                                    @AuthenticationPrincipal UserDetailsImpl userDetails) {
        commentService.deleteById(commentId, userDetails.getUser());
        return ResponseEntity.ok().build();
    }

}
