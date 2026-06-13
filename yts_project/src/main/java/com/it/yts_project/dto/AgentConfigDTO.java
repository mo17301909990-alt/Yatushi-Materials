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
public class AgentConfigDTO {
    private String id;
    private String name;
    private String role;
    private String department;
    private String description;
    private String status;
    private List<AgentSkillDTO> skills;
    private List<MaterialCategoryDTO> materialCategories;
}
