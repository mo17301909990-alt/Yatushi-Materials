<template>
  <div class="p-6 space-y-6 bg-gray-50 min-h-screen">
    <!-- 顶部标题 -->
    <div class="flex justify-between items-center">
      <h1 class="text-2xl font-bold">工藝評估 · 絲印 → 難度 → 資源組匹配</h1>
    </div>

    <!-- 基础尺寸 -->
    <div class="rounded-2xl bg-white border p-4">
      <h2 class="text-lg font-semibold mb-3">基礎尺寸</h2>
      <div class="grid grid-cols-2 md:grid-cols-4 gap-3">
        <label class="flex flex-col gap-1 text-sm">
          <span class="text-gray-600">長邊 L (mm)</span>
          <input type="number" class="input-field" v-model.number="params.len_mm" />
        </label>
        <label class="flex flex-col gap-1 text-sm">
          <span class="text-gray-600">寬邊 W (mm)</span>
          <input type="number" class="input-field" v-model.number="params.wid_mm" />
        </label>
      </div>
    </div>

    <!-- 絲印參數 -->
    <div class="rounded-2xl bg-white border p-4">
      <h2 class="text-lg font-semibold mb-3">工藝參數（絲印）</h2>
      <div class="grid grid-cols-2 md:grid-cols-4 gap-3">
        <label class="flex flex-col gap-1 text-sm">
          <span class="text-gray-600">用紙 (gsm)</span>
          <input type="number" class="input-field" v-model.number="params.sheet_gsm" />
        </label>
        <label class="flex flex-col gap-1 text-sm">
          <span class="text-gray-600">紙張類型</span>
          <select class="input-field" v-model="params.paper_type">
            <option value="">— 請選擇 —</option>
            <option>銅版紙</option>
            <option>藝術紙</option>
            <option>書紙</option>
            <option>咭紙</option>
            <option>其他</option>
          </select>
        </label>
        <label class="flex flex-col gap-1 text-sm">
          <span class="text-gray-600">絲印面積比例(%)</span>
          <input type="number" class="input-field" v-model.number="params.silk_area_ratio" />
        </label>
        <label class="flex flex-col gap-1 text-sm">
          <span class="text-gray-600">網目數</span>
          <input type="number" class="input-field" v-model.number="params.mesh_count" />
        </label>
        <label class="flex flex-col gap-1 text-sm">
          <span class="text-gray-600">油墨類型</span>
          <select class="input-field" v-model="params.ink_type">
            <option value="">— 請選擇 —</option>
            <option>水性</option>
            <option>油性</option>
            <option>UV</option>
            <option>其他</option>
          </select>
        </label>
        <label class="flex flex-col gap-1 text-sm">
          <span class="text-gray-600">是否特殊材質</span>
          <select class="input-field" v-model="params.special_material">
            <option :value="false">否</option>
            <option :value="true">是</option>
          </select>
        </label>
        <label class="flex flex-col gap-1 text-sm">
          <span class="text-gray-600">是否需對位</span>
          <select class="input-field" v-model="params.need_registration">
            <option :value="false">否</option>
            <option :value="true">是</option>
          </select>
        </label>
        <label class="flex flex-col gap-1 text-sm">
          <span class="text-gray-600">顏色數</span>
          <input type="number" class="input-field" v-model.number="params.color_count" />
        </label>
        <label class="flex flex-col gap-1 text-sm">
          <span class="text-gray-600">是否疊印</span>
          <select class="input-field" v-model="params.overprint">
            <option :value="false">否</option>
            <option :value="true">是</option>
          </select>
        </label>
        <label class="flex flex-col gap-1 text-sm">
          <span class="text-gray-600">是否漸變</span>
          <select class="input-field" v-model="params.gradient">
            <option :value="false">否</option>
            <option :value="true">是</option>
          </select>
        </label>
        <label class="flex flex-col gap-1 text-sm">
          <span class="text-gray-600">是否有金粉/銀粉</span>
          <select class="input-field" v-model="params.has_metallic_powder">
            <option :value="false">否</option>
            <option :value="true">是</option>
          </select>
        </label>
        <label class="flex flex-col gap-1 text-sm">
          <span class="text-gray-600">訂單數量</span>
          <input type="number" class="input-field" v-model.number="params.order_quantity" />
        </label>
      </div>

      <!-- 操作區 -->
      <div class="mt-4">
        <button class="btn-primary" @click="evaluate">評估</button>
      </div>
    </div>

    <!-- 解釋 + 能力 + 候選資源組 可視化區 -->
    <div class="grid grid-cols-1 lg:grid-cols-3 gap-4">
      <!-- 規則命中 -->
      <div class="rounded-2xl bg-white border p-4 lg:col-span-2">
        <h3 class="font-semibold mb-3">規則命中 (Explain)</h3>
        <div class="overflow-auto">
          <table class="w-full text-sm">
            <thead>
              <tr class="text-left text-gray-500">
                <th class="py-2 pr-4">來源</th>
                <th class="py-2 pr-4">規則</th>
                <th class="py-2 pr-4">動作</th>
                <th class="py-2 pr-4">原因</th>
                <th class="py-2 pr-0 text-right">score</th>
              </tr>
            </thead>
            <tbody>
              <tr v-if="ruleHits.length === 0">
                <td colspan="5" class="py-3 text-gray-400">（暫無命中，點擊上方「評估」查看）</td>
              </tr>
              <tr v-for="(r, i) in ruleHits" :key="i" class="border-t">
                <td class="py-2 pr-4 whitespace-nowrap">{{ r.source }}</td>
                <td class="py-2 pr-4">{{ r.rule }}</td>
                <td class="py-2 pr-4 text-gray-600">{{ r.action }}</td>
                <td class="py-2 pr-4 text-gray-600">{{ r.reason }}</td>
                <td class="py-2 pr-0 text-right">{{ typeof r.score === 'number' ? r.score.toFixed(2) : r.score }}</td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>

      <!-- 難度 & 能力 -->
      <div class="rounded-2xl bg-white border p-4">
        <h3 class="font-semibold mb-3">難度 & 能力</h3>
        <div class="grid grid-cols-3 gap-3 text-sm">
          <div class="rounded-xl border p-3 flex flex-col">
            <span class="text-gray-500">難度</span>
            <span class="text-lg font-semibold">{{ metrics.difficulty ?? '-' }}</span>
          </div>
          <div class="rounded-xl border p-3 flex flex-col">
            <span class="text-gray-500">準備時(min)</span>
            <span class="text-lg font-semibold">{{ metrics.prepMin ?? '-' }}</span>
          </div>
          <div class="rounded-xl border p-3 flex flex-col">
            <span class="text-gray-500">效率(件/小時)</span>
            <span class="text-lg font-semibold">{{ metrics.capacityPH ?? '-' }}</span>
          </div>
        </div>
      </div>
    </div>

    <!-- 候選資源組 -->
    <div class="rounded-2xl bg-white border p-4">
      <h3 class="font-semibold mb-3">候選資源組</h3>
      <div class="overflow-auto">
        <table class="w-full text-sm">
          <thead>
            <tr class="text-left text-gray-500">
              <th class="py-2 pr-4">編碼</th>
              <th class="py-2 pr-4">名稱</th>
              <th class="py-2 pr-4">類型</th>
              <th class="py-2 pr-4">幅面(mm)</th>
              <th class="py-2 pr-4">歷史</th>
              <th class="py-2 pr-4">成本</th>
              <th class="py-2 pr-0 text-right">得分</th>
            </tr>
          </thead>
          <tbody>
            <tr v-if="candidates.length === 0">
              <td colspan="7" class="py-3 text-gray-400">（暫無候選，點擊上方「評估」查看）</td>
            </tr>
            <tr
              v-for="(c, i) in candidates"
              :key="i"
              class="border-t"
              :class="{ 'bg-green-50': recommended === c.code }"
            >
              <td class="py-2 pr-4 whitespace-nowrap">{{ c.code }}</td>
              <td class="py-2 pr-4">{{ c.name }}</td>
              <td class="py-2 pr-4">{{ c.type }}</td>
              <td class="py-2 pr-4">{{ c.width }}</td>
              <td class="py-2 pr-4">{{ c.history }}</td>
              <td class="py-2 pr-4">{{ typeof c.cost === 'number' ? c.cost.toFixed(2) : c.cost }}</td>
              <td class="py-2 pr-0 text-right">{{ c.score }}</td>
            </tr>
          </tbody>
        </table>
      </div>
      <div v-if="recommended" class="text-sm mt-3 text-emerald-700">
        ✓ 推薦：<span class="font-medium">{{ recommended }}</span> — {{ getRecommendationText(recommended) }}
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';

