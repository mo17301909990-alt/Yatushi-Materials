package com.it.yts_project.util.converter;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.converters.ReadConverterContext;
import com.alibaba.excel.converters.WriteConverterContext;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.data.WriteCellData;

/**
 * 自定义Double转换器
 * 处理Excel中的空值、非数字字符串等异常情况，转换为null或默认值
 */
public class CustomDoubleConverter implements Converter<Double> {
    
    @Override
    public Class<?> supportJavaTypeKey() {
        return Double.class;
    }
    
    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return null; // 支持所有类型
    }
    
    /**
     * 读取Excel数据时转换（Excel -> Java）
     */
    @Override
    public Double convertToJavaData(ReadConverterContext<?> context) throws Exception {
        ReadCellData<?> cellData = context.getReadCellData();
        
        // 如果单元格为空，返回null
        if (cellData == null || cellData.getType() == CellDataTypeEnum.EMPTY) {
            return null;
        }
        
        // 获取单元格的值（可能是数字、字符串等）
        Object cellValue = null;
        
        // 尝试获取不同类型的值
        if (cellData.getType() == CellDataTypeEnum.NUMBER) {
            // 如果是数字类型，直接获取数字值
            cellValue = cellData.getNumberValue();
            if (cellValue != null) {
                if (cellValue instanceof Double) {
                    return (Double) cellValue;
                } else if (cellValue instanceof Integer) {
                    return ((Integer) cellValue).doubleValue();
                } else if (cellValue instanceof Long) {
                    return ((Long) cellValue).doubleValue();
                } else {
                    return Double.parseDouble(cellValue.toString());
                }
            }
        }
        
        // 如果是字符串类型（包括文本格式的数字），尝试转换
        String stringValue = cellData.getStringValue();
        
        // 如果字符串为空或只包含空白字符，返回null
        if (stringValue == null || stringValue.trim().isEmpty()) {
            return null;
        }
        
        // 去除前后空白字符
        stringValue = stringValue.trim();
        
        // 处理常见的非数字情况
        // 如果包含"无"、"N/A"、"NULL"等，返回null
        if (stringValue.equalsIgnoreCase("无") || 
            stringValue.equalsIgnoreCase("N/A") || 
            stringValue.equalsIgnoreCase("NULL") ||
            stringValue.equalsIgnoreCase("空") ||
            stringValue.equals("-") ||
            stringValue.equals("—")) {
            return null;
        }
        
        try {
            // 尝试转换为Double
            // 移除可能存在的货币符号、千位分隔符等
            stringValue = stringValue.replaceAll("[￥$€£,，]", "");
            // 处理可能的科学计数法格式
            return Double.parseDouble(stringValue);
        } catch (NumberFormatException e) {
            // 如果无法转换，返回null（而不是抛出异常）
            // 这样可以让导入继续进行，只是该字段为null
            return null;
        }
    }
    
    /**
     * 写入Excel数据时转换（Java -> Excel）
     */
    @Override
    public WriteCellData<?> convertToExcelData(WriteConverterContext<Double> context) throws Exception {
        Double value = context.getValue();
        if (value == null) {
            return new WriteCellData<>("");
        }
        return new WriteCellData<>(value.toString());
    }
}

