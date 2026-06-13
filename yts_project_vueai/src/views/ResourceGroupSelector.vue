<template>
  <div class="min-h-screen bg-gray-50 py-8">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
      <!-- 页面标题 -->
      <div class="mb-8 flex items-center justify-between">
        <h1 class="text-3xl font-bold text-gray-900 tracking-tight">資源組選擇工具</h1>
        <div class="text-sm text-gray-500">
          <span class="mr-4">版本 v1.0</span>
        </div>
      </div>

      <!-- 公共参数区域 -->
      <div class="bg-white rounded-xl shadow-sm border border-gray-100 p-6 mb-8 transition-shadow hover:shadow-md">
        <div class="flex items-center justify-between mb-4 border-b border-gray-100 pb-2">
          <h2 class="text-lg font-semibold text-gray-800 flex items-center gap-2">
            <span class="text-xl">📄</span> 公共參數
          </h2>
          <div class="text-xs text-blue-600 bg-blue-50 px-3 py-1 rounded-full font-medium">
            當前流程：{{ taskFlowPreview }}
          </div>
        </div>
        
        <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-6 gap-6">
          <!-- 事業部 -->
          <div class="flex flex-col gap-2">
            <label class="text-xs uppercase font-bold text-gray-400 tracking-wider">事業部</label>
            <select
              v-model="commonParams.department"
              class="w-full border border-gray-200 rounded-lg px-3 py-2 text-sm focus:ring-2 focus:ring-blue-400 focus:border-blue-400 outline-none transition-all bg-white"
            >
              <option value="">請選擇</option>
              <option v-for="dept in paramOptions.departments" :key="dept" :value="dept">{{ dept }}</option>
            </select>
          </div>

          <!-- 产品类型 -->
          <div class="flex flex-col gap-2">
            <label class="text-xs uppercase font-bold text-gray-400 tracking-wider">產品類型</label>
            <select
              v-model="commonParams.productType"
              class="w-full border border-gray-200 rounded-lg px-3 py-2 text-sm focus:ring-2 focus:ring-blue-400 focus:border-blue-400 outline-none transition-all bg-white"
            >
              <option value="">請選擇</option>
              <option v-for="pt in paramOptions.productTypes" :key="pt" :value="pt">{{ pt }}</option>
            </select>
          </div>

          <!-- 用紙類型（暂时隐藏）
          <div class="flex flex-col gap-2">
            <label class="text-xs uppercase font-bold text-gray-400 tracking-wider">用紙類型</label>
            <input
              type="text"
              v-model="commonParams.paper"
              class="w-full border border-gray-200 rounded-lg px-3 py-2 text-sm focus:ring-2 focus:ring-blue-400 focus:border-blue-400 outline-none transition-all"
              placeholder="請輸入用紙類型"
            />
          </div>
          -->

          <!-- 適用界面 -->
          <div class="flex flex-col gap-2">
            <label class="text-xs uppercase font-bold text-gray-400 tracking-wider">適用界面</label>
            <select
              v-model="commonParams.suitableSurface"
              class="w-full border border-gray-200 rounded-lg px-3 py-2 text-sm focus:ring-2 focus:ring-blue-400 focus:border-blue-400 outline-none transition-all bg-white"
            >
              <option value="">請選擇</option>
              <option v-for="st in paramOptions.surfaceTypes" :key="st" :value="st">{{ st }}</option>
            </select>
          </div>

          <!-- 选纸张数（暂时隐藏）
          <div class="flex flex-col gap-2">
            <label class="text-xs uppercase font-bold text-gray-400 tracking-wider">選紙張數</label>
            <input
              type="number"
              v-model.number="commonParams.selectedSheetCount"
              class="w-full border border-gray-200 rounded-lg px-3 py-2 text-sm focus:ring-2 focus:ring-blue-400 focus:border-blue-400 outline-none transition-all"
              placeholder="請輸入張數"
              min="1"
            />
          </div>
          -->

          <!-- 长纹/短纹 -->
          <div class="flex flex-col gap-2">
            <label class="text-xs uppercase font-bold text-gray-400 tracking-wider">紋路方向</label>
            <select
              v-model="commonParams.grainDirection"
              class="w-full border border-gray-200 rounded-lg px-3 py-2 text-sm focus:ring-2 focus:ring-blue-400 focus:border-blue-400 outline-none transition-all bg-white"
            >
              <option value="">請選擇</option>
              <option v-for="gd in paramOptions.grainDirections" :key="gd" :value="gd">{{ gd }}</option>
            </select>
          </div>
        </div>
      </div>

      <!-- 任务列表区域 -->
      <div class="space-y-4">
        <div class="flex items-center justify-between px-2">
          <h2 class="text-lg font-semibold text-gray-800 flex items-center gap-2">
            <span class="text-xl">🛠️</span> 工藝流程
          </h2>
          <button
            @click="addTask"
            class="flex items-center gap-2 px-4 py-2 bg-blue-600 text-white rounded-lg shadow-sm hover:bg-blue-700 transition-colors text-sm font-medium"
          >
            <span>+</span> 添加工序
          </button>
        </div>

        <div class="space-y-3">
          <div
            v-for="(task, index) in taskList"
            :key="task.id"
            class="group bg-white rounded-xl border border-gray-100 shadow-sm hover:shadow-md transition-all duration-200 overflow-hidden"
            draggable="true"
            @dragstart="onDragStart(index)"
            @dragover.prevent
            @drop="onDrop(index)"
          >
            <!-- 任务行头部 -->
            <div class="flex items-center gap-4 p-4 border-b border-gray-50 bg-gray-50/30">
              <!-- 拖拽手柄 -->
              <div class="cursor-move text-gray-300 hover:text-gray-500 transition-colors px-2">
                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><circle cx="9" cy="12" r="1"/><circle cx="9" cy="5" r="1"/><circle cx="9" cy="19" r="1"/><circle cx="15" cy="12" r="1"/><circle cx="15" cy="5" r="1"/><circle cx="15" cy="19" r="1"/></svg>
              </div>
              
              <!-- 序号 -->
              <div class="flex-shrink-0 w-8 h-8 flex items-center justify-center bg-gray-100 text-gray-600 rounded-full text-xs font-bold">
                {{ task.seqNo }}
              </div>

              <!-- 任务类型选择 -->
              <div class="w-48">
                <select
                  v-model="task.taskCode"
                  @change="onTaskTypeChange(task)"
                  class="w-full bg-white border border-gray-200 rounded-lg px-3 py-2 text-sm focus:ring-2 focus:ring-blue-100 focus:border-blue-400 transition-all outline-none"
                  :class="{'text-gray-400': !task.taskCode}"
                >
                  <option value="" disabled>請選擇工藝類型</option>
                  <option 
                    v-for="t in tasks" 
                    :key="t.taskCode" 
                    :value="t.taskCode"
                    :disabled="isTaskSelected(t.taskCode, task)"
                  >
                    {{ t.taskName }} {{ isTaskSelected(t.taskCode, task) ? '(已選)' : '' }}
                  </option>
                </select>
              </div>
              
              <!-- 任务说明 -->
              <div class="flex-1">
                <input
                  type="text"
                  v-model="task.taskDesc"
                  class="w-full bg-transparent border-none p-0 text-sm focus:ring-0 placeholder-gray-400"
                  placeholder="任務說明 / 備注信息..."
                />
              </div>

              <!-- 操作按钮 -->
              <div class="flex items-center gap-2 opacity-0 group-hover:opacity-100 transition-opacity">
                <button
                  @click="insertTaskBefore(index)"
                  class="p-2 text-gray-400 hover:text-blue-600 hover:bg-blue-50 rounded-lg transition-colors"
                  title="插入前工序"
                >
                  <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M5 12h14"/><path d="M12 5v14"/></svg>
                </button>
                <button
                  @click="removeTask(index)"
                  class="p-2 text-gray-400 hover:text-red-600 hover:bg-red-50 rounded-lg transition-colors"
                  title="刪除"
                >
                  <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M3 6h18"/><path d="M19 6v14c0 1-1 2-2 2H7c-1 0-2-1-2-2V6"/><path d="M8 6V4c0-1 1-2 2-2h4c1 0 2 1 2 2v2"/></svg>
                </button>
              </div>
            </div>

            <!-- 任务行内容：参数与匹配结果 -->
            <div class="p-4 grid grid-cols-12 gap-6 items-start">
              <!-- 参数输入区 -->
              <div class="col-span-12 lg:col-span-8">
                <div class="flex flex-wrap gap-4 items-center min-h-[40px]">
                  <!-- 无需参数提示 -->
                  <div v-if="!task.requiresSize && !task.requiresSheetCount && !task.requiresGsm && !task.requiresThickness && !task.remark && task.taskCode" class="text-sm text-gray-400 italic flex items-center gap-2">
                    <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><circle cx="12" cy="12" r="10"/><line x1="12" y1="16" x2="12" y2="12"/><line x1="12" y1="8" x2="12.01" y2="8"/></svg>
                    此工序無需額外參數
                  </div>
                  
                  <!-- 引导选择工序 -->
                  <div v-if="!task.taskCode" class="text-sm text-gray-400 italic">
                    ← 請先選擇左側工藝類型
                  </div>

                  <!-- 尺寸输入 -->
                  <div v-if="task.requiresSize" class="bg-gray-50 rounded-lg px-3 py-2 border border-gray-100 flex items-center gap-2 shadow-sm">
                    <span class="text-xs font-bold text-gray-500 uppercase">尺寸</span>
                    <input type="number" v-model.number="task.width" class="w-16 bg-white border border-gray-200 rounded px-2 py-1 text-sm text-center focus:ring-1 focus:ring-blue-400 focus:border-blue-400 outline-none" placeholder="寬" />
                    <span class="text-gray-400">×</span>
                    <input type="number" v-model.number="task.height" class="w-16 bg-white border border-gray-200 rounded px-2 py-1 text-sm text-center focus:ring-1 focus:ring-blue-400 focus:border-blue-400 outline-none" placeholder="高" />
                    <span class="text-xs text-gray-400">mm</span>
                  </div>

                  <!-- 印张石数 -->
                  <div v-if="task.requiresSheetCount" class="bg-gray-50 rounded-lg px-3 py-2 border border-gray-100 flex items-center gap-2 shadow-sm">
                    <span class="text-xs font-bold text-gray-500 uppercase">印張石数</span>
                    <input type="number" v-model.number="task.sheetCount" class="w-20 bg-white border border-gray-200 rounded px-2 py-1 text-sm text-center focus:ring-1 focus:ring-blue-400 focus:border-blue-400 outline-none" placeholder="石數" />
                  </div>

                  <!-- 克重 -->
                  <div v-if="task.requiresGsm" class="bg-gray-50 rounded-lg px-3 py-2 border border-gray-100 flex items-center gap-2 shadow-sm">
                    <span class="text-xs font-bold text-gray-500 uppercase">克重</span>
                    <input type="number" v-model.number="task.gsm" class="w-16 bg-white border border-gray-200 rounded px-2 py-1 text-sm text-center focus:ring-1 focus:ring-blue-400 focus:border-blue-400 outline-none" placeholder="g/m²" />
                  </div>

                  <!-- 厚度 -->
                  <div v-if="task.requiresThickness" class="bg-gray-50 rounded-lg px-3 py-2 border border-gray-100 flex items-center gap-2 shadow-sm">
                    <span class="text-xs font-bold text-gray-500 uppercase">厚度</span>
                    <input type="number" v-model.number="task.thickness" class="w-16 bg-white border border-gray-200 rounded px-2 py-1 text-sm text-center focus:ring-1 focus:ring-blue-400 focus:border-blue-400 outline-none" placeholder="mm" step="0.01" />
                  </div>
                </div>
              </div>

              <!-- 匹配状态区 -->
              <div class="col-span-12 lg:col-span-4 flex items-center justify-end gap-3 border-t lg:border-t-0 lg:border-l border-gray-100 pt-4 lg:pt-0 lg:pl-6">
                <!-- 已选机台展示 -->
                <div v-if="task.selectedResourceGroup" class="flex-1 text-right">
                  <div class="text-xs text-gray-400 mb-1">已選定機台</div>
                  <div class="font-bold text-blue-600 flex items-center justify-end gap-2">
                    {{ task.selectedResourceGroup.resourceGroupCode }}
                    <span class="w-2 h-2 rounded-full bg-green-500"></span>
                  </div>
                </div>
                
                <!-- 自动匹配按钮 -->
                <button
                  @click="autoMatchResourceGroup(task)"
                  :disabled="!task.taskCode"
                  class="flex-shrink-0 bg-white border border-blue-200 text-blue-600 hover:bg-blue-50 active:bg-blue-100 px-4 py-2 rounded-lg text-sm font-medium transition-colors disabled:opacity-50 disabled:cursor-not-allowed disabled:hover:bg-white flex items-center gap-2"
                >
                  <span>⚡</span> 自動匹配
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 资源组选择弹窗 -->
      <div
        v-if="showResourceDialog"
        class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 backdrop-blur-sm"
        @click.self="showResourceDialog = false"
      >
        <div class="bg-white rounded-2xl shadow-2xl w-full max-w-5xl max-h-[90vh] overflow-hidden flex flex-col">
          <!-- 弹窗头部 -->
          <div class="px-6 py-5 border-b border-gray-200 bg-gradient-to-r from-blue-50 to-indigo-50 flex justify-between items-center">
            <div>
              <h3 class="text-xl font-bold text-gray-900 mb-1">
                ⚡ 機台候選匹配結果
              </h3>
              <p class="text-sm text-gray-600">
                {{ currentTask?.taskDesc || getTaskName(currentTask?.taskCode) }}
              </p>
            </div>
            <button
              @click="showResourceDialog = false"
              class="text-gray-400 hover:text-gray-600 hover:bg-white rounded-full p-2 transition-colors"
            >
              <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><line x1="18" y1="6" x2="6" y2="18"/><line x1="6" y1="6" x2="18" y2="18"/></svg>
            </button>
          </div>
          
          <!-- 当前参数概况 -->
          <div class="px-6 py-4 bg-gray-50 border-b border-gray-100">
              <div class="flex items-center justify-between mb-3">
              <div class="flex items-center gap-2">
                <span class="text-sm font-semibold text-gray-700">工作中心：</span>
                <span class="px-3 py-1 bg-blue-100 text-blue-700 rounded-lg text-sm font-medium">
                  {{ selectResult?.taskCode }} {{ selectResult?.taskName }}
                </span>
                <!-- 查看工作中心詳情按钮已隐藏 -->
              </div>
            </div>
            <div class="grid grid-cols-2 md:grid-cols-4 gap-3">
              <div v-if="currentTask?.requiresSheetCount" class="bg-white rounded-lg px-3 py-2 border border-gray-200">
                <div class="text-xs text-gray-500 mb-1">印張石數</div>
                <div class="font-semibold text-gray-900">{{ currentTask?.sheetCount || '未填' }}</div>
              </div>
              <div v-if="currentTask?.requiresSize" class="bg-white rounded-lg px-3 py-2 border border-gray-200">
                <div class="text-xs text-gray-500 mb-1">成品尺寸</div>
                <div class="font-semibold text-gray-900">{{ currentTask?.width || '-' }} × {{ currentTask?.height || '-' }} mm</div>
              </div>
              <div v-if="currentTask?.requiresGsm" class="bg-white rounded-lg px-3 py-2 border border-gray-200">
                <div class="text-xs text-gray-500 mb-1">紙張克重</div>
                <div class="font-semibold text-gray-900">{{ currentTask?.gsm ? currentTask.gsm + ' g/m²' : '未填' }}</div>
              </div>
              <div v-if="currentTask?.requiresThickness" class="bg-white rounded-lg px-3 py-2 border border-gray-200">
                <div class="text-xs text-gray-500 mb-1">紙張厚度</div>
                <div class="font-semibold text-gray-900">{{ currentTask?.thickness ? currentTask.thickness + ' mm' : '未填' }}</div>
              </div>
            </div>
          </div>
          
          <!-- 统计信息 -->
          <div class="px-6 py-4 border-b border-gray-100 bg-white flex items-center gap-4">
            <div class="flex items-center gap-2">
              <div class="w-3 h-3 rounded-full bg-green-500"></div>
              <span class="text-sm font-medium text-gray-700">通過</span>
              <span class="px-2 py-0.5 bg-green-100 text-green-700 rounded-full text-sm font-bold">
                {{ selectResult?.passCount || 0 }}
              </span>
            </div>
            <div class="flex items-center gap-2">
              <div class="w-3 h-3 rounded-full bg-yellow-500"></div>
              <span class="text-sm font-medium text-gray-700">待定</span>
              <span class="px-2 py-0.5 bg-yellow-100 text-yellow-700 rounded-full text-sm font-bold">
                {{ selectResult?.unknownCount || 0 }}
              </span>
            </div>
          </div>
          
          <!-- 资源组列表 -->
          <div class="flex-1 overflow-y-auto p-6 bg-gray-50">
            <div class="space-y-4">
              <div
                v-for="candidate in selectResult?.candidates"
                :key="candidate.id"
                class="bg-white rounded-xl border-2 transition-all duration-200 hover:shadow-lg hover:scale-[1.01]"
                :class="{
                  'border-green-300 bg-green-50/30': candidate.matchStatus === 'PASS',
                  'border-red-300 bg-red-50/30': candidate.matchStatus === 'FAIL',
                  'border-yellow-300 bg-yellow-50/30': candidate.matchStatus === 'UNKNOWN'
                }"
              >
                <div class="p-5">
                  <!-- 资源组头部 -->
                  <div class="flex justify-between items-start mb-4">
                    <div class="flex items-start gap-4 flex-1">
                      <!-- 状态图标 -->
                      <div class="flex-shrink-0 mt-1">
                        <div v-if="candidate.matchStatus === 'PASS'" class="w-10 h-10 rounded-full bg-green-100 flex items-center justify-center">
                          <span class="text-xl">✅</span>
                        </div>
                        <div v-else-if="candidate.matchStatus === 'FAIL'" class="w-10 h-10 rounded-full bg-red-100 flex items-center justify-center">
                          <span class="text-xl">❌</span>
                        </div>
                        <div v-else class="w-10 h-10 rounded-full bg-yellow-100 flex items-center justify-center">
                          <span class="text-xl">⚠️</span>
                        </div>
                      </div>
                      
                      <!-- 资源组信息 -->
                      <div class="flex-1">
                        <div class="flex items-center gap-3 mb-2">
                          <span class="text-lg font-bold text-blue-600">{{ candidate.resourceGroupCode }}</span>
                          <span class="text-gray-800 font-medium">{{ candidate.name }}</span>
                          <span v-if="candidate.family" class="px-2.5 py-1 bg-purple-100 text-purple-700 text-xs rounded-full font-medium">
                            {{ candidate.family }}
                          </span>
                        </div>
                        <div class="flex items-center gap-4 text-sm text-gray-600">
                          <span>{{ candidate.workCenterCode }} {{ candidate.workCenterName }}</span>
                          <!-- 查看工作中心詳情按钮已隐藏 -->
                        </div>
                      </div>
                    </div>
                    
                    <!-- 操作按钮 -->
                    <div class="flex gap-2 ml-4">
                      <button
                        @click.stop="showResourceGroupDetail(candidate)"
                        class="px-4 py-2 bg-white border border-gray-300 text-gray-700 rounded-lg hover:bg-gray-50 hover:border-gray-400 text-sm font-medium transition-colors flex items-center gap-2"
                      >
                        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><circle cx="12" cy="12" r="10"/><line x1="12" y1="16" x2="12" y2="12"/><line x1="12" y1="8" x2="12.01" y2="8"/></svg>
                        詳情
                      </button>
                      <button
                        @click.stop="showRuleText(candidate)"
                        class="px-4 py-2 bg-blue-50 border border-blue-200 text-blue-700 rounded-lg hover:bg-blue-100 text-sm font-medium transition-colors"
                      >
                        規則
                      </button>
                      <button
                        @click.stop="selectResourceGroup(candidate)"
                        :disabled="candidate.matchStatus === 'FAIL'"
                        class="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 disabled:bg-gray-300 disabled:cursor-not-allowed text-sm font-medium transition-colors shadow-sm"
                      >
                        選擇
                      </button>
                    </div>
                  </div>

                  <!-- 可上机原因和不可上机原因（左右布局） -->
                  <div class="mt-4 pt-4 border-t border-gray-200">
                    <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                      <!-- 可上机原因（左侧） -->
                      <div v-if="candidate.ruleTextAllow" class="bg-green-50 rounded-lg p-4 border border-green-200 h-full">
                        <div class="flex items-center gap-2 mb-3">
                          <span class="text-green-600 font-semibold text-base">✅ 可上機原因</span>
                        </div>
                        <pre class="whitespace-pre-wrap text-sm text-gray-700 leading-relaxed max-h-48 overflow-y-auto">{{ candidate.ruleTextAllow }}</pre>
                      </div>
                      <div v-else class="bg-gray-50 rounded-lg p-4 border border-gray-200 h-full flex items-center justify-center">
                        <span class="text-sm text-gray-400 italic">無可上機原因</span>
                      </div>
                      
                      <!-- 不可上机原因（右侧） -->
                      <div v-if="candidate.ruleTextBlock" class="bg-red-50 rounded-lg p-4 border border-red-200 h-full">
                        <div class="flex items-center gap-2 mb-3">
                          <span class="text-red-600 font-semibold text-base">❌ 暫不上機原因</span>
                        </div>
                        <pre class="whitespace-pre-wrap text-sm text-gray-700 leading-relaxed max-h-48 overflow-y-auto">{{ candidate.ruleTextBlock }}</pre>
                      </div>
                      <div v-else class="bg-gray-50 rounded-lg p-4 border border-gray-200 h-full flex items-center justify-center">
                        <span class="text-sm text-gray-400 italic">無暫不上機原因</span>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
              
              <!-- 无结果 -->
              <div v-if="!selectResult?.candidates?.length" class="text-center py-16">
                <div class="text-gray-400 text-6xl mb-4">🔍</div>
                <div class="text-gray-500 text-lg font-medium">沒有找到支持該任務的資源組</div>
                <div class="text-gray-400 text-sm mt-2">請檢查輸入的參數或聯繫管理員</div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 资源组详情弹窗 -->
      <div
        v-if="showResourceGroupDetailDialog"
        class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-[60] backdrop-blur-sm"
        @click.self="showResourceGroupDetailDialog = false"
      >
        <div class="bg-white rounded-2xl shadow-2xl w-full max-w-4xl max-h-[90vh] overflow-hidden flex flex-col">
          <div class="px-6 py-5 border-b border-gray-200 bg-gradient-to-r from-indigo-50 to-purple-50 flex justify-between items-center">
            <div>
              <h3 class="text-xl font-bold text-gray-900 mb-1">
                📋 資源組詳情
              </h3>
              <p class="text-sm text-gray-600">
                {{ currentResourceGroupDetail?.resourceGroupCode }} {{ currentResourceGroupDetail?.name }}
              </p>
            </div>
            <button @click="showResourceGroupDetailDialog = false" class="text-gray-400 hover:text-gray-600 hover:bg-white rounded-full p-2 transition-colors">
              <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><line x1="18" y1="6" x2="6" y2="18"/><line x1="6" y1="6" x2="18" y2="18"/></svg>
            </button>
          </div>
          
          <div class="flex-1 overflow-y-auto p-6">
            <!-- 这里的内容和规则弹窗中的资源组详情部分相同，但更优化 -->
            <div class="space-y-6">
              <!-- 基本信息 -->
              <div class="bg-gray-50 rounded-xl p-5 border border-gray-200">
                <h4 class="text-lg font-semibold text-gray-800 mb-4 flex items-center gap-2">
                  <span>📋</span> 基本信息
                </h4>
                <div class="grid grid-cols-2 md:grid-cols-3 gap-4">
                  <div>
                    <div class="text-xs text-gray-500 mb-1">資源組編碼</div>
                    <div class="font-semibold text-gray-900">{{ currentResourceGroupDetail?.resourceGroupCode }}</div>
                  </div>
                  <div>
                    <div class="text-xs text-gray-500 mb-1">資源組名稱</div>
                    <div class="font-semibold text-gray-900">{{ currentResourceGroupDetail?.name }}</div>
                  </div>
                  <div v-if="currentResourceGroupDetail?.family">
                    <div class="text-xs text-gray-500 mb-1">家族</div>
                    <div class="px-2 py-1 bg-purple-100 text-purple-700 rounded text-sm font-medium inline-block">{{ currentResourceGroupDetail.family }}</div>
                  </div>
                </div>
              </div>

              <!-- 工作中心信息 -->
              <div v-if="currentResourceGroupDetail?.workCenterDetail" class="bg-blue-50 rounded-xl p-5 border border-blue-200">
                <div class="flex items-center justify-between mb-4">
                  <h4 class="text-lg font-semibold text-blue-800 flex items-center gap-2">
                    <span>🏭</span> 工作中心信息
                  </h4>
                  <button
                    @click="showWorkCenterDetail(currentResourceGroupDetail.workCenterDetail.workCenterCode)"
                    class="text-sm text-blue-600 hover:text-blue-800 hover:underline"
                  >
                    查看完整詳情 →
                  </button>
                </div>
                <div class="grid grid-cols-2 md:grid-cols-3 gap-4">
                  <div>
                    <div class="text-xs text-gray-600 mb-1">工作中心編碼</div>
                    <div class="font-semibold text-gray-900">{{ currentResourceGroupDetail.workCenterDetail.workCenterCode }}</div>
                  </div>
                  <div>
                    <div class="text-xs text-gray-600 mb-1">工作中心名稱</div>
                    <div class="font-semibold text-gray-900">{{ currentResourceGroupDetail.workCenterDetail.workCenterName }}</div>
                  </div>
                  <div v-if="currentResourceGroupDetail.workCenterDetail.departmentName">
                    <div class="text-xs text-gray-600 mb-1">所屬部門</div>
                    <div class="font-semibold text-gray-900">{{ currentResourceGroupDetail.workCenterDetail.departmentName }}</div>
                  </div>
                </div>
              </div>

              <!-- 技术参数 -->
              <div v-if="currentResourceGroupDetail?.techDetail" class="bg-indigo-50 rounded-xl p-5 border border-indigo-200">
                <h4 class="text-lg font-semibold text-indigo-800 mb-4 flex items-center gap-2">
                  <span>⚙️</span> 技術參數
                </h4>
                <div class="grid grid-cols-2 md:grid-cols-3 gap-4">
                  <div v-if="currentResourceGroupDetail.techDetail.maxWidthMm || currentResourceGroupDetail.techDetail.maxHeightMm" class="bg-white rounded-lg p-3 border border-indigo-100">
                    <div class="text-xs text-gray-500 mb-1">最大尺寸</div>
                    <div class="font-semibold text-gray-900">{{ currentResourceGroupDetail.techDetail.maxWidthMm ?? '-' }} × {{ currentResourceGroupDetail.techDetail.maxHeightMm ?? '-' }} mm</div>
                  </div>
                  <div v-if="currentResourceGroupDetail.techDetail.minWidthMm || currentResourceGroupDetail.techDetail.minHeightMm" class="bg-white rounded-lg p-3 border border-indigo-100">
                    <div class="text-xs text-gray-500 mb-1">最小尺寸</div>
                    <div class="font-semibold text-gray-900">{{ currentResourceGroupDetail.techDetail.minWidthMm ?? '-' }} × {{ currentResourceGroupDetail.techDetail.minHeightMm ?? '-' }} mm</div>
                  </div>
                  <div v-if="currentResourceGroupDetail.techDetail.minThicknessMm || currentResourceGroupDetail.techDetail.maxThicknessMm" class="bg-white rounded-lg p-3 border border-indigo-100">
                    <div class="text-xs text-gray-500 mb-1">厚度範圍</div>
                    <div class="font-semibold text-gray-900">{{ currentResourceGroupDetail.techDetail.minThicknessMm ?? '-' }} ~ {{ currentResourceGroupDetail.techDetail.maxThicknessMm ?? '-' }} mm</div>
                  </div>
                  <div v-if="currentResourceGroupDetail.techDetail.minGsm || currentResourceGroupDetail.techDetail.maxGsm" class="bg-white rounded-lg p-3 border border-indigo-100">
                    <div class="text-xs text-gray-500 mb-1">克重範圍</div>
                    <div class="font-semibold text-gray-900">{{ currentResourceGroupDetail.techDetail.minGsm ?? '-' }} ~ {{ currentResourceGroupDetail.techDetail.maxGsm ?? '-' }} g/m²</div>
                  </div>
                  <div v-if="currentResourceGroupDetail.techDetail.maxSpeedSheetPerHour" class="bg-white rounded-lg p-3 border border-indigo-100">
                    <div class="text-xs text-gray-500 mb-1">最高速度</div>
                    <div class="font-semibold text-gray-900">{{ currentResourceGroupDetail.techDetail.maxSpeedSheetPerHour }} 張/小時</div>
                  </div>
                  <div v-if="currentResourceGroupDetail.techDetail.setupTimeMin" class="bg-white rounded-lg p-3 border border-indigo-100">
                    <div class="text-xs text-gray-500 mb-1">換單時間</div>
                    <div class="font-semibold text-gray-900">{{ currentResourceGroupDetail.techDetail.setupTimeMin }} 分鐘</div>
                  </div>
                  <div v-if="currentResourceGroupDetail.techDetail.machineModel" class="bg-white rounded-lg p-3 border border-indigo-100">
                    <div class="text-xs text-gray-500 mb-1">機型型號</div>
                    <div class="font-semibold text-gray-900">{{ currentResourceGroupDetail.techDetail.machineModel }}</div>
                  </div>
                  <div v-if="currentResourceGroupDetail.techDetail.manufacturer" class="bg-white rounded-lg p-3 border border-indigo-100">
                    <div class="text-xs text-gray-500 mb-1">設備廠家</div>
                    <div class="font-semibold text-gray-900">{{ currentResourceGroupDetail.techDetail.manufacturer }}</div>
                  </div>
                </div>
              </div>

              <!-- 产能信息 -->
              <div v-if="currentResourceGroupDetail?.capacityList?.length" class="bg-purple-50 rounded-xl p-5 border border-purple-200">
                <h4 class="text-lg font-semibold text-purple-800 mb-4 flex items-center gap-2">
                  <span>⚡</span> 產能信息
                </h4>
                <div class="bg-white rounded-lg overflow-hidden border border-purple-100">
                  <table class="w-full text-sm">
                    <thead class="bg-purple-100">
                      <tr>
                        <th class="px-4 py-3 text-left text-gray-700 font-semibold">模式</th>
                        <th class="px-4 py-3 text-left text-gray-700 font-semibold">班次</th>
                        <th class="px-4 py-3 text-center text-gray-700 font-semibold">產能(張/時)</th>
                        <th class="px-4 py-3 text-center text-gray-700 font-semibold">產能(張/天)</th>
                        <th class="px-4 py-3 text-center text-gray-700 font-semibold">稼動率</th>
                      </tr>
                    </thead>
                    <tbody>
                      <tr v-for="cap in currentResourceGroupDetail.capacityList" :key="cap.id" class="border-b border-gray-100 hover:bg-purple-50">
                        <td class="px-4 py-3 font-medium">{{ cap.capacityMode }}</td>
                        <td class="px-4 py-3">{{ cap.shiftName || '-' }}</td>
                        <td class="px-4 py-3 text-center">{{ cap.capacitySheetPerHour ?? '-' }}</td>
                        <td class="px-4 py-3 text-center">{{ cap.capacitySheetPerDay ?? '-' }}</td>
                        <td class="px-4 py-3 text-center">{{ cap.utilizationRate ? cap.utilizationRate + '%' : '-' }}</td>
                      </tr>
                    </tbody>
                  </table>
                </div>
              </div>

              <!-- 规则信息 -->
              <div class="bg-green-50 rounded-xl p-5 border border-green-200">
                <h4 class="text-lg font-semibold text-green-800 mb-4 flex items-center gap-2">
                  <span>✅</span> 可上機規則
                </h4>
                <pre class="whitespace-pre-wrap text-gray-700 bg-white p-4 rounded-lg text-sm border border-green-100">{{ currentResourceGroupDetail?.ruleTextAllow || '無' }}</pre>
              </div>
              
              <div v-if="currentResourceGroupDetail?.ruleTextBlock" class="bg-red-50 rounded-xl p-5 border border-red-200">
                <h4 class="text-lg font-semibold text-red-800 mb-4 flex items-center gap-2">
                  <span>❌</span> 暫不上機原因
                </h4>
                <pre class="whitespace-pre-wrap text-gray-700 bg-white p-4 rounded-lg text-sm border border-red-100">{{ currentResourceGroupDetail.ruleTextBlock }}</pre>
              </div>
            </div>
          </div>
          
          <div class="px-6 py-4 border-t border-gray-200 bg-gray-50 flex justify-end">
            <button @click="showResourceGroupDetailDialog = false" class="px-6 py-2 bg-gray-200 text-gray-700 rounded-lg hover:bg-gray-300 font-medium transition-colors">關閉</button>
          </div>
        </div>
      </div>

      <!-- 工作中心详情弹窗 -->
      <div
        v-if="showWorkCenterDetailDialog"
        class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-[70] backdrop-blur-sm"
        @click.self="showWorkCenterDetailDialog = false"
      >
        <div class="bg-white rounded-2xl shadow-2xl w-full max-w-5xl max-h-[90vh] overflow-hidden flex flex-col">
          <div class="px-6 py-5 border-b border-gray-200 bg-gradient-to-r from-blue-50 to-cyan-50 flex justify-between items-center">
            <div>
              <h3 class="text-xl font-bold text-gray-900 mb-1">
                🏭 工作中心詳情
              </h3>
              <p class="text-sm text-gray-600">
                {{ currentWorkCenterDetail?.workCenterCode }} {{ currentWorkCenterDetail?.workCenterName }}
              </p>
            </div>
            <button @click="showWorkCenterDetailDialog = false" class="text-gray-400 hover:text-gray-600 hover:bg-white rounded-full p-2 transition-colors">
              <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><line x1="18" y1="6" x2="6" y2="18"/><line x1="6" y1="6" x2="18" y2="18"/></svg>
            </button>
          </div>
          
          <div class="flex-1 overflow-y-auto p-6">
            <div class="space-y-6">
              <!-- 工作中心基本信息 -->
              <div class="bg-blue-50 rounded-xl p-5 border border-blue-200">
                <h4 class="text-lg font-semibold text-blue-800 mb-4 flex items-center gap-2">
                  <span>📋</span> 基本信息
                </h4>
                <div class="grid grid-cols-2 md:grid-cols-3 gap-4">
                  <div>
                    <div class="text-xs text-gray-600 mb-1">工作中心編碼</div>
                    <div class="font-semibold text-gray-900">{{ currentWorkCenterDetail?.workCenterCode }}</div>
                  </div>
                  <div>
                    <div class="text-xs text-gray-600 mb-1">工作中心名稱</div>
                    <div class="font-semibold text-gray-900">{{ currentWorkCenterDetail?.workCenterName }}</div>
                  </div>
                  <div v-if="currentWorkCenterDetail?.departmentName">
                    <div class="text-xs text-gray-600 mb-1">所屬部門</div>
                    <div class="font-semibold text-gray-900">{{ currentWorkCenterDetail.departmentName }}</div>
                  </div>
                  <div v-if="currentWorkCenterDetail?.description" class="col-span-full">
                    <div class="text-xs text-gray-600 mb-1">說明</div>
                    <div class="font-medium text-gray-900">{{ currentWorkCenterDetail.description }}</div>
                  </div>
                </div>
              </div>

              <!-- 该工作中心下的所有资源组 -->
              <div class="bg-gray-50 rounded-xl p-5 border border-gray-200">
                <h4 class="text-lg font-semibold text-gray-800 mb-4 flex items-center gap-2">
                  <span>⚙️</span> 資源組列表 ({{ workCenterResourceGroups?.length || 0 }})
                </h4>
                <div v-if="workCenterResourceGroups && workCenterResourceGroups.length > 0" class="space-y-3">
                  <div
                    v-for="rg in workCenterResourceGroups"
                    :key="rg.id"
                    class="bg-white rounded-lg p-4 border border-gray-200 hover:border-blue-300 hover:shadow-md transition-all cursor-pointer"
                    @click="showResourceGroupDetailFromList(rg)"
                  >
                    <div class="flex items-center justify-between">
                      <div>
                        <div class="flex items-center gap-3 mb-2">
                          <span class="font-bold text-blue-600">{{ rg.resourceGroupCode }}</span>
                          <span class="text-gray-800 font-medium">{{ rg.name }}</span>
                          <span v-if="rg.family" class="px-2 py-0.5 bg-purple-100 text-purple-700 text-xs rounded-full">
                            {{ rg.family }}
                          </span>
                        </div>
                      </div>
                      <button
                        @click.stop="showResourceGroupDetailFromList(rg)"
                        class="px-3 py-1.5 bg-blue-50 text-blue-600 rounded-lg hover:bg-blue-100 text-sm font-medium transition-colors"
                      >
                        查看詳情
                      </button>
                    </div>
                  </div>
                </div>
                <div v-else class="text-center py-8 text-gray-400">
                  <div class="text-4xl mb-2">📦</div>
                  <div>該工作中心暫無資源組</div>
                </div>
              </div>
            </div>
          </div>
          
          <div class="px-6 py-4 border-t border-gray-200 bg-gray-50 flex justify-end">
            <button @click="showWorkCenterDetailDialog = false" class="px-6 py-2 bg-gray-200 text-gray-700 rounded-lg hover:bg-gray-300 font-medium transition-colors">關閉</button>
          </div>
        </div>
      </div>

      <!-- 规则原文弹窗（含详细信息） -->
      <div
        v-if="showRuleDialog"
        class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-[50] backdrop-blur-sm"
        @click.self="showRuleDialog = false"
      >
        <div class="bg-white rounded-2xl shadow-2xl w-full max-w-3xl max-h-[85vh] overflow-hidden flex flex-col">
          <div class="px-6 py-5 border-b border-gray-200 bg-gradient-to-r from-green-50 to-emerald-50 flex justify-between items-center">
            <div>
              <h3 class="text-xl font-bold text-gray-900 mb-1">
                📜 規則說明
              </h3>
              <p class="text-sm text-gray-600">
                {{ currentCandidate?.resourceGroupCode }} {{ currentCandidate?.name }}
              </p>
            </div>
            <button @click="showRuleDialog = false" class="text-gray-400 hover:text-gray-600 hover:bg-white rounded-full p-2 transition-colors">
              <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><line x1="18" y1="6" x2="6" y2="18"/><line x1="6" y1="6" x2="18" y2="18"/></svg>
            </button>
          </div>
          
          <div class="p-6 overflow-y-auto max-h-[70vh]">
            <!-- 工作中心信息 -->
            <div class="mb-6" v-if="currentCandidate?.workCenterDetail">
              <h4 class="text-blue-600 font-semibold mb-3">🏭 工作中心信息</h4>
              <div class="bg-blue-50 p-4 rounded-lg">
                <div class="grid grid-cols-2 md:grid-cols-3 gap-4 text-sm">
                  <div>
                    <span class="text-gray-500">工作中心編碼：</span>
                    <span class="font-medium">{{ currentCandidate.workCenterDetail.workCenterCode }}</span>
                  </div>
                  <div>
                    <span class="text-gray-500">工作中心名稱：</span>
                    <span class="font-medium">{{ currentCandidate.workCenterDetail.workCenterName }}</span>
                  </div>
                  <div v-if="currentCandidate.workCenterDetail.departmentName">
                    <span class="text-gray-500">所屬部門：</span>
                    <span class="font-medium">{{ currentCandidate.workCenterDetail.departmentName }}</span>
                  </div>
                  <div v-if="currentCandidate.workCenterDetail.description" class="col-span-full">
                    <span class="text-gray-500">說明：</span>
                    <span class="font-medium">{{ currentCandidate.workCenterDetail.description }}</span>
                  </div>
                </div>
              </div>
            </div>
            
            <!-- 技术参数详情 -->
            <div class="mb-6" v-if="currentCandidate?.techDetail">
              <h4 class="text-indigo-600 font-semibold mb-3">⚙️ 技術參數</h4>
              <div class="bg-indigo-50 p-4 rounded-lg">
                <div class="grid grid-cols-2 md:grid-cols-3 gap-4 text-sm">
                  <!-- 尺寸能力 -->
                  <div v-if="currentCandidate.techDetail.maxWidthMm || currentCandidate.techDetail.maxHeightMm">
                    <span class="text-gray-500">最大尺寸：</span>
                    <span class="font-medium">{{ currentCandidate.techDetail.maxWidthMm ?? '-' }} × {{ currentCandidate.techDetail.maxHeightMm ?? '-' }} mm</span>
                  </div>
                  <div v-if="currentCandidate.techDetail.minWidthMm || currentCandidate.techDetail.minHeightMm">
                    <span class="text-gray-500">最小尺寸：</span>
                    <span class="font-medium">{{ currentCandidate.techDetail.minWidthMm ?? '-' }} × {{ currentCandidate.techDetail.minHeightMm ?? '-' }} mm</span>
                  </div>
                  <!-- 厚度/克重 -->
                  <div v-if="currentCandidate.techDetail.minThicknessMm || currentCandidate.techDetail.maxThicknessMm">
                    <span class="text-gray-500">厚度範圍：</span>
                    <span class="font-medium">{{ currentCandidate.techDetail.minThicknessMm ?? '-' }} ~ {{ currentCandidate.techDetail.maxThicknessMm ?? '-' }} mm</span>
                  </div>
                  <div v-if="currentCandidate.techDetail.minGsm || currentCandidate.techDetail.maxGsm">
                    <span class="text-gray-500">克重範圍：</span>
                    <span class="font-medium">{{ currentCandidate.techDetail.minGsm ?? '-' }} ~ {{ currentCandidate.techDetail.maxGsm ?? '-' }} g/m²</span>
                  </div>
                  <!-- 速度 -->
                  <div v-if="currentCandidate.techDetail.maxSpeedSheetPerHour">
                    <span class="text-gray-500">最高速度：</span>
                    <span class="font-medium">{{ currentCandidate.techDetail.maxSpeedSheetPerHour }} 張/小時</span>
                  </div>
                  <div v-if="currentCandidate.techDetail.setupTimeMin">
                    <span class="text-gray-500">換單時間：</span>
                    <span class="font-medium">{{ currentCandidate.techDetail.setupTimeMin }} 分鐘</span>
                  </div>
                  <!-- 设备信息 -->
                  <div v-if="currentCandidate.techDetail.machineModel">
                    <span class="text-gray-500">機型型號：</span>
                    <span class="font-medium">{{ currentCandidate.techDetail.machineModel }}</span>
                  </div>
                  <div v-if="currentCandidate.techDetail.manufacturer">
                    <span class="text-gray-500">設備廠家：</span>
                    <span class="font-medium">{{ currentCandidate.techDetail.manufacturer }}</span>
                  </div>
                  <!-- 能力说明 -->
                  <div v-if="currentCandidate.techDetail.capabilityRemark" class="col-span-full">
                    <span class="text-gray-500">能力說明：</span>
                    <span class="font-medium">{{ currentCandidate.techDetail.capabilityRemark }}</span>
                  </div>
                </div>
              </div>
            </div>
            
            <!-- 产能信息 -->
            <div class="mb-6" v-if="currentCandidate?.capacityList?.length">
              <h4 class="text-purple-600 font-semibold mb-3">⚡ 產能信息</h4>
              <div class="bg-purple-50 p-4 rounded-lg">
                <table class="w-full text-sm">
                  <thead>
                    <tr class="border-b">
                      <th class="py-2 text-left text-gray-600">模式</th>
                      <th class="py-2 text-left text-gray-600">班次</th>
                      <th class="py-2 text-center text-gray-600">產能(張/時)</th>
                      <th class="py-2 text-center text-gray-600">產能(張/天)</th>
                      <th class="py-2 text-center text-gray-600">稼動率</th>
                    </tr>
                  </thead>
                  <tbody>
                    <tr v-for="cap in currentCandidate.capacityList" :key="cap.id" class="border-b">
                      <td class="py-2 font-medium">{{ cap.capacityMode }}</td>
                      <td class="py-2">{{ cap.shiftName || '-' }}</td>
                      <td class="py-2 text-center">{{ cap.capacitySheetPerHour ?? '-' }}</td>
                      <td class="py-2 text-center">{{ cap.capacitySheetPerDay ?? '-' }}</td>
                      <td class="py-2 text-center">{{ cap.utilizationRate ? cap.utilizationRate + '%' : '-' }}</td>
                    </tr>
                  </tbody>
                </table>
              </div>
            </div>
            
            <!-- 基本信息 -->
            <div class="mb-6">
              <h4 class="text-gray-600 font-semibold mb-3">📋 基本信息</h4>
              <div class="bg-gray-50 p-4 rounded-lg">
                <div class="grid grid-cols-2 md:grid-cols-3 gap-4 text-sm">
                  <div>
                    <span class="text-gray-500">資源組編碼：</span>
                    <span class="font-medium">{{ currentCandidate?.resourceGroupCode }}</span>
                  </div>
                  <div>
                    <span class="text-gray-500">資源組名稱：</span>
                    <span class="font-medium">{{ currentCandidate?.name }}</span>
                  </div>
                  <div v-if="currentCandidate?.family">
                    <span class="text-gray-500">家族：</span>
                    <span class="font-medium px-2 py-0.5 bg-purple-100 text-purple-700 rounded">{{ currentCandidate.family }}</span>
                  </div>
                </div>
              </div>
            </div>
            
            <!-- 可上机规则 -->
            <div class="mb-6">
              <h4 class="text-green-600 font-semibold mb-3">✅ 可上機規則</h4>
              <pre class="whitespace-pre-wrap text-gray-700 bg-green-50 p-4 rounded-lg text-sm">{{ currentCandidate?.ruleTextAllow || '無' }}</pre>
            </div>
            
            <!-- 暂不上机原因 -->
            <div v-if="currentCandidate?.ruleTextBlock">
              <h4 class="text-red-600 font-semibold mb-3">❌ 暫不上機原因</h4>
              <pre class="whitespace-pre-wrap text-gray-700 bg-red-50 p-4 rounded-lg text-sm">{{ currentCandidate?.ruleTextBlock }}</pre>
            </div>
          </div>
          
          <div class="px-6 py-4 border-t bg-gray-50 flex justify-end">
            <button @click="showRuleDialog = false" class="px-6 py-2 bg-gray-200 text-gray-700 rounded hover:bg-gray-300">關閉</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import axios from 'axios'

