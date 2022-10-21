package com.test.genesis.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.test.genesis.security.auth.UserEntityDetail;
import com.test.genesis.util.TimerUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final JwtProperties jwtProperties;

    public Algorithm getAlgorithm() {
        return Algorithm.HMAC256(jwtProperties.getSecret());
    }

    private String generateToken(UserDetails userDetails, Date expirationDate) {
        Algorithm algorithm = Algorithm.HMAC256(jwtProperties.getSecret().getBytes());
        UserEntityDetail userDetail = (UserEntityDetail) userDetails;
        return JWT.create()
                .withSubject(userDetail.getUsername())
                .withExpiresAt(expirationDate)
                .withClaim("id", userDetail.getId())
                .withClaim("roles", userDetail.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);
    }

    public JwtAccessToken generateAccessToken(UserDetails userDetails) {
        Date expirationDateForAccessToken
                = TimerUtils.getExpirationDate(jwtProperties.getExpirationTime());
        String token = generateToken(userDetails, expirationDateForAccessToken);
        return new JwtAccessToken(jwtProperties.getTokenPrefix() + token, expirationDateForAccessToken);
    }

    public JwtRefreshToken generateRefreshToken(UserDetails userDetails) {
        Date expirationDateForRefreshToken
                = TimerUtils.getExpirationDate(jwtProperties.getExpirationTime() * 2);
        String token = generateToken(userDetails, expirationDateForRefreshToken);
        return new JwtRefreshToken(token, expirationDateForRefreshToken);
    }
}
