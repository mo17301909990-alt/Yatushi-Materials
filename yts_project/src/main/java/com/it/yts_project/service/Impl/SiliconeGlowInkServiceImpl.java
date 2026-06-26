package com.it.yts_project.service.Impl;

import com.it.yts_project.dto.SiliconeGlowInkProductDTO;
import com.it.yts_project.dto.PagedResult;
import com.it.yts_project.mapper.SiliconeGlowInkMapper;
import com.it.yts_project.model.SiliconeGlowInkProduct;
import com.it.yts_project.model.SiliconeGlowInkCompatibility;
import com.it.yts_project.service.SiliconeGlowInkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class SiliconeGlowInkServiceImpl implements SiliconeGlowInkService {

    @Autowired
    private SiliconeGlowInkMapper siliconeGlowInkMapper;

    @Override
    public List<SiliconeGlowInkProduct> getAllProducts() { return siliconeGlowInkMapper.findAllProducts(); }

    @Override
    public SiliconeGlowInkProduct getProductById(Integer id) { return siliconeGlowInkMapper.findProductById(id); }

    @Override
    public List<SiliconeGlowInkProduct> searchProducts(String keyword) { return siliconeGlowInkMapper.searchProducts(keyword); }

    @Override
    public SiliconeGlowInkProduct saveProduct(SiliconeGlowInkProduct product) {
        if (product.getId() == null) { siliconeGlowInkMapper.insertProduct(product); }
        else { siliconeGlowInkMapper.updateProduct(product); }
        return product;
    }

    @Override
    public void deleteProduct(Integer id) { siliconeGlowInkMapper.deleteProductById(id); }

    @Override
    public List<SiliconeGlowInkCompatibility> getCompatibilitiesByProductId(Integer productId) {
        return siliconeGlowInkMapper.findCompatibilitiesByProductId(productId);
    }

    @Override
    public SiliconeGlowInkCompatibility getCompatibilityById(Integer id) {
        return siliconeGlowInkMapper.findCompatibilityById(id);
    }

    @Override
    public SiliconeGlowInkCompatibility saveCompatibility(SiliconeGlowInkCompatibility compatibility) {
        if (compatibility.getProductId() == null) throw new IllegalArgumentException("产品ID不能为空");
        SiliconeGlowInkProduct product = siliconeGlowInkMapper.findProductById(compatibility.getProductId());
        if (product == null) throw new IllegalArgumentException("产品ID " + compatibility.getProductId() + " 不存在");
        if (compatibility.getId() == null) {
            SiliconeGlowInkCompatibility existing = siliconeGlowInkMapper.findCompatibilityByUniqueKey(
                compatibility.getProductId(), compatibility.getPostProcessingStep());
            if (existing != null) throw new IllegalArgumentException("该产品与后加工工序的兼容性配置已存在");
            siliconeGlowInkMapper.insertCompatibility(compatibility);
        } else {
            SiliconeGlowInkCompatibility existing = siliconeGlowInkMapper.findCompatibilityByUniqueKey(
                compatibility.getProductId(), compatibility.getPostProcessingStep());
            if (existing != null && !existing.getId().equals(compatibility.getId()))
                throw new IllegalArgumentException("该产品与后加工工序的兼容性配置已存在");
            siliconeGlowInkMapper.updateCompatibility(compatibility);
        }
        compatibility.setProductName(product.getMaterialName());
        return compatibility;
    }

    @Override
    public void deleteCompatibility(Integer id) { siliconeGlowInkMapper.deleteCompatibilityById(id); }

    @Override
    public void batchSaveCompatibilities(List<SiliconeGlowInkCompatibility> compatibilities) {
        if (compatibilities == null || compatibilities.isEmpty()) return;
        for (SiliconeGlowInkCompatibility comp : compatibilities) {
            if (comp.getProductId() == null) throw new IllegalArgumentException("产品ID不能为空");
            SiliconeGlowInkProduct product = siliconeGlowInkMapper.findProductById(comp.getProductId());
            if (product == null) throw new IllegalArgumentException("产品ID " + comp.getProductId() + " 不存在");
            SiliconeGlowInkCompatibility existing = siliconeGlowInkMapper.findCompatibilityByUniqueKey(
                comp.getProductId(), comp.getPostProcessingStep());
            if (existing != null) { comp.setId(existing.getId()); siliconeGlowInkMapper.updateCompatibility(comp); }
            else { siliconeGlowInkMapper.insertCompatibility(comp); }
        }
    }

    @Override
    public PagedResult<SiliconeGlowInkProduct> searchProducts(String keyword, String stepName, int page, int size) {
        int offset = (page - 1) * size;
        List<SiliconeGlowInkProduct> items = siliconeGlowInkMapper.searchProductsWithStep(keyword, stepName, offset, size);
        Long total = siliconeGlowInkMapper.countProductsWithStep(keyword, stepName);
        return new PagedResult<>(items, total, size, page);
    }

    @Override
    public List<String> getDistinctSteps() { return siliconeGlowInkMapper.getDistinctPostProcessingSteps(); }

    @Override
    public SiliconeGlowInkProductDTO getProductDetail(Integer id) {
        SiliconeGlowInkProduct product = siliconeGlowInkMapper.findProductById(id);
        if (product == null) return null;
        List<SiliconeGlowInkCompatibility> compatibilities = siliconeGlowInkMapper.findCompatibilitiesByProductId(id);
        SiliconeGlowInkProductDTO dto = new SiliconeGlowInkProductDTO();
        dto.setProduct(product);
        dto.setCompatibilities(compatibilities);
        return dto;
    }
}
