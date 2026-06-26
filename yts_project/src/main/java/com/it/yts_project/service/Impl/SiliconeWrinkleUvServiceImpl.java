package com.it.yts_project.service.Impl;

import com.it.yts_project.dto.SiliconeWrinkleUvProductDTO;
import com.it.yts_project.dto.PagedResult;
import com.it.yts_project.mapper.SiliconeWrinkleUvMapper;
import com.it.yts_project.model.SiliconeWrinkleUvProduct;
import com.it.yts_project.model.SiliconeWrinkleUvCompatibility;
import com.it.yts_project.service.SiliconeWrinkleUvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class SiliconeWrinkleUvServiceImpl implements SiliconeWrinkleUvService {

    @Autowired
    private SiliconeWrinkleUvMapper siliconeWrinkleUvMapper;

    @Override
    public List<SiliconeWrinkleUvProduct> getAllProducts() { return siliconeWrinkleUvMapper.findAllProducts(); }

    @Override
    public SiliconeWrinkleUvProduct getProductById(Integer id) { return siliconeWrinkleUvMapper.findProductById(id); }

    @Override
    public List<SiliconeWrinkleUvProduct> searchProducts(String keyword) { return siliconeWrinkleUvMapper.searchProducts(keyword); }

    @Override
    public SiliconeWrinkleUvProduct saveProduct(SiliconeWrinkleUvProduct product) {
        if (product.getId() == null) { siliconeWrinkleUvMapper.insertProduct(product); }
        else { siliconeWrinkleUvMapper.updateProduct(product); }
        return product;
    }

    @Override
    public void deleteProduct(Integer id) { siliconeWrinkleUvMapper.deleteProductById(id); }

    @Override
    public List<SiliconeWrinkleUvCompatibility> getCompatibilitiesByProductId(Integer productId) {
        return siliconeWrinkleUvMapper.findCompatibilitiesByProductId(productId);
    }

    @Override
    public SiliconeWrinkleUvCompatibility getCompatibilityById(Integer id) {
        return siliconeWrinkleUvMapper.findCompatibilityById(id);
    }

    @Override
    public SiliconeWrinkleUvCompatibility saveCompatibility(SiliconeWrinkleUvCompatibility compatibility) {
        if (compatibility.getProductId() == null) throw new IllegalArgumentException("产品ID不能为空");
        SiliconeWrinkleUvProduct product = siliconeWrinkleUvMapper.findProductById(compatibility.getProductId());
        if (product == null) throw new IllegalArgumentException("产品ID " + compatibility.getProductId() + " 不存在");
        if (compatibility.getId() == null) {
            SiliconeWrinkleUvCompatibility existing = siliconeWrinkleUvMapper.findCompatibilityByUniqueKey(
                compatibility.getProductId(), compatibility.getPostProcessingStep());
            if (existing != null) throw new IllegalArgumentException("该产品与后加工工序的兼容性配置已存在");
            siliconeWrinkleUvMapper.insertCompatibility(compatibility);
        } else {
            SiliconeWrinkleUvCompatibility existing = siliconeWrinkleUvMapper.findCompatibilityByUniqueKey(
                compatibility.getProductId(), compatibility.getPostProcessingStep());
            if (existing != null && !existing.getId().equals(compatibility.getId()))
                throw new IllegalArgumentException("该产品与后加工工序的兼容性配置已存在");
            siliconeWrinkleUvMapper.updateCompatibility(compatibility);
        }
        compatibility.setProductName(product.getMaterialName());
        return compatibility;
    }

    @Override
    public void deleteCompatibility(Integer id) { siliconeWrinkleUvMapper.deleteCompatibilityById(id); }

    @Override
    public void batchSaveCompatibilities(List<SiliconeWrinkleUvCompatibility> compatibilities) {
        if (compatibilities == null || compatibilities.isEmpty()) return;
        for (SiliconeWrinkleUvCompatibility comp : compatibilities) {
            if (comp.getProductId() == null) throw new IllegalArgumentException("产品ID不能为空");
            SiliconeWrinkleUvProduct product = siliconeWrinkleUvMapper.findProductById(comp.getProductId());
            if (product == null) throw new IllegalArgumentException("产品ID " + comp.getProductId() + " 不存在");
            SiliconeWrinkleUvCompatibility existing = siliconeWrinkleUvMapper.findCompatibilityByUniqueKey(
                comp.getProductId(), comp.getPostProcessingStep());
            if (existing != null) { comp.setId(existing.getId()); siliconeWrinkleUvMapper.updateCompatibility(comp); }
            else { siliconeWrinkleUvMapper.insertCompatibility(comp); }
        }
    }

    @Override
    public PagedResult<SiliconeWrinkleUvProduct> searchProducts(String keyword, String stepName, int page, int size) {
        int offset = (page - 1) * size;
        List<SiliconeWrinkleUvProduct> items = siliconeWrinkleUvMapper.searchProductsWithStep(keyword, stepName, offset, size);
        Long total = siliconeWrinkleUvMapper.countProductsWithStep(keyword, stepName);
        return new PagedResult<>(items, total, size, page);
    }

    @Override
    public List<String> getDistinctSteps() { return siliconeWrinkleUvMapper.getDistinctPostProcessingSteps(); }

    @Override
    public SiliconeWrinkleUvProductDTO getProductDetail(Integer id) {
        SiliconeWrinkleUvProduct product = siliconeWrinkleUvMapper.findProductById(id);
        if (product == null) return null;
        List<SiliconeWrinkleUvCompatibility> compatibilities = siliconeWrinkleUvMapper.findCompatibilitiesByProductId(id);
        SiliconeWrinkleUvProductDTO dto = new SiliconeWrinkleUvProductDTO();
        dto.setProduct(product);
        dto.setCompatibilities(compatibilities);
        return dto;
    }
}
