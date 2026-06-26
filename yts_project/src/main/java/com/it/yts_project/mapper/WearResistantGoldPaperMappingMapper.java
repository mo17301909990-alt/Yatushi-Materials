package com.it.yts_project.mapper;

import com.it.yts_project.model.WearResistantGoldPaperMapping;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 耐磨金纸映射Mapper接口
 */
@Mapper
public interface WearResistantGoldPaperMappingMapper {
    
    /**
     * 获取所有映射
     */
    List<WearResistantGoldPaperMapping> findAll();
    
    /**
     * 分页获取映射
     */
    List<WearResistantGoldPaperMapping> findWithPagination(@Param("offset") int offset, @Param("limit") int limit);
    
    /**
     * 获取总记录数
     */
    int getTotalCount();
    
    /**
     * 根据ID获取映射
     */
    WearResistantGoldPaperMapping findById(@Param("id") Integer id);
    
    /**
     * 插入映射
     */
    int insert(WearResistantGoldPaperMapping mapping);
    
    /**
     * 更新映射
     */
    int update(WearResistantGoldPaperMapping mapping);
    
    /**
     * 删除映射
     */
    int deleteById(@Param("id") Integer id);
    
    /**
     * 批量删除映射
     */
    int batchDeleteByIds(@Param("ids") List<Integer> ids);
    
    /**
     * 根据燙金紙系列搜索
     */
    List<WearResistantGoldPaperMapping> findByProductName(@Param("productName") String productName);
    
    /**
     * 根据产品型号搜索
     */
    List<WearResistantGoldPaperMapping> findByProductModelNumber(@Param("productModelNumber") String productModelNumber);
    
    /**
     * 根据耐磨金纸类型搜索
     */
    List<WearResistantGoldPaperMapping> findByGoldPaperType(@Param("goldPaperType") String goldPaperType);
    
    /**
     * 根据唯一键查找记录
     * 唯一键：productName + productModelNumber + goldPaperType
     * 注意：productModelNumber 可以为空，空字符串应视为 NULL
     */
    WearResistantGoldPaperMapping findByUniqueKey(
        @Param("productName") String productName,
        @Param("productModelNumber") String productModelNumber,
        @Param("goldPaperType") String goldPaperType
    );
    
    /**
     * 批量更新兼容性状态
     */
    int batchUpdateStatus(@Param("ids") List<Integer> ids, @Param("compatibilityStatus") String compatibilityStatus);
    
    /**
     * 搜索（支持多条件）
     */
    List<WearResistantGoldPaperMapping> search(
        @Param("productName") String productName,
        @Param("productModelNumber") String productModelNumber,
        @Param("goldPaperType") String goldPaperType
    );
    
    /**
     * 获取所有可用的耐磨金纸类型
     */
    List<String> getAllGoldPaperTypes();

    /**
     * 更新注意事项ID列表
     */
    void updateNoticeIds(@Param("id") Integer id, @Param("noticeIds") List<Integer> noticeIds);
}


