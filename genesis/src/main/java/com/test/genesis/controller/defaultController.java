package com.test.genesis.controller;

import com.test.genesis.security.jwt.JwtAccessToken;
import com.test.genesis.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class defaultController {

    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/reissue")
    public ResponseEntity<JwtAccessToken> reissue(@RequestParam String refreshToken, @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(jwtTokenProvider.reissue(userDetails, refreshToken));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader(HttpHeaders.AUTHORIZATION) String accessToken) {
        jwtTokenProvider.blockToken(accessToken);
        return ResponseEntity.noContent().build();
    }
}
