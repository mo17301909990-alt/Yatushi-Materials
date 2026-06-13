package com.it.yts_project.mapper;

import com.it.yts_project.model.CompleteProductInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 完整产品信息Mapper
 */
@Mapper
public interface CompleteProductInfoMapper {
    
    /**
     * 获取所有完整产品信息
     */
    List<CompleteProductInfo> findAllCompleteProductInfo();
    
    /**
     * 根据产品ID获取完整产品信息
     */
    CompleteProductInfo findCompleteProductInfoById(@Param("id") Integer id);
    
    /**
     * 根据物料编号获取完整产品信息
     */
    List<CompleteProductInfo> findCompleteProductInfoByMaterialNumber(@Param("materialNumber") String materialNumber);
    
    /**
     * 根据型号获取完整产品信息
     */
    List<CompleteProductInfo> findCompleteProductInfoByModelNumber(@Param("modelNumber") String modelNumber);
    
    /**
     * 根据Leo Touch编号获取完整产品信息
     */
    List<CompleteProductInfo> findCompleteProductInfoByCompanyNumber(@Param("companyNumber") String companyNumber);
    
    /**
     * 根据SPNO获取完整产品信息
     */
    List<CompleteProductInfo> findCompleteProductInfoByGpNo(@Param("gpNo") String gpNo);
    
    /**
     * 根据烫金纸张类型获取完整产品信息
     */
    List<CompleteProductInfo> findCompleteProductInfoByHotStampingPaperType(@Param("hotStampingPaperType") String hotStampingPaperType);
}
