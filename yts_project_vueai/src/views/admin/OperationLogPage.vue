<template>
  <div class="operation-log-page">
    <div class="page-header">
      <div class="page-header-row">
        <AdminBackToPanel />
        <div class="page-header-text">
          <h1>操作日志</h1>
          <p class="subtitle">查看谁在何时对哪块数据做了何种操作，便于审计与追溯</p>
        </div>
      </div>
    </div>

    <el-card class="filter-card">
      <el-form :inline="true" :model="filters" class="filter-form">
        <el-form-item label="模块">
          <el-select v-model="filters.module" placeholder="全部" clearable style="width: 200px">
            <el-option label="全部" value="" />
            <el-option label="燙金物料信息" value="燙金物料信息" />
            <el-option label="導入備份與恢復" value="導入備份與恢復" />
            <el-option label="產品類型管理" value="產品類型管理" />
            <el-option label="燙金圖案(耐磨燙金紙選用)" value="燙金圖案(耐磨燙金紙選用)" />
            <el-option label="燙金類型管理" value="燙金類型管理" />
            <el-option label="適用界面(前工序)" value="適用界面(前工序)" />
            <el-option label="適用界面管理" value="適用界面管理" />
            <el-option label="燙金圖案(常用界面選用)" value="燙金圖案(常用界面選用)" />
            <el-option label="燙后加工(印刷)" value="燙后加工(印刷)" />
            <el-option label="燙后加工(UV)" value="燙后加工(UV)" />
            <el-option label="燙后加工(LED UV)" value="燙后加工(LED UV)" />
            <el-option label="耐磨金纸映射" value="耐磨金纸映射" />
            <el-option label="注意事項字典" value="注意事項字典" />
          </el-select>
        </el-form-item>
        <el-form-item label="操作类型">
          <el-select v-model="filters.operationType" placeholder="全部" clearable style="width: 140px">
            <el-option label="全部" value="" />
            <el-option label="导入" value="IMPORT" />
            <el-option label="恢复" value="RESTORE" />
            <el-option label="新增" value="CREATE" />
            <el-option label="修改" value="UPDATE" />
            <el-option label="删除" value="DELETE" />
          </el-select>
        </el-form-item>
        <el-form-item label="时间起">
          <el-date-picker
            v-model="filters.from"
            type="datetime"
            placeholder="选择开始时间"
            value-format="YYYY-MM-DDTHH:mm:ss"
            style="width: 180px"
          />
        </el-form-item>
        <el-form-item label="时间止">
          <el-date-picker
            v-model="filters.to"
            type="datetime"
            placeholder="选择结束时间"
            value-format="YYYY-MM-DDTHH:mm:ss"
            style="width: 180px"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="loadList">查询</el-button>
          <el-button @click="resetFilters">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card class="table-card" v-loading="loading">
      <el-table :data="list" stripe border>
        <el-table-column prop="operatorName" label="操作人" width="100" />
        <el-table-column prop="operatedAt" label="操作时间" width="180" />
        <el-table-column prop="operationType" label="操作类型" width="100" />
        <el-table-column prop="module" label="模块" width="120" />
        <el-table-column prop="targetType" label="对象类型" width="100" />
        <el-table-column prop="targetLabel" label="对象描述" width="140" show-overflow-tooltip>
          <template #default="{ row }">{{ row.targetLabel || '-' }}</template>
        </el-table-column>
        <el-table-column prop="changeSummary" label="变更摘要" width="160" show-overflow-tooltip />
        <el-table-column prop="changeDetail" label="变更详情" min-width="260" show-overflow-tooltip>
          <template #default="{ row }">{{ row.changeDetail || '-' }}</template>
        </el-table-column>
        <el-table-column prop="ip" label="IP" width="120" />
      </el-table>
      <div class="pagination-wrap">
        <el-pagination
          v-model:current-page="pageNo"
          :page-size="pageSize"
          :total="total"
          layout="total, prev, pager, next"
          @current-change="loadList"
        />
      </div>
      <div class="empty-tip" v-if="!loading && list.length === 0">
        暂无操作记录。在管理端进行导入、恢复等操作后会自动记录。
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import AdminBackToPanel from '@/components/admin/AdminBackToPanel.vue'
import { adminOperationLogApi, type AdminOperationLogItem } from '../../api/modules/adminOperationLog'

const loading = ref(false)
const list = ref<AdminOperationLogItem[]>([])
const total = ref(0)
const pageNo = ref(1)
const pageSize = ref(20)

const filters = reactive<{ module: string; operationType: string; from: string; to: string }>({
  module: '',
  operationType: '',
  from: '',
  to: ''
})

const loadList = async () => {
  loading.value = true
  try {
    const res = await adminOperationLogApi.list({
      module: filters.module || undefined,
      operationType: filters.operationType || undefined,
      from: filters.from || undefined,
      to: filters.to || undefined,
      pageNo: pageNo.value,
      pageSize: pageSize.value
    })
    list.value = res.list ?? []
    total.value = res.total ?? 0
  } catch (e) {
    console.error('加载操作日志失败', e)
    ElMessage.error('加载操作日志失败')
    list.value = []
  } finally {
    loading.value = false
  }
}

const resetFilters = () => {
  filters.module = ''
  filters.operationType = ''
  filters.from = ''
  filters.to = ''
  pageNo.value = 1
  loadList()
}

onMounted(() => {
  loadList()
})
</script>

<style scoped>
.operation-log-page {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}
.page-header {
  margin-bottom: 20px;
}
.page-header-row {
  display: flex;
  align-items: flex-start;
  gap: 12px;
}
.page-header-text {
  flex: 1;
  min-width: 0;
}
.page-header h1 {
  font-size: 24px;
  margin: 0 0 8px 0;
}
.subtitle {
  color: #666;
  font-size: 14px;
  margin: 0;
}
.filter-card {
  margin-bottom: 16px;
}
.filter-form {
  margin-bottom: 0;
}
.table-card {
  margin-bottom: 20px;
}
.pagination-wrap {
  margin-top: 16px;
  display: flex;
  justify-content: flex-end;
}
.empty-tip {
  padding: 24px;
  text-align: center;
  color: #999;
  font-size: 14px;
}
</style>
