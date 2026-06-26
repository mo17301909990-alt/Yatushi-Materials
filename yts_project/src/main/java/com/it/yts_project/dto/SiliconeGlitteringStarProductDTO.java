package com.it.yts_project.dto;

import com.it.yts_project.model.SiliconeGlitteringStarCompatibility;
import com.it.yts_project.model.SiliconeGlitteringStarProduct;
import lombok.Data;
import java.util.List;

@Data
public class SiliconeGlitteringStarProductDTO {
    private SiliconeGlitteringStarProduct product;
    private List<SiliconeGlitteringStarCompatibility> compatibilities;
}