// 类型定义
interface Task {
  taskCode: string
  taskName: string
}

interface TaskItem {
  id: number
  seqNo: number
  taskCode: string
  taskDesc: string
  remark: string
  workCenterCode: string
  selectedResourceGroup: Candidate | null
  resourceSource: string
  // 任务级参数
  width: number | null
  height: number | null
  sheetCount: number | null
  gsm: number | null
  thickness: number | null
  // 是否需要填写这些参数（根据规则动态判断）
  requiresSize: boolean
  requiresSheetCount: boolean
  requiresGsm: boolean
  requiresThickness: boolean
}

interface MatchDetail {
  paramName: string
  paramCode: string
  inputValue: string
  ruleValue: string
  status: string
}

// 工作中心详情
interface WorkCenterDetail {
  id: number
  workCenterCode: string
  workCenterName: string
  departmentName: string | null
  description: string | null
  remark: string | null
}

// 技术参数详情
interface TechDetail {
  id: number
  resourceGroupId: number
  maxWidthMm: number | null
  maxHeightMm: number | null
  minWidthMm: number | null
  minHeightMm: number | null
  minThicknessMm: number | null
  maxThicknessMm: number | null
  minGsm: number | null
  maxGsm: number | null
  minSpeedSheetPerHour: number | null
  maxSpeedSheetPerHour: number | null
  setupTimeMin: number | null
  machineModel: string | null
  manufacturer: string | null
  yearOfMake: string | null
  capabilityRemark: string | null
}

