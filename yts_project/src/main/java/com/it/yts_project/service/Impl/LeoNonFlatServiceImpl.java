package com.it.yts_project.service.Impl;

import com.it.yts_project.dto.LeoNonFlatProductDTO;
import com.it.yts_project.dto.PagedResult;
import com.it.yts_project.mapper.LeoNonFlatMapper;
import com.it.yts_project.model.LeoNonFlatProduct;
import com.it.yts_project.model.LeoNonFlatCompatibility;
import com.it.yts_project.service.LeoNonFlatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class LeoNonFlatServiceImpl implements LeoNonFlatService {

    @Autowired
    private LeoNonFlatMapper leoNonFlatMapper;

    @Override
    public List<LeoNonFlatProduct> getAllProducts() { return leoNonFlatMapper.findAllProducts(); }

    @Override
    public LeoNonFlatProduct getProductById(Integer id) { return leoNonFlatMapper.findProductById(id); }

    @Override
    public List<LeoNonFlatProduct> searchProducts(String keyword) { return leoNonFlatMapper.searchProducts(keyword); }

    @Override
    public LeoNonFlatProduct saveProduct(LeoNonFlatProduct product) {
        if (product.getId() == null) { leoNonFlatMapper.insertProduct(product); }
        else { leoNonFlatMapper.updateProduct(product); }
        return product;
    }

    @Override
    public void deleteProduct(Integer id) { leoNonFlatMapper.deleteProductById(id); }

    @Override
    public List<LeoNonFlatCompatibility> getCompatibilitiesByProductId(Integer productId) {
        return leoNonFlatMapper.findCompatibilitiesByProductId(productId);
    }

    @Override
    public LeoNonFlatCompatibility getCompatibilityById(Integer id) {
        return leoNonFlatMapper.findCompatibilityById(id);
    }

    @Override
    public LeoNonFlatCompatibility saveCompatibility(LeoNonFlatCompatibility compatibility) {
        if (compatibility.getProductId() == null) throw new IllegalArgumentException("产品ID不能为空");
        LeoNonFlatProduct product = leoNonFlatMapper.findProductById(compatibility.getProductId());
        if (product == null) throw new IllegalArgumentException("产品ID " + compatibility.getProductId() + " 不存在");
        if (compatibility.getId() == null) {
            LeoNonFlatCompatibility existing = leoNonFlatMapper.findCompatibilityByUniqueKey(
                compatibility.getProductId(), compatibility.getPostProcessingStep());
            if (existing != null) throw new IllegalArgumentException("该产品与后加工工序的兼容性配置已存在");
            leoNonFlatMapper.insertCompatibility(compatibility);
        } else {
            LeoNonFlatCompatibility existing = leoNonFlatMapper.findCompatibilityByUniqueKey(
                compatibility.getProductId(), compatibility.getPostProcessingStep());
            if (existing != null && !existing.getId().equals(compatibility.getId()))
                throw new IllegalArgumentException("该产品与后加工工序的兼容性配置已存在");
            leoNonFlatMapper.updateCompatibility(compatibility);
        }
        compatibility.setProductName(product.getMaterialName());
        return compatibility;
    }

    @Override
    public void deleteCompatibility(Integer id) { leoNonFlatMapper.deleteCompatibilityById(id); }

    @Override
    public void batchSaveCompatibilities(List<LeoNonFlatCompatibility> compatibilities) {
        if (compatibilities == null || compatibilities.isEmpty()) return;
        for (LeoNonFlatCompatibility comp : compatibilities) {
            if (comp.getProductId() == null) throw new IllegalArgumentException("产品ID不能为空");
            LeoNonFlatProduct product = leoNonFlatMapper.findProductById(comp.getProductId());
            if (product == null) throw new IllegalArgumentException("产品ID " + comp.getProductId() + " 不存在");
            LeoNonFlatCompatibility existing = leoNonFlatMapper.findCompatibilityByUniqueKey(
                comp.getProductId(), comp.getPostProcessingStep());
            if (existing != null) { comp.setId(existing.getId()); leoNonFlatMapper.updateCompatibility(comp); }
            else { leoNonFlatMapper.insertCompatibility(comp); }
        }
    }

    @Override
    public PagedResult<LeoNonFlatProduct> searchProducts(String keyword, String stepName, int page, int size) {
        int offset = (page - 1) * size;
        List<LeoNonFlatProduct> items = leoNonFlatMapper.searchProductsWithStep(keyword, stepName, offset, size);
        Long total = leoNonFlatMapper.countProductsWithStep(keyword, stepName);
        return new PagedResult<>(items, total, size, page);
    }

    @Override
    public List<String> getDistinctSteps() { return leoNonFlatMapper.getDistinctPostProcessingSteps(); }

    @Override
    public LeoNonFlatProductDTO getProductDetail(Integer id) {
        LeoNonFlatProduct product = leoNonFlatMapper.findProductById(id);
        if (product == null) return null;
        List<LeoNonFlatCompatibility> compatibilities = leoNonFlatMapper.findCompatibilitiesByProductId(id);
        LeoNonFlatProductDTO dto = new LeoNonFlatProductDTO();
        dto.setProduct(product);
        dto.setCompatibilities(compatibilities);
        return dto;
    }
}
