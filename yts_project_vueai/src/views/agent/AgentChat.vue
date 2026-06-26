<script setup lang="ts">
import { onMounted, watch } from 'vue'
import { useAgentChatStore } from '@/stores/agentChat'
import { usePermissionStore } from '@/stores/permission'
import ChatSessionList from '@/components/agent/ChatSessionList.vue'
import ChatArea from '@/components/agent/ChatArea.vue'

const store = useAgentChatStore()
const permissionStore = usePermissionStore()

onMounted(async () => {
  // 等待 RBAC 权限加载完成，避免竞态导致 isAdmin 误判
  if (!permissionStore.rbacReady) {
    await new Promise<void>(resolve => {
      const stop = watch(() => permissionStore.rbacReady, (val) => {
        if (val) { stop(); resolve() }
      })
    })
  }
  store.init({ isAdmin: permissionStore.isAdmin })
})
</script>

<template>
  <div class="flex h-[calc(100vh-4rem)] w-full overflow-hidden bg-gray-50">
    <ChatSessionList />
    <ChatArea />
  </div>
</template>
