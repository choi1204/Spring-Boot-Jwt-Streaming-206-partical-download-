package com.test.genesis.domain.user.dto;

import javax.validation.constraints.NotBlank;

public record UserLoginRequest(
        @NotBlank(message = "로그인 id를 입력해주세요")
        String email,
        @NotBlank(message = "로그인 패스워드를 입력해주세요")
        String password) {
}
