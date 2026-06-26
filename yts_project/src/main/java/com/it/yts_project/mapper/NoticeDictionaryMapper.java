package com.it.yts_project.mapper;

import com.it.yts_project.model.NoticeDictionary;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 注意事项字典表 Mapper 接口
 */
@Mapper
public interface NoticeDictionaryMapper {

    /**
     * 根据ID列表查询注意事项
     * @param ids ID列表
     * @return 注意事项列表
     */
    List<NoticeDictionary> findByIds(@Param("ids") List<Integer> ids);

    /**
     * 查询所有注意事项
     * @return 注意事项列表
     */
    List<NoticeDictionary> findAll();

    /**
     * 查询所有启用的注意事项
     * @return 注意事项列表
     */
    List<NoticeDictionary> findAllActive();

    /**
     * 根据ID查询注意事项
     * @param id ID
     * @return 注意事项
     */
    NoticeDictionary findById(@Param("id") Integer id);

    /**
     * 根据编码查询注意事项
     * @param noticeCode 编码
     * @return 注意事项
     */
    NoticeDictionary findByCode(@Param("noticeCode") String noticeCode);

    /**
     * 根据分类查询注意事项
     * @param category 分类
     * @return 注意事项列表
     */
    List<NoticeDictionary> findByCategory(@Param("category") String category);

    /**
     * 插入注意事项
     * @param notice 注意事项
     * @return 影响行数
     */
    int insert(NoticeDictionary notice);

    /**
     * 更新注意事项
     * @param notice 注意事项
     * @return 影响行数
     */
    int update(NoticeDictionary notice);

    /**
     * 根据ID删除注意事项
     * @param id ID
     * @return 影响行数
     */
    int deleteById(@Param("id") Integer id);
}
