package com.it.yts_project;

import com.it.yts_project.config.AbstractIntegrationTest;
import com.it.yts_project.service.GoldFoilProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class YtsProjectApplicationTests extends AbstractIntegrationTest {

    @Autowired
    private GoldFoilProductService goldFoilProductService;

    @Test
    void contextLoads() {
    }
}