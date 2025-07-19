package com.example.flow_test.file.repository;

import com.example.flow_test.file.domain.FileExtension;
import com.example.flow_test.file.domain.FileExtensionFormat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface FileExtensionRepository extends JpaRepository<FileExtension, Long> {
    Optional<FileExtension> findByFileExtensionName(String fileExtensionName);

    List<FileExtension> findByFileExtensionFormat(FileExtensionFormat fileExtensionFormat);

    Boolean existsByFileExtensionName(String fileExtensionName);
}
