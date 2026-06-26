package com.it.yts_project.service;

import com.it.yts_project.model.AdminOperationLog;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 后台操作日志：记录谁在何时对哪块数据做了何种操作
 */
public interface AdminOperationLogService {

    /**
     * 记录一条操作日志（操作人从 CurrentUserHolder 取，IP/User-Agent 从 request 取）。
     * targetLabel 为操作对象业务描述（如物料名称/编码）；变更详情从 ChangeDetailHolder 读取（如有）。
     */
    void record(HttpServletRequest request, String module, String operationType, String targetType, String targetId, String changeSummary, String targetLabel);

    /**
     * 分页查询，返回 list + total
     */
    Map<String, Object> list(Integer operatorId, String module, String operationType,
                             LocalDateTime from, LocalDateTime to, Integer pageNo, Integer pageSize);
}
