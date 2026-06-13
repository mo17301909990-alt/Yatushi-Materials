package com.it.yts_project.agent;

import java.util.HashMap;
import java.util.Map;

/**
 * 类型安全的结构化参数字典。
 * <p>替代 {@code Map<String, Object>} 传递查询参数，支持 {@link #from(Map)} 工厂方法和
 * {@link #toMap()} 输出。</p>
 */
public class MatchContext {

    private Integer productTypeId;
    private String paizi;
    private String colorNum;
    private Integer patternId;
    private Integer hotStampingTypeOptionId;
    private Integer clothPaperTypeId;
    private Integer preProcessingStepsOptionId;
    private String companyNumber;
    private String gpNo;
    private String postProcessing;
    private Integer priceLevel;
    private String hotStampingPaperType;

    // ========== 工厂方法 ==========

    /**
     * 从原始 Map 构建 MatchContext，只提取已知字段。
     */
    public static MatchContext from(Map<String, Object> map) {
        if (map == null) return new MatchContext();
        MatchContext ctx = new MatchContext();
        ctx.productTypeId = asInt(map.get("productTypeId"));
        ctx.paizi = asString(map.get("paizi"));
        ctx.colorNum = asString(map.get("colorNum"));
        ctx.patternId = asInt(map.get("patternId"));
        ctx.hotStampingTypeOptionId = asInt(map.get("hotStampingTypeOptionId"));
        ctx.clothPaperTypeId = asInt(map.get("clothPaperTypeId"));
        ctx.preProcessingStepsOptionId = asInt(map.get("preProcessingStepsOptionId"));
        ctx.companyNumber = asString(map.get("companyNumber"));
        ctx.gpNo = asString(map.get("gpNo"));
        ctx.postProcessing = asString(map.get("postProcessing"));
        ctx.priceLevel = asInt(map.get("priceLevel"));
        ctx.hotStampingPaperType = asString(map.get("hotStampingPaperType"));
        return ctx;
    }

    /**
     * 将当前上下文转换为 Map，仅包含非空字段。
     */
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        if (productTypeId != null) map.put("productTypeId", productTypeId);
        if (paizi != null) map.put("paizi", paizi);
        if (colorNum != null) map.put("colorNum", colorNum);
        if (patternId != null) map.put("patternId", patternId);
        if (hotStampingTypeOptionId != null) map.put("hotStampingTypeOptionId", hotStampingTypeOptionId);
        if (clothPaperTypeId != null) map.put("clothPaperTypeId", clothPaperTypeId);
        if (preProcessingStepsOptionId != null) map.put("preProcessingStepsOptionId", preProcessingStepsOptionId);
        if (companyNumber != null) map.put("companyNumber", companyNumber);
        if (gpNo != null) map.put("gpNo", gpNo);
        if (postProcessing != null) map.put("postProcessing", postProcessing);
        if (priceLevel != null) map.put("priceLevel", priceLevel);
        if (hotStampingPaperType != null) map.put("hotStampingPaperType", hotStampingPaperType);
        return map;
    }

    // ========== 辅助转换 ==========

    private static String asString(Object v) {
        if (v == null) return null;
        if (v instanceof String s) return s.isBlank() ? null : s.trim();
        return String.valueOf(v);
    }

    private static Integer asInt(Object v) {
        if (v == null) return null;
        if (v instanceof Number n) return n.intValue();
        try {
            return Integer.parseInt(v.toString().trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    // ========== getters / setters ==========

    public Integer getProductTypeId() { return productTypeId; }
    public void setProductTypeId(Integer productTypeId) { this.productTypeId = productTypeId; }

    public String getPaizi() { return paizi; }
    public void setPaizi(String paizi) { this.paizi = paizi; }

    public String getColorNum() { return colorNum; }
    public void setColorNum(String colorNum) { this.colorNum = colorNum; }

    public Integer getPatternId() { return patternId; }
    public void setPatternId(Integer patternId) { this.patternId = patternId; }

    public Integer getHotStampingTypeOptionId() { return hotStampingTypeOptionId; }
    public void setHotStampingTypeOptionId(Integer hotStampingTypeOptionId) { this.hotStampingTypeOptionId = hotStampingTypeOptionId; }

    public Integer getClothPaperTypeId() { return clothPaperTypeId; }
    public void setClothPaperTypeId(Integer clothPaperTypeId) { this.clothPaperTypeId = clothPaperTypeId; }

    public Integer getPreProcessingStepsOptionId() { return preProcessingStepsOptionId; }
    public void setPreProcessingStepsOptionId(Integer preProcessingStepsOptionId) { this.preProcessingStepsOptionId = preProcessingStepsOptionId; }

    public String getCompanyNumber() { return companyNumber; }
    public void setCompanyNumber(String companyNumber) { this.companyNumber = companyNumber; }

    public String getGpNo() { return gpNo; }
    public void setGpNo(String gpNo) { this.gpNo = gpNo; }

    public String getPostProcessing() { return postProcessing; }
    public void setPostProcessing(String postProcessing) { this.postProcessing = postProcessing; }

    public Integer getPriceLevel() { return priceLevel; }
    public void setPriceLevel(Integer priceLevel) { this.priceLevel = priceLevel; }

    public String getHotStampingPaperType() { return hotStampingPaperType; }
    public void setHotStampingPaperType(String hotStampingPaperType) { this.hotStampingPaperType = hotStampingPaperType; }
}
