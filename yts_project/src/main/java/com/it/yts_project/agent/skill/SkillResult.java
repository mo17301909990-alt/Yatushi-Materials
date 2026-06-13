package com.it.yts_project.agent.skill;

import java.util.List;

/**
 * Skill 执行结果包装。
 *
 * @param <T> 单条数据记录的类型
 */
public class SkillResult<T> {

    /** 是否执行成功 */
    private boolean success;

    /** 返回数据列表 */
    private List<T> data;

    /** 提示消息（错误原因或补充说明） */
    private String message;

    /** 匹配度（0-100），仅对查询类 Skill 有意义 */
    private Integer matchDegree;

    /** 匹配/命中总条数 */
    private Integer totalCount;

    public SkillResult() {
    }

    /**
     * 构造成功结果。
     */
    public static <T> SkillResult<T> success(List<T> data, int totalCount, int matchDegree, String message) {
        SkillResult<T> r = new SkillResult<>();
        r.success = true;
        r.data = data;
        r.totalCount = totalCount;
        r.matchDegree = matchDegree;
        r.message = message;
        return r;
    }

    /**
     * 构造失败结果。
     */
    public static <T> SkillResult<T> failure(String message) {
        SkillResult<T> r = new SkillResult<>();
        r.success = false;
        r.message = message;
        return r;
    }

    // ========== getters / setters ==========

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public List<T> getData() { return data; }
    public void setData(List<T> data) { this.data = data; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public Integer getMatchDegree() { return matchDegree; }
    public void setMatchDegree(Integer matchDegree) { this.matchDegree = matchDegree; }

    public Integer getTotalCount() { return totalCount; }
    public void setTotalCount(Integer totalCount) { this.totalCount = totalCount; }
}
