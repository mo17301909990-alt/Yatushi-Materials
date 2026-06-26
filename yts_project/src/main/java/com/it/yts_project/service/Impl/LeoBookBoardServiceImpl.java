package com.it.yts_project.service.Impl;

import com.it.yts_project.dto.LeoBookBoardProductDTO;
import com.it.yts_project.dto.PagedResult;
import com.it.yts_project.mapper.LeoBookBoardMapper;
import com.it.yts_project.model.LeoBookBoardProduct;
import com.it.yts_project.model.LeoBookBoardCompatibility;
import com.it.yts_project.service.LeoBookBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class LeoBookBoardServiceImpl implements LeoBookBoardService {

    @Autowired
    private LeoBookBoardMapper leoBookBoardMapper;

    @Override
    public List<LeoBookBoardProduct> getAllProducts() { return leoBookBoardMapper.findAllProducts(); }

    @Override
    public LeoBookBoardProduct getProductById(Integer id) { return leoBookBoardMapper.findProductById(id); }

    @Override
    public List<LeoBookBoardProduct> searchProducts(String keyword) { return leoBookBoardMapper.searchProducts(keyword); }

    @Override
    public LeoBookBoardProduct saveProduct(LeoBookBoardProduct product) {
        if (product.getId() == null) { leoBookBoardMapper.insertProduct(product); }
        else { leoBookBoardMapper.updateProduct(product); }
        return product;
    }

    @Override
    public void deleteProduct(Integer id) { leoBookBoardMapper.deleteProductById(id); }

    @Override
    public List<LeoBookBoardCompatibility> getCompatibilitiesByProductId(Integer productId) {
        return leoBookBoardMapper.findCompatibilitiesByProductId(productId);
    }

    @Override
    public LeoBookBoardCompatibility getCompatibilityById(Integer id) {
        return leoBookBoardMapper.findCompatibilityById(id);
    }

    @Override
    public LeoBookBoardCompatibility saveCompatibility(LeoBookBoardCompatibility compatibility) {
        if (compatibility.getProductId() == null) throw new IllegalArgumentException("产品ID不能为空");
        LeoBookBoardProduct product = leoBookBoardMapper.findProductById(compatibility.getProductId());
        if (product == null) throw new IllegalArgumentException("产品ID " + compatibility.getProductId() + " 不存在");
        if (compatibility.getId() == null) {
            LeoBookBoardCompatibility existing = leoBookBoardMapper.findCompatibilityByUniqueKey(
                compatibility.getProductId(), compatibility.getPostProcessingStep());
            if (existing != null) throw new IllegalArgumentException("该产品与后加工工序的兼容性配置已存在");
            leoBookBoardMapper.insertCompatibility(compatibility);
        } else {
            LeoBookBoardCompatibility existing = leoBookBoardMapper.findCompatibilityByUniqueKey(
                compatibility.getProductId(), compatibility.getPostProcessingStep());
            if (existing != null && !existing.getId().equals(compatibility.getId()))
                throw new IllegalArgumentException("该产品与后加工工序的兼容性配置已存在");
            leoBookBoardMapper.updateCompatibility(compatibility);
        }
        compatibility.setProductName(product.getMaterialName());
        return compatibility;
    }

    @Override
    public void deleteCompatibility(Integer id) { leoBookBoardMapper.deleteCompatibilityById(id); }

    @Override
    public void batchSaveCompatibilities(List<LeoBookBoardCompatibility> compatibilities) {
        if (compatibilities == null || compatibilities.isEmpty()) return;
        for (LeoBookBoardCompatibility comp : compatibilities) {
            if (comp.getProductId() == null) throw new IllegalArgumentException("产品ID不能为空");
            LeoBookBoardProduct product = leoBookBoardMapper.findProductById(comp.getProductId());
            if (product == null) throw new IllegalArgumentException("产品ID " + comp.getProductId() + " 不存在");
            LeoBookBoardCompatibility existing = leoBookBoardMapper.findCompatibilityByUniqueKey(
                comp.getProductId(), comp.getPostProcessingStep());
            if (existing != null) { comp.setId(existing.getId()); leoBookBoardMapper.updateCompatibility(comp); }
            else { leoBookBoardMapper.insertCompatibility(comp); }
        }
    }

    @Override
    public PagedResult<LeoBookBoardProduct> searchProducts(String keyword, String stepName, int page, int size) {
        int offset = (page - 1) * size;
        List<LeoBookBoardProduct> items = leoBookBoardMapper.searchProductsWithStep(keyword, stepName, offset, size);
        Long total = leoBookBoardMapper.countProductsWithStep(keyword, stepName);
        return new PagedResult<>(items, total, size, page);
    }

    @Override
    public List<String> getDistinctSteps() { return leoBookBoardMapper.getDistinctPostProcessingSteps(); }

    @Override
    public LeoBookBoardProductDTO getProductDetail(Integer id) {
        LeoBookBoardProduct product = leoBookBoardMapper.findProductById(id);
        if (product == null) return null;
        List<LeoBookBoardCompatibility> compatibilities = leoBookBoardMapper.findCompatibilitiesByProductId(id);
        LeoBookBoardProductDTO dto = new LeoBookBoardProductDTO();
        dto.setProduct(product);
        dto.setCompatibilities(compatibilities);
        return dto;
    }
}
