package com.it.yts_project.controller;

import com.it.yts_project.mapper.ProductsMapper;
import com.it.yts_project.model.Products;
import com.it.yts_project.util.ExcelExportUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 产品控制器
 */
@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = {
    "http://localhost:5173",
    "http://120.26.101.0",
    "http://120.26.101.0:80",
    "http://120.26.101.0:8080",
    "http://120.26.101.0:3000"
}, allowCredentials = "true")
public class ProductsController {
    
    @Autowired
    private ProductsMapper productsMapper;
    
    /**
     * 获取所有产品系列名称
     * @return 产品系列名称列表
     */
    @GetMapping("/series-names")
    public ResponseEntity<List<String>> getAllSeriesNames() {
        try {
            List<String> seriesNames = productsMapper.getAllSeriesNames();
            
            // 如果没有数据，返回空列表
            if (seriesNames == null || seriesNames.isEmpty()) {
                seriesNames = new ArrayList<>();
            }
            
            return ResponseEntity.ok(seriesNames);
        } catch (Exception e) {
            // 发生异常时返回空列表，不返回模拟数据
            return ResponseEntity.ok(new ArrayList<>());
        }
    }
    
    /**
     * 根据产品系列名称获取物料类型
     * @param seriesName 产品系列名称
     * @return 物料类型列表
     */
    @GetMapping("/paper-types/{seriesName}")
    public ResponseEntity<List<String>> getPaperTypesBySeriesName(@PathVariable String seriesName) {
        try {
            // 首先尝试从cloth_paper_types表获取
            List<String> paperTypes = productsMapper.getPaperTypesBySeriesName(seriesName);
            
            // 如果没有数据，返回空列表
            if (paperTypes == null || paperTypes.isEmpty()) {
                return ResponseEntity.ok(new ArrayList<>());
            }
            
            return ResponseEntity.ok(paperTypes);
        } catch (Exception e) {
            // 发生异常时返回空列表
            return ResponseEntity.ok(new ArrayList<>());
        }
    }
    
    /**
     * 根据ID获取产品
     * @param id 产品ID
     * @return 产品信息
     */
    @GetMapping("/{id}")
    public ResponseEntity<Products> getProductById(@PathVariable Integer id) {
        try {
            Products product = productsMapper.getProductById(id);
            if (product != null) {
                return ResponseEntity.ok(product);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * 根据产品系列名称获取产品列表
     * @param seriesName 产品系列名称
     * @return 产品列表
     */
    @GetMapping("/series/{seriesName}")
    public ResponseEntity<List<Products>> getProductsBySeriesName(@PathVariable String seriesName) {
        try {
            List<Products> products = productsMapper.getProductsBySeriesName(seriesName);
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * 根据烫金纸性能类型获取支持的产品系列名称列表
     * @param paperType 烫金纸性能类型
     * @return 产品系列名称列表
     */
    @GetMapping("/series-by-paper-type/{paperType}")
    public ResponseEntity<List<String>> getSeriesNamesByPaperType(@PathVariable String paperType) {
        try {
            List<String> seriesNames = productsMapper.getSeriesNamesByPaperType(paperType);
            return ResponseEntity.ok(seriesNames);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * 根据公司编号和烫金纸性能类型获取产品系列名称列表
     * @param companyNumber 公司编号
     * @param paperType 烫金纸性能类型
     * @return 产品系列名称列表
     */
    @GetMapping("/series-by-company-and-paper-type")
    public ResponseEntity<List<String>> getSeriesNamesByCompanyNumberAndPaperType(
            @RequestParam String companyNumber,
            @RequestParam String paperType) {
        try {
            List<String> seriesNames = productsMapper.getSeriesNamesByCompanyNumberAndPaperType(companyNumber, paperType);
            return ResponseEntity.ok(seriesNames);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * 根据公司编号、烫金纸性能类型和产品系列名称获取产品列表
     * @param companyNumber 公司编号
     * @param paperType 烫金纸性能类型
     * @param seriesName 产品系列名称
     * @return 产品列表
     */
    @GetMapping("/products-by-company-papertype-series")
    public ResponseEntity<List<Products>> getProductsByCompanyNumberAndPaperTypeAndSeries(
            @RequestParam String companyNumber,
            @RequestParam String paperType,
            @RequestParam String seriesName) {
        try {
            List<Products> products = productsMapper.getProductsByCompanyNumberAndPaperTypeAndSeries(companyNumber, paperType, seriesName);
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    // ========== Excel导出功能 ==========
    
    /**
     * 导出产品信息到Excel
     */
    @GetMapping("/export")
    public ResponseEntity<byte[]> exportProductsToExcel() {
        try {
            // 由于ProductsMapper没有getAllProducts方法，这里暂时返回空数据
            // 如果需要导出产品信息，建议通过具体的系列名称来获取
            List<Products> allData = new ArrayList<>();
            
            // 生成文件名
            String fileName = ExcelExportUtil.generateFileName("产品信息");
            
            // 导出Excel
            return ExcelExportUtil.exportToResponse(allData, Products.class, fileName, "产品信息");
            
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
}
