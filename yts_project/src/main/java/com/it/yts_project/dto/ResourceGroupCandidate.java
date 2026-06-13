package com.it.yts_project.dto;

import com.it.yts_project.model.RgResourceGroupCapacity;
import com.it.yts_project.model.RgResourceGroupDetail;
import com.it.yts_project.model.RgWorkCenter;
import lombok.Data;
import java.util.List;

/**
 * 资源组候选DTO
 */
@Data
public class ResourceGroupCandidate {
    private Integer id;
    private String resourceGroupCode;
    private String name;
    private String workCenterCode;
    private String workCenterName;
    private String family;
    
    /**
     * 匹配状态：PASS / FAIL / UNKNOWN
     */
    private String matchStatus;
    
    /**
     * 匹配详情列表
     */
    private List<MatchDetail> matchDetails;
    
    /**
     * 可上机规则原文
     */
    private String ruleTextAllow;
    
    /**
     * 暂不上机原因原文
     */
    private String ruleTextBlock;
    
    // ========== 关联详情信息 ==========
    
    /**
     * 工作中心详情
     */
    private RgWorkCenter workCenterDetail;
    
    /**
     * 资源组技术参数详情
     */
    private RgResourceGroupDetail techDetail;
    
    /**
     * 资源组产能列表
     */
    private List<RgResourceGroupCapacity> capacityList;
    
    /**
     * 匹配详情
     */
    @Data
    public static class MatchDetail {
        /**
         * 参数名称
         */
        private String paramName;
        
        /**
         * 参数编码
         */
        private String paramCode;
        
        /**
         * 用户输入值
         */
        private String inputValue;
        
        /**
         * 规则要求值
         */
        private String ruleValue;
        
        /**
         * 匹配状态：PASS / FAIL / UNKNOWN
         */
        private String status;
    }
}
