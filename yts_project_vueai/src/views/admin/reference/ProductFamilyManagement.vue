<template>
  <div class="product-family-management">
    <div class="page-header">
      <div class="page-header-row">
        <AdminBackToPanel />
        <div class="page-header-text">
          <h1 class="page-title">產品家族字典管理</h1>
          <p class="page-description">管理產品分類體系：大類 &gt; 子類 &gt; 詳細名稱</p>
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
          style="width: 240px"
          @input="handleSearch"
        />
        <el-select v-model="filterCategory" placeholder="選擇大類" clearable style="width: 160px" @change="handleSearch">
          <el-option v-for="cat in categoryOptions" :key="cat" :label="cat" :value="cat" />
        </el-select>
      </div>
      <div class="toolbar-right">
        <el-button type="primary" @click="handleAdd">
          <el-icon><Plus /></el-icon>
          新增產品家族
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
      <el-table-column prop="category" label="大類" width="120">
        <template #default="{ row }">
          <el-tag :type="getCategoryTagType(row.category)" size="small">
            {{ row.category }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="subCategory" label="子類" width="120">
        <template #default="{ row }">
          <span v-if="row.subCategory">{{ row.subCategory }}</span>
          <span v-else class="no-content">-</span>
        </template>
      </el-table-column>
      <el-table-column prop="detailName" label="詳細名稱" min-width="200" />
      <el-table-column prop="displayOrder" label="排序" width="80" align="center" />
      <el-table-column label="操作" width="150" align="center" fixed="right">
        <template #default="{ row }">
          <el-button type="primary" size="small" link @click="handleEdit(row)">編輯</el-button>
          <el-button type="danger" size="small" link @click="handleDelete(row)">刪除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="isEditing ? '編輯產品家族' : '新增產品家族'"
      width="600px"
      @close="handleDialogClose"
    >
      <el-form
        ref="formRef"
        :model="formData"
        :rules="formRules"
        label-width="100px"
        label-position="left"
      >
        <el-form-item label="大類" prop="category">
          <el-select
            v-model="formData.category"
            placeholder="選擇或輸入大類"
            allow-create
            filterable
            clearable
            style="width: 100%"
          >
            <el-option
              v-for="cat in categoryOptions"
              :key="cat"
              :label="cat"
              :value="cat"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="子類" prop="subCategory">
          <el-select
            v-model="formData.subCategory"
            placeholder="選擇或輸入子類（可選）"
            allow-create
            filterable
            clearable
            style="width: 100%"
          >
            <el-option
              v-for="sub in subCategoryOptions"
              :key="sub"
              :label="sub"
              :value="sub"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="詳細名稱" prop="detailName">
          <el-input v-model="formData.detailName" placeholder="請輸入詳細名稱" />
        </el-form-item>
        <el-form-item label="排序" prop="displayOrder">
          <el-input-number
            v-model="formData.displayOrder"
            :min="0"
            :max="9999"
            style="width: 100%"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="saving" @click="handleSave">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, watch } from 'vue'
import AdminBackToPanel from '@/components/admin/AdminBackToPanel.vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import {
  getAllProductFamilies,
  createProductFamily,
  updateProductFamily,
  deleteProductFamily,
  getDistinctCategories,
  getDistinctSubCategories,
  type ReferenceProductFamily
} from '@/api/modules/referenceProductFamily'

const loading = ref(false)
const saving = ref(false)
const dataList = ref<ReferenceProductFamily[]>([])
const searchKeyword = ref('')
const filterCategory = ref('')
const categoryOptions = ref<string[]>([])
const subCategoryOptions = ref<string[]>([])

const dialogVisible = ref(false)
const isEditing = ref(false)
const formRef = ref()
const formData = ref<Partial<ReferenceProductFamily>>({
  category: '',
  subCategory: '',
  detailName: '',
  displayOrder: 0
})

const formRules = {
  category: [{ required: true, message: '請選擇或輸入大類', trigger: 'blur' }],
  detailName: [{ required: true, message: '請輸入詳細名稱', trigger: 'blur' }]
}

const filteredData = computed(() => {
  let result = dataList.value
  if (searchKeyword.value) {
    const kw = searchKeyword.value.toLowerCase()
    result = result.filter(item => item.detailName?.toLowerCase().includes(kw))
  }
  if (filterCategory.value) {
    result = result.filter(item => item.category === filterCategory.value)
  }
  return result
})

const getCategoryTagType = (category: string): 'success' | 'warning' | 'info' | 'primary' | 'danger' | '' => {
  const map: Record<string, 'success' | 'warning' | 'info' | 'primary' | 'danger'> = {
    '书版': 'primary',
    '咭書': 'success',
    '活頁書': 'warning',
    '盒': '',
    '拼圖': 'danger',
    '贺咭': 'warning'
  }
  return map[category] || 'info'
}

const loadData = async () => {
  loading.value = true
  try {
    const [list, cats] = await Promise.all([
      getAllProductFamilies(),
      getDistinctCategories()
    ])
    dataList.value = list || []
    categoryOptions.value = cats || []
  } catch (error) {
    console.error('加载数据失败:', error)
    ElMessage.error('加載數據失敗')
  } finally {
    loading.value = false
  }
}

const loadSubCategories = async (category?: string) => {
  try {
    subCategoryOptions.value = (await getDistinctSubCategories(category)) || []
  } catch {
    subCategoryOptions.value = []
  }
}

const handleSearch = () => {
  // computed handles filtering
}

const handleAdd = () => {
  isEditing.value = false
  formData.value = { category: '', subCategory: '', detailName: '', displayOrder: 0 }
  dialogVisible.value = true
}

const handleEdit = (row: ReferenceProductFamily) => {
  isEditing.value = true
  formData.value = { ...row }
  loadSubCategories(row.category)
  dialogVisible.value = true
}

const handleDelete = async (row: ReferenceProductFamily) => {
  try {
    await ElMessageBox.confirm(
      `確定要刪除「${row.detailName}」嗎？`,
      '確認刪除',
      { confirmButtonText: '確定', cancelButtonText: '取消', type: 'warning' }
    )
    await deleteProductFamily(row.id!)
    ElMessage.success('刪除成功')
    await loadData()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除失败:', error)
      ElMessage.error('刪除失敗')
    }
  }
}

const handleSave = async () => {
  if (!formRef.value) return
  try {
    await formRef.value.validate()
    saving.value = true
    if (isEditing.value && formData.value.id) {
      await updateProductFamily(formData.value.id, formData.value)
      ElMessage.success('更新成功')
    } else {
      await createProductFamily(formData.value)
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

const handleDialogClose = () => {
  formRef.value?.resetFields()
}

watch(() => formData.value.category, (newCat) => {
  if (newCat) {
    loadSubCategories(newCat)
  } else {
    subCategoryOptions.value = []
  }
})

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.product-family-management {
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

.no-content {
  color: #c0c4cc;
}
</style>
