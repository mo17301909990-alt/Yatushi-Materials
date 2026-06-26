package com.it.yts_project.mapper;

import com.it.yts_project.model.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 产品信息Mapper接口
 */
@Mapper
public interface ProductMapper {
    
    /**
     * 查询所有产品
     */
    List<Product> findAll();

    /** 產品總數（燙金物料 Hub 統計等，避免拉全表） */
    long countAll();
    
    /**
     * 根据ID查询产品
     */
    Product findById(@Param("id") Integer id);
    
    /**
     * 根据燙金紙系列查询
     */
    List<Product> findByName(@Param("name") String name);
    
    /**
     * 根据型号查询
     */
    List<Product> findByModelNumber(@Param("modelNumber") String modelNumber);
    List<Product> findByModelNumberAndName(@Param("modelNumber") String modelNumber, @Param("name") String name);
    List<Product> findByModelNumberAndNameAndPaperType(@Param("modelNumber") String modelNumber, @Param("name") String name, @Param("paperType") String paperType);
    /**
     * 根据物料编号查询
     */
    List<Product> findByMaterialNumber(@Param("materialNumber") String materialNumber);
    
    /**
     * 根据烫金纸张类型查询
     */
    List<Product> findByHotStampingPaperType(@Param("hotStampingPaperType") String hotStampingPaperType);
    
    /**
     * 搜索产品
     */
    List<Product> searchProducts(@Param("keyword") String keyword);
    
    /**
     * 插入产品
     */
    int insert(Product product);
    
    /**
     * 更新产品
     */
    int update(Product product);
    
    /**
     * 删除产品
     */
    int deleteById(@Param("id") Integer id);
    
    /**
     * 获取所有烫金纸张类型选项
     */
    List<String> getHotStampingPaperTypeOptions();
    
    /**
     * 获取所有单位选项
     */
    List<String> getDanweiOptions();
    
    /**
     * 获取所有牌子选项
     */
    List<String> getPaiziOptions();
    
    /**
     * 获取所有封度选项
     */
    List<String> getFengduOptions();
    
    /**
     * 获取所有产品名称
     */
    List<String> getAllProductNames();
}
