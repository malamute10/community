package com.personal.community.domain.user.service;

import com.personal.community.domain.user.entity.User;
import com.personal.community.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User findUserById(Long userId){
        return userRepository.findById(userId)
                .orElseThrow(RuntimeException::new);
    }
}