const params = ref<any>({
  paper_type: '',
  ink_type: '',
});

// 解释表、产能卡片、候选资源组
const ruleHits = ref<any[]>([]);
const metrics = ref<{ difficulty?: string; prepMin?: number; capacityPH?: number }>({});
const candidates = ref<any[]>([]);
const recommended = ref<string | null>(null);

// 评估逻辑
const evaluate = () => {
  let difficulty: '特難' | '高難' | '難' | '中' | '易' = '中';
  const reasons: string[] = [];
  const hits: any[] = [];

  let score = 1.0;

  const L = Number(params.value.len_mm || 0);
  const W = Number(params.value.wid_mm || 0);
  const longSide = Math.max(L, W);
  const shortSide = Math.min(L, W);

  // 絲印规则
  if (params.value.sheet_gsm !== undefined) {
    if (params.value.sheet_gsm < 140) {
      reasons.push('用紙<140gsm');
      hits.push({
        source: '用紙',
        rule: '最小140gsm',
        action: 'SET_DIFFICULTY',
        reason: `gsm=${params.value.sheet_gsm}`,
        score: 0.2,
      });
    }
    if (params.value.sheet_gsm > 500) {
      reasons.push('用紙>500gsm');
      hits.push({
        source: '用紙',
        rule: '最大500gsm',
        action: 'SET_DIFFICULTY',
        reason: `gsm=${params.value.sheet_gsm}`,
        score: 0.2,
      });
    }
  }

  // 网目数规则
  if (params.value.mesh_count !== undefined) {
    if (params.value.mesh_count > 350) {
      reasons.push('網目數>350');
      hits.push({
        source: '網目',
        rule: '高網目數',
        action: 'SET_DIFFICULTY',
        reason: `mesh=${params.value.mesh_count}`,
        score: 0.3,
      });
    }
    if (params.value.mesh_count < 150) {
      reasons.push('網目數<150');
      hits.push({
        source: '網目',
        rule: '低網目數',
        action: 'SET_DIFFICULTY',
        reason: `mesh=${params.value.mesh_count}`,
        score: 0.2,
      });
    }
  }

  // 油墨类型规则
  if (params.value.ink_type === '油性') {
    reasons.push('油性油墨');
    hits.push({
      source: '油墨',
      rule: '油性',
      action: 'SET_DIFFICULTY',
      reason: '',
      score: 0.3,
    });
  }

  if (params.value.ink_type === 'UV') {
    reasons.push('UV油墨');
    hits.push({
      source: '油墨',
      rule: 'UV',
      action: 'SET_DIFFICULTY',
      reason: '',
      score: 0.4,
    });
  }

  // 对位规则
  if (params.value.need_registration) {
    reasons.push('需對位');
    hits.push({
      source: '對位',
      rule: '需對位',
      action: 'SET_DIFFICULTY',
      reason: '',
      score: 0.3,
    });
  }

  // 颜色数规则
  if (params.value.color_count !== undefined && params.value.color_count > 4) {
    reasons.push('顏色數>4');
    hits.push({
      source: '顏色',
      rule: '多色',
      action: 'SET_DIFFICULTY',
      reason: `colors=${params.value.color_count}`,
      score: 0.2,
    });
  }

  // 叠印规则
  if (params.value.overprint) {
    reasons.push('疊印');
    hits.push({
      source: '疊印',
      rule: '疊印',
      action: 'SET_DIFFICULTY',
      reason: '',
      score: 0.3,
    });
  }

  // 渐变规则
  if (params.value.gradient) {
    reasons.push('漸變');
    hits.push({
      source: '漸變',
      rule: '漸變',
      action: 'SET_DIFFICULTY',
      reason: '',
      score: 0.4,
    });
  }

  // 金粉/银粉规则
  if (params.value.has_metallic_powder) {
    reasons.push('有金粉/銀粉');
    hits.push({
      source: '特殊效果',
      rule: '金粉/銀粉',
      action: 'SET_DIFFICULTY',
      reason: '',
      score: 0.4,
    });
  }

  // 尺寸规则
  if (longSide > 850 || longSide < 400) {
    hits.push({
      source: '尺寸',
      rule: '長邊極值(≤400或>850)',
      action: 'SET_DIFFICULTY',
      reason: `L=${longSide}`,
      score: 0.2,
    });
  }

  if (shortSide > 600 || shortSide < 300) {
    hits.push({
      source: '尺寸',
      rule: '寬邊極值(≤300或>600)',
      action: 'SET_DIFFICULTY',
      reason: `W=${shortSide}`,
      score: 0.2,
    });
  }

  // 特殊材质规则
  if (params.value.special_material) {
    reasons.push('特殊材質');
    hits.push({
      source: '材質',
      rule: '特殊材質需評估',
      action: 'CONSULT',
      reason: '',
      score: 0.3,
    });
  }

  // 计算难度等级
  const hardTriggers = ['UV油墨', '漸變', '金粉/銀粉', '高網目數'];
  if (reasons.some((r) => hardTriggers.some((h) => r.includes(h)))) {
    difficulty = '特難';
  } else if (hits.filter((h) => String(h.action).includes('SET_DIFFICULTY')).length >= 4) {
    difficulty = '高難';
  } else if (hits.filter((h) => String(h.action).includes('SET_DIFFICULTY')).length >= 2) {
    difficulty = '難';
  } else if (hits.filter((h) => String(h.action).includes('SET_DIFFICULTY')).length >= 1) {
    difficulty = '中';
  } else {
    difficulty = '易';
  }

  // 产能与准备时
  const prepMap: any = { 易: 20, 中: 35, 難: 47, 高難: 70, 特難: 90 };
  let prepMin = prepMap[difficulty];
  let capacityPH = 1800; // 默认

  // 根据难度调整产能
  if (difficulty === '特難') capacityPH = 1200;
  else if (difficulty === '高難') capacityPH = 1500;
  else if (difficulty === '難') capacityPH = 1700;
  else if (difficulty === '中') capacityPH = 2000;
  else capacityPH = 2200;

  metrics.value = { difficulty, prepMin, capacityPH };
  ruleHits.value = hits;

  // 候选资源组
  const cands: any[] = [
    {
      code: 'WC07-SILK001',
      name: '絲印線（標準）',
      type: 'standard',
      width: 1200,
      history: '88%',
      cost: 1.0,
      score: (score + 0.1).toFixed(2),
    },
    {
      code: 'WC07-SILK002',
      name: '絲印線（高精度）',
      type: 'high-precision',
      width: 1200,
      history: '85%',
      cost: 1.15,
      score: (score + (difficulty === '特難' || difficulty === '高難' ? 0.2 : 0)).toFixed(2),
    },
  ];

  candidates.value = cands;
  const best = cands.length
    ? cands.reduce((a, b) => (Number(b.score) > Number(a.score) ? b : a), cands[0])
    : null;
  recommended.value = best ? best.code : null;
};

const getRecommendationText = (code: string) => {
  if (code.includes('SILK002')) return '高精度絲印機型';
  if (code.includes('SILK001')) return '標準絲印機型';
  return '機型優先級更高';
};
</script>

<style scoped>
.input-field {
  border: 1px solid #e5e7eb;
  border-radius: 12px;
  padding: 8px 10px;
  background: white;
  width: 100%;
}

.btn-primary {
  border: 1px solid #111827;
  border-radius: 12px;
  padding: 8px 14px;
  background: #111827;
  color: #fff;
  cursor: pointer;
}

.btn-primary:hover {
  background: #374151;
}
</style>

