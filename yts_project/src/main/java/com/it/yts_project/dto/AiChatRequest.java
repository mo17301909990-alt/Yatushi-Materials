package com.it.yts_project.dto;

import lombok.Data;

@Data
public class AiChatRequest {
    private String message;
    private String systemPrompt;
}
