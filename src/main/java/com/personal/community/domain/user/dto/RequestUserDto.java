package com.personal.community.domain.user.dto;

import com.personal.community.config.exception.CommunityException;
import com.personal.community.config.exception.ExceptionEnum;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

public class RequestUserDto {

    @Data
    public static class UserSignupDto {

        @Email(message = "올바른 이메일 형식을 입력해주세요.")
        private String email;

        @Pattern(regexp = "^[A-Za-z가-힣][a-zA-Z가-힣0-9]{1,7}$", message = "닉네임은 2자 이상 8자 이하여야 합니다.")
        private String nickname;

        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[a-zA-Z0-9]{8,20}", message = "비밀번호는 영문, 숫자를 섞어 8자 이상 20자 이하여야 합니다.")
        private String password;
        private String confirmPassword;

        public void checkPassword() {
            if(!password.equals(confirmPassword)){
                throw CommunityException.of(ExceptionEnum.MISMATCH_PASSWORD, "비밀번호가 일치하지 않습니다.");
            }
        }
    }
}
