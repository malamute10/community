package com.personal.community.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.personal.community.common.CommunityEnum;
import com.personal.community.common.MapStruct;
import com.personal.community.config.security.SecurityConfig;
import com.personal.community.domain.user.dto.RequestUserDto;
import com.personal.community.domain.user.dto.ResponseUserDto;
import com.personal.community.domain.user.dto.ResponseUserDto.UserInfoDto;
import com.personal.community.domain.user.entity.User;
import com.personal.community.domain.user.repository.UserRepository;
import com.personal.community.domain.user.service.UserService;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

@Slf4j
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    UserRepository userRepository;
    @InjectMocks
    UserService userService;
    @Spy
    MapStruct mapper = Mappers.getMapper(MapStruct.class);
    @Mock
    BCryptPasswordEncoder passwordEncoder;

    @Test
    @DisplayName("유저 조회 테스트")
    void userTest() {
        //given
        User user = createUserForTest();
        given(userRepository.findById(any())).willReturn(Optional.ofNullable(user));

        //when
        User savedUser = userService.findUserById(user.getId());

        //then
        assertThat(savedUser.getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("회원가입 서비스 테스트")
    void signup() {
        //given
        User user = User.ofCreate("email", "password", "nickname");
        RequestUserDto.UserSignupDto userSignupDto = new RequestUserDto.UserSignupDto();
        userSignupDto.setEmail("email");
        userSignupDto.setNickname("nickname");
        userSignupDto.setPassword("password");
        userSignupDto.setConfirmPassword("password");

        given(userRepository.save(any())).willReturn(user);
        given(passwordEncoder.encode(any())).willReturn("password");

        //when
        userService.signup(userSignupDto);

        //then
    }

    @Test
    @DisplayName("로그인 서비스 테스트")
    void signin() {
        //given
        User user = this.createUserForTest();
        given(userRepository.findByEmail(any())).willReturn(Optional.of(user));
        given(passwordEncoder.matches(any(), any())).willReturn(true);

        //when
        ResponseUserDto.SigninUserDto SigninUserDto = userService.signin("malamute10@naver.com", "password");

        //then
        assertThat(SigninUserDto.getId()).isEqualTo(1L);
        assertThat(SigninUserDto.getEmail()).isEqualTo("malamute10@naver.com");
        assertThat(SigninUserDto.getNickname()).isEqualTo("nickname");
        assertThat(SigninUserDto.getUserRole()).isEqualTo(CommunityEnum.UserRole.USER);
    }

    @Test
    @DisplayName("내 정보 조회 서비스 테스트")
    void getInfo() {
        //given
        User user = createUserForTest();
        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));

        //when
        UserInfoDto userInfo = userService.getUserInfo(user.getId());

        //then
        assertThat(userInfo.getId()).isEqualTo(1L);
        assertThat(userInfo.getEmail()).isEqualTo("malamute10@naver.com");
        assertThat(userInfo.getNickname()).isEqualTo("nickname");
    }

    private User createUserForTest(){
        return User.builder()
                .id(1L)
                .email("malamute10@naver.com")
                .password("password")
                .nickname("nickname")
                .build();
    }
}
