package com.it.yts_project.dto;

import com.it.yts_project.model.NoticeDictionary;
import lombok.Data;

import java.util.List;

/**
 * 匹配结果（包含注意事项）
 * 用于在匹配接口中返回产品列表和相关的注意事项
 * @param <T> 产品数据类型
 */
@Data
public class MatchResultWithNotices<T> {
    
    /**
     * 产品分页结果
     */
    private PagedResult<T> products;
    
    /**
     * 注意事项列表（匹配规则相关的注意事项并集）
     */
    private List<NoticeDictionary> notices;
    
    public MatchResultWithNotices() {
    }
    
    public MatchResultWithNotices(PagedResult<T> products, List<NoticeDictionary> notices) {
        this.products = products;
        this.notices = notices;
    }
}
