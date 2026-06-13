<script setup lang="ts">
defineProps<{
  headers?: string[]
  rows?: string[][]
}>()
</script>

<template>
  <div v-if="headers && headers.length > 0 && rows && rows.length > 0" class="rounded-xl border border-gray-200 bg-white shadow-sm overflow-hidden">
    <div class="overflow-x-auto">
      <table class="w-full text-xs">
        <thead>
          <tr class="bg-indigo-50">
            <th
              v-for="(h, i) in headers"
              :key="i"
              class="px-2.5 py-1.5 text-left text-indigo-700 font-semibold whitespace-nowrap"
            >{{ h }}</th>
          </tr>
        </thead>
        <tbody>
          <tr
            v-for="(row, ri) in rows"
            :key="ri"
            class="border-t border-gray-100"
            :class="ri % 2 === 0 ? 'bg-white' : 'bg-gray-50'"
          >
            <td
              v-for="(cell, ci) in row"
              :key="ci"
              class="px-2.5 py-1.5 text-gray-700 whitespace-nowrap"
              :class="ci === 0 ? 'font-medium text-gray-800' : ''"
            >{{ cell }}</td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
  <div v-else class="rounded-xl border border-gray-200 bg-white px-3 py-3 text-center text-xs text-gray-400">
    暂无兼容数据
  </div>
</template>
