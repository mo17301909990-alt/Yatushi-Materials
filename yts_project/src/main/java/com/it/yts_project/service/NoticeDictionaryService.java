package com.it.yts_project.service;

import com.it.yts_project.model.NoticeDictionary;

import java.util.List;

/**
 * 注意事项字典表 Service 接口
 */
public interface NoticeDictionaryService {

    /**
     * 根据ID列表查询注意事项
     * @param ids ID列表
     * @return 注意事项列表
     */
    List<NoticeDictionary> findByIds(List<Integer> ids);

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
    NoticeDictionary findById(Integer id);

    /**
     * 根据编码查询注意事项
     * @param noticeCode 编码
     * @return 注意事项
     */
    NoticeDictionary findByCode(String noticeCode);

    /**
     * 根据分类查询注意事项
     * @param category 分类
     * @return 注意事项列表
     */
    List<NoticeDictionary> findByCategory(String category);

    /**
     * 创建注意事项
     * @param notice 注意事项
     * @return 创建的注意事项
     */
    NoticeDictionary create(NoticeDictionary notice);

    /**
     * 更新注意事项
     * @param notice 注意事项
     * @return 更新的注意事项
     */
    NoticeDictionary update(NoticeDictionary notice);

    /**
     * 删除注意事项
     * @param id ID
     * @return 是否成功
     */
    boolean delete(Integer id);
}
