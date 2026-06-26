#!/usr/bin/env python3
"""
Generate ALL 12 silicone category matching pages following the UvOilMatte pattern.

Usage: python database_scripts/generate_silicone_categories.py

After running, also navigate to yts_project_vueai/src/router/index.ts
and add the import/routes for each new page (they are appended to the file).
"""

import os

CATEGORIES = [
    # (JavaClass, table_name, hyphen_slug, display_name)
    ("SiliconeGlowInk",           "silicone_glow_ink",           "silicone-glow-ink",           "硅胶发光油墨"),
    ("SiliconeWhiteUv",           "silicone_white_uv",           "silicone-white-uv",           "硅胶白UV"),
    ("SiliconeGlitteringStar",    "silicone_glittering_star",    "silicone-glittering-star",    "硅胶Glittering-Star"),
    ("SiliconeScreenUv",          "silicone_screen_uv",          "silicone-screen-uv",          "硅胶丝印UV"),
    ("SiliconeLedUvSpray",        "silicone_led_uv_spray",       "silicone-led-uv-spray",       "硅胶LED UV喷涂"),
    ("SiliconeWaterBaseTransparent", "silicone_water_base_transparent", "silicone-water-base-transparent", "硅胶水性透明白墨"),
    ("SiliconeCoarseUv",          "silicone_coarse_uv",          "silicone-coarse-uv",          "硅胶粗纹UV"),
    ("SiliconeOrangePeelUv",      "silicone_orange_peel_uv",     "silicone-orange-peel-uv",     "硅胶桔纹UV"),
    ("SiliconeSandblastUv",       "silicone_sandblast_uv",       "silicone-sandblast-uv",       "硅胶磨砂UV"),
    ("SiliconeWrinkleUv",         "silicone_wrinkle_uv",         "silicone-wrinkle-uv",         "硅胶皱纹UV"),
    ("SiliconeWatercolorInk",     "silicone_watercolor_ink",     "silicone-watercolor-ink",     "硅胶水彩油墨"),
    ("SiliconeMicaPearl",         "silicone_mica_pearl",         "silicone-mica-pearl",         "硅胶Mica Pearl"),
]

def camel_to_lower_first(s):
    return s[0].lower() + s[1:]

