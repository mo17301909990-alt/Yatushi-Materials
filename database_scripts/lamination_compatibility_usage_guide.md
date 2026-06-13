# 过胶材质兼容性数据插入指南

## 概述

本指南说明如何将图片中的过胶材质兼容性表格数据插入到您的数据库系统中。

## 文件说明

### 1. `insert_lamination_compatibility_data.sql`
- 基础版本，包含示例数据
- 适合测试和验证表结构

### 2. `insert_lamination_compatibility_complete.sql`
- 完整版本，使用CTE生成所有组合
- 自动处理所有烫金纸系列的兼容性关系

### 3. `insert_lamination_compatibility_accurate.sql`
- 精确版本，基于实际表格数据
- 推荐用于生产环境

## 数据结构

### 界面类型选项表 (interface_type_options)
```sql
- 粉紙面+普通油墨 (ID: 1)
- 粉紙面+LED UV墨 (ID: 2)  
- 粉紙面+快乾油墨 (ID: 3)
```

### 后加工步骤选项表 (post_processing_options)
```sql
- 無任何加工 (ID: 1)
- 擊凹/凸、絲印UV+擊凹/凸 (ID: 2)
- 啤折切、絲印UV+啤折切 (ID: 3)
- 踩坑位、絲印UV+踩坑位 (ID: 4)
- 壓紋 (ID: 5)
```

### 过胶材质选项表 (lamination_material_options)
```sql
- 普通預塗菲林-光膠 (ID: 1)
- 普通預塗菲林-啞膠 (ID: 2)
- 平價耐磨啞膠 (ID: 3)
- 超粘預塗菲林-光膠 (ID: 4)
- 超粘預塗菲林-啞膠 (ID: 5)
- 高價耐磨啞膠 (ID: 6)
- 預塗柔感膠 (ID: 7)
- 抗靜電光膠 (ID: 8)
- 抗靜電啞膠 (ID: 9)
- 含相逢水性 (ID: 10)
```

### 烫金纸系列分类
```sql
-- 粉紙面+普通油墨
SY6-, SY1-, SY+, SB, SA, S19, S16, LL

-- 粉紙面+LED UV墨  
LA

-- 粉紙面+快乾油墨
L817, KM, KB, KA, G6, G1, E8, A23
```

## 使用步骤

### 1. 执行表创建脚本
首先确保已创建所有必要的表结构。

### 2. 选择合适的数据插入脚本
- **测试环境**: 使用 `insert_lamination_compatibility_data.sql`
- **生产环境**: 使用 `insert_lamination_compatibility_accurate.sql`

### 3. 执行数据插入
```bash
psql -d your_database -f insert_lamination_compatibility_accurate.sql
```

### 4. 验证数据
脚本末尾包含验证查询，检查数据插入是否成功。

## 查询示例

### 查看特定烫金纸系列的兼容性
```sql
SELECT 
    lc.foil_series,
    ito.interface_name,
    ppo.step_name,
    lmo.material_name,
    lc.compatibility
FROM lamination_compatibility lc
JOIN interface_type_options ito ON lc.interface_type_id = ito.id
JOIN post_processing_options ppo ON lc.post_processing_step_id = ppo.id
JOIN lamination_material_options lmo ON lc.lamination_material_id = lmo.id
WHERE lc.foil_series = 'SY6-'
ORDER BY ppo.display_order, lmo.display_order;
```

### 查找兼容的过胶材质
```sql
SELECT DISTINCT lmo.material_name
FROM lamination_compatibility lc
JOIN lamination_material_options lmo ON lc.lamination_material_id = lmo.id
WHERE lc.foil_series = 'LA' 
  AND lc.compatibility = 'V'
  AND lc.post_processing_step_id = 1; -- 無任何加工
```

### 统计兼容性分布
```sql
SELECT 
    lc.compatibility,
    COUNT(*) as count
FROM lamination_compatibility lc
GROUP BY lc.compatibility;
```

## 注意事项

1. **数据完整性**: 根据图片中的表格，所有组合都显示为兼容（V），但实际使用时可能需要根据具体工艺要求调整。

2. **外键约束**: 确保所有外键引用的表都已正确创建并包含相应数据。

3. **索引优化**: 脚本已包含必要的索引，可根据实际查询模式进一步优化。

4. **数据更新**: 如需修改兼容性关系，建议使用UPDATE语句而不是重新插入。

## 扩展建议

1. **添加更多属性**: 可考虑添加成本、工艺难度等额外属性。
2. **版本控制**: 建议为兼容性数据添加版本字段，便于跟踪变更。
3. **审计日志**: 可添加变更日志表，记录数据修改历史。

