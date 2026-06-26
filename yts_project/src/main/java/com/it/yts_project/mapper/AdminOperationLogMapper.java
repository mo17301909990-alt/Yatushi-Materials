package com.it.yts_project.mapper;

import com.it.yts_project.model.AdminOperationLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface AdminOperationLogMapper {

    int insert(AdminOperationLog log);

    List<AdminOperationLog> list(
            @Param("operatorId") Integer operatorId,
            @Param("module") String module,
            @Param("operationType") String operationType,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to,
            @Param("offset") Integer offset,
            @Param("limit") Integer limit
    );

    long count(
            @Param("operatorId") Integer operatorId,
            @Param("module") String module,
            @Param("operationType") String operationType,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to
    );
}
