<template>
  <el-dialog
    v-model="dialogVisible"
    title="产品信息导入"
    width="600px"
    :before-close="handleClose"
  >
    <div class="import-container">
      <!-- 文件上传区域 -->
      <div class="upload-section">
        <el-upload
          ref="uploadRef"
          :auto-upload="false"
          :on-change="handleFileChange"
          :on-remove="handleFileRemove"
          :accept="'.xlsx,.xls'"
          :limit="1"
          drag
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
      </div>

      <!-- 配置选项 -->
      <div class="config-section">
        <h4>导入配置</h4>
        <el-form :model="importConfig" label-width="120px">
          <el-form-item label="冲突处理策略">
            <el-select v-model="importConfig.conflictStrategy" placeholder="请选择">
              <el-option
                v-for="strategy in conflictStrategies"
                :key="strategy.value"
                :label="strategy.label"
                :value="strategy.value"
              />
            </el-select>
          </el-form-item>
          
          <el-form-item label="数据验证">
            <el-switch v-model="importConfig.validateData" />
          </el-form-item>
          
          <el-form-item label="导入前备份" v-if="importConfig.conflictStrategy === 'OVERWRITE'">
            <el-switch v-model="importConfig.backupBeforeImport" />
          </el-form-item>
        </el-form>
      </div>

      <!-- 导入进度 -->
      <div class="progress-section" v-if="isImporting">
        <el-progress
          :percentage="importProgress"
          :status="importStatus"
          :stroke-width="8"
        />
        <p class="progress-text">{{ progressText }}</p>
      </div>

      <!-- 导入结果 -->
      <div class="result-section" v-if="importResult">
        <el-alert
          :title="importResult.success ? '导入成功' : '导入完成'"
          :type="importResult.success ? 'success' : 'warning'"
          :description="importResult.message"
          show-icon
        />
        
        <div class="result-details" v-if="importResult">
          <el-descriptions :column="3" border>
            <el-descriptions-item label="总记录数">
              {{ importResult.totalCount }}
            </el-descriptions-item>
            <el-descriptions-item label="成功记录数">
              <el-tag type="success">{{ importResult.successCount }}</el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="失败记录数">
              <el-tag type="danger">{{ importResult.errorCount }}</el-tag>
            </el-descriptions-item>
          </el-descriptions>
        </div>

        <!-- 错误信息 -->
        <div class="error-section" v-if="importResult.errorMessages && importResult.errorMessages.length > 0">
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

    <template #footer>
      <div class="dialog-footer">
        <el-button @click="handleClose">取消</el-button>
        <el-button 
          type="primary" 
          @click="handleImport"
          :loading="isImporting"
          :disabled="!selectedFile"
        >
          {{ isImporting ? '导入中...' : '开始导入' }}
        </el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import { ref, reactive, computed, watch } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { UploadFilled } from '@element-plus/icons-vue'
import { productImportApi } from '../../api/modules/productImport'

// 组件属性
interface Props {
  modelValue: boolean
  importType: 'complete' | 'basic'
}

// 组件事件
interface Emits {
  (e: 'update:modelValue', value: boolean): void
  (e: 'import-success'): void
}

const props = defineProps<Props>()
const emit = defineEmits<Emits>()

// 响应式数据
const dialogVisible = computed({
  get: () => props.modelValue,
  set: (value) => emit('update:modelValue', value)
})

const uploadRef = ref()
const selectedFile = ref<File | null>(null)
const isImporting = ref(false)
const importProgress = ref(0)
const importStatus = ref<'success' | 'exception' | ''>('')
const progressText = ref('')
const importResult = ref<any>(null)

// 导入配置
const importConfig = reactive({
  conflictStrategy: 'SKIP',
  validateData: true,
  backupBeforeImport: false
})

