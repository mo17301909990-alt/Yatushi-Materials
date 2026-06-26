package com.it.yts_project.mapper;

import com.it.yts_project.model.Products;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 产品Mapper接口
 */
@Mapper
public interface ProductsMapper {
    
    /**
     * 获取所有去重的纸张类型
     * @return 纸张类型列表
     */
    List<String> getAllDistinctPaperTypes();

    /**
     * 获取所有产品系列名称
     * @return 产品系列名称列表
     */
    List<String> getAllSeriesNames();
    
    /**
     * 根据产品系列名称获取纸张类型
     * @param seriesName 产品系列名称
     * @return 纸张类型列表
     */
    List<String> getPaperTypesBySeriesName(@Param("seriesName") String seriesName);
    
    /**
     * 根据产品系列名称获取纸张类型（备选方法，使用products表）
     * @param seriesName 产品系列名称
     * @return 纸张类型列表
     */
    List<String> getPaperTypesBySeriesNameFallback(@Param("seriesName") String seriesName);
    
    /**
     * 根据ID获取产品
     * @param id 产品ID
     * @return 产品信息
     */
    Products getProductById(@Param("id") Integer id);
    
    /**
     * 根据产品系列名称获取产品列表
     * @param seriesName 产品系列名称
     * @return 产品列表
     */
    List<Products> getProductsBySeriesName(@Param("seriesName") String seriesName);
    
    /**
     * 根据烫金纸性能类型获取支持的产品系列名称列表
     * @param paperType 烫金纸性能类型
     * @return 产品系列名称列表
     */
    List<String> getSeriesNamesByPaperType(@Param("paperType") String paperType);
    
    /**
     * 检查产品系列是否支持指定的烫金纸性能类型
     * @param seriesName 产品系列名称
     * @param paperType 烫金纸性能类型
     * @return 如果支持返回true，否则返回false
     */
    boolean checkSeriesSupportsPaperType(@Param("seriesName") String seriesName, @Param("paperType") String paperType);
    
    /**
     * 根据公司编号和烫金纸性能类型获取产品系列名称列表
     * @param companyNumber 公司编号
     * @param paperType 烫金纸性能类型
     * @return 产品系列名称列表
     */
    List<String> getSeriesNamesByCompanyNumberAndPaperType(
        @Param("companyNumber") String companyNumber, 
        @Param("paperType") String paperType
    );
    
    /**
     * 根据公司编号、烫金纸性能类型和产品系列名称获取产品列表
     * @param companyNumber 公司编号
     * @param paperType 烫金纸性能类型
     * @param seriesName 产品系列名称
     * @return 产品列表
     */
    List<Products> getProductsByCompanyNumberAndPaperTypeAndSeries(
        @Param("companyNumber") String companyNumber,
        @Param("paperType") String paperType,
        @Param("seriesName") String seriesName
    );
}
