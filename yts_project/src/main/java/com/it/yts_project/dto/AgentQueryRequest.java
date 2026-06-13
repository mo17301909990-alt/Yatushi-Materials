package com.it.yts_project.dto;

/**
 * Agent 查询请求 DTO。
 */
public class AgentQueryRequest {

    private String query;

    public AgentQueryRequest() {}

    public AgentQueryRequest(String query) {
        this.query = query;
    }

    public String getQuery() { return query; }
    public void setQuery(String query) { this.query = query; }
}
