package com.it.yts_project.mapper;

import com.it.yts_project.dto.SeriesPriorityRuleDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface SeriesPriorityRuleMapper {

    List<SeriesPriorityRuleDTO> selectAll();

    SeriesPriorityRuleDTO selectById(@Param("id") Integer id);

    int insert(SeriesPriorityRuleDTO rule);

    int updateById(@Param("id") Integer id, @Param("ruleName") String ruleName,
                   @Param("sortOrder") Integer sortOrder, @Param("isActive") Boolean isActive);

    int deleteById(@Param("id") Integer id);
}
