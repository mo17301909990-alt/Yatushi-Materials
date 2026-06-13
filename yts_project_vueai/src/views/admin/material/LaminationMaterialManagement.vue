<template>
  <div class="lamination-material-management">
    <h2 class="page-title">過膠材料產品管理</h2>

    <!-- 搜索栏 -->
    <el-row :gutter="16" class="toolbar">
      <el-col :span="8">
        <el-input
          v-model="searchKeyword"
          placeholder="搜索產品名稱、編碼、顏色..."
          clearable
          @clear="fetchProducts"
          @keyup.enter="fetchProducts"
        >
          <template #append>
            <el-button @click="fetchProducts">搜索</el-button>
          </template>
        </el-input>
      </el-col>
      <el-col :span="4">
        <el-button type="primary" @click="openProductDialog()">新增產品</el-button>
      </el-col>
    </el-row>

    <!-- 产品列表 -->
    <el-table :data="products" border stripe v-loading="loading" style="width: 100%">
      <el-table-column prop="id" label="ID" width="60" />
      <el-table-column prop="materialCode" label="物料編碼" min-width="140" />
      <el-table-column prop="stockCode" label="存倉編號" min-width="120" />
      <el-table-column prop="materialName" label="物料名稱" min-width="160" show-overflow-tooltip />
      <el-table-column prop="materialType" label="材料類型" width="100" />
      <el-table-column prop="color" label="顏色" width="80" />
      <el-table-column prop="category" label="材質" width="100" />
      <el-table-column prop="isActive" label="狀態" width="70">
        <template #default="{ row }">
          <el-tag :type="row.isActive ? 'success' : 'danger'" size="small">
            {{ row.isActive ? '啟用' : '停用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="200" fixed="right">
        <template #default="{ row }">
          <el-button size="small" @click="openCompatibilityDialog(row)">兼容性</el-button>
          <el-button size="small" @click="openProductDialog(row)">編輯</el-button>
          <el-popconfirm title="確定刪除該產品？" @confirm="deleteProduct(row.id)">
            <template #reference>
              <el-button size="small" type="danger">刪除</el-button>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>

    <!-- 产品编辑对话框 -->
    <el-dialog v-model="productDialogVisible" :title="editingProduct?.id ? '編輯產品' : '新增產品'" width="700px">
      <el-form :model="editingProduct" label-width="110px" v-loading="saving">
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="物料編碼">
              <el-input v-model="editingProduct.materialCode" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="存倉編號">
              <el-input v-model="editingProduct.stockCode" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="物料名稱" required>
          <el-input v-model="editingProduct.materialName" />
        </el-form-item>
        <el-form-item label="物料用途">
          <el-input v-model="editingProduct.usageText" type="textarea" :rows="2" />
        </el-form-item>
        <el-row :gutter="16">
          <el-col :span="8">
            <el-form-item label="材料類型">
              <el-input v-model="editingProduct.materialType" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="顏色">
              <el-input v-model="editingProduct.color" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="材質">
              <el-input v-model="editingProduct.category" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="膠膜厚度">
              <el-input v-model="editingProduct.thicknessFilm" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="膠水厚度">
              <el-input v-model="editingProduct.thicknessGlue" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="尺寸資訊">
              <el-input v-model="editingProduct.sizeInfo" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="形狀">
              <el-input v-model="editingProduct.shape" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="責任人">
          <el-input v-model="editingProduct.responsiblePerson" />
        </el-form-item>
        <el-form-item label="啟用狀態">
          <el-switch v-model="editingProduct.isActive" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="productDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveProduct" :loading="saving">保存</el-button>
      </template>
    </el-dialog>

    <!-- 兼容性管理对话框 -->
    <el-dialog v-model="compatibilityDialogVisible" :title="'兼容性管理 - ' + currentProduct?.materialName" width="800px">
      <el-table :data="compatibilities" border stripe v-loading="compatLoading" style="width: 100%">
        <el-table-column prop="postProcessingStep" label="後加工工序" min-width="200" />
        <el-table-column prop="compatibilityStatus" label="兼容性狀態" width="120">
          <template #default="{ row }">
            <el-tag
              :type="row.compatibilityStatus === 'V' ? 'success' : row.compatibilityStatus === 'X' ? 'danger' : 'warning'"
              size="small"
            >
              {{ row.compatibilityStatus === 'V' ? '兼容' : row.compatibilityStatus === 'X' ? '不兼容' : '有條件兼容' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="remark" label="備註" min-width="200" show-overflow-tooltip />
        <el-table-column prop="displayOrder" label="排序" width="60" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button size="small" @click="openCompatibilityEditDialog(row)">編輯</el-button>
            <el-popconfirm title="確定刪除此兼容性配置？" @confirm="deleteCompatibility(row.id)">
              <template #reference>
                <el-button size="small" type="danger">刪除</el-button>
              </template>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>
      <div style="margin-top: 16px; text-align: right">
        <el-button type="primary" @click="openCompatibilityEditDialog()">新增兼容性</el-button>
      </div>
    </el-dialog>

    <!-- 兼容性编辑对话框 -->
    <el-dialog v-model="compatEditDialogVisible" :title="editingCompat?.id ? '編輯兼容性' : '新增兼容性'" width="500px">
      <el-form :model="editingCompat" label-width="120px">
        <el-form-item label="後加工工序" required>
          <el-input v-model="editingCompat.postProcessingStep" />
        </el-form-item>
        <el-form-item label="兼容性狀態" required>
          <el-select v-model="editingCompat.compatibilityStatus" style="width: 100%">
            <el-option label="V - 兼容" value="V" />
            <el-option label="X - 不兼容" value="X" />
            <el-option label="▷ - 有條件兼容" value="▷" />
          </el-select>
        </el-form-item>
        <el-form-item label="備註">
          <el-input v-model="editingCompat.remark" type="textarea" :rows="2" />
        </el-form-item>
        <el-form-item label="排序">
          <el-input-number v-model="editingCompat.displayOrder" :min="0" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="compatEditDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveCompatibility" :loading="saving">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { laminationMaterialApi, type LaminationMaterialProduct, type LaminationMaterialCompatibility } from '@/api/modules/laminationMaterial';
import { ElMessage } from 'element-plus';

const loading = ref(false);
const saving = ref(false);
const products = ref<LaminationMaterialProduct[]>([]);
const searchKeyword = ref('');

const productDialogVisible = ref(false);
const editingProduct = ref<LaminationMaterialProduct>({});

const compatibilityDialogVisible = ref(false);
const compatLoading = ref(false);
const currentProduct = ref<LaminationMaterialProduct | null>(null);
const compatibilities = ref<LaminationMaterialCompatibility[]>([]);

const compatEditDialogVisible = ref(false);
const editingCompat = ref<LaminationMaterialCompatibility>({ productId: 0, postProcessingStep: '', compatibilityStatus: 'V', displayOrder: 0 });

async function fetchProducts() {
  loading.value = true;
  try {
    if (searchKeyword.value.trim()) {
      products.value = await laminationMaterialApi.searchProducts(searchKeyword.value.trim());
    } else {
      products.value = await laminationMaterialApi.getProducts();
    }
  } catch (e: any) {
    ElMessage.error('加載產品列表失敗: ' + (e.message || e));
  } finally {
    loading.value = false;
  }
}

function openProductDialog(product?: LaminationMaterialProduct) {
  editingProduct.value = product ? { ...product } : {
    materialName: '',
    isActive: true,
    displayOrder: 0
  } as any;
  productDialogVisible.value = true;
}

async function saveProduct() {
  if (!editingProduct.value.materialName) {
    ElMessage.warning('請填寫物料名稱');
    return;
  }
  saving.value = true;
  try {
    if (editingProduct.value.id) {
      await laminationMaterialApi.updateProduct(editingProduct.value.id, editingProduct.value);
      ElMessage.success('更新成功');
    } else {
      await laminationMaterialApi.createProduct(editingProduct.value);
      ElMessage.success('新增成功');
    }
    productDialogVisible.value = false;
    await fetchProducts();
  } catch (e: any) {
    ElMessage.error('保存失敗: ' + (e.message || e));
  } finally {
    saving.value = false;
  }
}

async function deleteProduct(id: number | undefined) {
  if (!id) return;
  try {
    await laminationMaterialApi.deleteProduct(id);
    ElMessage.success('刪除成功');
    await fetchProducts();
  } catch (e: any) {
    ElMessage.error('刪除失敗: ' + (e.message || e));
  }
}

async function openCompatibilityDialog(product: LaminationMaterialProduct) {
  currentProduct.value = product;
  compatibilityDialogVisible.value = true;
  await fetchCompatibilities(product.id!);
}

async function fetchCompatibilities(productId: number) {
  compatLoading.value = true;
  try {
    compatibilities.value = await laminationMaterialApi.getCompatibilitiesByProductId(productId);
  } catch (e: any) {
    ElMessage.error('加載兼容性列表失敗: ' + (e.message || e));
  } finally {
    compatLoading.value = false;
  }
}

function openCompatibilityEditDialog(compat?: LaminationMaterialCompatibility) {
  editingCompat.value = compat ? { ...compat } : {
    productId: currentProduct.value?.id || 0,
    postProcessingStep: '',
    compatibilityStatus: 'V',
    displayOrder: 0
  } as any;
  compatEditDialogVisible.value = true;
}

async function saveCompatibility() {
  if (!editingCompat.value.postProcessingStep) {
    ElMessage.warning('請填寫後加工工序');
    return;
  }
  if (!editingCompat.value.compatibilityStatus) {
    ElMessage.warning('請選擇兼容性狀態');
    return;
  }
  saving.value = true;
  try {
    if (editingCompat.value.id) {
      await laminationMaterialApi.updateCompatibility(editingCompat.value.id, editingCompat.value);
      ElMessage.success('更新成功');
    } else {
      editingCompat.value.productId = currentProduct.value?.id || 0;
      await laminationMaterialApi.createCompatibility(editingCompat.value);
      ElMessage.success('新增成功');
    }
    compatEditDialogVisible.value = false;
    if (currentProduct.value?.id) {
      await fetchCompatibilities(currentProduct.value.id);
    }
  } catch (e: any) {
    ElMessage.error('保存失敗: ' + (e.message || e));
  } finally {
    saving.value = false;
  }
}

async function deleteCompatibility(id: number | undefined) {
  if (!id) return;
  try {
    await laminationMaterialApi.deleteCompatibility(id);
    ElMessage.success('刪除成功');
    if (currentProduct.value?.id) {
      await fetchCompatibilities(currentProduct.value.id);
    }
  } catch (e: any) {
    ElMessage.error('刪除失敗: ' + (e.message || e));
  }
}

onMounted(() => {
  fetchProducts();
});
</script>

<style scoped>
.lamination-material-management {
  padding: 20px;
}
.page-title {
  margin-bottom: 16px;
  font-size: 20px;
  font-weight: 600;
}
.toolbar {
  margin-bottom: 16px;
}
</style>
