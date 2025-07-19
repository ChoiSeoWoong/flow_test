package com.example.flow_test.file.service;

import com.example.flow_test.file.repository.FileExtensionRepository;
import com.example.flow_test.file.validation.FileExtensionValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;


class FileExtensionServiceTest {
    @InjectMocks
    private FileExtensionService fileExtensionService;

    @Mock
    private FileExtensionRepository fileExtensionRepository;

    @Mock
    private FileExtensionValidator fileExtensionValidator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("고정 확장자 조회")
    void getFixExtensionName_ShouldReturnList() {

    }
}