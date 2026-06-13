package com.it.yts_project.dto;

import com.it.yts_project.enums.ApplicabilityEnum;
import lombok.Data;

import java.util.List;


@Data
public class GoldFoilQueryParams {
    // 1.公司编号
    private String companyNumber;

    // 1.1.客户编号
    private String gpNo;

    // 3.产品类型
    private String productType;

    // 4.烫金图案(X)
    private String patternType;


    // 2.适用界面(前工序)相关字段
    private String oil6812Glossy33013440Dumb;
    private String gvLedUvGlossMatte;
    private String oilBasedGlossMattePowderPaper;
    private String oilBasedGlossMatteNonPowderPaper;
    private String standardFuronghui22d;
    private String preCoatedHy120665;
    private String highTackPreCoatedHy40;
    private String preCoatedEconomicalHighWearResistantYt008a;
    private String preCoatedHighWearResistantTn008;
    private String preCoatedStandardWearResistantKvmbF;
    private String softTouchMatteLaminate6015a;

    // 6.适用界面相关字段
    // 膠片相关
    private String plasticFilmNormalVET;
    private String plasticFilmScratchResistantAVET;
    // 牛油紙相关
    private String waxPaperGTF;
    private String waxPaperZTF;
    // 布面紙相关
    private String fabricPaperNylonJHT8001;
    private String fabricPaperNylonJHT8004;
    private String fabricPaperNylonJHT8013;
    private String fabricPaperPolyesterJHT8002;
    private String fabricPaperPolyesterJHT8010;
    private String fabricPaperPolyesterJHT8014;
    private String fabricPaperPolyesterJHT8015;
    private String fabricPaperPolyesterLTS8003;
    private String fabricPaperPolyesterESM;
    private String fabricPaperImitationCottonJHT8003;
    private String fabricPaperImitationCottonJHT8017;
    private String fabricPaperPolyCottonJHT8006;
    private String fabricPaperPolyCottonJHT8008;
    private String fabricPaperPolyCottonJHT8011;
    private String fabricPaperPolyCottonJHT8016;
    private String fabricPaperPolyCottonJHT8018;
    private String fabricPaperPolyCottonJHT8019;
    private String fabricPaperPolyCottonJHT910;
    private String fabricPaperHempCottonJHT8007;
    private String fabricPaperPureCottonJHT8009;
    private String fabricPaperPureCottonJHT8012;
    private String fabricPaperCottonJHT0001to0103;
    private String fabricPaperMercerizedCottonJHT0104to0195;
    private String fabricPaperMercerizedCottonJHT0199to0209;
    private String fabricPaperMercerizedCottonJHT0211;
    private String fabricPaperMercerizedCottonJHT0213;
    private String fabricPaperSilkJHT0196to0198;
    private String fabricPaperSilkJHT0265to0311;
    private String fabricPaperFlashFabricJHT0216;
    private String fabricPaperFlashFabricJHT0218;
    private String fabricPaperFlashFabricJHT0312to0330;
    private String fabricPaperSilkFabricJHT0219to0264;
    private String fabricPaperHempPressFabricJHT0334to0351;
    private String fabricPaperTexturedFabricJHT0352to0386;
    private String fabricPaperMercerizedHempFabricJHT0387to0407;
    private String fabricPaperCoarseFabricJHT0419to0429;


    // 7.烫金图案相关字段
    private String gradientAndHots;
    private List<String> hotStampingPaperTypes;
    private String gradientAndDots; // 7.1漸變、網點
    private String thinLinesAndLetters; // 7.2幅細線條、字母
    private String mediumSmallAreaThinLinesAndLetters; // 7.3中小面積細線條、字母
    private String mediumLargeAreaThinLinesAndLetters; // 7.4中大面積、細線條、字母
    private String extraLargeArea; // 7.5超大面積
    // 8.烫金类型相关字段
    private String stampingType;
    private String flatHotStamping; // 8.1平面燫金
    private String embossedGoldStamping; // 8.2立體燫金
    private String refractiveHolographicPatternedTexturedHotStamping; // 8.3折光燫金、有紋燫金
    private String postHotStampingEmbossingDebossing; // 8.4燫金後擊凸、壓紋

    // 9.价格优先度相关字段
    private Integer priceLevel; // 价格优先度级别（1-10）

    // 10.兼容性筛选相关字段
    private String paperPerformance; // 烫金纸性能类型：普通金紙、普通耐磨、高耐磨
    private String patternCharacteristic; // 图案特征描述
    private Double lineThickness; // 线条粗细 (mm)
    private Double solidAreaSize; // 实地尺寸 (mm)
    private Boolean isMixedPattern; // 是否为混合图案
    private String patternTypeForCompatibility; // 图案类型：LINE、SOLID、MIXED、ALL
    private List<String> targetHotStampingTypes; // 目标烫金类型列表
    private String hotStampingType;
    // 11.燙後加工相关字段
    private String uvPrinting; // 11.印刷UV
    private String silkScreenLedUvSparklePowder;
    private String silk_screen_led_uv_sparkle_powder; // 11.絲印LED UV灑閃粉
    private String printing; // 12.烫金+印刷
    private String embossing; // 17.燙後加工-擊凸（V 表示启用该过滤）

    // 14.燙金+過膠相关字段
    private String postProcessingStep; // 14.1 燙金+過膠->後加工步骤
    private String laminationMaterial; // 14.2 過膠材質
    private String laminationCompatibility; // 14.4 兼容性状态 (V/X)
    
