package com.it.yts_project.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SiliconeSandblastUvCompatibility {
    private Integer id;
    private Integer productId;
    private String postProcessingStep;
    private String compatibilityStatus;
    private Integer displayOrder;
    private LocalDateTime createdAt;
    private String productName;
}
