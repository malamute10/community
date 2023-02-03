package com.personal.community.integration_test.post;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.personal.community.common.CommunityEnum.PostType;
import com.personal.community.domain.post.entity.Post;
import com.personal.community.domain.post.service.PostService;
import com.personal.community.domain.user.entity.User;
import com.personal.community.integration_test.IntegrationTest;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

public class FindAllPostsTest extends IntegrationTest {

    @Autowired
    PostService postService;

    Logger log = LoggerFactory.getLogger(Logger.class);

    @AfterEach
    void clear() {
        postRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("게시글 목록 조회 테스트")
    void searchPost() throws Exception {
        //given
        String email = "malamute10@naver.com";
        String password = "password";
        User user = createUserForTest(email, password);
        User savedUser = userRepository.save(user);
        postRepository.saveAll(List.of(
                Post.builder().title("제목1").author(savedUser.getNickname()).content("작성글1")
                        .user(savedUser).type(PostType.FREE_BOARD).build(),
                Post.builder().title("다른제목").author(savedUser.getNickname()).content("작성글1")
                        .user(savedUser).type(PostType.FREE_BOARD).build(),
                Post.builder().title("제외").author(savedUser.getNickname()).content("작성글1")
                        .user(savedUser).type(PostType.FREE_BOARD).build(),
                Post.builder().title("제목").author(savedUser.getNickname()).content("작성글1")
                        .user(savedUser).type(PostType.FREE_BOARD).build()));

        //when
        ResultActions resultAction = mvc.perform(get(postBaseUrl)
                                                         .param("page", "1")
                                                         .param("size", "2")
                                                         .param("searchTarget", "TITLE")
                                                         .param("searchText", "제목")
                                                         .contentType(MediaType.APPLICATION_JSON));

        //then
        resultAction.andExpect(status().isOk())
                .andDo(print())
                .andDo(restDocs.document(
                        queryParameters(
                                parameterWithName("searchTarget").description("link:../enum/SearchTarget.html[검색 대상, role=\"popup\"]"),
                                parameterWithName("searchText").description("검색어"),
                                parameterWithName("page").description("페이지 번호"),
                                parameterWithName("size").description("페이지당 갯수")
                        ),
                        responseFields(
                                fieldWithPath("postDtoList[].id").description("게시물 식별 번호"),
                                fieldWithPath("postDtoList[].title").description("제목"),
                                fieldWithPath("postDtoList[].content").description("내용"),
                                fieldWithPath("postDtoList[].author").description("작성자"),
                                fieldWithPath("postDtoList[].type").description("link:../enum/PostType.html[게시물 타입, role=\"popup\"]"),
                                fieldWithPath("postDtoList[].view").description("조회수"),
                                fieldWithPath("postDtoList[].createdDate").description("작성일"),
                                fieldWithPath("paging.page").description("페이지 번호"),
                                fieldWithPath("paging.size").description("페이지당 갯수"),
                                fieldWithPath("paging.totalElement").description("검색된 총 글 수")
                        )));
    }
}
