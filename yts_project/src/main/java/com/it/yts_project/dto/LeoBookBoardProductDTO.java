package com.it.yts_project.dto;

import com.it.yts_project.model.LeoBookBoardCompatibility;
import com.it.yts_project.model.LeoBookBoardProduct;
import lombok.Data;
import java.util.List;

@Data
public class LeoBookBoardProductDTO {
    private LeoBookBoardProduct product;
    private List<LeoBookBoardCompatibility> compatibilities;
}
