package com.example.flow_test.common.exception;

import com.example.flow_test.common.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    // CustomException
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<?> handleCustomException(CustomException e) {
        log.error("log info : {}", e.getErrorCode().getErrorReason());

        ErrorCode code = e.getErrorCode();
        ErrorResponse response = ErrorResponse.builder()
                .code(code.getStatusCode())
                .message(code.getErrorReason())
                .build();

        return ResponseEntity
                .status(code.getStatusCode())
                .body(response);
    }

    // ServerException
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception e) {
        log.error("log info : {}", e.getMessage());

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .build();
    }
}
