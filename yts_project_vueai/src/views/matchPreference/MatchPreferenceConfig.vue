<script setup lang="ts">
import { ref, reactive, onMounted, computed } from 'vue';
import { useAuthStore } from '@/stores/auth';
import { matchPreferenceApi } from '@/api/modules/matchPreference';
import type { MatchFieldsConfig, UserMatchPreferencesGrouped } from '@/api/types/matchPreference';

// 獲取認證store
const authStore = useAuthStore();

// 響應式數據
const loading = ref(false);
const saving = ref(false);
const message = ref('');
const messageType = ref<'success' | 'error' | ''>('');

// 匹配字段配置
const MATCH_FIELDS: MatchFieldsConfig = {
  color: {
    label: '顏色',
    options: [
      '七彩',
      '古銅色',
      '啞古銅',
      '啞金',
      '啞金色',
      '啞銀',
      '啞銀色',
      '啞銅色',
      '啡色',
      '彩虹',
      '彩虹色',
      '拉絲銀色',
      '橙',
      '橙色',
      '深藍',
      '淺藍',
      '淺藍色',
      '灰色',
      '灰金色',
      '玫瑰紅',
      '珍珠白',
      '珍珠粉紅色',
      '珍珠綠色',
      '白色',
      '粉紅',
      '粉紅色',
      '紅',
      '紅色',
      '素面古銅',
      '素面綠',
      '紫',
      '紫紅',
      '紫紅色',
      '紫色',
      '綠',
      '綠色',
      '藍',
      '藍綠色',
      '藍色',
      '透明',
      '金',
      '金色',
      '金色 ',
      '金色  ',
      '金黃',
      '銀',
      '銀色',
      '銅',
      '銅色',
      '青銅色',
      '黃色',
      '黑',
      '黑色'
    ]

  },
  size: {
    label: '尺寸',
    options: [
      '1220mm x 244m',
      '1280mm x 120m',
      '1280mm x 180m',
      '1280mm x 240m',
      '1280mm x 360m',
      '1280mm x 480m',
      '610mm x 120m',
      '610mm x 240m',
      '630mm x 120m',
      '630mm x 183m',
      '630mm x 240m',
      '640mm x 120m',
      '640mm x 180m',
      '640mm x 240m',
      '640mm x 360m',
      '680mm x 120m'
    ]
  },
  tightness: {
    label: '燙金鬆緊度',
    options: [
      ' 金紙松緊度 : 標准',
      ' 金紙松緊度 : 標準',
      ' 金紙松緊度 : 檣准',
      ' 金紙松緊度 : 標準偏緊',
      ' 金紙松緊度 : 松身',
      ' 金紙松緊度 : 標准偏緊',
      ' 金紙松緊度 : 標准偏松',
      ' 金紙松緊度 : 緊身',
      ' 金紙松緊度 : 標准(用於美心產品燙後不能有粉)',
      ' 金紙松緊度 : 松',
      ' 版縫距離 : 無縫',
      ' 版縫距離 : 46CM',
      ' 版縫距離 : 無縫(KM100S-P220)',
      ' 版縫距離 : 52CM',
      ' 版縫距離 : 54CM',
      ' 版縫距離 : 61CM',
      ' 版縫距離 : 50CM',
      ' 版縫距離 : 版距46CM',
      ' 版縫距離 : 有縫',
      ' 版縫距離 : 無縫(燙後印刷)',
      ' 版縫距離 : 縫距:46CM'
    ]

  },
  temperature: {
    label: '溫度範圍',
    options: ['100~120', '105~125', '110~130']
  },
  performance: {
    label: '特性與場景',
    options: [
      ' 性能 : 非耐磨 ',
      ' 性能 : 普通耐磨 ',
      ' 性能 : 高耐磨 ',
      ' 性能 : 抗氧化 ',
      '版縫粗0.2mm',
      '燙後不能有粉',
      ' 性能 : 耐磨 ',
      '版縫線粗為0.46MM',
      '燙後印',
      '版縫線粗0.46MM',
      '線粗0.3MM',
      '版式縫線粗0.3MM',
      '版距46CM',
      '版距50CM',
      '版距:53.5CM',
      '版縫線粗為0.3MM',
      '與401-07相似'
    ]

  }
};

// 用戶偏好數據
const userPreferences = reactive<UserMatchPreferencesGrouped>({
  color: [],
  size: [],
  tightness: [],
  temperature: [],
  performance: []
});

// 自定義輸入
const customInputs = reactive<Record<string, string>>({
  color: '',
  size: '',
  tightness: '',
  temperature: '',
  performance: ''
});

// 計算屬性：當前用戶
const currentUser = computed(() => authStore.userInfo);

