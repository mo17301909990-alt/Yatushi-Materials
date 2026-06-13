package com.it.yts_project.mapper;

import com.it.yts_project.model.LeoGpNumber;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Leo GP Number Mapper接口
 */
@Mapper
public interface LeoGpNumberMapper {
    
    /**
     * 根据项目ID查询Leo GP Number
     */
    List<LeoGpNumber> findByProjectId(@Param("projectId") Integer projectId);


    /**
     * 插入Leo GP Number
     */
    int insert(LeoGpNumber leoGpNumber);
    
    /**
     * 更新Leo GP Number
     */
    int update(LeoGpNumber leoGpNumber);
    
    /**
     * 根据ID删除Leo GP Number
     */
    int deleteById(@Param("id") Integer id);
    
    /**
     * 根据项目ID删除Leo GP Number
     */
    int deleteByProjectId(@Param("projectId") Integer projectId);
    
    /**
     * 根据产品系列名称获取所有公司编号
     */
    List<String> getCompanyNumbersByProductName(@Param("productName") String productName);
    
    /**
     * 根据产品型号和名称获取公司编号
     */
    List<String> getCompanyNumbersByProductModelNumberAndName(
        @Param("productModelNumber") String productModelNumber,
        @Param("productName") String productName
    );
}
