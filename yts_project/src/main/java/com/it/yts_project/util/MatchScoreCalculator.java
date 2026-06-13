package com.it.yts_project.util;

import com.it.yts_project.dto.GoldFoilProductDTO;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 匹配度计算工具类
 * 用于计算产品与用户偏好的匹配度
 */
public class MatchScoreCalculator {
    
    // 匹配字段列表
    private static final String[] MATCH_FIELDS = {"color", "size", "tightness", "temperature", "performance"};
    
    /**
     * 计算单个产品的匹配度
     * 
     * @param product 产品数据
     * @param userPreferences 用户匹配偏好（按字段分组）
     * @return 匹配度分数（0-100）
     */
    public static int calculateMatchScore(GoldFoilProductDTO product, Map<String, List<String>> userPreferences) {
        if (userPreferences == null || userPreferences.isEmpty()) {
            return 0; // 没有用户偏好时，匹配度为0
        }
        
        int matchedCount = 0;
        int totalFields = 0;
        
        // 遍历所有匹配字段
        for (String field : MATCH_FIELDS) {
            List<String> userValues = userPreferences.get(field);
            
            // 如果用户没有设置该字段的偏好，跳过该字段
            if (userValues == null || userValues.isEmpty()) {
                continue;
            }
            
            // 获取产品在该字段的值
            String productValue = getProductFieldValue(product, field);
            
            // 检查产品值是否在用户偏好中（严格全等匹配）
            boolean isMatched = false;
            if (productValue != null && !productValue.trim().isEmpty()) {
                String cleanProductValue = productValue.trim();
                for (String userValue : userValues) {
                    if (userValue != null && userValue.trim().equals(cleanProductValue)) {
                        isMatched = true;
                        break;
                    }
                }
            }
            
            totalFields++;
            if (isMatched) {
                matchedCount++;
            }
        }
        
        // 计算匹配度百分比
        if (totalFields == 0) {
            return 0;
        }
        
        return Math.round((matchedCount * 100.0f) / totalFields);
    }
    
    /**
     * 根据字段名获取产品对应的字段值
     */
    private static String getProductFieldValue(GoldFoilProductDTO product, String fieldName) {
        if (product == null) {
            return null;
        }
        
        switch (fieldName) {
            case "color":
                return product.getColor();
            case "size":
                return product.getSize();
            case "tightness":
                return product.getTightness();
            case "temperature":
                return product.getTemperatureRange(); // 注意：前端使用temperatureRange
            case "performance":
                return product.getPerformance();
            default:
                return null;
        }
    }
    
    /**
     * 批量计算多个产品的匹配度
     * 
     * @param products 产品列表
     * @param userPreferences 用户匹配偏好
     * @return 带有匹配度的产品列表（通过setMatchScore设置）
     */
    public static void calculateBatchMatchScores(
            List<GoldFoilProductDTO> products, 
            Map<String, List<String>> userPreferences) {
        if (products == null || products.isEmpty()) {
            return;
        }
        
        for (GoldFoilProductDTO product : products) {
            int matchScore = calculateMatchScore(product, userPreferences);
            product.setMatchScore(matchScore);
        }
    }
    
