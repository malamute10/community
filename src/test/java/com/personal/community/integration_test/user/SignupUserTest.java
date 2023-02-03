package com.personal.community.integration_test.user;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.personal.community.config.RestDocsConfig;
import com.personal.community.domain.user.dto.RequestUserDto.UserSigninDto;
import com.personal.community.domain.user.dto.RequestUserDto.UserSignupDto;
import com.personal.community.integration_test.IntegrationTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

@Import(RestDocsConfig.class)
public class SignupUserTest extends IntegrationTest {

    @AfterEach
    void clear() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("회원가입 테스트")
    void signup() throws Exception {
        //given
        UserSignupDto userSignupDto = new UserSignupDto();
        userSignupDto.setEmail("malamute10@naver.com");
        userSignupDto.setNickname("nickname");
        userSignupDto.setPassword("!@#$qwer1234");
        userSignupDto.setConfirmPassword("!@#$qwer1234");

        //when
        ResultActions result = mvc.perform(post(userBaseUrl + "/signup")
                                                   .contentType(MediaType.APPLICATION_JSON)
                                                   .content(objectMapper.writeValueAsString(userSignupDto)));
        //then
        result.andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestFields(
                                fieldWithPath("email").description("이메일"),
                                fieldWithPath("nickname").description("닉네임"),
                                fieldWithPath("password").description("비밀번호"),
                                fieldWithPath("confirmPassword").description("확인용 비밀번호")
                        )));
    }

}
