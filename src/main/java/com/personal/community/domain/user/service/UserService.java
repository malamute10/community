package com.personal.community.domain.user.service;

import com.personal.community.common.MapStruct;
import com.personal.community.config.exception.CommunityException;
import com.personal.community.config.exception.ExceptionEnum;
import com.personal.community.domain.user.dto.RequestUserDto;
import com.personal.community.domain.user.dto.ResponseUserDto;
import com.personal.community.domain.user.dto.ResponseUserDto.UserInfoDto;
import com.personal.community.domain.user.entity.User;
import com.personal.community.domain.user.repository.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final MapStruct mapper;

    @Transactional(readOnly = true)
    public User findUserById(Long userId){
        return userRepository.findById(userId)
                .orElseThrow(RuntimeException::new);
    }

    @Transactional
    public void signup(RequestUserDto.UserSignupDto userSignupDto) {
        User user = mapper.convertDtoToEntity(userSignupDto);
        Optional<User> optionalUser = this.findUserByEmail(user.getEmail());

        if(optionalUser.isPresent()) {
            throw CommunityException.of(ExceptionEnum.ALREADY_EXIST, "이미 존재하는 이메일입니다.");
        }

        user.signup(passwordEncoder.encode(user.getPassword()));

        userRepository.save(user);
    }

    @Transactional
    public ResponseUserDto.SigninUserDto signin(String email, String password) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> CommunityException.of(ExceptionEnum.NOT_FOUND, "아이디 또는 비밀번호를 확인해주세요."));

        if(!passwordEncoder.matches(user.getPassword(), password)) {
            throw CommunityException.of(ExceptionEnum.NOT_FOUND, "아이디 또는 비밀번호를 확인해주세요.");
        }
        user.signin();

        return mapper.convertEntityToDto(user);
    }

    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public UserInfoDto getUserInfo(Long id) {
        User user = this.findUserById(id);
        return mapper.convertUserToUserInfoDto(user);
    }
}