// 产能信息
interface CapacityInfo {
  id: number
  resourceGroupId: number
  capacityMode: string
  shiftName: string | null
  workHoursPerShift: number | null
  shiftsPerDay: number | null
  capacitySheetPerHour: number | null
  capacitySqmPerHour: number | null
  capacitySheetPerDay: number | null
  capacitySqmPerDay: number | null
  utilizationRate: number | null
  remark: string | null
}

interface Candidate {
  id: number
  resourceGroupCode: string
  name: string
  workCenterCode: string
  workCenterName: string
  family: string | null
  matchStatus: string
  matchDetails: MatchDetail[]
  ruleTextAllow: string
  ruleTextBlock: string
  // 关联详情
  workCenterDetail: WorkCenterDetail | null
  techDetail: TechDetail | null
  capacityList: CapacityInfo[]
}

interface SelectResult {
  taskCode: string
  taskName: string
  candidates: Candidate[]
  passCount: number
  failCount: number
  unknownCount: number
}

// 公共参数（纸张相关的通用参数）
const commonParams = ref({
  department: '',               // 事業部
  productType: '',              // 產品類型（盒类/贺咭/拼图/信封/其他）
  paper: '',                    // 用紙類型
  suitableSurface: '',          // 適用界面（光面/啞面/特種/其他）
  selectedSheetCount: null as number | null,  // 選紙張數
  grainDirection: ''            // 紋路方向（长纹/短纹/無紋）
})

