package com.personal.community.integration_test.user;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.personal.community.common.CommunityEnum;
import com.personal.community.config.RestDocsConfig;
import com.personal.community.domain.user.dto.RequestUserDto.UserSigninDto;
import com.personal.community.domain.user.dto.ResponseUserDto.TokenDto;
import com.personal.community.integration_test.IntegrationTest;
import org.aspectj.lang.annotation.After;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

@Import(RestDocsConfig.class)
public class SigninUserTest extends IntegrationTest {

    @AfterEach
    void clear() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("로그인 테스트")
    void signin() throws Exception {
        //given
        saveUser(createUserForTest("malamute10@naver.com", "!@#$qwer1234"));
        UserSigninDto userSigninDto = new UserSigninDto();
        userSigninDto.setEmail("malamute10@naver.com");
        userSigninDto.setPassword("!@#$qwer1234");

        //when
        ResultActions result = mvc.perform(post(userBaseUrl + "/signin")
                                                   .contentType(MediaType.APPLICATION_JSON)
                                                   .content(objectMapper.writeValueAsString(userSigninDto)));
        //then
        result.andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestFields(
                                fieldWithPath("email").description("이메일"),
                                fieldWithPath("password").description("비밀번호")
                        ),
                        responseFields(
                                fieldWithPath("id").description("유저 식별 번호"),
                                fieldWithPath("email").description("이메일"),
                                fieldWithPath("nickname").description("닉네임"),
                                fieldWithPath("userRole").description("link:../enum/UserRole.html[유저 권한정보, role=\"popup\"]"),
                                fieldWithPath("token.accessToken").description("엑세스 토큰")
                        )));
    }

}
