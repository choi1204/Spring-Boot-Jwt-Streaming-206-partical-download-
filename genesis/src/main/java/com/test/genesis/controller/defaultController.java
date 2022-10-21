package com.test.genesis.controller;

import com.test.genesis.domain.user.UserEntity;
import com.test.genesis.domain.user.dto.UserSignRequest;
import com.test.genesis.domain.user.dto.UserUpdateRequest;
import com.test.genesis.security.annotation.LoginUser;
import com.test.genesis.security.annotation.UserSecured;
import com.test.genesis.security.jwt.JwtAccessToken;
import com.test.genesis.security.jwt.JwtTokenProvider;
import com.test.genesis.service.UserService;
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

    private final UserService userService;

    @PostMapping("/user")
    public ResponseEntity<Void> sign(@RequestBody UserSignRequest userSignRequest) {
        userService.sign(userSignRequest);
        return ResponseEntity.ok().build();
    }

    @UserSecured
    @PatchMapping("/user")
    public ResponseEntity<Void> update(@LoginUser UserEntity userEntity , @RequestBody UserUpdateRequest userUpdateRequest) {
        userService.update(userEntity.getId(), userUpdateRequest);
        return ResponseEntity.ok().build();
    }

    @UserSecured
    @PostMapping("/reissue")
    public ResponseEntity<JwtAccessToken> reissue(@RequestParam String refreshToken, @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(jwtTokenProvider.reissue(userDetails, refreshToken));
    }

    @UserSecured
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader(HttpHeaders.AUTHORIZATION) String accessToken) {
        jwtTokenProvider.blockToken(accessToken);
        return ResponseEntity.ok().build();
    }
}