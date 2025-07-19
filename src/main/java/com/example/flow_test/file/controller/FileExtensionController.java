package com.example.flow_test.file.controller;

import com.example.flow_test.file.dto.FileCustomExtensionRequest;
import com.example.flow_test.file.service.FileExtensionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/file-extension")
public class FileExtensionController {
    private final FileExtensionService fileExtensionService;

//    @GetMapping("/fix/all")
//    public ResponseEntity<?> getFixExtensionName() {
//        fileExtensionService.
//    }
//
//    @GetMapping("/custom/all")
//    public ResponseEntity<?> getCustomExtensionName() {
//
//    }

    @PostMapping("/fix")
    public ResponseEntity<Void> inputFixExtensionName(@RequestParam("fileExtensionName") String fileExtensionName) {
        fileExtensionService.inputFixExtensionName(fileExtensionName);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/custom")
    public ResponseEntity<Void> inputCustomExtensionName(@RequestBody FileCustomExtensionRequest request) {
        fileExtensionService.inputCustomExtensionName(request);
        return ResponseEntity.ok().build();
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
