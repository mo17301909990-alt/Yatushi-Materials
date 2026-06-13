package com.it.yts_project.mapper;

import com.it.yts_project.model.AdminChangeRecord;
import com.it.yts_project.model.AdminChangeSnapshot;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AdminChangeMapper {

    // ========== 快照 ==========

    int insertSnapshot(AdminChangeSnapshot snapshot);

    AdminChangeSnapshot findSnapshotById(@Param("id") Long id);

    List<AdminChangeSnapshot> findSnapshotsByRecordId(@Param("recordId") Long recordId);

    // ========== 变更记录 ==========

    int insertRecord(AdminChangeRecord record);

    AdminChangeRecord findRecordById(@Param("id") Long id);

    List<AdminChangeRecord> findRecordsByEntity(
            @Param("entityType") String entityType,
            @Param("entityId") Long entityId);

    List<AdminChangeRecord> findRecords(
            @Param("operatorId") Integer operatorId,
            @Param("entityType") String entityType,
            @Param("status") String status,
            @Param("offset") int offset,
            @Param("limit") int limit);

    int countRecords(
            @Param("operatorId") Integer operatorId,
            @Param("entityType") String entityType,
            @Param("status") String status);

    int updateRecordStatus(
            @Param("id") Long id,
            @Param("status") String status,
            @Param("errorMessage") String errorMessage);

    int markRollback(
            @Param("id") Long id,
            @Param("rollbackRecordId") Long rollbackRecordId);

    int updateSnapshotAfterData(
            @Param("id") Long id,
            @Param("afterData") String afterData);
}
