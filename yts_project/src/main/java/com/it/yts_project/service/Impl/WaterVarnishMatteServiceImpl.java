package com.it.yts_project.service.Impl;

import com.it.yts_project.dto.PagedResult;
import com.it.yts_project.dto.WaterVarnishMatteProductDTO;
import com.it.yts_project.mapper.WaterVarnishMatteMapper;
import com.it.yts_project.model.WaterVarnishMatteProduct;
import com.it.yts_project.model.WaterVarnishMatteCompatibility;
import com.it.yts_project.service.WaterVarnishMatteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 水油(哑光) Service实现类
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
    public WaterVarnishMatteProduct getProductById(Integer id) {
        return waterVarnishMatteMapper.findProductById(id);
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
    public WaterVarnishMatteCompatibility getCompatibilityById(Integer id) {
        return waterVarnishMatteMapper.findCompatibilityById(id);
    }

    @Override
    public WaterVarnishMatteCompatibility saveCompatibility(WaterVarnishMatteCompatibility compatibility) {
        // 验证产品是否存在
        if (compatibility.getProductId() == null) {
            throw new IllegalArgumentException("产品ID不能为空");
        }

        WaterVarnishMatteProduct product = waterVarnishMatteMapper.findProductById(compatibility.getProductId());
        if (product == null) {
            throw new IllegalArgumentException("产品ID " + compatibility.getProductId() + " 不存在");
        }

        if (compatibility.getId() == null) {
            // 新增：检查唯一键
            WaterVarnishMatteCompatibility existing = waterVarnishMatteMapper.findCompatibilityByUniqueKey(
                compatibility.getProductId(), compatibility.getPostProcessingStep());
            if (existing != null) {
                throw new IllegalArgumentException("该产品与后加工工序的兼容性配置已存在");
            }
            waterVarnishMatteMapper.insertCompatibility(compatibility);
        } else {
            // 更新：检查唯一键(排除自身)
            WaterVarnishMatteCompatibility existing = waterVarnishMatteMapper.findCompatibilityByUniqueKey(
                compatibility.getProductId(), compatibility.getPostProcessingStep());
            if (existing != null && !existing.getId().equals(compatibility.getId())) {
                throw new IllegalArgumentException("该产品与后加工工序的兼容性配置已存在");
            }
            waterVarnishMatteMapper.updateCompatibility(compatibility);
        }

        // 回填产品名称
        compatibility.setProductName(product.getMaterialName());
        return compatibility;
    }

    @Override
    public void deleteCompatibility(Integer id) {
        waterVarnishMatteMapper.deleteCompatibilityById(id);
    }

    @Override
    public void batchSaveCompatibilities(List<WaterVarnishMatteCompatibility> compatibilities) {
        if (compatibilities == null || compatibilities.isEmpty()) {
            return;
        }

        for (WaterVarnishMatteCompatibility compatibility : compatibilities) {
            // 验证产品ID
            if (compatibility.getProductId() == null) {
                throw new IllegalArgumentException("产品ID不能为空");
            }

            WaterVarnishMatteProduct product = waterVarnishMatteMapper.findProductById(compatibility.getProductId());
            if (product == null) {
                throw new IllegalArgumentException("产品ID " + compatibility.getProductId() + " 不存在");
            }

            // 检查是否已存在
            WaterVarnishMatteCompatibility existing = waterVarnishMatteMapper.findCompatibilityByUniqueKey(
                compatibility.getProductId(), compatibility.getPostProcessingStep());
            if (existing != null) {
                // 已存在则更新
                compatibility.setId(existing.getId());
                waterVarnishMatteMapper.updateCompatibility(compatibility);
            } else {
                // 不存在则插入
                waterVarnishMatteMapper.insertCompatibility(compatibility);
            }
        }
    }

    // ========== 匹配查询 ==========

    @Override
    public PagedResult<WaterVarnishMatteProduct> searchProducts(String keyword, String stepName, int page, int size) {
        int offset = (page - 1) * size;
        List<WaterVarnishMatteProduct> items = waterVarnishMatteMapper.searchProductsWithStep(keyword, stepName, offset, size);
        Long total = waterVarnishMatteMapper.countProductsWithStep(keyword, stepName);
        return new PagedResult<>(items, total, size, page);
    }

    @Override
    public List<String> getDistinctSteps() {
        return waterVarnishMatteMapper.getDistinctPostProcessingSteps();
    }

    @Override
    public WaterVarnishMatteProductDTO getProductDetail(Integer id) {
        WaterVarnishMatteProduct product = waterVarnishMatteMapper.findProductById(id);
        if (product == null) {
            return null;
        }
        List<WaterVarnishMatteCompatibility> compatibilities = waterVarnishMatteMapper.findCompatibilitiesByProductId(id);
        WaterVarnishMatteProductDTO dto = new WaterVarnishMatteProductDTO();
        dto.setProduct(product);
        dto.setCompatibilities(compatibilities);
        return dto;
    }
}
