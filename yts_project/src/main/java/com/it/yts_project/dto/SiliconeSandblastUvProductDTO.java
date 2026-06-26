package com.it.yts_project.dto;

import com.it.yts_project.model.SiliconeSandblastUvCompatibility;
import com.it.yts_project.model.SiliconeSandblastUvProduct;
import lombok.Data;
import java.util.List;

@Data
public class SiliconeSandblastUvProductDTO {
    private SiliconeSandblastUvProduct product;
    private List<SiliconeSandblastUvCompatibility> compatibilities;
}
