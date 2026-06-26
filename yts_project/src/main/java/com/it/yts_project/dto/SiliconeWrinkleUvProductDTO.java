package com.it.yts_project.dto;

import com.it.yts_project.model.SiliconeWrinkleUvCompatibility;
import com.it.yts_project.model.SiliconeWrinkleUvProduct;
import lombok.Data;
import java.util.List;

@Data
public class SiliconeWrinkleUvProductDTO {
    private SiliconeWrinkleUvProduct product;
    private List<SiliconeWrinkleUvCompatibility> compatibilities;
}