def generate_backend(base_dir):
    src = os.path.join(base_dir, "yts_project", "src", "main", "java", "com", "it", "yts_project")
    res = os.path.join(base_dir, "yts_project", "src", "main", "resources", "mapper")
    pkg = "com.it.yts_project"

    for cls, tn, hyphen, display in CATEGORIES:
        mf = camel_to_lower_first(cls)  # e.g. siliconeGlowInk
        mapper_field = mf + "Mapper"
        service_field = mf + "Service"
        print(f"\n  [{cls}] {display}")

        # --- Product Model ---
        path = os.path.join(src, "model", f"{cls}Product.java")
        if not os.path.exists(path):
            write_j(path, f"""package {pkg}.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class {cls}Product {{
    private Integer id;
    private String materialCode;
    private String supplierCode;
    private String stockCode;
    private String materialName;
    private String usage;
    private String category;
    private String color;
    private String responsiblePerson;
    private String minSheetSize;
    private String maxSheetSize;
    private String minSpacing;
    private String designInfo;
    private String applicableInterface;
    private String notes;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}}
""")
        else:
            print(f"  (skip model product)")

        # --- Compatibility Model ---
        path = os.path.join(src, "model", f"{cls}Compatibility.java")
        if not os.path.exists(path):
            write_j(path, f"""package {pkg}.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class {cls}Compatibility {{
    private Integer id;
    private Integer productId;
    private String postProcessingStep;
    private String compatibilityStatus;
    private Integer displayOrder;
    private LocalDateTime createdAt;
    private String productName;
}}
""")
        else:
            print(f"  (skip model compatibility)")

        # --- ProductDTO ---
        path = os.path.join(src, "dto", f"{cls}ProductDTO.java")
        write_j(path, f"""package {pkg}.dto;

import {pkg}.model.{cls}Compatibility;
import {pkg}.model.{cls}Product;
import lombok.Data;
import java.util.List;

@Data
public class {cls}ProductDTO {{
    private {cls}Product product;
    private List<{cls}Compatibility> compatibilities;
}}
""")

        # --- QueryParams ---
        path = os.path.join(src, "dto", f"{cls}QueryParams.java")
        write_j(path, f"""package {pkg}.dto;

import lombok.Data;

@Data
public class {cls}QueryParams {{
    private String keyword;
    private String stepName;
    private Integer page = 1;
    private Integer size = 15;
}}
""")

        # --- Mapper ---
        path = os.path.join(src, "mapper", f"{cls}Mapper.java")
        write_j(path, f"""package {pkg}.mapper;

import {pkg}.model.{cls}Product;
import {pkg}.model.{cls}Compatibility;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface {cls}Mapper {{
    List<{cls}Product> findAllProducts();
    {cls}Product findProductById(@Param("id") Integer id);
    List<{cls}Product> searchProducts(@Param("keyword") String keyword);
    int insertProduct({cls}Product product);
    int updateProduct({cls}Product product);
    int deleteProductById(@Param("id") Integer id);

    List<{cls}Compatibility> findCompatibilitiesByProductId(@Param("productId") Integer productId);
    {cls}Compatibility findCompatibilityById(@Param("id") Integer id);
    int insertCompatibility({cls}Compatibility compatibility);
    int updateCompatibility({cls}Compatibility compatibility);
    int deleteCompatibilityById(@Param("id") Integer id);
    int deleteCompatibilitiesByProductId(@Param("productId") Integer productId);
    int batchInsertCompatibilities(List<{cls}Compatibility> compatibilities);
    {cls}Compatibility findCompatibilityByUniqueKey(
            @Param("productId") Integer productId,
            @Param("postProcessingStep") String postProcessingStep);

    List<{cls}Product> searchProductsWithStep(
            @Param("keyword") String keyword,
            @Param("stepName") String stepName,
            @Param("offset") Integer offset,
            @Param("limit") Integer limit);
    Long countProductsWithStep(
            @Param("keyword") String keyword,
            @Param("stepName") String stepName);
    List<String> getDistinctPostProcessingSteps();
}}
""")

        # --- Mapper XML ---
        path = os.path.join(res, f"{cls}Mapper.xml")
        write_j(path, f"""<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="{pkg}.mapper.{cls}Mapper">

    <resultMap id="ProductResultMap" type="{pkg}.model.{cls}Product">
        <id property="id" column="id"/>
        <result property="materialCode" column="material_code"/>
        <result property="supplierCode" column="supplier_code"/>
        <result property="stockCode" column="stock_code"/>
        <result property="materialName" column="material_name"/>
        <result property="usage" column="usage"/>
        <result property="category" column="category"/>
        <result property="color" column="color"/>
        <result property="responsiblePerson" column="responsible_person"/>
        <result property="minSheetSize" column="min_sheet_size"/>
        <result property="maxSheetSize" column="max_sheet_size"/>
        <result property="minSpacing" column="min_spacing"/>
        <result property="designInfo" column="design_info"/>
        <result property="applicableInterface" column="applicable_interface"/>
        <result property="notes" column="notes"/>
        <result property="isActive" column="is_active"/>
        <result property="createdAt" column="created_at"/>
        <result property="updatedAt" column="updated_at"/>
    </resultMap>

    <resultMap id="CompatibilityResultMap" type="{pkg}.model.{cls}Compatibility">
        <id property="id" column="id"/>
        <result property="productId" column="product_id"/>
        <result property="postProcessingStep" column="post_processing_step"/>
        <result property="compatibilityStatus" column="compatibility_status"/>
        <result property="displayOrder" column="display_order"/>
        <result property="createdAt" column="created_at"/>
        <result property="productName" column="product_name"/>
    </resultMap>

    <select id="findAllProducts" resultMap="ProductResultMap">
        SELECT * FROM {tn}_product ORDER BY material_code ASC, id ASC
    </select>

    <select id="findProductById" resultMap="ProductResultMap">
        SELECT * FROM {tn}_product WHERE id = #{{id}}
    </select>

    <select id="searchProducts" resultMap="ProductResultMap">
        SELECT * FROM {tn}_product
        WHERE material_name LIKE CONCAT('%', #{{keyword}}, '%')
           OR material_code LIKE CONCAT('%', #{{keyword}}, '%')
           OR stock_code LIKE CONCAT('%', #{{keyword}}, '%')
           OR notes LIKE CONCAT('%', #{{keyword}}, '%')
        ORDER BY material_code ASC, id ASC
    </select>

    <insert id="insertProduct" parameterType="{pkg}.model.{cls}Product" useGeneratedKeys="true" keyProperty="id">
        INSERT INTO {tn}_product (material_code, supplier_code, stock_code, material_name, usage,
            category, color, responsible_person, min_sheet_size, max_sheet_size,
            min_spacing, design_info, applicable_interface, notes, is_active)
        VALUES (#{{materialCode}}, #{{supplierCode}}, #{{stockCode}}, #{{materialName}}, #{{usage}},
            #{{category}}, #{{color}}, #{{responsiblePerson}}, #{{minSheetSize}}, #{{maxSheetSize}},
            #{{minSpacing}}, #{{designInfo}}, #{{applicableInterface}}, #{{notes}}, #{{isActive}})
    </insert>

    <update id="updateProduct" parameterType="{pkg}.model.{cls}Product">
        UPDATE {tn}_product SET
            material_code = #{{materialCode}}, supplier_code = #{{supplierCode}},
            stock_code = #{{stockCode}}, material_name = #{{materialName}},
            usage = #{{usage}}, category = #{{category}}, color = #{{color}},
            responsible_person = #{{responsiblePerson}},
            min_sheet_size = #{{minSheetSize}}, max_sheet_size = #{{maxSheetSize}},
            min_spacing = #{{minSpacing}}, design_info = #{{designInfo}},
            applicable_interface = #{{applicableInterface}}, notes = #{{notes}},
            is_active = #{{isActive}}, updated_at = CURRENT_TIMESTAMP
        WHERE id = #{{id}}
    </update>

    <delete id="deleteProductById">
        DELETE FROM {tn}_product WHERE id = #{{id}}
    </delete>

    <select id="findCompatibilitiesByProductId" resultMap="CompatibilityResultMap">
        SELECT c.*, p.material_name AS product_name
        FROM {tn}_compatibility c LEFT JOIN {tn}_product p ON c.product_id = p.id
        WHERE c.product_id = #{{productId}}
        ORDER BY c.display_order ASC, c.id ASC
    </select>

    <select id="findCompatibilityById" resultMap="CompatibilityResultMap">
        SELECT c.*, p.material_name AS product_name
        FROM {tn}_compatibility c LEFT JOIN {tn}_product p ON c.product_id = p.id
        WHERE c.id = #{{id}}
    </select>

    <insert id="insertCompatibility" parameterType="{pkg}.model.{cls}Compatibility" useGeneratedKeys="true" keyProperty="id">
        INSERT INTO {tn}_compatibility (product_id, post_processing_step, compatibility_status, display_order)
        VALUES (#{{productId}}, #{{postProcessingStep}}, #{{compatibilityStatus}}, #{{displayOrder}})
    </insert>

    <update id="updateCompatibility" parameterType="{pkg}.model.{cls}Compatibility">
        UPDATE {tn}_compatibility SET
            product_id = #{{productId}}, post_processing_step = #{{postProcessingStep}},
            compatibility_status = #{{compatibilityStatus}}, display_order = #{{displayOrder}}
        WHERE id = #{{id}}
    </update>

    <delete id="deleteCompatibilityById">
        DELETE FROM {tn}_compatibility WHERE id = #{{id}}
    </delete>

    <delete id="deleteCompatibilitiesByProductId">
        DELETE FROM {tn}_compatibility WHERE product_id = #{{productId}}
    </delete>

    <insert id="batchInsertCompatibilities" parameterType="java.util.List">
        INSERT INTO {tn}_compatibility (product_id, post_processing_step, compatibility_status, display_order)
        VALUES
        <foreach collection="list" item="item" separator=",">
            (#{{item.productId}}, #{{item.postProcessingStep}}, #{{item.compatibilityStatus}}, #{{item.displayOrder}})
        </foreach>
    </insert>

    <select id="findCompatibilityByUniqueKey" resultMap="CompatibilityResultMap">
        SELECT c.*, p.material_name AS product_name
        FROM {tn}_compatibility c LEFT JOIN {tn}_product p ON c.product_id = p.id
        WHERE c.product_id = #{{productId}} AND c.post_processing_step = #{{postProcessingStep}}
    </select>

    <select id="searchProductsWithStep" resultMap="ProductResultMap">
        SELECT DISTINCT p.* FROM {tn}_product p
        <if test="stepName != null and stepName != ''">
            INNER JOIN {tn}_compatibility c ON p.id = c.product_id
        </if>
        <where>
            <if test="keyword != null and keyword != ''">
                AND (p.material_name LIKE CONCAT('%', #{{keyword}}, '%')
                   OR p.material_code LIKE CONCAT('%', #{{keyword}}, '%')
                   OR p.stock_code LIKE CONCAT('%', #{{keyword}}, '%')
                   OR p.notes LIKE CONCAT('%', #{{keyword}}, '%'))
            </if>
            <if test="stepName != null and stepName != ''">
                AND c.post_processing_step = #{{stepName}}
            </if>
        </where>
        ORDER BY p.material_code ASC, p.id ASC
        LIMIT #{{limit}} OFFSET #{{offset}}
    </select>

    <select id="countProductsWithStep" resultType="java.lang.Long">
        SELECT COUNT(DISTINCT p.id) FROM {tn}_product p
        <if test="stepName != null and stepName != ''">
            INNER JOIN {tn}_compatibility c ON p.id = c.product_id
        </if>
        <where>
            <if test="keyword != null and keyword != ''">
                AND (p.material_name LIKE CONCAT('%', #{{keyword}}, '%')
                   OR p.material_code LIKE CONCAT('%', #{{keyword}}, '%')
                   OR p.stock_code LIKE CONCAT('%', #{{keyword}}, '%')
                   OR p.notes LIKE CONCAT('%', #{{keyword}}, '%'))
            </if>
            <if test="stepName != null and stepName != ''">
                AND c.post_processing_step = #{{stepName}}
            </if>
        </where>
    </select>

    <select id="getDistinctPostProcessingSteps" resultType="java.lang.String">
        SELECT DISTINCT post_processing_step FROM {tn}_compatibility ORDER BY post_processing_step ASC
    </select>

</mapper>
""")

        # --- Service interface ---
        path = os.path.join(src, "service", f"{cls}Service.java")
        write_j(path, f"""package {pkg}.service;

import {pkg}.dto.{cls}ProductDTO;
import {pkg}.dto.PagedResult;
import {pkg}.model.{cls}Product;
import {pkg}.model.{cls}Compatibility;
import java.util.List;

public interface {cls}Service {{
    List<{cls}Product> getAllProducts();
    {cls}Product getProductById(Integer id);
    List<{cls}Product> searchProducts(String keyword);
    {cls}Product saveProduct({cls}Product product);
    void deleteProduct(Integer id);

    List<{cls}Compatibility> getCompatibilitiesByProductId(Integer productId);
    {cls}Compatibility getCompatibilityById(Integer id);
    {cls}Compatibility saveCompatibility({cls}Compatibility compatibility);
    void deleteCompatibility(Integer id);
    void batchSaveCompatibilities(List<{cls}Compatibility> compatibilities);

    PagedResult<{cls}Product> searchProducts(String keyword, String stepName, int page, int size);
    List<String> getDistinctSteps();
    {cls}ProductDTO getProductDetail(Integer id);
}}
""")

        # --- Service Impl ---
        path = os.path.join(src, "service", "Impl", f"{cls}ServiceImpl.java")
        write_j(path, f"""package {pkg}.service.Impl;

import {pkg}.dto.{cls}ProductDTO;
import {pkg}.dto.PagedResult;
import {pkg}.mapper.{cls}Mapper;
import {pkg}.model.{cls}Product;
import {pkg}.model.{cls}Compatibility;
import {pkg}.service.{cls}Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class {cls}ServiceImpl implements {cls}Service {{

    @Autowired
    private {cls}Mapper {mapper_field};

    @Override
    public List<{cls}Product> getAllProducts() {{ return {mapper_field}.findAllProducts(); }}

    @Override
    public {cls}Product getProductById(Integer id) {{ return {mapper_field}.findProductById(id); }}

    @Override
    public List<{cls}Product> searchProducts(String keyword) {{ return {mapper_field}.searchProducts(keyword); }}

    @Override
    public {cls}Product saveProduct({cls}Product product) {{
        if (product.getId() == null) {{ {mapper_field}.insertProduct(product); }}
        else {{ {mapper_field}.updateProduct(product); }}
        return product;
    }}

    @Override
    public void deleteProduct(Integer id) {{ {mapper_field}.deleteProductById(id); }}

    @Override
    public List<{cls}Compatibility> getCompatibilitiesByProductId(Integer productId) {{
        return {mapper_field}.findCompatibilitiesByProductId(productId);
    }}

    @Override
    public {cls}Compatibility getCompatibilityById(Integer id) {{
        return {mapper_field}.findCompatibilityById(id);
    }}

    @Override
    public {cls}Compatibility saveCompatibility({cls}Compatibility compatibility) {{
        if (compatibility.getProductId() == null) throw new IllegalArgumentException("产品ID不能为空");
        {cls}Product product = {mapper_field}.findProductById(compatibility.getProductId());
        if (product == null) throw new IllegalArgumentException("产品ID " + compatibility.getProductId() + " 不存在");
        if (compatibility.getId() == null) {{
            {cls}Compatibility existing = {mapper_field}.findCompatibilityByUniqueKey(
                compatibility.getProductId(), compatibility.getPostProcessingStep());
            if (existing != null) throw new IllegalArgumentException("该产品与后加工工序的兼容性配置已存在");
            {mapper_field}.insertCompatibility(compatibility);
        }} else {{
            {cls}Compatibility existing = {mapper_field}.findCompatibilityByUniqueKey(
                compatibility.getProductId(), compatibility.getPostProcessingStep());
            if (existing != null && !existing.getId().equals(compatibility.getId()))
                throw new IllegalArgumentException("该产品与后加工工序的兼容性配置已存在");
            {mapper_field}.updateCompatibility(compatibility);
        }}
        compatibility.setProductName(product.getMaterialName());
        return compatibility;
    }}

    @Override
    public void deleteCompatibility(Integer id) {{ {mapper_field}.deleteCompatibilityById(id); }}

    @Override
    public void batchSaveCompatibilities(List<{cls}Compatibility> compatibilities) {{
        if (compatibilities == null || compatibilities.isEmpty()) return;
        for ({cls}Compatibility comp : compatibilities) {{
            if (comp.getProductId() == null) throw new IllegalArgumentException("产品ID不能为空");
            {cls}Product product = {mapper_field}.findProductById(comp.getProductId());
            if (product == null) throw new IllegalArgumentException("产品ID " + comp.getProductId() + " 不存在");
            {cls}Compatibility existing = {mapper_field}.findCompatibilityByUniqueKey(
                comp.getProductId(), comp.getPostProcessingStep());
            if (existing != null) {{ comp.setId(existing.getId()); {mapper_field}.updateCompatibility(comp); }}
            else {{ {mapper_field}.insertCompatibility(comp); }}
        }}
    }}

    @Override
    public PagedResult<{cls}Product> searchProducts(String keyword, String stepName, int page, int size) {{
        int offset = (page - 1) * size;
        List<{cls}Product> items = {mapper_field}.searchProductsWithStep(keyword, stepName, offset, size);
        Long total = {mapper_field}.countProductsWithStep(keyword, stepName);
        return new PagedResult<>(items, total, size, page);
    }}

    @Override
    public List<String> getDistinctSteps() {{ return {mapper_field}.getDistinctPostProcessingSteps(); }}

    @Override
    public {cls}ProductDTO getProductDetail(Integer id) {{
        {cls}Product product = {mapper_field}.findProductById(id);
        if (product == null) return null;
        List<{cls}Compatibility> compatibilities = {mapper_field}.findCompatibilitiesByProductId(id);
        {cls}ProductDTO dto = new {cls}ProductDTO();
        dto.setProduct(product);
        dto.setCompatibilities(compatibilities);
        return dto;
    }}
}}
""")

        # --- Controller ---
        path = os.path.join(src, "controller", f"{cls}Controller.java")
        write_j(path, f"""package {pkg}.controller;

import {pkg}.dto.PagedResult;
import {pkg}.dto.{cls}ProductDTO;
import {pkg}.dto.{cls}QueryParams;
import {pkg}.model.{cls}Product;
import {pkg}.model.{cls}Compatibility;
import {pkg}.service.{cls}Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping({{"/api/{hyphen}", "/api/{tn}"}})
@CrossOrigin(origins = {{
    "http://localhost:5173",
    "http://120.26.101.0",
    "http://120.26.101.0:80",
    "http://120.26.101.0:8080",
    "http://120.26.101.0:3000"
}}, allowCredentials = "true")
public class {cls}Controller {{

    @Autowired
    private {cls}Service {service_field};

    // ========== 产品管理 ==========

    @GetMapping("/products")
    public ResponseEntity<List<{cls}Product>> getAllProducts() {{
        return ResponseEntity.ok({service_field}.getAllProducts());
    }}

    @GetMapping("/products/{{id}}")
    public ResponseEntity<{cls}Product> getProductById(@PathVariable Integer id) {{
        {cls}Product product = {service_field}.getProductById(id);
        if (product != null) return ResponseEntity.ok(product);
        return ResponseEntity.notFound().build();
    }}

    @GetMapping("/products/search")
    public ResponseEntity<List<{cls}Product>> searchProducts(@RequestParam String keyword) {{
        return ResponseEntity.ok({service_field}.searchProducts(keyword));
    }}

    @PostMapping("/products")
    public ResponseEntity<{cls}Product> createProduct(@RequestBody {cls}Product product) {{
        return ResponseEntity.ok({service_field}.saveProduct(product));
    }}

    @PutMapping("/products/{{id}}")
    public ResponseEntity<{cls}Product> updateProduct(@PathVariable Integer id, @RequestBody {cls}Product product) {{
        product.setId(id);
        return ResponseEntity.ok({service_field}.saveProduct(product));
    }}

    @DeleteMapping("/products/{{id}}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Integer id) {{
        {service_field}.deleteProduct(id);
        return ResponseEntity.ok().build();
    }}

    // ========== 兼容性管理 ==========

    @GetMapping("/compatibilities")
    public ResponseEntity<List<{cls}Compatibility>> getCompatibilitiesByProductId(@RequestParam Integer productId) {{
        return ResponseEntity.ok({service_field}.getCompatibilitiesByProductId(productId));
    }}

    @GetMapping("/compatibilities/{{id}}")
    public ResponseEntity<{cls}Compatibility> getCompatibilityById(@PathVariable Integer id) {{
        {cls}Compatibility compatibility = {service_field}.getCompatibilityById(id);
        if (compatibility != null) return ResponseEntity.ok(compatibility);
        return ResponseEntity.notFound().build();
    }}

    @PostMapping("/compatibilities")
    public ResponseEntity<?> createCompatibility(@RequestBody {cls}Compatibility compatibility) {{
        try {{
            return ResponseEntity.ok({service_field}.saveCompatibility(compatibility));
        }} catch (IllegalArgumentException e) {{
            Map<String, Object> r = new HashMap<>(); r.put("success", false); r.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(r);
        }}
    }}

    @PutMapping("/compatibilities/{{id}}")
    public ResponseEntity<?> updateCompatibility(@PathVariable Integer id, @RequestBody {cls}Compatibility compatibility) {{
        try {{
            compatibility.setId(id);
            return ResponseEntity.ok({service_field}.saveCompatibility(compatibility));
        }} catch (IllegalArgumentException e) {{
            Map<String, Object> r = new HashMap<>(); r.put("success", false); r.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(r);
        }}
    }}

    @PostMapping("/compatibilities/batch")
    public ResponseEntity<?> batchSaveCompatibilities(@RequestBody List<{cls}Compatibility> compatibilities) {{
        try {{
            {service_field}.batchSaveCompatibilities(compatibilities);
            Map<String, Object> r = new HashMap<>(); r.put("success", true); r.put("message", "批量保存成功");
            return ResponseEntity.ok(r);
        }} catch (IllegalArgumentException e) {{
            Map<String, Object> r = new HashMap<>(); r.put("success", false); r.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(r);
        }}
    }}

    @DeleteMapping("/compatibilities/{{id}}")
    public ResponseEntity<Void> deleteCompatibility(@PathVariable Integer id) {{
        {service_field}.deleteCompatibility(id);
        return ResponseEntity.ok().build();
    }}

    @DeleteMapping("/compatibilities/batch")
    public ResponseEntity<Map<String, Object>> batchDeleteCompatibility(@RequestBody List<Integer> ids) {{
        Map<String, Object> r = new HashMap<>();
        if (ids == null || ids.isEmpty()) {{
            r.put("success", false); r.put("message", "请选择要删除的记录");
            return ResponseEntity.badRequest().body(r);
        }}
        for (Integer id : ids) {{ {service_field}.deleteCompatibility(id); }}
        r.put("success", true); r.put("message", String.format("成功删除 %d 条记录", ids.size()));
        return ResponseEntity.ok(r);
    }}

    // ========== 匹配查询 ==========

    @PostMapping("/match")
    public ResponseEntity<PagedResult<{cls}Product>> match(@RequestBody {cls}QueryParams params) {{
        if (params.getPage() == null || params.getPage() < 1) params.setPage(1);
        if (params.getSize() == null || params.getSize() < 1) params.setSize(15);
        return ResponseEntity.ok({service_field}.searchProducts(
            params.getKeyword(), params.getStepName(), params.getPage(), params.getSize()));
    }}

    @GetMapping("/steps")
    public ResponseEntity<List<String>> getSteps() {{
        return ResponseEntity.ok({service_field}.getDistinctSteps());
    }}

    @GetMapping("/products/{{id}}/detail")
    public ResponseEntity<{cls}ProductDTO> getProductDetail(@PathVariable Integer id) {{
        {cls}ProductDTO dto = {service_field}.getProductDetail(id);
        if (dto != null) return ResponseEntity.ok(dto);
        return ResponseEntity.notFound().build();
    }}
}}
""")


