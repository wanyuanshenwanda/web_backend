package com.xzf.backend.entity.dto;


import lombok.Data;

@Data
public class FileUploadDto {
    private String localPath;
    private String originalFilename;
}
