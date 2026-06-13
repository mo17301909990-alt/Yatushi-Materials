package com.it.yts_project.service.Impl;

import com.it.yts_project.mapper.YaguangUvOilMapper;
import com.it.yts_project.model.YaguangUvOilProduct;
import com.it.yts_project.model.YaguangUvOilCompatibility;
import com.it.yts_project.service.YaguangUvOilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 哑光UV油 Service实现类
 */
@Service
@Transactional
public class YaguangUvOilServiceImpl implements YaguangUvOilService {

    @Autowired
    private YaguangUvOilMapper yaguangUvOilMapper;

    // ========== 产品管理 ==========

    @Override
    public List<YaguangUvOilProduct> getAllProducts() {
        return yaguangUvOilMapper.findAllProducts();
    }

    @Override
    public YaguangUvOilProduct getProductById(Integer id) {
        return yaguangUvOilMapper.findProductById(id);
    }

    @Override
    public List<YaguangUvOilProduct> searchProducts(String keyword) {
        return yaguangUvOilMapper.searchProducts(keyword);
    }

    @Override
    public YaguangUvOilProduct saveProduct(YaguangUvOilProduct product) {
        if (product.getId() == null) {
            yaguangUvOilMapper.insertProduct(product);
        } else {
            yaguangUvOilMapper.updateProduct(product);
        }
        return product;
    }

    @Override
    public void deleteProduct(Integer id) {
        yaguangUvOilMapper.deleteProductById(id);
    }

    // ========== 兼容性管理 ==========

    @Override
    public List<YaguangUvOilCompatibility> getCompatibilitiesByProductId(Integer productId) {
        return yaguangUvOilMapper.findCompatibilitiesByProductId(productId);
    }

    @Override
    public YaguangUvOilCompatibility getCompatibilityById(Integer id) {
        return yaguangUvOilMapper.findCompatibilityById(id);
    }

    @Override
    public YaguangUvOilCompatibility saveCompatibility(YaguangUvOilCompatibility compatibility) {
        // 验证产品是否存在
        if (compatibility.getProductId() == null) {
            throw new IllegalArgumentException("产品ID不能为空");
        }

        YaguangUvOilProduct product = yaguangUvOilMapper.findProductById(compatibility.getProductId());
        if (product == null) {
            throw new IllegalArgumentException("产品ID " + compatibility.getProductId() + " 不存在");
        }

        if (compatibility.getId() == null) {
            // 新增：检查唯一键
            YaguangUvOilCompatibility existing = yaguangUvOilMapper.findCompatibilityByUniqueKey(
                    compatibility.getProductId(), compatibility.getStepName());
            if (existing != null) {
                throw new IllegalArgumentException("该产品与步骤名称的兼容性配置已存在");
            }
            yaguangUvOilMapper.insertCompatibility(compatibility);
        } else {
            // 更新：检查唯一键(排除自身)
            YaguangUvOilCompatibility existing = yaguangUvOilMapper.findCompatibilityByUniqueKey(
                    compatibility.getProductId(), compatibility.getStepName());
            if (existing != null && !existing.getId().equals(compatibility.getId())) {
                throw new IllegalArgumentException("该产品与步骤名称的兼容性配置已存在");
            }
            yaguangUvOilMapper.updateCompatibility(compatibility);
        }

        // 回填产品名称
        compatibility.setProductName(product.getMaterialName());
        return compatibility;
    }

    @Override
    public void deleteCompatibility(Integer id) {
        yaguangUvOilMapper.deleteCompatibilityById(id);
    }

    @Override
    public void batchSaveCompatibilities(List<YaguangUvOilCompatibility> compatibilities) {
        if (compatibilities == null || compatibilities.isEmpty()) {
            return;
        }

        for (YaguangUvOilCompatibility compatibility : compatibilities) {
            // 验证产品ID
            if (compatibility.getProductId() == null) {
                throw new IllegalArgumentException("产品ID不能为空");
            }

            YaguangUvOilProduct product = yaguangUvOilMapper.findProductById(compatibility.getProductId());
            if (product == null) {
                throw new IllegalArgumentException("产品ID " + compatibility.getProductId() + " 不存在");
            }

            // 检查是否已存在
            YaguangUvOilCompatibility existing = yaguangUvOilMapper.findCompatibilityByUniqueKey(
                    compatibility.getProductId(), compatibility.getStepName());
            if (existing != null) {
                // 已存在则更新
                compatibility.setId(existing.getId());
                yaguangUvOilMapper.updateCompatibility(compatibility);
            } else {
                // 不存在则插入
                yaguangUvOilMapper.insertCompatibility(compatibility);
            }
        }
    }
}
