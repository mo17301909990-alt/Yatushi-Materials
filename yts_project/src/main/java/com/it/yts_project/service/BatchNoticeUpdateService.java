package com.it.yts_project.service;

import com.it.yts_project.dto.BatchNoticeUpdateRequest;
import com.it.yts_project.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 批量更新注意事项服务
 */
@Service
public class BatchNoticeUpdateService {

    @Autowired
    private PostProcessingPrintMapper postProcessingPrintMapper;

    @Autowired
    private PostProcessingLeduvglitterMapper postProcessingLeduvglitterMapper;

    @Autowired
    private PostProcessingPrintUvMapper postProcessingPrintUvMapper;

    @Autowired
    private LaminationCompatibilityMapper laminationCompatibilityMapper;

    @Autowired
    private WearResistantGoldPaperMappingMapper wearResistantGoldPaperMappingMapper;

    @Autowired
    private HotStampingPatternBaseMapper hotStampingPatternBaseMapper;

    @Autowired
    private HotStampingTypeOptionsMapper hotStampingTypeOptionsMapper;

    @Autowired
    private HotStampingPatternAreaOptionMapper hotStampingPatternAreaOptionMapper;

    @Autowired
    private ProductTypeOptionsMapper productTypeOptionsMapper;

    @Autowired
    private ClothPaperTypeMapper clothPaperTypeMapper;

    @Autowired
    private PreProcessingStepsMapper preProcessingStepsMapper;

    @Autowired
    private ClothPaperCompatibilityMapper clothPaperCompatibilityMapper;

    @Autowired
    private HotStampingCompatibilityMapper hotStampingCompatibilityMapper;

    /**
     * 批量更新指定规则类型的注意事项
     * @param ruleType 规则类型
     * @param request 批量更新请求
     * @return 更新的记录数
     */
    @Transactional
    public int batchUpdateNotices(String ruleType, BatchNoticeUpdateRequest request) {
        List<Integer> ids = request.getIds();
        List<Integer> noticeIds = request.getNoticeIds();
        String mode = request.getMode();

        if (ids == null || ids.isEmpty()) {
            return 0;
        }

        int updatedCount = 0;

        for (Integer id : ids) {
            List<Integer> currentNoticeIds = getCurrentNoticeIds(ruleType, id);
            List<Integer> newNoticeIds = calculateNewNoticeIds(currentNoticeIds, noticeIds, mode);

            boolean updated = updateNoticeIds(ruleType, id, newNoticeIds);
            if (updated) {
                updatedCount++;
            }
        }

        return updatedCount;
    }

