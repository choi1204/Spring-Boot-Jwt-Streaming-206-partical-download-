package com.test.genesis.domain.user.dto;

import com.sun.istack.NotNull;
import com.test.genesis.domain.user.Role;
import com.test.genesis.domain.user.UserEntity;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;

public record UserSignRequest (
    @NotBlank
    String email,

    @NotBlank
    String name,

    @NotBlank
    String phoneNumber,

    Role role
) {
    public UserEntity toEntity() {
        UserEntity userEntity = null;
        switch (role) {
            case USER -> userEntity = UserEntity.createUser(email, name, phoneNumber);
            case ADMIN -> userEntity = UserEntity.createAdmin(email, name, phoneNumber);
        }
        return userEntity;
    }
}