    // 过胶兼容性详细参数
    private Integer postProcessingStepId; // 后加工步骤ID
    private Integer laminationMaterialId; // 过胶材质ID
    private Integer interfaceTypeId; // 接口类型ID
    private Character laminationCompatibilityStatus; // 兼容性状态 (V/X)
    
    // 前工序步骤相关参数
    private Integer preProcessingStepsOptionId; // 前工序选项ID（来自pre_processing_steps_options表）
    
    // 产品类型ID相关参数
    private Integer productTypeId; // 产品类型ID（来自product_type_options表）
    
    // 图案类型ID相关参数
    private Integer patternId; // 图案类型ID（来自hot_stamping_pattern_base表）
    
    // 图案区域选项ID（来自 hot_stamping_patternx_area_options 表）
    private Integer patternAreaOptionId;
    
    // 烫金类型选项ID相关参数
    private Integer hotStampingTypeOptionId; // 烫金类型选项ID（来自hot_stamping_type_options表，对应hot_stamping_compatibility表的hot_stamping_type_id字段）
    private Boolean hasDurableMapping;
    private Boolean hasCommonMapping;
    
    // 布料纸类型相关参数
    private Integer clothPaperTypeId; // 布料纸类型ID（来自cloth_paper_types表，对应cloth_paper_compatibility表的cloth_paper_type_id字段）
    private String clothPaperCompatibilityStatus; // 布料纸兼容性状态（V=适用，X=不适用，▷=需要作「絲印打底處理」再烫金）
    
    // 综合筛选相关参数
    private String paizi; // 牌子筛选
    private String colorNum; // 颜色编码筛选
    
    // 排序相关参数
    private String sortBy; // 排序字段：'price' 或 'matchScore' 或 'default'
    private String sortOrder; // 排序方向：'asc' 或 'desc'
    
    // 用户ID（用于获取用户匹配偏好，计算匹配度）
    private Integer userId; // 用户ID
    
    // 匹配度规则类型：'tag' 表示标签配置匹配度，'price' 表示价格匹配度
    private String matchScoreRule = "price"; // 默认使用价格匹配度规则
    
    // 分页相关参数
    private Integer pageSize = 15; // 每页显示条数，默认15
    private Integer offset = 0;    // 偏移量，用于分页
    
    // 跳过耐磨映射的烫金纸类型列表（用于在 SQL EXISTS 检查中跳过）
    private List<String> skipWearResistantPaperTypes;


    public static ApplicabilityEnum convertToEnum(String value) {
        return ApplicabilityEnum.fromCode(value);
    }


    public ApplicabilityEnum getOil6812Glossy33013440DumbEnum() {
        return convertToEnum(oil6812Glossy33013440Dumb);
    }


    public ApplicabilityEnum getGvLedUvGlossMattEnum() {
        return convertToEnum(gvLedUvGlossMatte);
    }


    public ApplicabilityEnum getStandardFuronghui22dEnum() {
        return convertToEnum(standardFuronghui22d);
    }

    /**
     * 获取oilBasedGlossMattePowderPaper的枚举值
     * @return 枚举值
     */
    public ApplicabilityEnum getOilBasedGlossMattePowderPaperEnum() {
        return convertToEnum(oilBasedGlossMattePowderPaper);
    }

    /**
     * 获取oilBasedGlossMatteNonPowderPaper的枚举值
     * @return 枚举值
     */
    public ApplicabilityEnum getOilBasedGlossMatteNonPowderPaperEnum() {
        return convertToEnum(oilBasedGlossMatteNonPowderPaper);
    }

    /**
     * 获取preCoatedHy120665的枚举值
     * @return 枚举值
     */
    public ApplicabilityEnum getPreCoatedHy120665Enum() {
        return convertToEnum(preCoatedHy120665);
    }

    /**
     * 获取highTackPreCoatedHy40的枚举值
     * @return 枚举值
     */
    public ApplicabilityEnum getHighTackPreCoatedHy40Enum() {
        return convertToEnum(highTackPreCoatedHy40);
    }

    /**
     * 获取preCoatedEconomicalHighWearResistantYt008a的枚举值
     * @return 枚举值
     */
    public ApplicabilityEnum getPreCoatedEconomicalHighWearResistantYt008aEnum() {
        return convertToEnum(preCoatedEconomicalHighWearResistantYt008a);
    }

    /**
     * 获取preCoatedHighWearResistantTn008的枚举值
     * @return 枚举值
     */
    public ApplicabilityEnum getPreCoatedHighWearResistantTn008Enum() {
        return convertToEnum(preCoatedHighWearResistantTn008);
    }

    /**
     * 获取preCoatedStandardWearResistantKvmbF的枚举值
     * @return 枚举值
     */
    public ApplicabilityEnum getPreCoatedStandardWearResistantKvmbFEnum() {
        return convertToEnum(preCoatedStandardWearResistantKvmbF);
    }

    /**
     * 获取softTouchMatteLaminate6015a的枚举值
     * @return 枚举值
     */
    public ApplicabilityEnum getSoftTouchMatteLaminate6015aEnum() {
        return convertToEnum(softTouchMatteLaminate6015a);
    }

    /**
     * 获取ledUvGlitter的枚举值
     * @return 枚举值
     */
    public ApplicabilityEnum getLedUvGlitterEnum() {
        return convertToEnum(silk_screen_led_uv_sparkle_powder);
    }

    /**
     * 获取stampingPrinting的枚举值
     * @return 枚举值
     */
    public ApplicabilityEnum getStampingPrintingEnum() {
        return convertToEnum(printing);
    }
}
