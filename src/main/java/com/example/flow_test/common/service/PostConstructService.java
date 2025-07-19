package com.example.flow_test.common.service;

import com.example.flow_test.file.domain.FileExtension;
import com.example.flow_test.file.domain.FileExtensionFormat;
import com.example.flow_test.file.repository.FileExtensionRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostConstructService {
    private final FileExtensionRepository fileExtensionRepository;
    private final static List<String> FILE_FIX_EXTENSIONS = List.of("bat", "cmd", "com", "cpl", "exe", "scr", "js");

    @PostConstruct
    public void init() {
        fileExtensionRepository.deleteAll();

        for(String extension : FILE_FIX_EXTENSIONS) {
            FileExtension fileExtension = FileExtension.builder()
                    .fileExtensionName(extension)
                    .fileExtensionFormat(FileExtensionFormat.FIX)
                    .checked(false)
                    .createdAt(LocalDateTime.now())
                    .build();

            fileExtensionRepository.save(fileExtension);
        }
    }
}
