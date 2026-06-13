<template>
  <div class="notice-dictionary-management">
    <div class="page-header">
      <div class="page-header-row">
        <AdminBackToPanel />
        <div class="page-header-text">
          <h1 class="page-title">注意事項字典管理</h1>
          <p class="page-description">管理所有規則匹配時需要顯示的注意事項</p>
        </div>
      </div>
    </div>

    <!-- 工具栏 -->
    <div class="toolbar">
      <div class="toolbar-left">
        <el-input
          v-model="searchKeyword"
          placeholder="搜索編碼、標題或描述..."
          prefix-icon="Search"
          clearable
          style="width: 300px"
          @input="handleSearch"
        />
        <el-select v-model="filterCategory" placeholder="選擇分類" clearable style="width: 150px" @change="handleSearch">
          <el-option v-for="cat in categoryOptions" :key="cat" :label="cat" :value="cat" />
        </el-select>
      </div>
      <div class="toolbar-right">
        <el-button type="primary" @click="handleAdd">
          <el-icon><Plus /></el-icon>
          新增注意事項
        </el-button>
      </div>
    </div>

    <!-- 数据表格 -->
    <el-table
      v-loading="loading"
      :data="filteredData"
      border
      stripe
      style="width: 100%"
      max-height="600"
    >
      <el-table-column prop="id" label="ID" width="60" align="center" />
      <el-table-column prop="noticeCode" label="編碼" width="120" />
      <el-table-column prop="title" label="標題" width="180" show-overflow-tooltip />
      <el-table-column prop="problemDescription" label="問題描述" min-width="200" show-overflow-tooltip />
      <el-table-column prop="solution" label="解決方案" min-width="200" show-overflow-tooltip />
      <el-table-column prop="category" label="分類" width="100" align="center">
        <template #default="{ row }">
          <el-tag :type="getCategoryType(row.category)" size="small">
            {{ row.category || '通用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="priority" label="優先級" width="80" align="center">
        <template #default="{ row }">
          <el-tag :type="row.priority >= 5 ? 'danger' : 'info'" size="small">
            {{ row.priority || 0 }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="isActive" label="狀態" width="80" align="center">
        <template #default="{ row }">
          <el-tag :type="row.isActive ? 'success' : 'info'" size="small">
            {{ row.isActive ? '啟用' : '停用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="richContent" label="富文本" width="80" align="center">
        <template #default="{ row }">
          <el-button 
            v-if="row.richContent" 
            type="success" 
            size="small" 
            link 
            @click="handlePreview(row)"
          >
            預覽
          </el-button>
          <span v-else class="no-content">-</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="150" align="center" fixed="right">
        <template #default="{ row }">
          <el-button type="primary" size="small" link @click="handleEdit(row)">
            編輯
          </el-button>
          <el-button type="danger" size="small" link @click="handleDelete(row)">
            刪除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="isEditing ? '編輯注意事項' : '新增注意事項'"
      width="900px"
      @close="handleDialogClose"
    >
      <el-form
        ref="formRef"
        :model="formData"
        :rules="formRules"
        label-width="100px"
        label-position="left"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="編碼" prop="noticeCode">
              <el-input v-model="formData.noticeCode" placeholder="如：PRINT_001" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="標題" prop="title">
              <el-input v-model="formData.title" placeholder="注意事項標題（可選）" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="分類" prop="category">
              <el-select 
                v-model="formData.category" 
                placeholder="選擇或輸入分類" 
                clearable 
                allow-create 
                filterable
                style="width: 100%"
              >
                <el-option v-for="cat in categoryOptions" :key="cat" :label="cat" :value="cat" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="優先級" prop="priority">
              <el-input-number v-model="formData.priority" :min="0" :max="10" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="狀態" prop="isActive">
              <el-switch v-model="formData.isActive" active-text="啟用" inactive-text="停用" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-divider content-position="left">簡要描述（可選）</el-divider>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="問題描述" prop="problemDescription">
              <el-input
                v-model="formData.problemDescription"
                type="textarea"
                :rows="2"
                placeholder="簡要描述問題（可選）"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="解決方案" prop="solution">
              <el-input
                v-model="formData.solution"
                type="textarea"
                :rows="2"
                placeholder="簡要解決方案（可選）"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-divider content-position="left">富文本內容（支持表格、圖片等）</el-divider>
        <el-form-item label="" prop="richContent" label-width="0">
          <div class="rich-editor-container">
            <Toolbar
              class="toolbar-container"
              :editor="editorRef"
              :defaultConfig="toolbarConfig"
              :mode="mode"
            />
            <Editor
              :key="editorKey"
              class="editor-container"
              :defaultHtml="formData.richContent || ''"
              :defaultConfig="editorConfig"
              :mode="mode"
              @onCreated="handleCreated"
              @onChange="handleChange"
            />
          </div>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="saving" @click="handleSave">
          保存
        </el-button>
      </template>
    </el-dialog>

    <!-- 预览对话框 -->
    <el-dialog
      v-model="previewVisible"
      title="內容預覽"
      width="800px"
    >
      <div class="preview-content" v-html="previewContent"></div>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, onBeforeUnmount, shallowRef } from 'vue'
import AdminBackToPanel from '@/components/admin/AdminBackToPanel.vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import { noticeDictionaryApi } from '@/api/modules/noticeDictionary'
import type { NoticeDictionary } from '@/types/materialManagement'
import '@wangeditor/editor/dist/css/style.css'
import { Editor, Toolbar } from '@wangeditor/editor-for-vue'
import type { IDomEditor, IEditorConfig, IToolbarConfig } from '@wangeditor/editor'

// 数据
const loading = ref(false)
const saving = ref(false)
const dataList = ref<NoticeDictionary[]>([])
const searchKeyword = ref('')
const filterCategory = ref('')
const previewVisible = ref(false)
const previewContent = ref('')

// WangEditor 相关
const editorRef = shallowRef<IDomEditor | null>(null)
const mode = 'default'
const editorKey = ref(0)  // 用于强制重新创建编辑器

// 分类选项 - 从数据库中动态获取
const categoryOptions = computed(() => {
  const categories = new Set<string>()
  // 从现有数据中提取所有分类
  dataList.value.forEach(item => {
    if (item.category && item.category.trim()) {
      categories.add(item.category.trim())
    }
  })
  // 转换为数组并排序
  return Array.from(categories).sort()
})

// 对话框
const dialogVisible = ref(false)
const isEditing = ref(false)
const formRef = ref()
const formData = ref<Partial<NoticeDictionary>>({
  noticeCode: '',
  title: '',
  problemDescription: '',
  solution: '',
  category: '',
  priority: 0,
  isActive: true,
  richContent: ''
})

// WangEditor 配置：支持本地图片插入（转为 Base64，无需上传服务器）
const editorConfig: Partial<IEditorConfig> = {
  placeholder: '在此輸入詳細內容，支持表格、圖片等...',
  MENU_CONF: {
    uploadImage: {
      // 将本地图片转为 Base64 插入，不依赖上传接口
      async customUpload(file: File, insertFn: (url: string, alt?: string, href?: string) => void) {
        const reader = new FileReader()
        reader.onload = (e) => {
          const base64Url = (e.target?.result as string) || ''
          if (base64Url) insertFn(base64Url, file.name, '')
        }
        reader.readAsDataURL(file)
      },
      maxFileSize: 5 * 1024 * 1024, // 5MB
      maxNumberOfFiles: 5,
      allowedFileTypes: ['image/*']
    }
  }
}

const toolbarConfig: Partial<IToolbarConfig> = {
  excludeKeys: [
    'uploadVideo',
    'group-video'
  ]
}

// 编辑器创建回调
const handleCreated = (editor: IDomEditor) => {
  editorRef.value = editor
}

// 编辑器内容变化
const handleChange = (editor: IDomEditor) => {
  formData.value.richContent = editor.getHtml()
}

// 表单验证规则
const formRules = {
  noticeCode: [
    { required: true, message: '請輸入編碼', trigger: 'blur' },
    { max: 50, message: '編碼長度不能超過50個字符', trigger: 'blur' }
  ]
  // problemDescription 和 solution 为可选字段，不需要必填验证
}

// 过滤后的数据
const filteredData = computed(() => {
  let result = dataList.value

  if (searchKeyword.value) {
    const keyword = searchKeyword.value.toLowerCase()
    result = result.filter(item =>
      item.noticeCode?.toLowerCase().includes(keyword) ||
      item.title?.toLowerCase().includes(keyword) ||
      item.problemDescription?.toLowerCase().includes(keyword) ||
      item.solution?.toLowerCase().includes(keyword)
    )
  }

  if (filterCategory.value) {
    result = result.filter(item => item.category === filterCategory.value)
  }

  return result
})

// 加载数据
const loadData = async () => {
  loading.value = true
  try {
    const response = await noticeDictionaryApi.getAll()
    dataList.value = response || []
  } catch (error) {
    console.error('加载数据失败:', error)
    ElMessage.error('加載數據失敗')
  } finally {
    loading.value = false
  }
}

// 搜索
const handleSearch = () => {
  // 过滤在 computed 中自动处理
}

// 获取分类标签类型
const getCategoryType = (category?: string): 'success' | 'warning' | 'info' | 'primary' | 'danger' | '' => {
  const typeMap: Record<string, 'success' | 'warning' | 'info' | 'primary' | 'danger'> = {
    '印刷': 'warning',
    '過膠': 'info',
    '燙金': 'success',
    '前工序': 'primary',
    '絲印': 'danger'
  }
  return typeMap[category || ''] || ''
}

// 新增
const handleAdd = () => {
  isEditing.value = false
  // 先销毁旧编辑器
  if (editorRef.value) {
    editorRef.value.destroy()
    editorRef.value = null
  }
  formData.value = {
    noticeCode: '',
    title: '',
    problemDescription: '',
    solution: '',
    category: '',
    priority: 0,
    isActive: true,
    richContent: ''
  }
  // 强制重新创建编辑器
  editorKey.value++
  dialogVisible.value = true
}

// 编辑
const handleEdit = (row: NoticeDictionary) => {
  isEditing.value = true
  // 先销毁旧编辑器
  if (editorRef.value) {
    editorRef.value.destroy()
    editorRef.value = null
  }
  formData.value = { ...row }
  // 强制重新创建编辑器
  editorKey.value++
  dialogVisible.value = true
}

// 预览富文本内容
const handlePreview = (row: NoticeDictionary) => {
  previewContent.value = row.richContent || '<p>暫無富文本內容</p>'
  previewVisible.value = true
}

// 删除
const handleDelete = async (row: NoticeDictionary) => {
  try {
    await ElMessageBox.confirm(
      `確定要刪除注意事項「${row.noticeCode}」嗎？`,
      '確認刪除',
      {
        confirmButtonText: '確定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )

    await noticeDictionaryApi.delete(row.id!)
    ElMessage.success('刪除成功')
    await loadData()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除失败:', error)
      ElMessage.error('刪除失敗')
    }
  }
}

// 保存
const handleSave = async () => {
  if (!formRef.value) return

  try {
    await formRef.value.validate()
    saving.value = true

    if (isEditing.value && formData.value.id) {
      await noticeDictionaryApi.update(formData.value.id, formData.value)
      ElMessage.success('更新成功')
    } else {
      await noticeDictionaryApi.create(formData.value)
      ElMessage.success('創建成功')
    }

    dialogVisible.value = false
    await loadData()
  } catch (error) {
    console.error('保存失败:', error)
    ElMessage.error('保存失敗')
  } finally {
    saving.value = false
  }
}

// 对话框关闭
const handleDialogClose = () => {
  formRef.value?.resetFields()
  // 销毁编辑器避免内存泄漏
  if (editorRef.value) {
    editorRef.value.destroy()
    editorRef.value = null
  }
}

// 初始化
onMounted(() => {
  loadData()
})

// 销毁编辑器
onBeforeUnmount(() => {
  if (editorRef.value) {
    editorRef.value.destroy()
    editorRef.value = null
  }
})
</script>

<style scoped>
.notice-dictionary-management {
  padding: 20px;
  background: #fff;
  border-radius: 8px;
}

.page-header {
  margin-bottom: 24px;
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

.page-header .page-title {
  font-size: 20px;
  font-weight: 600;
  color: #303133;
  margin: 0 0 8px 0;
}

.page-header .page-description {
  font-size: 14px;
  color: #909399;
  margin: 0;
}

.toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.toolbar .toolbar-left {
  display: flex;
  gap: 12px;
}

.priority-hint {
  margin-left: 12px;
  font-size: 12px;
  color: #909399;
}

/* 富文本编辑器容器 */
.rich-editor-container {
  width: 100%;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  overflow: hidden;
}

.rich-editor-container .toolbar-container {
  border-bottom: 1px solid #dcdfe6;
}

.rich-editor-container .editor-container {
  height: 300px;
  overflow-y: auto;
}

/* 预览对话框内容样式 */
.preview-content {
  padding: 16px;
  min-height: 200px;
  max-height: 500px;
  overflow-y: auto;
  background: #fafafa;
  border-radius: 4px;
}

/* 预览内容中的表格样式 */
.preview-content :deep(table) {
  width: 100%;
  border-collapse: collapse;
  margin: 10px 0;
}

.preview-content :deep(table td),
.preview-content :deep(table th) {
  border: 1px solid #dcdfe6;
  padding: 8px 12px;
  text-align: left;
}

.preview-content :deep(table th) {
  background: #f5f7fa;
  font-weight: 600;
}

.preview-content :deep(table tr:nth-child(even)) {
  background: #fafafa;
}

.no-content {
  color: #c0c4cc;
}
</style>

