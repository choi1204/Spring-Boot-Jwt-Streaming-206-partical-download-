package com.test.genesis.domain.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@Getter
@RequiredArgsConstructor
public enum Role implements GrantedAuthority {

    USER("ROLE_USER", "일반 유저"),
    ADMIN("ROLE_ADMIN", "관리자");

    private final String authority;
    private final String description;

}
