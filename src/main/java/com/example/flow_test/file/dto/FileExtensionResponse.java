package com.example.flow_test.file.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FileExtensionResponse {
    private Long fileId;
    private String fileExtensionName;
    private Boolean checked;

    public static FileExtensionResponse from(Long fileId, String fileExtensionName, Boolean checked) {
        return new FileExtensionResponse(fileId, fileExtensionName, checked);
    }

    public static FileExtensionResponse from(Long fileId, String fileExtensionName) {
        return new FileExtensionResponse(fileId, fileExtensionName, false);
    }
}