// 公共参数下拉选项（从规则中推断）
const paramOptions = ref<{
  departments: string[]
  productTypes: string[]
  surfaceTypes: string[]
  grainDirections: string[]
}>({
  departments: [],
  productTypes: [],
  surfaceTypes: [],
  grainDirections: []
})

// 任务列表
const tasks = ref<Task[]>([])
const taskList = ref<TaskItem[]>([])
let taskIdCounter = 1

// 弹窗状态
const showResourceDialog = ref(false)
const showRuleDialog = ref(false)
const showResourceGroupDetailDialog = ref(false)
const showWorkCenterDetailDialog = ref(false)
const currentTask = ref<TaskItem | null>(null)
const currentCandidate = ref<Candidate | null>(null)
const currentResourceGroupDetail = ref<Candidate | null>(null)
const currentWorkCenterDetail = ref<WorkCenterDetail | null>(null)
const workCenterResourceGroups = ref<Candidate[]>([])
const selectResult = ref<SelectResult | null>(null)

// 拖拽状态
let dragIndex = -1

// 工艺流程预览
const taskFlowPreview = computed(() => {
  return taskList.value
    .filter(t => t.taskCode)
    .map(t => getTaskName(t.taskCode))
    .join(' → ') || '-'
})

// 获取任务名称
const getTaskName = (taskCode: string | undefined) => {
  if (!taskCode) return ''
  const task = tasks.value.find(t => t.taskCode === taskCode)
  return task?.taskName || taskCode
}

