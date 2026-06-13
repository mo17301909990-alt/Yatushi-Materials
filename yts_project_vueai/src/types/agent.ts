/** Agent 配置 */
import type { ChannelType } from './channel'
export interface AgentConfig {
  id: string;
  name: string;
  role: string;
  department: string;
  description: string;
  status: 'online' | 'offline' | 'busy';
  avatar?: string;
  skills: AgentSkill[];
  materialCategories: MaterialCategory[];
  channels?: ChannelType[]
}

/** Agent 技能 */
export interface AgentSkill {
  id: string;
  name: string;
  description: string;
  triggerEvents?: string[];
  priority?: number;
}

/** 资料分类 */
export interface MaterialCategory {
  id: string;
  name: string;
  icon: string;
  count: number;
  standards: MaterialStandard[];
}

/** 材料标准书 */
export interface MaterialStandard {
  id: string;
  title: string;
  fileName: string;
  filePath: string;
  updatedAt: string;
  category: string;
  tags: string[];
}
