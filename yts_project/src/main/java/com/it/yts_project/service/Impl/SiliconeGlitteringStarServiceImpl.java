package com.it.yts_project.service.Impl;

import com.it.yts_project.dto.SiliconeGlitteringStarProductDTO;
import com.it.yts_project.dto.PagedResult;
import com.it.yts_project.mapper.SiliconeGlitteringStarMapper;
import com.it.yts_project.model.SiliconeGlitteringStarProduct;
import com.it.yts_project.model.SiliconeGlitteringStarCompatibility;
import com.it.yts_project.service.SiliconeGlitteringStarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class SiliconeGlitteringStarServiceImpl implements SiliconeGlitteringStarService {

    @Autowired
    private SiliconeGlitteringStarMapper siliconeGlitteringStarMapper;

    @Override
    public List<SiliconeGlitteringStarProduct> getAllProducts() { return siliconeGlitteringStarMapper.findAllProducts(); }

    @Override
    public SiliconeGlitteringStarProduct getProductById(Integer id) { return siliconeGlitteringStarMapper.findProductById(id); }

    @Override
    public List<SiliconeGlitteringStarProduct> searchProducts(String keyword) { return siliconeGlitteringStarMapper.searchProducts(keyword); }

    @Override
    public SiliconeGlitteringStarProduct saveProduct(SiliconeGlitteringStarProduct product) {
        if (product.getId() == null) { siliconeGlitteringStarMapper.insertProduct(product); }
        else { siliconeGlitteringStarMapper.updateProduct(product); }
        return product;
    }

    @Override
    public void deleteProduct(Integer id) { siliconeGlitteringStarMapper.deleteProductById(id); }

    @Override
    public List<SiliconeGlitteringStarCompatibility> getCompatibilitiesByProductId(Integer productId) {
        return siliconeGlitteringStarMapper.findCompatibilitiesByProductId(productId);
    }

    @Override
    public SiliconeGlitteringStarCompatibility getCompatibilityById(Integer id) {
        return siliconeGlitteringStarMapper.findCompatibilityById(id);
    }

    @Override
    public SiliconeGlitteringStarCompatibility saveCompatibility(SiliconeGlitteringStarCompatibility compatibility) {
        if (compatibility.getProductId() == null) throw new IllegalArgumentException("产品ID不能为空");
        SiliconeGlitteringStarProduct product = siliconeGlitteringStarMapper.findProductById(compatibility.getProductId());
        if (product == null) throw new IllegalArgumentException("产品ID " + compatibility.getProductId() + " 不存在");
        if (compatibility.getId() == null) {
            SiliconeGlitteringStarCompatibility existing = siliconeGlitteringStarMapper.findCompatibilityByUniqueKey(
                compatibility.getProductId(), compatibility.getPostProcessingStep());
            if (existing != null) throw new IllegalArgumentException("该产品与后加工工序的兼容性配置已存在");
            siliconeGlitteringStarMapper.insertCompatibility(compatibility);
        } else {
            SiliconeGlitteringStarCompatibility existing = siliconeGlitteringStarMapper.findCompatibilityByUniqueKey(
                compatibility.getProductId(), compatibility.getPostProcessingStep());
            if (existing != null && !existing.getId().equals(compatibility.getId()))
                throw new IllegalArgumentException("该产品与后加工工序的兼容性配置已存在");
            siliconeGlitteringStarMapper.updateCompatibility(compatibility);
        }
        compatibility.setProductName(product.getMaterialName());
        return compatibility;
    }

    @Override
    public void deleteCompatibility(Integer id) { siliconeGlitteringStarMapper.deleteCompatibilityById(id); }

    @Override
    public void batchSaveCompatibilities(List<SiliconeGlitteringStarCompatibility> compatibilities) {
        if (compatibilities == null || compatibilities.isEmpty()) return;
        for (SiliconeGlitteringStarCompatibility comp : compatibilities) {
            if (comp.getProductId() == null) throw new IllegalArgumentException("产品ID不能为空");
            SiliconeGlitteringStarProduct product = siliconeGlitteringStarMapper.findProductById(comp.getProductId());
            if (product == null) throw new IllegalArgumentException("产品ID " + comp.getProductId() + " 不存在");
            SiliconeGlitteringStarCompatibility existing = siliconeGlitteringStarMapper.findCompatibilityByUniqueKey(
                comp.getProductId(), comp.getPostProcessingStep());
            if (existing != null) { comp.setId(existing.getId()); siliconeGlitteringStarMapper.updateCompatibility(comp); }
            else { siliconeGlitteringStarMapper.insertCompatibility(comp); }
        }
    }

    @Override
    public PagedResult<SiliconeGlitteringStarProduct> searchProducts(String keyword, String stepName, int page, int size) {
        int offset = (page - 1) * size;
        List<SiliconeGlitteringStarProduct> items = siliconeGlitteringStarMapper.searchProductsWithStep(keyword, stepName, offset, size);
        Long total = siliconeGlitteringStarMapper.countProductsWithStep(keyword, stepName);
        return new PagedResult<>(items, total, size, page);
    }

    @Override
    public List<String> getDistinctSteps() { return siliconeGlitteringStarMapper.getDistinctPostProcessingSteps(); }

    @Override
    public SiliconeGlitteringStarProductDTO getProductDetail(Integer id) {
        SiliconeGlitteringStarProduct product = siliconeGlitteringStarMapper.findProductById(id);
        if (product == null) return null;
        List<SiliconeGlitteringStarCompatibility> compatibilities = siliconeGlitteringStarMapper.findCompatibilitiesByProductId(id);
        SiliconeGlitteringStarProductDTO dto = new SiliconeGlitteringStarProductDTO();
        dto.setProduct(product);
        dto.setCompatibilities(compatibilities);
        return dto;
    }
}
