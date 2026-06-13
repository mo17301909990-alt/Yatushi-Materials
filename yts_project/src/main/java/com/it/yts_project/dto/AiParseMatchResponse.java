package com.it.yts_project.dto;

import lombok.Data;
import java.util.Map;

@Data
public class AiParseMatchResponse {
    private PagedResult<GoldFoilProductDTO> products;
    private java.util.List<com.it.yts_project.model.NoticeDictionary> notices;
    private Map<String, Object> params;

    public AiParseMatchResponse() {}

    public AiParseMatchResponse(PagedResult<GoldFoilProductDTO> products,
                                java.util.List<com.it.yts_project.model.NoticeDictionary> notices,
                                Map<String, Object> params) {
        this.products = products;
        this.notices = notices;
        this.params = params;
    }
}
