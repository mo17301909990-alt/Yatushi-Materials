<template>
  <div class="min-h-screen bg-gray-50 py-8">
    <div class="max-w-5xl mx-auto px-4 sm:px-6 lg:px-8">
      <div class="mb-6 flex items-start gap-3">
        <AdminBackToPanel />
        <div class="flex-1 min-w-0 flex flex-col sm:flex-row sm:items-start sm:justify-between gap-4">
          <div>
            <h1 class="text-2xl font-bold text-gray-900">系列優先級規則管理（占位頁）</h1>
            <p class="mt-2 text-sm text-gray-600">
              這個頁面目前僅作為路由佔位，避免報錯 <code>SeriesPriorityRuleManagement is not defined</code>。
              實際系列優先級配置已經在後端和其它頁面中使用，你可以稍後再補充完整的前端管理界面。
            </p>
          </div>
          <router-link
            to="/admin/material-rule-management"
            class="inline-flex shrink-0 items-center px-4 py-2 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 bg-white hover:bg-gray-50"
          >
            <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10 19l-7-7m0 0l7-7m-7 7h18" />
            </svg>
            返回物料規則管理
          </router-link>
        </div>
      </div>

      <div class="bg-white rounded-lg shadow p-6">
        <h2 class="text-lg font-medium text-gray-900 mb-4">當前狀態</h2>
        <ul class="list-disc list-inside text-sm text-gray-700 space-y-2">
          <li>後端已存在 <code>SeriesPriorityRuleController / Service / Mapper</code> 等邏輯。</li>
          <li>前端僅缺少此 Vue 組件導致路由導入報錯，這個佔位頁先解決編譯問題。</li>
          <li>未來如果需要可在此頁補充：系列規則列表、編輯表單、與布面紙類型的關聯等 UI。</li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import AdminBackToPanel from '@/components/admin/AdminBackToPanel.vue'
</script>

<style scoped>
</style>

