<template>
  <div class="ink-surface-management">
    <div class="page-header">
      <div class="page-header-row">
        <AdminBackToPanel />
        <div class="page-header-text">
          <h1 class="page-title">印刷油墨面字典管理</h1>
          <p class="page-description">管理印刷油墨面分類字典（普通油墨/UV系油墨/特種油墨）</p>
        </div>
      </div>
    </div>

    <!-- 工具栏 -->
    <div class="toolbar">
      <div class="toolbar-left">
        <el-input
          v-model="searchKeyword"
          placeholder="搜索詳細名稱..."
          prefix-icon="Search"
          clearable
          style="width: 250px"
          @input="handleSearch"
        />
        <el-select v-model="filterCategory" placeholder="選擇分類" clearable style="width: 150px" @change="handleSearch">
          <el-option v-for="cat in categoryOptions" :key="cat" :label="cat" :value="cat" />
        </el-select>
      </div>
      <div class="toolbar-right">
        <el-button type="primary" @click="handleAdd">
          <el-icon><Plus /></el-icon>
          新增油墨面
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
      <el-table-column prop="category" label="分類" width="140" align="center">
        <template #default="{ row }">
          <el-tag :type="getCategoryType(row.category)" size="small">
            {{ row.category }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="detailName" label="油墨名稱" min-width="200" />
      <el-table-column prop="displayOrder" label="排序" width="80" align="center" />
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
      :title="isEditing ? '編輯油墨面' : '新增油墨面'"
      width="500px"
      @close="handleDialogClose"
    >
      <el-form
        ref="formRef"
        :model="formData"
        :rules="formRules"
        label-width="100px"
        label-position="left"
      >
        <el-form-item label="分類" prop="category">
          <el-select
            v-model="formData.category"
            placeholder="選擇分類"
            clearable
            allow-create
            filterable
            style="width: 100%"
          >
            <el-option v-for="cat in categoryOptions" :key="cat" :label="cat" :value="cat" />
          </el-select>
        </el-form-item>
        <el-form-item label="油墨名稱" prop="detailName">
          <el-input v-model="formData.detailName" placeholder="請輸入油墨名稱" />
        </el-form-item>
        <el-form-item label="排序" prop="displayOrder">
          <el-input-number v-model="formData.displayOrder" :min="0" :max="9999" style="width: 100%" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="saving" @click="handleSave">
          保存
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import AdminBackToPanel from '@/components/admin/AdminBackToPanel.vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import { inkSurfaceApi } from '@/api/modules/inkSurface'
import type { InkSurface } from '@/types/materialManagement'

// 数据
const loading = ref(false)
const saving = ref(false)
const dataList = ref<InkSurface[]>([])
const searchKeyword = ref('')
const filterCategory = ref('')

// 分类选项 - 从数据库中动态获取
const categoryOptions = computed(() => {
  const categories = new Set<string>()
  dataList.value.forEach(item => {
    if (item.category && item.category.trim()) {
      categories.add(item.category.trim())
    }
  })
  return Array.from(categories).sort()
})

// 对话框
const dialogVisible = ref(false)
const isEditing = ref(false)
const formRef = ref()
const formData = ref<Partial<InkSurface>>({
  category: '',
  detailName: '',
  displayOrder: 0
})

// 表单验证规则
const formRules = {
  category: [
    { required: true, message: '請選擇分類', trigger: 'change' }
  ],
  detailName: [
    { required: true, message: '請輸入油墨名稱', trigger: 'blur' },
    { max: 100, message: '名稱長度不能超過100個字符', trigger: 'blur' }
  ]
}

// 过滤后的数据
const filteredData = computed(() => {
  let result = dataList.value

  if (searchKeyword.value) {
    const keyword = searchKeyword.value.toLowerCase()
    result = result.filter(item =>
      item.detailName?.toLowerCase().includes(keyword) ||
      item.category?.toLowerCase().includes(keyword)
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
    const response = await inkSurfaceApi.getAll()
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
    '普通油墨': 'info',
    'UV系油墨': 'warning',
    '特种油墨': 'danger'
  }
  return typeMap[category || ''] || ''
}

// 新增
const handleAdd = () => {
  isEditing.value = false
  formData.value = {
    category: '',
    detailName: '',
    displayOrder: 0
  }
  dialogVisible.value = true
}

// 编辑
const handleEdit = (row: InkSurface) => {
  isEditing.value = true
  formData.value = { ...row }
  dialogVisible.value = true
}

// 删除
const handleDelete = async (row: InkSurface) => {
  try {
    await ElMessageBox.confirm(
      `確定要刪除油墨面「${row.detailName}」嗎？`,
      '確認刪除',
      {
        confirmButtonText: '確定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )

    await inkSurfaceApi.delete(row.id!)
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
      await inkSurfaceApi.update(formData.value.id, formData.value)
      ElMessage.success('更新成功')
    } else {
      await inkSurfaceApi.create(formData.value)
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
}

// 初始化
onMounted(() => {
  loadData()
})
</script>

<style scoped>
.ink-surface-management {
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
</style>
