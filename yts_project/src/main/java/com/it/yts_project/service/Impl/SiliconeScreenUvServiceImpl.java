package com.it.yts_project.service.Impl;

import com.it.yts_project.dto.SiliconeScreenUvProductDTO;
import com.it.yts_project.dto.PagedResult;
import com.it.yts_project.mapper.SiliconeScreenUvMapper;
import com.it.yts_project.model.SiliconeScreenUvProduct;
import com.it.yts_project.model.SiliconeScreenUvCompatibility;
import com.it.yts_project.service.SiliconeScreenUvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class SiliconeScreenUvServiceImpl implements SiliconeScreenUvService {

    @Autowired
    private SiliconeScreenUvMapper siliconeScreenUvMapper;

    @Override
    public List<SiliconeScreenUvProduct> getAllProducts() { return siliconeScreenUvMapper.findAllProducts(); }

    @Override
    public SiliconeScreenUvProduct getProductById(Integer id) { return siliconeScreenUvMapper.findProductById(id); }

    @Override
    public List<SiliconeScreenUvProduct> searchProducts(String keyword) { return siliconeScreenUvMapper.searchProducts(keyword); }

    @Override
    public SiliconeScreenUvProduct saveProduct(SiliconeScreenUvProduct product) {
        if (product.getId() == null) { siliconeScreenUvMapper.insertProduct(product); }
        else { siliconeScreenUvMapper.updateProduct(product); }
        return product;
    }

    @Override
    public void deleteProduct(Integer id) { siliconeScreenUvMapper.deleteProductById(id); }

    @Override
    public List<SiliconeScreenUvCompatibility> getCompatibilitiesByProductId(Integer productId) {
        return siliconeScreenUvMapper.findCompatibilitiesByProductId(productId);
    }

    @Override
    public SiliconeScreenUvCompatibility getCompatibilityById(Integer id) {
        return siliconeScreenUvMapper.findCompatibilityById(id);
    }

    @Override
    public SiliconeScreenUvCompatibility saveCompatibility(SiliconeScreenUvCompatibility compatibility) {
        if (compatibility.getProductId() == null) throw new IllegalArgumentException("产品ID不能为空");
        SiliconeScreenUvProduct product = siliconeScreenUvMapper.findProductById(compatibility.getProductId());
        if (product == null) throw new IllegalArgumentException("产品ID " + compatibility.getProductId() + " 不存在");
        if (compatibility.getId() == null) {
            SiliconeScreenUvCompatibility existing = siliconeScreenUvMapper.findCompatibilityByUniqueKey(
                compatibility.getProductId(), compatibility.getPostProcessingStep());
            if (existing != null) throw new IllegalArgumentException("该产品与后加工工序的兼容性配置已存在");
            siliconeScreenUvMapper.insertCompatibility(compatibility);
        } else {
            SiliconeScreenUvCompatibility existing = siliconeScreenUvMapper.findCompatibilityByUniqueKey(
                compatibility.getProductId(), compatibility.getPostProcessingStep());
            if (existing != null && !existing.getId().equals(compatibility.getId()))
                throw new IllegalArgumentException("该产品与后加工工序的兼容性配置已存在");
            siliconeScreenUvMapper.updateCompatibility(compatibility);
        }
        compatibility.setProductName(product.getMaterialName());
        return compatibility;
    }

    @Override
    public void deleteCompatibility(Integer id) { siliconeScreenUvMapper.deleteCompatibilityById(id); }

    @Override
    public void batchSaveCompatibilities(List<SiliconeScreenUvCompatibility> compatibilities) {
        if (compatibilities == null || compatibilities.isEmpty()) return;
        for (SiliconeScreenUvCompatibility comp : compatibilities) {
            if (comp.getProductId() == null) throw new IllegalArgumentException("产品ID不能为空");
            SiliconeScreenUvProduct product = siliconeScreenUvMapper.findProductById(comp.getProductId());
            if (product == null) throw new IllegalArgumentException("产品ID " + comp.getProductId() + " 不存在");
            SiliconeScreenUvCompatibility existing = siliconeScreenUvMapper.findCompatibilityByUniqueKey(
                comp.getProductId(), comp.getPostProcessingStep());
            if (existing != null) { comp.setId(existing.getId()); siliconeScreenUvMapper.updateCompatibility(comp); }
            else { siliconeScreenUvMapper.insertCompatibility(comp); }
        }
    }

    @Override
    public PagedResult<SiliconeScreenUvProduct> searchProducts(String keyword, String stepName, int page, int size) {
        int offset = (page - 1) * size;
        List<SiliconeScreenUvProduct> items = siliconeScreenUvMapper.searchProductsWithStep(keyword, stepName, offset, size);
        Long total = siliconeScreenUvMapper.countProductsWithStep(keyword, stepName);
        return new PagedResult<>(items, total, size, page);
    }

    @Override
    public List<String> getDistinctSteps() { return siliconeScreenUvMapper.getDistinctPostProcessingSteps(); }

    @Override
    public SiliconeScreenUvProductDTO getProductDetail(Integer id) {
        SiliconeScreenUvProduct product = siliconeScreenUvMapper.findProductById(id);
        if (product == null) return null;
        List<SiliconeScreenUvCompatibility> compatibilities = siliconeScreenUvMapper.findCompatibilitiesByProductId(id);
        SiliconeScreenUvProductDTO dto = new SiliconeScreenUvProductDTO();
        dto.setProduct(product);
        dto.setCompatibilities(compatibilities);
        return dto;
    }
}
