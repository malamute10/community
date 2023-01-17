package com.personal.community.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.personal.community.domain.user.entity.User;
import com.personal.community.domain.user.repository.UserRepository;
import com.personal.community.domain.user.service.UserService;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserService userService;

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

    private User createUserForTest(){
        return User.builder()
                .id(1L)
                .email("malamut10@naver.com")
                .password("password")
                .nickname("nickname")
                .build();
    }
}
