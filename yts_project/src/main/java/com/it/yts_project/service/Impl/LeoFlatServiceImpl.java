package com.it.yts_project.service.Impl;

import com.it.yts_project.dto.LeoFlatProductDTO;
import com.it.yts_project.dto.PagedResult;
import com.it.yts_project.mapper.LeoFlatMapper;
import com.it.yts_project.model.LeoFlatProduct;
import com.it.yts_project.model.LeoFlatCompatibility;
import com.it.yts_project.service.LeoFlatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class LeoFlatServiceImpl implements LeoFlatService {

    @Autowired
    private LeoFlatMapper leoFlatMapper;

    @Override
    public List<LeoFlatProduct> getAllProducts() { return leoFlatMapper.findAllProducts(); }

    @Override
    public LeoFlatProduct getProductById(Integer id) { return leoFlatMapper.findProductById(id); }

    @Override
    public List<LeoFlatProduct> searchProducts(String keyword) { return leoFlatMapper.searchProducts(keyword); }

    @Override
    public LeoFlatProduct saveProduct(LeoFlatProduct product) {
        if (product.getId() == null) { leoFlatMapper.insertProduct(product); }
        else { leoFlatMapper.updateProduct(product); }
        return product;
    }

    @Override
    public void deleteProduct(Integer id) { leoFlatMapper.deleteProductById(id); }

    @Override
    public List<LeoFlatCompatibility> getCompatibilitiesByProductId(Integer productId) {
        return leoFlatMapper.findCompatibilitiesByProductId(productId);
    }

    @Override
    public LeoFlatCompatibility getCompatibilityById(Integer id) {
        return leoFlatMapper.findCompatibilityById(id);
    }

    @Override
    public LeoFlatCompatibility saveCompatibility(LeoFlatCompatibility compatibility) {
        if (compatibility.getProductId() == null) throw new IllegalArgumentException("产品ID不能为空");
        LeoFlatProduct product = leoFlatMapper.findProductById(compatibility.getProductId());
        if (product == null) throw new IllegalArgumentException("产品ID " + compatibility.getProductId() + " 不存在");
        if (compatibility.getId() == null) {
            LeoFlatCompatibility existing = leoFlatMapper.findCompatibilityByUniqueKey(
                compatibility.getProductId(), compatibility.getPostProcessingStep());
            if (existing != null) throw new IllegalArgumentException("该产品与后加工工序的兼容性配置已存在");
            leoFlatMapper.insertCompatibility(compatibility);
        } else {
            LeoFlatCompatibility existing = leoFlatMapper.findCompatibilityByUniqueKey(
                compatibility.getProductId(), compatibility.getPostProcessingStep());
            if (existing != null && !existing.getId().equals(compatibility.getId()))
                throw new IllegalArgumentException("该产品与后加工工序的兼容性配置已存在");
            leoFlatMapper.updateCompatibility(compatibility);
        }
        compatibility.setProductName(product.getMaterialName());
        return compatibility;
    }

    @Override
    public void deleteCompatibility(Integer id) { leoFlatMapper.deleteCompatibilityById(id); }

    @Override
    public void batchSaveCompatibilities(List<LeoFlatCompatibility> compatibilities) {
        if (compatibilities == null || compatibilities.isEmpty()) return;
        for (LeoFlatCompatibility comp : compatibilities) {
            if (comp.getProductId() == null) throw new IllegalArgumentException("产品ID不能为空");
            LeoFlatProduct product = leoFlatMapper.findProductById(comp.getProductId());
            if (product == null) throw new IllegalArgumentException("产品ID " + comp.getProductId() + " 不存在");
            LeoFlatCompatibility existing = leoFlatMapper.findCompatibilityByUniqueKey(
                comp.getProductId(), comp.getPostProcessingStep());
            if (existing != null) { comp.setId(existing.getId()); leoFlatMapper.updateCompatibility(comp); }
            else { leoFlatMapper.insertCompatibility(comp); }
        }
    }

    @Override
    public PagedResult<LeoFlatProduct> searchProducts(String keyword, String stepName, int page, int size) {
        int offset = (page - 1) * size;
        List<LeoFlatProduct> items = leoFlatMapper.searchProductsWithStep(keyword, stepName, offset, size);
        Long total = leoFlatMapper.countProductsWithStep(keyword, stepName);
        return new PagedResult<>(items, total, size, page);
    }

    @Override
    public List<String> getDistinctSteps() { return leoFlatMapper.getDistinctPostProcessingSteps(); }

    @Override
    public LeoFlatProductDTO getProductDetail(Integer id) {
        LeoFlatProduct product = leoFlatMapper.findProductById(id);
        if (product == null) return null;
        List<LeoFlatCompatibility> compatibilities = leoFlatMapper.findCompatibilitiesByProductId(id);
        LeoFlatProductDTO dto = new LeoFlatProductDTO();
        dto.setProduct(product);
        dto.setCompatibilities(compatibilities);
        return dto;
    }
}
