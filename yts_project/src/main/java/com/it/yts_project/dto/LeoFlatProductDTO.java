package com.it.yts_project.dto;

import com.it.yts_project.model.LeoFlatCompatibility;
import com.it.yts_project.model.LeoFlatProduct;
import lombok.Data;
import java.util.List;

@Data
public class LeoFlatProductDTO {
    private LeoFlatProduct product;
    private List<LeoFlatCompatibility> compatibilities;
}
