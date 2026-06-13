package com.it.yts_project.service;

import com.it.yts_project.dto.ImportResult;
import com.it.yts_project.dto.ImportConfig;
import com.it.yts_project.model.CompleteProductInfo;
import com.it.yts_project.model.Product;
import org.springframework.web.multipart.MultipartFile;

/**
 * 产品导入服务接口
 */
public interface ProductImportService {
    
    /**
     * 导入完整产品信息
     * 
     * @param file Excel文件
     * @param config 导入配置
     * @return 导入结果
     */
    ImportResult<CompleteProductInfo> importCompleteProductInfo(MultipartFile file, ImportConfig config);
    
    /**
     * 导入基础产品信息
     * 
     * @param file Excel文件
     * @param config 导入配置
     * @return 导入结果
     */
    ImportResult<Product> importBasicProductInfo(MultipartFile file, ImportConfig config);
}