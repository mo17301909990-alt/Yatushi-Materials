package com.it.yts_project.util;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.it.yts_project.dto.ExcelExportResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Excel导出工具类
 * 提供通用的Excel导出功能，支持大数据量导出和自定义样式
 */
@Slf4j
public class ExcelExportUtil {
    
    /**
     * 默认日期时间格式
     */
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
    
    /**
     * 导出Excel文件到ResponseEntity
     * 
     * @param data 要导出的数据列表
     * @param clazz 数据对象的Class类型
     * @param fileName 文件名（不包含扩展名）
     * @param sheetName 工作表名称
     * @return ResponseEntity<byte[]> 包含Excel文件的响应实体
     */
    public static <T> ResponseEntity<byte[]> exportToResponse(List<T> data, Class<T> clazz, String fileName, String sheetName) {
        try {
            // 生成带时间戳的文件名
            String timestamp = LocalDateTime.now().format(DATE_TIME_FORMATTER);
            String fullFileName = fileName + "_" + timestamp + ".xlsx";
            
            // 导出到字节数组
            byte[] excelBytes = exportToBytes(data, clazz, sheetName);
            
            // 设置响应头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", URLEncoder.encode(fullFileName, StandardCharsets.UTF_8));
            headers.setContentLength(excelBytes.length);
            
            log.info("Excel导出成功: 文件名={}, 记录数={}, 文件大小={}字节", fullFileName, data.size(), excelBytes.length);
            
            return new ResponseEntity<>(excelBytes, headers, HttpStatus.OK);
            
        } catch (Exception e) {
            log.error("Excel导出失败: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * 导出Excel文件到字节数组
     * 
     * @param data 要导出的数据列表
     * @param clazz 数据对象的Class类型
     * @param sheetName 工作表名称
     * @return byte[] Excel文件的字节数组
     */
    public static <T> byte[] exportToBytes(List<T> data, Class<T> clazz, String sheetName) throws IOException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            EasyExcel.write(outputStream, clazz)
                    .sheet(sheetName)
                    .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy()) // 自动列宽
                    .doWrite(data);
            
            return outputStream.toByteArray();
        }
    }
    
    /**
     * 创建Excel导出响应对象
     * 
     * @param data 导出的数据
     * @param fileName 文件名
     * @return ExcelExportResponse 导出响应对象
     */
    @SuppressWarnings("unchecked")
    public static <T> ExcelExportResponse createExportResponse(List<T> data, String fileName) {
        try {
            if (data.isEmpty()) {
                return ExcelExportResponse.failure("数据为空，无法导出");
            }
            byte[] excelBytes = exportToBytes(data, (Class<T>) data.get(0).getClass(), "数据导出");
            return ExcelExportResponse.success(fileName, (long) excelBytes.length, data.size());
        } catch (Exception e) {
            log.error("创建导出响应失败: {}", e.getMessage(), e);
            return ExcelExportResponse.failure(e.getMessage());
        }
    }
    
    /**
     * 验证导出数据
     * 
     * @param data 要导出的数据
     * @param maxRecords 最大记录数限制
     * @return boolean 验证是否通过
     */
    public static boolean validateExportData(List<?> data, int maxRecords) {
        if (data == null || data.isEmpty()) {
            log.warn("导出数据为空");
            return false;
        }
        
        if (data.size() > maxRecords) {
            log.warn("导出数据量超过限制: 当前={}, 限制={}", data.size(), maxRecords);
            return false;
        }
        
        return true;
    }
    
    /**
     * 生成默认文件名
     * 
     * @param moduleName 模块名称
     * @return String 生成的文件名
     */
    public static String generateFileName(String moduleName) {
        String timestamp = LocalDateTime.now().format(DATE_TIME_FORMATTER);
        return moduleName + "_导出_" + timestamp;
    }
}
