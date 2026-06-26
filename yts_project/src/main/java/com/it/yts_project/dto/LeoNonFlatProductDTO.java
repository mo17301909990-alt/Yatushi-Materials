package com.it.yts_project.dto;

import com.it.yts_project.model.LeoNonFlatCompatibility;
import com.it.yts_project.model.LeoNonFlatProduct;
import lombok.Data;
import java.util.List;

@Data
public class LeoNonFlatProductDTO {
    private LeoNonFlatProduct product;
    private List<LeoNonFlatCompatibility> compatibilities;
}
