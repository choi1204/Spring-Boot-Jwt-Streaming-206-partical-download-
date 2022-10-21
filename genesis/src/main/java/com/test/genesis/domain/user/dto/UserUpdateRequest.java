package com.test.genesis.domain.user.dto;

import javax.validation.constraints.NotBlank;

public record UserUpdateRequest (
        @NotBlank
        String name,

        @NotBlank
        String phoneNumber
) {
}
