package com.it.yts_project.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

/**
 * 啟動時確保 system_content 表存在並插入默認燙金物料指南占位（可後台富文本覆蓋）。
 */
@Component
public class SystemContentTableBootstrap {

    private static final Logger log = LoggerFactory.getLogger(SystemContentTableBootstrap.class);

    private static final String DEFAULT_KEY = "hot_stamping_material_guide";
    private static final String DEFAULT_TITLE = "燙金紙物料數據更新操作指南";
    private static final String DEFAULT_HTML = "<p>歡迎使用本指南。管理員可點擊「編輯指南」更新內容；您也可將 Word 內容直接粘貼到編輯器中。</p>";

    @Autowired
    private DataSource dataSource;

    @Order(50)
    @EventListener(ApplicationReadyEvent.class)
    public void ensureTableAndSeed() {
        try (Connection c = dataSource.getConnection()) {
            try (Statement st = c.createStatement()) {
                st.execute("""
                        CREATE TABLE IF NOT EXISTS system_content (
                            id BIGSERIAL PRIMARY KEY,
                            content_key VARCHAR(128) NOT NULL UNIQUE,
                            title VARCHAR(512) NOT NULL DEFAULT '',
                            body_html TEXT,
                            updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
                            updated_by INTEGER
                        )
                        """);
            }
            String sql = "INSERT INTO system_content (content_key, title, body_html) VALUES (?, ?, ?) ON CONFLICT (content_key) DO NOTHING";
            try (PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setString(1, DEFAULT_KEY);
                ps.setString(2, DEFAULT_TITLE);
                ps.setString(3, DEFAULT_HTML);
                int n = ps.executeUpdate();
                if (n > 0) {
                    log.info("已初始化系統內容占位: {}", DEFAULT_KEY);
                }
            }
        } catch (Exception e) {
            log.warn("system_content 表初始化失敗（若已手動建表可忽略）: {}", e.getMessage());
        }
    }
}