// 获取任务列表
const fetchTasks = async () => {
  try {
    const res = await axios.get('/api/resource-group-select/tasks')
    if (res.data.success) {
      tasks.value = res.data.data
    }
  } catch (error) {
    console.error('獲取任務列表失敗', error)
  }
}

// 获取公共参数下拉选项（从规则中推断）
const fetchParamOptions = async () => {
  try {
    const res = await axios.get('/api/resource-group-select/param-options')
    if (res.data.success) {
      paramOptions.value = res.data.data
    }
  } catch (error) {
    console.error('獲取參數選項失敗', error)
  }
}

// 添加任务
const addTask = () => {
  const newSeqNo = (taskList.value.length + 1) * 10
  taskList.value.push({
    id: taskIdCounter++,
    seqNo: newSeqNo,
    taskCode: '',
    taskDesc: '',
    remark: '',
    workCenterCode: '',
    selectedResourceGroup: null,
    resourceSource: '',
    width: null,
    height: null,
    sheetCount: null,
    gsm: null,
    thickness: null,
    requiresSize: false,
    requiresSheetCount: false,
    requiresGsm: false,
    requiresThickness: false
  })
}

// 插入任务
const insertTaskBefore = (index: number) => {
  const newSeqNo = taskList.value[index]?.seqNo - 5 || 5
  taskList.value.splice(index, 0, {
    id: taskIdCounter++,
    seqNo: newSeqNo,
    taskCode: '',
    taskDesc: '',
    remark: '',
    workCenterCode: '',
    selectedResourceGroup: null,
    resourceSource: '',
    width: null,
    height: null,
    sheetCount: null,
    gsm: null,
    thickness: null,
    requiresSize: false,
    requiresSheetCount: false,
    requiresGsm: false,
    requiresThickness: false
  })
}

