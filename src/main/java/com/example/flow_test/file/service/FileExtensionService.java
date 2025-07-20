package com.example.flow_test.file.service;

import com.example.flow_test.common.exception.CustomException;
import com.example.flow_test.common.exception.ErrorCode;
import com.example.flow_test.file.domain.FileExtension;
import com.example.flow_test.file.domain.FileExtensionFormat;
import com.example.flow_test.file.dto.FileCustomExtensionRequest;
import com.example.flow_test.file.dto.FileExtensionResponse;
import com.example.flow_test.file.repository.FileExtensionRepository;
import com.example.flow_test.file.validation.FileExtensionValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class FileExtensionService {
    private final FileExtensionRepository fileExtensionRepository;
    private final FileExtensionValidator fileExtensionValidator;

    public List<FileExtensionResponse> getFixExtensionName() {
        List<FileExtension> fileExtensions = fileExtensionRepository.findByFileExtensionFormat(FileExtensionFormat.FIX);

        return fileExtensions.stream()
                .filter(Objects::nonNull)
                .map(extension -> FileExtensionResponse.from(extension.getFileId(), extension.getFileExtensionName(), extension.getChecked()))
                .toList();
    }

    public List<FileExtensionResponse> getCustomExtensionName() {
        List<FileExtension> fileExtensions = fileExtensionRepository.findByFileExtensionFormat(FileExtensionFormat.CUSTOM);
        return fileExtensions.stream()
                .map(fileExtension ->
                        FileExtensionResponse.from(fileExtension.getFileId(), fileExtension.getFileExtensionName())
                )
                .toList();
    }

    @Transactional
    public Long inputFixExtensionName(Long fileId) {
        synchronized (this) {
            FileExtension fileExtension = fileExtensionRepository.findById(fileId)
                    .orElseThrow(() -> new CustomException(ErrorCode.FILE_EXTENSION_NOT_FOUND));

            if(!fileExtension.getChecked()) {
                fileExtension.setCheckedTrue();
            }

            return fileExtension.getFileId();
        }
    }

    @Transactional
    public Long inputCustomExtensionName(FileCustomExtensionRequest request) {
        String fileExtensionName = request.getFileExtensionName();
        int fileExtensionNameListCount = request.getFileExtensionNameListCount();

        if (fileExtensionRepository.findByFileExtensionName(fileExtensionName).isPresent()) {
            throw new CustomException(ErrorCode.DUPLICATED_FILE_EXTENSION_NAME);
        }

        if (!fileExtensionValidator.validateFileExtensionNameOfLength(fileExtensionName)) {
            throw new CustomException(ErrorCode.EXCEED_FILE_EXTENSION_NAME_MAX_LENGTH);
        }

        if (!fileExtensionValidator.validateCustomFileExtensionListMaxSize(fileExtensionNameListCount)) {
            throw new CustomException(ErrorCode.EXCEED_CUSTOM_FILE_EXTENSION_NAME_LIST_SIZE);
        }

        if(fileExtensionValidator.validateCustomFileExtensionNameHasSpace(fileExtensionName)) {
            throw new CustomException(ErrorCode.FILE_EXTENSION_NAME_SHOULD_NOT_HAVE_SPACE);
        }

        FileExtension fileExtension = FileExtension.builder()
                .fileExtensionName(fileExtensionName)
                .fileExtensionFormat(FileExtensionFormat.CUSTOM)
                .checked(false)
                .createdAt(LocalDateTime.now())
                .build();

        fileExtensionRepository.save(fileExtension);

        return fileExtension.getFileId();
    }

    @Transactional
    public void deleteFixExtensionName(Long fileId) {
        synchronized (this) {
            FileExtension fileExtension = fileExtensionRepository.findById(fileId)
                    .orElseThrow(() -> new CustomException(ErrorCode.FILE_EXTENSION_NOT_FOUND));

            if(fileExtension.getChecked()) {
                fileExtension.setCheckedFalse();
            }
        }
    }

    @Transactional
    public void deleteCustomExtensionName(Long fileId) {
        synchronized (this) {
            FileExtension fileExtension = fileExtensionRepository.findById(fileId)
                    .orElseThrow(() -> new CustomException(ErrorCode.FILE_EXTENSION_NOT_FOUND));

            fileExtensionRepository.delete(fileExtension);
        }
    }
}
