package com.it.yts_project.mapper;

import com.it.yts_project.model.CodeMapping;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 物料编码映射 Mapper 接口
 */
@Mapper
public interface CodeMappingMapper {

    /**
     * 获取所有映射
     */
    List<CodeMapping> findAll();

    /**
     * 分页获取映射
     */
    List<CodeMapping> findWithPagination(@Param("offset") int offset, @Param("limit") int limit);

    /**
     * 获取总记录数
     */
    int getTotalCount();

    /**
     * 根据 ID 获取映射
     */
    CodeMapping findById(@Param("id") Integer id);

    /**
     * 插入映射
     */
    int insert(CodeMapping codeMapping);

    /**
     * 更新映射
     */
    int update(CodeMapping codeMapping);

    /**
     * 删除映射
     */
    int deleteById(@Param("id") Integer id);

    /**
     * 批量删除映射
     */
    int batchDeleteByIds(@Param("ids") List<Integer> ids);

    /**
     * 根据 P0 来源查询映射
     */
    List<CodeMapping> findByP0Source(
        @Param("p0TableName") String p0TableName,
        @Param("p0RowId") Integer p0RowId
    );

    /**
     * 根据目标类型和 ID 查询映射
     */
    List<CodeMapping> findByTarget(
        @Param("targetType") String targetType,
        @Param("targetId") Integer targetId
    );

    /**
     * 根据匹配类型查询映射
     */
    List<CodeMapping> findByMatchType(@Param("matchType") String matchType);

    /**
     * 搜索（支持多条件）
     */
    List<CodeMapping> search(
        @Param("p0TableName") String p0TableName,
        @Param("p0MaterialName") String p0MaterialName,
        @Param("targetType") String targetType,
        @Param("matchType") String matchType
    );

    /**
     * 获取所有来源表名列表
     */
    List<String> getAllP0TableNames();
}