// 删除任务
const removeTask = (index: number) => {
  taskList.value.splice(index, 1)
}

// 判断任务是否已被选中（当前行除外）
const isTaskSelected = (taskCode: string, currentTask: TaskItem) => {
  return taskList.value.some(t => t.taskCode === taskCode && t !== currentTask)
}

// 任务类型变更 - 查询该任务需要哪些参数
const onTaskTypeChange = async (task: TaskItem) => {
  task.selectedResourceGroup = null
  task.workCenterCode = ''
  task.resourceSource = ''
  task.requiresSize = false
  task.requiresSheetCount = false
  task.requiresGsm = false
  task.requiresThickness = false
  
  if (!task.taskCode) return
  
  try {
    // 查询该任务对应的资源组规则，判断需要哪些参数
    const res = await axios.get(`/api/resource-group-select/task-params/${task.taskCode}`)
    if (res.data.success) {
      task.requiresSize = res.data.data.requiresSize
      task.requiresSheetCount = res.data.data.requiresSheetCount
      task.requiresGsm = res.data.data.requiresGsm
      task.requiresThickness = res.data.data.requiresThickness
    }
  } catch (error) {
    console.error('獲取任務參數需求失敗', error)
  }
}

// 拖拽开始
const onDragStart = (index: number) => {
  dragIndex = index
}

