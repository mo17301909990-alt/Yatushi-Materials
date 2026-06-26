package com.it.yts_project.mapper;

import com.it.yts_project.model.SystemContent;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SystemContentMapper {

    SystemContent findByContentKey(@Param("contentKey") String contentKey);

    int upsert(@Param("contentKey") String contentKey,
               @Param("title") String title,
               @Param("bodyHtml") String bodyHtml,
               @Param("updatedBy") Integer updatedBy);
}
