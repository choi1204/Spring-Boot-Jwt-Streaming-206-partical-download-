package com.test.genesis.service;

import com.test.genesis.domain.user.Role;
import com.test.genesis.domain.user.UserEntity;
import com.test.genesis.domain.user.dto.UserSignRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class UserServiceTest {

    @Autowired
    UserService userService;

    UserEntity user;
    UserEntity delete;
    @BeforeEach
    public void init() {
        userService.deleteAll();
        UserSignRequest userSignRequest = new UserSignRequest("test@naver.com", "test", "010-0100-1000", Role.USER);
        user = userService.sign(userSignRequest);

        UserSignRequest deleteUser = new UserSignRequest("delete@naver.com", "delete", "010-0000-0000", Role.USER);
        delete = userService.sign(deleteUser);
        userService.softDelete(delete.getId());
    }

    @Test
    @DisplayName("isDelete가 false인 유저는 조회할 수 있다.")
    public void _1() {
        UserEntity userEntity = userService.findById(user.getId());
        assertThat(userEntity).isNotNull();
    }

    @Test
    @DisplayName("isDelete가 true인 유저는 조회할 수 없다.")
    public void _2() {
        assertThrows(RuntimeException.class, () -> userService.findById(delete.getId()));
    }
}