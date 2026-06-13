import request from '../request';
import type { CardPayload } from '@/types/channel';

export interface SourceRef {
  title: string;
  confidence?: number;
}

export interface CopilotResponse {
  reply: string;
  suggestions: string[];
  params: Record<string, unknown>;
  sources?: SourceRef[];
  type?: string;
  data?: Record<string, unknown>;
  agentId?: string;
  agentName?: string;
  card?: CardPayload;
}

/** 从回复文本中提取 [来源: ...] 标记，返回分离后的来源列表和纯文本 */
export function extractSources(reply: string): { sources: SourceRef[]; cleanReply: string } {
  const sources: SourceRef[] = [];
  const lines = reply.split('\n');
  const cleanedLines = lines.filter((line) => {
    const m = line.match(/^\[来源:\s*(.+)\]$/);
    if (m) {
      sources.push({ title: m[1].trim() });
      return false;
    }
    return true;
  });
  return { sources, cleanReply: cleanedLines.join('\n').trim() };
}

export const copilotApi = {
  chat(message: string, systemPrompt?: string) {
    return request({
      url: '/ai/copilot/chat',
      method: 'post',
      data: systemPrompt ? { message, systemPrompt } : { message },
    });
  },
};
