package com.it.yts_project.dto;

import com.it.yts_project.model.SiliconeWatercolorInkCompatibility;
import com.it.yts_project.model.SiliconeWatercolorInkProduct;
import lombok.Data;
import java.util.List;

@Data
public class SiliconeWatercolorInkProductDTO {
    private SiliconeWatercolorInkProduct product;
    private List<SiliconeWatercolorInkCompatibility> compatibilities;
}
