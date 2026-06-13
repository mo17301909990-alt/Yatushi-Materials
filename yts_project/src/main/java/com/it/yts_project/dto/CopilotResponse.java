package com.it.yts_project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CopilotResponse {
    private String reply;
    private List<String> suggestions;
    private Map<String, Object> params;
    @Builder.Default
    private String type = "text";
    @Builder.Default
    private Map<String, Object> data = new java.util.HashMap<>();
}
