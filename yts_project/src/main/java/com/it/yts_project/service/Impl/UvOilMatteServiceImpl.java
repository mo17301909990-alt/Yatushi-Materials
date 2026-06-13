package com.it.yts_project.service.Impl;

import com.it.yts_project.mapper.UvOilMatteMapper;
import com.it.yts_project.model.UvOilMatteProduct;
import com.it.yts_project.model.UvOilMatteCompatibility;
import com.it.yts_project.service.UvOilMatteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * UV油_哑光UV油 Service实现类
 */
@Service
@Transactional
public class UvOilMatteServiceImpl implements UvOilMatteService {

    @Autowired
    private UvOilMatteMapper uvOilMatteMapper;

    // ========== 产品管理 ==========

    @Override
    public List<UvOilMatteProduct> getAllProducts() {
        return uvOilMatteMapper.findAllProducts();
    }

    @Override
    public UvOilMatteProduct getProductById(Integer id) {
        return uvOilMatteMapper.findProductById(id);
    }

    @Override
    public List<UvOilMatteProduct> searchProducts(String keyword) {
        return uvOilMatteMapper.searchProducts(keyword);
    }

    @Override
    public UvOilMatteProduct saveProduct(UvOilMatteProduct product) {
        if (product.getId() == null) {
            uvOilMatteMapper.insertProduct(product);
        } else {
            uvOilMatteMapper.updateProduct(product);
        }
        return product;
    }

    @Override
    public void deleteProduct(Integer id) {
        uvOilMatteMapper.deleteProductById(id);
    }

    // ========== 兼容性管理 ==========

    @Override
    public List<UvOilMatteCompatibility> getCompatibilitiesByProductId(Integer productId) {
        return uvOilMatteMapper.findCompatibilitiesByProductId(productId);
    }

    @Override
    public UvOilMatteCompatibility getCompatibilityById(Integer id) {
        return uvOilMatteMapper.findCompatibilityById(id);
    }

    @Override
    public UvOilMatteCompatibility saveCompatibility(UvOilMatteCompatibility compatibility) {
        // 验证产品是否存在
        if (compatibility.getProductId() == null) {
            throw new IllegalArgumentException("产品ID不能为空");
        }

        UvOilMatteProduct product = uvOilMatteMapper.findProductById(compatibility.getProductId());
        if (product == null) {
            throw new IllegalArgumentException("产品ID " + compatibility.getProductId() + " 不存在");
        }

        if (compatibility.getId() == null) {
            // 新增：检查唯一键
            UvOilMatteCompatibility existing = uvOilMatteMapper.findCompatibilityByUniqueKey(
                compatibility.getProductId(), compatibility.getPostProcessingStep());
            if (existing != null) {
                throw new IllegalArgumentException("该产品与后加工工序的兼容性配置已存在");
            }
            uvOilMatteMapper.insertCompatibility(compatibility);
        } else {
            // 更新：检查唯一键(排除自身)
            UvOilMatteCompatibility existing = uvOilMatteMapper.findCompatibilityByUniqueKey(
                compatibility.getProductId(), compatibility.getPostProcessingStep());
            if (existing != null && !existing.getId().equals(compatibility.getId())) {
                throw new IllegalArgumentException("该产品与后加工工序的兼容性配置已存在");
            }
            uvOilMatteMapper.updateCompatibility(compatibility);
        }

        // 回填产品名称
        compatibility.setProductName(product.getMaterialName());
        return compatibility;
    }

    @Override
    public void deleteCompatibility(Integer id) {
        uvOilMatteMapper.deleteCompatibilityById(id);
    }

    @Override
    public void batchSaveCompatibilities(List<UvOilMatteCompatibility> compatibilities) {
        if (compatibilities == null || compatibilities.isEmpty()) {
            return;
        }

        for (UvOilMatteCompatibility compatibility : compatibilities) {
            // 验证产品ID
            if (compatibility.getProductId() == null) {
                throw new IllegalArgumentException("产品ID不能为空");
            }

            UvOilMatteProduct product = uvOilMatteMapper.findProductById(compatibility.getProductId());
            if (product == null) {
                throw new IllegalArgumentException("产品ID " + compatibility.getProductId() + " 不存在");
            }

            // 检查是否已存在
            UvOilMatteCompatibility existing = uvOilMatteMapper.findCompatibilityByUniqueKey(
                compatibility.getProductId(), compatibility.getPostProcessingStep());
            if (existing != null) {
                // 已存在则更新
                compatibility.setId(existing.getId());
                uvOilMatteMapper.updateCompatibility(compatibility);
            } else {
                // 不存在则插入
                uvOilMatteMapper.insertCompatibility(compatibility);
            }
        }
    }
}
