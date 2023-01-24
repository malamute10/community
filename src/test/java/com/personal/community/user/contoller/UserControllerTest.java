package com.personal.community.user.contoller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.personal.community.common.CommunityEnum;
import com.personal.community.config.jwt.TokenService;
import com.personal.community.config.security.SecurityConfig;
import com.personal.community.domain.user.controller.UserController;
import com.personal.community.domain.user.dto.RequestUserDto;
import com.personal.community.domain.user.dto.ResponseUserDto;
import com.personal.community.domain.user.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.filter.OncePerRequestFilter;

@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = UserController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = OncePerRequestFilter.class)})
public class UserControllerTest {

    @MockBean
    UserService userService;
    @MockBean
    TokenService tokenService;
    @Autowired
    MockMvc mvc;
    @Autowired
    ObjectMapper objectMapper;


    private final String baseUrl = "/api/v1/users";

    @Test
    @DisplayName("회원가입 컨트롤러 테스트")
    @WithMockUser(roles = "USER")
    void signup() throws Exception {
        //given
        RequestUserDto.UserSignupDto userSignupDto = new RequestUserDto.UserSignupDto();
        userSignupDto.setEmail("email@gmail.com");
        userSignupDto.setNickname("nickname");
        userSignupDto.setPassword("password12");
        userSignupDto.setConfirmPassword("password12");

        //when
        ResultActions result = mvc.perform(post(baseUrl + "/signup")
                                                   .contentType(MediaType.APPLICATION_JSON)
                                                    .content(objectMapper.writeValueAsString(userSignupDto)));

        //then
        result.andExpect(status().isOk()).andDo(print());
    }

    @Test
    @DisplayName("로그인 컨트롤러 테스트")
    @WithMockUser(roles = "USER")
    void signin() throws Exception {
        //given
        RequestUserDto.UserSigninDto userSigninDto = new RequestUserDto.UserSigninDto();
        userSigninDto.setEmail("malamute10@naver.com");
        userSigninDto.setPassword("password");

        ResponseUserDto.SigninUserDto signinUserDto = new ResponseUserDto.SigninUserDto();
        signinUserDto.setId(1L);
        signinUserDto.setEmail("malamute10@naver.com");
        signinUserDto.setNickname("malamute10");
        signinUserDto.setUserRole(CommunityEnum.UserRole.USER);

        given(userService.signin(any(), any())).willReturn(signinUserDto);
        given(tokenService.generateAccessToken(any(), any())).willReturn("accessToken");

        //when
        ResultActions result = mvc.perform(post(baseUrl + "/signin").contentType(MediaType.APPLICATION_JSON)
                                                    .content(objectMapper.writeValueAsBytes(userSigninDto)));

        //then
        result.andExpect(status().isOk()).andDo(print());
    }
}
