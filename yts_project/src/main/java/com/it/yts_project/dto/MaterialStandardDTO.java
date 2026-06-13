package com.it.yts_project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaterialStandardDTO {
    private String id;
    private String title;
    private String fileName;
    private String filePath;
    private String updatedAt;
    private String category;
    private List<String> tags;
}
