package com.test.genesis.service;

import com.test.genesis.TestInitUtil;
import com.test.genesis.domain.user.Role;
import com.test.genesis.domain.user.UserEntity;
import com.test.genesis.domain.user.dto.UserSignRequest;
import com.test.genesis.domain.user.dto.UserUpdateRequest;
import com.test.genesis.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UserServiceMockTest {

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @InjectMocks
    UserService userService;

    @Test
    @DisplayName("회원 가입 할 수 있다.")
    public void _1() {
        UserSignRequest userSignRequest = new UserSignRequest("test@naver.com", "test", "password", "010-0100-1000", Role.USER.getType());
        given(userRepository.save(any())).willReturn(userSignRequest.toEntity());
        UserEntity userEntity = userService.sign(userSignRequest);

        assertThat(userSignRequest.email()).isEqualTo(userEntity.getEmail());
        assertThat(userSignRequest.name()).isEqualTo(userEntity.getName());
        assertThat(userSignRequest.phoneNumber()).isEqualTo(userEntity.getPhoneNumber());
        assertThat(userSignRequest.type()).isEqualTo(userEntity.getRole().getType());
    }

    @Test
    @DisplayName("유저를 조회할 수 있다.")
    public void _2() {
        UserEntity user = TestInitUtil.createUser();
        given(userRepository.findById(any())).willReturn(Optional.of(user));

        UserEntity userEntity = userService.findById(1L);

        assertThat(userEntity).isNotNull();
    }

    @Test
    @DisplayName("유저를 조회할 수 없다.")
    public void _2_fail() {
        given(userRepository.findById(1L)).willReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.findById(1L));
    }

    @Test
    @DisplayName("유저 정보를 수정할 수 있다.")
    public void _3() {
        UserEntity user = TestInitUtil.createUser();
        given(userRepository.findById(any())).willReturn(Optional.of(user));
        UserUpdateRequest userUpdateRequest = new UserUpdateRequest("update", "010-1111-1111", "password");
        userService.update(1L, userUpdateRequest);

        assertThat(user.getName()).isEqualTo(userUpdateRequest.name());
        assertThat(user.getPhoneNumber()).isEqualTo(userUpdateRequest.phoneNumber());
    }

    @Test
    @DisplayName("회원 탈퇴할 수 있다.")
    public void _4() {
        UserEntity user = TestInitUtil.createUser();
        given(userRepository.findById(any())).willReturn(Optional.of(user));

        userService.softDelete(1L);

        assertThat(user.isDelete()).isTrue();
        assertThat(user.getDeletedAt()).isNotNull();
    }
}