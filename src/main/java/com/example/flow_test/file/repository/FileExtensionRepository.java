package com.example.flow_test.file.repository;

import com.example.flow_test.file.domain.FileExtension;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface FileExtensionRepository extends JpaRepository<FileExtension, Long> {
    Optional<FileExtension> findByFileExtensionName(String fileExtensionName);

//    @Query("""
//        SELECT
//    """)
}
