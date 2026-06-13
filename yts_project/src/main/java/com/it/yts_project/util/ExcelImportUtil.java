package com.it.yts_project.util;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.it.yts_project.dto.ImportResult;
import com.it.yts_project.dto.ImportConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import com.alibaba.excel.annotation.ExcelProperty;

/**
 * Excel导入工具类
 * 提供通用的Excel导入功能，支持数据验证和错误处理
 */
@Slf4j
public class ExcelImportUtil {
    
    // 使用ThreadLocal存储Excel中存在的列名
    private static final ThreadLocal<Set<String>> EXISTING_COLUMNS = new ThreadLocal<>();
    
    /**
     * 获取当前线程中Excel存在的列名
     */
    public static Set<String> getExistingColumns() {
        return EXISTING_COLUMNS.get();
    }
    
    /**
     * 清除ThreadLocal
     */
    public static void clearExistingColumns() {
        EXISTING_COLUMNS.remove();
    }
    
    /**
     * 从Java类中获取所有带有@ExcelProperty注解的字段的value值（列名）
     * @param clazz 目标Java类
     * @return 包含所有@ExcelProperty value的Set
     */
    public static Set<String> getAllExcelPropertyValues(Class<?> clazz) {
        Set<String> excelPropertyValues = new HashSet<>();
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(ExcelProperty.class)) {
                ExcelProperty excelProperty = field.getAnnotation(ExcelProperty.class);
                if (excelProperty.value().length > 0) {
                    excelPropertyValues.add(excelProperty.value()[0]);
                }
            }
        }
        return excelPropertyValues;
    }
    
    /**
     * 智能导入Excel文件（自动判断新增/更新）
     * 
     * @param file 上传的Excel文件
     * @param clazz 数据对象的Class类型
     * @param config 导入配置
     * @param dataProcessor 数据处理函数
     * @param <T> 数据类型
     * @return ImportResult 导入结果
     */
    public static <T> ImportResult<T> importExcelSmart(
            MultipartFile file, 
            Class<T> clazz, 
            ImportConfig config,
            Consumer<List<T>> dataProcessor) {
        
        ImportResult<T> result = new ImportResult<>();
        List<T> successData = new ArrayList<>();
        List<String> errorMessages = new ArrayList<>();
        
        try {
            // 验证文件
            if (file == null || file.isEmpty()) {
                result.setSuccess(false);
                result.setMessage("文件不能为空");
                return result;
            }
            
            // 验证文件类型
            String fileName = file.getOriginalFilename();
            if (fileName == null || (!fileName.endsWith(".xlsx") && !fileName.endsWith(".xls"))) {
                result.setSuccess(false);
                result.setMessage("文件格式不正确，请上传Excel文件");
                return result;
            }
            
            // 验证文件大小
            if (!validateFileSize(file, config.getMaxImportCount() * 1024L)) {
                result.setSuccess(false);
                result.setMessage("文件过大，请分批导入");
                return result;
            }
            
            // 读取Excel数据
            EasyExcel.read(file.getInputStream(), clazz, new ReadListener<T>() {
                private final List<T> cachedDataList = new ArrayList<>();
                private int processedCount = 0;
                
                @Override
                public void invoke(T data, AnalysisContext context) {
                    try {
                        // 检查是否超过最大导入数量
                        if (processedCount >= config.getMaxImportCount()) {
                            errorMessages.add(String.format("超过最大导入数量限制：%d", config.getMaxImportCount()));
                            return;
                        }
                        
                        // 数据验证
                        String validationError = validateData(data);
                        if (validationError != null) {
                            errorMessages.add(String.format("第%d行: %s", context.readRowHolder().getRowIndex() + 1, validationError));
                            return;
                        }
                        
                        cachedDataList.add(data);
                        processedCount++;
                    } catch (Exception e) {
                        errorMessages.add(String.format("第%d行: 数据解析错误 - %s", context.readRowHolder().getRowIndex() + 1, e.getMessage()));
                    }
                }
                
                @Override
                public void doAfterAllAnalysed(AnalysisContext context) {
                    // 处理数据（智能模式）
                    if (!cachedDataList.isEmpty()) {
                        try {
                            dataProcessor.accept(cachedDataList);
                            successData.addAll(cachedDataList);
                        } catch (Exception e) {
                            errorMessages.add("数据处理失败: " + e.getMessage());
                        }
                    }
                }
            }).sheet().doRead();
            
            // 设置结果
            result.setSuccess(errorMessages.isEmpty());
            result.setData(successData);
            result.setErrorMessages(errorMessages);
            result.setTotalCount(successData.size() + errorMessages.size());
            result.setSuccessCount(successData.size());
            result.setErrorCount(errorMessages.size());
            
            if (result.isSuccess()) {
                result.setMessage(String.format("智能导入成功，共处理%d条数据", successData.size()));
            } else {
                result.setMessage(String.format("智能导入完成，成功%d条，失败%d条", successData.size(), errorMessages.size()));
            }
            
        } catch (IOException e) {
            log.error("Excel智能导入失败: {}", e.getMessage(), e);
            result.setSuccess(false);
            result.setMessage("文件读取失败: " + e.getMessage());
        } catch (Exception e) {
            log.error("Excel智能导入异常: {}", e.getMessage(), e);
            result.setSuccess(false);
            result.setMessage("导入处理失败: " + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * 导入Excel文件并处理数据（支持冲突处理）
     * 
     * @param file 上传的Excel文件
     * @param clazz 数据对象的Class类型
     * @param config 导入配置
     * @param dataProcessor 数据处理函数
     * @param conflictChecker 冲突检查函数
     * @param <T> 数据类型
     * @return ImportResult 导入结果
     */
    public static <T> ImportResult<T> importExcelWithConflictHandling(
            MultipartFile file, 
            Class<T> clazz, 
            ImportConfig config,
            Consumer<List<T>> dataProcessor,
            Function<T, Boolean> conflictChecker) {
        
        ImportResult<T> result = new ImportResult<>();
        List<T> successData = new ArrayList<>();
        List<T> conflictData = new ArrayList<>();
        List<String> errorMessages = new ArrayList<>();
        
        try {
            // 验证文件
            if (file == null || file.isEmpty()) {
                result.setSuccess(false);
                result.setMessage("文件不能为空");
                return result;
            }
            
            // 验证文件类型
            String fileName = file.getOriginalFilename();
            if (fileName == null || (!fileName.endsWith(".xlsx") && !fileName.endsWith(".xls"))) {
                result.setSuccess(false);
                result.setMessage("文件格式不正确，请上传Excel文件");
                return result;
            }
            
            // 验证文件大小
            if (!validateFileSize(file, config.getMaxImportCount() * 1024L)) {
                result.setSuccess(false);
                result.setMessage("文件过大，请分批导入");
                return result;
            }
            
            // 读取Excel数据
            EasyExcel.read(file.getInputStream(), clazz, new ReadListener<T>() {
                private final List<T> cachedDataList = new ArrayList<>();
                private int processedCount = 0;
                
                @Override
                public void invoke(T data, AnalysisContext context) {
                    try {
                        // 检查是否超过最大导入数量
                        if (processedCount >= config.getMaxImportCount()) {
                            errorMessages.add(String.format("超过最大导入数量限制：%d", config.getMaxImportCount()));
                            return;
                        }
                        
                        // 数据验证
                        String validationError = validateData(data);
                        if (validationError != null) {
                            errorMessages.add(String.format("第%d行: %s", context.readRowHolder().getRowIndex() + 1, validationError));
                            return;
                        }
                        
                        // 冲突检查
                        boolean hasConflict = conflictChecker != null && conflictChecker.apply(data);
                        if (hasConflict) {
                            conflictData.add(data);
                            errorMessages.add(String.format("第%d行: 数据已存在，需要处理冲突", context.readRowHolder().getRowIndex() + 1));
                        } else {
                            cachedDataList.add(data);
                        }
                        
                        processedCount++;
                    } catch (Exception e) {
                        errorMessages.add(String.format("第%d行: 数据解析错误 - %s", context.readRowHolder().getRowIndex() + 1, e.getMessage()));
                    }
                }
                
                @Override
                public void doAfterAllAnalysed(AnalysisContext context) {
                    // 处理成功的数据
                    if (!cachedDataList.isEmpty()) {
                        try {
                            dataProcessor.accept(cachedDataList);
                            successData.addAll(cachedDataList);
                        } catch (Exception e) {
                            errorMessages.add("数据处理失败: " + e.getMessage());
                        }
                    }
                    
                    // 处理冲突数据
                    if (!conflictData.isEmpty()) {
                        handleConflictData(conflictData, config, dataProcessor, successData, errorMessages);
                    }
                }
            }).sheet().doRead();
            
            // 设置结果
            result.setSuccess(errorMessages.isEmpty());
            result.setData(successData);
            result.setErrorMessages(errorMessages);
            result.setTotalCount(successData.size() + conflictData.size() + errorMessages.size());
            result.setSuccessCount(successData.size());
            result.setErrorCount(errorMessages.size());
            
            if (result.isSuccess()) {
                result.setMessage(String.format("导入成功，共处理%d条数据", successData.size()));
            } else {
                result.setMessage(String.format("导入完成，成功%d条，冲突%d条，失败%d条", 
                    successData.size(), conflictData.size(), errorMessages.size()));
            }
            
        } catch (IOException e) {
            log.error("Excel导入失败: {}", e.getMessage(), e);
            result.setSuccess(false);
            result.setMessage("文件读取失败: " + e.getMessage());
        } catch (Exception e) {
            log.error("Excel导入异常: {}", e.getMessage(), e);
            result.setSuccess(false);
            result.setMessage("导入处理失败: " + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * 导入Excel文件并处理数据（简化版本）
     * 
     * @param file 上传的Excel文件
     * @param clazz 数据对象的Class类型
     * @param dataProcessor 数据处理函数
     * @param <T> 数据类型
     * @return ImportResult 导入结果
     */
    public static <T> ImportResult<T> importExcel(MultipartFile file, Class<T> clazz, Consumer<List<T>> dataProcessor) {
        ImportResult<T> result = new ImportResult<>();
        List<T> successData = new ArrayList<>();
        List<String> errorMessages = new ArrayList<>();
        
        try {
            // 验证文件
            if (file == null || file.isEmpty()) {
                result.setSuccess(false);
                result.setMessage("文件不能为空");
                return result;
            }
            
            // 验证文件类型
            String fileName = file.getOriginalFilename();
            if (fileName == null || (!fileName.endsWith(".xlsx") && !fileName.endsWith(".xls"))) {
                result.setSuccess(false);
                result.setMessage("文件格式不正确，请上传Excel文件");
                return result;
            }
            
            // 读取Excel数据
            EasyExcel.read(file.getInputStream(), clazz, new ReadListener<T>() {
                private final List<T> cachedDataList = new ArrayList<>();
                
                @Override
                public void invoke(T data, AnalysisContext context) {
                    try {
                        // 数据验证
                        String validationError = validateData(data);
                        if (validationError != null) {
                            errorMessages.add(String.format("第%d行: %s", context.readRowHolder().getRowIndex() + 1, validationError));
                        } else {
                            cachedDataList.add(data);
                        }
                    } catch (Exception e) {
                        errorMessages.add(String.format("第%d行: 数据解析错误 - %s", context.readRowHolder().getRowIndex() + 1, e.getMessage()));
                    }
                }
                
                @Override
                public void doAfterAllAnalysed(AnalysisContext context) {
                    // 处理成功的数据
                    if (!cachedDataList.isEmpty()) {
                        try {
                            dataProcessor.accept(cachedDataList);
                            successData.addAll(cachedDataList);
                        } catch (Exception e) {
                            errorMessages.add("数据处理失败: " + e.getMessage());
                        }
                    }
                }
            }).sheet().doRead();
            
            // 设置结果
            result.setSuccess(errorMessages.isEmpty());
            result.setData(successData);
            result.setErrorMessages(errorMessages);
            result.setTotalCount(successData.size() + errorMessages.size());
            result.setSuccessCount(successData.size());
            result.setErrorCount(errorMessages.size());
            
            if (result.isSuccess()) {
                result.setMessage(String.format("导入成功，共处理%d条数据", successData.size()));
            } else {
                result.setMessage(String.format("导入完成，成功%d条，失败%d条", successData.size(), errorMessages.size()));
            }
            
        } catch (IOException e) {
            log.error("Excel导入失败: {}", e.getMessage(), e);
            result.setSuccess(false);
            result.setMessage("文件读取失败: " + e.getMessage());
        } catch (Exception e) {
            log.error("Excel导入异常: {}", e.getMessage(), e);
            result.setSuccess(false);
            result.setMessage("导入处理失败: " + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * 数据验证方法（子类可重写）
     * 
     * @param data 数据对象
     * @return String 验证错误信息，null表示验证通过
     */
    protected static String validateData(Object data) {
        // 基础验证逻辑，子类可重写
        if (data == null) {
            return "数据不能为空";
        }
        return null;
    }
    
    /**
     * 验证文件大小
     * 
     * @param file 上传文件
     * @param maxSize 最大文件大小（字节）
     * @return boolean 验证是否通过
     */
    public static boolean validateFileSize(MultipartFile file, long maxSize) {
        if (file == null || file.isEmpty()) {
            return false;
        }
        return file.getSize() <= maxSize;
    }
    
    /**
     * 验证文件类型
     * 
     * @param fileName 文件名
     * @return boolean 验证是否通过
     */
    public static boolean validateFileType(String fileName) {
        if (fileName == null) {
            return false;
        }
        String lowerCaseFileName = fileName.toLowerCase();
        return lowerCaseFileName.endsWith(".xlsx") || lowerCaseFileName.endsWith(".xls");
    }
    
    /**
     * 处理冲突数据
     * 
     * @param conflictData 冲突数据列表
     * @param config 导入配置
     * @param dataProcessor 数据处理函数
     * @param successData 成功数据列表
     * @param errorMessages 错误信息列表
     * @param <T> 数据类型
     */
    private static <T> void handleConflictData(
            List<T> conflictData, 
            ImportConfig config, 
            Consumer<List<T>> dataProcessor,
            List<T> successData, 
            List<String> errorMessages) {
        
        switch (config.getConflictStrategy()) {
            case SKIP:
                // 跳过冲突数据
                errorMessages.add(String.format("跳过%d条冲突数据", conflictData.size()));
                break;
                
            case OVERWRITE:
                // 覆盖现有数据
                try {
                    dataProcessor.accept(conflictData);
                    successData.addAll(conflictData);
                } catch (Exception e) {
                    errorMessages.add("覆盖冲突数据失败: " + e.getMessage());
                }
                break;
                
            case CREATE_NEW:
                // 创建新记录（忽略冲突）
                try {
                    dataProcessor.accept(conflictData);
                    successData.addAll(conflictData);
                } catch (Exception e) {
                    errorMessages.add("创建新记录失败: " + e.getMessage());
                }
                break;
                
            case PROMPT_USER:
                // 提示用户选择（这里需要前端配合）
                errorMessages.add(String.format("发现%d条冲突数据，需要用户确认处理方式", conflictData.size()));
                break;
                
            default:
                errorMessages.add("未知的冲突处理策略");
                break;
        }
    }
    
    /**
     * 智能导入Excel文件（支持返回详细结果的数据处理器）
     * 
     * @param file 上传的文件
     * @param clazz 数据类型
     * @param dataProcessor 数据处理器，返回ImportResult
     * @return 导入结果
     */
    public static <T> ImportResult<T> importExcelSmart(
            MultipartFile file, 
            Class<T> clazz, 
            Function<List<T>, ImportResult<T>> dataProcessor) {
        
        ImportResult<T> result = new ImportResult<>();
        List<String> errorMessages = new ArrayList<>();
        
        try {
            // 验证文件
            if (file == null || file.isEmpty()) {
                result.setSuccess(false);
                result.setMessage("文件不能为空");
                return result;
            }
            
            // 验证文件类型
            String fileName = file.getOriginalFilename();
            if (fileName == null || (!fileName.endsWith(".xlsx") && !fileName.endsWith(".xls"))) {
                result.setSuccess(false);
                result.setMessage("文件格式不正确，请上传Excel文件");
                return result;
            }
            
            // 读取Excel数据
            List<T> dataList = new ArrayList<>();
            Set<String> existingColumns = new HashSet<>();
            EXISTING_COLUMNS.set(existingColumns);
            
            try {
                // 先读取表头，获取Excel中实际存在的列名
                EasyExcel.read(file.getInputStream(), clazz, new ReadListener<T>() {
                    private boolean headRead = false;
                    
                    @Override
                    public void invoke(T data, AnalysisContext context) {
                        // 第一次读取时，从context中获取表头信息
                        if (!headRead) {
                            try {
                                // 通过AnalysisContext获取表头信息
                                com.alibaba.excel.read.metadata.holder.ReadSheetHolder readSheetHolder = context.readSheetHolder();
                                if (readSheetHolder != null) {
                                    // 尝试通过反射获取headMap字段
                                    try {
                                        java.lang.reflect.Field headMapField = readSheetHolder.getClass().getDeclaredField("headMap");
                                        headMapField.setAccessible(true);
                                        @SuppressWarnings("unchecked")
                                        Map<Integer, String> headMap = (Map<Integer, String>) headMapField.get(readSheetHolder);
                                        if (headMap != null && !headMap.isEmpty()) {
                                            existingColumns.addAll(headMap.values());
                                            log.info("成功获取Excel表头列: {}, 价格列是否存在: {}", existingColumns, existingColumns.contains("價格"));
                                        } else {
                                            log.warn("表头映射为空，无法获取列信息");
                                        }
                                    } catch (NoSuchFieldException | IllegalAccessException e) {
                                        // 如果反射失败，尝试从readRowHolder获取
                                        try {
                                            com.alibaba.excel.read.metadata.holder.ReadRowHolder readRowHolder = context.readRowHolder();
                                            if (readRowHolder != null) {
                                                java.lang.reflect.Field headMapField2 = readRowHolder.getClass().getDeclaredField("headMap");
                                                headMapField2.setAccessible(true);
                                                @SuppressWarnings("unchecked")
                                                Map<Integer, String> headMap2 = (Map<Integer, String>) headMapField2.get(readRowHolder);
                                                if (headMap2 != null && !headMap2.isEmpty()) {
                                                    existingColumns.addAll(headMap2.values());
                                                    log.info("成功获取Excel表头列(从ReadRowHolder): {}, 价格列是否存在: {}", existingColumns, existingColumns.contains("價格"));
                                                }
                                            }
                                        } catch (Exception e2) {
                                            log.warn("无法通过反射获取表头信息: {}", e2.getMessage());
                                            // 如果所有方法都失败，使用兼容模式：从类注解中获取所有可能的列名
                                            Set<String> allColumns = getAllExcelPropertyValues(clazz);
                                            existingColumns.addAll(allColumns);
                                            log.warn("将使用兼容模式：假设所有列都存在，共{}列: {}", allColumns.size(), allColumns);
                                        }
                                    }
                                }
                            } catch (Exception e) {
                                // 如果无法获取表头，使用兼容模式：从类注解中获取所有可能的列名
                                log.warn("无法获取Excel表头信息，将使用兼容模式: {}", e.getMessage());
                                Set<String> allColumns = getAllExcelPropertyValues(clazz);
                                existingColumns.addAll(allColumns);
                                log.warn("兼容模式：假设所有列都存在，共{}列: {}", allColumns.size(), allColumns);
                            }
                            headRead = true;
                        }
                        
                        try {
                            // 数据验证
                            String validationError = validateData(data);
                            if (validationError != null) {
                                errorMessages.add(String.format("第%d行: %s", context.readRowHolder().getRowIndex() + 1, validationError));
                                return;
                            }
                            
                            dataList.add(data);
                        } catch (Exception e) {
                            errorMessages.add(String.format("第%d行: 数据解析错误 - %s", context.readRowHolder().getRowIndex() + 1, e.getMessage()));
                        }
                    }
                    
                    @Override
                    public void doAfterAllAnalysed(AnalysisContext context) {
                        // 处理数据
                        if (!dataList.isEmpty()) {
                            try {
                                ImportResult<T> processResult = dataProcessor.apply(dataList);
                                result.setSuccess(processResult.isSuccess());
                                result.setMessage(processResult.getMessage());
                                result.setTotalCount(processResult.getTotalCount());
                                result.setCreatedCount(processResult.getCreatedCount());
                                result.setUpdatedCount(processResult.getUpdatedCount());
                                result.setSkippedCount(processResult.getSkippedCount());
                            } catch (Exception e) {
                                errorMessages.add("数据处理失败: " + e.getMessage());
                            }
                        }
                    }
                }).sheet().doRead();
            } finally {
                // 清除ThreadLocal
                EXISTING_COLUMNS.remove();
            }
            
            // 如果有错误消息，添加到结果中
            if (!errorMessages.isEmpty()) {
                String errorMsg = result.getMessage() != null ? result.getMessage() + "\n" : "";
                errorMsg += "解析错误:\n" + String.join("\n", errorMessages);
                result.setMessage(errorMsg);
                result.setSuccess(false);
            }
            
        } catch (Exception e) {
            result.setSuccess(false);
            String errorMsg = e.getMessage();
            // 针对数据类型转换错误，提供更友好的提示
            if (errorMsg != null && errorMsg.contains("Convert data")) {
                errorMsg = "文件读取失败: Excel中某些单元格的数据格式不正确（如价格字段应为数字）。请检查Excel文件中的数据格式。";
            } else {
                errorMsg = "文件读取失败: " + (errorMsg != null ? errorMsg : e.getClass().getSimpleName());
            }
            result.setMessage(errorMsg);
            log.error("Excel导入失败", e);
        }
        
        return result;
    }
}
