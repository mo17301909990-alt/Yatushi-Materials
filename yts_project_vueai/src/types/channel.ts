export type ChannelType = 'feishu' | 'dingtalk' | 'wecom' | 'qq' | 'web'
export type ConversationType = 'p2p' | 'group'
export type MessageContentType = 'text' | 'image' | 'action'

export interface UnifiedMessage {
  id: string
  channel: ChannelType
  sender: { id: string; name: string; avatar?: string }
  conversation: { id: string; type: ConversationType; title?: string }
  content: { type: MessageContentType; text?: string; raw?: unknown }
  raw: unknown
  receivedAt: number
}

export interface ActionButton {
  id: string
  label: string
  primary?: boolean
  url?: string
  value?: Record<string, unknown>
}

export interface CardPayload {
  title?: string
  markdown?: string
  header?: { title: string; theme?: 'primary' | 'success' | 'warning' | 'danger' }
  fields?: { label: string; value: string; isShort?: boolean }[]
  actions?: ActionButton[]
  raw?: Record<string, unknown>
}

export interface SourceRef {
  title: string
  url?: string
  confidence?: number
}

export interface AgentResponse {
  reply: string
  card: CardPayload | null
  actions?: ActionButton[]
  streaming?: boolean
  agentId: string
  agentName: string
  toChannel: ChannelType
  metadata: {
    confidence: number
    sources?: SourceRef[]
    suggestedAgents?: string[]
    processingTimeMs?: number
  }
}

export interface IMessageChannel {
  readonly name: string
  readonly channelType: ChannelType
  send(response: AgentResponse): Promise<void>
  sendStream(response: AgentResponse): AsyncGenerator<string, void, unknown>
  onMessage(handler: (msg: UnifiedMessage) => void): void
  start(): Promise<void>
  stop(): Promise<void>
}
