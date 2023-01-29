package com.personal.community.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.personal.community.common.CommunityEnum;
import com.personal.community.common.MapStruct;
import com.personal.community.domain.post.entity.Post;
import com.personal.community.domain.user.dto.RequestUserDto;
import com.personal.community.domain.user.dto.ResponseUserDto;
import com.personal.community.domain.user.dto.ResponseUserDto.UserInfoDto;
import com.personal.community.domain.user.entity.User;
import com.personal.community.domain.user.repository.UserRepository;
import com.personal.community.domain.user.service.UserService;
import com.personal.community.user.UserTest;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@Slf4j
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class UserServiceTest extends UserTest {

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

    @Test
    @DisplayName("스크랩 추가 서비스 테스트")
    void createScrap() {
        //given
        User user = createUserForTest();
        Post post = createPostForTest(1L, user);
        given(userRepository.findById(1L)).willReturn(Optional.of(user));

        //when
        userService.addScrap(user.getId(), post);

        //then
    }

    @Test
    @DisplayName("스크랩 삭제 서비스 테스트")
    void deleteScrap() {
        //given
        User user = createUserForTest();
        Post post = createPostForTest(1L, user);
        given(userRepository.findById(1L)).willReturn(Optional.of(user));

        //when
        userService.deleteScrap(user.getId(), post);

        //then
    }

    @Test
    @DisplayName("스크랩 조회 서비스 테스트")
    void findScraps() {
        //given
        User user = createUserForTest();

        for(long i=1L; i<=10; i++) {
            user.addScrap(createPostForTest(i, user));
        }

        Pageable pageable = PageRequest.of(0, 3);

        given(userRepository.findById(1L)).willReturn(Optional.of(user));

        //when
        List<Post> scrapsByUserId = userService.findScrapsByUserId(user.getId(), pageable);

        //then
        assertThat(scrapsByUserId.size()).isEqualTo(3);
        assertThat(scrapsByUserId.get(1).getId()).isEqualTo(2L);
    }
}
