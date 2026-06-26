package com.it.yts_project.dto;

import com.it.yts_project.model.SiliconeWaterBaseTransparentCompatibility;
import com.it.yts_project.model.SiliconeWaterBaseTransparentProduct;
import lombok.Data;
import java.util.List;

@Data
public class SiliconeWaterBaseTransparentProductDTO {
    private SiliconeWaterBaseTransparentProduct product;
    private List<SiliconeWaterBaseTransparentCompatibility> compatibilities;
}
