package com.it.yts_project.mapper;

import com.it.yts_project.model.SysAnnouncement;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysAnnouncementMapper {

    /**
     * 插入消息
     */
    int insert(SysAnnouncement announcement);

    /**
     * 根据ID查询消息
     */
    SysAnnouncement selectById(@Param("id") String id);

    /**
     * 更新消息
     */
    int updateById(SysAnnouncement announcement);

    /**
     * 删除消息
     */
    int deleteById(@Param("id") String id);

    /**
     * 查询消息列表（管理员）
     */
    List<SysAnnouncement> selectList(@Param("title") String title,
                                     @Param("msgCategory") String msgCategory,
                                     @Param("sendStatus") Integer sendStatus,
                                     @Param("offset") Integer offset,
                                     @Param("limit") Integer limit);

    /**
     * 统计消息总数（管理员）
     */
    int countList(@Param("title") String title,
                   @Param("msgCategory") String msgCategory,
                   @Param("sendStatus") Integer sendStatus);

    /**
     * 查询用户消息列表（联表查询）
     */
    List<SysAnnouncement> selectListByUser(@Param("userId") Integer userId,
                                           @Param("msgCategory") String msgCategory,
                                           @Param("readFlag") Integer readFlag,
                                           @Param("startTime") String startTime,
                                           @Param("endTime") String endTime,
                                           @Param("offset") Integer offset,
                                           @Param("limit") Integer limit);

    /**
     * 统计用户消息总数
     */
    int countListByUser(@Param("userId") Integer userId,
                       @Param("msgCategory") String msgCategory,
                       @Param("readFlag") Integer readFlag,
                       @Param("startTime") String startTime,
                       @Param("endTime") String endTime);

    /**
     * 查询所有活跃用户ID（用于发布给全体用户）
     */
    List<Integer> selectAllActiveUserIds();
}

