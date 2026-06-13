<template>
  <div class="product-import-management">
    <div class="page-header">
      <h2>产品信息导入管理</h2>
      <p>支持Excel文件导入产品信息，提供灵活的冲突处理策略</p>
    </div>

    <div class="import-cards">
      <!-- 完整产品信息导入 -->
      <el-card class="import-card">
        <template #header>
          <div class="card-header">
            <h3>完整产品信息导入</h3>
            <el-tag type="primary">推荐</el-tag>
          </div>
        </template>
        
        <div class="card-content">
          <p>导入包含所有产品信息的完整数据，包括规格、价格等详细信息</p>
          
          <div class="card-actions">
            <el-button type="primary" @click="openImportDialog('complete')">
              <el-icon><upload /></el-icon>
              导入完整产品信息
            </el-button>
            
            <el-button @click="downloadTemplate('complete')">
              <el-icon><download /></el-icon>
              下载模板
            </el-button>
          </div>
        </div>
      </el-card>

      <!-- 基础产品信息导入 -->
      <el-card class="import-card">
        <template #header>
          <div class="card-header">
            <h3>基础产品信息导入</h3>
            <el-tag type="info">基础</el-tag>
          </div>
        </template>
        
        <div class="card-content">
          <p>导入基础产品信息，包括产品名称、型号、物料编号等基本信息</p>
          
          <div class="card-actions">
            <el-button type="primary" @click="openImportDialog('basic')">
              <el-icon><upload /></el-icon>
              导入基础产品信息
            </el-button>
            
            <el-button @click="downloadTemplate('basic')">
              <el-icon><download /></el-icon>
              下载模板
            </el-button>
          </div>
        </div>
      </el-card>
    </div>

    <!-- 导入历史 -->
    <el-card class="history-card">
      <template #header>
        <div class="card-header">
          <h3>导入历史</h3>
          <el-button @click="refreshHistory">
            <el-icon><refresh /></el-icon>
            刷新
          </el-button>
        </div>
      </template>
      
      <el-table :data="importHistory" v-loading="historyLoading">
        <el-table-column prop="fileName" label="文件名" width="200" />
        <el-table-column prop="importType" label="导入类型" width="120">
          <template #default="{ row }">
            <el-tag :type="row.importType === 'complete' ? 'primary' : 'info'">
              {{ row.importType === 'complete' ? '完整信息' : '基础信息' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="conflictStrategy" label="冲突策略" width="120">
          <template #default="{ row }">
            <el-tag :type="getStrategyTagType(row.conflictStrategy)">
              {{ getStrategyLabel(row.conflictStrategy) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="totalCount" label="总记录数" width="100" />
        <el-table-column prop="successCount" label="成功数" width="100">
          <template #default="{ row }">
            <el-tag type="success">{{ row.successCount }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="errorCount" label="失败数" width="100">
          <template #default="{ row }">
            <el-tag type="danger" v-if="row.errorCount > 0">{{ row.errorCount }}</el-tag>
            <span v-else>0</span>
          </template>
        </el-table-column>
        <el-table-column prop="importTime" label="导入时间" width="180" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 'success' ? 'success' : 'warning'">
              {{ row.status === 'success' ? '成功' : '部分失败' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120">
          <template #default="{ row }">
            <el-button size="small" @click="viewDetails(row)">查看详情</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 导入对话框 -->
    <ProductImportDialog
      v-model="importDialogVisible"
      :import-type="currentImportType"
      @import-success="handleImportSuccess"
    />

    <!-- 详情对话框 -->
    <el-dialog
      v-model="detailDialogVisible"
      title="导入详情"
      width="800px"
    >
      <div v-if="selectedHistory">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="文件名">{{ selectedHistory.fileName }}</el-descriptions-item>
          <el-descriptions-item label="导入类型">
            <el-tag :type="selectedHistory.importType === 'complete' ? 'primary' : 'info'">
              {{ selectedHistory.importType === 'complete' ? '完整信息' : '基础信息' }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="冲突策略">
            <el-tag :type="getStrategyTagType(selectedHistory.conflictStrategy)">
              {{ getStrategyLabel(selectedHistory.conflictStrategy) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="导入时间">{{ selectedHistory.importTime }}</el-descriptions-item>
          <el-descriptions-item label="总记录数">{{ selectedHistory.totalCount }}</el-descriptions-item>
          <el-descriptions-item label="成功记录数">
            <el-tag type="success">{{ selectedHistory.successCount }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="失败记录数">
            <el-tag type="danger">{{ selectedHistory.errorCount }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="状态">
            <el-tag :type="selectedHistory.status === 'success' ? 'success' : 'warning'">
              {{ selectedHistory.status === 'success' ? '成功' : '部分失败' }}
            </el-tag>
          </el-descriptions-item>
        </el-descriptions>

        <!-- 错误信息 -->
        <div v-if="selectedHistory.errorMessages && selectedHistory.errorMessages.length > 0" class="error-section">
          <h4>错误详情：</h4>
          <el-collapse>
            <el-collapse-item title="查看错误信息" name="errors">
              <ul class="error-list">
                <li v-for="(error, index) in selectedHistory.errorMessages" :key="index">
                  {{ error }}
                </li>
              </ul>
            </el-collapse-item>
          </el-collapse>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Upload, Download, Refresh } from '@element-plus/icons-vue'
import ProductImportDialog from '../../components/import/ProductImportDialog.vue'
import { productImportApi } from '../../api/modules/productImport'

// 响应式数据
const importDialogVisible = ref(false)
const currentImportType = ref<'complete' | 'basic'>('complete')
const importHistory = ref<any[]>([])
const historyLoading = ref(false)
const detailDialogVisible = ref(false)
const selectedHistory = ref<any>(null)

// 打开导入对话框
const openImportDialog = (type: 'complete' | 'basic') => {
  currentImportType.value = type
  importDialogVisible.value = true
}

// 下载模板
const downloadTemplate = async (type: 'complete' | 'basic') => {
  try {
    const blob = await productImportApi.downloadTemplate(type)
    const url = window.URL.createObjectURL(blob)
    const link = document.createElement('a')
    link.href = url
    link.download = `${type === 'complete' ? '完整产品信息' : '基础产品信息'}_模板.xlsx`
    document.body.appendChild(link)
    link.click()
    document.body.removeChild(link)
    window.URL.revokeObjectURL(url)
    
    ElMessage.success('模板下载成功')
  } catch (error: any) {
    ElMessage.error('模板下载失败: ' + error.message)
  }
}

// 导入成功处理
const handleImportSuccess = () => {
  refreshHistory()
  ElMessage.success('导入成功')
}

// 刷新历史记录
const refreshHistory = async () => {
  historyLoading.value = true
  try {
    // 模拟获取历史记录
    await new Promise(resolve => setTimeout(resolve, 1000))
    importHistory.value = [
      {
        id: 1,
        fileName: '产品信息_20241201.xlsx',
        importType: 'complete',
        conflictStrategy: 'SKIP',
        totalCount: 100,
        successCount: 95,
        errorCount: 5,
        importTime: '2024-12-01 10:30:00',
        status: 'success',
        errorMessages: ['第5行: 数据格式错误', '第10行: 必填字段缺失']
      },
      {
        id: 2,
        fileName: '基础产品_20241201.xlsx',
        importType: 'basic',
        conflictStrategy: 'OVERWRITE',
        totalCount: 50,
        successCount: 50,
        errorCount: 0,
        importTime: '2024-12-01 09:15:00',
        status: 'success',
        errorMessages: []
      }
    ]
  } catch (error) {
    ElMessage.error('获取历史记录失败')
  } finally {
    historyLoading.value = false
  }
}

// 查看详情
const viewDetails = (row: any) => {
  selectedHistory.value = row
  detailDialogVisible.value = true
}

// 获取策略标签类型
const getStrategyTagType = (strategy: string) => {
  const typeMap: Record<string, string> = {
    'SKIP': 'info',
    'OVERWRITE': 'warning',
    'CREATE_NEW': 'success',
    'PROMPT_USER': 'primary'
  }
  return typeMap[strategy] || 'info'
}

// 获取策略标签
const getStrategyLabel = (strategy: string) => {
  const labelMap: Record<string, string> = {
    'SKIP': '跳过',
    'OVERWRITE': '覆盖',
    'CREATE_NEW': '新建',
    'PROMPT_USER': '用户选择'
  }
  return labelMap[strategy] || strategy
}

// 组件挂载时加载数据
onMounted(() => {
  refreshHistory()
})
</script>

<style scoped>
.product-import-management {
  padding: 20px;
}

.page-header {
  margin-bottom: 30px;
}

.page-header h2 {
  margin: 0 0 10px 0;
  color: #303133;
}

.page-header p {
  margin: 0;
  color: #606266;
}

.import-cards {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(400px, 1fr));
  gap: 20px;
  margin-bottom: 30px;
}

.import-card {
  border: 1px solid #e4e7ed;
  border-radius: 8px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-header h3 {
  margin: 0;
  color: #303133;
}

.card-content {
  padding: 20px 0;
}

.card-content p {
  margin: 0 0 20px 0;
  color: #606266;
  line-height: 1.6;
}

.card-actions {
  display: flex;
  gap: 10px;
}

.history-card {
  margin-top: 20px;
}

.error-section {
  margin-top: 20px;
}

.error-list {
  max-height: 200px;
  overflow-y: auto;
  padding: 0;
  margin: 0;
}

.error-list li {
  padding: 5px 0;
  border-bottom: 1px solid #ebeef5;
  color: #f56c6c;
}

:deep(.el-card__header) {
  background-color: #f5f7fa;
  border-bottom: 1px solid #e4e7ed;
}

:deep(.el-table) {
  border-radius: 4px;
}
</style>
