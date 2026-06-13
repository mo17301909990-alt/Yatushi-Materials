<template>
  <div class="min-h-screen bg-white py-6">
    <div class="max-w-5xl mx-auto px-4 sm:px-6 lg:px-8">
      <div class="mb-6 flex flex-wrap justify-between items-start gap-4">
        <div>
          <h1 class="text-2xl font-semibold text-gray-900">
            {{ displayTitle }}
          </h1>
          <p v-if="doc?.updatedAt && !isEditing" class="mt-1 text-sm text-gray-500">
            最後更新：{{ formatUpdatedAt(doc.updatedAt) }}
          </p>
        </div>
        <div class="flex flex-wrap gap-2">
          <template v-if="canEdit">
            <button
              v-if="!isEditing"
              type="button"
              class="px-4 py-2 rounded-md text-sm font-medium bg-indigo-600 text-white hover:bg-indigo-700"
              @click="startEdit"
            >
              編輯指南
            </button>
            <template v-else>
              <button
                type="button"
                class="px-4 py-2 rounded-md text-sm font-medium bg-indigo-600 text-white hover:bg-indigo-700 disabled:opacity-50"
                :disabled="saving"
                @click="save"
              >
                {{ saving ? '保存中…' : '保存' }}
              </button>
              <button
                type="button"
                class="px-4 py-2 rounded-md text-sm font-medium border border-gray-300 text-gray-700 hover:bg-gray-50"
                :disabled="saving"
                @click="cancelEdit"
              >
                取消
              </button>
            </template>
          </template>
        </div>
      </div>

      <div v-if="loading" class="py-12 text-center text-gray-500">加載中…</div>

      <template v-else>
        <div v-if="!isEditing" class="guide-html rounded-lg border border-gray-100 bg-gray-50/50 p-6" v-html="doc?.bodyHtml || ''" />

        <div v-else class="space-y-4">
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">標題</label>
            <input
              v-model="editTitle"
              type="text"
              class="w-full rounded-md border border-gray-300 px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-indigo-500"
            />
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">正文</label>
            <div class="border border-gray-200 rounded-md overflow-hidden bg-white">
              <Toolbar
                :key="'tb-' + editorKey"
                style="border-bottom: 1px solid #e5e7eb"
                :editor="editorRef"
                :defaultConfig="toolbarConfig"
                :mode="mode"
              />
              <Editor
                :key="'ed-' + editorKey"
                style="height: min(70vh, 520px); overflow-y: auto"
                :defaultHtml="editHtml"
                :defaultConfig="editorConfig"
                :mode="mode"
                @onCreated="handleCreated"
                @onChange="handleChange"
              />
            </div>
            <p class="mt-2 text-xs text-gray-500">
              可從 Word 複製內容粘貼；圖片和視頻可插入本地文件（Base64 寫入 HTML，請控制檔案大小）。
            </p>
          </div>
        </div>
      </template>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, onBeforeUnmount, shallowRef } from 'vue'
import { ElMessage } from 'element-plus'
import axios from 'axios'
import '@wangeditor/editor/dist/css/style.css'
import { Editor, Toolbar } from '@wangeditor/editor-for-vue'
import type { IDomEditor, IEditorConfig, IToolbarConfig } from '@wangeditor/editor'
import { usePermissionStore } from '@/stores/permission'
import {
  systemContentApi,
  HOT_STAMPING_MATERIAL_GUIDE_KEY,
  type SystemContentDto
} from '@/api/modules/systemContent'

const permissionStore = usePermissionStore()
const loading = ref(true)
const saving = ref(false)
const doc = ref<SystemContentDto | null>(null)
const isEditing = ref(false)
const editTitle = ref('')
const editHtml = ref('')
const editorKey = ref(0)

const editorRef = shallowRef<IDomEditor | null>(null)
const mode = 'default'

const canEdit = computed(
  () => permissionStore.isAdmin || permissionStore.hasPermission('system:guide:edit')
)

const displayTitle = computed(() => {
  if (isEditing.value) return editTitle.value || '燙金紙物料數據更新操作指南'
  return doc.value?.title?.trim() || '燙金紙物料數據更新操作指南'
})

