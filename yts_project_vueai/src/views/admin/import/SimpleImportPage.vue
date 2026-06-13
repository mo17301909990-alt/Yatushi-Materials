<template>
  <div class="simple-import-page">
    <el-card>
      <template #header>
        <h3>产品信息导入</h3>
      </template>
      
      <div class="import-form">
        <!-- 文件上传 -->
        <el-upload
          ref="uploadRef"
          :auto-upload="false"
          :on-change="handleFileChange"
          :accept="'.xlsx,.xls'"
          :limit="1"
          drag
          class="upload-dragger"
        >
          <el-icon class="el-icon--upload"><upload-filled /></el-icon>
          <div class="el-upload__text">
            将文件拖到此处，或<em>点击上传</em>
          </div>
          <template #tip>
            <div class="el-upload__tip">
              只能上传 .xlsx/.xls 文件，且不超过 10MB
            </div>
          </template>
        </el-upload>

        <!-- 导入配置 -->
        <div class="config-section">
          <h4>导入配置</h4>
          <el-form :model="form" label-width="120px">
            <el-form-item label="导入类型">
              <el-radio-group v-model="form.importType">
                <el-radio value="complete">完整产品信息</el-radio>
                <el-radio value="basic">基础产品信息</el-radio>
              </el-radio-group>
            </el-form-item>
            
            <el-form-item label="冲突处理策略">
              <el-select v-model="form.conflictStrategy" placeholder="请选择">
                <el-option label="跳过冲突数据（推荐）" value="SKIP" />
                <el-option label="覆盖现有数据" value="OVERWRITE" />
                <el-option label="创建新记录" value="CREATE_NEW" />
                <el-option label="用户选择" value="PROMPT_USER" />
              </el-select>
            </el-form-item>
          </el-form>
        </div>

        <!-- 导入按钮 -->
        <div class="action-section">
          <el-button 
            type="primary" 
            @click="handleImport"
            :loading="isImporting"
            :disabled="!selectedFile"
            size="large"
          >
            {{ isImporting ? '导入中...' : '开始导入' }}
          </el-button>
          
          <el-button @click="downloadTemplate" size="large">
            下载模板
          </el-button>
        </div>

        <!-- 导入结果 -->
        <div v-if="importResult" class="result-section">
          <el-alert
            :title="importResult.success ? '导入成功' : '导入完成'"
            :type="importResult.success ? 'success' : 'warning'"
            :description="importResult.message"
            show-icon
          />
          
          <div class="result-details">
            <el-row :gutter="20">
              <el-col :span="8">
                <el-statistic title="总记录数" :value="importResult.totalCount" />
              </el-col>
              <el-col :span="8">
                <el-statistic title="成功记录数" :value="importResult.successCount" />
              </el-col>
              <el-col :span="8">
                <el-statistic title="失败记录数" :value="importResult.errorCount" />
              </el-col>
            </el-row>
          </div>

          <!-- 错误信息 -->
          <div v-if="importResult.errorMessages && importResult.errorMessages.length > 0" class="error-section">
            <el-collapse>
              <el-collapse-item title="查看错误详情" name="errors">
                <ul class="error-list">
                  <li v-for="(error, index) in importResult.errorMessages" :key="index">
                    {{ error }}
                  </li>
                </ul>
              </el-collapse-item>
            </el-collapse>
          </div>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus'
import { UploadFilled } from '@element-plus/icons-vue'

// 响应式数据
const uploadRef = ref()
const selectedFile = ref<File | null>(null)
const isImporting = ref(false)
const importResult = ref<any>(null)

// 表单数据
const form = reactive({
  importType: 'complete',
  conflictStrategy: 'SKIP'
})

// 文件变化处理
const handleFileChange = (file: any) => {
  selectedFile.value = file.raw
  importResult.value = null
}

// 导入处理
const handleImport = async () => {
  if (!selectedFile.value) {
    ElMessage.warning('请先选择要导入的文件')
    return
  }

  try {
    isImporting.value = true
    importResult.value = null

    // 构建表单数据
    const formData = new FormData()
    formData.append('file', selectedFile.value)
    formData.append('conflictStrategy', form.conflictStrategy)

    // 调用导入API
    const response = await fetch(`/api/product-import/${form.importType}`, {
      method: 'POST',
      body: formData,
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('token')}`
      }
    })

    if (!response.ok) {
      throw new Error('导入失败')
    }

    const result = await response.json()
    importResult.value = result

    if (result.success) {
      ElMessage.success('导入成功')
    } else {
      ElMessage.warning('导入完成，但有部分数据失败')
    }

  } catch (error: any) {
    console.error('导入失败:', error)
    ElMessage.error('导入失败: ' + error.message)
  } finally {
    isImporting.value = false
  }
}

// 下载模板
const downloadTemplate = () => {
  const templateType = form.importType === 'complete' ? '完整产品信息' : '基础产品信息'
  ElMessage.info(`正在下载${templateType}模板...`)
  // 这里可以添加实际的模板下载逻辑
}
</script>

<style scoped>
.simple-import-page {
  padding: 20px;
  max-width: 800px;
  margin: 0 auto;
}

.import-form {
  padding: 20px 0;
}

.upload-dragger {
  margin-bottom: 30px;
}

.config-section {
  margin-bottom: 30px;
  padding: 20px;
  background-color: #f5f7fa;
  border-radius: 4px;
}

.config-section h4 {
  margin: 0 0 20px 0;
  color: #303133;
}

.action-section {
  text-align: center;
  margin-bottom: 30px;
}

.action-section .el-button {
  margin: 0 10px;
}

.result-section {
  margin-top: 30px;
}

.result-details {
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

:deep(.el-upload-dragger) {
  width: 100%;
}

:deep(.el-card__header) {
  background-color: #f5f7fa;
  border-bottom: 1px solid #e4e7ed;
}
</style>
