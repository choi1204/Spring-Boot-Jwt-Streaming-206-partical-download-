package com.test.genesis.service;

import com.test.genesis.domain.user.UserEntity;
import com.test.genesis.domain.user.dto.UserSignRequest;
import com.test.genesis.domain.user.dto.UserUpdateRequest;
import com.test.genesis.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public UserEntity sign(UserSignRequest userSignRequest) {
        UserEntity userEntity = userSignRequest.toEntity();
        return userRepository.save(userEntity);
    }

    public UserEntity findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("존재하지 않는 id입니다."));
    }

    public UserEntity findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("존재하지 않는 id입니다."));
    }

    public List<UserEntity> findAll() {
        return userRepository.findAll();
    }

    @Transactional
    public void update(Long id, UserUpdateRequest userUpdateRequest) {
        UserEntity userEntity = findById(id);
        userEntity.update(userUpdateRequest.name(), userUpdateRequest.phoneNumber());
    }

    @Transactional
    public void softDelete(Long id) {
        UserEntity userEntity = findById(id);
        userEntity.delete();
    }

    @Transactional
    public void deleteAll() {
        userRepository.deleteAllInBatch();
    }
}