def generate_frontend(base_dir):
    api_dir = os.path.join(base_dir, "yts_project_vueai", "src", "api", "modules")
    vue_dir = os.path.join(base_dir, "yts_project_vueai", "src", "views", "matching")

    for cls, tn, hyphen, display in CATEGORIES:
        api_field = camel_to_lower_first(cls) + "Api"
        api_file = camel_to_lower_first(cls)

        # --- API module ---
        path = os.path.join(api_dir, f"{api_file}.ts")
        write_j(path, f"""import request from '../request';

export interface {cls}Product {{
  id?: number;
  materialCode?: string;
  supplierCode?: string;
  stockCode?: string;
  materialName?: string;
  usage?: string;
  category?: string;
  color?: string;
  responsiblePerson?: string;
  minSheetSize?: string;
  maxSheetSize?: string;
  minSpacing?: string;
  designInfo?: string;
  applicableInterface?: string;
  notes?: string;
  isActive?: boolean;
  createdAt?: string;
  updatedAt?: string;
}}

export interface {cls}Compatibility {{
  id?: number;
  productId?: number;
  postProcessingStep?: string;
  compatibilityStatus?: string;
  displayOrder?: number;
  createdAt?: string;
  productName?: string;
}}

export interface {cls}MatchParams {{
  keyword?: string;
  stepName?: string;
  page?: number;
  size?: number;
}}

export interface PagedItems<T> {{
  items: T[];
  total: number;
  pageSize: number;
  currentPage: number;
  totalPages: number;
}}

export const {api_field} = {{
  getAllProducts() {{ return request.get('/{tn}/products'); }},
  getProductById(id: number) {{ return request.get(`/{tn}/products/${{id}}`); }},
  searchProducts(keyword: string) {{ return request.get('/{tn}/products/search', {{ params: {{ keyword }} }}); }},
  createProduct(product: {cls}Product) {{ return request.post('/{tn}/products', product); }},
  updateProduct(id: number, product: {cls}Product) {{ return request.put(`/{tn}/products/${{id}}`, product); }},
  deleteProduct(id: number) {{ return request.delete(`/{tn}/products/${{id}}`); }},

  getCompatibilitiesByProductId(productId: number) {{
    return request.get('/{tn}/compatibilities', {{ params: {{ productId }} }});
  }},
  getCompatibilityById(id: number) {{ return request.get(`/{tn}/compatibilities/${{id}}`); }},
  createCompatibility(compatibility: {cls}Compatibility) {{ return request.post('/{tn}/compatibilities', compatibility); }},
  updateCompatibility(id: number, compatibility: {cls}Compatibility) {{
    return request.put(`/{tn}/compatibilities/${{id}}`, compatibility);
  }},
  deleteCompatibility(id: number) {{ return request.delete(`/{tn}/compatibilities/${{id}}`); }},
  batchSaveCompatibilities(compatibilities: {cls}Compatibility[]) {{
    return request.post('/{tn}/compatibilities/batch', compatibilities);
  }},
  batchDeleteCompatibilities(ids: number[]) {{
    return request.delete('/{tn}/compatibilities/batch', {{ data: ids }});
  }},

  match(params: {cls}MatchParams) {{
    return request.post<PagedItems<{cls}Product>>('/{tn}/match', params);
  }},
  getSteps() {{ return request.get<string[]>('/{tn}/steps'); }},
  getProductDetail(id: number) {{ return request.get(`/{tn}/products/${{id}}/detail`); }},
}};
""")

        # --- Vue page ---
        path = os.path.join(vue_dir, f"{cls}Matching.vue")
        write_j(path, f"""<template>
  <div class="min-h-screen bg-gray-100 pt-20 px-4 sm:px-6 lg:px-8">
    <div class="max-w-7xl mx-auto">
      <div class="mb-6">
        <h1 class="text-2xl font-bold text-gray-900">{display}</h1>
        <p class="mt-1 text-sm text-gray-500">搜索{display}产品，按后加工工序筛选兼容性</p>
      </div>

      <div class="bg-white rounded-lg shadow px-5 py-5 mb-6">
        <div class="flex flex-wrap items-end gap-4">
          <div class="flex-1 min-w-[200px]">
            <label class="block text-sm font-medium text-gray-700 mb-1">关键词搜索</label>
            <el-input v-model="keyword" placeholder="物料名称 / 编码 / 型号" clearable @keyup.enter="onSearch" />
          </div>
          <div class="w-56">
            <label class="block text-sm font-medium text-gray-700 mb-1">后加工工序</label>
            <el-select v-model="selectedStep" placeholder="全部工序" clearable class="w-full" @change="onSearch">
              <el-option v-for="step in steps" :key="step" :label="step" :value="step" />
            </el-select>
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1 invisible">搜索</label>
            <el-button type="primary" @click="onSearch">
              <el-icon class="mr-1"><Search /></el-icon> 搜索
            </el-button>
          </div>
        </div>
      </div>

      <div v-if="loading" class="flex justify-center py-12">
        <el-icon class="is-loading text-2xl text-indigo-500"><Loading /></el-icon>
        <span class="ml-3 text-gray-500">加载中...</span>
      </div>

      <div v-if="!loading && pagedResult" class="flex items-center justify-between mb-4">
        <p class="text-sm text-gray-500">
          共 <span class="font-medium text-gray-700">{{ pagedResult.total }}</span> 条结果
          <span v-if="keyword || selectedStep" class="ml-2">
            <el-tag v-if="keyword" closable size="small" type="info" @close="keyword = ''; onSearch()">
              关键词: {{ keyword }}
            </el-tag>
            <el-tag v-if="selectedStep" closable size="small" type="info" class="ml-1" @close="selectedStep = ''; onSearch()">
              工序: {{ selectedStep }}
            </el-tag>
          </span>
        </p>
      </div>

      <div v-if="!loading && pagedResult && pagedResult.items.length === 0"
           class="bg-white rounded-lg shadow py-16 text-center">
        <el-empty description="暂无匹配结果">
          <template #image>
            <svg class="w-24 h-24 mx-auto text-gray-300" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5"
                d="M9.172 16.172a4 4 0 015.656 0M9 10h.01M15 10h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
            </svg>
          </template>
          <p class="text-gray-400 mt-2">{{ keyword || selectedStep ? '未找到匹配条件的产品' : '暂无数据' }}</p>
        </el-empty>
      </div>

      <div v-if="!loading && pagedResult && pagedResult.items.length > 0" class="space-y-4">
        <div v-for="product in pagedResult.items" :key="product.id"
             class="bg-white rounded-lg shadow-sm border border-gray-200 hover:shadow-md transition-shadow">
          <div class="px-5 py-4 flex items-start justify-between cursor-pointer" @click="toggleExpand(product.id!)">
            <div class="flex-1 min-w-0">
              <div class="flex items-center gap-3">
                <h3 class="text-base font-semibold text-gray-900 truncate">{{ product.materialName || '-' }}</h3>
                <el-tag v-if="product.isActive === false" size="small" type="danger">停用</el-tag>
              </div>
              <div class="mt-1 flex flex-wrap gap-x-4 gap-y-1 text-sm text-gray-500">
                <span>编码: {{ product.materialCode || '-' }}</span>
                <span>型号: {{ product.stockCode || '-' }}</span>
                <span>颜色: {{ product.color || '-' }}</span>
                <span>材质: {{ product.category || '-' }}</span>
              </div>
            </div>
            <div class="flex items-center gap-2 ml-4">
              <el-icon class="text-gray-400 transition-transform duration-200"
                       :class="{{ 'rotate-180': expandedId === product.id }}">
                <ArrowDown />
              </el-icon>
            </div>
          </div>

          <div v-if="expandedId === product.id" class="border-t border-gray-100 px-5 py-4 bg-gray-50 rounded-b-lg">
            <div v-if="detailLoading" class="flex items-center justify-center py-4">
              <el-icon class="is-loading text-indigo-500"><Loading /></el-icon>
              <span class="ml-2 text-sm text-gray-400">加载详情...</span>
            </div>
            <div v-else-if="currentDetail">
              <div class="grid grid-cols-1 md:grid-cols-3 gap-4 mb-4">
                <div><label class="text-xs text-gray-400">物料名称</label><p class="text-sm text-gray-700">{{ currentDetail.product?.materialName || '-' }}</p></div>
                <div><label class="text-xs text-gray-400">物料编码</label><p class="text-sm text-gray-700">{{ currentDetail.product?.materialCode || '-' }}</p></div>
                <div><label class="text-xs text-gray-400">供应商编码</label><p class="text-sm text-gray-700">{{ currentDetail.product?.supplierCode || '-' }}</p></div>
                <div><label class="text-xs text-gray-400">存仓物料号</label><p class="text-sm text-gray-700">{{ currentDetail.product?.stockCode || '-' }}</p></div>
                <div><label class="text-xs text-gray-400">颜色</label><p class="text-sm text-gray-700">{{ currentDetail.product?.color || '-' }}</p></div>
                <div><label class="text-xs text-gray-400">材质分类</label><p class="text-sm text-gray-700">{{ currentDetail.product?.category || '-' }}</p></div>
                <div><label class="text-xs text-gray-400">测试员</label><p class="text-sm text-gray-700">{{ currentDetail.product?.responsiblePerson || '-' }}</p></div>
                <div><label class="text-xs text-gray-400">用纸尺寸</label><p class="text-sm text-gray-700">{{ currentDetail.product?.minSheetSize || '-' }} ~ {{ currentDetail.product?.maxSheetSize || '-' }}</p></div>
                <div><label class="text-xs text-gray-400">间距(最小)</label><p class="text-sm text-gray-700">{{ currentDetail.product?.minSpacing || '-' }}</p></div>
              </div>
              <div v-if="currentDetail.product?.usage" class="mb-4">
                <label class="text-xs text-gray-400">物料用途</label><p class="text-sm text-gray-700">{{ currentDetail.product.usage }}</p>
              </div>
              <div v-if="currentDetail.product?.designInfo" class="mb-4">
                <label class="text-xs text-gray-400">设计限制信息</label><p class="text-sm text-gray-700">{{ currentDetail.product.designInfo }}</p>
              </div>
              <div v-if="currentDetail.product?.applicableInterface" class="mb-4">
                <label class="text-xs text-gray-400">适用界面</label><p class="text-sm text-gray-700">{{ currentDetail.product.applicableInterface }}</p>
              </div>
              <div v-if="currentDetail.product?.notes" class="mb-4">
                <label class="text-xs text-gray-400">备注</label><p class="text-sm text-gray-700">{{ currentDetail.product.notes }}</p>
              </div>
              <div v-if="currentDetail.compatibilities && currentDetail.compatibilities.length > 0">
                <h4 class="text-sm font-medium text-gray-700 mb-3">后加工工序兼容性</h4>
                <div class="flex flex-wrap gap-2">
                  <div v-for="comp in currentDetail.compatibilities" :key="comp.id"
                       class="inline-flex items-center gap-1.5 px-3 py-1.5 rounded-full text-sm"
                       :class="statusBadgeClass(comp.compatibilityStatus)">
                    <span>{{ comp.postProcessingStep }}</span>
                    <span class="font-bold">{{ comp.compatibilityStatus === 'V' ? 'V' : comp.compatibilityStatus === 'X' ? 'X' : comp.compatibilityStatus === '△' ? '△' : comp.compatibilityStatus }}</span>
                  </div>
                </div>
              </div>
              <div v-else class="text-sm text-gray-400 italic">暂无兼容性数据</div>
            </div>
            <div v-else class="text-sm text-gray-400 italic py-4 text-center">加载详情失败</div>
          </div>
        </div>
      </div>

      <div v-if="pagedResult && pagedResult.totalPages > 1" class="flex justify-center mt-6 pb-8">
        <el-pagination v-model:current-page="currentPage" :page-size="pageSize" :total="pagedResult.total"
          layout="prev, pager, next, total" background @current-change="onPageChange" />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import {{ ref, onMounted }} from 'vue';
import {{ {api_field}, type {cls}Product, type PagedItems }} from '@/api/modules/{api_file}';
import {{ Search, Loading, ArrowDown }} from '@element-plus/icons-vue';
import {{ ElMessage }} from 'element-plus';

const keyword = ref('');
const selectedStep = ref('');
const steps = ref<string[]>([]);
const loading = ref(false);
const pageSize = ref(15);
const currentPage = ref(1);
const pagedResult = ref<PagedItems<{cls}Product> | null>(null);
const expandedId = ref<number | null>(null);
const currentDetail = ref<any>(null);
const detailLoading = ref(false);

onMounted(() => {{ loadSteps(); loadMatch(); }});

async function loadSteps() {{
  try {{ const res: any = await {api_field}.getSteps(); steps.value = res || []; }} catch {{}}
}}

async function loadMatch() {{
  loading.value = true;
  try {{
    const res: any = await {api_field}.match({{
      keyword: keyword.value || undefined,
      stepName: selectedStep.value || undefined,
      page: currentPage.value,
      size: pageSize.value,
    }});
    pagedResult.value = res || null;
  }} catch {{
    ElMessage.error('查询失败，请重试');
    pagedResult.value = {{ items: [], total: 0, pageSize: pageSize.value, currentPage: currentPage.value, totalPages: 0 }};
  }} finally {{ loading.value = false; }}
}}

function onSearch() {{ currentPage.value = 1; loadMatch(); }}

function onPageChange() {{ loadMatch(); expandedId.value = null; currentDetail.value = null; }}

async function toggleExpand(id: number) {{
  if (expandedId.value === id) {{ expandedId.value = null; currentDetail.value = null; return; }}
  expandedId.value = id;
  detailLoading.value = true;
  currentDetail.value = null;
  try {{ const res: any = await {api_field}.getProductDetail(id); currentDetail.value = res || null; }}
  catch {{ ElMessage.error('加载产品详情失败'); currentDetail.value = null; }}
  finally {{ detailLoading.value = false; }}
}}

function statusBadgeClass(status?: string): string {{
  switch (status) {{
    case 'V': return 'bg-green-100 text-green-800';
    case 'X': return 'bg-red-100 text-red-800';
    case '△': case '▷': return 'bg-yellow-100 text-yellow-800';
    default: return 'bg-gray-100 text-gray-600';
  }}
}}
</script>

<style scoped>
.rotate-180 {{ transform: rotate(180deg); }}
</style>
""")


