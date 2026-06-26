package com.it.yts_project.service.Impl;

import com.it.yts_project.dto.SiliconeSandblastUvProductDTO;
import com.it.yts_project.dto.PagedResult;
import com.it.yts_project.mapper.SiliconeSandblastUvMapper;
import com.it.yts_project.model.SiliconeSandblastUvProduct;
import com.it.yts_project.model.SiliconeSandblastUvCompatibility;
import com.it.yts_project.service.SiliconeSandblastUvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class SiliconeSandblastUvServiceImpl implements SiliconeSandblastUvService {

    @Autowired
    private SiliconeSandblastUvMapper siliconeSandblastUvMapper;

    @Override
    public List<SiliconeSandblastUvProduct> getAllProducts() { return siliconeSandblastUvMapper.findAllProducts(); }

    @Override
    public SiliconeSandblastUvProduct getProductById(Integer id) { return siliconeSandblastUvMapper.findProductById(id); }

    @Override
    public List<SiliconeSandblastUvProduct> searchProducts(String keyword) { return siliconeSandblastUvMapper.searchProducts(keyword); }

    @Override
    public SiliconeSandblastUvProduct saveProduct(SiliconeSandblastUvProduct product) {
        if (product.getId() == null) { siliconeSandblastUvMapper.insertProduct(product); }
        else { siliconeSandblastUvMapper.updateProduct(product); }
        return product;
    }

    @Override
    public void deleteProduct(Integer id) { siliconeSandblastUvMapper.deleteProductById(id); }

    @Override
    public List<SiliconeSandblastUvCompatibility> getCompatibilitiesByProductId(Integer productId) {
        return siliconeSandblastUvMapper.findCompatibilitiesByProductId(productId);
    }

    @Override
    public SiliconeSandblastUvCompatibility getCompatibilityById(Integer id) {
        return siliconeSandblastUvMapper.findCompatibilityById(id);
    }

    @Override
    public SiliconeSandblastUvCompatibility saveCompatibility(SiliconeSandblastUvCompatibility compatibility) {
        if (compatibility.getProductId() == null) throw new IllegalArgumentException("产品ID不能为空");
        SiliconeSandblastUvProduct product = siliconeSandblastUvMapper.findProductById(compatibility.getProductId());
        if (product == null) throw new IllegalArgumentException("产品ID " + compatibility.getProductId() + " 不存在");
        if (compatibility.getId() == null) {
            SiliconeSandblastUvCompatibility existing = siliconeSandblastUvMapper.findCompatibilityByUniqueKey(
                compatibility.getProductId(), compatibility.getPostProcessingStep());
            if (existing != null) throw new IllegalArgumentException("该产品与后加工工序的兼容性配置已存在");
            siliconeSandblastUvMapper.insertCompatibility(compatibility);
        } else {
            SiliconeSandblastUvCompatibility existing = siliconeSandblastUvMapper.findCompatibilityByUniqueKey(
                compatibility.getProductId(), compatibility.getPostProcessingStep());
            if (existing != null && !existing.getId().equals(compatibility.getId()))
                throw new IllegalArgumentException("该产品与后加工工序的兼容性配置已存在");
            siliconeSandblastUvMapper.updateCompatibility(compatibility);
        }
        compatibility.setProductName(product.getMaterialName());
        return compatibility;
    }

    @Override
    public void deleteCompatibility(Integer id) { siliconeSandblastUvMapper.deleteCompatibilityById(id); }

    @Override
    public void batchSaveCompatibilities(List<SiliconeSandblastUvCompatibility> compatibilities) {
        if (compatibilities == null || compatibilities.isEmpty()) return;
        for (SiliconeSandblastUvCompatibility comp : compatibilities) {
            if (comp.getProductId() == null) throw new IllegalArgumentException("产品ID不能为空");
            SiliconeSandblastUvProduct product = siliconeSandblastUvMapper.findProductById(comp.getProductId());
            if (product == null) throw new IllegalArgumentException("产品ID " + comp.getProductId() + " 不存在");
            SiliconeSandblastUvCompatibility existing = siliconeSandblastUvMapper.findCompatibilityByUniqueKey(
                comp.getProductId(), comp.getPostProcessingStep());
            if (existing != null) { comp.setId(existing.getId()); siliconeSandblastUvMapper.updateCompatibility(comp); }
            else { siliconeSandblastUvMapper.insertCompatibility(comp); }
        }
    }

    @Override
    public PagedResult<SiliconeSandblastUvProduct> searchProducts(String keyword, String stepName, int page, int size) {
        int offset = (page - 1) * size;
        List<SiliconeSandblastUvProduct> items = siliconeSandblastUvMapper.searchProductsWithStep(keyword, stepName, offset, size);
        Long total = siliconeSandblastUvMapper.countProductsWithStep(keyword, stepName);
        return new PagedResult<>(items, total, size, page);
    }

    @Override
    public List<String> getDistinctSteps() { return siliconeSandblastUvMapper.getDistinctPostProcessingSteps(); }

    @Override
    public SiliconeSandblastUvProductDTO getProductDetail(Integer id) {
        SiliconeSandblastUvProduct product = siliconeSandblastUvMapper.findProductById(id);
        if (product == null) return null;
        List<SiliconeSandblastUvCompatibility> compatibilities = siliconeSandblastUvMapper.findCompatibilitiesByProductId(id);
        SiliconeSandblastUvProductDTO dto = new SiliconeSandblastUvProductDTO();
        dto.setProduct(product);
        dto.setCompatibilities(compatibilities);
        return dto;
    }
}
