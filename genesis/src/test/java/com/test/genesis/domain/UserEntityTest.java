package com.test.genesis.domain;

import com.test.genesis.domain.user.Role;
import com.test.genesis.domain.user.UserEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

class UserEntityTest {

    @Test
    @DisplayName("유저를 생성할 수 있다.")
    public void _1() {
        String email = "test@naver.com";
        String name = "choi";
        String phoneNumber = "010-0000-0000";
        String password = "password";
        Role role = Role.USER;
        UserEntity userEntity = UserEntity.builder().email(email).name(name).phoneNumber(phoneNumber).password(password).role(role).build();

        assertThat(userEntity.getEmail()).isEqualTo(email);
        assertThat(userEntity.getName()).isEqualTo(name);
        assertThat(userEntity.getPhoneNumber()).isEqualTo(phoneNumber);
        assertThat(userEntity.getPassword()).isEqualTo(password);
        assertThat(userEntity.getRole()).isEqualTo(Role.USER);
    }

    @Test
    @DisplayName("관리자를 생성할 수 있다.")
    public void _2() {
        String email = "test@naver.com";
        String name = "choi";
        String phoneNumber = "010-0000-0000";
        String password = "password";
        Role role = Role.ADMIN;
        UserEntity userEntity = UserEntity.builder().email(email).name(name).phoneNumber(phoneNumber).password(password).role(role).build();

        assertThat(userEntity.getEmail()).isEqualTo(email);
        assertThat(userEntity.getName()).isEqualTo(name);
        assertThat(userEntity.getPhoneNumber()).isEqualTo(phoneNumber);
        assertThat(userEntity.getRole()).isEqualTo(Role.ADMIN);
    }

    @Test
    @DisplayName("유저 정보를 수정할 수있다.")
    public void _3() {
        String email = "test@naver.com";
        String name = "choi";
        String phoneNumber = "010-0000-0000";
        String password = "password";
        Role role = Role.USER;
        UserEntity userEntity = UserEntity.builder().email(email).name(name).phoneNumber(phoneNumber).password(password).role(role).build();

        String updateName = "updateName";
        String updatePhoneNumber = "updatePhoneNumber";
        String updatePassword = "updatePassword";
        userEntity.update(updateName, updatePhoneNumber, updatePassword);

        assertThat(userEntity.getEmail()).isEqualTo(email);
        assertThat(userEntity.getName()).isEqualTo(updateName);
        assertThat(userEntity.getPhoneNumber()).isEqualTo(updatePhoneNumber);
        assertThat(userEntity.getPassword()).isEqualTo(updatePassword);
        assertThat(userEntity.getRole()).isEqualTo(Role.USER);
    }

    @Test
    @DisplayName("관리자 정보를 수정할 수있다.")
    public void _4() {
        String email = "test@naver.com";
        String name = "choi";
        String phoneNumber = "010-0000-0000";
        String password = "password";
        Role role = Role.ADMIN;
        UserEntity userEntity = UserEntity.builder().email(email).name(name).phoneNumber(phoneNumber).password(password).role(role).build();

        String updateName = "updateName";
        String updatePhoneNumber = "updatePhoneNumber";
        String updatePassword = "updatePassword";
        userEntity.update(updateName, updatePhoneNumber, updatePassword);

        assertThat(userEntity.getEmail()).isEqualTo(email);
        assertThat(userEntity.getName()).isEqualTo(updateName);
        assertThat(userEntity.getPhoneNumber()).isEqualTo(updatePhoneNumber);
        assertThat(userEntity.getPassword()).isEqualTo(updatePassword);
        assertThat(userEntity.getRole()).isEqualTo(Role.ADMIN);
    }

    @Test
    @DisplayName("유저 탈퇴할 수있다.")
    public void _5() {
        String email = "test@naver.com";
        String name = "choi";
        String phoneNumber = "010-0000-0000";
        String password = "password";
        Role role = Role.ADMIN;
        UserEntity userEntity = UserEntity.builder().email(email).name(name).phoneNumber(phoneNumber).password(password).role(role).build();

        userEntity.delete();

        assertThat(userEntity.getDeletedAt()).isNotNull();
        assertThat(userEntity.isDelete()).isTrue();
    }
}