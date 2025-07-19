package com.example.flow_test.file.controller;

import com.example.flow_test.file.dto.FileCustomExtensionRequest;
import com.example.flow_test.file.dto.FileExtensionResponse;
import com.example.flow_test.file.service.FileExtensionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/file-extension")
public class FileExtensionController {
    private final FileExtensionService fileExtensionService;

    @GetMapping("/fix/all")
    public ResponseEntity<List<FileExtensionResponse>> getFixExtensionName() {
        List<FileExtensionResponse> responses = fileExtensionService.getFixExtensionName();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/custom/all")
    public ResponseEntity<List<FileExtensionResponse>> getCustomExtensionName() {
        List<FileExtensionResponse> responses = fileExtensionService.getCustomExtensionName();
        return ResponseEntity.ok(responses);
    }

    @PostMapping("/fix")
    public ResponseEntity<Long> inputFixExtensionName(@RequestParam("fileId") Long fileId) {
        Long responseFileId = fileExtensionService.inputFixExtensionName(fileId);
        return ResponseEntity.ok().body(responseFileId);
    }

    @PostMapping("/custom")
    public ResponseEntity<Long> inputCustomExtensionName(@RequestBody FileCustomExtensionRequest request) {
        Long fileId = fileExtensionService.inputCustomExtensionName(request);
        return ResponseEntity.ok().body(fileId);
    }

    @DeleteMapping("/fix/{fileId}")
    public ResponseEntity<Void> deleteFixExtensionName(@PathVariable("fileId") Long fileId) {
        fileExtensionService.deleteFixExtensionName(fileId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/custom/{fileId}")
    public ResponseEntity<Void> deleteCustomExtensionName(@PathVariable("fileId") Long fileId) {
        fileExtensionService.deleteCustomExtensionName(fileId);
        return ResponseEntity.ok().build();
    }
}