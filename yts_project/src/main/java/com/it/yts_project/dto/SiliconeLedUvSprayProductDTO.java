package com.it.yts_project.dto;

import com.it.yts_project.model.SiliconeLedUvSprayCompatibility;
import com.it.yts_project.model.SiliconeLedUvSprayProduct;
import lombok.Data;
import java.util.List;

@Data
public class SiliconeLedUvSprayProductDTO {
    private SiliconeLedUvSprayProduct product;
    private List<SiliconeLedUvSprayCompatibility> compatibilities;
}
