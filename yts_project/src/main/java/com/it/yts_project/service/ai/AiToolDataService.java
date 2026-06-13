package com.it.yts_project.service.ai;

import com.it.yts_project.dto.CompatibilityQueryParams;
import com.it.yts_project.dto.GoldFoilProductDTO;
import com.it.yts_project.dto.GoldFoilQueryParams;
import com.it.yts_project.mapper.ProductMapper;
import com.it.yts_project.model.HotStampingCompatibility;
import com.it.yts_project.model.PreProcessingStepsOptions;
import com.it.yts_project.model.Product;
import com.it.yts_project.model.ProductTypeOptions;
import com.it.yts_project.service.GoldFoilProductService;
import com.it.yts_project.service.PreProcessingStepsOptionsService;
import com.it.yts_project.service.ProductTypeOptionsService;
import com.it.yts_project.service.SmartCompatibilityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 方式 2：从现有 Service/Mapper 拉取可序列化为文本的「系统事实」，供 AI 单轮注入（非 function-calling 多轮时的 MVP）。
 */
@Service
public class AiToolDataService {

    private static final Logger log = LoggerFactory.getLogger(AiToolDataService.class);
    /** 含 # 的型号（如 HS#1S、HG#01GR）须纳入，否则整句会误作关键词搜索导致 0 条 */
    private static final Pattern MODEL_LIKE = Pattern.compile("[A-Za-z0-9][A-Za-z0-9#\\-]{2,}");
    /** 用户想了解「烫金类型兼容规则」矩阵数据（须注入规则表，勿仅用产品模糊搜索） */
    private static final Pattern RULES_INTENT = Pattern.compile(
            ".*(烫金规则|兼容规则|烫金.*兼容|查查.*规则|规则.*有哪些|有哪些.*规则|V和X|V/X|兼容性.*规则|图案.*烫金.*类型).*",
            Pattern.CASE_INSENSITIVE | Pattern.DOTALL
    );
    private static final Pattern STATS_TRIGGER = Pattern.compile(".*(规则统计|规则.*条|烫金.*类型.*统计|有多少.*规则|统计.*规则).*");
    /** 用户问「产品类型有多少」等 */
    private static final Pattern PRODUCT_TYPE_INTENT = Pattern.compile(
            ".*(产品类型|產品類型|产品 类型|有多少.*类型|几个.*类型|类型.*几个|类型.*多少).*",
            Pattern.CASE_INSENSITIVE | Pattern.DOTALL
    );
    /** 与烫金页「适用界面(前工序)」下拉同源：pre_processing_steps_options 启用项 */
    private static final Pattern APPLICABLE_INTERFACE_INTENT = Pattern.compile(
            ".*(适用界面|適用界面|前工序|前工序選項|前工序选项|界面.*多少|界面.*几|界面.*種|界面.*种|幾種.*界面|几种.*界面|多少.*界面|查看.*界面|查查.*界面).*",
            Pattern.CASE_INSENSITIVE | Pattern.DOTALL
    );

    @Autowired
    private GoldFoilProductService goldFoilProductService;
    @Autowired
    private ProductMapper productMapper;
    @Autowired(required = false)
    private SmartCompatibilityService smartCompatibilityService;
    @Autowired
    private ProductTypeOptionsService productTypeOptionsService;
    @Autowired
    private PreProcessingStepsOptionsService preProcessingStepsOptionsService;

