package com.it.yts_project.service;

import com.it.yts_project.config.AbstractIntegrationTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

/**
 * GoldFoilProductService 集成测试。
 * 使用 H2 内存数据库（PostgreSQL 兼容模式），验证 Service 层基础方法可正常调用。
 * <p>
 * 注意：H2 不支持 PostgreSQL 专有 SQL（如 jsonb、array_agg DISTINCT 等），
 * 部分查询可能在 H2 下抛出异常。测试通过 @Disabled 标记需要真实 PG 的用例。
 */
@Slf4j
@SpringBootTest
@ActiveProfiles("test")
class GoldFoilProductServiceTest extends AbstractIntegrationTest {

    @Autowired
    private GoldFoilProductService goldFoilProductService;

    @Test
    @DisplayName("获取全部烫金纸产品列表，返回非空列表或空列表（不抛异常）")
    void getAllGoldFoilProducts_shouldReturnListWithoutException() {
        assertDoesNotThrow(() -> {
            List<?> products = goldFoilProductService.getAllGoldFoilProducts();
            assertNotNull(products, "getAllGoldFoilProducts() 不应返回 null");
            log.info("getAllGoldFoilProducts 返回 {} 条记录（H2 模式，可能为空）", products.size());
        });
    }

    @Test
    @DisplayName("获取去重公司编号列表，不抛异常")
    void getDistinctCompanyNumbers_shouldNotThrow() {
        assertDoesNotThrow(() -> {
            List<String> numbers = goldFoilProductService.getDistinctCompanyNumbers();
            assertNotNull(numbers, "getDistinctCompanyNumbers() 不应返回 null");
            log.info("getDistinctCompanyNumbers 返回 {} 条记录", numbers.size());
        });
    }

    @Test
    @DisplayName("获取去重烫金纸编号列表，不抛异常")
    void getDistinctGpNumbers_shouldNotThrow() {
        assertDoesNotThrow(() -> {
            List<String> numbers = goldFoilProductService.getDistinctGpNumbers();
            assertNotNull(numbers, "getDistinctGpNumbers() 不应返回 null");
            log.info("getDistinctGpNumbers 返回 {} 条记录", numbers.size());
        });
    }
}