// 拖拽放下
const onDrop = (index: number) => {
  if (dragIndex === -1 || dragIndex === index) return
  const item = taskList.value.splice(dragIndex, 1)[0]
  taskList.value.splice(index, 0, item)
  // 重新排序序号
  taskList.value.forEach((t, i) => {
    t.seqNo = (i + 1) * 10
  })
  dragIndex = -1
}

// 打开参数编辑器
const openParamEditor = (task: TaskItem) => {
  // TODO: 打开参数编辑弹窗
  alert('參數編輯功能開發中...')
}

// 自动匹配资源组
const autoMatchResourceGroup = async (task: TaskItem) => {
  if (!task.taskCode) {
    alert('請先選擇工藝類型')
    return
  }
  
  currentTask.value = task
  
  try {
    const workflowTaskCodes = taskList.value
      .map(t => t.taskCode)
      .filter((code): code is string => Boolean(code))
    
    const res = await axios.post('/api/resource-group-select/select', {
      taskCode: task.taskCode,
      // 任务级参数
      sheetCount: task.sheetCount,
      width: task.width,
      height: task.height,
      gsm: task.gsm,
      thickness: task.thickness,
      // 公共参数
      department: commonParams.value.department,
      productType: commonParams.value.productType,
      suitableSurface: commonParams.value.suitableSurface,
      selectedSheetCount: commonParams.value.selectedSheetCount,
      grainDirection: commonParams.value.grainDirection,
      workflowTaskCodes
    })
    
    if (res.data.success) {
      selectResult.value = res.data.data
      showResourceDialog.value = true
    }
  } catch (error) {
    console.error('匹配失敗', error)
    alert('匹配失敗，請稍後重試')
  }
}