    /**
     * 根据用户问题组装一段纯文本事实块（失败时返回说明性文字，不抛给上层中断对话）。
     */
    public String buildToolContext(String userMessage) {
        String um = userMessage == null ? "" : userMessage.trim();
        StringBuilder sb = new StringBuilder();
        boolean rulesIntent = smartCompatibilityService != null && RULES_INTENT.matcher(um).matches();
        boolean statsIntent = smartCompatibilityService != null && STATS_TRIGGER.matcher(um).matches();
        boolean productTypeIntent = PRODUCT_TYPE_INTENT.matcher(um).matches();
        boolean applicableInterfaceIntent = APPLICABLE_INTERFACE_INTENT.matcher(um).matches();

        if (applicableInterfaceIntent) {
            try {
                List<PreProcessingStepsOptions> steps = preProcessingStepsOptionsService.getAllActiveOptions();
                sb.append("【适用界面(前工序)选项（与烫金页左侧下拉一致，表 pre_processing_steps_options，当前启用）】共 ")
                        .append(steps == null ? 0 : steps.size())
                        .append(" 种。\n");
                if (steps != null) {
                    int n = 0;
                    for (PreProcessingStepsOptions s : steps) {
                        n++;
                        if (n > 80) {
                            sb.append("…（仅列出前 80 条，其余略）\n");
                            break;
                        }
                        sb.append(n).append(". ").append(nullToEmpty(s.getPreProcessingStepsName()))
                                .append(" (id=").append(s.getId() == null ? "?" : s.getId()).append(")\n");
                    }
                }
                sb.append("\n");
            } catch (Exception e) {
                log.warn("读取前工序/适用界面选项失败: {}", e.getMessage());
                sb.append("【适用界面(前工序)】读取失败: ").append(e.getMessage()).append("\n\n");
            }
        }

        if (productTypeIntent) {
            try {
                List<ProductTypeOptions> opts = productTypeOptionsService.getAllActiveOptions();
                sb.append("【产品类型选项（product_type_options，当前启用 is_active）】共 ")
                        .append(opts == null ? 0 : opts.size())
                        .append(" 个。\n");
                if (opts != null) {
                    int n = 0;
                    for (ProductTypeOptions o : opts) {
                        n++;
                        if (n > 60) {
                            sb.append("…（仅列出前 60 条名称，其余略）\n");
                            break;
                        }
                        sb.append(n).append(". ").append(nullToEmpty(o.getProductName()))
                                .append(" (id=").append(o.getId() == null ? "?" : o.getId()).append(")\n");
                    }
                }
                sb.append("\n");
            } catch (Exception e) {
                log.warn("读取产品类型选项失败: {}", e.getMessage());
                sb.append("【产品类型选项】读取失败: ").append(e.getMessage()).append("\n\n");
            }
        }

        try {
            GoldFoilQueryParams p = new GoldFoilQueryParams();
            p.setPageSize(5);
            p.setOffset(0);
            Long total = goldFoilProductService.countProducts(p);
            sb.append("【烫金匹配产品池】未设置筛选条件时，当前可参与匹配的产品总数: ").append(total).append("\n");
            List<GoldFoilProductDTO> sample = goldFoilProductService.getProducts(p);
            sb.append("【示例记录，仅展示前 5 条，字段以系统为准】\n");
            int i = 0;
            for (GoldFoilProductDTO dto : sample) {
                i++;
                sb.append(i).append(". ")
                        .append("系列/名称: ").append(nullToEmpty(dto.getName()))
                        .append(" | 型号: ").append(nullToEmpty(dto.getModelNumber()))
                        .append(" | 烫金纸类型: ").append(nullToEmpty(dto.getHotStampingPaperType()))
                        .append("\n");
            }
            if (sample.isEmpty()) {
                sb.append("(无示例记录)\n");
            }
        } catch (Exception e) {
            log.warn("拉取烫金产品快照失败: {}", e.getMessage());
            sb.append("【烫金匹配产品池】暂时无法读取数据库: ").append(e.getMessage()).append("\n");
        }

        // 问「烫金规则」时不要用整句去搜产品（会 0 条）；若句中含型号再搜产品
        String kw = rulesIntent ? extractModelKeywordOnly(um) : extractSearchKeyword(um);
        if (kw != null) {
            try {
                List<Product> found = productMapper.searchProducts(kw);
                sb.append("\n【关键词模糊搜索】关键词「").append(kw).append("」命中 ").append(found.size()).append(" 条（最多列出 8 条）\n");
                int max = Math.min(8, found.size());
                for (int j = 0; j < max; j++) {
                    Product pr = found.get(j);
                    sb.append(j + 1).append(". ")
                            .append(nullToEmpty(pr.getName()))
                            .append(" | ").append(nullToEmpty(pr.getModelNumber()))
                            .append(" | ").append(nullToEmpty(pr.getHotStampingPaperType()))
                            .append("\n");
                }
            } catch (Exception e) {
                log.warn("关键词搜索失败: {}", e.getMessage());
                sb.append("\n【关键词搜索】失败: ").append(e.getMessage()).append("\n");
            }
        }

        try {
            List<String> types = productMapper.getHotStampingPaperTypeOptions();
            if (types != null && !types.isEmpty()) {
                int show = Math.min(40, types.size());
                sb.append("\n【系统中出现的烫金纸张类型选项（节选 ").append(show).append("/").append(types.size()).append("）】\n");
                sb.append(String.join("、", types.subList(0, show)));
                if (types.size() > show) {
                    sb.append(" …");
                }
                sb.append("\n");
            }
        } catch (Exception e) {
            log.warn("读取烫金纸类型选项失败: {}", e.getMessage());
        }

        if (smartCompatibilityService != null && rulesIntent) {
            appendCompatibilityRulesSample(sb, um);
        }

        if (smartCompatibilityService != null && (statsIntent || rulesIntent)) {
            try {
                Map<String, Object> stats = smartCompatibilityService.getStatistics();
                sb.append("\n【烫金类型兼容规则统计（库内汇总）】\n");
                sb.append(stats.toString()).append("\n");
            } catch (Exception e) {
                log.warn("兼容规则统计失败: {}", e.getMessage());
                sb.append("\n【兼容规则统计】读取失败: ").append(e.getMessage()).append("\n");
            }
        }

        return sb.toString().trim();
    }

