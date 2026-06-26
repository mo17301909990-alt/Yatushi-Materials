package com.it.yts_project.service.Impl;

import com.it.yts_project.dto.SiliconeWatercolorInkProductDTO;
import com.it.yts_project.dto.PagedResult;
import com.it.yts_project.mapper.SiliconeWatercolorInkMapper;
import com.it.yts_project.model.SiliconeWatercolorInkProduct;
import com.it.yts_project.model.SiliconeWatercolorInkCompatibility;
import com.it.yts_project.service.SiliconeWatercolorInkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class SiliconeWatercolorInkServiceImpl implements SiliconeWatercolorInkService {

    @Autowired
    private SiliconeWatercolorInkMapper siliconeWatercolorInkMapper;

    @Override
    public List<SiliconeWatercolorInkProduct> getAllProducts() { return siliconeWatercolorInkMapper.findAllProducts(); }

    @Override
    public SiliconeWatercolorInkProduct getProductById(Integer id) { return siliconeWatercolorInkMapper.findProductById(id); }

    @Override
    public List<SiliconeWatercolorInkProduct> searchProducts(String keyword) { return siliconeWatercolorInkMapper.searchProducts(keyword); }

    @Override
    public SiliconeWatercolorInkProduct saveProduct(SiliconeWatercolorInkProduct product) {
        if (product.getId() == null) { siliconeWatercolorInkMapper.insertProduct(product); }
        else { siliconeWatercolorInkMapper.updateProduct(product); }
        return product;
    }

    @Override
    public void deleteProduct(Integer id) { siliconeWatercolorInkMapper.deleteProductById(id); }

    @Override
    public List<SiliconeWatercolorInkCompatibility> getCompatibilitiesByProductId(Integer productId) {
        return siliconeWatercolorInkMapper.findCompatibilitiesByProductId(productId);
    }

    @Override
    public SiliconeWatercolorInkCompatibility getCompatibilityById(Integer id) {
        return siliconeWatercolorInkMapper.findCompatibilityById(id);
    }

    @Override
    public SiliconeWatercolorInkCompatibility saveCompatibility(SiliconeWatercolorInkCompatibility compatibility) {
        if (compatibility.getProductId() == null) throw new IllegalArgumentException("产品ID不能为空");
        SiliconeWatercolorInkProduct product = siliconeWatercolorInkMapper.findProductById(compatibility.getProductId());
        if (product == null) throw new IllegalArgumentException("产品ID " + compatibility.getProductId() + " 不存在");
        if (compatibility.getId() == null) {
            SiliconeWatercolorInkCompatibility existing = siliconeWatercolorInkMapper.findCompatibilityByUniqueKey(
                compatibility.getProductId(), compatibility.getPostProcessingStep());
            if (existing != null) throw new IllegalArgumentException("该产品与后加工工序的兼容性配置已存在");
            siliconeWatercolorInkMapper.insertCompatibility(compatibility);
        } else {
            SiliconeWatercolorInkCompatibility existing = siliconeWatercolorInkMapper.findCompatibilityByUniqueKey(
                compatibility.getProductId(), compatibility.getPostProcessingStep());
            if (existing != null && !existing.getId().equals(compatibility.getId()))
                throw new IllegalArgumentException("该产品与后加工工序的兼容性配置已存在");
            siliconeWatercolorInkMapper.updateCompatibility(compatibility);
        }
        compatibility.setProductName(product.getMaterialName());
        return compatibility;
    }

    @Override
    public void deleteCompatibility(Integer id) { siliconeWatercolorInkMapper.deleteCompatibilityById(id); }

    @Override
    public void batchSaveCompatibilities(List<SiliconeWatercolorInkCompatibility> compatibilities) {
        if (compatibilities == null || compatibilities.isEmpty()) return;
        for (SiliconeWatercolorInkCompatibility comp : compatibilities) {
            if (comp.getProductId() == null) throw new IllegalArgumentException("产品ID不能为空");
            SiliconeWatercolorInkProduct product = siliconeWatercolorInkMapper.findProductById(comp.getProductId());
            if (product == null) throw new IllegalArgumentException("产品ID " + comp.getProductId() + " 不存在");
            SiliconeWatercolorInkCompatibility existing = siliconeWatercolorInkMapper.findCompatibilityByUniqueKey(
                comp.getProductId(), comp.getPostProcessingStep());
            if (existing != null) { comp.setId(existing.getId()); siliconeWatercolorInkMapper.updateCompatibility(comp); }
            else { siliconeWatercolorInkMapper.insertCompatibility(comp); }
        }
    }

    @Override
    public PagedResult<SiliconeWatercolorInkProduct> searchProducts(String keyword, String stepName, int page, int size) {
        int offset = (page - 1) * size;
        List<SiliconeWatercolorInkProduct> items = siliconeWatercolorInkMapper.searchProductsWithStep(keyword, stepName, offset, size);
        Long total = siliconeWatercolorInkMapper.countProductsWithStep(keyword, stepName);
        return new PagedResult<>(items, total, size, page);
    }

    @Override
    public List<String> getDistinctSteps() { return siliconeWatercolorInkMapper.getDistinctPostProcessingSteps(); }

    @Override
    public SiliconeWatercolorInkProductDTO getProductDetail(Integer id) {
        SiliconeWatercolorInkProduct product = siliconeWatercolorInkMapper.findProductById(id);
        if (product == null) return null;
        List<SiliconeWatercolorInkCompatibility> compatibilities = siliconeWatercolorInkMapper.findCompatibilitiesByProductId(id);
        SiliconeWatercolorInkProductDTO dto = new SiliconeWatercolorInkProductDTO();
        dto.setProduct(product);
        dto.setCompatibilities(compatibilities);
        return dto;
    }
}
