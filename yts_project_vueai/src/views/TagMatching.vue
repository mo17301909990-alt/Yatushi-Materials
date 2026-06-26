<script setup lang="ts">
import { ref, computed } from 'vue';
import { useHotStampingStore, type Product } from '../stores/hotStamping';
const hotStampingStore = useHotStampingStore();

const selectedTagSequence = ref<Array<{
  id: string;
  label: string;
  color: string;
  category: string;
  subCategory?: string;
  matchStep: number;
}>>([]);

const currentMatchStep = ref(0);

// 標籤接口定義
interface Tag {
  id: string;
  label: string;
}

interface SubCategory {
  title: string;
  tags: Tag[];
}

interface CategoryWithSub {
  title: string;
  color: string;
  subCategories: Record<string, SubCategory>;
}

interface CategoryWithTags {
  title: string;
  color: string;
  tags: Tag[];
}

const tags: {
  specifications: CategoryWithSub;
  features: CategoryWithSub;
  scenarios: CategoryWithTags;
  compatibility: CategoryWithTags;
} = {
  specifications: {
    title: '規格信息標籤',
    color: 'blue',
    subCategories: {
      color: {
        title: '顏色',
        tags: [
          { id: 'color-gold', label: '亮金' },
          { id: 'color-matt-gold', label: '啞金' },
          { id: 'color-rose', label: '玫瑰金' },
          { id: 'color-champagne', label: '香槟金' },
          { id: 'color-silver', label: '銀色' },
          { id: 'color-copper', label: '銅色' }
        ]
      },
      size: {
        title: '尺寸',
        tags: [
          { id: 'size-640x100', label: '640mm × 100m' },
          { id: 'size-640x120', label: '640mm × 120m' },
          { id: 'size-640x150', label: '640mm × 150m' },
          { id: 'size-640x200', label: '640mm × 200m' },
          { id: 'size-640x300', label: '640mm × 300m' }
        ]
      },
      tension: {
        title: '鬆緊度',
        tags: [
          { id: 'tension-tight', label: '緊身' },
          { id: 'tension-standard', label: '標準' },
          { id: 'tension-loose', label: '鬆身' }
        ]
      },
      priceLevel: {
        title: '價格等級',
        tags: [
          { id: 'price-1', label: '等級 1' },
          { id: 'price-2', label: '等級 2' },
          { id: 'price-3', label: '等級 3' },
          { id: 'price-4', label: '等級 4' },
          { id: 'price-5', label: '等級 5' }
        ]
      },
      temperature: {
        title: '溫度範圍',
        tags: [
          { id: 'temp-105-125', label: '105~125℃' },
          { id: 'temp-110-130', label: '110~130℃' },
          { id: 'temp-115-135', label: '115~135℃' },
          { id: 'temp-120-140', label: '120~140℃' }
        ]
      }
    }
  },
  features: {
    title: '特性標籤',
    color: 'green',
    subCategories: {
      patternArea: {
        title: '圖案面積',
        tags: [
          { id: 'area-extra-large', label: '超大面積' },
          { id: 'area-large', label: '大面積' },
          { id: 'area-medium', label: '中面積' },
          { id: 'area-small', label: '小面積' },
          { id: 'area-extra-small', label: '超小面積' }
        ]
      },
      wearResistance: {
        title: '耐磨性',
        tags: [
          { id: 'wear-high', label: '高耐磨' },
          { id: 'wear-medium', label: '中等耐磨' },
          { id: 'wear-normal', label: '普通耐磨' }
        ]
      }
    }
  },
  scenarios: {
    title: '場景標籤',
    color: 'purple',
    tags: [
      { id: 'scenario-card', label: '賀卡' },
      { id: 'scenario-package', label: '包裝' },
      { id: 'scenario-book', label: '精平裝' },
      { id: 'scenario-general', label: '綜合' }
    ]
  },
  compatibility: {
    title: '兼容性信息標籤',
    color: 'yellow',
    tags: [
      { id: 'laminating-all', label: '全兼容過膠' },
      { id: 'laminating-partial', label: '部分兼容過膠' },
      { id: 'laminating-none', label: '不兼容過膠' },
      { id: 'uv-printing', label: 'UV印刷兼容' },
      { id: 'led-uv', label: 'LED UV閃光兼容' },
      { id: 'printing', label: '印刷兼容' },
      { id: 'flat-stamping', label: '平面燙金兼容' },
      { id: 'relief-stamping', label: '浮雕燙金兼容' },
      { id: 'matte-stamping', label: '啞面燙金兼容' },
      { id: 'glitter-stamping', label: '閃光燙金兼容' }
    ]
  }
};

