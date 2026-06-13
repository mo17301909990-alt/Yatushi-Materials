package com.it.yts_project.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminChangeSnapshot {

    private Long id;
    private String entityType;
    private Long entityId;
    private String beforeData;
    private String afterData;
    private String checksum;
    private LocalDateTime createdAt;
    private Integer createdBy;
}
