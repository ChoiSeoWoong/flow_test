package com.example.flow_test.file.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Table(name = "fileextension")
@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FileExtension {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fileId;

    @Column(length = 20)
    private String fileExtensionName;

    @Column
    @Enumerated(EnumType.STRING)
    private FileExtensionFormat fileExtensionFormat;

    @CreatedDate
    private LocalDateTime createdAt;
}