const selectTag = (tag: { id: string; label: string }, category: string, subCategory: string | undefined, color: string) => {
  currentMatchStep.value++;
  selectedTagSequence.value.push({
    ...tag,
    color,
    category,
    subCategory,
    matchStep: currentMatchStep.value
  });
};

const removeTag = (index: number) => {
  selectedTagSequence.value = selectedTagSequence.value.filter((_, i) => i !== index);
  selectedTagSequence.value.forEach((tag, i) => {
    tag.matchStep = i + 1;
  });
  currentMatchStep.value = selectedTagSequence.value.length;
};

const matchResults = computed(() => {
  if (selectedTagSequence.value.length === 0) return [];

  const allProducts = [
    ...hotStampingStore.getPapersByType('normal'),
    ...hotStampingStore.getPapersByType('normalWearResistant'),
    ...hotStampingStore.getPapersByType('highWearResistant')
  ];

  return allProducts.map((product: Product) => {
    let score = 0;
    let matchDetails: Array<{ step: number; score: number }> = [];

    selectedTagSequence.value.forEach((tag, index) => {
      let matched = false;

      if (tag.category === 'specifications') {
        switch (tag.subCategory) {
          case 'color':
            matched = tag.id.split('-')[1] === product.color?.toLowerCase();
            break;
          case 'size':
            const [width, length] = tag.id.split('-')[1].split('x');
            matched = product.width.includes(width) && product.length.includes(length);
            break;
          case 'tension':
            matched = product.tension.toLowerCase().includes(tag.id.split('-')[1]);
            break;
          case 'priceLevel':
            matched = product.costIndex === parseInt(tag.id.split('-')[1]);
            break;
          case 'temperature':
            matched = product.temperature.includes(tag.label);
            break;
        }
      }

      else if (tag.category === 'features') {
        if (tag.subCategory === 'patternArea') {
          const areaSize = tag.id.split('-')[1];
          matched = product.validAreas.some(area => area.toLowerCase().includes(areaSize));
        } else if (tag.subCategory === 'wearResistance') {
          const wearLevel = tag.id.split('-')[1];
          matched = product.features.some(f => f.toLowerCase().includes(wearLevel));
        }
      }
      
      else if (tag.category === 'scenarios') {
        const scenario = tag.label;
        matched = product.scenarios.includes(scenario);
      }
      
      else if (tag.category === 'compatibility') {
        if (tag.id === 'laminating-all') {
          matched = product.laminatingCompatibility.status === 'all';
        } else if (tag.id === 'laminating-partial') {
          matched = product.laminatingCompatibility.status === 'partial';
        } else if (tag.id === 'laminating-none') {
          matched = product.laminatingCompatibility.status === 'none';
        } else if (tag.id === 'uv-printing') {
          matched = product.uvPrintingCompatible;
        } else if (tag.id === 'led-uv') {
          matched = product.ledUvGlitterCompatible;
        } else if (tag.id === 'printing') {
          matched = product.printingCompatible;
        } else if (tag.id === 'flat-stamping') {
          matched = product.flatStampingCompatible;
        } else if (tag.id === 'relief-stamping') {
          matched = product.reliefStampingCompatible;
        } else if (tag.id === 'matte-stamping') {
          matched = product.matteStampingCompatible;
        } else if (tag.id === 'glitter-stamping') {
          matched = product.glitterStampingCompatible;
        }
      }

      if (matched) {
        score += Math.round(((selectedTagSequence.value.length - index) / selectedTagSequence.value.length) * 100);
        matchDetails.push({
          step: index + 1,
          score: Math.round(((selectedTagSequence.value.length - index) / selectedTagSequence.value.length) * 100)
        });
      }
    });

    return {
      ...product,
      score: Math.round(score / selectedTagSequence.value.length),
      matchSteps: matchDetails
    };
  }).filter(product => product.score > 0)
    .sort((a, b) => b.score - a.score);
});

const getTagColorClasses = (color: string) => {
  const baseClasses = 'px-3 py-1 rounded-full text-sm font-medium cursor-pointer transition-colors';
  const colorClasses: Record<string, string> = {
    blue: 'bg-blue-100 text-blue-800 hover:bg-blue-200',
    green: 'bg-green-100 text-green-800 hover:bg-green-200',
    yellow: 'bg-yellow-100 text-yellow-800 hover:bg-yellow-200',
    purple: 'bg-purple-100 text-purple-800 hover:bg-purple-200'
  };

  return `${baseClasses} ${colorClasses[color] || colorClasses.blue}`;
};
</script>

