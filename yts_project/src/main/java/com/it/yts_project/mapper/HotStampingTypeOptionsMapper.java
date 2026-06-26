package com.it.yts_project.mapper;

import com.it.yts_project.model.HotStampingTypeOptions;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface HotStampingTypeOptionsMapper {

    /**
     * 获取所有激活的烫金类型选项
     */
    @Select("SELECT * FROM hot_stamping_type_options WHERE is_active = true ORDER BY sort_order, id")
    List<HotStampingTypeOptions> getActiveTypes();

    /**
     * 获取所有烫金类型选项
     */
    @Select("SELECT * FROM hot_stamping_type_options ORDER BY sort_order, id")
    List<HotStampingTypeOptions> getAllTypes();

    /**
     * 根据ID获取烫金类型选项
     */
    @Select("SELECT * FROM hot_stamping_type_options WHERE id = #{id}")
    HotStampingTypeOptions getTypeById(Long id);

    /**
     * 更新注意事项ID列表
     */
    void updateNoticeIds(@Param("id") Integer id, @Param("noticeIds") List<Integer> noticeIds);

    /**
     * 插入烫金类型选项
     * @param type 烫金类型选项
     * @return 影响行数
     */
    int insert(HotStampingTypeOptions type);

    /**
     * 更新烫金类型选项
     * @param type 烫金类型选项
     * @return 影响行数
     */
    int update(HotStampingTypeOptions type);

    /**
     * 根据ID删除烫金类型选项
     * @param id 主键ID
     * @return 影响行数
     */
    int deleteById(@Param("id") Long id);
}
