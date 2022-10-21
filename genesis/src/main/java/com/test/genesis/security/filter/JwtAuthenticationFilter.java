package com.test.genesis.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.test.genesis.domain.user.UserEntity;
import com.test.genesis.domain.user.dto.UserLoginRequest;
import com.test.genesis.security.auth.UserEntityDetail;
import com.test.genesis.security.jwt.JwtAccessToken;
import com.test.genesis.security.jwt.JwtRefreshToken;
import com.test.genesis.security.jwt.JwtTokenProvider;
import com.test.genesis.security.jwt.JwtTokenResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RequiredArgsConstructor
public final class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;

    private final RedisTemplate<String, Object> redisTemplate;

    private final ObjectMapper objectMapper = getObjectMapper();
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        UsernamePasswordAuthenticationToken authenticationToken = null;
        try {
            UserLoginRequest userLoginRequest = objectMapper.readValue(
                    request.getInputStream(), UserLoginRequest.class);
            authenticationToken = new UsernamePasswordAuthenticationToken(
                    userLoginRequest.email(), userLoginRequest.password());
        } catch (IOException e) {
            log.error("Error logging in {}", e.getMessage());
        }
        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain, Authentication authentication) throws IOException {
        UserEntityDetail userDetail = (UserEntityDetail) authentication.getPrincipal();
        JwtTokenResponse tokenResponse = makePayload(userDetail);
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(userDetail.getUsername(), tokenResponse.jwtRefreshToken());

        response.setContentType(APPLICATION_JSON_VALUE);
        objectMapper.writeValue(response.getOutputStream(), tokenResponse);
    }

    private JwtTokenResponse makePayload(UserEntityDetail userDetail) {
        JwtAccessToken accessToken = jwtTokenProvider.generateAccessToken(userDetail);
        JwtRefreshToken refreshToken = jwtTokenProvider.generateRefreshToken(userDetail);

        return new JwtTokenResponse(accessToken, refreshToken);
    }

    private ObjectMapper getObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return objectMapper;
    }
}
