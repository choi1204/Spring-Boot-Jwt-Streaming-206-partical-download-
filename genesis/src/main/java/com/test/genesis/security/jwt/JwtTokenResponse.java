package com.test.genesis.security.jwt;

import com.fasterxml.jackson.annotation.JsonProperty;

public record JwtTokenResponse(
        @JsonProperty(value = "accessToken")
        JwtAccessToken jwtAccessToken,
        @JsonProperty(value = "refreshToken")
        JwtRefreshToken jwtRefreshToken)
{
}