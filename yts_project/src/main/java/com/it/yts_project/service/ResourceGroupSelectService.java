package com.it.yts_project.service;

import com.it.yts_project.dto.ResourceGroupSelectRequest;
import com.it.yts_project.dto.ResourceGroupSelectResponse;
import com.it.yts_project.model.RgResourceGroup;
import com.it.yts_project.model.RgTaskDefinition;
import com.it.yts_project.model.RgWorkCenter;
import java.util.List;
import java.util.Map;

/**
 * 资源组选择服务接口
 */
public interface ResourceGroupSelectService {
    
    /**
     * 获取所有任务定义
     */
    List<RgTaskDefinition> getAllTasks();
    
    /**
     * 获取任务需要的参数
     * 返回 requiresSize, requiresSheetCount 等布尔值
     */
    Map<String, Boolean> getTaskRequiredParams(String taskCode);
    
    /**
     * 根据条件筛选资源组
     */
    ResourceGroupSelectResponse selectResourceGroups(ResourceGroupSelectRequest request);
    
    /**
     * 获取工作中心详情及该工作中心下的所有资源组
     */
    Map<String, Object> getWorkCenterDetail(String workCenterCode);
    
    /**
     * 获取公共参数的下拉选项（从规则中推断）
     * 返回 departments, productTypes, surfaceTypes, grainDirections 等列表
     */
    Map<String, List<String>> getParamOptions();
}

