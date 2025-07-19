package com.example.flow_test.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    /**
     * File-Extension
     */
    EXCEED_FILE_EXTENSION_NAME_MAX_LENGTH(400, "확장자 최대 입력 길이(20자)를 초과하였습니다."),
    INVALID_FILE_EXTENSION_NAME(400, "유효하지 않은 파일 차단 확장자 명입니다."),
    EXCEED_CUSTOM_FILE_EXTENSION_NAME_LIST_SIZE(400, "커스텀 확장자를 더이상 추가할 수 없습니다. 최대 200개까지 추가할 수 있습니다."),
    DUPLICATED_FILE_EXTENSION_NAME(400, "중복된 커스텀 파일 확장자입니다."),
    FILE_EXTENSION_NOT_FOUND(404, "해당 확장자 명을 찾을 수 없습니다."),

    /**
     * Server error
     */
    INTERNAL_SERVER_ERROR(500, "서버에 문제가 발생했습니다.");

    private final int statusCode;
    private final String errorReason;
}
