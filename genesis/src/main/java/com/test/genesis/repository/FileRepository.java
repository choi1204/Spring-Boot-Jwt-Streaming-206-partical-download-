package com.test.genesis.repository;

import com.test.genesis.domain.file.FileEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FileRepository extends JpaRepository<FileEntity, Long> {

    @EntityGraph(attributePaths = {"userEntity"})
    Optional<FileEntity> findEntityGraphById(Long fileId);

}
