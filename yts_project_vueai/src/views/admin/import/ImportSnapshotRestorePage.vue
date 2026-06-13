<template>
  <div class="import-snapshot-restore-page">
    <div class="page-header">
      <div class="page-header-row">
        <AdminBackToPanel />
        <div class="page-header-text">
          <h1>导入备份与恢复</h1>
          <p class="subtitle">覆盖式导入前会自动备份，此处按「导入批次」查看并恢复</p>
        </div>
      </div>
    </div>

    <div class="warn-box">
      <el-alert
        title="恢复说明"
        type="warning"
        :closable="false"
        show-icon
      >
        <template #default>
          <p>恢复将用该次导入前的数据覆盖当前数据，操作不可撤销，请确认后再点「恢复此次导入」。</p>
        </template>
      </el-alert>
    </div>

    <el-card class="filter-card">
      <el-form :inline="true" :model="filters" class="filter-form">
        <el-form-item label="备份内容">
          <el-select v-model="filters.tableName" placeholder="全部" clearable style="width: 140px">
            <el-option label="全部" value="" />
            <el-option label="产品主数据" value="products" />
            <el-option label="价目" value="pricing" />
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

    <el-card class="batch-card" v-loading="loading">
      <div v-for="batch in batchList" :key="batch.key" class="batch-section">
        <div class="batch-header">
          <span class="batch-time">{{ batch.label }}</span>
          <span class="batch-desc">产品导入备份（{{ batch.items.length }} 项）</span>
          <el-button type="primary" size="small" @click="handleRestoreBatch(batch)">恢复此次导入</el-button>
        </div>
        <ul class="batch-items">
          <li v-for="item in batch.items" :key="item.id">
            <span class="item-name">{{ tableNameLabel(item.tableName) }}</span>
            <span class="item-count">约 {{ item.recordCount }} 条</span>
          </li>
        </ul>
      </div>
      <div class="empty-tip" v-if="!loading && batchList.length === 0">
        暂无备份记录。在「物料信息管理」里做覆盖式导入并勾选「导入前备份」时会自动生成。
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted } from 'vue'
import AdminBackToPanel from '@/components/admin/AdminBackToPanel.vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { importSnapshotApi, type ImportSnapshot } from '../../../api/modules/importSnapshot'

const TABLE_LABELS: Record<string, string> = {
  products: '产品主数据',
  pricing: '价目'
}

function tableNameLabel(tableName: string): string {
  return TABLE_LABELS[tableName] ?? tableName
}

const loading = ref(false)
const list = ref<ImportSnapshot[]>([])
const filters = reactive<{ tableName: string; from: string; to: string }>({
  tableName: '',
  from: '',
  to: ''
})

const loadList = async () => {
  loading.value = true
  try {
    const res = await importSnapshotApi.list({
      tableName: filters.tableName || undefined,
      from: filters.from || undefined,
      to: filters.to || undefined,
      limit: 100
    })
    list.value = Array.isArray(res) ? res : []
  } catch (e) {
    console.error('加载快照列表失败', e)
    ElMessage.error('加载快照列表失败')
    list.value = []
  } finally {
    loading.value = false
  }
}

const resetFilters = () => {
  filters.tableName = ''
  filters.from = ''
  filters.to = ''
  loadList()
}

// 按「导入批次」分组：同一分钟内的快照视为同一次导入
const batchList = computed(() => {
  const map = new Map<string, ImportSnapshot[]>()
  const order = ['products', 'pricing']
  for (const row of list.value) {
    const t = row.operatedAt || ''
    const key = t.slice(0, 16)
    if (!map.has(key)) map.set(key, [])
    map.get(key)!.push(row)
  }
  const batches: { key: string; label: string; items: ImportSnapshot[] }[] = []
  map.forEach((items, key) => {
    items.sort((a, b) => order.indexOf(a.tableName) - order.indexOf(b.tableName))
    const [datePart, timePart] = key.split('T')
    const [y, m, d] = (datePart || '').split('-')
    const [h, min] = (timePart || '').split(':')
    const label = `${y}年${parseInt(m, 10)}月${parseInt(d, 10)}日 ${h}:${min}`
    batches.push({ key, label, items })
  })
  batches.sort((a, b) => (b.key > a.key ? 1 : -1))
  return batches
})

const handleRestoreBatch = (batch: { key: string; label: string; items: ImportSnapshot[] }) => {
  const names = batch.items.map(i => tableNameLabel(i.tableName)).join('、')
  ElMessageBox.confirm(
    `确定要恢复「${batch.label}」的导入备份吗？将用该次备份覆盖当前「${names}」数据，操作不可撤销。`,
    '恢复此次导入',
    {
      confirmButtonText: '确定恢复',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(async () => {
    try {
      for (const row of batch.items) {
        const res = await importSnapshotApi.restore(row.id)
        if (!res?.success) {
          ElMessage.error(res?.message || `恢复 ${tableNameLabel(row.tableName)} 失败`)
          return
        }
      }
      ElMessage.success('恢复成功')
      loadList()
    } catch (e: any) {
      ElMessage.error(e?.response?.data?.message || e?.message || '恢复失败')
    }
  }).catch(() => {})
}

onMounted(() => {
  loadList()
})
</script>

<style scoped>
.import-snapshot-restore-page {
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
.warn-box {
  margin-bottom: 16px;
}
.filter-card {
  margin-bottom: 16px;
}
.filter-form {
  margin-bottom: 0;
}
.batch-card {
  margin-bottom: 20px;
}
.batch-section {
  padding: 14px 0;
  border-bottom: 1px solid #eee;
}
.batch-section:last-of-type {
  border-bottom: none;
}
.batch-header {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 8px;
}
.batch-time {
  font-weight: 600;
  color: #303133;
}
.batch-desc {
  color: #909399;
  font-size: 13px;
}
.batch-items {
  margin: 0;
  padding-left: 20px;
  color: #606266;
  font-size: 14px;
}
.batch-items li {
  margin: 4px 0;
}
.item-name {
  margin-right: 8px;
}
.item-count {
  color: #909399;
  font-size: 13px;
}
.empty-tip {
  padding: 24px;
  text-align: center;
  color: #999;
  font-size: 14px;
}
</style>
