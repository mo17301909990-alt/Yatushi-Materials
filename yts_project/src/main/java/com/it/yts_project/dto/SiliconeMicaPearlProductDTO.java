package com.it.yts_project.dto;

import com.it.yts_project.model.SiliconeMicaPearlCompatibility;
import com.it.yts_project.model.SiliconeMicaPearlProduct;
import lombok.Data;
import java.util.List;

@Data
public class SiliconeMicaPearlProductDTO {
    private SiliconeMicaPearlProduct product;
    private List<SiliconeMicaPearlCompatibility> compatibilities;
}
