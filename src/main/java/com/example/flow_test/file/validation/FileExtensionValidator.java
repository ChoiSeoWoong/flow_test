package com.example.flow_test.file.validation;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FileExtensionValidator {
    private final static int FILE_EXTENSION_NAME_MAX_LENGTH = 20;
    private final static int CUSTOM_FILE_EXTENSION_LIST_MAX_SIZE = 200;
    private final List<String> fileFixExtensionNameList = List.of("bat", "cmd", "com", "cpl", "exe", "scr", "js");

    public boolean validateFileFixExtensionName(String fileExtensionName) {
        return fileFixExtensionNameList.contains(fileExtensionName);
    }

    public boolean validateFileExtensionNameOfLength(String fileExtensionName) {
        return fileExtensionName.length() <= FILE_EXTENSION_NAME_MAX_LENGTH;
    }

    public boolean validateCustomFileExtensionListMaxSize(int fileExtensionListSize) {
        return fileExtensionListSize <= CUSTOM_FILE_EXTENSION_LIST_MAX_SIZE;
    }

    public boolean validateCustomFileExtensionNameHasSpace(String fileExtensionName) {
        return fileExtensionName.contains(" ");
    }
}