// 獲取用戶匹配偏好
const loadUserPreferences = async () => {
  if (!currentUser.value?.id) {
    message.value = '請先登錄';
    messageType.value = 'error';
    return;
  }

  loading.value = true;
  try {
    const preferences = await matchPreferenceApi.getUserPreferences(currentUser.value.id);

    // 更新用户偏好数据
    Object.keys(userPreferences).forEach(field => {
      userPreferences[field as keyof UserMatchPreferencesGrouped] = preferences[field as keyof UserMatchPreferencesGrouped] || [];
    });

    console.log('加載的用戶偏好:', preferences);
  } catch (error: any) {
    console.error('加載用戶偏好失敗:', error);
    message.value = '加載用戶偏好失敗: ' + (error.message || '未知錯誤');
    messageType.value = 'error';
  } finally {
    loading.value = false;
  }
};

// 切換選項選擇狀態
const toggleOption = (fieldName: string, option: string) => {
  const field = userPreferences[fieldName as keyof UserMatchPreferencesGrouped];
  const index = field.indexOf(option);

  if (index > -1) {
    // 如果已選擇，則移除
    field.splice(index, 1);
  } else {
    // 如果未選擇，則添加
    field.push(option);
  }
};

// 添加自定義選項
const addCustomOption = (fieldName: string) => {
  const customValue = customInputs[fieldName].trim();
  if (!customValue) {
    return;
  }

  const field = userPreferences[fieldName as keyof UserMatchPreferencesGrouped];

  // 檢查是否已存在
  if (!field.includes(customValue)) {
    field.push(customValue);
    customInputs[fieldName] = ''; // 清空輸入框
  } else {
    message.value = '⚠️ 該選項已存在，請輸入不同的選項。';
    messageType.value = 'error';
    setTimeout(() => {
      message.value = '';
      messageType.value = '';
    }, 10000);
  }
};

// 移除選擇的選項
const removeOption = (fieldName: string, option: string) => {
  const field = userPreferences[fieldName as keyof UserMatchPreferencesGrouped];
  const index = field.indexOf(option);
  if (index > -1) {
    field.splice(index, 1);
  }
};

// 保存用戶偏好
const savePreferences = async () => {
  if (!currentUser.value?.id) {
    message.value = '請先登錄';
    messageType.value = 'error';
    return;
  }

  saving.value = true;
  try {
    const response = await matchPreferenceApi.batchSavePreferences(
      currentUser.value.id,
      userPreferences
    );

    message.value = '✅ 匹配偏好保存成功！所有設置已同步到數據庫。';
    messageType.value = 'success';

    console.log('保存成功:', response);
  } catch (error: any) {
    console.error('保存失敗:', error);
    message.value = '保存失敗: ' + (error.message || '未知錯誤');
    messageType.value = 'error';
  } finally {
    saving.value = false;

    // 10秒後清除消息
    setTimeout(() => {
      message.value = '';
      messageType.value = '';
    }, 10000);
  }
};

// 重置偏好設置
const resetPreferences = () => {
  Object.keys(userPreferences).forEach(field => {
    userPreferences[field as keyof UserMatchPreferencesGrouped] = [];
  });

  Object.keys(customInputs).forEach(field => {
    customInputs[field] = '';
  });

  message.value = '🔄 偏好設置已重置！所有選擇已清空，可以重新配置。';
  messageType.value = 'success';

  setTimeout(() => {
    message.value = '';
    messageType.value = '';
  }, 10000);
};

// 檢查選項是否被選中
const isOptionSelected = (fieldName: string, option: string): boolean => {
  return userPreferences[fieldName as keyof UserMatchPreferencesGrouped].includes(option);
};

// 計算總的偏好數量
const totalPreferencesCount = computed(() => {
  return Object.values(userPreferences).reduce((total, preferences) => total + preferences.length, 0);
});

// 計算已配置的字段數量
const configuredFieldsCount = computed(() => {
  return Object.values(userPreferences).filter(preferences => preferences.length > 0).length;
});

// 組件掛載時加載數據
onMounted(() => {
  loadUserPreferences();
});
</script>

