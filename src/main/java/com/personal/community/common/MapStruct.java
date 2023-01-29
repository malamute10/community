package com.personal.community.common;

import com.personal.community.domain.post.dto.RequestPostDto;
import com.personal.community.domain.post.dto.ResponseCommentDto.CommentDto;
import com.personal.community.domain.post.dto.ResponsePostDto;
import com.personal.community.domain.post.dto.ResponsePostDto.PostDto;
import com.personal.community.domain.post.entity.Comment;
import com.personal.community.domain.post.entity.Post;
import com.personal.community.domain.user.dto.RequestUserDto;
import com.personal.community.domain.user.dto.ResponseUserDto;
import com.personal.community.domain.user.dto.ResponseUserDto.MyPostDto;
import com.personal.community.domain.user.dto.ResponseUserDto.ScrapDto;
import com.personal.community.domain.user.entity.User;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.NullValueMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL
)
public interface MapStruct {

    Post createPostDtoToPost(RequestPostDto.CreatePostDto createPostDto);
    User signupDtoToUser(RequestUserDto.UserSignupDto userSignupDto);

    ResponsePostDto.PostDto postToPostDto(Post post);

    ResponseUserDto.SigninUserDto userToSigninDto(User user);

    ResponseUserDto.UserInfoDto userToUserInfoDto(User user);
    List<CommentDto> commentToCommentDto(List<Comment> commentList);

    List<ScrapDto> postToScrapDto(List<Post> scrapList);
    @Mapping(source = "id", target = "postId")
    ScrapDto postToScrapDto(Post post);

    List<PostDto> postToPostDto(List<Post> postList);

    List<MyPostDto> postToMyPostDto(List<Post> postList);

    @Mapping(source = "id", target = "postId")
    @Mapping(target = "commentCount", expression = "java((long) post.getComments().size())")
    MyPostDto postToMyPostDto(Post post);
}
