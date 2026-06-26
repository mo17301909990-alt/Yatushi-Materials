package com.it.yts_project.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

/**
 * 后台操作日志：记录谁在何时对哪块数据做了何种操作，供管理员审计
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminOperationLog {

    private Long id;
    private Integer operatorId;
    private String operatorName;
    private LocalDateTime operatedAt;
    private String operationType;
    private String module;
    private String targetType;
    private String targetId;
    private String targetLabel;
    private String changeSummary;
    private String changeDetail;
    private String ip;
    private String userAgent;
}
