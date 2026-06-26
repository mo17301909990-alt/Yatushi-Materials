package com.it.yts_project.service.Impl;

import com.it.yts_project.dto.SiliconeWaterBaseTransparentProductDTO;
import com.it.yts_project.dto.PagedResult;
import com.it.yts_project.mapper.SiliconeWaterBaseTransparentMapper;
import com.it.yts_project.model.SiliconeWaterBaseTransparentProduct;
import com.it.yts_project.model.SiliconeWaterBaseTransparentCompatibility;
import com.it.yts_project.service.SiliconeWaterBaseTransparentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class SiliconeWaterBaseTransparentServiceImpl implements SiliconeWaterBaseTransparentService {

    @Autowired
    private SiliconeWaterBaseTransparentMapper siliconeWaterBaseTransparentMapper;

    @Override
    public List<SiliconeWaterBaseTransparentProduct> getAllProducts() { return siliconeWaterBaseTransparentMapper.findAllProducts(); }

    @Override
    public SiliconeWaterBaseTransparentProduct getProductById(Integer id) { return siliconeWaterBaseTransparentMapper.findProductById(id); }

    @Override
    public List<SiliconeWaterBaseTransparentProduct> searchProducts(String keyword) { return siliconeWaterBaseTransparentMapper.searchProducts(keyword); }

    @Override
    public SiliconeWaterBaseTransparentProduct saveProduct(SiliconeWaterBaseTransparentProduct product) {
        if (product.getId() == null) { siliconeWaterBaseTransparentMapper.insertProduct(product); }
        else { siliconeWaterBaseTransparentMapper.updateProduct(product); }
        return product;
    }

    @Override
    public void deleteProduct(Integer id) { siliconeWaterBaseTransparentMapper.deleteProductById(id); }

    @Override
    public List<SiliconeWaterBaseTransparentCompatibility> getCompatibilitiesByProductId(Integer productId) {
        return siliconeWaterBaseTransparentMapper.findCompatibilitiesByProductId(productId);
    }

    @Override
    public SiliconeWaterBaseTransparentCompatibility getCompatibilityById(Integer id) {
        return siliconeWaterBaseTransparentMapper.findCompatibilityById(id);
    }

    @Override
    public SiliconeWaterBaseTransparentCompatibility saveCompatibility(SiliconeWaterBaseTransparentCompatibility compatibility) {
        if (compatibility.getProductId() == null) throw new IllegalArgumentException("产品ID不能为空");
        SiliconeWaterBaseTransparentProduct product = siliconeWaterBaseTransparentMapper.findProductById(compatibility.getProductId());
        if (product == null) throw new IllegalArgumentException("产品ID " + compatibility.getProductId() + " 不存在");
        if (compatibility.getId() == null) {
            SiliconeWaterBaseTransparentCompatibility existing = siliconeWaterBaseTransparentMapper.findCompatibilityByUniqueKey(
                compatibility.getProductId(), compatibility.getPostProcessingStep());
            if (existing != null) throw new IllegalArgumentException("该产品与后加工工序的兼容性配置已存在");
            siliconeWaterBaseTransparentMapper.insertCompatibility(compatibility);
        } else {
            SiliconeWaterBaseTransparentCompatibility existing = siliconeWaterBaseTransparentMapper.findCompatibilityByUniqueKey(
                compatibility.getProductId(), compatibility.getPostProcessingStep());
            if (existing != null && !existing.getId().equals(compatibility.getId()))
                throw new IllegalArgumentException("该产品与后加工工序的兼容性配置已存在");
            siliconeWaterBaseTransparentMapper.updateCompatibility(compatibility);
        }
        compatibility.setProductName(product.getMaterialName());
        return compatibility;
    }

    @Override
    public void deleteCompatibility(Integer id) { siliconeWaterBaseTransparentMapper.deleteCompatibilityById(id); }

    @Override
    public void batchSaveCompatibilities(List<SiliconeWaterBaseTransparentCompatibility> compatibilities) {
        if (compatibilities == null || compatibilities.isEmpty()) return;
        for (SiliconeWaterBaseTransparentCompatibility comp : compatibilities) {
            if (comp.getProductId() == null) throw new IllegalArgumentException("产品ID不能为空");
            SiliconeWaterBaseTransparentProduct product = siliconeWaterBaseTransparentMapper.findProductById(comp.getProductId());
            if (product == null) throw new IllegalArgumentException("产品ID " + comp.getProductId() + " 不存在");
            SiliconeWaterBaseTransparentCompatibility existing = siliconeWaterBaseTransparentMapper.findCompatibilityByUniqueKey(
                comp.getProductId(), comp.getPostProcessingStep());
            if (existing != null) { comp.setId(existing.getId()); siliconeWaterBaseTransparentMapper.updateCompatibility(comp); }
            else { siliconeWaterBaseTransparentMapper.insertCompatibility(comp); }
        }
    }

    @Override
    public PagedResult<SiliconeWaterBaseTransparentProduct> searchProducts(String keyword, String stepName, int page, int size) {
        int offset = (page - 1) * size;
        List<SiliconeWaterBaseTransparentProduct> items = siliconeWaterBaseTransparentMapper.searchProductsWithStep(keyword, stepName, offset, size);
        Long total = siliconeWaterBaseTransparentMapper.countProductsWithStep(keyword, stepName);
        return new PagedResult<>(items, total, size, page);
    }

    @Override
    public List<String> getDistinctSteps() { return siliconeWaterBaseTransparentMapper.getDistinctPostProcessingSteps(); }

    @Override
    public SiliconeWaterBaseTransparentProductDTO getProductDetail(Integer id) {
        SiliconeWaterBaseTransparentProduct product = siliconeWaterBaseTransparentMapper.findProductById(id);
        if (product == null) return null;
        List<SiliconeWaterBaseTransparentCompatibility> compatibilities = siliconeWaterBaseTransparentMapper.findCompatibilitiesByProductId(id);
        SiliconeWaterBaseTransparentProductDTO dto = new SiliconeWaterBaseTransparentProductDTO();
        dto.setProduct(product);
        dto.setCompatibilities(compatibilities);
        return dto;
    }
}
