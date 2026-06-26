package com.it.yts_project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 后加工工序分类分组
 * 用于前端两级选择：先选大类（印刷/烫金/过胶/丝印/植毛/啤/手工/其他），再选具体步骤
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StepCategoryGroup {
    /** 大类名称（印刷/烫金/过胶/丝印/植毛/啤/手工/其他） */
    private String category;

    /** 该大类下的所有具体步骤 */
    private List<String> steps;
}
