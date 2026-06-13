package com.it.yts_project.agent;

import com.it.yts_project.agent.skill.ExecutableSkill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * 简单 Skill 路由器。
 * <p>自动注入所有 {@link ExecutableSkill} Spring Bean，
 * 按实现类的简单类名映射到对应的 {@link Intent}：</p>
 * <ul>
 *   <li>{@code ProductSearchSkill} → {@link Intent#MATCH}</li>
 *   <li>{@code CompatibilitySkill} → {@link Intent#COMPATIBILITY}</li>
 *   <li>{@code SupplierSkill} → {@link Intent#QUERY}</li>
 * </ul>
 */
@Component
public class SimpleSkillRouter {

    private static final Logger log = LoggerFactory.getLogger(SimpleSkillRouter.class);

    private final Map<Intent, ExecutableSkill> skillMap = new EnumMap<>(Intent.class);

    @Autowired
    public SimpleSkillRouter(List<ExecutableSkill> skills) {
        for (ExecutableSkill skill : skills) {
            Intent intent = mapIntent(skill);
            if (intent != null) {
                skillMap.put(intent, skill);
                log.debug("[SimpleSkillRouter] 注册 Skill: {} -> {}", intent, skill.getClass().getSimpleName());
            } else {
                log.warn("[SimpleSkillRouter] 无法映射 Skill: {}", skill.getClass().getSimpleName());
            }
        }
        log.info("[SimpleSkillRouter] 已注册 {} 个 Skill", skillMap.size());
    }

    /**
     * 根据意图获取对应的 Skill。
     *
     * @param intent 用户意图
     * @return 匹配的 Skill；如果没有注册则返回 {@code null}
     */
    public ExecutableSkill route(Intent intent) {
        return skillMap.get(intent);
    }

    /**
     * 获取所有已注册的意图-Skill 映射（只读视图）。
     */
    public Map<Intent, ExecutableSkill> getSkillMap() {
        return skillMap;
    }

    // ========== 辅助方法 ==========

    private static Intent mapIntent(ExecutableSkill skill) {
        String className = skill.getClass().getSimpleName();
        return switch (className) {
            case "ProductSearchSkill" -> Intent.MATCH;
            case "CompatibilitySkill" -> Intent.COMPATIBILITY;
            case "SupplierSkill" -> Intent.QUERY;
            case "AdminModifySkill" -> Intent.ADMIN_MODIFY;
            default -> null;
        };
    }
}
