package com.it.yts_project.mapper;

import com.it.yts_project.model.PostProcessingPrint;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 印刷后加工Mapper接口
 */
@Mapper
public interface PostProcessingPrintMapper {
    
    /**
     * 获取所有印刷配置
     */
    List<PostProcessingPrint> findAll();
    
    /**
     * 分页获取印刷配置
     */
    List<PostProcessingPrint> findWithPagination(@Param("offset") int offset, @Param("limit") int limit);
    
    /**
     * 获取总记录数
     */
    int getTotalCount();
    
    /**
     * 根据ID获取印刷配置
     */
    PostProcessingPrint findById(@Param("id") Integer id);
    
    /**
     * 插入印刷配置
     */
    int insert(PostProcessingPrint postProcessingPrint);
    
    /**
     * 更新印刷配置
     */
    int update(PostProcessingPrint postProcessingPrint);
    
    /**
     * 删除印刷配置
     */
    int deleteById(@Param("id") Integer id);
    
    /**
     * 批量删除印刷配置
     */
    int batchDeleteByIds(@Param("ids") List<Integer> ids);
    
    /**
     * 根据燙金紙系列搜索
     */
    List<PostProcessingPrint> findByProductName(@Param("productName") String productName);
    
    /**
     * 根据产品型号搜索
     */
    List<PostProcessingPrint> findByProductModelNumber(@Param("productModelNumber") String productModelNumber);
    
    /**
     * 根据颜色搜索
     */
    List<PostProcessingPrint> findByColor(@Param("color") String color);
    
    /**
     * 根据布面纸类型ID搜索
     */
    List<PostProcessingPrint> findByClothPaperTypeId(@Param("clothPaperTypeId") Integer clothPaperTypeId);
    
    /**
     * 获取所有可用的燙金紙系列
     */
    List<String> getAllProductNames();
    
    /**
     * 获取所有可用的产品型号
     */
    List<String> getAllProductModelNumbers();
    
    /**
     * 获取所有可用的颜色
     */
    List<String> getAllColors();
    
    /**
     * 根据产品名称和型号获取颜色选项
     */
    List<String> getColorsByProductAndModel(@Param("productName") String productName, @Param("productModelNumber") String productModelNumber);
    
    /**
     * 根据产品名称获取颜色选项
     */
    List<String> getColorsByProductName(@Param("productName") String productName);
    
    /**
     * 根据产品型号获取颜色选项
     */
    List<String> getColorsByProductModelNumber(@Param("productModelNumber") String productModelNumber);
    
    /**
     * 根据唯一键查找记录
     * 唯一键：productName + productModelNumber + color + clothPaperTypeId + paperType
     */
    PostProcessingPrint findByUniqueKey(
        @Param("productName") String productName,
        @Param("productModelNumber") String productModelNumber,
        @Param("color") String color,
        @Param("clothPaperTypeId") Integer clothPaperTypeId,
        @Param("paperType") String paperType
    );
    
    /**
     * 批量更新兼容性状态
     */
    int batchUpdateStatus(@Param("ids") List<Integer> ids, @Param("compatibilityStatus") String compatibilityStatus);
    
    /**
     * 验证颜色是否存在且属于指定的产品
     */
    boolean validateColor(
        @Param("color") String color,
        @Param("productName") String productName,
        @Param("productModelNumber") String productModelNumber
    );

    /**
     * 更新注意事项ID列表
     */
    void updateNoticeIds(@Param("id") Integer id, @Param("noticeIds") List<Integer> noticeIds);
}