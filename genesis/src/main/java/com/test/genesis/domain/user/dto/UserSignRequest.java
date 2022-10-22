package com.test.genesis.domain.user.dto;

import com.test.genesis.domain.user.Role;
import com.test.genesis.domain.user.UserEntity;

import javax.validation.constraints.NotBlank;

public record UserSignRequest(
        @NotBlank
        String email,

        @NotBlank
        String name,

        @NotBlank
        String password,

        @NotBlank
        String phoneNumber,
        @NotBlank
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
