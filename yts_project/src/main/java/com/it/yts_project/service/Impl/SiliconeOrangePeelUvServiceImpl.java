package com.it.yts_project.service.Impl;

import com.it.yts_project.dto.SiliconeOrangePeelUvProductDTO;
import com.it.yts_project.dto.PagedResult;
import com.it.yts_project.mapper.SiliconeOrangePeelUvMapper;
import com.it.yts_project.model.SiliconeOrangePeelUvProduct;
import com.it.yts_project.model.SiliconeOrangePeelUvCompatibility;
import com.it.yts_project.service.SiliconeOrangePeelUvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class SiliconeOrangePeelUvServiceImpl implements SiliconeOrangePeelUvService {

    @Autowired
    private SiliconeOrangePeelUvMapper siliconeOrangePeelUvMapper;

    @Override
    public List<SiliconeOrangePeelUvProduct> getAllProducts() { return siliconeOrangePeelUvMapper.findAllProducts(); }

    @Override
    public SiliconeOrangePeelUvProduct getProductById(Integer id) { return siliconeOrangePeelUvMapper.findProductById(id); }

    @Override
    public List<SiliconeOrangePeelUvProduct> searchProducts(String keyword) { return siliconeOrangePeelUvMapper.searchProducts(keyword); }

    @Override
    public SiliconeOrangePeelUvProduct saveProduct(SiliconeOrangePeelUvProduct product) {
        if (product.getId() == null) { siliconeOrangePeelUvMapper.insertProduct(product); }
        else { siliconeOrangePeelUvMapper.updateProduct(product); }
        return product;
    }

    @Override
    public void deleteProduct(Integer id) { siliconeOrangePeelUvMapper.deleteProductById(id); }

    @Override
    public List<SiliconeOrangePeelUvCompatibility> getCompatibilitiesByProductId(Integer productId) {
        return siliconeOrangePeelUvMapper.findCompatibilitiesByProductId(productId);
    }

    @Override
    public SiliconeOrangePeelUvCompatibility getCompatibilityById(Integer id) {
        return siliconeOrangePeelUvMapper.findCompatibilityById(id);
    }

    @Override
    public SiliconeOrangePeelUvCompatibility saveCompatibility(SiliconeOrangePeelUvCompatibility compatibility) {
        if (compatibility.getProductId() == null) throw new IllegalArgumentException("产品ID不能为空");
        SiliconeOrangePeelUvProduct product = siliconeOrangePeelUvMapper.findProductById(compatibility.getProductId());
        if (product == null) throw new IllegalArgumentException("产品ID " + compatibility.getProductId() + " 不存在");
        if (compatibility.getId() == null) {
            SiliconeOrangePeelUvCompatibility existing = siliconeOrangePeelUvMapper.findCompatibilityByUniqueKey(
                compatibility.getProductId(), compatibility.getPostProcessingStep());
            if (existing != null) throw new IllegalArgumentException("该产品与后加工工序的兼容性配置已存在");
            siliconeOrangePeelUvMapper.insertCompatibility(compatibility);
        } else {
            SiliconeOrangePeelUvCompatibility existing = siliconeOrangePeelUvMapper.findCompatibilityByUniqueKey(
                compatibility.getProductId(), compatibility.getPostProcessingStep());
            if (existing != null && !existing.getId().equals(compatibility.getId()))
                throw new IllegalArgumentException("该产品与后加工工序的兼容性配置已存在");
            siliconeOrangePeelUvMapper.updateCompatibility(compatibility);
        }
        compatibility.setProductName(product.getMaterialName());
        return compatibility;
    }

    @Override
    public void deleteCompatibility(Integer id) { siliconeOrangePeelUvMapper.deleteCompatibilityById(id); }

    @Override
    public void batchSaveCompatibilities(List<SiliconeOrangePeelUvCompatibility> compatibilities) {
        if (compatibilities == null || compatibilities.isEmpty()) return;
        for (SiliconeOrangePeelUvCompatibility comp : compatibilities) {
            if (comp.getProductId() == null) throw new IllegalArgumentException("产品ID不能为空");
            SiliconeOrangePeelUvProduct product = siliconeOrangePeelUvMapper.findProductById(comp.getProductId());
            if (product == null) throw new IllegalArgumentException("产品ID " + comp.getProductId() + " 不存在");
            SiliconeOrangePeelUvCompatibility existing = siliconeOrangePeelUvMapper.findCompatibilityByUniqueKey(
                comp.getProductId(), comp.getPostProcessingStep());
            if (existing != null) { comp.setId(existing.getId()); siliconeOrangePeelUvMapper.updateCompatibility(comp); }
            else { siliconeOrangePeelUvMapper.insertCompatibility(comp); }
        }
    }

    @Override
    public PagedResult<SiliconeOrangePeelUvProduct> searchProducts(String keyword, String stepName, int page, int size) {
        int offset = (page - 1) * size;
        List<SiliconeOrangePeelUvProduct> items = siliconeOrangePeelUvMapper.searchProductsWithStep(keyword, stepName, offset, size);
        Long total = siliconeOrangePeelUvMapper.countProductsWithStep(keyword, stepName);
        return new PagedResult<>(items, total, size, page);
    }

    @Override
    public List<String> getDistinctSteps() { return siliconeOrangePeelUvMapper.getDistinctPostProcessingSteps(); }

    @Override
    public SiliconeOrangePeelUvProductDTO getProductDetail(Integer id) {
        SiliconeOrangePeelUvProduct product = siliconeOrangePeelUvMapper.findProductById(id);
        if (product == null) return null;
        List<SiliconeOrangePeelUvCompatibility> compatibilities = siliconeOrangePeelUvMapper.findCompatibilitiesByProductId(id);
        SiliconeOrangePeelUvProductDTO dto = new SiliconeOrangePeelUvProductDTO();
        dto.setProduct(product);
        dto.setCompatibilities(compatibilities);
        return dto;
    }
}
