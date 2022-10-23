package com.test.genesis.domain.user.dto;

import com.test.genesis.domain.user.Role;
import com.test.genesis.domain.user.UserEntity;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public record UserSignRequest(
        @Email(message = "이메일 형식이 아닙니다.")
        @NotBlank(message = "유저 이메일을 입력해주세요")
        String email,

        @NotBlank(message = "유저 이름을 입력해주세요")
        String name,

        @NotBlank(message = "유저 패스워드를 입력해주세요")
        String password,

        @NotBlank(message = "휴대폰 번호를 입력해주세요")
        String phoneNumber,
        @NotBlank(message = "유저 타입을 골라주세요.(ADMIN, USER)")
        String type
) {
    public UserEntity toEntity() {
        Role role = Role.getRole(type);

        return UserEntity.builder()
                .email(email)
                .name(name)
                .password(password)
                .phoneNumber(phoneNumber)
                .role(role)
                .build();
    }
}