<template>
  <div class="min-h-screen bg-gray-100 pt-20 px-4 sm:px-6 lg:px-8">
    <div class="max-w-7xl mx-auto">
      <div class="bg-white rounded-lg shadow px-5 py-6 sm:px-6">
        <h1 class="text-2xl font-bold mb-6">標籤匹配系統</h1>
        
        <div class="grid grid-cols-12 gap-6">
          <div class="col-span-4 space-y-6">
            <div v-if="tags.specifications" class="space-y-4">
              <h2 class="text-lg font-medium text-gray-700">{{ tags.specifications.title }}</h2>
              <div v-for="(subCategory, subKey) in tags.specifications.subCategories" :key="subKey" class="space-y-2">
                <h3 class="text-sm font-medium text-gray-600">{{ subCategory.title }}</h3>
                <div class="flex flex-wrap gap-2">
                  <span
                    v-for="tag in subCategory.tags"
                    :key="tag.id"
                    :class="getTagColorClasses(tags.specifications.color)"
                    v-permission="'matching:tag:select'"
                    @click="selectTag(tag, 'specifications', String(subKey), tags.specifications.color)"
                  >
                    {{ tag.label }}
                  </span>
                </div>
              </div>
            </div>

            <div v-for="(category, key) in tags" :key="key" class="space-y-2">
              <template v-if="key !== 'specifications'">
                <h2 class="text-lg font-medium text-gray-700">{{ category.title }}</h2>
                <div v-if="'subCategories' in category" class="space-y-4">
                  <div v-for="(subCategory, subKey) in (category as CategoryWithSub).subCategories" :key="subKey" class="space-y-2">
                    <h3 class="text-sm font-medium text-gray-600">{{ subCategory.title }}</h3>
                    <div class="flex flex-wrap gap-2">
                      <span
                        v-for="tag in subCategory.tags"
                        :key="tag.id"
                        :class="getTagColorClasses(category.color)"
                        v-permission="'matching:tag:select'"
                        @click="selectTag(tag, String(key), String(subKey), category.color)"
                      >
                        {{ tag.label }}
                      </span>
                    </div>
                  </div>
                </div>
                <div v-else class="flex flex-wrap gap-2">
                  <span
                    v-for="tag in (category as CategoryWithTags).tags"
                    :key="tag.id"
                    :class="getTagColorClasses(category.color)"
                    v-permission="'matching:tag:select'"
                    @click="selectTag(tag, String(key), undefined, category.color)"
                  >
                    {{ tag.label }}
                  </span>
                </div>
              </template>
            </div>
          </div>
          
          <div class="col-span-8">
            <div class="mb-6">
              <h2 class="text-lg font-medium text-gray-700 mb-3">匹配序列</h2>
              <div class="flex flex-wrap gap-2">
                <div
                  v-for="(tag, index) in selectedTagSequence"
                  :key="index"
                  class="flex items-center gap-2 px-3 py-1 rounded-full text-sm font-medium"
                  :class="{
                    'bg-blue-500 text-white': tag.color === 'blue',
                    'bg-green-500 text-white': tag.color === 'green',
                    'bg-yellow-500 text-white': tag.color === 'yellow',
                    'bg-purple-500 text-white': tag.color === 'purple'
                  }"
                >
                  <span>{{ tag.matchStep }}. {{ tag.label }}</span>
                  <button
                    @click="removeTag(index)"
                    v-permission="'matching:tag:remove'"
                    class="ml-1 text-white hover:text-gray-200"
                  >
                    ×
                  </button>
                </div>
              </div>
            </div>
            
            <div>
              <h2 class="text-lg font-medium text-gray-700 mb-3">匹配結果</h2>
              
              <div v-if="matchResults.length === 0" class="text-center text-gray-500 py-8">
                請選擇標籤以開始匹
              </div>
              
              <div v-else class="space-y-4">
                <div
                  v-for="product in matchResults"
                  :key="product.name"
                  class="bg-gray-50 rounded-lg p-4 border border-gray-200"
                >
                  <div class="flex justify-between items-start mb-3">
                    <div>
                      <h3 class="text-lg font-medium">{{ product.name }}</h3>
                      <div class="text-sm text-gray-500">
                        <span>型號: {{ product.model }}</span>
                        <span class="mx-2">|</span>
                        <span>物料號: {{ product.id }}</span>
                      </div>
                    </div>
                    <div class="flex gap-2">
                      <span
                        v-for="matchStep in product.matchSteps"
                        :key="matchStep.step"
                        class="px-2 py-1 bg-green-100 text-green-800 rounded-full text-xs"
                      >
                        第{{ matchStep.step }}次: {{ matchStep.score }}%
                      </span>
                    </div>
                  </div>
                  
                  <div class="grid grid-cols-2 gap-4 text-sm">
                    <div>
                      <h4 class="font-medium text-gray-700 mb-2">規格信息</h4>
                      <div class="space-y-1 text-gray-600">
                        <p>顏色: {{ product.color }}</p>
                        <p>尺寸: {{ product.width }} × {{ product.length }}</p>
                        <p>鬆緊度: {{ product.tension }}</p>
                        <p>溫度範圍: {{ product.temperature }}</p>
                        <p>價格等級: {{ product.costIndex }}</p>
                      </div>
                    </div>
                    
                    <div>
                      <h4 class="font-medium text-gray-700 mb-2">應用場景</h4>
                      <div class="flex flex-wrap gap-1">
                        <span
                          v-for="scenario in product.scenarios"
                          :key="scenario"
                          class="px-2 py-1 bg-green-100 text-green-800 rounded-full text-xs"
                        >
                          {{ scenario }}
                        </span>
                      </div>
                    </div>
                    
                    <div class="col-span-2">
                      <h4 class="font-medium text-gray-700 mb-2">特性</h4>
                      <div class="flex flex-wrap gap-1">
                        <span
                          v-for="feature in product.features"
                          :key="feature"
                          class="px-2 py-1 bg-blue-100 text-blue-800 rounded-full text-xs"
                        >
                          {{ feature }}
                        </span>
                      </div>
                    </div>

                    <div class="col-span-2">
                      <h4 class="font-medium text-gray-700 mb-2">兼容性信息</h4>
                      <div class="grid grid-cols-2 gap-2">
                        <div class="flex items-center gap-2">
                          <span class="text-gray-600">過膠兼容性:</span>
                          <span :class="{
                            'text-green-600': product.laminatingCompatibility.status === 'all',
                            'text-yellow-600': product.laminatingCompatibility.status === 'partial',
                            'text-red-600': product.laminatingCompatibility.status === 'none'
                          }">
                            {{ product.laminatingCompatibility.status === 'all' ? '全兼容' : 
                               product.laminatingCompatibility.status === 'partial' ? '部分兼容' : '不兼容' }}
                          </span>
                        </div>

                        <div class="flex items-center gap-2">
                          <span class="text-gray-600">UV印刷:</span>
                          <span :class="product.uvPrintingCompatible ? 'text-green-600' : 'text-red-600'">
                            {{ product.uvPrintingCompatible ? '兼容' : '不兼容' }}
                          </span>
                        </div>
                        <div class="flex items-center gap-2">
                          <span class="text-gray-600">LED UV閃光:</span>
                          <span :class="product.ledUvGlitterCompatible ? 'text-green-600' : 'text-red-600'">
                            {{ product.ledUvGlitterCompatible ? '兼容' : '不兼容' }}
                          </span>
                        </div>
                        <div class="flex items-center gap-2">
                          <span class="text-gray-600">印刷:</span>
                          <span :class="product.printingCompatible ? 'text-green-600' : 'text-red-600'">
                            {{ product.printingCompatible ? '兼容' : '不兼容' }}
                          </span>
                        </div>
                        <div class="flex items-center gap-2">
                          <span class="text-gray-600">平面燙金:</span>
                          <span :class="product.flatStampingCompatible ? 'text-green-600' : 'text-red-600'">
                            {{ product.flatStampingCompatible ? '兼容' : '不兼容' }}
                          </span>
                        </div>
                        <div class="flex items-center gap-2">
                          <span class="text-gray-600">浮雕燙金:</span>
                          <span :class="product.reliefStampingCompatible ? 'text-green-600' : 'text-red-600'">
                            {{ product.reliefStampingCompatible ? '兼容' : '不兼容' }}
                          </span>
                        </div>
                        <div class="flex items-center gap-2">
                          <span class="text-gray-600">啞面燙金:</span>
                          <span :class="product.matteStampingCompatible ? 'text-green-600' : 'text-red-600'">
                            {{ product.matteStampingCompatible ? '兼容' : '不兼容' }}
                          </span>
                        </div>
                        <div class="flex items-center gap-2">
                          <span class="text-gray-600">閃光燙金:</span>
                          <span :class="product.glitterStampingCompatible ? 'text-green-600' : 'text-red-600'">
                            {{ product.glitterStampingCompatible ? '兼容' : '不兼容' }}
                          </span>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>