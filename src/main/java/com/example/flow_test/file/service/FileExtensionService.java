package com.example.flow_test.file.service;

import com.example.flow_test.common.exception.CustomException;
import com.example.flow_test.common.exception.ErrorCode;
import com.example.flow_test.file.domain.FileExtension;
import com.example.flow_test.file.domain.FileExtensionFormat;
import com.example.flow_test.file.dto.FileCustomExtensionRequest;
import com.example.flow_test.file.repository.FileExtensionRepository;
import com.example.flow_test.file.validation.FileExtensionValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FileExtensionService {
    private final FileExtensionRepository fileExtensionRepository;
    private final FileExtensionValidator fileExtensionValidator;

//    public  getFixExtensionName() {
//        fileExtensionRepository.
//    }

    public void inputFixExtensionName(String fileExtensionName) {
        if(!fileExtensionValidator.validateFileFixExtensionName(fileExtensionName)) {
            throw new CustomException(ErrorCode.INVALID_FILE_EXTENSION_NAME);
        }

        if(!fileExtensionValidator.validateFileExtensionNameOfLength(fileExtensionName)) {
            throw new CustomException(ErrorCode.EXCEED_FILE_EXTENSION_NAME_MAX_LENGTH);
        }

        FileExtension fileExtension = FileExtension.builder()
                .fileExtensionName(fileExtensionName)
                .fileExtensionFormat(FileExtensionFormat.FIX)
                .build();

        fileExtensionRepository.save(fileExtension);
    }

    public void inputCustomExtensionName(FileCustomExtensionRequest request) {
        String fileExtensionName = request.getFileExtensionName();
        int fileExtensionNameListCount = request.getFileExtensionNameListCount();

        if(fileExtensionRepository.findByFileExtensionName(fileExtensionName).isPresent()) {
            throw new CustomException(ErrorCode.DUPLICATED_FILE_EXTENSION_NAME);
        }

        if(!fileExtensionValidator.validateCustomFileExtensionListMaxSize(fileExtensionNameListCount)) {
            throw new CustomException(ErrorCode.EXCEED_CUSTOM_FILE_EXTENSION_NAME_LIST_SIZE);
        }

        FileExtension fileExtension = FileExtension.builder()
                .fileExtensionName(fileExtensionName)
                .fileExtensionFormat(FileExtensionFormat.CUSTOM)
                .build();

        fileExtensionRepository.save(fileExtension);
    }

    public void deleteFixExtensionName(Long fileId) {
        FileExtension fileExtension = fileExtensionRepository.findById(fileId)
                .orElseThrow(() -> new CustomException(ErrorCode.FILE_EXTENSION_NOT_FOUND));

        fileExtensionRepository.delete(fileExtension);
    }

    public void deleteCustomExtensionName(Long fileId) {
        FileExtension fileExtension = fileExtensionRepository.findById(fileId)
                .orElseThrow(() -> new CustomException(ErrorCode.FILE_EXTENSION_NOT_FOUND));

        fileExtensionRepository.delete(fileExtension);
    }
}
