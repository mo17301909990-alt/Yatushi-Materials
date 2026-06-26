package com.it.yts_project.service.Impl;

import com.it.yts_project.dto.SiliconeCoarseUvProductDTO;
import com.it.yts_project.dto.PagedResult;
import com.it.yts_project.mapper.SiliconeCoarseUvMapper;
import com.it.yts_project.model.SiliconeCoarseUvProduct;
import com.it.yts_project.model.SiliconeCoarseUvCompatibility;
import com.it.yts_project.service.SiliconeCoarseUvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class SiliconeCoarseUvServiceImpl implements SiliconeCoarseUvService {

    @Autowired
    private SiliconeCoarseUvMapper siliconeCoarseUvMapper;

    @Override
    public List<SiliconeCoarseUvProduct> getAllProducts() { return siliconeCoarseUvMapper.findAllProducts(); }

    @Override
    public SiliconeCoarseUvProduct getProductById(Integer id) { return siliconeCoarseUvMapper.findProductById(id); }

    @Override
    public List<SiliconeCoarseUvProduct> searchProducts(String keyword) { return siliconeCoarseUvMapper.searchProducts(keyword); }

    @Override
    public SiliconeCoarseUvProduct saveProduct(SiliconeCoarseUvProduct product) {
        if (product.getId() == null) { siliconeCoarseUvMapper.insertProduct(product); }
        else { siliconeCoarseUvMapper.updateProduct(product); }
        return product;
    }

    @Override
    public void deleteProduct(Integer id) { siliconeCoarseUvMapper.deleteProductById(id); }

    @Override
    public List<SiliconeCoarseUvCompatibility> getCompatibilitiesByProductId(Integer productId) {
        return siliconeCoarseUvMapper.findCompatibilitiesByProductId(productId);
    }

    @Override
    public SiliconeCoarseUvCompatibility getCompatibilityById(Integer id) {
        return siliconeCoarseUvMapper.findCompatibilityById(id);
    }

    @Override
    public SiliconeCoarseUvCompatibility saveCompatibility(SiliconeCoarseUvCompatibility compatibility) {
        if (compatibility.getProductId() == null) throw new IllegalArgumentException("产品ID不能为空");
        SiliconeCoarseUvProduct product = siliconeCoarseUvMapper.findProductById(compatibility.getProductId());
        if (product == null) throw new IllegalArgumentException("产品ID " + compatibility.getProductId() + " 不存在");
        if (compatibility.getId() == null) {
            SiliconeCoarseUvCompatibility existing = siliconeCoarseUvMapper.findCompatibilityByUniqueKey(
                compatibility.getProductId(), compatibility.getPostProcessingStep());
            if (existing != null) throw new IllegalArgumentException("该产品与后加工工序的兼容性配置已存在");
            siliconeCoarseUvMapper.insertCompatibility(compatibility);
        } else {
            SiliconeCoarseUvCompatibility existing = siliconeCoarseUvMapper.findCompatibilityByUniqueKey(
                compatibility.getProductId(), compatibility.getPostProcessingStep());
            if (existing != null && !existing.getId().equals(compatibility.getId()))
                throw new IllegalArgumentException("该产品与后加工工序的兼容性配置已存在");
            siliconeCoarseUvMapper.updateCompatibility(compatibility);
        }
        compatibility.setProductName(product.getMaterialName());
        return compatibility;
    }

    @Override
    public void deleteCompatibility(Integer id) { siliconeCoarseUvMapper.deleteCompatibilityById(id); }

    @Override
    public void batchSaveCompatibilities(List<SiliconeCoarseUvCompatibility> compatibilities) {
        if (compatibilities == null || compatibilities.isEmpty()) return;
        for (SiliconeCoarseUvCompatibility comp : compatibilities) {
            if (comp.getProductId() == null) throw new IllegalArgumentException("产品ID不能为空");
            SiliconeCoarseUvProduct product = siliconeCoarseUvMapper.findProductById(comp.getProductId());
            if (product == null) throw new IllegalArgumentException("产品ID " + comp.getProductId() + " 不存在");
            SiliconeCoarseUvCompatibility existing = siliconeCoarseUvMapper.findCompatibilityByUniqueKey(
                comp.getProductId(), comp.getPostProcessingStep());
            if (existing != null) { comp.setId(existing.getId()); siliconeCoarseUvMapper.updateCompatibility(comp); }
            else { siliconeCoarseUvMapper.insertCompatibility(comp); }
        }
    }

    @Override
    public PagedResult<SiliconeCoarseUvProduct> searchProducts(String keyword, String stepName, int page, int size) {
        int offset = (page - 1) * size;
        List<SiliconeCoarseUvProduct> items = siliconeCoarseUvMapper.searchProductsWithStep(keyword, stepName, offset, size);
        Long total = siliconeCoarseUvMapper.countProductsWithStep(keyword, stepName);
        return new PagedResult<>(items, total, size, page);
    }

    @Override
    public List<String> getDistinctSteps() { return siliconeCoarseUvMapper.getDistinctPostProcessingSteps(); }

    @Override
    public SiliconeCoarseUvProductDTO getProductDetail(Integer id) {
        SiliconeCoarseUvProduct product = siliconeCoarseUvMapper.findProductById(id);
        if (product == null) return null;
        List<SiliconeCoarseUvCompatibility> compatibilities = siliconeCoarseUvMapper.findCompatibilitiesByProductId(id);
        SiliconeCoarseUvProductDTO dto = new SiliconeCoarseUvProductDTO();
        dto.setProduct(product);
        dto.setCompatibilities(compatibilities);
        return dto;
    }
}
