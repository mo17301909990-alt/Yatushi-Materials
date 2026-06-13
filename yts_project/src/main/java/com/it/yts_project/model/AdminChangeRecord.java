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
public class AdminChangeRecord {

    private Long id;
    private String entityType;
    private Long entityId;
    private String changeSet;
    private String snapshotIds;
    private Integer operatorId;
    private String operatorName;
    private String risk;
    private String reason;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime committedAt;
    private LocalDateTime rolledBackAt;
    private Long rollbackRecordId;
    private String errorMessage;
}
