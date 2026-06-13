package com.it.yts_project.service.Impl;

import com.it.yts_project.mapper.WaterVarnishMatteMapper;
import com.it.yts_project.model.WaterVarnishMatteCompatibility;
import com.it.yts_project.model.WaterVarnishMatteProduct;
import com.it.yts_project.service.WaterVarnishMatteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * UV油_哑光水油 Service实现类
 */
@Service
@Transactional
public class WaterVarnishMatteServiceImpl implements WaterVarnishMatteService {

    @Autowired
    private WaterVarnishMatteMapper waterVarnishMatteMapper;

    // ========== 产品管理 ==========

    @Override
    public List<WaterVarnishMatteProduct> getAllProducts() {
        return waterVarnishMatteMapper.findAllProducts();
    }

    @Override
    public List<WaterVarnishMatteProduct> getActiveProducts() {
        return waterVarnishMatteMapper.findActiveProducts();
    }

    @Override
    public WaterVarnishMatteProduct getProductById(Integer id) {
        return waterVarnishMatteMapper.findProductById(id);
    }

    @Override
    public WaterVarnishMatteProduct getProductByMaterialCode(String materialCode) {
        return waterVarnishMatteMapper.findProductByMaterialCode(materialCode);
    }

    @Override
    public List<WaterVarnishMatteProduct> searchProducts(String keyword) {
        return waterVarnishMatteMapper.searchProducts(keyword);
    }

    @Override
    public WaterVarnishMatteProduct saveProduct(WaterVarnishMatteProduct product) {
        if (product.getId() == null) {
            waterVarnishMatteMapper.insertProduct(product);
        } else {
            waterVarnishMatteMapper.updateProduct(product);
        }
        return product;
    }

    @Override
    public void deleteProduct(Integer id) {
        waterVarnishMatteMapper.deleteProductById(id);
    }

    // ========== 兼容性管理 ==========

    @Override
    public List<WaterVarnishMatteCompatibility> getCompatibilitiesByProductId(Integer productId) {
        return waterVarnishMatteMapper.findCompatibilitiesByProductId(productId);
    }

    @Override
    public List<WaterVarnishMatteCompatibility> getAllCompatibilities() {
        return waterVarnishMatteMapper.findAllCompatibilities();
    }

    @Override
    public WaterVarnishMatteCompatibility getCompatibilityById(Integer id) {
        return waterVarnishMatteMapper.findCompatibilityById(id);
    }

    @Override
    public WaterVarnishMatteCompatibility saveCompatibility(WaterVarnishMatteCompatibility compatibility) {
        if (compatibility.getId() == null) {
            // 检查是否已存在相同的唯一键组合
            WaterVarnishMatteCompatibility existing = waterVarnishMatteMapper.findCompatibilityByProductAndStep(
                    compatibility.getProductId(), compatibility.getPostProcessingStep());
            if (existing != null) {
                throw new IllegalArgumentException("该产品与后加工工序的兼容性配置已存在，请勿重复添加");
            }
            waterVarnishMatteMapper.insertCompatibility(compatibility);
        } else {
            waterVarnishMatteMapper.updateCompatibility(compatibility);
        }
        return compatibility;
    }

    @Override
    public void batchSaveCompatibility(List<WaterVarnishMatteCompatibility> compatibilities) {
        for (WaterVarnishMatteCompatibility compatibility : compatibilities) {
            if (compatibility.getProductId() == null) {
                throw new IllegalArgumentException("产品ID不能为空");
            }
            // 检查是否已存在
            WaterVarnishMatteCompatibility existing = waterVarnishMatteMapper.findCompatibilityByProductAndStep(
                    compatibility.getProductId(), compatibility.getPostProcessingStep());
            if (existing != null) {
                compatibility.setId(existing.getId());
                waterVarnishMatteMapper.updateCompatibility(compatibility);
            } else {
                waterVarnishMatteMapper.insertCompatibility(compatibility);
            }
        }
    }

    @Override
    public void deleteCompatibility(Integer id) {
        waterVarnishMatteMapper.deleteCompatibilityById(id);
    }

    @Override
    public void deleteCompatibilityByProductId(Integer productId) {
        waterVarnishMatteMapper.deleteCompatibilityByProductId(productId);
    }

    @Override
    public List<String> getAllPostProcessingSteps() {
        return waterVarnishMatteMapper.getAllPostProcessingSteps();
    }
}
