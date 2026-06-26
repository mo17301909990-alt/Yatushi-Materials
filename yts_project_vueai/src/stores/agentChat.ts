import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { copilotApi, extractSources } from '@/api/modules/copilot'
import { adminChangeApi } from '@/api/modules/adminChange'
import { goldFoilApi } from '@/api/modules/goldFoil'
import type { ChatMessage } from '@/types/copilot'
import { ROLE_DEFAULTS } from '@/types/copilot'
import type { GoldFoilProduct } from '@/types/goldFoil'

export type AgentType = 'general' | 'matching' | 'guide' | 'admin'

export interface AgentConversation {
  id: string
  name: string
  agentType: AgentType
  avatar: string
  messages: ChatMessage[]
  createdAt: number
  updatedAt: number
  processing: boolean
}

// 匹配专家专用聊天角色提示（不含 ===PARAMS=== 指令，参数提取由 parse-and-match 端点处理）
const MATCHING_CHAT_PROMPT = '你是燙金紙匹配專家。根據用戶描述的工藝需求，引導用戶補充關鍵條件（如產品類型、燙金圖案、燙金類型等），然後推薦合適的燙金紙。回答時可提問以收窄範圍，也可介紹燙金紙分類和選型知識。回答簡潔實用。'

// 生产助手 prompt：资源组匹配与设备推荐
const PRODUCTION_CHAT_PROMPT = '你是印刷厂生产助手，负责资源组匹配和工艺路线推荐。你拥有完整的资源组数据（工序大类、资源组、设备规格、选择规则、产能数据）。\n\n'
    + '【职责】\n'
    + '1. 根据用户描述的印刷工艺需求（如纸度、色数、工艺、后加工等），推荐合适的资源组和设备\n'
    + '2. 解释同一工序不同资源组的区别（机型、纸度范围、色数、特殊工艺）\n'
    + '3. 回答关于设备能力、产能、机台分配的问题\n'
    + '4. 当同一工序有多个资源组可选时，对比它们的适用条件\n\n'
    + '【回答要求】\n'
    + '- 基于【资源组与产能数据】中的真实数据回答，不要编造型号或规格\n'
    + '- 纸度、色数、工艺条件以各资源组的规则为准\n'
    + '- 如果用户问的设备/工艺在数据中不存在，明确说明未收录\n'
    + '- 涉及机台编号（#号机）时引用数据'
    + '- 回答简洁实用，分点列出关键信息'

const STORAGE_KEY = 'agent-chat-conversations'
const MAX_STORED_MSGS = 50

const DEFAULT_CONVERSATIONS: Omit<AgentConversation, 'id' | 'messages' | 'createdAt' | 'updatedAt' | 'processing'>[] = [
  { name: '工艺 Agent', agentType: 'general', avatar: '🤖' },
  { name: '匹配专家', agentType: 'matching', avatar: '🎯' },
  { name: '生产助手', agentType: 'guide', avatar: '🏭' },
]

function loadFromStorage(): AgentConversation[] | null {
  try {
    const raw = localStorage.getItem(STORAGE_KEY)
    if (!raw) return null
    const data = JSON.parse(raw)
    if (!Array.isArray(data) || data.length === 0) return null
    return data
  } catch {
    return null
  }
}

function saveToStorage(conversations: AgentConversation[]) {
  try {
    const saved = conversations.map(c => ({
      ...c,
      messages: c.messages.slice(-MAX_STORED_MSGS),
    }))
    localStorage.setItem(STORAGE_KEY, JSON.stringify(saved))
  } catch {
    // localStorage full or unavailable — silently fail
  }
}

