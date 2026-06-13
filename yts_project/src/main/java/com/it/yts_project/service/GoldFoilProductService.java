package com.it.yts_project.service;

import com.it.yts_project.dto.GoldFoilProductDTO;
import com.it.yts_project.dto.GoldFoilQueryParams;

import java.util.List;

public interface GoldFoilProductService {

    /**
     * 查询右侧烫金纸供应型号列表
     * @return 烫金纸产品列表
     */
    List<GoldFoilProductDTO> getAllGoldFoilProducts();

    /**
     * 匹配查询：使用 GoldFoilQueryParams 参数进行查询
     * 这个方法支持更灵活的查询，特别是对于产品类型和烫金图案类型的处理
     * 可用于第一次匹配、第二次匹配和未来的第三次匹配
     *
     * @param params 查询参数，包含公司编号、产品类型、烫金图案类型等
     * @return 匹配的烫金纸产品列表
     */
    List<GoldFoilProductDTO> getProducts(GoldFoilQueryParams params);

    /**
     * 获取匹配查询的总记录数（用于分页）
     *
     * @param params 查询参数
     * @return 总记录数
     */
    Long countProducts(GoldFoilQueryParams params);

    /** 获取所有去重后的公司编号 */
    List<String> getDistinctCompanyNumbers();

    /** 获取所有去重后的烫金纸型号 */
    List<String> getDistinctGpNumbers();
}