def remove_old_files(base_dir):
    """Remove old snake_case files that conflict with PascalCase versions."""
    old = [
        "yts_project/src/main/java/com/it/yts_project/controller/silicone_coarse_uvController.java",
        "yts_project/src/main/java/com/it/yts_project/controller/silicone_sandblast_uvController.java",
        "yts_project/src/main/java/com/it/yts_project/controller/silicone_white_uvController.java",

        "yts_project/src/main/java/com/it/yts_project/service/silicone_coarse_uvService.java",
        "yts_project/src/main/java/com/it/yts_project/service/silicone_sandblast_uvService.java",
        "yts_project/src/main/java/com/it/yts_project/service/silicone_white_uvService.java",

        "yts_project/src/main/java/com/it/yts_project/service/Impl/silicone_coarse_uvServiceImpl.java",
        "yts_project/src/main/java/com/it/yts_project/service/Impl/silicone_sandblast_uvServiceImpl.java",
        "yts_project/src/main/java/com/it/yts_project/service/Impl/silicone_white_uvServiceImpl.java",

        "yts_project/src/main/java/com/it/yts_project/mapper/silicone_coarse_uvMapper.java",
        "yts_project/src/main/java/com/it/yts_project/mapper/silicone_sandblast_uvMapper.java",
        "yts_project/src/main/java/com/it/yts_project/mapper/silicone_white_uvMapper.java",

        "yts_project/src/main/java/com/it/yts_project/model/silicone_coarse_uvCompatibility.java",
        "yts_project/src/main/java/com/it/yts_project/model/silicone_coarse_uvProduct.java",
        "yts_project/src/main/java/com/it/yts_project/model/silicone_sandblast_uvCompatibility.java",
        "yts_project/src/main/java/com/it/yts_project/model/silicone_sandblast_uvProduct.java",
        "yts_project/src/main/java/com/it/yts_project/model/silicone_white_uvCompatibility.java",
        "yts_project/src/main/java/com/it/yts_project/model/silicone_white_uvProduct.java",

        "yts_project/src/main/resources/mapper/silicone_coarse_uvMapper.xml",
        "yts_project/src/main/resources/mapper/silicone_sandblast_uvMapper.xml",
        "yts_project/src/main/resources/mapper/silicone_white_uvMapper.xml",

        "yts_project_vueai/src/api/modules/silicone_coarse_uv.ts",
        "yts_project_vueai/src/api/modules/silicone_sandblast_uv.ts",
        "yts_project_vueai/src/api/modules/silicone_white_uv.ts",
        "yts_project_vueai/src/api/modules/silicone_glow_ink.ts",
        "yts_project_vueai/src/api/modules/silicone_watercolor_ink.ts",
        "yts_project_vueai/src/api/modules/siliconeGlowInk.ts",
        "yts_project_vueai/src/api/modules/siliconeOrangePeelUv.ts",
        "yts_project_vueai/src/api/modules/siliconeWatercolorInk.ts",
        "yts_project_vueai/src/api/modules/siliconeWrinkleUv.ts",
    ]

    for rel in old:
        path = os.path.join(base_dir, rel)
        if os.path.exists(path):
            os.remove(path)
            print(f"  REMOVED: {rel}")
        else:
            # try target classes too
            pass


def write_j(path, content):
    os.makedirs(os.path.dirname(path), exist_ok=True)
    with open(path, 'w', encoding='utf-8') as f:
        f.write(content.lstrip('\n'))


if __name__ == "__main__":
    base = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))

    print("=" * 60)
    print("Step 1: Remove old snake_case files...")
    print("=" * 60)
    remove_old_files(base)

    print("\n" + "=" * 60)
    print("Step 2: Generate backend files (12 categories)...")
    print("=" * 60)
    generate_backend(base)

    print("\n" + "=" * 60)
    print("Step 3: Generate frontend files (12 categories)...")
    print("=" * 60)
    generate_frontend(base)

    print("\n" + "=" * 60)
    print("ALL DONE!")
    print("=" * 60)
    print("\nNext steps:")
    print("  1. Run: cd yts_project && mvn compile -q   (verify backend compiles)")
    print("  2. Update router: add imports and routes to yts_project_vueai/src/router/index.ts")
    print("  3. Add nav links to sidebar")
    print("  4. Run: cd yts_project_vueai && npm run build   (verify frontend builds)")
