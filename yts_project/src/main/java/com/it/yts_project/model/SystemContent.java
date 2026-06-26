package com.it.yts_project.model;

import lombok.Data;

import java.time.OffsetDateTime;

/**
 * 系統可編輯內容（如燙金物料操作指南），存 HTML。
 * <p>updated_at 在 PostgreSQL 中為 TIMESTAMPTZ，需用 {@link OffsetDateTime}，不可使用 LocalDateTime（驅動無法直接轉換）。
 */
@Data
public class SystemContent {
    private Long id;
    private String contentKey;
    private String title;
    private String bodyHtml;
    private OffsetDateTime updatedAt;
    private Integer updatedBy;
}
