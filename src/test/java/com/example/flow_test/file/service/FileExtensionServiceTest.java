
package com.example.flow_test.file.service;

import com.example.flow_test.common.exception.CustomException;
import com.example.flow_test.common.exception.ErrorCode;
import com.example.flow_test.file.domain.FileExtension;
import com.example.flow_test.file.domain.FileExtensionFormat;
import com.example.flow_test.file.dto.FileCustomExtensionRequest;
import com.example.flow_test.file.dto.FileExtensionResponse;
import com.example.flow_test.file.repository.FileExtensionRepository;
import com.example.flow_test.file.validation.FileExtensionValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FileExtensionServiceTest {

    private FileExtensionRepository repository;
    private FileExtensionValidator validator;
    private FileExtensionService service;

    @BeforeEach
    void setUp() {
        repository = mock(FileExtensionRepository.class);
        validator = mock(FileExtensionValidator.class);
        service = new FileExtensionService(repository, validator);
    }

    @Test
    @DisplayName("checked = true면 response에도 true로 반환되어야 한다.")
    void getFixExtensionName_shouldReturnCheckedExtensions() {
        FileExtension ext1 = FileExtension.builder()
                .fileId(1L)
                .fileExtensionName("exe")
                .checked(true)
                .fileExtensionFormat(FileExtensionFormat.FIX)
                .build();

        when(repository.findByFileExtensionFormat(FileExtensionFormat.FIX))
                .thenReturn(List.of(ext1));

        List<FileExtensionResponse> result = service.getFixExtensionName();

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getFileExtensionName()).isEqualTo("exe");
        assertThat(result.getFirst().getChecked()).isTrue();
    }

    @Test
    @DisplayName("inputFixExtensionName 메서드 호출 시 checked = true여야 한다.")
    void inputFixExtensionName_shouldSetCheckedTrue() {
        FileExtension ext = FileExtension.builder()
                .fileId(1L)
                .fileExtensionName("bat")
                .checked(false)
                .fileExtensionFormat(FileExtensionFormat.FIX)
                .build();

        when(repository.findById(1L))
                .thenReturn(Optional.of(ext));

        Long result = service.inputFixExtensionName(1L);

        assertThat(result).isEqualTo(1L);
        assertThat(ext.getChecked()).isTrue();
    }

    @Test
    @DisplayName("inputFixExtensionName 호출 시 값을 찾을 수 없으면 예외가 발생해야 한다.")
    void inputFixExtensionName_shouldThrowIfNotFound() {
        when(repository.findById(999L))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.inputFixExtensionName(999L))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining(ErrorCode.FILE_EXTENSION_NOT_FOUND.getErrorReason());
    }

    @Test
    @DisplayName("inputCustomExtensionName 호출 시 Custom 확장자를 포함해야 한다.")
    void inputCustomExtensionName_shouldSaveCustomExtension() {
        FileCustomExtensionRequest req = new FileCustomExtensionRequest();

        ReflectionTestUtils.setField(req, "fileExtensionName", "myext");
        ReflectionTestUtils.setField(req, "fileExtensionNameListCount", 3);

        when(repository.findByFileExtensionName("myext"))
                .thenReturn(Optional.empty());

        when(validator.validateFileExtensionNameOfLength("myext"))
                .thenReturn(true);

        when(validator.validateCustomFileExtensionListMaxSize(3))
                .thenReturn(true);

        when(validator.validateCustomFileExtensionNameHasSpace("myext"))
                .thenReturn(false);

        ArgumentCaptor<FileExtension> captor = ArgumentCaptor.forClass(FileExtension.class);
        when(repository.save(captor.capture())).thenAnswer(invocation -> {
            FileExtension fe = captor.getValue();

            ReflectionTestUtils.setField(fe, "fileId", 10L);
            return fe;
        });

        Long id = service.inputCustomExtensionName(req);

        assertThat(id).isEqualTo(10L);

        FileExtension saved = captor.getValue();
        assertThat(saved.getFileExtensionName()).isEqualTo("myext");
        assertThat(saved.getFileExtensionFormat()).isEqualTo(FileExtensionFormat.CUSTOM);
    }

    @Test
    @DisplayName("deleteFixExtensionName 호출 시 checked = false여야 한다.")
    void deleteFixExtensionName_shouldSetCheckedFalse() {
        FileExtension ext = FileExtension.builder()
                .fileId(2L)
                .fileExtensionName("com")
                .checked(true)
                .fileExtensionFormat(FileExtensionFormat.FIX)
                .build();

        when(repository.findById(2L))
                .thenReturn(Optional.of(ext));

        service.deleteFixExtensionName(2L);

        assertThat(ext.getChecked()).isFalse();
    }

    @Test
    @DisplayName("deleteCustomExtensionName 호출 시 해당 확장자 명이 삭제되어야 한다.")
    void deleteCustomExtensionName_shouldDelete() {
        FileExtension ext = FileExtension.builder()
                .fileId(3L)
                .fileExtensionName("temp")
                .fileExtensionFormat(FileExtensionFormat.CUSTOM)
                .build();

        when(repository.findById(3L))
                .thenReturn(Optional.of(ext));

        service.deleteCustomExtensionName(3L);

        verify(repository).delete(ext);
    }
}
