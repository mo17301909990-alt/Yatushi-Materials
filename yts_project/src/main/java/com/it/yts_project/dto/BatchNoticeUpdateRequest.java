package com.it.yts_project.dto;

import lombok.Data;
import java.util.List;

/**
 * 批量更新注意事项请求DTO
 */
@Data
public class BatchNoticeUpdateRequest {
    /**
     * 要更新的规则ID列表
     */
    private List<Integer> ids;

    /**
     * 要设置的注意事项ID列表
     */
    private List<Integer> noticeIds;

    /**
     * 操作模式：append(追加), replace(替换), clear(清除)
     */
    private String mode;
}
