package com.it.yts_project.dto;

import com.it.yts_project.model.SiliconeWhiteUvCompatibility;
import com.it.yts_project.model.SiliconeWhiteUvProduct;
import lombok.Data;
import java.util.List;

@Data
public class SiliconeWhiteUvProductDTO {
    private SiliconeWhiteUvProduct product;
    private List<SiliconeWhiteUvCompatibility> compatibilities;
}
