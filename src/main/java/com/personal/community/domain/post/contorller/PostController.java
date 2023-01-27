package com.personal.community.domain.post.contorller;


import com.personal.community.common.MapStruct;
import com.personal.community.domain.post.dto.RequestCommentDto.CreateCommentDto;
import com.personal.community.domain.post.dto.RequestPostDto;
import com.personal.community.domain.post.dto.ResponsePostDto;
import com.personal.community.domain.post.entity.Comment;
import com.personal.community.domain.post.entity.Post;
import com.personal.community.domain.post.service.CommentService;
import com.personal.community.domain.post.service.PostService;
import com.personal.community.domain.user.entity.User;
import com.personal.community.domain.user.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final UserService userService;
    private final CommentService commentService;
    private final MapStruct mapper;

    @PostMapping("/create")
    public ResponseEntity<Object> createPostDto(@RequestParam Long userId, @RequestBody RequestPostDto.CreatePostDto req){
        User user = userService.findUserById(userId);
        postService.createPost(req, user);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{postId}")
    public ResponseEntity<ResponsePostDto.PostDto> findPostById(@PathVariable Long postId){
        Post post = postService.findById(postId);
        ResponsePostDto.PostDto postDto = mapper.convertEntityToDto(post);
        return ResponseEntity.ok(postDto);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Object> deletePostById(@PathVariable Long postId){
        postService.deleteById(postId);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<ResponsePostDto.PostDtoList> findAll(){
        List<Post> postList = postService.findAll();
        List<ResponsePostDto.PostDto> postDtoList = mapper.convertEntityToDto(postList);
        return ResponseEntity.ok(ResponsePostDto.PostDtoList.ofCreate(postDtoList));
    }

    @PostMapping("/{postId}/comments")
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
}