    /**
     * 从兼容规则表分页取样本写入上下文（与后台「智能兼容」同源数据）。
     */
    @SuppressWarnings("unchecked")
    private void appendCompatibilityRulesSample(StringBuilder sb, String um) {
        try {
            CompatibilityQueryParams qp = new CompatibilityQueryParams();
            qp.setPage(1);
            qp.setSize(45);
            // 若用户话里除「规则/兼容」外还有明显检索词，可交给 getRules 内过滤（产品/图案/烫金类型字段）
            String refined = refineRuleSearchFromMessage(um);
            if (refined != null && !refined.isBlank()) {
                qp.setSearch(refined);
            }
            Map<String, Object> page = smartCompatibilityService.getRules(qp);
            Object totalObj = page.get("total");
            long total = totalObj instanceof Number ? ((Number) totalObj).longValue() : 0L;
            List<HotStampingCompatibility> items = (List<HotStampingCompatibility>) page.get("items");
            if (items == null) {
                items = List.of();
            }
            sb.append("\n【烫金类型兼容规则（数据库 hot_stamping_compatibility，节选）】\n");
            sb.append("规则总条数（当前筛选条件下）: ").append(total).append("；以下为前 ").append(items.size()).append(" 条。\n");
            sb.append("字段说明：燙金紙性能類型=建议选用的耐磨等级；兼容性 V=适用/兼容，X=不适用；產品類型/圖案特徵/燙金類型 为矩阵条件。\n");
            int n = 0;
            for (HotStampingCompatibility r : items) {
                n++;
                String line = String.format(
                        "%d. 性能[%s] 兼容[%s] | 產品[%s] | 圖案[%s] | 燙金[%s]",
                        n,
                        truncate(nullToEmpty(r.getPaperPerformance()), 24),
                        nullToEmpty(r.getCompatibility()),
                        truncate(nullToEmpty(r.getProductType()), 20),
                        truncate(nullToEmpty(r.getPatternCharacteristic()), 36),
                        truncate(nullToEmpty(r.getHotStampingType()), 28)
                );
                sb.append(line).append("\n");
            }
            if (total > items.size()) {
                sb.append("（其余 ").append(total - items.size()).append(" 条未列出，可在系统「智能兼容规则」中按条件查看。）\n");
            }
        } catch (Exception e) {
            log.warn("读取烫金兼容规则样本失败: {}", e.getMessage());
            sb.append("\n【烫金兼容规则】读取失败: ").append(e.getMessage()).append("\n");
        }
    }

    private static String refineRuleSearchFromMessage(String um) {
        String s = um.replaceAll("(查查|查一下|帮我查|请问|一下|规则|烫金|兼容|有哪些|什么|的|吗|？|\\?)", " ").trim();
        s = s.replaceAll("\\s+", " ");
        if (s.length() < 2 || s.length() > 40) {
            return null;
        }
        return s;
    }

    private static String truncate(String s, int max) {
        if (s == null || s.length() <= max) {
            return s == null ? "" : s;
        }
        return s.substring(0, max) + "…";
    }

    /** 规则类问题里仅提取型号式 token 做产品搜索 */
    private static String extractModelKeywordOnly(String um) {
        Matcher m = MODEL_LIKE.matcher(um);
        return m.find() ? m.group() : null;
    }

    private static String nullToEmpty(String s) {
        return s == null ? "" : s;
    }

    /**
     * 从用户话中提取适合 LIKE 搜索的关键词：优先型号/字母数字片段；否则在较短陈述句中取前若干字。
     */
    private static String extractSearchKeyword(String um) {
        if (um.length() < 2) {
            return null;
        }
        Matcher m = MODEL_LIKE.matcher(um);
        if (m.find()) {
            return m.group();
        }
        if (um.matches(".*[？?什么怎么如何哪].*")) {
            return null;
        }
        if (um.length() > 24) {
            return null;
        }
        return um;
    }

    /**
     * 轻量摘要上下文（不含完整规则列表），用于渐进式注入的第一层。
     */
    public String buildSummaryContext(String userMessage) {
        StringBuilder sb = new StringBuilder();
        try {
            GoldFoilQueryParams p = new GoldFoilQueryParams();
            p.setPageSize(1);
            Long total = goldFoilProductService.countProducts(p);
            sb.append("产品池总数: ").append(total).append("\n");
        } catch (Exception e) {
            sb.append("产品池: 读取失败\n");
        }
        try {
            List<ProductTypeOptions> opts = productTypeOptionsService.getAllActiveOptions();
            sb.append("产品类型: ").append(opts == null ? 0 : opts.size()).append(" 种\n");
        } catch (Exception e) {
            sb.append("产品类型: 读取失败\n");
        }
        try {
            List<PreProcessingStepsOptions> steps = preProcessingStepsOptionsService.getAllActiveOptions();
            sb.append("适用界面(前工序): ").append(steps == null ? 0 : steps.size()).append(" 种\n");
        } catch (Exception e) {
            sb.append("适用界面: 读取失败\n");
        }
        return sb.toString().trim();
    }
}
