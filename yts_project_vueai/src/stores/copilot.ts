import { defineStore } from 'pinia';
import { ref } from 'vue';
import { copilotApi, extractSources } from '@/api/modules/copilot';
import type { ChatMessage, AgentRole, ConversationStage } from '@/types/copilot';
import type { GoldFoilProduct } from '@/types/goldFoil';
import { ROLE_DEFAULTS, STAGE_SYSTEM_PROMPTS } from '@/types/copilot';

const GUIDE_PARAM_MAP: Record<string, (val: string) => Record<string, unknown>> = {
  'selecting-product': (val) => {
    if (val.includes('精平裝') || val.includes('咭書') || val === '1') return { productTypeId: 1 }
    if (val.includes('賀咭') || val.includes('紙袋') || val === '2') return { productTypeId: 2 }
    if (val.includes('包裝') || val === '3') return { productTypeId: 3 }
    return {}
  },
  'selecting-pattern': (val) => {
    if (val.includes('淨線條') || val.includes('0.5mm')) return { patternId: 1 }
    if (val.includes('5mm')) return { patternId: 3 }
    if (val.includes('混合')) return { patternId: 5 }
    if (val.includes('實地') || val.includes('10mm')) return { patternId: 4 }
    if (val.includes('20mm')) return { patternId: 9 }
    return {}
  },
  'selecting-stamping': (val) => {
    if (val.includes('平面')) return { hotStampingTypeOptionId: 1 }
    if (val.includes('立體')) return { hotStampingTypeOptionId: 2 }
    if (val.includes('磨砂')) return { hotStampingTypeOptionId: 3 }
    if (val.includes('折光')) return { hotStampingTypeOptionId: 4 }
    if (val.includes('擊凸')) return { hotStampingTypeOptionId: 5 }
    if (val.includes('壓紋')) return { hotStampingTypeOptionId: 6 }
    if (val.includes('有紋')) return { hotStampingTypeOptionId: 7 }
    return {}
  },
}

