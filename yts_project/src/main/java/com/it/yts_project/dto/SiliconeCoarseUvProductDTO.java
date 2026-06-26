package com.it.yts_project.dto;

import com.it.yts_project.model.SiliconeCoarseUvCompatibility;
import com.it.yts_project.model.SiliconeCoarseUvProduct;
import lombok.Data;
import java.util.List;

@Data
public class SiliconeCoarseUvProductDTO {
    private SiliconeCoarseUvProduct product;
    private List<SiliconeCoarseUvCompatibility> compatibilities;
}
