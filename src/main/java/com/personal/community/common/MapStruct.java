package com.personal.community.common;

import com.personal.community.domain.post.dto.RequestPostDto;
import com.personal.community.domain.post.dto.ResponseCommentDto.CommentDto;
import com.personal.community.domain.post.dto.ResponseCommentDto.CommentListDto;
import com.personal.community.domain.post.dto.ResponsePostDto;
import com.personal.community.domain.post.entity.Comment;
import com.personal.community.domain.post.entity.Post;
import com.personal.community.domain.user.dto.RequestUserDto;
import com.personal.community.domain.user.dto.ResponseUserDto;
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

    Post convertDtoToEntity(RequestPostDto.CreatePostDto createPostDto);
    User convertDtoToEntity(RequestUserDto.UserSignupDto userSignupDto);

    ResponsePostDto.PostDto convertEntityToDto(Post post);

    List<ResponsePostDto.PostDto> convertEntityToDto(List<Post> post);

    ResponseUserDto.SigninUserDto convertEntityToDto(User user);

    ResponseUserDto.UserInfoDto convertUserToUserInfoDto(User user);
    List<CommentDto> convertEntityListToDto(List<Comment> commentList);
}