// 冲突处理策略选项
const conflictStrategies = [
  { value: 'SKIP', label: '跳过冲突数据（推荐，安全）' },
  { value: 'OVERWRITE', label: '覆盖现有数据（谨慎使用）' },
  { value: 'CREATE_NEW', label: '创建新记录（忽略冲突）' },
  { value: 'PROMPT_USER', label: '提示用户选择' }
]

// 文件变化处理
const handleFileChange = (file: any) => {
  selectedFile.value = file.raw
  importResult.value = null
}

// 文件移除处理
const handleFileRemove = () => {
  selectedFile.value = null
  importResult.value = null
}

// 导入处理
const handleImport = async () => {
  if (!selectedFile.value) {
    ElMessage.warning('请先选择要导入的文件')
    return
  }

  // 方案 C 事中加固：覆盖式导入前二次确认
  if (importConfig.conflictStrategy === 'OVERWRITE') {
    try {
      await ElMessageBox.confirm(
        '将覆盖现有数据，是否继续？' + (importConfig.backupBeforeImport ? ' 导入前将自动备份。' : ''),
        '二次确认',
        {
          confirmButtonText: '确定导入',
          cancelButtonText: '取消',
          type: 'warning'
        }
      )
    } catch {
      return
    }
  }

  try {
    isImporting.value = true
    importProgress.value = 0
    progressText.value = '正在上传文件...'
    importStatus.value = ''

    // 构建表单数据
    const formData = new FormData()
    formData.append('file', selectedFile.value)
    formData.append('conflictStrategy', importConfig.conflictStrategy)
    formData.append('validateData', String(importConfig.validateData))
    formData.append('backupBeforeImport', String(importConfig.backupBeforeImport))

    // 调用导入API
    const result = await productImportApi.importProduct(
      props.importType,
      formData,
      (progress) => {
        importProgress.value = progress
        progressText.value = `导入进度: ${progress}%`
      }
    )

    importResult.value = result
    importProgress.value = 100
    importStatus.value = result.success ? 'success' : 'exception'
    progressText.value = result.success ? '导入完成' : '导入完成（有错误）'

    if (result.success) {
      ElMessage.success('导入成功')
      emit('import-success')
    } else {
      ElMessage.warning('导入完成，但有部分数据失败')
    }

  } catch (error: any) {
    console.error('导入失败:', error)
    ElMessage.error('导入失败: ' + error.message)
    importStatus.value = 'exception'
    progressText.value = '导入失败'
  } finally {
    isImporting.value = false
  }
}

// 关闭对话框
const handleClose = () => {
  if (isImporting.value) {
    ElMessageBox.confirm('导入正在进行中，确定要关闭吗？', '确认', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }).then(() => {
      dialogVisible.value = false
    })
  } else {
    dialogVisible.value = false
  }
}

// 重置状态
const resetState = () => {
  selectedFile.value = null
  importResult.value = null
  isImporting.value = false
  importProgress.value = 0
  importStatus.value = ''
  progressText.value = ''
  importConfig.conflictStrategy = 'SKIP'
  importConfig.validateData = true
  importConfig.backupBeforeImport = false
}

// 监听对话框显示状态
watch(() => props.modelValue, (newVal) => {
  if (newVal) {
    resetState()
  }
})
</script>

<style scoped>
.import-container {
  padding: 20px 0;
}

.upload-section {
  margin-bottom: 20px;
}

.config-section {
  margin-bottom: 20px;
  padding: 15px;
  background-color: #f5f7fa;
  border-radius: 4px;
}

.config-section h4 {
  margin: 0 0 15px 0;
  color: #303133;
}

.progress-section {
  margin-bottom: 20px;
}

.progress-text {
  text-align: center;
  margin-top: 10px;
  color: #606266;
}

.result-section {
  margin-bottom: 20px;
}

.result-details {
  margin-top: 15px;
}

.error-section {
  margin-top: 15px;
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

.dialog-footer {
  text-align: right;
}

:deep(.el-upload-dragger) {
  width: 100%;
}
</style>