export const useCopilotStore = defineStore('copilot', () => {
  const messages = ref<ChatMessage[]>([]);
  const processing = ref(false);
  const collapsed = ref(false);
  const error = ref<string | null>(null);

  // B3: Agent 角色
  const agentRole = ref<AgentRole>('general');

  // B2: 工艺选型引导
  const stage = ref<ConversationStage>('idle');
  const guideStepIndex = ref(0);
  const guideFilters = ref<Record<string, unknown>>({});

  // A4: 待内嵌显示的搜索结果
  const pendingInlineResults = ref<{ params: Record<string, unknown> } | null>(null)

  // B1: 单向联动 — AI 推参数到筛选面板
  const filterParams = ref<Record<string, unknown>>({});

  function initWelcome() {
    messages.value = [];
    messages.value.push({
      id: crypto.randomUUID(),
      role: 'assistant',
      content: '你好！我是 Copilot 助手，有什么可以帮助你的？',
      timestamp: Date.now(),
      suggestions: ['查看系統功能概覽', '如何創建產品', '物料兼容性說明'],
    });
  }

  initWelcome();

  function advanceGuideStage(): ConversationStage {
    const stages: ConversationStage[] = [
      'selecting-product', 'selecting-pattern', 'selecting-stamping',
      'selecting-paper', 'selecting-preprocess', 'selecting-postprocess', 'complete'
    ];
    const idx = stages.indexOf(stage.value);
    if (idx >= stages.length - 1) {
      stage.value = 'complete';
      guideStepIndex.value = stages.length;
      return 'complete';
    }
    const next = stages[idx + 1];
    stage.value = next;
    guideStepIndex.value = idx + 1;
    return next;
  }

  function recordGuideSelection(text: string) {
    const builder = GUIDE_PARAM_MAP[stage.value]
    if (builder) {
      const params = builder(text)
      guideFilters.value = { ...guideFilters.value, ...params }
    }
  }

  function setAgentRole(role: AgentRole) {
    agentRole.value = role;
    if (role === 'guide') {
      stage.value = 'selecting-product';
      guideStepIndex.value = 0;
      guideFilters.value = {};
      messages.value.push({
        id: crypto.randomUUID(),
        role: 'assistant',
        content: '好的，開始工藝選型引導！\n\n請先選擇**產品類型**：\n1. 精平裝/咭書\n2. 賀咭/紙袋\n3. 包裝\n\n請回覆數字或名稱。',
        timestamp: Date.now(),
        suggestions: ['精平裝/咭書', '賀咭/紙袋', '包裝'],
      });
    } else {
      stage.value = 'idle';
      guideStepIndex.value = 0;
      guideFilters.value = {};
    }
  }

  function setFilterParams(params: Record<string, unknown>) {
    filterParams.value = { ...params };
  }

  function pushInlineResults(products: GoldFoilProduct[], reply: string) {
    if (products.length === 0) return
    messages.value.push({
      id: crypto.randomUUID(),
      role: 'assistant',
      content: reply || `找到 ${products.length} 個匹配結果：`,
      timestamp: Date.now(),
      products: products.slice(0, 5),
      suggestions: ['查看全部結果', '重新搜索', '咨詢客服'],
    });
  }

  function parseParamsFromReply(reply: string): Record<string, unknown> | null {
    const m = reply.match(/===PARAMS===\s*(\{[\s\S]*?\})\s*===PARAMS===/);
    if (m) {
      try {
        return JSON.parse(m[1]);
      } catch { /* ignore */ }
    }
    return null;
  }

  async function sendMessage(text: string) {
    const trimmed = text.trim();
    if (!trimmed || processing.value) return;

    error.value = null;

    const userMsg: ChatMessage = {
      id: crypto.randomUUID(),
      role: 'user',
      content: trimmed,
      timestamp: Date.now(),
    };
    messages.value.push(userMsg);

    // Guide 角色 - 按键推进
    if (agentRole.value === 'guide' && stage.value !== 'idle' && stage.value !== 'complete') {
      processing.value = true;
      try {
        recordGuideSelection(trimmed);

        const systemPrompt = STAGE_SYSTEM_PROMPTS[stage.value];
        const response = await copilotApi.chat(trimmed, systemPrompt) as any;
        const reply = typeof response.reply === 'string' ? response.reply : '已記錄你的選擇。';

        // 提取来源标记
        const { sources, cleanReply } = extractSources(reply);

        messages.value.push({
          id: crypto.randomUUID(),
          role: 'assistant',
          content: cleanReply || reply,
          timestamp: Date.now(),
          suggestions: [],
          sources: sources.length > 0 ? sources : undefined,
        });

        // 自动推进到下一步
        const nextStage = advanceGuideStage();
        if (nextStage === 'complete') {
          if (Object.keys(guideFilters.value).length > 0) {
            setFilterParams(guideFilters.value);
          }
          messages.value.push({
            id: crypto.randomUUID(),
            role: 'assistant',
            content: '🎉 工藝選型已完成！正在根據你的選擇查找匹配物料...',
            timestamp: Date.now(),
            suggestions: ['查看匹配結果', '重新開始', '咨詢客服'],
          });
        }
      } catch (err: unknown) {
        const msg = (err as { message?: string })?.message || '請求失敗';
        error.value = msg;
        messages.value.push({
          id: crypto.randomUUID(),
          role: 'assistant',
          content: `抱歉，出錯了：${msg}`,
          timestamp: Date.now(),
        });
      } finally {
        processing.value = false;
      }
      return;
    }

    processing.value = true;
    try {
      const systemPrompt = agentRole.value !== 'general' ? ROLE_DEFAULTS[agentRole.value] : undefined;
      const response = await copilotApi.chat(trimmed, systemPrompt) as any;
      const reply = typeof response.reply === 'string' ? response.reply : '';
      const suggestions = Array.isArray(response.suggestions) ? response.suggestions : [];
      const cardType = (response as any)?.type || undefined;
      const cardData = (response as any)?.data || undefined;

      // 从回复中提取来源标记
      const { sources, cleanReply } = extractSources(reply);

      // B1: matching 角色 — 尝试解析参数
      if (agentRole.value === 'matching') {
        const p = response.params && Object.keys(response.params).length > 0
          ? response.params
          : parseParamsFromReply(reply);
        if (p) {
          setFilterParams(p);
          pendingInlineResults.value = { params: p };
        }
      }

      // 创建 assistant 消息时附加来源和卡片数据
      const msg: ChatMessage = {
        id: crypto.randomUUID(),
        role: 'assistant',
        content: cleanReply || reply,
        timestamp: Date.now(),
        suggestions: suggestions,
        sources: sources.length > 0 ? sources : undefined,
        cardType: cardType,
        cardData: cardData,
      };

      // 流式效果：先加一条空消息模拟流式
      if (msg.content.length > 100) {
        msg.isStreaming = true;
        messages.value.push(msg);
        await new Promise(r => setTimeout(r, 200));
        msg.isStreaming = false;
      } else {
        messages.value.push(msg);
      }
    } catch (err: unknown) {
      const message = (err as { message?: string })?.message || '請求失敗，請稍後重試';
      error.value = message;
      messages.value.push({
        id: crypto.randomUUID(),
        role: 'assistant',
        content: `抱歉，出錯了：${message}`,
        timestamp: Date.now(),
      });
    } finally {
      processing.value = false;
    }
  }

  function clearMessages() {
    messages.value = [];
    initWelcome();
    stage.value = 'idle';
    guideStepIndex.value = 0;
    guideFilters.value = {};
  }

  function toggleCollapse() {
    collapsed.value = !collapsed.value;
  }

  return {
    messages,
    processing,
    collapsed,
    error,
    agentRole,
    stage,
    guideStepIndex,
    filterParams,
    guideFilters,
    pendingInlineResults,
    setAgentRole,
    setFilterParams,
    pushInlineResults,
    sendMessage,
    clearMessages,
    toggleCollapse,
  };
});
