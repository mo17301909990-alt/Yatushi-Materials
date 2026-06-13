package com.it.yts_project;

import com.it.yts_project.service.GoldFoilProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class YtsProjectApplicationTests {

    @Autowired
    private GoldFoilProductService goldFoilProductService;

    @Test
    void contextLoads() {
    }
}