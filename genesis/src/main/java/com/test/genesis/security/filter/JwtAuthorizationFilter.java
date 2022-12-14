package com.test.genesis.security.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.genesis.domain.user.Role;
import com.test.genesis.domain.user.UserEntity;
import com.test.genesis.security.auth.UserEntityDetail;
import com.test.genesis.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (!Objects.isNull(bearerToken)) {
            try {
                if (redisTemplate.opsForValue().get(bearerToken) != null) {
                    logger.warn("this token already logout!");
                } else {
                    Authentication authentication = getAuthentication(bearerToken);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (Exception e) {
                log.error("Error logging in {}", e.getMessage());
                response.setHeader("error", e.getMessage());
                response.setStatus(HttpStatus.FORBIDDEN.value());
                Map<String, String> error = new HashMap<>();
                error.put("error_message", e.getMessage());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), error);
            }
        }
        filterChain.doFilter(request, response);
    }

    private Authentication getAuthentication(String token) {
        DecodedJWT decodedJWT = jwtTokenProvider.verifyJwtToken(token);
        String email = decodedJWT.getSubject();
        String[] rolesString = decodedJWT.getClaim("roles").asArray(String.class);
        Long id = decodedJWT.getClaim("id").asLong();
        Collection<GrantedAuthority> roles = new ArrayList<>();
        for (String roleString : rolesString) {
            roles.add(new SimpleGrantedAuthority(roleString));
        }

        UserEntity userEntity = UserEntity.builder()
                .id(id)
                .email(email)
                .build();
        UserEntityDetail userEntityDetail = new UserEntityDetail(userEntity);

        return new UsernamePasswordAuthenticationToken(userEntityDetail, null, roles);
    }


}
