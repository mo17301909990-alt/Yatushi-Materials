package com.it.yts_project.service.Impl;

import com.it.yts_project.dto.SiliconeWhiteUvProductDTO;
import com.it.yts_project.dto.PagedResult;
import com.it.yts_project.mapper.SiliconeWhiteUvMapper;
import com.it.yts_project.model.SiliconeWhiteUvProduct;
import com.it.yts_project.model.SiliconeWhiteUvCompatibility;
import com.it.yts_project.service.SiliconeWhiteUvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class SiliconeWhiteUvServiceImpl implements SiliconeWhiteUvService {

    @Autowired
    private SiliconeWhiteUvMapper siliconeWhiteUvMapper;

    @Override
    public List<SiliconeWhiteUvProduct> getAllProducts() { return siliconeWhiteUvMapper.findAllProducts(); }

    @Override
    public SiliconeWhiteUvProduct getProductById(Integer id) { return siliconeWhiteUvMapper.findProductById(id); }

    @Override
    public List<SiliconeWhiteUvProduct> searchProducts(String keyword) { return siliconeWhiteUvMapper.searchProducts(keyword); }

    @Override
    public SiliconeWhiteUvProduct saveProduct(SiliconeWhiteUvProduct product) {
        if (product.getId() == null) { siliconeWhiteUvMapper.insertProduct(product); }
        else { siliconeWhiteUvMapper.updateProduct(product); }
        return product;
    }

    @Override
    public void deleteProduct(Integer id) { siliconeWhiteUvMapper.deleteProductById(id); }

    @Override
    public List<SiliconeWhiteUvCompatibility> getCompatibilitiesByProductId(Integer productId) {
        return siliconeWhiteUvMapper.findCompatibilitiesByProductId(productId);
    }

    @Override
    public SiliconeWhiteUvCompatibility getCompatibilityById(Integer id) {
        return siliconeWhiteUvMapper.findCompatibilityById(id);
    }

    @Override
    public SiliconeWhiteUvCompatibility saveCompatibility(SiliconeWhiteUvCompatibility compatibility) {
        if (compatibility.getProductId() == null) throw new IllegalArgumentException("产品ID不能为空");
        SiliconeWhiteUvProduct product = siliconeWhiteUvMapper.findProductById(compatibility.getProductId());
        if (product == null) throw new IllegalArgumentException("产品ID " + compatibility.getProductId() + " 不存在");
        if (compatibility.getId() == null) {
            SiliconeWhiteUvCompatibility existing = siliconeWhiteUvMapper.findCompatibilityByUniqueKey(
                compatibility.getProductId(), compatibility.getPostProcessingStep());
            if (existing != null) throw new IllegalArgumentException("该产品与后加工工序的兼容性配置已存在");
            siliconeWhiteUvMapper.insertCompatibility(compatibility);
        } else {
            SiliconeWhiteUvCompatibility existing = siliconeWhiteUvMapper.findCompatibilityByUniqueKey(
                compatibility.getProductId(), compatibility.getPostProcessingStep());
            if (existing != null && !existing.getId().equals(compatibility.getId()))
                throw new IllegalArgumentException("该产品与后加工工序的兼容性配置已存在");
            siliconeWhiteUvMapper.updateCompatibility(compatibility);
        }
        compatibility.setProductName(product.getMaterialName());
        return compatibility;
    }

    @Override
    public void deleteCompatibility(Integer id) { siliconeWhiteUvMapper.deleteCompatibilityById(id); }

    @Override
    public void batchSaveCompatibilities(List<SiliconeWhiteUvCompatibility> compatibilities) {
        if (compatibilities == null || compatibilities.isEmpty()) return;
        for (SiliconeWhiteUvCompatibility comp : compatibilities) {
            if (comp.getProductId() == null) throw new IllegalArgumentException("产品ID不能为空");
            SiliconeWhiteUvProduct product = siliconeWhiteUvMapper.findProductById(comp.getProductId());
            if (product == null) throw new IllegalArgumentException("产品ID " + comp.getProductId() + " 不存在");
            SiliconeWhiteUvCompatibility existing = siliconeWhiteUvMapper.findCompatibilityByUniqueKey(
                comp.getProductId(), comp.getPostProcessingStep());
            if (existing != null) { comp.setId(existing.getId()); siliconeWhiteUvMapper.updateCompatibility(comp); }
            else { siliconeWhiteUvMapper.insertCompatibility(comp); }
        }
    }

    @Override
    public PagedResult<SiliconeWhiteUvProduct> searchProducts(String keyword, String stepName, int page, int size) {
        int offset = (page - 1) * size;
        List<SiliconeWhiteUvProduct> items = siliconeWhiteUvMapper.searchProductsWithStep(keyword, stepName, offset, size);
        Long total = siliconeWhiteUvMapper.countProductsWithStep(keyword, stepName);
        return new PagedResult<>(items, total, size, page);
    }

    @Override
    public List<String> getDistinctSteps() { return siliconeWhiteUvMapper.getDistinctPostProcessingSteps(); }

    @Override
    public SiliconeWhiteUvProductDTO getProductDetail(Integer id) {
        SiliconeWhiteUvProduct product = siliconeWhiteUvMapper.findProductById(id);
        if (product == null) return null;
        List<SiliconeWhiteUvCompatibility> compatibilities = siliconeWhiteUvMapper.findCompatibilitiesByProductId(id);
        SiliconeWhiteUvProductDTO dto = new SiliconeWhiteUvProductDTO();
        dto.setProduct(product);
        dto.setCompatibilities(compatibilities);
        return dto;
    }
}
