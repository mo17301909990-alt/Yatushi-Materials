package com.it.yts_project.dto;

import com.it.yts_project.model.SiliconeGlowInkCompatibility;
import com.it.yts_project.model.SiliconeGlowInkProduct;
import lombok.Data;
import java.util.List;

@Data
public class SiliconeGlowInkProductDTO {
    private SiliconeGlowInkProduct product;
    private List<SiliconeGlowInkCompatibility> compatibilities;
}
