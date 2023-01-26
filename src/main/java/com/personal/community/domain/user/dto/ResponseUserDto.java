package com.personal.community.domain.user.dto;

import com.personal.community.common.CommunityEnum;
import java.time.LocalDateTime;
import lombok.Data;

public class ResponseUserDto {

    @Data
    public static class SigninUserDto {
        private Long id;
        private String email;
        private String nickname;
        private CommunityEnum.UserRole userRole;
        private TokenDto token;
    }

    @Data
    public static class TokenDto {
        private String accessToken;


        public static TokenDto ofCreate(String accessToken) {
            TokenDto tokenDto = new TokenDto();
            tokenDto.accessToken = accessToken;
            return tokenDto;
        }
    }

    @Data
    public static class UserInfoDto {
        private Long id;
        private String email;
        private String nickname;
        private LocalDateTime createdDate;
        private LocalDateTime lastLoginDate;
    }
}