    /**
     * 检查用户是否有设置偏好
     */
    public static boolean hasUserPreferences(Map<String, List<String>> userPreferences) {
        if (userPreferences == null || userPreferences.isEmpty()) {
            return false;
        }
        
        // 检查是否有任何字段设置了偏好
        for (String field : MATCH_FIELDS) {
            List<String> values = userPreferences.get(field);
            if (values != null && !values.isEmpty()) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * 根据价格计算匹配度
     * 规则：最低价格为100%，第二低为90%，第三低为80%，以此类推
     * 相同价格的产品应该有相同的匹配度
     * 
     * @param products 产品列表（需要先按价格排序）
     */
    public static void calculatePriceBasedMatchScores(List<GoldFoilProductDTO> products) {
        if (products == null || products.isEmpty()) {
            return;
        }
        
        // 按价格升序排序
        products.sort((a, b) -> {
            Double priceA = a.getPrice() != null ? a.getPrice() : Double.MAX_VALUE;
            Double priceB = b.getPrice() != null ? b.getPrice() : Double.MAX_VALUE;
            return priceA.compareTo(priceB);
        });
        
        // 获取所有不同的价格（按升序）
        Set<Double> uniquePrices = products.stream()
            .map(p -> p.getPrice())
            .filter(price -> price != null)
            .collect(Collectors.toCollection(LinkedHashSet::new));
        
        // 如果所有产品都没有价格，设置匹配度为0
        if (uniquePrices.isEmpty()) {
            for (GoldFoilProductDTO product : products) {
                product.setMatchScore(0);
            }
            return;
        }
        
        // 将唯一价格转换为列表以便按索引访问
        List<Double> sortedPrices = new ArrayList<>(uniquePrices);
        int priceCount = sortedPrices.size();
        
        // 创建价格到匹配度的映射
        Map<Double, Integer> priceToScoreMap = new HashMap<>();
        for (int i = 0; i < sortedPrices.size(); i++) {
            // 匹配度计算公式：100% * (n - i) / n
            // 最低价格（i=0）：100% * n / n = 100%
            // 第二低价格（i=1）：100% * (n-1) / n
            // 第三低价格（i=2）：100% * (n-2) / n
            // 以此类推
            double scorePercent = 100.0 * (priceCount - i) / priceCount;
            int score = (int) Math.round(scorePercent);
            priceToScoreMap.put(sortedPrices.get(i), score);
        }
        
        // 为每个产品设置匹配度
        for (GoldFoilProductDTO product : products) {
            Double price = product.getPrice();
            if (price != null && priceToScoreMap.containsKey(price)) {
                product.setMatchScore(priceToScoreMap.get(price));
            } else {
                // 如果产品没有价格，设置匹配度为0
                product.setMatchScore(0);
            }
        }
        
        System.out.println("价格匹配度计算完成，不同价格数量: " + sortedPrices.size() + 
                         ", 价格范围: " + sortedPrices.get(0) + " - " + sortedPrices.get(sortedPrices.size() - 1));
    }
    
    /**
     * 根据价格和使用次数计算最终匹配度（新算法）
     * 算法说明：
     * 1. 多条件筛选后，先根据不同price去重并排序，按"从贵到便宜"分成k档（k=不同价格数量）
     * 2. 第j档的基础匹配度 = j/k * 100
     * 3. 每一档j的实际匹配度区间是 (base(j-1), base(j)]，j=1时前一档视为0
     * 4. 同一档位内的物料，按usage_count从大到小排
     * 5. 使用 final_match = prev + (curr - prev) * (m - rank + 1) / m 把它们均匀分布到区间内
     * 6. 最终排序按final_match从高到低
     * 7. 没有价格的产品匹配度设为0
     * 
     * @param products 产品列表
     * @param tierCount 分档数量参数（已废弃，实际分档数=不同价格数量）
     */
    public static void calculatePriceUsageMatchScores(List<GoldFoilProductDTO> products, int tierCount) {
        if (products == null || products.isEmpty()) {
            return;
        }
        
        // 分离有价格和无价格的产品
        List<GoldFoilProductDTO> productsWithPrice = new ArrayList<>();
        List<GoldFoilProductDTO> productsWithoutPrice = new ArrayList<>();
        
        for (GoldFoilProductDTO product : products) {
            if (product.getPrice() != null) {
                productsWithPrice.add(product);
            } else {
                productsWithoutPrice.add(product);
                // 没有价格的产品匹配度设为0
                product.setFinalMatchScore(0.0);
                product.setMatchScore(0);
            }
        }
        
        // 如果没有有价格的产品，直接返回
        if (productsWithPrice.isEmpty()) {
            System.out.println("所有产品都没有价格，匹配度全部设为0");
            return;
        }
        
        // 按价格从高到低去重并排序
        Set<Double> uniquePrices = productsWithPrice.stream()
            .map(p -> p.getPrice())
            .filter(price -> price != null)
            .collect(Collectors.toCollection(LinkedHashSet::new));
        
        List<Double> sortedPrices = new ArrayList<>(uniquePrices);
        sortedPrices.sort((a, b) -> b.compareTo(a)); // 从高到低排序
        
        int uniquePriceCount = sortedPrices.size();
        // 实际分档数：有多少个不同价格就分多少档，每个价格一个档位
        int k = uniquePriceCount;
        
        // 计算每档的基础匹配度
        // 从贵到便宜分成k档，第1档（最贵）= 1/k * 100%，第k档（最便宜）= 100%
        // 基础匹配度公式：base(j) = j / k * 100
        Map<Double, Integer> priceToTierMap = new HashMap<>();
        for (int i = 0; i < sortedPrices.size(); i++) {
            // 将价格分配到档位：第0个价格（最贵）在第1档，第1个价格在第2档，以此类推
            // 每个价格占一个档位
            int tier = i + 1; // 从1开始编号
            priceToTierMap.put(sortedPrices.get(i), tier);
        }
        
        // 按档位分组产品
        Map<Integer, List<GoldFoilProductDTO>> tierProductsMap = new HashMap<>();
        for (GoldFoilProductDTO product : productsWithPrice) {
            Integer tier = priceToTierMap.get(product.getPrice());
            if (tier != null) {
                tierProductsMap.computeIfAbsent(tier, k1 -> new ArrayList<>()).add(product);
            }
        }
        
        // 为每个档位内的产品计算最终匹配度
        for (Map.Entry<Integer, List<GoldFoilProductDTO>> entry : tierProductsMap.entrySet()) {
            int tier = entry.getKey();
            List<GoldFoilProductDTO> tierProducts = entry.getValue();
            
            // 计算基础匹配度
            // 价格最低的应该是最匹配的（100%），价格最高的应该匹配度最低
            // 第j档的基础匹配度 = j / k * 100
            // 第1档（最贵）= 1 / k * 100（最低）
            // 第k档（最便宜）= k / k * 100 = 100%（最高）
            double baseCurr = tier * 100.0 / k; // 当前档的基础匹配度
            double basePrev = tier > 1 ? (tier - 1) * 100.0 / k : 0.0; // 前一档的基础匹配度，j=1时前一档视为0
            
            // 同一档位内按usage_count从大到小排序
            tierProducts.sort((a, b) -> {
                Long countA = a.getUsageCount() != null ? a.getUsageCount() : 0L;
                Long countB = b.getUsageCount() != null ? b.getUsageCount() : 0L;
                return countB.compareTo(countA); // 降序
            });
            
            // 按usage_count分组，相同usage_count的物料应该得到相同的匹配度
            Map<Long, List<GoldFoilProductDTO>> usageCountGroups = new HashMap<>();
            for (GoldFoilProductDTO product : tierProducts) {
                Long usageCount = product.getUsageCount() != null ? product.getUsageCount() : 0L;
                usageCountGroups.computeIfAbsent(usageCount, key -> new ArrayList<>()).add(product);
            }
            
            // 按usage_count降序获取分组列表
            List<Map.Entry<Long, List<GoldFoilProductDTO>>> sortedGroups = new ArrayList<>(usageCountGroups.entrySet());
            sortedGroups.sort((a, b) -> b.getKey().compareTo(a.getKey())); // 按usage_count降序
            
            // 计算每个分组应该使用的rank（使用该组内第一个物料的rank）
            int m = tierProducts.size();
            int currentRank = 1;
            
            for (Map.Entry<Long, List<GoldFoilProductDTO>> group : sortedGroups) {
                List<GoldFoilProductDTO> groupProducts = group.getValue();
                int groupSize = groupProducts.size();
                
                // 使用该组内第一个物料的rank来计算匹配度
                // 对于相同usage_count的物料，使用相同的rank（组内第一个rank）
                int groupRank = currentRank;
                
                // 计算该组的匹配度
                // 修改公式：final_match = prev + (curr - prev) * (m - rank + 1) / m
                // 这样当rank=1时，能达到curr（100%），当rank=m时，为prev + (curr-prev)/m
                // 区间为 [prev + (curr-prev)/m, curr]，即 (prev, curr] 的闭区间形式
                double finalMatch = basePrev + (baseCurr - basePrev) * (m - groupRank + 1.0) / m;
                
                // 给组内所有物料设置相同的匹配度
                for (GoldFoilProductDTO product : groupProducts) {
                    product.setFinalMatchScore(finalMatch);
                    product.setMatchScore((int) Math.round(finalMatch));
                }
                
                // 更新rank，跳过该组的所有物料
                currentRank += groupSize;
            }
        }
        
        System.out.println(String.format("价格+使用次数匹配度计算完成，不同价格数量: %d, 分档数: %d, 有价格产品数: %d, 无价格产品数: %d",
            uniquePriceCount, k, productsWithPrice.size(), productsWithoutPrice.size()));
    }
    
    /**
     * 根据价格和使用次数计算最终匹配度
     * 实际分档数 = 不同价格数量（每个价格一个档位）
     */
    public static void calculatePriceUsageMatchScores(List<GoldFoilProductDTO> products) {
        calculatePriceUsageMatchScores(products, 0); // tierCount参数已废弃，传入任意值都可以
    }
}

