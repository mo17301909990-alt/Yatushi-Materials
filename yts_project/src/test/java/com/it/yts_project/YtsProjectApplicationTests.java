package com.it.yts_project;

import com.it.yts_project.config.AbstractIntegrationTest;
import com.it.yts_project.service.GoldFoilProductService;
import com.it.yts_project.service.HotStampingCompatibilityService;
import com.it.yts_project.service.LaminatingService;
import com.it.yts_project.service.LeoMatchService;
import com.it.yts_project.service.SiliconeService;
import com.it.yts_project.service.UvOilMatteService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
class YtsProjectApplicationTests extends AbstractIntegrationTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private GoldFoilProductService goldFoilProductService;

    @Test
    @DisplayName("Spring 上下文加载成功")
    void contextLoads() {
    }

    @Test
    @DisplayName("核心 Service Bean 注册检查")
    void keyBeansShouldBeRegistered() {
        assertNotNull(applicationContext.getBean(GoldFoilProductService.class),
                "GoldFoilProductService bean must be registered");
        assertNotNull(applicationContext.getBean(HotStampingCompatibilityService.class),
                "HotStampingCompatibilityService bean must be registered");
        assertNotNull(applicationContext.getBean(LaminatingService.class),
                "LaminatingService bean must be registered");
        assertNotNull(applicationContext.getBean(LeoMatchService.class),
                "LeoMatchService bean must be registered");
        assertNotNull(applicationContext.getBean(SiliconeService.class),
                "SiliconeService bean must be registered");
        assertNotNull(applicationContext.getBean(UvOilMatteService.class),
                "UvOilMatteService bean must be registered");
    }

    @Test
    @DisplayName("自动注入的 goldFoilProductService 不为 null")
    void injectedServiceShouldNotBeNull() {
        assertNotNull(goldFoilProductService, "Auto-wired GoldFoilProductService must not be null");
    }
}