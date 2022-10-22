package com.test.genesis.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.test.genesis.domain.user.UserEntity;
import com.test.genesis.security.auth.UserEntityDetail;
import com.test.genesis.util.TimerUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final JwtProperties jwtProperties;

    private final RedisTemplate<String, Object> redisTemplate;

    public Algorithm getAlgorithm() {
        return Algorithm.HMAC256(jwtProperties.getSecret().getBytes());
    }

    private String generateToken(UserDetails userDetails, Date expirationDate) {
        Algorithm algorithm = getAlgorithm();
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
                = TimerUtils.getExpirationDate(jwtProperties.getExpirationTime() * 7);
        String token = generateToken(userDetails, expirationDateForRefreshToken);
        return new JwtRefreshToken(jwtProperties.getTokenPrefix() + token, expirationDateForRefreshToken);
    }

    public boolean isExpired(String token) {
        DecodedJWT decodedJWT = verifyJwtToken(token);
        return TimerUtils.isExpired(decodedJWT.getExpiresAt());
    }
    public DecodedJWT verifyJwtToken(String token) {
        if (token == null || !token.startsWith(jwtProperties.getTokenPrefix())) {
            throw new JWTVerificationException("부적절한 토큰값입니다.");
        }
        token = token.substring(jwtProperties.getTokenPrefix().length());
        Algorithm algorithm = getAlgorithm();
        JWTVerifier jwtVerifier = JWT.require(algorithm).build();
        return jwtVerifier.verify(token);
    }

    public JwtAccessToken reissue(UserDetails userDetail, String refreshToken) {
        String tokenInRedis = redisTemplate.opsForValue().get(userDetail.getUsername()).toString();
        if (Objects.isNull(refreshToken) || !refreshToken.equals(tokenInRedis)) {
            throw new RuntimeException("refreshToken이 잘못됬습니다.");
        }
        return generateAccessToken(userDetail);
    }

    public void blockToken(String token) {
        DecodedJWT decodedJWT = verifyJwtToken(token);
        String username = decodedJWT.getSubject();
        redisTemplate.delete(username);
        redisTemplate.opsForValue().set(token, true);
        redisTemplate.expire(token, jwtProperties.getExpirationTime(), TimeUnit.MILLISECONDS);
    }
}
