package com.it.yts_project.service;

import com.it.yts_project.dto.PagedResult;
import com.it.yts_project.dto.SiliconeProductDTO;

import java.util.List;
import java.util.Map;

/**
 * 硅胶组合匹配 Service 接口
 */
public interface SiliconeService {

    /**
     * 匹配查询：关键词搜索 + 多类型 UNION ALL + 工序筛选 + 分页
     *
     * @param types   硅胶类型列表（如 white_uv, glow_ink）
     * @param keyword 搜索关键词
     * @param steps   后加工工序步骤列表（多步骤 INTERSECT）
     * @param page    当前页码
     * @param size    每页条数
     * @return 分页结果
     */
    PagedResult<SiliconeProductDTO> match(List<String> types, String keyword, List<String> steps, int page, int size);

    /**
     * 获取后加工工序步骤（两级结构）
     *
     * @param types 硅胶类型列表
     * @return 按大类分组的步骤列表
     */
    List<Map<String, Object>> getSteps(List<String> types);

    /**
     * 获取产品详情（含兼容性列表）
     *
     * @param id   产品 ID
     * @param type 硅胶类型
     * @return 产品详情 DTO
     */
    SiliconeProductDTO getProductDetail(Integer id, String type);
}