// 选择资源组
const selectResourceGroup = (candidate: Candidate) => {
  if (currentTask.value) {
    currentTask.value.selectedResourceGroup = candidate
    currentTask.value.workCenterCode = candidate.workCenterCode
    currentTask.value.resourceSource = '自動匹配'
  }
  showResourceDialog.value = false
}

// 显示规则原文
const showRuleText = (candidate: Candidate) => {
  currentCandidate.value = candidate
  showRuleDialog.value = true
}

// 显示资源组详情
const showResourceGroupDetail = (candidate: Candidate) => {
  currentResourceGroupDetail.value = candidate
  showResourceGroupDetailDialog.value = true
}

// 从工作中心资源组列表显示资源组详情
const showResourceGroupDetailFromList = async (rg: any) => {
  try {
    // 获取资源组的完整信息（包括详情）
    const res = await axios.post('/api/resource-group-select/select', {
      taskCode: '', // 不需要任务代码，只需要资源组信息
      resourceGroupId: rg.id
    })
    if (res.data.success && res.data.data.candidates && res.data.data.candidates.length > 0) {
      currentResourceGroupDetail.value = res.data.data.candidates[0]
      showResourceGroupDetailDialog.value = true
    } else {
      // 如果接口不支持，直接使用基本信息
      currentResourceGroupDetail.value = {
        ...rg,
        workCenterDetail: currentWorkCenterDetail.value,
        techDetail: null,
        capacityList: [],
        ruleTextAllow: '',
        ruleTextBlock: ''
      } as Candidate
      showResourceGroupDetailDialog.value = true
    }
  } catch (error) {
    console.error('獲取資源組詳情失敗', error)
    // 使用基本信息
    currentResourceGroupDetail.value = {
      ...rg,
      workCenterDetail: currentWorkCenterDetail.value,
      techDetail: null,
      capacityList: [],
      ruleTextAllow: '',
      ruleTextBlock: ''
    } as Candidate
    showResourceGroupDetailDialog.value = true
  }
}

// 显示工作中心详情
const showWorkCenterDetail = async (workCenterCode: string) => {
  try {
    // 获取工作中心详情
    const res = await axios.get(`/api/resource-group-select/work-center/${workCenterCode}`)
    if (res.data.success) {
      currentWorkCenterDetail.value = res.data.data.workCenter
      workCenterResourceGroups.value = res.data.data.resourceGroups || []
      showWorkCenterDetailDialog.value = true
    }
  } catch (error) {
    console.error('獲取工作中心詳情失敗', error)
    // 如果接口不存在，尝试从已有数据中查找
    if (selectResult.value?.candidates) {
      const candidate = selectResult.value.candidates.find(c => c.workCenterCode === workCenterCode)
      if (candidate?.workCenterDetail) {
        currentWorkCenterDetail.value = candidate.workCenterDetail
        // 获取该工作中心下的所有资源组
        workCenterResourceGroups.value = selectResult.value.candidates
          .filter(c => c.workCenterCode === workCenterCode)
          .map(c => ({
            id: c.id,
            resourceGroupCode: c.resourceGroupCode,
            name: c.name,
            workCenterCode: c.workCenterCode,
            workCenterName: c.workCenterName,
            family: c.family
          })) as any[]
        showWorkCenterDetailDialog.value = true
      }
    }
  }
}

onMounted(() => {
  fetchTasks()
  fetchParamOptions() // 获取公共参数下拉选项
  // 初始化一些默认任务
  addTask()
})
</script>

<style scoped>
pre {
  font-family: inherit;
}
</style>
