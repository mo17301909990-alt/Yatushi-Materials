package com.it.yts_project.util.temp;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.*;

/**
 * 临时工具类：用于分析Excel文件内容
 * 仅用于读取和分析，不修改任何数据
 */
@Slf4j
public class ExcelAnalyzer {
    
    public static void analyzeExcelFiles(String folderPath) {
        File folder = new File(folderPath);
        if (!folder.exists() || !folder.isDirectory()) {
            System.out.println("文件夹不存在: " + folderPath);
            return;
        }
        
        File[] excelFiles = folder.listFiles((dir, name) -> 
            name.endsWith(".xlsx") || name.endsWith(".xls")
        );
        
        if (excelFiles == null || excelFiles.length == 0) {
            System.out.println("未找到Excel文件");
            return;
        }
        
        System.out.println("=".repeat(80));
        System.out.println("找到 " + excelFiles.length + " 个Excel文件");
        System.out.println("=".repeat(80));
        
        for (File excelFile : excelFiles) {
            analyzeSingleFile(excelFile);
        }
    }
    
    private static void analyzeSingleFile(File excelFile) {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("文件: " + excelFile.getName());
        System.out.println("-".repeat(80));
        
        try {
            // 使用EasyExcel读取，使用List<List<String>>来接收原始数据
            final List<List<String>> allRows = new ArrayList<>();
            final List<String> headers = new ArrayList<>();
            final boolean[] isFirstRow = {true};
            
            EasyExcel.read(excelFile)
                .sheet(0)  // 先读取第一个工作表
                .headRowNumber(0)  // 不跳过表头，我们自己处理
                .registerReadListener(new ReadListener<List<String>>() {
                    @Override
                    public void invoke(List<String> data, AnalysisContext context) {
                        if (isFirstRow[0]) {
                            headers.addAll(data);
                            isFirstRow[0] = false;
                        } else {
                            if (allRows.size() < 5) {  // 只保存前5行数据
                                allRows.add(new ArrayList<>(data));
                            }
                        }
                    }
                    
                    @Override
                    public void doAfterAllAnalysed(AnalysisContext context) {
                        // 分析完成
                    }
                })
                .doRead();
            
            System.out.println("工作表: Sheet1 (第一个工作表)");
            System.out.println("  总行数: " + (allRows.size() + 1));  // +1 for header
            System.out.println("  列数: " + headers.size());
            
            if (!headers.isEmpty()) {
                System.out.println("  列名: " + String.join(", ", headers.subList(0, Math.min(headers.size(), 15))));
                if (headers.size() > 15) {
                    System.out.println("    ... (共 " + headers.size() + " 列)");
                }
            }
            
            if (!allRows.isEmpty()) {
                System.out.println("  前5行数据预览:");
                for (int i = 0; i < allRows.size(); i++) {
                    List<String> row = allRows.get(i);
                    System.out.print("    行" + (i + 2) + ": ");
                    List<String> displayValues = new ArrayList<>();
                    for (int j = 0; j < Math.min(row.size(), 10); j++) {
                        String val = row.get(j);
                        displayValues.add(val != null && !val.trim().isEmpty() ? val : "(空)");
                    }
                    System.out.println(String.join(" | ", displayValues));
                    if (row.size() > 10) {
                        System.out.println("      ... (还有 " + (row.size() - 10) + " 列)");
                    }
                }
            }
            
        } catch (Exception e) {
            System.out.println("读取文件失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        // 分析AI建bom资源组文件夹
        String folderPath = "../AI建bom资源组";
        if (args.length > 0) {
            folderPath = args[0];
        }
        analyzeExcelFiles(folderPath);
    }
}

