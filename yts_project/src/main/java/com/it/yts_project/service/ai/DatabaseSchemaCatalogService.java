package com.it.yts_project.service.ai;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 从 PostgreSQL 元数据生成 public 模式下「用户表」的结构目录（表名、约行数、字段与类型），
 * 供 AI 对话注入。不包含表内业务数据，避免体积与隐私问题。
 */
@Service
public class DatabaseSchemaCatalogService {

    private static final Logger log = LoggerFactory.getLogger(DatabaseSchemaCatalogService.class);

    /** 目录缓存 TTL，避免每次对话都扫 information_schema */
    private static final long CACHE_TTL_MS = 15 * 60 * 1000L;

    private final JdbcTemplate jdbcTemplate;

    @Value("${ai.chat.schema-catalog.max-chars:80000}")
    private int maxCatalogChars;

    private volatile String cachedCatalog;
    private volatile long cacheExpiresAtMs;

    @Autowired
    public DatabaseSchemaCatalogService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * 返回可注入大模型的纯文本目录；带缓存与长度截断。
     */
    public String getCatalogOrRefresh() {
        long now = System.currentTimeMillis();
        if (cachedCatalog != null && now < cacheExpiresAtMs) {
            return cachedCatalog;
        }
        synchronized (this) {
            if (cachedCatalog != null && now < cacheExpiresAtMs) {
                return cachedCatalog;
            }
            try {
                cachedCatalog = buildCatalog();
            } catch (Exception e) {
                log.warn("构建数据库表结构目录失败: {}", e.getMessage());
                cachedCatalog = "(读取数据库元数据失败: " + e.getMessage() + ")\n";
            }
            cacheExpiresAtMs = System.currentTimeMillis() + CACHE_TTL_MS;
            return cachedCatalog;
        }
    }

    /** 管理或测试时可调用，使下次对话重新拉取元数据 */
    public void invalidateCache() {
        synchronized (this) {
            cachedCatalog = null;
            cacheExpiresAtMs = 0L;
        }
    }

