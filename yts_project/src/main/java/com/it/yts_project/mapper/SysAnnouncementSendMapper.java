package com.it.yts_project.mapper;

import com.it.yts_project.model.SysAnnouncementSend;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysAnnouncementSendMapper {

    /**
     * 插入用户消息关联
     */
    int insert(SysAnnouncementSend send);

    /**
     * 批量插入用户消息关联
     */
    int batchInsert(@Param("list") List<SysAnnouncementSend> list);

    /**
     * 根据消息ID和用户ID查询
     */
    SysAnnouncementSend selectByAnntIdAndUserId(@Param("anntId") String anntId,
                                                  @Param("userId") Integer userId);

    /**
     * 标记为已读
     */
    int markAsRead(@Param("anntId") String anntId, @Param("userId") Integer userId);

    /**
     * 批量标记为已读
     */
    int batchMarkAsRead(@Param("anntIds") List<String> anntIds, @Param("userId") Integer userId);

    /**
     * 删除用户消息关联
     */
    int deleteByAnntIdAndUserId(@Param("anntId") String anntId, @Param("userId") Integer userId);

    /**
     * 统计用户未读消息数
     */
    int countUnreadByUserId(@Param("userId") Integer userId);

    /**
     * 根据消息ID删除所有关联记录
     */
    int deleteByAnntId(@Param("anntId") String anntId);
}

