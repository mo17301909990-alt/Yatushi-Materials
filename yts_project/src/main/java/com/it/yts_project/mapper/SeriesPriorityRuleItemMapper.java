package com.it.yts_project.mapper;

import com.it.yts_project.dto.SeriesPriorityRuleItemDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface SeriesPriorityRuleItemMapper {

    List<SeriesPriorityRuleItemDTO> selectByRuleId(@Param("ruleId") Integer ruleId);

    int insert(SeriesPriorityRuleItemDTO item);

    int updateById(SeriesPriorityRuleItemDTO item);

    int deleteById(@Param("id") Integer id);

    int deleteByRuleId(@Param("ruleId") Integer ruleId);
}
