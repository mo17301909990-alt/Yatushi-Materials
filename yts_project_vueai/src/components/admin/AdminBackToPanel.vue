<script setup lang="ts">
/**
 * 返回後台管理首頁 (/admin) 的圓形左箭頭，各後台二級頁統一使用。
 */
</script>
<template>
  <router-link
    to="/admin"
    class="mt-1 inline-flex shrink-0 items-center justify-center rounded-full p-2 text-gray-600 hover:bg-gray-200 hover:text-gray-900 transition-colors"
    title="返回後台管理"
    aria-label="返回後台管理"
  >
    <svg class="h-6 w-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10 19l-7-7m0 0l7-7m-7 7h18" />
    </svg>
  </router-link>
</template>
