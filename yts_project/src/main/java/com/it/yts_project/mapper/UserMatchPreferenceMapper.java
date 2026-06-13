package com.it.yts_project.mapper;

import com.it.yts_project.model.UserMatchPreference;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户匹配偏好数据访问层接口
 */
@Mapper
public interface UserMatchPreferenceMapper {
    
    /**
     * 根据用户ID查询所有匹配偏好
     * @param userId 用户ID
     * @return 用户的所有匹配偏好列表
     */
    List<UserMatchPreference> findByUserId(@Param("userId") Integer userId);
    
    /**
     * 插入单个匹配偏好
     * @param preference 匹配偏好对象
     * @return 影响的行数
     */
    int insert(UserMatchPreference preference);
    
    /**
     * 批量插入匹配偏好
     * @param preferences 匹配偏好列表
     * @return 影响的行数
     */
    int batchInsert(@Param("preferences") List<UserMatchPreference> preferences);
    
    /**
     * 根据用户ID删除所有匹配偏好
     * @param userId 用户ID
     * @return 影响的行数
     */
    int deleteByUserId(@Param("userId") Integer userId);
    
    /**
     * 根据ID删除单个匹配偏好
     * @param id 匹配偏好ID
     * @return 影响的行数
     */
    int deleteById(@Param("id") Integer id);
    
    /**
     * 根据用户ID和字段名查询匹配偏好
     * @param userId 用户ID
     * @param fieldName 字段名
     * @return 匹配偏好列表
     */
    List<UserMatchPreference> findByUserIdAndFieldName(@Param("userId") Integer userId, @Param("fieldName") String fieldName);
}
