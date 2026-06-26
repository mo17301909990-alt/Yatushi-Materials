package com.it.yts_project.service.Impl;

import com.it.yts_project.mapper.AdminOperationLogMapper;
import com.it.yts_project.model.AdminOperationLog;
import com.it.yts_project.service.AdminOperationLogService;
import com.it.yts_project.util.ChangeDetailHolder;
import com.it.yts_project.util.CurrentUserHolder;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class AdminOperationLogServiceImpl implements AdminOperationLogService {

    @Autowired
    private AdminOperationLogMapper adminOperationLogMapper;

    @Override
    public void record(HttpServletRequest request, String module, String operationType, String targetType, String targetId, String changeSummary, String targetLabel) {
        try {
            Integer operatorId = CurrentUserHolder.getOperatorId();
            String operatorName = CurrentUserHolder.getOperatorName();
            if (operatorName == null && request != null && request.getHeader("X-User-Name") != null) {
                operatorName = request.getHeader("X-User-Name").trim();
            }
            String ip = getClientIp(request);
            String userAgent = request != null ? request.getHeader("User-Agent") : null;
            if (userAgent != null && userAgent.length() > 500) {
                userAgent = userAgent.substring(0, 500);
            }
            String changeDetail = ChangeDetailHolder.getAndClear();
            if (changeDetail != null && changeDetail.length() > 2000) {
                changeDetail = changeDetail.substring(0, 2000);
            }
            AdminOperationLog logEntity = AdminOperationLog.builder()
                    .operatorId(operatorId)
                    .operatorName(operatorName)
                    .operatedAt(LocalDateTime.now())
                    .operationType(operationType)
                    .module(module)
                    .targetType(targetType)
                    .targetId(targetId)
                    .targetLabel(trimTo(targetLabel, 512))
                    .changeSummary(trimTo(changeSummary, 500))
                    .changeDetail(changeDetail)
                    .ip(ip != null && ip.length() > 64 ? ip.substring(0, 64) : ip)
                    .userAgent(userAgent)
                    .build();
            adminOperationLogMapper.insert(logEntity);
        } catch (Exception e) {
            log.warn("Failed to record operation log: {}", e.getMessage());
        }
    }

    private static String trimTo(String s, int maxLen) {
        if (s == null) return null;
        return s.length() > maxLen ? s.substring(0, maxLen) : s;
    }

    private static String getClientIp(HttpServletRequest request) {
        if (request == null) return null;
        String xff = request.getHeader("X-Forwarded-For");
        if (xff != null && !xff.isBlank()) {
            int comma = xff.indexOf(',');
            return comma > 0 ? xff.substring(0, comma).trim() : xff.trim();
        }
        return request.getRemoteAddr();
    }

    @Override
    public Map<String, Object> list(Integer operatorId, String module, String operationType,
                                    LocalDateTime from, LocalDateTime to, Integer pageNo, Integer pageSize) {
        int size = pageSize != null && pageSize > 0 ? pageSize : 20;
        int no = pageNo != null && pageNo > 0 ? pageNo : 1;
        int offset = (no - 1) * size;
        List<AdminOperationLog> list = adminOperationLogMapper.list(operatorId, module, operationType, from, to, offset, size);
        long total = adminOperationLogMapper.count(operatorId, module, operationType, from, to);
        Map<String, Object> result = new HashMap<>();
        result.put("list", list);
        result.put("total", total);
        result.put("pageNo", no);
        result.put("pageSize", size);
        result.put("totalPages", (int) Math.ceil((double) total / size));
        return result;
    }
}
