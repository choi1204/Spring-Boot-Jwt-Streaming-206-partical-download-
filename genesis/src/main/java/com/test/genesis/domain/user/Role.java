package com.test.genesis.domain.user;

import com.test.genesis.strategy.EnumFindable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@Getter
@RequiredArgsConstructor
public enum Role implements GrantedAuthority, EnumFindable {

    USER("USER", "일반 유저"),
    ADMIN("ADMIN", "관리자");

    private final String type;
    private final String description;

    @Override
    public String getAuthority() {
        return "ROLE_" + getType();
    }

    public static Role getRole(String type) {
        return EnumFindable.find(type, Role.values());
    }
}
