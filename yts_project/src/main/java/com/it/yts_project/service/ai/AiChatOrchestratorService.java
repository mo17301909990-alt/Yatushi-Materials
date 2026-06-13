package com.it.yts_project.service.ai;

import com.it.yts_project.service.DashScopeChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 编排器：3 层渐进式上下文注入。
 * Layer 1 = BASE_RULES（始终注入）
 * Layer 2 = 系统数据摘要（轻量，始终注入）
 * Layer 3 = 数据库表结构 + RAG 片段（仅需要详细上下文时注入）
 */
@Service
public class AiChatOrchestratorService {

    private static final String BASE_RULES = ""
            + "你是印刷厂管理系统 AI 助手，由雅土仕烫金纸匹配系统驱动。\n"
            + "回答必须优先依据下方【系统实时数据】与【知识库片段】；若其中没有相关信息，请明确说明「当前系统数据中未提供」，不要编造具体型号、规则条数或客户数据。\n"
            + "烫金页左侧「适用界面(前工序)」对应数据库中的前工序选项；若【系统实时数据】里已出现「前工序/适用界面选项」或「产品类型选项」等段落，必须据实回答种类与数量，不要说系统未收录。\n"
            + "【数据库表结构】段落列出 public 模式下各表字段与约行数（元数据），用于回答「有哪些表、某表有哪些字段」；具体业务数值仍以【系统实时数据】为准，勿编造行内数据。\n"
            + "可结合常识解释印刷/烫金术语，但涉及本系统内的产品列表、规则统计、选项值，只能使用【系统实时数据】与【知识库片段】中出现的表述。\n"
            + "若用户询问某型号能否「先烫金后印刷」「烫金后染 UV」「过预涂光胶」等工艺顺序，但未说明适用界面/前工序、布料纸类型等关键条件，应先简短追问 1～2 项（优先问烫于什么界面或前工序），再结合【系统实时数据】作答。\n"
            + "若问题涉及多种实施方案（例如可直接烫金与需丝印打底油），仅当【系统实时数据】或【知识库片段】有明确依据时分条列明；无依据则说明当前数据未细化到该粒度，勿编造。\n"
            + "回答简洁、分点列出关键信息为宜；勿用冗长罗列数据库表名与行数代替业务结论。\n"
            + "\n"
            + "以下是工厂指引书（#N-BF-008 ~ #N-BF-013）萃取的核心知识。如用户询问品质标准、金纸分类、温度参数、工艺顺序、问题解决方案，请结合【知识库片段】回答：\n"
            + "- 金纸价格排序（低→高）：SY1/ST→SY6/G1/GN→SA→SB→E8→KA→KB→L817→S16→S19→镭射系列→色箔系列→YZT\n"
            + "- 金纸按性能分四类：普通金紙、普通耐磨、高耐磨、鐳射燙金紙（具体数量以【系统实时数据】为准）\n"
            + "- 品质标准见【知识库片段】：3M#810胶带测试、电晕笔≥38dyne、耐磨≤10%等\n"
            + "- 过胶规则：光胶先烫后过(所有金纸可用)；哑胶接触须用耐磨纸；不接触则所有金纸可用\n"
            + "- 布面纸三态：✓直接烫、▷需丝印打底(#CB-1202A 77T 8h)、✗不能烫\n"
            + "- 兼容系统三态：V=适用、X=不适用、▷=有条件适用(需测试)。▷存在于hot_stamping_compatibility和cloth_paper_compatibility两个表中\n"
            + "- 耐磨映射路径：标准型号→普通耐磨(ST/GN系列)→高耐磨(TB815系列)\n"
            + "- 每条回答末尾必须标注信息来源：若有【知识库片段】，引用对应段落来源（如 #N-BF-009 §品质标准）；若有【系统实时数据】，标注“系统实时数据”；无明确来源的结论必须说明「当前系统未收录此信息」。\n"
            + "\n"
            + "【卡片输出格式】\n"
            + "当查询到具体产品信息时，在回答末尾附上结构化卡片数据，格式如下：\n"
            + "\n"
            + "===CARD_START===\n"
            + "{\n"
            + "  \"type\": \"product_card\",\n"
            + "  \"data\": {\n"
            + "    \"materialNumber\": \"GP-123\",\n"
            + "    \"color\": \"金色\",\n"
            + "    \"size\": \"640mm×120m\",\n"
            + "    \"tightness\": \"中松\",\n"
            + "    \"compatibility\": {\"hotStamping\": \"compatible\", \"laminating\": \"partial\"},\n"
            + "    \"postProcesses\": [\"过光胶\", \"丝印打底\"]\n"
            + "  }\n"
            + "}\n"
            + "===CARD_END===\n"
            + "\n"
            + "支持三种卡片类型：\n"
            + "1. product_card — 产品信息卡片（materialNumber/color/size/tightness/compatibility/postProcesses）\n"
            + "2. compatibility_table — 兼容性对比表格（headers: string[], rows: string[][]）\n"
            + "3. action_buttons — 推荐操作按钮（actions: {id, label, primary}[]）\n"
            + "\n"
            + "三种触发场景：\n"
            + "- 用户查具体产品 → 附 product_card\n"
            + "- 用户查兼容性对比 → 附 compatibility_table\n"
            + "- 用户问\"接下来怎么做\" → 附 action_buttons\n"
            + "- 纯文本问答/无数据命中 → 不加卡片 block，纯文本不受影响\n"
            + "卡片数据必须基于【系统实时数据】中的真实信息，不要编造。\n";

