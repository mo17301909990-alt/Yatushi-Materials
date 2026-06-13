package com.it.yts_project.mapper;

import com.it.yts_project.model.RgResourceRuleCondition;
import com.it.yts_project.model.RgResourceRuleHeader;
import org.apache.ibatis.annotations.*;
import java.util.List;

/**
 * 资源组规则Mapper
 */
@Mapper
public interface RgResourceRuleMapper {
    
    /**
     * 查询资源组的规则头
     */
    @Select("SELECT * FROM rg_resource_rule_header WHERE resource_group_id = #{resourceGroupId} AND is_active = true")
    @Results({
        @Result(property = "resourceGroupId", column = "resource_group_id"),
        @Result(property = "ruleType", column = "rule_type"),
        @Result(property = "ruleName", column = "rule_name"),
        @Result(property = "isActive", column = "is_active"),
        @Result(property = "createdAt", column = "created_at")
    })
    List<RgResourceRuleHeader> findHeadersByResourceGroupId(Integer resourceGroupId);
    
    /**
     * 查询规则头下的所有条件
     */
    @Select("SELECT * FROM rg_resource_rule_condition WHERE rule_header_id = #{ruleHeaderId} ORDER BY sort_order")
    @Results({
        @Result(property = "ruleHeaderId", column = "rule_header_id"),
        @Result(property = "conditionGroup", column = "condition_group"),
        @Result(property = "paramCode", column = "param_code"),
        @Result(property = "paramName", column = "param_name"),
        @Result(property = "valueMin", column = "value_min"),
        @Result(property = "valueMax", column = "value_max"),
        @Result(property = "valueText", column = "value_text"),
        @Result(property = "ruleType", column = "rule_type"),
        @Result(property = "isBlocking", column = "is_blocking"),
        @Result(property = "isRequired", column = "is_required"),
        @Result(property = "sortOrder", column = "sort_order"),
        @Result(property = "createdAt", column = "created_at")
    })
    List<RgResourceRuleCondition> findConditionsByHeaderId(Integer ruleHeaderId);
    
    /**
     * 查询资源组的结构化规则条件
     */
    @Select("""
        SELECT rc.* FROM rg_resource_rule_condition rc
        JOIN rg_resource_rule_header rh ON rc.rule_header_id = rh.id
        WHERE rh.resource_group_id = #{resourceGroupId}
          AND rh.rule_type = 'STRUCT_RULES'
          AND rh.is_active = true
        ORDER BY rc.sort_order
    """)
    @Results({
        @Result(property = "ruleHeaderId", column = "rule_header_id"),
        @Result(property = "conditionGroup", column = "condition_group"),
        @Result(property = "paramCode", column = "param_code"),
        @Result(property = "paramName", column = "param_name"),
        @Result(property = "valueMin", column = "value_min"),
        @Result(property = "valueMax", column = "value_max"),
        @Result(property = "valueText", column = "value_text"),
        @Result(property = "ruleType", column = "rule_type"),
        @Result(property = "isBlocking", column = "is_blocking"),
        @Result(property = "isRequired", column = "is_required"),
        @Result(property = "sortOrder", column = "sort_order"),
        @Result(property = "createdAt", column = "created_at")
    })
    List<RgResourceRuleCondition> findStructRulesByResourceGroupId(Integer resourceGroupId);
    
    /**
     * 查询资源组的文本规则条件
     */
    @Select("""
        SELECT rc.* FROM rg_resource_rule_condition rc
        JOIN rg_resource_rule_header rh ON rc.rule_header_id = rh.id
        WHERE rh.resource_group_id = #{resourceGroupId}
          AND rh.rule_type = 'TEXT_RULES'
          AND rh.is_active = true
        ORDER BY rc.sort_order
    """)
    @Results({
        @Result(property = "ruleHeaderId", column = "rule_header_id"),
        @Result(property = "conditionGroup", column = "condition_group"),
        @Result(property = "paramCode", column = "param_code"),
        @Result(property = "paramName", column = "param_name"),
        @Result(property = "valueMin", column = "value_min"),
        @Result(property = "valueMax", column = "value_max"),
        @Result(property = "valueText", column = "value_text"),
        @Result(property = "ruleType", column = "rule_type"),
        @Result(property = "isBlocking", column = "is_blocking"),
        @Result(property = "isRequired", column = "is_required"),
        @Result(property = "sortOrder", column = "sort_order"),
        @Result(property = "createdAt", column = "created_at")
    })
    List<RgResourceRuleCondition> findTextRulesByResourceGroupId(Integer resourceGroupId);
    
    /**
     * 获取指定参数类型的所有不同值（用于下拉选项）
     * 从规则条件表中提取 value_text 字段
     */
    @Select("""
        SELECT DISTINCT value_text FROM rg_resource_rule_condition 
        WHERE param_code = #{paramCode} 
          AND value_text IS NOT NULL 
          AND value_text != ''
    """)
    List<String> findDistinctValuesByParamCode(String paramCode);
}

