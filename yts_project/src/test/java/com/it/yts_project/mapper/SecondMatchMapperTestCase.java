package com.it.yts_project.mapper;

import com.it.yts_project.config.AbstractIntegrationTest;
import com.it.yts_project.dto.GoldFoilProductDTO;
import com.it.yts_project.dto.GoldFoilQueryParams;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Disabled("需要真实 PostgreSQL，仅集成测试时启用")
@SpringBootTest
@ActiveProfiles("test")
public class SecondMatchMapperTestCase extends AbstractIntegrationTest {


    @Autowired
    private  GoldFoilProductMapper goldFoilProductMapper;


    @Test
    void shouldGetProductsToFirstMatch() {
        // -------------------- 第一次匹配  --------------------------
        // 验证第一次匹配 (前端直接传值：companyNumber = "HS#1S")
        GoldFoilQueryParams params = new GoldFoilQueryParams();
        params.setCompanyNumber("HS#1S");
        final List<GoldFoilProductDTO> products = goldFoilProductMapper.getProducts(params);
        System.out.println(products);


        // -------------------- 第二次匹配  --------------------------
        // 假设 companyNumber 和  hotStampingPaperTypeChoose 不为空
        // 前端传值：
        final String companyNumber = "HS#1S";
        final List<String> hotStampingPaperTypeChoose = List.of("3.3 包裝", "4.1");

        // 后端二次解析值
        params = new GoldFoilQueryParams();
        params.setCompanyNumber(companyNumber);
        params.setHotStampingPaperTypes(applyRules(hotStampingPaperTypeChoose));
        final List<GoldFoilProductDTO> secondMatchProducts = goldFoilProductMapper.getProducts(params);
        System.out.println(secondMatchProducts);
    }

    // List<String> hotStampingPaperTypeChoose = []
    // hotStampingPaperTypes
    protected List<String> applyRules(List<String> values) {
        List<String> result = new ArrayList<>();

        if (values.contains("3.1 精平裝/咭書")) {
            result.addAll(Arrays.asList("普通金纸", "普通耐磨", "高耐磨"));
        }
        if (values.contains("3.2 賀咭/紙袋") || values.stream().anyMatch(v -> v.contains("4.2") || v.contains("4.3")
                || v.contains("4.4")|| v.contains("4.6"))) {
            result.addAll(Arrays.asList("普通金纸", "普通耐磨"));
        }
        if (values.contains("3.3 包裝") || values.stream().anyMatch(v -> v.contains("4.8")) || values.contains("4.11")) {
            result.add("高耐磨");
        }
        if (values.stream().anyMatch(v ->v.contains("4.5"))){
            result.addAll(Arrays.asList("普通金纸","高耐磨"));
        }
        if (values.stream().anyMatch(v ->v.contains("4.8"))){
            result.addAll(Arrays.asList("普通耐磨","高耐磨"));
        }
        if (values.stream().anyMatch(v -> v.contains("4.7")|| v.contains("4.9") || v.contains("4.10"))) {
            result.add("普通耐磨");
        }
        if (values.stream().anyMatch(v -> v.contains("4.1"))) {
            result.add("普通金纸");
        }
        return result.isEmpty() ? null : new ArrayList<>(result); // 如果结果为空，返回 null，否则返回新的列表
    }

    // 第一次匹配最终SQL：
    /*
    SELECT
    p.name,
    p.model_number,
    p.material_number,
    s.color,
    s.size,
    s.tightness,
    s.temperature_range,
    s.performance,
    g.flat_hot_stamping,
    g.embossed_gold_stamping,
    g.refractive_holographic_patterned_textured_hot_stamping,
    g.post_hot_stamping_embossing_debossing,
    pr.price,
    l.company_number,
    l.gp_no,
    pi.uv_printing,
    pi.silk_screen_led_uv_sparkle_powder,
    pi.printing
FROM
    products p
LEFT JOIN
    specifications s ON p.id = s.project_id
LEFT JOIN
    gold_foil_type g ON p.id = g.project_id
LEFT JOIN
    pricing pr ON p.id = pr.project_id
LEFT JOIN
    leo_gp_numbers l ON p.id = l.project_id
LEFT JOIN
    processing_after_ironing pi ON p.id = pi.project_id
LEFT JOIN
    oil_and_uv_types o ON p.id = o.project_id
LEFT JOIN
    matte_lamination m ON p.id = m.project_id
LEFT JOIN
    film_butter_paper f ON p.id = f.project_id
LEFT JOIN
    cloth_paper c ON p.id = c.project_id
LEFT JOIN
    pattern pt ON p.id = pt.project_id
WHERE
    l.company_number = 'HS#1S'
     */


    // 第二次匹配最终SQL：
    /**
     SELECT
     p.name,
     p.model_number,
     p.material_number,
     s.color,
     s.size,
     s.tightness,
     s.temperature_range,
     s.performance,
     g.flat_hot_stamping,
     g.embossed_gold_stamping,
     g.refractive_holographic_patterned_textured_hot_stamping,
     g.post_hot_stamping_embossing_debossing,
     pr.price,
     l.company_number,
     l.gp_no,
     pi.uv_printing,
     pi.silk_screen_led_uv_sparkle_powder,
     pi.printing
     FROM
     products p
     LEFT JOIN
     specifications s ON p.id = s.project_id
     LEFT JOIN
     gold_foil_type g ON p.id = g.project_id
     LEFT JOIN
     pricing pr ON p.id = pr.project_id
     LEFT JOIN
     leo_gp_numbers l ON p.id = l.project_id
     LEFT JOIN
     processing_after_ironing pi ON p.id = pi.project_id
     LEFT JOIN
     oil_and_uv_types o ON p.id = o.project_id
     LEFT JOIN
     matte_lamination m ON p.id = m.project_id
     LEFT JOIN
     film_butter_paper f ON p.id = f.project_id
     LEFT JOIN
     cloth_paper c ON p.id = c.project_id
     LEFT JOIN
     pattern pt ON p.id = pt.project_id
     WHERE
     l.company_number = 'HS#1S'
     AND p.hot_stamping_paper_type IN ('高耐磨', '普通金纸')
     */
}
