import { defineStore } from 'pinia';
import { announcementApi, type SysAnnouncement, type UserAnnouncementListParams } from '@/api/modules/announcement';
import { useAuthStore } from './auth';

export const useAnnouncementStore = defineStore('announcement', {
  state: () => ({
    unreadCount: 0,
    messages: [] as SysAnnouncement[],
    currentMessage: null as SysAnnouncement | null,
    wsConnected: false
  }),

  actions: {
    // 获取未读消息数量
    async fetchUnreadCount() {
      try {
        const authStore = useAuthStore();
        if (!authStore.userInfo?.id) {
          return;
        }
        const res = await announcementApi.getUnreadCount(authStore.userInfo.id);
        if (res.success && res.result) {
          this.unreadCount = res.result.unreadCount;
        }
      } catch (error) {
        console.error('获取未读消息数量失败:', error);
      }
    },

    // 获取消息列表（用户）
    async fetchMessages(params: UserAnnouncementListParams) {
      try {
        const res = await announcementApi.listByUser(params);
        if (res.success && res.result) {
          this.messages = res.result.records;
          return res.result;
        }
        return null;
      } catch (error) {
        console.error('获取消息列表失败:', error);
        return null;
      }
    },

    // 标记已读
    async markAsRead(id: string) {
      try {
        const authStore = useAuthStore();
        if (!authStore.userInfo?.id) {
          return;
        }
        await announcementApi.read(id, authStore.userInfo.id);
        // 更新本地状态
        const message = this.messages.find(m => m.id === id);
        if (message) {
          message.readFlag = 1;
          message.readTime = new Date().toISOString();
        }
        this.unreadCount = Math.max(0, this.unreadCount - 1);
      } catch (error) {
        console.error('标记已读失败:', error);
        throw error;
      }
    },

    // 批量标记已读
    async batchMarkAsRead(ids: string[]) {
      try {
        const authStore = useAuthStore();
        if (!authStore.userInfo?.id) {
          return;
        }
        await announcementApi.batchRead(ids, authStore.userInfo.id);
        // 更新本地状态
        ids.forEach(id => {
          const message = this.messages.find(m => m.id === id);
          if (message) {
            message.readFlag = 1;
            message.readTime = new Date().toISOString();
          }
        });
        this.unreadCount = Math.max(0, this.unreadCount - ids.length);
      } catch (error) {
        console.error('批量标记已读失败:', error);
        throw error;
      }
    },

    // 删除消息
    async deleteMessage(id: string) {
      try {
        const authStore = useAuthStore();
        if (!authStore.userInfo?.id) {
          return;
        }
        await announcementApi.deleteByUser(id, authStore.userInfo.id);
        // 从列表中移除
        this.messages = this.messages.filter(m => m.id !== id);
        // 如果删除的是未读消息，更新未读数量
        const message = this.messages.find(m => m.id === id);
        if (message && message.readFlag === 0) {
          this.unreadCount = Math.max(0, this.unreadCount - 1);
        }
      } catch (error) {
        console.error('删除消息失败:', error);
        throw error;
      }
    },

    // 跳转到业务详情（根据 busType 和 busId）
    navigateToBusiness(busType: string, busId: string) {
      const routeMap: Record<string, string> = {
        'ORDER': `/order/detail/${busId}`,
        'TASK': `/task/detail/${busId}`,
        'APPROVAL': `/approval/detail/${busId}`,
        'SYSTEM': `/system/detail/${busId}`,
        // 可以根据实际业务添加更多映射
      };
      const route = routeMap[busType];
      if (route) {
        // 使用 router 跳转（需要在组件中调用）
        return route;
      }
      return null;
    },

    // WebSocket 连接（可选，后续实现）
    connectWebSocket(userId: number) {
      // TODO: 实现 WebSocket 连接
      // const ws = new WebSocket(`ws://localhost:8092/websocket/${userId}`);
      // ws.onmessage = (event) => {
      //   const message = JSON.parse(event.data);
      //   this.handleWebSocketMessage(message);
      // };
      // this.wsConnected = true;
    },

    // 处理 WebSocket 消息
    handleWebSocketMessage(message: any) {
      if (message.type === 'NEW_MESSAGE') {
        // 更新未读数量
        this.fetchUnreadCount();
        // 可以显示通知
        // 如果包含 busType/busId，提供"去处理"按钮
      }
    }
  }
});

