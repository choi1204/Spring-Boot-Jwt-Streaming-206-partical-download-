package com.test.genesis;

import com.test.genesis.domain.user.Role;
import com.test.genesis.domain.user.UserEntity;
import com.test.genesis.security.auth.UserEntityDetail;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.ArrayList;
import java.util.Collection;

public class WithMockAdminSecurityContextFactory implements WithSecurityContextFactory<WithMockAdmin> {

    @Override
    public SecurityContext createSecurityContext(WithMockAdmin annotation) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        UserEntity user = TestInitUtil.createAdmin();
        UserEntityDetail userEntityDetail = new UserEntityDetail(user);
        Collection<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority(Role.USER.getAuthority()));

        Authentication auth = new UsernamePasswordAuthenticationToken(userEntityDetail, null, roles);
        context.setAuthentication(auth);
        return context;
    }
}