const editorConfig: Partial<IEditorConfig> = {
  placeholder: '在此編輯操作指南正文…',
  MENU_CONF: {
    uploadImage: {
      async customUpload(file: File, insertFn: (url: string, alt?: string, href?: string) => void) {
        try {
          const formData = new FormData()
          formData.append('file', file)
          const response = await axios.post('/api/guide-upload/image', formData, {
            headers: { 'Content-Type': 'multipart/form-data' }
          })
          insertFn(response.data.url, file.name, '')
        } catch (err) {
          console.error('图片上传失败:', err)
          ElMessage.error('图片上传失败')
        }
      },
      maxFileSize: 5 * 1024 * 1024,
      maxNumberOfFiles: 5,
      allowedFileTypes: ['image/*']
    },
    uploadVideo: {
      async customUpload(file: File, insertFn: (url: string, alt?: string, href?: string) => void) {
        try {
          const formData = new FormData()
          formData.append('file', file)
          const response = await axios.post('/api/guide-upload/video', formData, {
            headers: { 'Content-Type': 'multipart/form-data' }
          })
          const videoUrl = response.data.url
          // 插入视频URL（点击播放时才加载）
          insertFn(videoUrl, file.name, '')
        } catch (err) {
          console.error('视频上传失败:', err)
          ElMessage.error('视频上传失败')
        }
      },
      maxFileSize: 100 * 1024 * 1024,
      maxNumberOfFiles: 3,
      allowedFileTypes: ['video/*']
    }
  }
}

const toolbarConfig: Partial<IToolbarConfig> = {
  excludeKeys: []
}

function handleCreated(editor: IDomEditor) {
  editorRef.value = editor
}

function handleChange(editor: IDomEditor) {
  editHtml.value = editor.getHtml()
}

function formatUpdatedAt(iso: string) {
  try {
    const d = new Date(iso)
    if (Number.isNaN(d.getTime())) return iso
    return d.toLocaleString('zh-TW', { hour12: false })
  } catch {
    return iso
  }
}

async function load() {
  loading.value = true
  try {
    if (!permissionStore.currentUserRbacReady) {
      await permissionStore.initPermissions()
    }
    const data = await systemContentApi.getByKey(HOT_STAMPING_MATERIAL_GUIDE_KEY)
    doc.value = data
  } catch (e) {
    console.error(e)
    ElMessage.error('加載操作指南失敗')
  } finally {
    loading.value = false
  }
}

function startEdit() {
  editTitle.value = doc.value?.title?.trim() || '燙金紙物料數據更新操作指南'
  editHtml.value = doc.value?.bodyHtml || ''
  editorKey.value += 1
  isEditing.value = true
}

function cancelEdit() {
  isEditing.value = false
  editorRef.value?.destroy()
  editorRef.value = null
}

async function save() {
  const title = editTitle.value.trim()
  if (!title) {
    ElMessage.warning('請填寫標題')
    return
  }
  saving.value = true
  try {
    const html = editorRef.value?.getHtml() ?? editHtml.value
    const updated = await systemContentApi.updateByKey(HOT_STAMPING_MATERIAL_GUIDE_KEY, {
      title,
      bodyHtml: html
    })
    doc.value = updated
    ElMessage.success('已保存')
    cancelEdit()
  } catch (e: unknown) {
    console.error(e)
    const msg =
      (e as { response?: { data?: { message?: string } } })?.response?.data?.message ||
      '保存失敗（需管理員權限）'
    ElMessage.error(msg)
  } finally {
    saving.value = false
  }
}

onMounted(() => {
  void load()
})

onBeforeUnmount(() => {
  editorRef.value?.destroy()
  editorRef.value = null
})
</script>

<style scoped>
.guide-html :deep(h1),
.guide-html :deep(h2),
.guide-html :deep(h3) {
  font-weight: 600;
  margin-top: 1rem;
  margin-bottom: 0.5rem;
  color: #111827;
}
.guide-html :deep(h1) {
  font-size: 1.5rem;
}
.guide-html :deep(h2) {
  font-size: 1.25rem;
}
.guide-html :deep(h3) {
  font-size: 1.1rem;
}
.guide-html :deep(p) {
  margin: 0.5rem 0;
  line-height: 1.6;
  color: #374151;
}
.guide-html :deep(ul),
.guide-html :deep(ol) {
  margin: 0.5rem 0 0.5rem 1.25rem;
  padding-left: 0.5rem;
}
.guide-html :deep(table) {
  width: 100%;
  border-collapse: collapse;
  margin: 0.75rem 0;
  font-size: 0.875rem;
}
.guide-html :deep(th),
.guide-html :deep(td) {
  border: 1px solid #e5e7eb;
  padding: 0.375rem 0.5rem;
}
.guide-html :deep(img) {
  max-width: 100%;
  height: auto;
}
.guide-html :deep(video) {
  max-width: 100%;
  height: auto;
  display: block;
  margin: 1rem 0;
}
</style>
