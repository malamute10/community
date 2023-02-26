package com.personal.community.common;

import com.personal.community.domain.post.dto.RequestPostDto;
import com.personal.community.domain.post.dto.ResponseCommentDto;
import com.personal.community.domain.post.dto.ResponsePostDto;
import com.personal.community.domain.post.entity.Comment;
import com.personal.community.domain.post.entity.Post;
import com.personal.community.domain.user.dto.RequestUserDto;
import com.personal.community.domain.user.dto.ResponseUserDto;
import com.personal.community.domain.user.entity.User;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-02-26T16:36:33+0900",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.5 (JetBrains s.r.o.)"
)
@Component
public class MapStructImpl implements MapStruct {

    @Override
    public Post createPostDtoToPost(RequestPostDto.CreatePostDto createPostDto) {
        if ( createPostDto == null ) {
            return null;
        }

        Post.PostBuilder post = Post.builder();

        post.title( createPostDto.getTitle() );
        post.content( createPostDto.getContent() );
        post.author( createPostDto.getAuthor() );
        post.type( createPostDto.getType() );

        return post.build();
    }

    @Override
    public User signupDtoToUser(RequestUserDto.UserSignupDto userSignupDto) {
        if ( userSignupDto == null ) {
            return null;
        }

        User.UserBuilder user = User.builder();

        user.email( userSignupDto.getEmail() );
        user.password( userSignupDto.getPassword() );
        user.nickname( userSignupDto.getNickname() );

        return user.build();
    }

    @Override
    public ResponsePostDto.PostDto postToPostDto(Post post) {
        if ( post == null ) {
            return null;
        }

        ResponsePostDto.PostDto postDto = new ResponsePostDto.PostDto();

        postDto.setId( post.getId() );
        postDto.setTitle( post.getTitle() );
        postDto.setContent( post.getContent() );
        postDto.setAuthor( post.getAuthor() );
        postDto.setType( post.getType() );
        postDto.setView( post.getView() );
        postDto.setCreatedDate( post.getCreatedDate() );

        return postDto;
    }

    @Override
    public ResponseUserDto.SigninUserDto userToSigninDto(User user) {
        if ( user == null ) {
            return null;
        }

        ResponseUserDto.SigninUserDto signinUserDto = new ResponseUserDto.SigninUserDto();

        signinUserDto.setId( user.getId() );
        signinUserDto.setEmail( user.getEmail() );
        signinUserDto.setNickname( user.getNickname() );
        signinUserDto.setUserRole( user.getUserRole() );

        return signinUserDto;
    }

    @Override
    public ResponseUserDto.UserInfoDto userToUserInfoDto(User user) {
        if ( user == null ) {
            return null;
        }

        ResponseUserDto.UserInfoDto userInfoDto = new ResponseUserDto.UserInfoDto();

        userInfoDto.setId( user.getId() );
        userInfoDto.setEmail( user.getEmail() );
        userInfoDto.setNickname( user.getNickname() );
        userInfoDto.setCreatedDate( user.getCreatedDate() );
        userInfoDto.setLastLoginDate( user.getLastLoginDate() );

        return userInfoDto;
    }

    @Override
    public List<ResponseCommentDto.CommentDto> commentToCommentDto(List<Comment> commentList) {
        if ( commentList == null ) {
            return null;
        }

        List<ResponseCommentDto.CommentDto> list = new ArrayList<ResponseCommentDto.CommentDto>( commentList.size() );
        for ( Comment comment : commentList ) {
            list.add( commentToCommentDto1( comment ) );
        }

        return list;
    }

    @Override
    public List<ResponseUserDto.ScrapDto> postToScrapDto(List<Post> scrapList) {
        if ( scrapList == null ) {
            return null;
        }

        List<ResponseUserDto.ScrapDto> list = new ArrayList<ResponseUserDto.ScrapDto>( scrapList.size() );
        for ( Post post : scrapList ) {
            list.add( postToScrapDto( post ) );
        }

        return list;
    }

    @Override
    public ResponseUserDto.ScrapDto postToScrapDto(Post post) {
        if ( post == null ) {
            return null;
        }

        ResponseUserDto.ScrapDto scrapDto = new ResponseUserDto.ScrapDto();

        scrapDto.setPostId( post.getId() );
        scrapDto.setAuthor( post.getAuthor() );
        scrapDto.setTitle( post.getTitle() );

        return scrapDto;
    }

    @Override
    public List<ResponsePostDto.PostDto> postToPostDto(List<Post> postList) {
        if ( postList == null ) {
            return null;
        }

        List<ResponsePostDto.PostDto> list = new ArrayList<ResponsePostDto.PostDto>( postList.size() );
        for ( Post post : postList ) {
            list.add( postToPostDto( post ) );
        }

        return list;
    }

    @Override
    public List<ResponseUserDto.MyPostDto> postToMyPostDto(List<Post> postList) {
        if ( postList == null ) {
            return null;
        }

        List<ResponseUserDto.MyPostDto> list = new ArrayList<ResponseUserDto.MyPostDto>( postList.size() );
        for ( Post post : postList ) {
            list.add( postToMyPostDto( post ) );
        }

        return list;
    }

    @Override
    public ResponseUserDto.MyPostDto postToMyPostDto(Post post) {
        if ( post == null ) {
            return null;
        }

        ResponseUserDto.MyPostDto myPostDto = new ResponseUserDto.MyPostDto();

        myPostDto.setPostId( post.getId() );
        myPostDto.setTitle( post.getTitle() );
        myPostDto.setView( post.getView() );
        myPostDto.setCreatedDate( post.getCreatedDate() );

        myPostDto.setCommentCount( (long) post.getComments().size() );

        return myPostDto;
    }

    protected ResponseCommentDto.CommentDto commentToCommentDto1(Comment comment) {
        if ( comment == null ) {
            return null;
        }

        ResponseCommentDto.CommentDto commentDto = new ResponseCommentDto.CommentDto();

        commentDto.setId( comment.getId() );
        commentDto.setAuthor( comment.getAuthor() );
        commentDto.setContent( comment.getContent() );
        commentDto.setCreatedDate( comment.getCreatedDate() );

        return commentDto;
    }
}
