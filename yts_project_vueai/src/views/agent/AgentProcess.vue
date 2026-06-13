<script setup lang="ts">
import { ref, computed, onMounted, watch } from 'vue'
import type { AgentConfig, MaterialStandard } from '@/types/agent'
import { agentApi } from '@/api/modules/agent'

const activeTab = ref<'skills' | 'knowledge' | 'matching'>('knowledge')
const selectedCategory = ref<string | null>(null)
const loading = ref(true)
const agent = ref<AgentConfig | null>(null)
const categoryStandards = ref<MaterialStandard[]>([])

const currentStandards = computed(() => categoryStandards.value)

const currentCategoryName = computed(() => {
  if (!selectedCategory.value || !agent.value) return ''
  return agent.value.materialCategories.find(c => c.id === selectedCategory.value)?.name ?? ''
})

function selectCategory(id: string) {
  selectedCategory.value = selectedCategory.value === id ? null : id
}

async function loadAgent() {
  loading.value = true
  try {
    agent.value = await agentApi.getConfig()
  } catch (e) {
    console.error('加载 Agent 配置失败', e)
  } finally {
    loading.value = false
  }
}

async function loadStandards(categoryId: string) {
  try {
    categoryStandards.value = await agentApi.getStandardsByCategory(categoryId)
  } catch (e) {
    console.error('加载标准书列表失败', e)
    categoryStandards.value = []
  }
}

watch(selectedCategory, (val) => {
  if (val) {
    loadStandards(val)
  } else {
    categoryStandards.value = []
  }
})

onMounted(loadAgent)
</script>

