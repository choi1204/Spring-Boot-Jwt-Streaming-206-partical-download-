package com.test.genesis;

import com.test.genesis.domain.user.UserEntity;

public class TestInitUtil {

    public static UserEntity createUser() {
        String email = "test@naver.com";
        String name = "choi";
        String phoneNumber = "010-0000-0000";
        return UserEntity.createUser(email, name, phoneNumber);
    }
}
