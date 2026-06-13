package com.it.yts_project.service;

import com.it.yts_project.dto.*;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AgentService {

    private static final Logger log = LoggerFactory.getLogger(AgentService.class);

    @Value("${agent.material.base-path:G:/雅图仕/download/}")
    private String materialBasePath;

    private final Map<String, MaterialCategoryDTO> categoryCache = new LinkedHashMap<>();

    @PostConstruct
    public void init() {
        refreshStandards();
    }

    /** 扫描文件系统，刷新标准书缓存 */
    public void refreshStandards() {
        categoryCache.clear();
        File baseDir = new File(materialBasePath);
        if (!baseDir.isDirectory()) {
            log.warn("材料标准书目录不存在: {}", materialBasePath);
            return;
        }

        File[] subDirs = baseDir.listFiles(File::isDirectory);
        if (subDirs == null) return;

        for (File dir : subDirs) {
            String dirName = dir.getName();
            var category = buildCategory(dirName, dir);
            if (category != null) {
                categoryCache.put(category.getId(), category);
            }
        }
        log.info("已加载 {} 个材料分类, {} 份标准书", categoryCache.size(),
            categoryCache.values().stream().mapToInt(MaterialCategoryDTO::getCount).sum());
    }

    private MaterialCategoryDTO buildCategory(String dirName, File dir) {
        CategoryMeta meta = CATEGORY_MAP.get(dirName);
        if (meta == null) return null;

        File[] files = dir.listFiles(f -> f.isFile() && isSupportedFile(f.getName()));
        if (files == null) return null;

        List<MaterialStandardDTO> standards = Arrays.stream(files)
            .map(this::buildStandard)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());

        return MaterialCategoryDTO.builder()
            .id(meta.id)
            .name(meta.name)
            .icon(meta.icon)
            .count(standards.size())
            .standards(standards)
            .build();
    }

    private MaterialStandardDTO buildStandard(File file) {
        try {
            String name = file.getName();
            BasicFileAttributes attrs = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
            String updatedAt = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                .withZone(ZoneId.systemDefault())
                .format(attrs.lastModifiedTime().toInstant());

            String dirName = file.getParentFile().getName();
            CategoryMeta meta = CATEGORY_MAP.get(dirName);
            String categoryId = meta != null ? meta.id : dirName;
            List<String> tags = extractTags(name, meta);

            return MaterialStandardDTO.builder()
                .id(categoryId + "-" + UUID.randomUUID().toString().substring(0, 8))
                .title(cleanTitle(name))
                .fileName(name)
                .filePath(file.getParent() + "/")
                .updatedAt(updatedAt)
                .category(categoryId)
                .tags(tags)
                .build();
        } catch (Exception e) {
            log.warn("读取标准书文件信息失败: {}", file.getName(), e);
            return null;
        }
    }

    private String cleanTitle(String fileName) {
        // "硅胶丝印UV标准书-20250927 (4).xlsx" -> "硅胶丝印UV标准书"
        int idx = fileName.lastIndexOf('-');
        if (idx > 0) {
            String candidate = fileName.substring(0, idx);
            if (candidate.length() > 4) return candidate;
        }
        // 去掉扩展名
        int dot = fileName.lastIndexOf('.');
        return dot > 0 ? fileName.substring(0, dot) : fileName;
    }

    private List<String> extractTags(String fileName, CategoryMeta meta) {
        List<String> tags = new ArrayList<>();
        if (meta != null) tags.add(meta.name);
        // 从文件名中提取关键词（含繁简体）
        String base = fileName.toLowerCase();
        if (base.contains("uv")) tags.add("UV");
        if (base.contains("硅胶")) tags.add("硅胶");
        if (base.contains("丝印") || base.contains("絲印")) tags.add("丝印");
        if (base.contains("纸品") || base.contains("纸") || base.contains("紙")) tags.add("纸品");
        if (base.contains("后加工") || base.contains("後加工")) tags.add("后加工");
        if (base.contains("哑光")) tags.add("哑光");
        if (base.contains("水油")) tags.add("水油");
        if (base.contains("过胶") || base.contains("過膠")) tags.add("过胶");
        if (base.contains("耐磨")) tags.add("耐磨");
        if (base.contains("布面紙") || base.contains("布面纸")) tags.add("布面纸");
        if (base.contains("凸字粉")) tags.add("凸字粉");
        if (base.contains("灑閃粉") || base.contains("洒闪粉")) tags.add("洒闪粉");
        return tags;
    }

    private boolean isSupportedFile(String name) {
        String lower = name.toLowerCase();
        return lower.endsWith(".xlsx") || lower.endsWith(".xls") || lower.endsWith(".docx") || lower.endsWith(".pdf");
    }

    // ====== Agent 配置 ======

    public AgentConfigDTO getAgentConfig() {
        return AgentConfigDTO.builder()
            .id("process-agent-001")
            .name("工艺 Agent")
            .role("工艺知识专家")
            .department("生产技术部")
            .description("负责印刷工艺材料知识管理，涵盖烫金、丝印、过胶、UV 印刷、LED UV 等工艺的标准书与匹配规则。")
            .status("online")
            .skills(buildDefaultSkills())
            .materialCategories(new ArrayList<>(categoryCache.values()))
            .build();
    }

    public List<MaterialCategoryDTO> getCategories() {
        return new ArrayList<>(categoryCache.values());
    }

    public MaterialCategoryDTO getCategory(String categoryId) {
        return categoryCache.get(categoryId);
    }

    public List<MaterialStandardDTO> getStandardsByCategory(String categoryId) {
        MaterialCategoryDTO cat = categoryCache.get(categoryId);
        return cat != null ? cat.getStandards() : List.of();
    }

    private List<AgentSkillDTO> buildDefaultSkills() {
        return List.of(
            AgentSkillDTO.builder().id("s1").name("烫金工艺匹配")
                .description("根据产品类型、图案、烫金类型推荐合适的烫金纸系列")
                .triggerEvents(List.of("hotstamping:match")).build(),
            AgentSkillDTO.builder().id("s2").name("丝印 UV 匹配")
                .description("硅胶丝印 UV 油墨与材料的兼容性匹配")
                .triggerEvents(List.of("silkscreen:match")).build(),
            AgentSkillDTO.builder().id("s3").name("过胶工艺匹配")
                .description("覆膜/过胶材料与纸张的兼容性匹配")
                .triggerEvents(List.of("lamination:match")).build(),
            AgentSkillDTO.builder().id("s4").name("后加工匹配")
                .description("印刷后加工工艺（UV、击凸、压纹等）的选型建议")
                .triggerEvents(List.of("postprocess:match")).build(),
            AgentSkillDTO.builder().id("s5").name("材料兼容性查询")
                .description("查询材料三态兼容性（V/▷/X）规则")
                .triggerEvents(List.of("compatibility:query")).build()
        );
    }

    // ====== 分类映射 ======

    private static final Map<String, CategoryMeta> CATEGORY_MAP = new LinkedHashMap<>();

    static {
        CATEGORY_MAP.put("硅胶", new CategoryMeta("silicone", "硅胶材料", "🧪"));
        CATEGORY_MAP.put("UV油墨", new CategoryMeta("uv-ink", "UV 油墨", "🎨"));
        CATEGORY_MAP.put("LEO纸品", new CategoryMeta("leo-paper", "LEO 纸品", "📄"));
        CATEGORY_MAP.put("印刷加工", new CategoryMeta("print-processing", "印刷加工", "🖨️"));
        CATEGORY_MAP.put("烫金", new CategoryMeta("hotstamping", "烫金材料", "✨"));
    }

    private record CategoryMeta(String id, String name, String icon) {}
}
