package com.it.yts_project.service.Impl;

import com.it.yts_project.dto.SiliconeMicaPearlProductDTO;
import com.it.yts_project.dto.PagedResult;
import com.it.yts_project.mapper.SiliconeMicaPearlMapper;
import com.it.yts_project.model.SiliconeMicaPearlProduct;
import com.it.yts_project.model.SiliconeMicaPearlCompatibility;
import com.it.yts_project.service.SiliconeMicaPearlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class SiliconeMicaPearlServiceImpl implements SiliconeMicaPearlService {

    @Autowired
    private SiliconeMicaPearlMapper siliconeMicaPearlMapper;

    @Override
    public List<SiliconeMicaPearlProduct> getAllProducts() { return siliconeMicaPearlMapper.findAllProducts(); }

    @Override
    public SiliconeMicaPearlProduct getProductById(Integer id) { return siliconeMicaPearlMapper.findProductById(id); }

    @Override
    public List<SiliconeMicaPearlProduct> searchProducts(String keyword) { return siliconeMicaPearlMapper.searchProducts(keyword); }

    @Override
    public SiliconeMicaPearlProduct saveProduct(SiliconeMicaPearlProduct product) {
        if (product.getId() == null) { siliconeMicaPearlMapper.insertProduct(product); }
        else { siliconeMicaPearlMapper.updateProduct(product); }
        return product;
    }

    @Override
    public void deleteProduct(Integer id) { siliconeMicaPearlMapper.deleteProductById(id); }

    @Override
    public List<SiliconeMicaPearlCompatibility> getCompatibilitiesByProductId(Integer productId) {
        return siliconeMicaPearlMapper.findCompatibilitiesByProductId(productId);
    }

    @Override
    public SiliconeMicaPearlCompatibility getCompatibilityById(Integer id) {
        return siliconeMicaPearlMapper.findCompatibilityById(id);
    }

    @Override
    public SiliconeMicaPearlCompatibility saveCompatibility(SiliconeMicaPearlCompatibility compatibility) {
        if (compatibility.getProductId() == null) throw new IllegalArgumentException("产品ID不能为空");
        SiliconeMicaPearlProduct product = siliconeMicaPearlMapper.findProductById(compatibility.getProductId());
        if (product == null) throw new IllegalArgumentException("产品ID " + compatibility.getProductId() + " 不存在");
        if (compatibility.getId() == null) {
            SiliconeMicaPearlCompatibility existing = siliconeMicaPearlMapper.findCompatibilityByUniqueKey(
                compatibility.getProductId(), compatibility.getPostProcessingStep());
            if (existing != null) throw new IllegalArgumentException("该产品与后加工工序的兼容性配置已存在");
            siliconeMicaPearlMapper.insertCompatibility(compatibility);
        } else {
            SiliconeMicaPearlCompatibility existing = siliconeMicaPearlMapper.findCompatibilityByUniqueKey(
                compatibility.getProductId(), compatibility.getPostProcessingStep());
            if (existing != null && !existing.getId().equals(compatibility.getId()))
                throw new IllegalArgumentException("该产品与后加工工序的兼容性配置已存在");
            siliconeMicaPearlMapper.updateCompatibility(compatibility);
        }
        compatibility.setProductName(product.getMaterialName());
        return compatibility;
    }

    @Override
    public void deleteCompatibility(Integer id) { siliconeMicaPearlMapper.deleteCompatibilityById(id); }

    @Override
    public void batchSaveCompatibilities(List<SiliconeMicaPearlCompatibility> compatibilities) {
        if (compatibilities == null || compatibilities.isEmpty()) return;
        for (SiliconeMicaPearlCompatibility comp : compatibilities) {
            if (comp.getProductId() == null) throw new IllegalArgumentException("产品ID不能为空");
            SiliconeMicaPearlProduct product = siliconeMicaPearlMapper.findProductById(comp.getProductId());
            if (product == null) throw new IllegalArgumentException("产品ID " + comp.getProductId() + " 不存在");
            SiliconeMicaPearlCompatibility existing = siliconeMicaPearlMapper.findCompatibilityByUniqueKey(
                comp.getProductId(), comp.getPostProcessingStep());
            if (existing != null) { comp.setId(existing.getId()); siliconeMicaPearlMapper.updateCompatibility(comp); }
            else { siliconeMicaPearlMapper.insertCompatibility(comp); }
        }
    }

    @Override
    public PagedResult<SiliconeMicaPearlProduct> searchProducts(String keyword, String stepName, int page, int size) {
        int offset = (page - 1) * size;
        List<SiliconeMicaPearlProduct> items = siliconeMicaPearlMapper.searchProductsWithStep(keyword, stepName, offset, size);
        Long total = siliconeMicaPearlMapper.countProductsWithStep(keyword, stepName);
        return new PagedResult<>(items, total, size, page);
    }

    @Override
    public List<String> getDistinctSteps() { return siliconeMicaPearlMapper.getDistinctPostProcessingSteps(); }

    @Override
    public SiliconeMicaPearlProductDTO getProductDetail(Integer id) {
        SiliconeMicaPearlProduct product = siliconeMicaPearlMapper.findProductById(id);
        if (product == null) return null;
        List<SiliconeMicaPearlCompatibility> compatibilities = siliconeMicaPearlMapper.findCompatibilitiesByProductId(id);
        SiliconeMicaPearlProductDTO dto = new SiliconeMicaPearlProductDTO();
        dto.setProduct(product);
        dto.setCompatibilities(compatibilities);
        return dto;
    }
}
