package com.it.yts_project.service.Impl;

import com.it.yts_project.dto.SiliconeLedUvSprayProductDTO;
import com.it.yts_project.dto.PagedResult;
import com.it.yts_project.mapper.SiliconeLedUvSprayMapper;
import com.it.yts_project.model.SiliconeLedUvSprayProduct;
import com.it.yts_project.model.SiliconeLedUvSprayCompatibility;
import com.it.yts_project.service.SiliconeLedUvSprayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class SiliconeLedUvSprayServiceImpl implements SiliconeLedUvSprayService {

    @Autowired
    private SiliconeLedUvSprayMapper siliconeLedUvSprayMapper;

    @Override
    public List<SiliconeLedUvSprayProduct> getAllProducts() { return siliconeLedUvSprayMapper.findAllProducts(); }

    @Override
    public SiliconeLedUvSprayProduct getProductById(Integer id) { return siliconeLedUvSprayMapper.findProductById(id); }

    @Override
    public List<SiliconeLedUvSprayProduct> searchProducts(String keyword) { return siliconeLedUvSprayMapper.searchProducts(keyword); }

    @Override
    public SiliconeLedUvSprayProduct saveProduct(SiliconeLedUvSprayProduct product) {
        if (product.getId() == null) { siliconeLedUvSprayMapper.insertProduct(product); }
        else { siliconeLedUvSprayMapper.updateProduct(product); }
        return product;
    }

    @Override
    public void deleteProduct(Integer id) { siliconeLedUvSprayMapper.deleteProductById(id); }

    @Override
    public List<SiliconeLedUvSprayCompatibility> getCompatibilitiesByProductId(Integer productId) {
        return siliconeLedUvSprayMapper.findCompatibilitiesByProductId(productId);
    }

    @Override
    public SiliconeLedUvSprayCompatibility getCompatibilityById(Integer id) {
        return siliconeLedUvSprayMapper.findCompatibilityById(id);
    }

    @Override
    public SiliconeLedUvSprayCompatibility saveCompatibility(SiliconeLedUvSprayCompatibility compatibility) {
        if (compatibility.getProductId() == null) throw new IllegalArgumentException("产品ID不能为空");
        SiliconeLedUvSprayProduct product = siliconeLedUvSprayMapper.findProductById(compatibility.getProductId());
        if (product == null) throw new IllegalArgumentException("产品ID " + compatibility.getProductId() + " 不存在");
        if (compatibility.getId() == null) {
            SiliconeLedUvSprayCompatibility existing = siliconeLedUvSprayMapper.findCompatibilityByUniqueKey(
                compatibility.getProductId(), compatibility.getPostProcessingStep());
            if (existing != null) throw new IllegalArgumentException("该产品与后加工工序的兼容性配置已存在");
            siliconeLedUvSprayMapper.insertCompatibility(compatibility);
        } else {
            SiliconeLedUvSprayCompatibility existing = siliconeLedUvSprayMapper.findCompatibilityByUniqueKey(
                compatibility.getProductId(), compatibility.getPostProcessingStep());
            if (existing != null && !existing.getId().equals(compatibility.getId()))
                throw new IllegalArgumentException("该产品与后加工工序的兼容性配置已存在");
            siliconeLedUvSprayMapper.updateCompatibility(compatibility);
        }
        compatibility.setProductName(product.getMaterialName());
        return compatibility;
    }

    @Override
    public void deleteCompatibility(Integer id) { siliconeLedUvSprayMapper.deleteCompatibilityById(id); }

    @Override
    public void batchSaveCompatibilities(List<SiliconeLedUvSprayCompatibility> compatibilities) {
        if (compatibilities == null || compatibilities.isEmpty()) return;
        for (SiliconeLedUvSprayCompatibility comp : compatibilities) {
            if (comp.getProductId() == null) throw new IllegalArgumentException("产品ID不能为空");
            SiliconeLedUvSprayProduct product = siliconeLedUvSprayMapper.findProductById(comp.getProductId());
            if (product == null) throw new IllegalArgumentException("产品ID " + comp.getProductId() + " 不存在");
            SiliconeLedUvSprayCompatibility existing = siliconeLedUvSprayMapper.findCompatibilityByUniqueKey(
                comp.getProductId(), comp.getPostProcessingStep());
            if (existing != null) { comp.setId(existing.getId()); siliconeLedUvSprayMapper.updateCompatibility(comp); }
            else { siliconeLedUvSprayMapper.insertCompatibility(comp); }
        }
    }

    @Override
    public PagedResult<SiliconeLedUvSprayProduct> searchProducts(String keyword, String stepName, int page, int size) {
        int offset = (page - 1) * size;
        List<SiliconeLedUvSprayProduct> items = siliconeLedUvSprayMapper.searchProductsWithStep(keyword, stepName, offset, size);
        Long total = siliconeLedUvSprayMapper.countProductsWithStep(keyword, stepName);
        return new PagedResult<>(items, total, size, page);
    }

    @Override
    public List<String> getDistinctSteps() { return siliconeLedUvSprayMapper.getDistinctPostProcessingSteps(); }

    @Override
    public SiliconeLedUvSprayProductDTO getProductDetail(Integer id) {
        SiliconeLedUvSprayProduct product = siliconeLedUvSprayMapper.findProductById(id);
        if (product == null) return null;
        List<SiliconeLedUvSprayCompatibility> compatibilities = siliconeLedUvSprayMapper.findCompatibilitiesByProductId(id);
        SiliconeLedUvSprayProductDTO dto = new SiliconeLedUvSprayProductDTO();
        dto.setProduct(product);
        dto.setCompatibilities(compatibilities);
        return dto;
    }
}