<template>
  <div class="min-h-screen bg-gray-50">
    <div class="max-w-6xl mx-auto px-4 sm:px-6 lg:px-8 py-6">
      <!-- 加载中 -->
      <div v-if="loading" class="flex items-center justify-center py-20">
        <div class="flex items-center gap-2 text-gray-400">
          <span class="w-2 h-2 rounded-full bg-indigo-400 animate-bounce" style="animation-delay:0ms"></span>
          <span class="w-2 h-2 rounded-full bg-indigo-400 animate-bounce" style="animation-delay:150ms"></span>
          <span class="w-2 h-2 rounded-full bg-indigo-400 animate-bounce" style="animation-delay:300ms"></span>
        </div>
      </div>

      <template v-if="agent">
      <!-- Agent 头部卡片 -->
      <div class="bg-white rounded-xl shadow-sm border border-gray-200 p-6 mb-6">
        <div class="flex items-start gap-5">
          <!-- Agent 头像 -->
          <div class="w-16 h-16 rounded-xl bg-gradient-to-br from-indigo-500 to-purple-600 flex items-center justify-center text-white text-2xl font-bold shrink-0">
            A
          </div>
          <div class="flex-1 min-w-0">
            <div class="flex items-center gap-3 mb-1">
              <h1 class="text-xl font-semibold text-gray-900">{{ agent.name }}</h1>
              <span class="inline-flex items-center gap-1 px-2 py-0.5 rounded-full text-xs font-medium bg-green-50 text-green-700 border border-green-200">
                <span class="w-1.5 h-1.5 rounded-full bg-green-500"></span>
                {{ agent.status === 'online' ? '在线' : agent.status === 'busy' ? '忙碌' : '离线' }}
              </span>
            </div>
            <p class="text-sm text-gray-500 mb-2">{{ agent.role }} · {{ agent.department }}</p>
            <p class="text-sm text-gray-600">{{ agent.description }}</p>
          </div>
        </div>
      </div>

      <!-- Tab 导航 -->
      <div class="flex gap-1 mb-6 border-b border-gray-200">
        <button
          v-for="tab in [{ key: 'skills', label: '技能', icon: '⚡' }, { key: 'knowledge', label: '资料库', icon: '📚' }, { key: 'matching', label: '工艺匹配', icon: '🎯' }]"
          :key="tab.key"
          class="flex items-center gap-1.5 px-4 py-3 text-sm font-medium transition-colors border-b-2 -mb-px"
          :class="activeTab === tab.key ? 'border-indigo-600 text-indigo-600' : 'border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300'"
          @click="activeTab = tab.key as typeof activeTab"
        >
          {{ tab.icon }} {{ tab.label }}
        </button>
      </div>

      <!-- 技能 Tab -->
      <div v-if="activeTab === 'skills'" class="grid grid-cols-1 md:grid-cols-2 gap-4">
        <div
          v-for="skill in agent.skills"
          :key="skill.id"
          class="bg-white rounded-lg border border-gray-200 p-4 hover:shadow-sm transition-shadow"
        >
          <div class="flex items-start gap-3">
            <div class="w-8 h-8 rounded-lg bg-indigo-100 flex items-center justify-center text-indigo-600 text-sm shrink-0">⚙</div>
            <div>
              <h3 class="text-sm font-medium text-gray-900">{{ skill.name }}</h3>
              <p class="text-xs text-gray-500 mt-1">{{ skill.description }}</p>
              <div v-if="skill.triggerEvents?.length" class="flex flex-wrap gap-1 mt-2">
                <span
                  v-for="evt in skill.triggerEvents"
                  :key="evt"
                  class="inline-flex items-center px-1.5 py-0.5 text-[10px] rounded bg-gray-100 text-gray-500"
                >{{ evt }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 资料库 Tab -->
      <div v-if="activeTab === 'knowledge'">
        <!-- 分类卡片 -->
        <div v-if="!selectedCategory" class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
          <button
            v-for="cat in agent.materialCategories"
            :key="cat.id"
            class="bg-white rounded-xl border border-gray-200 p-5 text-left hover:shadow-md hover:border-indigo-200 transition-all group"
            @click="selectCategory(cat.id)"
          >
            <div class="text-3xl mb-3">{{ cat.icon }}</div>
            <h3 class="text-sm font-semibold text-gray-900 group-hover:text-indigo-600 transition-colors">{{ cat.name }}</h3>
            <p class="text-xs text-gray-400 mt-1">{{ cat.count }} 份标准书</p>
          </button>
        </div>

        <!-- 分类内标准列表 -->
        <div v-else>
          <button
            class="flex items-center gap-1 text-sm text-gray-500 hover:text-gray-700 mb-4"
            @click="selectedCategory = null"
          >
            ← 返回分类
          </button>
          <div class="bg-white rounded-xl border border-gray-200 divide-y divide-gray-100">
            <div class="px-4 py-3 text-sm font-medium text-gray-900 bg-gray-50 rounded-t-xl">
              {{ currentCategoryName }}（{{ currentStandards.length }} 份）
            </div>
            <div
              v-for="std in currentStandards"
              :key="std.id"
              class="px-4 py-3 flex items-center gap-3 hover:bg-gray-50 transition-colors"
            >
              <span class="text-gray-400 shrink-0">📄</span>
              <div class="flex-1 min-w-0">
                <p class="text-sm text-gray-900 truncate">{{ std.title }}</p>
                <p class="text-xs text-gray-400 mt-0.5">{{ std.fileName }}</p>
              </div>
              <div class="flex items-center gap-2 shrink-0">
                <div class="flex flex-wrap gap-1">
                  <span
                    v-for="tag in std.tags"
                    :key="tag"
                    class="inline-flex items-center px-1.5 py-0.5 text-[10px] rounded bg-gray-100 text-gray-500"
                  >{{ tag }}</span>
                </div>
                <span class="text-xs text-gray-400">{{ std.updatedAt }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 工艺匹配 Tab -->
      <div v-if="activeTab === 'matching'" class="bg-white rounded-xl border border-gray-200 p-8 text-center">
        <div class="text-4xl mb-4">🔧</div>
        <h3 class="text-base font-medium text-gray-900 mb-2">工艺匹配查询</h3>
        <p class="text-sm text-gray-500 mb-6">通过 AI 对话助手进行工艺材料匹配，或使用筛选面板查询兼容性。</p>
        <div class="flex justify-center gap-3">
          <router-link
            to="/smart-version"
            class="inline-flex items-center gap-1.5 px-4 py-2 rounded-lg bg-indigo-600 text-white text-sm font-medium hover:bg-indigo-700 transition-colors"
          >
            ✨ 前往 AI 智能版
          </router-link>
          <router-link
            to="/process-config"
            class="inline-flex items-center gap-1.5 px-4 py-2 rounded-lg border border-gray-200 text-gray-700 text-sm font-medium hover:bg-gray-50 transition-colors"
          >
            前往工艺匹配
          </router-link>
        </div>
      </div>
      </template> <!-- end agent -->
    </div>
  </div>
</template>