<template>
  <div class="min-h-screen bg-gray-50 py-8">
    <div class="max-w-6xl mx-auto px-4 sm:px-6 lg:px-8">
      <!-- 页面标题 -->
      <div class="mb-8">
        <h1 class="text-3xl font-bold text-gray-900">匹配度配置管理</h1>
        <p class="mt-2 text-gray-600">設置您的匹配偏好，系統將根據這些設置計算匹配度</p>
        <div class="mt-4 flex items-center justify-between">
          <p class="text-sm text-gray-500">支持多選和自定義選項，設置會自動保存</p>
          <div class="text-sm text-gray-600 bg-gray-50 px-3 py-1 rounded-full">
            已配置 {{ configuredFieldsCount }}/5 個字段，共 {{ totalPreferencesCount }} 個偏好
          </div>
        </div>
      </div>

      <!-- 消息提示 -->
      <div v-if="message" :class="[
        'mb-6 p-4 rounded-lg shadow-md border-l-4 transition-all duration-300',
        messageType === 'success'
          ? 'bg-green-50 text-green-800 border-l-green-500 border border-green-200'
          : 'bg-red-50 text-red-800 border-l-red-500 border border-red-200'
      ]">
        <div class="flex items-center">
          <div class="flex-shrink-0">
            <span v-if="messageType === 'success'" class="text-green-500 text-lg">✅</span>
            <span v-else class="text-red-500 text-lg">❌</span>
          </div>
          <div class="ml-3">
            <p class="font-medium">{{ message }}</p>
          </div>
        </div>
      </div>

      <!-- 加载状态 -->
      <div v-if="loading" class="text-center py-8">
        <div class="inline-block animate-spin rounded-full h-8 w-8 border-b-2 border-indigo-600"></div>
        <p class="mt-2 text-gray-600">加載中...</p>
      </div>

      <!-- 匹配字段配置 -->
      <div v-else class="space-y-6">
        <div v-for="(config, fieldName) in MATCH_FIELDS" :key="fieldName" class="bg-white rounded-lg shadow-md p-6">

          <!-- 字段标题 -->
          <h3 class="text-lg font-semibold text-gray-900 mb-4">
            {{ config.label }}
            <span class="text-sm font-normal text-gray-500">
              (已選擇 {{ userPreferences[fieldName as keyof UserMatchPreferencesGrouped].length }} 項)
            </span>
          </h3>

          <!-- 预设选项 -->
          <div class="mb-4">
            <h4 class="text-sm font-medium text-gray-700 mb-2">預設選項：</h4>
            <div class="flex flex-wrap gap-2">
              <button v-for="option in config.options" :key="option" @click="toggleOption(fieldName, option)" :class="[
                'px-3 py-1 rounded-full text-sm border transition-colors',
                isOptionSelected(fieldName, option)
                  ? 'bg-indigo-100 text-indigo-700 border-indigo-300'
                  : 'bg-gray-100 text-gray-700 border-gray-300 hover:bg-gray-200'
              ]">
                {{ option }}
              </button>
            </div>
          </div>

          <!-- 自定义输入 -->
          <div class="mb-4">
            <h4 class="text-sm font-medium text-gray-700 mb-2">自定義選項：</h4>
            <div class="flex gap-2">
              <input v-model="customInputs[fieldName]" @keyup.enter="addCustomOption(fieldName)"
                :placeholder="`輸入自定義${config.label}選項`"
                class="flex-1 px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500 text-sm">
              <button @click="addCustomOption(fieldName)" :disabled="!customInputs[fieldName].trim()"
                class="px-4 py-2 bg-indigo-600 text-white rounded-md hover:bg-indigo-700 disabled:bg-gray-400 disabled:cursor-not-allowed text-sm">
                添加
              </button>
            </div>
          </div>

          <!-- 已选择的选项 -->
          <div v-if="userPreferences[fieldName as keyof UserMatchPreferencesGrouped].length > 0">
            <h4 class="text-sm font-medium text-gray-700 mb-2">已選擇的選項：</h4>
            <div class="flex flex-wrap gap-2">
              <span v-for="option in userPreferences[fieldName as keyof UserMatchPreferencesGrouped]" :key="option"
                class="inline-flex items-center px-3 py-1 rounded-full text-sm bg-green-100 text-green-700 border border-green-300">
                {{ option }}
                <button @click="removeOption(fieldName, option)" class="ml-2 text-green-500 hover:text-green-700">
                  ×
                </button>
              </span>
            </div>
          </div>
        </div>
      </div>



      <!-- 操作按钮 -->
      <div class="mt-8 flex justify-center space-x-4">
        <button @click="savePreferences" :disabled="saving || loading"
          class="px-6 py-3 bg-indigo-600 text-white rounded-md hover:bg-indigo-700 disabled:bg-gray-400 disabled:cursor-not-allowed font-medium">
          <span v-if="saving">保存中...</span>
          <span v-else>保存設置</span>
        </button>

        <button @click="resetPreferences" :disabled="saving || loading"
          class="px-6 py-3 bg-gray-600 text-white rounded-md hover:bg-gray-700 disabled:bg-gray-400 disabled:cursor-not-allowed font-medium">
          重置設置
        </button>
      </div>
    </div>
  </div>
</template>

<style scoped>
/* 自定义样式 */
.animate-spin {
  animation: spin 1s linear infinite;
}

@keyframes spin {
  from {
    transform: rotate(0deg);
  }

  to {
    transform: rotate(360deg);
  }
}
</style>
