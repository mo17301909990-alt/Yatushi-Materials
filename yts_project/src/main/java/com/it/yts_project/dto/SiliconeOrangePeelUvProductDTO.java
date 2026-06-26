package com.it.yts_project.dto;

import com.it.yts_project.model.SiliconeOrangePeelUvCompatibility;
import com.it.yts_project.model.SiliconeOrangePeelUvProduct;
import lombok.Data;
import java.util.List;

@Data
public class SiliconeOrangePeelUvProductDTO {
    private SiliconeOrangePeelUvProduct product;
    private List<SiliconeOrangePeelUvCompatibility> compatibilities;
}
