package com.it.yts_project.mapper;

import com.it.yts_project.dto.GoldFoilProductDTO;
import com.it.yts_project.dto.GoldFoilQueryParams;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface GoldFoilProductMapper {

    /**
     * 获取烫金纸供应型号全部数据
     * @return 烫金纸产品列表
     */
    List<GoldFoilProductDTO> getAllGoldFoilProducts();

    /**
     * 通用查询方法：使用 GoldFoilQueryParams 参数进行查询
     * 这个方法支持更灵活的查询，特别是对于产品类型和烫金图案类型的处理
     *
     * @param params 查询参数，包含公司编号、产品类型、烫金图案类型等
     * @return 匹配的烫金纸产品列表
     */
    List<GoldFoilProductDTO> getProducts(@Param("params") GoldFoilQueryParams params);

    /**
     * 获取匹配查询的总记录数（用于分页）
     * 使用相同的查询条件，但不包含LIMIT和OFFSET
     *
     * @param params 查询参数
     * @return 总记录数
     */
    Long countProducts(@Param("params") GoldFoilQueryParams params);

    /**
     * 批量更新产品的usage_count（每次被筛选出来时+1）
     *
     * @param productIds 产品ID列表
     */
    void incrementUsageCountBatch(@Param("productIds") List<Long> productIds);

    /** 获取所有去重后的公司编号（用于下拉建议） */
    List<String> getDistinctCompanyNumbers();

    /** 获取所有去重后的烫金纸型号（用于下拉建议） */
    List<String> getDistinctGpNumbers();
}
