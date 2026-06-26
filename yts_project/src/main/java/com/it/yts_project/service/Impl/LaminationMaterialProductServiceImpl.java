package com.it.yts_project.service.Impl;

import com.it.yts_project.dto.LaminationMaterialProductDTO;
import com.it.yts_project.dto.PagedResult;
import com.it.yts_project.mapper.LaminationMaterialProductMapper;
import com.it.yts_project.model.LaminationMaterialCompatibility;
import com.it.yts_project.model.LaminationMaterialProduct;
import com.it.yts_project.service.LaminationMaterialProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 过胶材料产品Service实现类
 */
@Service
@Transactional
@RequiredArgsConstructor
public class LaminationMaterialProductServiceImpl implements LaminationMaterialProductService {

    private final LaminationMaterialProductMapper laminationMaterialProductMapper;

    // ========== 产品管理 ==========

    @Override
    public List<LaminationMaterialProduct> getAllProducts() {
        return laminationMaterialProductMapper.findAllProducts();
    }

    @Override
    public LaminationMaterialProduct getProductById(Integer id) {
        return laminationMaterialProductMapper.findProductById(id);
    }

    @Override
    public List<LaminationMaterialProduct> searchProducts(String keyword) {
        return laminationMaterialProductMapper.searchProducts(keyword);
    }

    @Override
    public LaminationMaterialProduct saveProduct(LaminationMaterialProduct product) {
        if (product.getId() == null) {
            if (product.getIsActive() == null) {
                product.setIsActive(true);
            }
            laminationMaterialProductMapper.insertProduct(product);
        } else {
            laminationMaterialProductMapper.updateProduct(product);
        }
        return product;
    }

    @Override
    public void deleteProduct(Integer id) {
        laminationMaterialProductMapper.deleteProductById(id);
    }

    // ========== 兼容性管理 ==========

    @Override
    public List<LaminationMaterialCompatibility> getCompatibilitiesByProductId(Integer productId) {
        return laminationMaterialProductMapper.findCompatibilitiesByProductId(productId);
    }

    @Override
    public List<LaminationMaterialCompatibility> getAllCompatibilities() {
        return laminationMaterialProductMapper.findAllCompatibilities();
    }

    @Override
    public LaminationMaterialCompatibility getCompatibilityById(Integer id) {
        return laminationMaterialProductMapper.findCompatibilityById(id);
    }

    @Override
    public LaminationMaterialCompatibility saveCompatibility(LaminationMaterialCompatibility compatibility) {
        if (compatibility.getProductId() == null) {
            throw new IllegalArgumentException("产品ID不能为空");
        }

        LaminationMaterialProduct product = laminationMaterialProductMapper.findProductById(compatibility.getProductId());
        if (product == null) {
            throw new IllegalArgumentException("产品ID " + compatibility.getProductId() + " 不存在");
        }

        if (compatibility.getId() == null) {
            LaminationMaterialCompatibility existing = laminationMaterialProductMapper.findCompatibilityByUniqueKey(
                    compatibility.getProductId(), compatibility.getPostProcessingStep());

            if (existing != null) {
                throw new IllegalArgumentException("该产品与工序的兼容性配置已存在，请勿重复添加");
            }

            if (compatibility.getDisplayOrder() == null) {
                compatibility.setDisplayOrder(0);
            }

            laminationMaterialProductMapper.insertCompatibility(compatibility);
        } else {
            LaminationMaterialCompatibility existing = laminationMaterialProductMapper.findCompatibilityByUniqueKey(
                    compatibility.getProductId(), compatibility.getPostProcessingStep());

            if (existing != null && !existing.getId().equals(compatibility.getId())) {
                throw new IllegalArgumentException("该产品与工序的兼容性配置已存在");
            }

            laminationMaterialProductMapper.updateCompatibility(compatibility);
        }

        return compatibility;
    }

    @Override
    public void deleteCompatibility(Integer id) {
        laminationMaterialProductMapper.deleteCompatibilityById(id);
    }

    @Override
    public void batchSaveCompatibilities(List<LaminationMaterialCompatibility> compatibilities) {
        if (compatibilities == null || compatibilities.isEmpty()) {
            return;
        }

        for (LaminationMaterialCompatibility compatibility : compatibilities) {
            if (compatibility.getProductId() == null) {
                throw new IllegalArgumentException("产品ID不能为空");
            }

            LaminationMaterialProduct product = laminationMaterialProductMapper.findProductById(compatibility.getProductId());
            if (product == null) {
                throw new IllegalArgumentException("产品ID " + compatibility.getProductId() + " 不存在");
            }

            if (compatibility.getDisplayOrder() == null) {
                compatibility.setDisplayOrder(0);
            }

            LaminationMaterialCompatibility existing = laminationMaterialProductMapper.findCompatibilityByUniqueKey(
                    compatibility.getProductId(), compatibility.getPostProcessingStep());

            if (existing != null) {
                compatibility.setId(existing.getId());
                laminationMaterialProductMapper.updateCompatibility(compatibility);
            } else {
                laminationMaterialProductMapper.insertCompatibility(compatibility);
            }
        }
    }

    @Override
    public List<String> getAllPostProcessingSteps() {
        return laminationMaterialProductMapper.getAllPostProcessingSteps();
    }

    // ========== 匹配查询 ==========

    @Override
    public PagedResult<LaminationMaterialProduct> searchProducts(String keyword, List<String> materialTypes, List<String> steps, int page, int size) {
        int offset = (page - 1) * size;
        List<LaminationMaterialProduct> items = laminationMaterialProductMapper.searchProductsWithStep(keyword, materialTypes, steps, offset, size);
        Long total = laminationMaterialProductMapper.countProductsWithStep(keyword, materialTypes, steps);
        return new PagedResult<>(items, total, size, page);
    }

    @Override
    public List<String> getDistinctSteps() {
        return laminationMaterialProductMapper.getDistinctPostProcessingSteps();
    }

    @Override
    public List<String> getDistinctMaterialTypes() {
        return laminationMaterialProductMapper.getDistinctMaterialTypes();
    }

    @Override
    public LaminationMaterialProductDTO getProductDetail(Integer id) {
        LaminationMaterialProduct product = laminationMaterialProductMapper.findProductById(id);
        if (product == null) {
            return null;
        }
        List<LaminationMaterialCompatibility> compatibilities = laminationMaterialProductMapper.findCompatibilitiesByProductId(id);
        LaminationMaterialProductDTO dto = new LaminationMaterialProductDTO();
        dto.setProduct(product);
        dto.setCompatibilities(compatibilities);
        return dto;
    }
}
