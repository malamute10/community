package com.personal.community.domain.user.service;

import com.personal.community.common.MapStruct;
import com.personal.community.config.exception.CommunityException;
import com.personal.community.config.exception.ExceptionEnum;
import com.personal.community.domain.user.dto.RequestUserDto;
import com.personal.community.domain.user.entity.User;
import com.personal.community.domain.user.repository.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
