package com.test.genesis.domain.user.dto;

import javax.validation.constraints.NotBlank;

public record UserUpdateRequest (
        @NotBlank(message = "수정할 이름을 입력해주세요")
        String name,
        @NotBlank(message = "수정할 휴대폰 번호를 입력해주세요")
        String phoneNumber,
        @NotBlank(message = "수정할 비밀번호를 입력해주세요")
        String password
) {
}