    @Autowired
    private DashScopeChatService dashScopeChatService;
    @Autowired
    private AiToolDataService aiToolDataService;
    @Autowired
    private AiKnowledgeRetriever aiKnowledgeRetriever;
    @Autowired
    private DatabaseSchemaCatalogService databaseSchemaCatalogService;
    @Autowired
    private AiResourceGroupDataService aiResourceGroupDataService;

    public String chat(String userMessage) {
        return chat(userMessage, null);
    }

    /**
     * 3 层渐进式上下文注入：
     * Layer 1 = BASE_RULES（始终注入）
     * Layer 2 = 系统数据摘要（轻量，始终注入）
     * Layer 3 = 数据库表结构 + RAG（仅当需要详细上下文时）
     */
    public String chat(String userMessage, String extraSystemPrompt) {
        StringBuilder system = new StringBuilder();

        // Layer 1: 基础规则（始终注入）
        system.append(BASE_RULES);

        // Layer 2: 系统数据摘要（轻量，始终注入）
        system.append("\n----------\n【系统实时数据摘要】\n");
        String summary = aiToolDataService.buildSummaryContext(userMessage);
        system.append(summary.isEmpty() ? "(无)" : summary);

        // Layer 2b: 生产助手 — 资源组+产能数据
        // 当 extraSystemPrompt 包含生产相关关键词时注入
        boolean isProductionAssistant = extraSystemPrompt != null
                && (extraSystemPrompt.contains("资源组")
                    || extraSystemPrompt.contains("生产")
                    || extraSystemPrompt.contains("设备")
                    || extraSystemPrompt.contains("工艺路线"));
        if (isProductionAssistant) {
            String rgContext = aiResourceGroupDataService.buildResourceGroupContext();
            if (!rgContext.contains("读取资源组数据失败")) {
                system.append("\n----------\n【资源组与产能数据 — 生产助手专用】\n");
                system.append(rgContext);
            }
        }

        // Layer 3: 详细上下文（仅需时注入）
        if (needsDetailedContext(userMessage)) {
            String toolBlock = aiToolDataService.buildToolContext(userMessage);
            if (!toolBlock.isEmpty()) {
                system.append("\n----------\n【系统实时数据（完整）】\n");
                system.append(toolBlock);
            }

            String schemaBlock = databaseSchemaCatalogService.getCatalogOrRefresh();
            if (schemaBlock != null && !schemaBlock.isBlank()) {
                system.append("\n----------\n【数据库表结构(public)】\n");
                system.append(schemaBlock);
            }

            String ragBlock = aiKnowledgeRetriever.retrieveTop(userMessage, 4, 6000);
            if (!ragBlock.isEmpty()) {
                system.append("\n----------\n【知识库片段（段落末尾 --- 后的 [来源: ...] 为引用标记，回答须标注对应来源）】\n");
                system.append(ragBlock);
            }
        }

        // extra 层放在最后，离用户消息最近，确保角色定义优先级高于 BASE_RULES
        if (extraSystemPrompt != null && !extraSystemPrompt.isBlank()) {
            system.append("\n----------\n【角色指令 — 这是你的核心身份，必须优先遵守于上述所有规则】\n");
            system.append(extraSystemPrompt);
        }

        return dashScopeChatService.chat(system.toString(), userMessage);
    }

    /**
     * 判断是否需要在 Layer 3 注入详细上下文（完整数据 + 知识库）。
     * 简单问候/打招呼不需要全套数据。
     */
    private static boolean needsDetailedContext(String message) {
        if (message == null || message.isBlank()) return false;
        // 简短问候不需要
        String m = message.trim();
        if (m.length() <= 4) return false;
        // 典型的打招呼
        String lower = m.toLowerCase();
        if (lower.matches("^(你好|您好|hi|hello|hey|早|嗨|在吗|在不在|早安|下午好|晚上好)\\s*$")) {
            return false;
        }
        // 其余情况注入完整上下文
        return true;
    }
}
