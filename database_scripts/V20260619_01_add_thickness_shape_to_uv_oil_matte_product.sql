-- =============================================================
-- Migration: Add thickness and shape columns to uv_oil_matte_product
--
-- 审计报告指出的 P1 丢失维度：
-- - 厚度（xlsx 列 L）：物料物理厚度
-- - 形状（xlsx 列 O）：物料形状描述（如卷装/片装）
--
-- 注意：DB 已有 responsible_person 字段但被错误映射为"尺寸"值，
-- 正确的测试员信息在 xlsx 列 P。此迁移不涉及 responsible_person 修复。
-- =============================================================

ALTER TABLE uv_oil_matte_product
    ADD COLUMN IF NOT EXISTS thickness VARCHAR(200) DEFAULT NULL;

ALTER TABLE uv_oil_matte_product
    ADD COLUMN IF NOT EXISTS shape VARCHAR(200) DEFAULT NULL;

-- 回滚脚本
-- ALTER TABLE uv_oil_matte_product DROP COLUMN IF EXISTS thickness;
-- ALTER TABLE uv_oil_matte_product DROP COLUMN IF EXISTS shape;