export const useAgentChatStore = defineStore('agentChat', () => {
  const conversations = ref<AgentConversation[]>([])
  const activeId = ref<string | null>(null)
  const adminAvailable = ref(false) // set from outside (permissionStore.isAdmin)

  /** 匹配专家：跨轮次累加的筛选参数，每次 AI 返回新参数时合并进去 */
  const matchingAccumulatedParams = ref<Record<string, unknown>>({})

  const activeConversation = computed(() =>
    conversations.value.find(c => c.id === activeId.value) ?? null
  )

  const isAdminAgent = computed(() =>
    activeConversation.value?.agentType === 'admin'
  )

  function save() {
    saveToStorage(conversations.value)
  }

  function init(options?: { isAdmin?: boolean }) {
    adminAvailable.value = options?.isAdmin ?? false

    const stored = loadFromStorage()
    if (stored) {
      // Filter out admin conversations if user is no longer admin
      const filtered = adminAvailable.value
        ? stored
        : stored.filter(c => c.agentType !== 'admin')
      conversations.value = filtered
      if (filtered.length > 0 && !filtered.find(c => c.id === activeId.value)) {
        activeId.value = filtered[0].id
      }
      if (filtered.length > 0) {
        save()
        return
      }
    }

    // Create default conversations
    const now = Date.now()
    const defaults = DEFAULT_CONVERSATIONS.map((d, i) => ({
      ...d,
      id: crypto.randomUUID(),
      messages: [welcomeMessage(d.agentType)],
      createdAt: now + i,
      updatedAt: now + i,
      processing: false,
    }))

    // Add admin conversation if user is admin
    if (adminAvailable.value) {
      defaults.push({
        name: '管理 Agent',
        agentType: 'admin',
        avatar: '⚙️',
        id: crypto.randomUUID(),
        messages: [welcomeMessage('admin')],
        createdAt: now + defaults.length,
        updatedAt: now + defaults.length,
        processing: false,
      })
    }

    conversations.value = defaults
    activeId.value = defaults[0]?.id ?? null
    save()
  }

  function welcomeMessage(agentType: AgentType): ChatMessage {
    const contents: Record<AgentType, string> = {
      general: '你好！我是**工艺 Agent**，负责印刷工艺材料知识管理，涵盖烫金、丝印、过胶、UV 印刷、LED UV 等工艺。有什么可以帮助你的？',
      matching: '你好！我是**匹配专家**，可以根据你的需求帮你找到合适的物料。描述你的工艺需求，我来为你匹配合适的烫金纸。',
      guide: '你好！我是**生产助手**，可以引导你完成工艺选型流程。从产品类型开始，一步步为你推荐最佳方案。',
      admin: '你好！我是**管理 Agent**，可以帮你修改后台数据。\n\n**支持的操作：**\n- 调整价格：`调整 GP-1001 价格为 15.5`\n- 修改规格：`修改规格 42 的颜色为红色`\n- 预览变更：`预览 GP-1001 的新价格 16.0`\n\n操作前会先预览变更内容，确认后才会执行。所有变更可追溯、可回滚。',
    }
    const suggestions: Record<AgentType, string[]> = {
      general: ['烫金工艺说明', '过胶兼容性', 'UV 印刷要求'],
      matching: ['帮我匹配烫金纸', 'LED UV 适用型号', '高耐磨金纸推荐'],
      guide: ['开始工艺引导', '产品类型说明', '查看流程图'],
      admin: ['预览调价', '查看变更记录', '修改规格'],
    }
    return {
      id: crypto.randomUUID(),
      role: 'assistant',
      content: contents[agentType],
      timestamp: Date.now(),
      suggestions: suggestions[agentType],
    }
  }

  function createConversation(name: string, agentType: AgentType = 'general') {
    const now = Date.now()
    const avatarMap: Record<AgentType, string> = {
      general: '🤖', matching: '🎯', guide: '🏭', admin: '⚙️',
    }
    const conv: AgentConversation = {
      id: crypto.randomUUID(),
      name,
      agentType,
      avatar: avatarMap[agentType],
      messages: [welcomeMessage(agentType)],
      createdAt: now,
      updatedAt: now,
      processing: false,
    }
    conversations.value.unshift(conv)
    activeId.value = conv.id
    save()
  }

  function deleteConversation(id: string) {
    const idx = conversations.value.findIndex(c => c.id === id)
    if (idx === -1) return
    conversations.value.splice(idx, 1)
    if (activeId.value === id) {
      activeId.value = conversations.value[0]?.id ?? null
    }
    save()
  }

  function switchConversation(id: string) {
    activeId.value = id
  }

  function clearActiveMessages() {
    const conv = activeConversation.value
    if (!conv) return
    const welcome = welcomeMessage(conv.agentType)
    conv.messages = [welcome]
    conv.updatedAt = Date.now()
    if (conv.agentType === 'matching') {
      matchingAccumulatedParams.value = {}
    }
    save()
  }

  async function sendMessage(text: string) {
    const conv = activeConversation.value
    if (!conv || !text.trim() || conv.processing) return

    const trimmed = text.trim()
    const userMsg: ChatMessage = {
      id: crypto.randomUUID(),
      role: 'user',
      content: trimmed,
      timestamp: Date.now(),
    }
    conv.messages.push(userMsg)
    conv.updatedAt = Date.now()

    conv.processing = true
    try {
      if (conv.agentType === 'admin') {
        await handleAdminMessage(conv, trimmed)
      } else {
        await handleCopilotMessage(conv, trimmed)
      }
    } catch (err: unknown) {
      const message = (err as { message?: string })?.message || '请求失败，请稍后重试'
      conv.messages.push({
        id: crypto.randomUUID(),
        role: 'assistant',
        content: `抱歉，出错了：${message}`,
        timestamp: Date.now(),
      })
    } finally {
      conv.processing = false
      conv.updatedAt = Date.now()
      save()
    }
  }

  /** 管理 Agent 消息处理：解析自然语言 → 预览/执行/回滚 */
  async function handleAdminMessage(conv: AgentConversation, text: string) {
    // Simple pattern matching for admin commands
    const previewMatch = text.match(/预览[：:]\s*(.+)/)
    const rollbackMatch = text.match(/回滚[：:]\s*(\d+)/)
    const changeQueryMatch = text.match(/变更记录|查看变更|变更历史|changes/)
    const isPreview = text.includes('预览') && !previewMatch
    const priceMatch = text.match(/(?:GP[#\-]?\s*)?(\d+)\s*(?:价格|单价|价钱|价)[：:为是]?\s*(\d+\.?\d*)/i)
    const specMatch = text.match(/规格\s*(\d+)\s*(.*?)[：:为是]?\s*(.+)/)
    const entityQuery = text.match(/查[询看]?\s*(?:价格|定价|规格|产品)/)

    if (rollbackMatch) {
      const recordId = parseInt(rollbackMatch[1])
      const result = await adminChangeApi.rollback(recordId)
      conv.messages.push({
        id: crypto.randomUUID(),
        role: 'assistant',
        content: `✅ 回滚成功！\n\n记录 #${result.recordId} 已回滚，状态：${result.status}\n${result.message}`,
        timestamp: Date.now(),
      })
      return
    }

    if (changeQueryMatch) {
      const result = await adminChangeApi.queryChanges({ pageNo: 1, pageSize: 10 })
      const lines = result.list.map(r =>
        `- #${r.id} [${r.status}] ${r.entityType}/${r.entityId} — ${r.reason || '无说明'} (${new Date(r.createdAt).toLocaleString()})`
      ).join('\n')
      conv.messages.push({
        id: crypto.randomUUID(),
        role: 'assistant',
        content: `**近期变更记录：**\n\n${lines || '暂无变更记录'}\n\n共 ${result.total} 条`,
        timestamp: Date.now(),
      })
      return
    }

    // Price modification: "GP-1001 价格调整为 15.5"
    if (priceMatch) {
      const entityId = parseInt(priceMatch[1])
      const newPrice = parseFloat(priceMatch[2])

      // First preview
      const preview = await adminChangeApi.preview({
        entityType: 'pricing', entityId, changes: { price: newPrice },
      })

      if (isPreview || text.includes('预览')) {
        conv.messages.push({
          id: crypto.randomUUID(),
          role: 'assistant',
          content: `预览定价 #${preview.entityId}，请确认变更：`,
          timestamp: Date.now(),
          cardType: 'admin_preview',
          cardData: {
            entityType: preview.entityType,
            entityId: preview.entityId,
            currentValues: preview.currentValues,
            proposedChanges: preview.proposedChanges,
            risk: preview.risk,
          },
        })
        return
      }

      // Execute (with confirmed=false — server will reject if high risk, UI handles dialog)
      const result = await adminChangeApi.execute({
        entityType: 'pricing', entityId, changes: { price: newPrice },
        risk: preview.risk, reason: text, confirmed: false,
      })
      conv.messages.push({
        id: crypto.randomUUID(),
        role: 'assistant',
        content: `变更已执行 (#${result.recordId})`,
        timestamp: Date.now(),
        cardType: 'admin_result',
        cardData: {
          recordId: result.recordId,
          entityType: result.entityType,
          entityId: result.entityId,
          status: result.status,
        },
      })
      return
    }

    // Specification modification
    if (specMatch) {
      const entityId = parseInt(specMatch[1])
      const field = specMatch[2].trim()
      const value = specMatch[3].trim()

      if (!field) {
        conv.messages.push({
          id: crypto.randomUUID(),
          role: 'assistant',
          content: '请指定要修改的字段，例如：`规格 42 的颜色为红色`',
          timestamp: Date.now(),
        })
        return
      }

      const changes: Record<string, unknown> = {}
      const fieldMap: Record<string, string> = { 颜色: 'color', 尺寸: 'size', 紧密度: 'tightness', 温度: 'temperatureRange', 性能: 'performance' }
      const mappedField = fieldMap[field] || field
      changes[mappedField] = value

      const preview = await adminChangeApi.preview({
        entityType: 'specification', entityId, changes,
      })

      if (isPreview || text.includes('预览')) {
        conv.messages.push({
          id: crypto.randomUUID(),
          role: 'assistant',
          content: `预览规格 #${preview.entityId}，请确认变更：`,
          timestamp: Date.now(),
          cardType: 'admin_preview',
          cardData: {
            entityType: preview.entityType,
            entityId: preview.entityId,
            currentValues: preview.currentValues,
            proposedChanges: preview.proposedChanges,
            risk: preview.risk,
          },
        })
        return
      }

      const result = await adminChangeApi.execute({
        entityType: 'specification', entityId, changes,
        risk: preview.risk, reason: text, confirmed: false,
      })
      conv.messages.push({
        id: crypto.randomUUID(),
        role: 'assistant',
        content: `变更已执行 (#${result.recordId})`,
        timestamp: Date.now(),
        cardType: 'admin_result',
        cardData: {
          recordId: result.recordId,
          entityType: result.entityType,
          entityId: result.entityId,
          status: result.status,
        },
      })
      return
    }

    // Generic entity query → fall back to agent query endpoint
    if (entityQuery) {
      conv.messages.push({
        id: crypto.randomUUID(),
        role: 'assistant',
        content: '请指定具体的实体类型和 ID，例如：\n- `查询价格 42`\n- `查看定价 1001`\n- `查规格 5`',
        timestamp: Date.now(),
      })
      return
    }

    // Fallback: generic message
    conv.messages.push({
      id: crypto.randomUUID(),
      role: 'assistant',
      content: '**管理 Agent 支持的操作：**\n\n' +
        '1️⃣ **调价** — `GP-1001 价格调整为 15.5`\n' +
        '2️⃣ **改规格** — `规格 42 的颜色改为红色`\n' +
        '3️⃣ **预览** — `预览 GP-1001 的新价格 16.0`\n' +
        '4️⃣ **回滚** — `回滚：42`\n' +
        '5️⃣ **查看记录** — `变更记录`\n\n' +
        '所有操作均先预览 → 确认 → 执行，支持自动回滚。',
      timestamp: Date.now(),
    })
  }

  /** 构建当前累积筛选参数的文本描述，注入 AI 上下文 */
  function buildFilterContext(params: Record<string, unknown>): string {
    const keys = Object.keys(params)
    if (keys.length === 0) return ''
    const pairs = keys.map(k => `${k}=${JSON.stringify(params[k])}`).join(', ')
    return `【当前已累积的筛选参数】${pairs}\n（请在此条件基础上，结合用户新消息输出包含所有已有条件和新增条件的完整 JSON。）\n`
  }

  /** 通用 Copilot 消息处理 */
  async function handleCopilotMessage(conv: AgentConversation, text: string) {
    const isMatching = conv.agentType === 'matching'

    // 匹配专家：把已累积的筛选参数作为上下文传给 AI，让它知道当前筛选状态
    const filterContext = isMatching ? buildFilterContext(matchingAccumulatedParams.value) : ''
    const messageForAi = filterContext ? `${filterContext}\n用户消息: ${text}` : text
    // 根据 agent 类型传递不同角色提示，确保各 agent 回复差异化
    const rolePrompt: Record<string, string> = {
      general: ROLE_DEFAULTS.general,
      matching: MATCHING_CHAT_PROMPT,
      guide: PRODUCTION_CHAT_PROMPT,
    }
    const response = await copilotApi.chat(messageForAi, rolePrompt[conv.agentType] ?? ROLE_DEFAULTS.general) as any

    const reply = typeof response.reply === 'string' ? response.reply : ''
    const suggestions = Array.isArray(response.suggestions) ? response.suggestions : []
    const cardType = (response as any)?.type || undefined
    const cardData = (response as any)?.data || undefined

    const { sources, cleanReply } = extractSources(reply)

    const msg: ChatMessage = {
      id: crypto.randomUUID(),
      role: 'assistant',
      content: cleanReply || reply,
      timestamp: Date.now(),
      suggestions,
      sources: sources.length > 0 ? sources : undefined,
      cardType,
      cardData,
    }

    // matching 专家：使用 parseAndMatch 端点从自然语言提取参数并执行匹配
    if (isMatching) {
      try {
        // 把累积参数作为上下文传给 parse-and-match，让它知道已选条件
        const parseMessage = filterContext
          ? `${filterContext}\n用户消息: ${text}`
          : text
        const parseResult = await goldFoilApi.parseAndMatch(parseMessage) as any

        if (parseResult?.products?.items) {
          const products: GoldFoilProduct[] = parseResult.products.items
          const total = parseResult.products.total

          msg.products = products.slice(0, 5)
          msg.filteredTotal = total

          // 更新累积参数，供下一轮做上下文
          if (parseResult.params && Object.keys(parseResult.params).length > 0) {
            matchingAccumulatedParams.value = { ...parseResult.params }
          }
        }
      } catch (e) {
        console.warn('parseAndMatch 失败，不影响文字回复', e)
      }
    }

    if (msg.content.length > 100) {
      msg.isStreaming = true
      conv.messages.push(msg)
      save()
      await new Promise(r => setTimeout(r, 200))
      msg.isStreaming = false
    } else {
      conv.messages.push(msg)
    }
  }

  /** 执行管理员变更（由 AdminChangePreview 卡片确认按钮触发） */
  async function executeAdminChange(messageId: string, confirmed: boolean, reason?: string) {
    const conv = activeConversation.value
    if (!conv) return

    const msg = conv.messages.find(m => m.id === messageId)
    if (!msg || msg.cardType !== 'admin_preview') return

    const data = msg.cardData as any
    if (!data) return

    conv.processing = true
    try {
      const result = await adminChangeApi.execute({
        entityType: data.entityType,
        entityId: data.entityId,
        changes: data.proposedChanges,
        risk: data.risk,
        reason: reason || '管理员通过 Agent 执行变更',
        confirmed,
      })

      const resultMsg: ChatMessage = {
        id: crypto.randomUUID(),
        role: 'assistant',
        content: `变更已执行 (#${result.recordId})`,
        timestamp: Date.now(),
        cardType: 'admin_result',
        cardData: {
          recordId: result.recordId,
          entityType: result.entityType,
          entityId: result.entityId,
          status: result.status,
        },
      }
      conv.messages.push(resultMsg)
    } catch (err: unknown) {
      const message = (err as { message?: string })?.message || '执行失败'
      conv.messages.push({
        id: crypto.randomUUID(),
        role: 'assistant',
        content: `❌ 变更执行失败：${message}`,
        timestamp: Date.now(),
      })
    } finally {
      conv.processing = false
      conv.updatedAt = Date.now()
      save()
    }
  }

  return {
    conversations,
    activeId,
    activeConversation,
    isAdminAgent,
    adminAvailable,
    matchingAccumulatedParams,
    init,
    createConversation,
    deleteConversation,
    switchConversation,
    clearActiveMessages,
    sendMessage,
    executeAdminChange,
  }
})
