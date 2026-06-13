package com.it.yts_project.dto;

import java.util.List;
import java.util.Map;

/**
 * Agent 匹配引擎响应 DTO。
 * <p>封装 intent 识别、参数提取、匹配结果和建议。</p>
 */
public class AgentResponse {

    private String intent;
    private Map<String, Object> params;
    private List<Map<String, Object>> results;
    private List<String> suggestions;
    private String reply;

    public AgentResponse() {}

    public AgentResponse(String intent, Map<String, Object> params,
                         List<Map<String, Object>> results,
                         List<String> suggestions, String reply) {
        this.intent = intent;
        this.params = params;
        this.results = results;
        this.suggestions = suggestions;
        this.reply = reply;
    }

    public String getIntent() { return intent; }
    public void setIntent(String intent) { this.intent = intent; }

    public Map<String, Object> getParams() { return params; }
    public void setParams(Map<String, Object> params) { this.params = params; }

    public List<Map<String, Object>> getResults() { return results; }
    public void setResults(List<Map<String, Object>> results) { this.results = results; }

    public List<String> getSuggestions() { return suggestions; }
    public void setSuggestions(List<String> suggestions) { this.suggestions = suggestions; }

    public String getReply() { return reply; }
    public void setReply(String reply) { this.reply = reply; }
}