    /**
     * 获取当前注意事项ID列表
     */
    private List<Integer> getCurrentNoticeIds(String ruleType, Integer id) {
        try {
            switch (ruleType) {
                case "post_processing_print":
                    var print = postProcessingPrintMapper.findById(id);
                    return print != null && print.getNoticeIds() != null ? print.getNoticeIds() : new ArrayList<>();
                case "post_processing_leduvglitter":
                    var leduv = postProcessingLeduvglitterMapper.findById(id);
                    return leduv != null && leduv.getNoticeIds() != null ? leduv.getNoticeIds() : new ArrayList<>();
                case "post_processing_print_uv":
                    var printUv = postProcessingPrintUvMapper.findById(id);
                    return printUv != null && printUv.getNoticeIds() != null ? printUv.getNoticeIds() : new ArrayList<>();
                case "lamination_compatibility":
                    var lamination = laminationCompatibilityMapper.findById(id);
                    return lamination != null && lamination.getNoticeIds() != null ? lamination.getNoticeIds() : new ArrayList<>();
                case "wear_resistant_gold_paper_mapping":
                    var wearResistant = wearResistantGoldPaperMappingMapper.findById(id);
                    return wearResistant != null && wearResistant.getNoticeIds() != null ? wearResistant.getNoticeIds() : new ArrayList<>();
                case "hot_stamping_pattern_base":
                    var pattern = hotStampingPatternBaseMapper.getPatternById(id.longValue());
                    return pattern != null && pattern.getNoticeIds() != null ? pattern.getNoticeIds() : new ArrayList<>();
                case "hot_stamping_type_options":
                    var typeOption = hotStampingTypeOptionsMapper.getTypeById(id.longValue());
                    return typeOption != null && typeOption.getNoticeIds() != null ? typeOption.getNoticeIds() : new ArrayList<>();
                case "hot_stamping_pattern_area_option":
                    var patternArea = hotStampingPatternAreaOptionMapper.getPatternAreaOptionById(id);
                    return patternArea != null && patternArea.getNoticeIds() != null ? patternArea.getNoticeIds() : new ArrayList<>();
                case "product_type_options":
                    var productType = productTypeOptionsMapper.getById(id);
                    return productType != null && productType.getNoticeIds() != null ? productType.getNoticeIds() : new ArrayList<>();
                case "cloth_paper_type":
                    var clothPaperType = clothPaperTypeMapper.getById(id);
                    return clothPaperType != null && clothPaperType.getNoticeIds() != null ? clothPaperType.getNoticeIds() : new ArrayList<>();
                case "pre_processing_steps":
                    var preProcessing = preProcessingStepsMapper.getById(id);
                    return preProcessing != null && preProcessing.getNoticeIds() != null ? preProcessing.getNoticeIds() : new ArrayList<>();
                case "cloth_paper_compatibility":
                    var clothPaperCompatibility = clothPaperCompatibilityMapper.getById(id);
                    return clothPaperCompatibility != null && clothPaperCompatibility.getNoticeIds() != null ? clothPaperCompatibility.getNoticeIds() : new ArrayList<>();
                case "smart_compatibility":
                case "hot_stamping_compatibility":
                    var hotStamping = hotStampingCompatibilityMapper.getById(id.longValue());
                    return hotStamping != null && hotStamping.getNoticeIds() != null ? hotStamping.getNoticeIds() : new ArrayList<>();
                default:
                    return new ArrayList<>();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * 计算新的注意事项ID列表
     */
    private List<Integer> calculateNewNoticeIds(List<Integer> currentIds, List<Integer> newIds, String mode) {
        if ("clear".equals(mode)) {
            return new ArrayList<>();
        } else if ("replace".equals(mode)) {
            return newIds != null ? new ArrayList<>(newIds) : new ArrayList<>();
        } else { // append
            Set<Integer> mergedIds = new HashSet<>();
            if (currentIds != null) {
                mergedIds.addAll(currentIds);
            }
            if (newIds != null) {
                mergedIds.addAll(newIds);
            }
            return new ArrayList<>(mergedIds);
        }
    }

    /**
     * 更新指定规则的注意事项
     */
    private boolean updateNoticeIds(String ruleType, Integer id, List<Integer> noticeIds) {
        try {
            switch (ruleType) {
                case "post_processing_print":
                    postProcessingPrintMapper.updateNoticeIds(id, noticeIds);
                    break;
                case "post_processing_leduvglitter":
                    postProcessingLeduvglitterMapper.updateNoticeIds(id, noticeIds);
                    break;
                case "post_processing_print_uv":
                    postProcessingPrintUvMapper.updateNoticeIds(id, noticeIds);
                    break;
                case "lamination_compatibility":
                    laminationCompatibilityMapper.updateNoticeIds(id, noticeIds);
                    break;
                case "wear_resistant_gold_paper_mapping":
                    wearResistantGoldPaperMappingMapper.updateNoticeIds(id, noticeIds);
                    break;
                case "hot_stamping_pattern_base":
                    hotStampingPatternBaseMapper.updateNoticeIds(id, noticeIds);
                    break;
                case "hot_stamping_type_options":
                    hotStampingTypeOptionsMapper.updateNoticeIds(id, noticeIds);
                    break;
                case "hot_stamping_pattern_area_option":
                    hotStampingPatternAreaOptionMapper.updateNoticeIds(id, noticeIds);
                    break;
                case "product_type_options":
                    productTypeOptionsMapper.updateNoticeIds(id, noticeIds);
                    break;
                case "cloth_paper_type":
                    clothPaperTypeMapper.updateNoticeIds(id, noticeIds);
                    break;
                case "pre_processing_steps":
                    preProcessingStepsMapper.updateNoticeIds(id, noticeIds);
                    break;
                case "cloth_paper_compatibility":
                    clothPaperCompatibilityMapper.updateNoticeIds(id, noticeIds);
                    break;
                case "smart_compatibility":
                case "hot_stamping_compatibility":
                    hotStampingCompatibilityMapper.updateNoticeIds(id.longValue(), noticeIds);
                    break;
                default:
                    return false;
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