    private String buildCatalog() {
        List<String> tables = jdbcTemplate.query(
                """
                        SELECT table_name FROM information_schema.tables
                        WHERE table_schema = 'public' AND table_type = 'BASE TABLE'
                        ORDER BY table_name
                        """,
                (rs, rowNum) -> rs.getString("table_name")
        );

        Map<String, Long> rowEstimates = new HashMap<>();
        try {
            List<Map<String, Object>> statRows = jdbcTemplate.queryForList(
                    "SELECT relname, COALESCE(n_live_tup::bigint, 0) AS n FROM pg_stat_user_tables WHERE schemaname = 'public'"
            );
            for (Map<String, Object> row : statRows) {
                Object rel = row.get("relname");
                Object n = row.get("n");
                if (rel != null && n instanceof Number) {
                    rowEstimates.put(rel.toString(), ((Number) n).longValue());
                }
            }
        } catch (Exception e) {
            log.debug("读取 pg_stat_user_tables 失败（可忽略）: {}", e.getMessage());
        }

        Map<String, String> primaryKeys = new HashMap<>();
        try {
            List<Map<String, Object>> pkRows = jdbcTemplate.queryForList(
                    """
                            SELECT tc.table_name,
                                   string_agg(kcu.column_name, ', ' ORDER BY kcu.ordinal_position) AS pk_cols
                            FROM information_schema.table_constraints tc
                            JOIN information_schema.key_column_usage kcu
                              ON tc.constraint_name = kcu.constraint_name
                             AND tc.table_schema = kcu.table_schema
                             AND tc.table_name = kcu.table_name
                            WHERE tc.table_schema = 'public' AND tc.constraint_type = 'PRIMARY KEY'
                            GROUP BY tc.table_name
                            """
            );
            for (Map<String, Object> row : pkRows) {
                Object tn = row.get("table_name");
                Object pk = row.get("pk_cols");
                if (tn != null && pk != null) {
                    primaryKeys.put(tn.toString(), pk.toString());
                }
            }
        } catch (Exception e) {
            log.debug("读取主键失败（可忽略）: {}", e.getMessage());
        }

        List<ColRow> allCols = jdbcTemplate.query(
                """
                        SELECT table_name, column_name, data_type, udt_name,
                               character_maximum_length, numeric_precision, numeric_scale, is_nullable
                        FROM information_schema.columns
                        WHERE table_schema = 'public'
                          AND table_name IN (
                            SELECT table_name FROM information_schema.tables
                            WHERE table_schema = 'public' AND table_type = 'BASE TABLE'
                          )
                        ORDER BY table_name, ordinal_position
                        """,
                (rs, rowNum) -> new ColRow(
                        rs.getString("table_name"),
                        rs.getString("column_name"),
                        rs.getString("data_type"),
                        rs.getString("udt_name"),
                        (Integer) rs.getObject("character_maximum_length"),
                        (Integer) rs.getObject("numeric_precision"),
                        (Integer) rs.getObject("numeric_scale"),
                        "YES".equalsIgnoreCase(rs.getString("is_nullable"))
                )
        );

        Map<String, List<ColRow>> byTable = allCols.stream()
                .collect(Collectors.groupingBy(c -> c.tableName, LinkedHashMap::new, Collectors.toList()));

        StringBuilder sb = new StringBuilder();
        sb.append("【说明】以下为当前连接库 public 模式下用户表（BASE TABLE）的结构与字段类型；约行数来自 pg_stat_user_tables.n_live_tup（统计估算，非实时精确 COUNT）。不含表内具体业务数据。\n");
        sb.append("【表数量】").append(tables.size()).append("\n\n");

        for (String t : tables) {
            StringBuilder section = new StringBuilder();
            long approx = rowEstimates.getOrDefault(t, -1L);
            section.append("### ").append(t);
            if (approx >= 0) {
                section.append(" （约 ").append(approx).append(" 行）");
            }
            section.append("\n");
            String pk = primaryKeys.get(t);
            if (pk != null && !pk.isBlank()) {
                section.append("主键: ").append(pk).append("\n");
            }
            List<ColRow> cols = byTable.getOrDefault(t, List.of());
            for (ColRow c : cols) {
                section.append("- ").append(c.columnName).append(": ").append(formatType(c)).append("\n");
            }
            section.append("\n");

            if (sb.length() + section.length() > maxCatalogChars) {
                sb.append("\n…（目录已超过 ai.chat.schema-catalog.max-chars=").append(maxCatalogChars)
                        .append("，后续表已省略；可增大该配置或缩小库表。）\n");
                break;
            }
            sb.append(section);
        }

        String out = sb.toString();
        if (out.length() > maxCatalogChars) {
            return out.substring(0, maxCatalogChars) + "\n…（已截断）\n";
        }
        return out;
    }

    private static String formatType(ColRow c) {
        String dt = c.dataType != null ? c.dataType : "";
        String udt = c.udtName != null ? c.udtName : "";
        if (("character varying".equalsIgnoreCase(dt) || "varchar".equalsIgnoreCase(udt)) && c.charMaxLen != null) {
            return "varchar(" + c.charMaxLen + ")" + nullSuffix(c.nullable);
        }
        if ("character".equalsIgnoreCase(dt) && c.charMaxLen != null) {
            return "char(" + c.charMaxLen + ")" + nullSuffix(c.nullable);
        }
        if (("numeric".equalsIgnoreCase(dt) || "decimal".equalsIgnoreCase(dt)) && c.numPrec != null) {
            String s = "numeric";
            if (c.numPrec != null) {
                s += "(" + c.numPrec;
                if (c.numScale != null) {
                    s += "," + c.numScale;
                }
                s += ")";
            }
            return s + nullSuffix(c.nullable);
        }
        String base = !udt.isBlank() ? udt : dt;
        return base + nullSuffix(c.nullable);
    }

    private static String nullSuffix(boolean nullable) {
        return nullable ? " NULL" : " NOT NULL";
    }

    private record ColRow(
            String tableName,
            String columnName,
            String dataType,
            String udtName,
            Integer charMaxLen,
            Integer numPrec,
            Integer numScale,
            boolean nullable
    ) {
    }
}
