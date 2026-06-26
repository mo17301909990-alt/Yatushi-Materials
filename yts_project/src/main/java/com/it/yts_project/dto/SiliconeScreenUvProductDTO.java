package com.it.yts_project.dto;

import com.it.yts_project.model.SiliconeScreenUvCompatibility;
import com.it.yts_project.model.SiliconeScreenUvProduct;
import lombok.Data;
import java.util.List;

@Data
public class SiliconeScreenUvProductDTO {
    private SiliconeScreenUvProduct product;
    private List<SiliconeScreenUvCompatibility> compatibilities;
}
