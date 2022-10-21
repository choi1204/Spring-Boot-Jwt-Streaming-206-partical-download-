package com.test.genesis.domain.user.dto;

import javax.validation.constraints.NotBlank;

public record UserLoginRequest(
        @NotBlank
        String email,
        @NotBlank
        String password) {
}
