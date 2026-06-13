<template>
  <div class="p-6 space-y-6 bg-gray-50 min-h-screen">
    <!-- 顶部标题 + 工艺切换 -->
    <div class="flex justify-between items-center">
      <h1 class="text-2xl font-bold">工藝評估 · 規格 → 難度 → 資源組匹配</h1>
      <div class="flex gap-2">
        <button
          :class="['px-4 py-2 rounded-xl', tab === 'hotfoil' ? 'bg-gray-800 text-white' : 'bg-gray-200 text-gray-700']"
          @click="tab = 'hotfoil'"
        >
          燙金
        </button>
        <button
          :class="['px-4 py-2 rounded-xl', tab === 'mounting' ? 'bg-gray-800 text-white' : 'bg-gray-200 text-gray-700']"
          @click="tab = 'mounting'"
        >
          裱紙
        </button>
        <button
          :class="['px-4 py-2 rounded-xl', tab === 'lamination' ? 'bg-gray-800 text-white' : 'bg-gray-200 text-gray-700']"
          @click="tab = 'lamination'"
        >
          過膠
        </button>
        <button
          :class="['px-4 py-2 rounded-xl', tab === 'diecut' ? 'bg-gray-800 text-white' : 'bg-gray-200 text-gray-700']"
          @click="tab = 'diecut'"
        >
          啤機
        </button>
        <button
          :class="['px-4 py-2 rounded-xl', tab === 'silkscreen' ? 'bg-gray-800 text-white' : 'bg-gray-200 text-gray-700']"
          @click="tab = 'silkscreen'"
        >
          絲印
        </button>
      </div>
    </div>

    <!-- 基础尺寸 -->
    <div class="rounded-2xl bg-white border p-4">
      <h2 class="text-lg font-semibold mb-3">基礎尺寸</h2>
      <div class="grid grid-cols-2 md:grid-cols-4 gap-3">
        <label class="flex flex-col gap-1 text-sm">
          <span class="text-gray-600">長邊 L (mm)</span>
          <input
            type="number"
            class="input-field"
            v-model.number="params.len_mm"
          />
        </label>
        <label class="flex flex-col gap-1 text-sm">
          <span class="text-gray-600">寬邊 W (mm)</span>
          <input
            type="number"
            class="input-field"
            v-model.number="params.wid_mm"
          />
        </label>
      </div>
    </div>

    <!-- 裱紙參數 -->
    <div v-if="tab === 'mounting'" class="rounded-2xl bg-white border p-4">
      <h2 class="text-lg font-semibold mb-3">工藝參數（裱紙）</h2>
      <div class="grid grid-cols-2 md:grid-cols-4 gap-3">
        <label class="flex flex-col gap-1 text-sm">
          <span class="text-gray-600">裱紙類型</span>
          <select class="input-field" v-model="params.mounting_type">
            <option value="normal">普通裱紙</option>
            <option value="box">盒類裱紙</option>
          </select>
        </label>
        <label class="flex flex-col gap-1 text-sm">
          <span class="text-gray-600">面紙 (gsm)</span>
          <input type="number" class="input-field" v-model.number="params.top_gsm" />
          <span class="text-xs text-gray-400 mt-1"></span>
        </label>
        <label class="flex flex-col gap-1 text-sm">
          <span class="text-gray-600">面紙是否有色位限制</span>
          <select class="input-field" v-model="params.face_color_limit">
            <option :value="false">否</option>
            <option :value="true">是</option>
          </select>
        </label>
        <label class="flex flex-col gap-1 text-sm">
          <span class="text-gray-600">底紙類型</span>
          <select class="input-field" v-model="params.bottom_type">
            <option>咭紙</option>
            <option>書紙</option>
            <option>粉灰紙</option>
            <option>板紙</option>
            <option>牛皮紙</option>
          </select>
        </label>
        <label class="flex flex-col gap-1 text-sm">
          <span class="text-gray-600">底紙 (gsm)</span>
          <input type="number" class="input-field" v-model.number="params.bottom_gsm" />
          <span class="text-xs text-gray-400 mt-1"></span>
        </label>
        <label class="flex flex-col gap-1 text-sm">
          <span class="text-gray-600">厚度 (mm)</span>
          <input type="number" class="input-field" v-model.number="params.board_thickness_mm" />
        </label>
        <label class="flex flex-col gap-1 text-sm">
          <span class="text-gray-600">有印色/夾色/駁圖</span>
          <select class="input-field" v-model="params.printed">
            <option :value="false">否</option>
            <option :value="true">是</option>
          </select>
        </label>
        <label class="flex flex-col gap-1 text-sm">
          <span class="text-gray-600">對裱層數</span>
          <select class="input-field" v-model="params.mounting_layers">
            <option value="2">2層</option>
            <option value="3">3層</option>
          </select>
        </label>
        <template v-if="params.mounting_layers === '3'">

          <label class="flex flex-col gap-1 text-sm">
            <span class="text-gray-600">第三層是否印刷</span>
            <select class="input-field" v-model="params.third_layer_printed">
              <option :value="false">否</option>
              <option :value="true">是</option>
            </select>
          </label>
        </template>
        <label class="flex flex-col gap-1 text-sm">
          <span class="text-gray-600">開法</span>
          <select class="input-field" v-model="params.open_type">
            <option>對開及以上</option>
            <option>原開</option>
          </select>
        </label>


        <label class="flex flex-col gap-1 text-sm">
          <span class="text-gray-600">是否可短紋</span>
          <select class="input-field" v-model="params.short_grain_ok">
            <option :value="false">否</option>
            <option :value="true">是</option>
          </select>
        </label>
      </div>

      <!-- 操作區 -->
      <div class="mt-4">
        <button class="btn-primary" @click="evaluate">評估</button>
      </div>
    </div>

    <!-- 過膠參數 -->
    <div v-if="tab === 'lamination'" class="rounded-2xl bg-white border p-4">
      <h2 class="text-lg font-semibold mb-3">工藝參數（過膠）</h2>
      <div class="grid grid-cols-2 md:grid-cols-4 gap-3">
        <label class="flex flex-col gap-1 text-sm">
          <span class="text-gray-600">前工序</span>
          <select class="input-field" v-model="params.lamination_pre_process">
            <option value="">無</option>
            <option value="gold_powder">掃金粉後過膠</option>
            <option value="large_hotfoil">大實地燙金後過膠</option>
            <option value="emboss">壓紋加工</option>
            <option value="deboss">擊凹凸加工</option>
            <option value="metallic_ink">金屬油墨</option>
            <option value="other">其他前工序</option>
          </select>
        </label>
        <label class="flex flex-col gap-1 text-sm">
          <span class="text-gray-600">後工序</span>
          <select class="input-field" v-model="params.lamination_post_process">
            <option value="">— 請選擇 —</option>
            <option value="none">無任何加工</option>
            <option value="deboss_emboss">擊凹/凸、絲印UV+擊凹/凸</option>
            <option value="diecut_fold">啤折切、絲印UV+啤折切</option>
            <option value="creasing">踩坑位、絲印UV+踩坑位</option>
            <option value="texture">壓紋</option>
          </select>
        </label>
        <label class="flex flex-col gap-1 text-sm">
          <span class="text-gray-600">過膠類型</span>
          <select class="input-field" v-model="params.lamination_type">
            <option value="">— 請選擇 —</option>
            <option>光膠</option>
            <option>啞膠</option>
            <option>觸感膜</option>
            <option>抗刮膜</option>
            <option>立體膜</option>
            <option>其他</option>
          </select>
        </label>
        <label class="flex flex-col gap-1 text-sm">
          <span class="text-gray-600">過膠面積比例(%)</span>
          <input type="number" class="input-field" v-model.number="params.lamination_area_ratio" />
        </label>
        <label class="flex flex-col gap-1 text-sm">
          <span class="text-gray-600">過膠面數</span>
          <select class="input-field" v-model="params.lamination_sides">
            <option value="single">單面</option>
            <option value="double">雙面</option>
          </select>
        </label>
        <label class="flex flex-col gap-1 text-sm">
          <span class="text-gray-600">雙面過油</span>
          <select class="input-field" v-model="params.double_side_oil">
            <option :value="false">否</option>
            <option :value="true">是</option>
          </select>
        </label>


        <label class="flex flex-col gap-1 text-sm">
          <span class="text-gray-600">适用界面</span>
          <select class="input-field" v-model="params.material">
            <option value="">— 請選擇 —</option>
            <option>銅版紙</option>
            <option>藝術紙</option>
            <option>書紙</option>
            <option>咭紙</option>
            <option>單粉紙</option>
            <option>雙粉紙</option>
            <option>光柵片</option>
            <option>貼紙</option>
            <option>金/銀銻紙</option>
            <option>其他</option>
          </select>
        </label>

        <label class="flex flex-col gap-1 text-sm">
          <span class="text-gray-600">膜型</span>
          <select class="input-field" v-model="params.film_type">
            <option value="">— 請選擇 —</option>
            <option>亮膜</option>
            <option>亞膜</option>
            <option>柔感膜</option>
            <option>觸感膜</option>
            <option>抗刮膜</option>
            <option>立體膜</option>
            <option>光柵片裱PP膜</option>
            <option>其他</option>
          </select>
        </label>

        <label class="flex flex-col gap-1 text-sm">
          <span class="text-gray-600">虛度到紙邊 (mm)</span>
          <input type="number" class="input-field" v-model.number="params.bleed_edge_mm" placeholder="輸入毫米數" />
        </label>

        <label class="flex flex-col gap-1 text-sm">
          <span class="text-gray-600">LDP產品</span>
          <select class="input-field" v-model="params.ldp_product">
            <option :value="false">否</option>
            <option :value="true">是</option>
          </select>
        </label>
        <label class="flex flex-col gap-1 text-sm">
          <span class="text-gray-600">雙面印刷</span>
          <select class="input-field" v-model="params.double_side_print">
            <option :value="false">否</option>
            <option :value="true">是</option>
          </select>
        </label>
        <label class="flex flex-col gap-1 text-sm">
          <span class="text-gray-600">紙張克重 (gsm)</span>
          <input type="number" class="input-field" v-model.number="params.lamination_gsm" />
        </label>
      </div>
      <div class="mt-4 flex gap-2">
        <button class="btn-primary" @click="evaluate">評估</button>
        <button class="btn-secondary" @click="loadDemoExample">載入示例</button>
      </div>
    </div>

    <!-- 啤機參數 -->
    <div v-if="tab === 'diecut'" class="rounded-2xl bg-white border p-4">
      <h2 class="text-lg font-semibold mb-3">工藝參數（啤機）</h2>
      <div class="grid grid-cols-2 md:grid-cols-4 gap-3">
        <label class="flex flex-col gap-1 text-sm">
          <span class="text-gray-600">部門</span>
          <select class="input-field" v-model="params.dept">
            <option value="">— 請選擇 —</option>
            <option>咭書</option>
            <option>精平裝</option>
            <option>綜合</option>
            <option>賀卡</option>
          </select>
        </label>
        <label class="flex flex-col gap-1 text-sm">
          <span class="text-gray-600">石數</span>
          <input type="number" class="input-field" v-model.number="params.stones" />
        </label>
        <label class="flex flex-col gap-1 text-sm">
          <span class="text-gray-600">用紙 (gsm)</span>
          <input type="number" class="input-field" v-model.number="params.sheet_gsm" />
        </label>
        <label class="flex flex-col gap-1 text-sm">
          <span class="text-gray-600">是否貼紙</span>
          <select class="input-field" v-model="params.is_sticker">
            <option :value="false">否</option>
            <option :value="true">是</option>
          </select>
        </label>
        <label class="flex flex-col gap-1 text-sm">
          <span class="text-gray-600">是否有清外圍</span>
          <select class="input-field" v-model="params.clean_outer">
            <option :value="true">是</option>
            <option :value="false">否</option>
          </select>
        </label>
        <label class="flex flex-col gap-1 text-sm">
          <span class="text-gray-600">清廢針距 (mm)</span>
          <input type="number" class="input-field" v-model.number="params.pin_spacing_mm" />
        </label>
        <label class="flex flex-col gap-1 text-sm">
          <span class="text-gray-600">長邊 (mm)</span>
          <input type="number" class="input-field" v-model.number="params.len_mm" />
        </label>
        <label class="flex flex-col gap-1 text-sm">
          <span class="text-gray-600">寬邊 (mm)</span>
          <input type="number" class="input-field" v-model.number="params.wid_mm" />
        </label>
        <label class="flex flex-col gap-1 text-sm">
          <span class="text-gray-600">清廢窿大小(mm)</span>
          <input type="number" class="input-field" v-model.number="params.hole_size_mm" />
        </label>
        <label class="flex flex-col gap-1 text-sm">
          <span class="text-gray-600">啤實度紙度</span>
          <select class="input-field" v-model="params.paper_density_level">
            <option>松</option>
            <option>中</option>
            <option>實</option>
          </select>
        </label>
        <label class="flex flex-col gap-1 text-sm">
          <span class="text-gray-600">產品結構</span>
          <select class="input-field" v-model="params.structure_type">
            <option>普通</option>
            <option>風琴袋</option>
            <option>立體書</option>
            <option>多層件</option>
          </select>
        </label>
        <label class="flex flex-col gap-1 text-sm">
          <span class="text-gray-600">清潔面積(%)</span>
          <input type="number" class="input-field" v-model.number="params.clean_area_ratio" />
        </label>
        <label class="flex flex-col gap-1 text-sm">
          <span class="text-gray-600">是否有風琴袋</span>
          <select class="input-field" v-model="params.fold_bag">
            <option :value="false">否</option>
            <option :value="true">是</option>
          </select>
        </label>
        <label class="flex flex-col gap-1 text-sm">
          <span class="text-gray-600">立體書散件占比(%)</span>
          <input type="number" class="input-field" v-model.number="params.popup_ratio" />
        </label>
        <label class="flex flex-col gap-1 text-sm">
          <span class="text-gray-600">是否有駁圖</span>
          <select class="input-field" v-model="params.joint_diagram">
            <option :value="false">否</option>
            <option :value="true">是</option>
          </select>
        </label>
        <label class="flex flex-col gap-1 text-sm">
          <span class="text-gray-600">紙邊大小(mm)</span>
          <input type="number" class="input-field" v-model.number="params.edge_margin_mm" />
        </label>
      </div>
      <div class="mt-4">
        <button class="btn-primary" @click="evaluate">評估</button>
      </div>
    </div>

    <!-- 絲印參數 -->
    <div v-if="tab === 'silkscreen'" class="rounded-2xl bg-white border p-4">
      <h2 class="text-lg font-semibold mb-3">工藝參數（絲印）</h2>
      <div class="grid grid-cols-2 md:grid-cols-4 gap-3">
        <label class="flex flex-col gap-1 text-sm">
          <span class="text-gray-600">訂單數量（石數）</span>
          <input type="number" class="input-field" v-model.number="params.silk_order_quantity" placeholder="輸入石數" />
        </label>
        <label class="flex flex-col gap-1 text-sm">
          <span class="text-gray-600">用紙 (gsm)</span>
          <input type="number" class="input-field" v-model.number="params.sheet_gsm" />
        </label>
        <label class="flex flex-col gap-1 text-sm">
          <span class="text-gray-600">适用界面</span>
          <select class="input-field" v-model="params.silk_paper_type">
            <option value="">— 請選擇 —</option>
            <option>銅版紙</option>
            <option>藝術紙</option>
            <option>書紙</option>
            <option>咭紙</option>
            <option>銀銻紙</option>
            <option>瓦楞紙</option>
            <option>牛油紙</option>
            <option>塑料片</option>
            <option>紋理紙</option>
            <option>布紋紙</option>
            <option>其他</option>
          </select>
        </label>
        <label class="flex flex-col gap-1 text-sm">
          <span class="text-gray-600">絲印面積比例(%)</span>
          <input type="number" class="input-field" v-model.number="params.silk_area_ratio" />
        </label>
        <label class="flex flex-col gap-1 text-sm">
          <span class="text-gray-600">實心絲印≥100×100mm布局數</span>
          <input type="number" class="input-field" v-model.number="params.solid_silk_layout_count" placeholder="布局數量" />
        </label>
        <label class="flex flex-col gap-1 text-sm">
          <span class="text-gray-600">UV類型及金粉處理</span>
          <select class="input-field" v-model="params.uv_gold_process">
            <option value="">— 請選擇 —</option>
            <option value="normal_uv">普通UV</option>
            <option value="frosted_uv">磨砂UV</option>
            <option value="rock_uv">岩石UV</option>
            <option value="orange_peel_uv">橘皮UV</option>
            <option value="colored_uv">彩色UV</option>
            <option value="wrinkled_uv">皺紋UV</option>
            <option value="uv_oven">過UV烘箱</option>
            <option value="quv">QUV</option>
            <option value="leak_channel_gold">漏/通道金粉</option>
            <option value="double_side_uv_channel_gold">雙面UV通道金粉</option>
            <option value="single_uv_oven">單UV烘箱過</option>
            <option>無UV/金粉處理</option>
          </select>
        </label>
        <label class="flex flex-col gap-1 text-sm" v-if="params.uv_gold_process && ['leak_channel_gold', 'double_side_uv_channel_gold'].includes(params.uv_gold_process)">
          <span class="text-gray-600">UV漏/通道金粉面積(平方英寸)</span>
          <input type="number" class="input-field" v-model.number="params.uv_leak_gold_area_sqin" placeholder="面積" />
        </label>
        <label class="flex flex-col gap-1 text-sm">
          <span class="text-gray-600">金粉轉數</span>
          <input type="number" class="input-field" v-model.number="params.gold_powder_turns" placeholder="轉數" />
        </label>
        <label class="flex flex-col gap-1 text-sm">
          <span class="text-gray-600">全金粉比例（油:粉）</span>
          <select class="input-field" v-model="params.full_gold_powder_ratio">
            <option value="">— 請選擇 —</option>
            <option value="2:1">2:1</option>
            <option value="1.5:1">1.5:1</option>
            <option value="1:1">1:1</option>
            <option>其他</option>
          </select>
        </label>
        <label class="flex flex-col gap-1 text-sm">
          <span class="text-gray-600">前工序</span>
          <select class="input-field" v-model="params.silk_pre_process">
            <option value="">— 請選擇 —</option>
            <option value="deboss_emboss">擊凹凸/掃金粉後絲印</option>
            <option value="emboss_245_400">擊凸加工（245-400g）</option>
            <option value="emboss_unlimited">擊凸加工（不限克重）</option>
            <option value="soft_matte_lamination">過柔感啞膠後絲印</option>
            <option value="lamination_120g">過膠後印件≥120g</option>
            <option value="other">其他前工序</option>
            <option>無前工序</option>
          </select>
        </label>
        <label class="flex flex-col gap-1 text-sm">
          <span class="text-gray-600">另一面處理</span>
          <select class="input-field" v-model="params.other_side_process">
            <option value="">— 請選擇 —</option>
            <option value="lamination">過膠（除柔感啞膠）</option>
            <option value="oiling">過油</option>
            <option>無</option>
          </select>
        </label>
        <label class="flex flex-col gap-1 text-sm">
          <span class="text-gray-600">特殊工藝</span>
          <select class="input-field" v-model="params.special_process">
            <option value="">— 請選擇 —</option>
            <option value="water_varnish_gold">水性光油通道/撒金粉</option>
            <option value="foaming_paste">發泡漿</option>
            <option value="foaming_paste_gold">發泡漿撒金粉</option>
            <option value="hotfoil_base_oil">燙金底油</option>
            <option value="pearl_ink">珠光油墨</option>
            <option value="water_visible_transparent">水顯透明油墨</option>
            <option value="colored_emboss_oil">彩色擊凸油（賀咭）</option>
            <option>無特殊工藝</option>
          </select>
        </label>
        <label class="flex flex-col gap-1 text-sm">
          <span class="text-gray-600">特殊條件</span>
          <select class="input-field" v-model="params.silk_special_conditions">
            <option value="">— 請選擇 —</option>
            <option value="need_registration">需對位</option>
            <option value="single_silk_area_gt_a4">單只絲印面積>A4</option>
            <option value="process_line_width_1mm">處理線寬≤1mm</option>
            <option value="silk_overlap_hotfoil">絲印與燙金圖案重疊</option>
            <option value="silk_ink_with_solvent">絲印油墨（含溶劑）</option>
            <option value="japanese_stationery">日本文具</option>
            <option value="ldp_product_silk">LDP產品</option>
            <option value="fluorescent_color">熒光色（單只閃粉機）</option>
            <option value="multi_turn_silk">多轉絲印</option>
            <option value="card_sticking_layout">絲印連自動啤機-卡紙粘貼</option>
            <option>無特殊條件</option>
          </select>
        </label>
        <label class="flex flex-col gap-1 text-sm">
          <span class="text-gray-600">書名</span>
          <select class="input-field" v-model="params.silk_book_name">
            <option value="">— 請選擇 —</option>
            <option>CB-小狗警察</option>
            <option>CB-小貓</option>
            <option>CB-空軍上校</option>
            <option>其他</option>
          </select>
        </label>
      </div>

      <!-- 操作區 -->
      <div class="mt-4 flex gap-2">
        <button class="btn-primary" @click="evaluate">評估</button>
        <button class="btn-secondary" @click="loadDemoExample">載入示例</button>
      </div>
    </div>

    <!-- 燙金參數 -->
    <div v-if="tab === 'hotfoil'" class="rounded-2xl bg-white border p-4">
      <h2 class="text-lg font-semibold mb-3">工藝參數（燙金）</h2>
      <div class="grid grid-cols-2 md:grid-cols-4 gap-3">
        <label class="flex flex-col gap-1 text-sm">
          <span class="text-gray-600">箔材型號/系列</span>
          <select class="input-field" v-model="params.material_code" @change="onMaterialCodeChange">
            <option v-for="m in MATERIALS" :key="m.code" :value="m.code">{{ m.code }}</option>
          </select>
        </label>
        <label class="flex flex-col gap-1 text-sm">
          <span class="text-gray-600">适用界面</span>
          <select class="input-field" v-model="params.paper_type">
            <option value="">請選擇</option>
            <option value="coated">銅版紙</option>
            <option value="art">藝術紙</option>
            <option value="book">書紙</option>
            <option value="card">咭紙</option>
            <option value="special">特種紙</option>
            <option value="cloth">布紋紙</option>
            <option value="greaseproof">牛油紙</option>
            <option value="texture">有紋理/壓紋</option>
            <option value="film_base">膠片/透明膠片底紙</option>
            <option value="other">其他</option>
          </select>
        </label>
        <label class="flex flex-col gap-1 text-sm">
          <span class="text-gray-600">用紙 (gsm)</span>
          <input type="number" class="input-field" v-model.number="params.sheet_gsm" />
          <span class="text-xs text-gray-400 mt-1"></span>
        </label>
        <label class="flex flex-col gap-1 text-sm">
          <span class="text-gray-600">拉金長度(mm)</span>
          <input type="number" class="input-field" v-model.number="params.foil_length_mm" step="0.1" placeholder="燙金拉金長度" />
          <span class="text-xs text-gray-400 mt-1"></span>
        </label>
        <label class="flex flex-col gap-1 text-sm">
          <span class="text-gray-600">燙金類型</span>
          <select class="input-field" v-model="params.foil_type">
            <option value="">請選擇</option>
            <option value="gold_silver">金/銀</option>
            <option value="laser">雷射</option>
            <option value="emboss">局部浮雕</option>
            <option value="full_foil">全版覆燙</option>
            <option value="3d">立體燙金</option>
            <option value="matte">亞面燙金</option>
            <option value="double_side_hotfoil">雙面燙金（正反兩面重疊）</option>
          </select>
        </label>
        <label class="flex flex-col gap-1 text-sm">
          <span class="text-gray-600">燙金面積(%)</span>
          <input type="number" class="input-field" v-model.number="params.area_ratio" />
        </label>
        <label class="flex flex-col gap-1 text-sm">
          <span class="text-gray-600">加工面積（A5數量）</span>
          <input type="number" class="input-field" v-model.number="params.a5_count" step="0.5" placeholder="如：1、2、3、4" />
          <span class="text-xs text-gray-400 mt-1">A5=148×210mm（A4的一半）</span>
        </label>
        <label class="flex flex-col gap-1 text-sm">
          <span class="text-gray-600">燙金線寬(mm)</span>
          <input type="number" class="input-field" v-model.number="params.line_thickness_mm" step="0.01" placeholder="最細線寬" />
        </label>
        <template v-if="params.foil_type === '3d'">
          <label class="flex flex-col gap-1 text-sm">
            <span class="text-gray-600">單個燙金面積（相對A5）</span>
            <select class="input-field" v-model="params.single_area_ratio">
              <option value="">請選擇</option>
              <option value="1_16">≤1/16 A5（最小）</option>
              <option value="1_16_to_1_8">1/16 A5 < X ≤ 1/8 A5</option>
              <option value="1_8_to_1_4">1/8 A5 < X ≤ 1/4 A5</option>
              <option value="1_4_to_1_2">1/4 A5 < X ≤ 1/2 A5</option>
              <option value="1_2_to_1">1/2 A5 < X ≤ 1 A5（最大）</option>
            </select>
            <span class="text-xs text-gray-400 mt-1">立體燙需指定單個圖案面積</span>
          </label>
        </template>
        <label class="flex flex-col gap-1 text-sm">
          <span class="text-gray-600">需與印刷對位</span>
          <select class="input-field" v-model="params.registration">
            <option :value="false">否</option>
            <option :value="true">是</option>
          </select>
        </label>
        <label class="flex flex-col gap-1 text-sm">
          <span class="text-gray-600">浮雕深度(mm)</span>
          <input type="number" class="input-field" v-model.number="params.emboss_depth_mm" step="0.01" />
        </label>
        <label class="flex flex-col gap-1 text-sm">
          <span class="text-gray-600">是否多次燙(>1 pass)</span>
          <select class="input-field" v-model="params.multi_pass">
            <option :value="false">否</option>
            <option :value="true">是</option>
          </select>
        </label>
        <label class="flex flex-col gap-1 text-sm">
          <span class="text-gray-600">是否有色位限制</span>
          <select class="input-field" v-model="params.has_color_limit">
            <option :value="false">否</option>
            <option :value="true">是</option>
          </select>
        </label>
        <label class="flex flex-col gap-1 text-sm">
          <span class="text-gray-600">工單設定溫度(℃)</span>
          <input type="number" class="input-field" v-model.number="params.job_temp_c" />
        </label>
        <label class="flex flex-col gap-1 text-sm">
          <span class="text-gray-600">前工序</span>
          <select class="input-field" v-model="params.pre_process">
            <option value="">無</option>
            <option value="emboss_deboss">壓紋/擊紋後燙金</option>
            <option value="texture">擊紋</option>
            <option value="deboss_print">擊凹凸（純印張面）</option>
            <option value="after_lamination">過膠後燙金</option>
            <option value="after_mounting_3d">裱後擊凸/壓紋/3D燙金</option>
          </select>
        </label>
        <template v-if="params.pre_process === 'after_lamination'">
          <label class="flex flex-col gap-1 text-sm">
            <span class="text-gray-600">過膠後線寬<=5mm</span>
            <select class="input-field" v-model="params.lamination_line_width_ok">
              <option :value="false">否</option>
              <option :value="true">是</option>
            </select>
          </label>
          <label class="flex flex-col gap-1 text-sm">
            <span class="text-gray-600">過膠後面積<=A5</span>
            <select class="input-field" v-model="params.lamination_area_ok">
              <option :value="false">否</option>
              <option :value="true">是</option>
            </select>
          </label>
        </template>

        <label class="flex flex-col gap-1 text-sm">
          <span class="text-gray-600">燙金後加工</span>
          <select class="input-field" v-model="params.post_process">
            <option value="">無</option>
            <option value="lamination">燙金後過膠</option>
            <option value="uv">燙金後過UV</option>
            <option value="oil">燙金後過水油</option>
            <option value="print_after">先燙金後印刷</option>
            <option value="none">燙金後無其他加工</option>
          </select>
        </label>
        <label class="flex flex-col gap-1 text-sm">
          <span class="text-gray-600">緊身金紙</span>
          <select class="input-field" v-model="params.tight_foil">
            <option :value="false">否</option>
            <option :value="true">是</option>
          </select>
        </label>


        <label class="flex flex-col gap-1 text-sm">
          <span class="text-gray-600">產品圖案</span>
          <select class="input-field" v-model="params.product_pattern">
            <option value="">無特殊圖案</option>
            <option value="solid_reverse">實地反白</option>
            <option value="reverse_line">反白線條</option>
            <option value="reverse_dot">反白點</option>
          </select>
        </label>
        <label class="flex flex-col gap-1 text-sm">
          <span class="text-gray-600">訂單數量（石數）</span>
          <input type="number" class="input-field" v-model.number="params.order_quantity" />
        </label>
        <label class="flex flex-col gap-1 text-sm">
          <span class="text-gray-600">特殊條件</span>
          <select class="input-field" v-model="params.special_condition">
            <option value="">無</option>
            <option value="double_sided">雙面印刷內文/內頁（印張鬆散度下降，難吸紙或雙張）</option>
            <option value="japanese_stationery">日本文具產品（非賀咭）、GHD客戶</option>
            <option value="mesh_pattern">網紋圖案（使用緊身金紙）</option>
            <option value="meixin_box">美心月餅盒（書名）</option>
            <option value="cb_little_sister">CB-小妹妹系列（書名）</option>
            <option value="other_book">其他書名</option>
          </select>
        </label>
        
        <template v-if="params.special_condition === 'other_book'">
          <label class="flex flex-col gap-1 text-sm">
            <span class="text-gray-600">書名</span>
            <input type="text" class="input-field" v-model="params.book_name" placeholder="請輸入書名" />
          </label>
        </template>
      </div>
      <div class="text-xs text-gray-500 mt-2">
      </div>
      <div class="mt-4 flex gap-2">
        <button class="btn-primary" @click="evaluate">評估</button>
        <button class="btn-secondary" @click="loadDemoExample">載入示例</button>
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
import { ref, computed } from 'vue';

type Tab = 'mounting' | 'lamination' | 'diecut' | 'silkscreen' | 'hotfoil';

const tab = ref<Tab>('hotfoil');

// =============== 燙金物料庫 ===============
const MATERIALS = [
  {
    code: 'U-LION-1260-G',
    name: '宇狮 #1260 金箔',
    foil_type: '金/銀',
    temp_c: [95, 125],
    pressure: 'M',
    min_line_mm: 0.18,
    max_area_pct: 70,
    papers: ['銅版紙', '書紙', '咭紙'],
  },
  {
    code: 'U-LION-1260-LZ',
    name: '宇狮 #1260 雷射箔',
    foil_type: '雷射',
    temp_c: [100, 130],
    pressure: 'H',
    min_line_mm: 0.22,
    max_area_pct: 60,
    papers: ['銅版紙', '咭紙'],
  },
  {
    code: 'U-LION-EMB-900',
    name: '宇狮 EMB-900 浮雕箔',
    foil_type: '局部浮雕',
    temp_c: [110, 135],
    pressure: 'H',
    min_line_mm: 0.25,
    max_area_pct: 55,
    papers: ['咭紙', '特種紙'],
  },
];

// =============== 燙金機台能力庫 ===============
const MACHINES = [
  {
    code: 'WC06-HOT001',
    name: '平平台燙金',
    type: 'flat',
    bed: [1000, 700],
    pressure: ['L', 'M'],
    min_line_mm: 0.18,
    max_area_pct: 75,
    max_color_positions: 4, // 最大色位數量
    registration_ok: true,
    emboss_support: false,
    multipass_ok: true,
    hist: '90%',
    cost: 1.0,
    bias: 0.10,
  },
  {
    code: 'WC06-HOT002',
    name: '浮雕燙金',
    type: 'emboss',
    bed: [900, 650],
    pressure: ['M', 'H'],
    min_line_mm: 0.22,
    max_area_pct: 65,
    max_color_positions: 4, // 最大色位數量
    registration_ok: true,
    emboss_support: true,
    multipass_ok: true,
    hist: '86%',
    cost: 1.15,
    bias: 0.08,
  },
  {
    code: 'WC06-HOT003',
    name: '多工位燙金',
    type: 'multi-pass',
    bed: [1000, 700],
    pressure: ['M', 'H'],
    min_line_mm: 0.20,
    max_area_pct: 85,
    max_color_positions: 6, // 最大色位數量（多工位可支持更多色位）
    registration_ok: true,
    emboss_support: true,
    multipass_ok: true,
    hist: '82%',
    cost: 1.20,
    bias: 0.06,
  },
  {
    code: 'WC06-HOT004',
    name: '滾筒燙金',
    type: 'roll',
    bed: [1200, 850], // 上機紙度：非賀咭最大640×860mm，賀咭最大609.6×889mm
    max_production_area: { width: 560, height: 822 }, // 最大上機生產面積（版尺寸）：560×822mm
    pressure: ['L', 'M', 'H'],
    min_line_mm: 0.15,
    max_area_pct: 90,
    max_color_positions: 6, // 最大色位數量（滾筒機通常支持更多色位）
    registration_ok: true,
    emboss_support: false, // 不能用于壓紋燙金
    multipass_ok: true,
    hist: '78%',
    cost: 1.25,
    bias: 0.05,
  },
  {
    code: 'WC06-HOT005',
    name: '手落燙金',
    type: 'manual',
    bed: [1000, 700],
    pressure: ['L', 'M', 'H'],
    min_line_mm: 0.18,
    max_area_pct: 80,
    max_color_positions: 1, // 最大色位數量（手落機通常單色位）
    registration_ok: true,
    emboss_support: false,
    multipass_ok: false,
    hist: '70%',
    cost: 1.3,
    bias: 0.04,
  },
];

const params = ref<any>({
  // 裱紙參數
  mounting_type: 'normal', // 裱紙類型：'normal'=普通裱紙, 'box'=盒類裱紙
  top_gsm: undefined, // 面紙 (gsm)
  face_color_limit: false, // 面紙是否有色位限制
  bottom_type: '咭紙', // 底紙類型
  bottom_gsm: undefined, // 底紙 (gsm)
  board_thickness_mm: undefined, // 板紙厚度 (mm)
  printed: false, // 有印色/夾色/駁圖
  mounting_layers: '2', // 對裱層數：'2'=2層, '3'=3層
  third_layer_combo: '', // 第三層組合：''=無, '薄紙+板紙'=薄紙+板紙, '其他'=其他
  third_layer_printed: false, // 第三層是否印刷
  open_type: '對開及以上', // 開法
  reams: undefined, // 出紙數量 (令)
  is_book_paper: false, // 書紙工況
  short_grain_ok: false, // 是否可短紋
  // 其他參數
  paper_density_level: '中',
  structure_type: '普通',
  // 燙金參數
  material_code: MATERIALS[0]?.code || '',
  paper_type: '', // 适用界面：''=請選擇, 'coated'=銅版紙, 'art'=藝術紙, 'book'=書紙, 'card'=咭紙, 'special'=特種紙, 'cloth'=布紋紙, 'greaseproof'=牛油紙, 'texture'=有紋理/壓紋, 'film_base'=膠片/透明膠片底紙, 'other'=其他
  sheet_gsm: 250,
  foil_length_mm: undefined, // 拉金長度(mm)：燙金拉金長度，用於判斷難度
  foil_type: MATERIALS[0]?.foil_type || '金/銀', // 燙金類型：'gold_silver'=金/銀, 'laser'=雷射, 'emboss'=局部浮雕, 'full_foil'=全版覆燙, '3d'=立體燙金, 'matte'=亞面燙金, 'double_side_hotfoil'=雙面燙金（正反兩面重疊）
  area_ratio: 35,
  a5_count: undefined, // 加工面積（A5數量）：整個加工區域相當於幾個A5紙（A5=148×210mm）
  line_thickness_mm: 0.2,
  single_area_ratio: '', // 單個燙金面積（立體燙用）：'1_16'=≤1/16A5, '1_16_to_1_8'=1/16-1/8A5, '1_8_to_1_4'=1/8-1/4A5, '1_4_to_1_2'=1/4-1/2A5, '1_2_to_1'=1/2-1A5
  registration: false,
  emboss_depth_mm: 0,
  multi_pass: false,
  has_color_limit: false, // 是否有色位限制
  job_temp_c: 115,
  // 前工序（整合为下拉框）
  pre_process: '', // 前工序：''=無, 'emboss_deboss'=壓紋/擊紋後燙金, 'texture'=擊紋, 'deboss_print'=擊凹凸（純印張面）, 'after_lamination'=過膠後燙金, 'after_mounting_3d'=裱後擊凸/壓紋/3D燙金
  // 過膠後燙金相關（僅當前工序為'after_lamination'時使用）
  lamination_line_width_ok: false, // 過膠後燙金線寬<=5mm
  lamination_area_ok: false, // 過膠後燙金面積<=A5
  // 燙金後加工（整合為下拉框）
  post_process: '', // 燙金後加工：''=無, 'lamination'=過膠, 'uv'=過UV, 'oil'=過水油, 'print_after'=先燙金後印刷, 'none'=無其他加工
  tight_foil: false, // 金紙為緊身金紙
  // 咭書類特殊處理
  is_cardbook: false, // 針對咭書類產品
  product_pattern: '', // 產品圖案：''=無特殊圖案, 'solid_reverse'=實地反白, 'reverse_line'=反白線條, 'reverse_dot'=反白點
  reverse_line_dot_mm: undefined, // 反白線條或點(mm)
  // 數量
  order_quantity: undefined, // 訂單數量（石數）
  // 特殊條件
  special_condition: '', // 特殊條件：''=無, 'double_sided'=雙面印刷內文/內頁, 'film_production'=需對菲林生產, 'after_mounting'=裱後燙金, 'special_material'=特殊材料, 'japanese_stationery'=日本文具, 'flocked_surface'=植毛面, 'mesh_pattern'=網紋圖案, 'meixin_box'=美心月餅盒, 'cb_little_sister'=CB-小妹妹系列, 'other_book'=其他書名
  book_name: '', // 書名（當special_condition='other_book'時使用）
  is_greeting_card: false, // 是否賀咭類（用於區分賀咭和非賀咭的規則）
  // 過膠參數
  lamination_pre_process: '', // 過膠前工序：''=無, 'gold_powder'=掃金粉後過膠, 'large_hotfoil'=大實地燙金後過膠, 'emboss'=壓紋加工, 'deboss'=擊凹凸加工, 'metallic_ink'=金屬油墨, 'other'=其他前工序
  lamination_post_process: '', // 過膠後工序：''=請選擇, 'none'=無任何加工, 'deboss_emboss'=擊凹/凸、絲印UV+擊凹/凸, 'diecut_fold'=啤折切、絲印UV+啤折切, 'creasing'=踩坑位、絲印UV+踩坑位, 'texture'=壓紋
  lamination_type: '', // 過膠類型：''=請選擇, '光膠'=光膠, '啞膠'=啞膠, '觸感膜'=觸感膜, '抗刮膜'=抗刮膜, '立體膜'=立體膜, '其他'=其他
  lamination_area_ratio: undefined, // 過膠面積比例(%)
  lamination_sides: 'single', // 過膠面數：'single'=單面, 'double'=雙面
  double_side_oil: false, // 雙面過油
  double_side_uv: false, // 雙面過UV
  silk_screen: false, // 絲印
  material: '', // 材質：''=請選擇, '銅版紙'=銅版紙, '藝術紙'=藝術紙, '書紙'=書紙, '咭紙'=咭紙, '單粉紙'=單粉紙, '雙粉紙'=雙粉紙, '光柵片'=光柵片, '貼紙'=貼紙, '其他'=其他
  special_material: false, // 是否特殊材質
  film_type: '', // 膜型：''=請選擇, '亮膜'=亮膜, '亞膜'=亞膜, '柔感膜'=柔感膜, '觸感膜'=觸感膜, '抗刮膜'=抗刮膜, '立體膜'=立體膜, '其他'=其他
  lamination_gsm: undefined, // 紙張克重 (gsm)
  // 過膠其他條件
  no_powder_side: false, // 無粉面過膠
  bleed_edge_mm: undefined, // 虛度到紙邊 (mm)
  long_grain_paper: false, // 印張開長紋紙
  ldp_product: false, // LDP產品
  double_side_print: false, // 雙面印刷
  // 絲印參數
  silk_order_quantity: undefined, // 訂單數量（石數）
  silk_paper_type: '', // 适用界面（絲印專用）
  silk_area_ratio: undefined, // 絲印面積比例(%)
  solid_silk_layout_count: undefined, // 實心絲印≥100×100mm布局數
  uv_gold_process: '', // UV類型及金粉處理（合併字段）
  gold_powder_turns: undefined, // 金粉轉數
  full_gold_powder_ratio: '', // 全金粉比例（油:粉）
  uv_leak_gold_area_sqin: undefined, // UV漏/通道金粉面積(平方英寸)
  silk_pre_process: '', // 前工序（包含擊凸加工）
  other_side_process: '', // 另一面處理
  special_process: '', // 特殊工藝
  silk_special_conditions: '', // 特殊條件（合併字段）
  silk_book_name: '', // 書名（絲印）
  // 以下字段保留用於評估邏輯兼容
  need_registration: false, // 是否需對位（從silk_special_conditions映射）
  single_silk_area_gt_a4: false, // 單只絲印面積>A4（從silk_special_conditions映射）
  process_line_width_1mm: false, // 處理線寬≤1mm（從silk_special_conditions映射）
  uv_type: '', // UV類型（從uv_gold_process映射）
  double_side_uv_channel_gold: false, // 雙面UV通道金粉（從uv_gold_process映射）
  silk_overlap_hotfoil: false, // 絲印與燙金圖案重疊（從silk_special_conditions映射）
  silk_ink_with_solvent: false, // 絲印油墨（含溶劑）（從silk_special_conditions映射）
  japanese_stationery: false, // 是否日本文具（從silk_special_conditions映射）
  ldp_product_silk: false, // 是否LDP產品（從silk_special_conditions映射）
  single_uv_oven_pass: false, // 是否單UV烘箱過（從uv_gold_process映射）
  card_sticking_layout: false, // 絲印連自動啤機-卡紙粘貼（從silk_special_conditions映射）
  fluorescent_color: false, // 是否熒光色（從silk_special_conditions映射）
  multi_turn_silk: false, // 是否多轉絲印（從silk_special_conditions映射）
  emboss_gsm_range: '', // 擊凸加工（gsm範圍）（從silk_pre_process映射）
});

// 物料选择变更处理
const onMaterialCodeChange = () => {
  const material = MATERIALS.find((m) => m.code === params.value.material_code);
  if (material) {
    // 根据物料类型映射到foil_type
    if (material.foil_type === '金/銀') params.value.foil_type = 'gold_silver';
    else if (material.foil_type === '雷射') params.value.foil_type = 'laser';
    else if (material.foil_type === '局部浮雕') params.value.foil_type = 'emboss';
    else params.value.foil_type = '';
  }
};

// 計算當前選中的物料
const currentMaterial = computed(() => {
  return MATERIALS.find((m) => m.code === params.value.material_code);
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

  let canMachine: '可上機' | '需手落' | '需諮詢' = '可上機';
  let resource: string | null = null;
  let score = 1.0;

  const L = Number(params.value.len_mm || 0);
  const W = Number(params.value.wid_mm || 0);
  const longSide = Math.max(L, W);
  const shortSide = Math.min(L, W);

  if (tab.value === 'mounting') {
    // 裱纸规则
    if (params.value.top_gsm !== undefined) {
      const minTop = params.value.face_color_limit ? 140 : 100;
      if (params.value.top_gsm < minTop) {
        reasons.push('面紙克重過低');
        hits.push({
          source: '面紙',
          rule: `${params.value.face_color_limit ? '有色位' : ''}最小${minTop}gsm`,
          action: 'SET_DIFFICULTY',
          reason: `top_gsm=${params.value.top_gsm}`,
          score: 0.3,
        });
      }
      if (params.value.top_gsm > 800) {
        reasons.push('面紙克重過高');
        hits.push({
          source: '面紙',
          rule: '最大800gsm',
          action: 'SET_DIFFICULTY',
          reason: `top_gsm=${params.value.top_gsm}`,
          score: 0.2,
        });
      }
    }

    // 底纸范围检查
    if (params.value.bottom_type) {
      const g = Number(params.value.bottom_gsm || 0);
      const checkRange = (min: number, max: number, label: string) => {
        if (g < min || g > max) {
          reasons.push(`${label}超範圍`);
          hits.push({
            source: '底紙',
            rule: `${label}${min}~${max}gsm`,
            action: 'CONSULT',
            reason: `bottom_gsm=${g}`,
            score: 0.3,
          });
          canMachine = '需諮詢';
        }
      };

      if (params.value.bottom_type === '咭紙') checkRange(210, 1400, '咭紙');
      if (params.value.bottom_type === '書紙') checkRange(180, 1400, '書紙');
      if (params.value.bottom_type === '粉灰紙') checkRange(250, 1400, '粉灰紙');
      if (
        params.value.bottom_type === '板紙' &&
        params.value.board_thickness_mm !== undefined &&
        params.value.board_thickness_mm > 2.87
      ) {
        reasons.push('板紙>2.87mm');
        hits.push({
          source: '板紙',
          rule: '厚度≤2.87mm',
          action: 'CONSULT',
          reason: `t=${params.value.board_thickness_mm}`,
          score: 0.5,
        });
        canMachine = '需諮詢';
      }
    }

    // 牛皮纸底纸
    if (
      params.value.bottom_type === '牛皮紙' &&
      params.value.bottom_gsm !== undefined &&
      params.value.bottom_gsm < 300
    ) {
      reasons.push('牛皮紙<300gsm：只能手裱');
      hits.push({
        source: '底紙',
        rule: '牛皮紙≥300gsm',
        action: 'HAND_MOUNT',
        reason: `bottom_gsm=${params.value.bottom_gsm}`,
        score: 0.6,
      });
      canMachine = '需手落';
    }

    // 底纸<250gsm需短纹
    if (params.value.bottom_gsm !== undefined && params.value.bottom_gsm < 250) {
      reasons.push('底紙<250gsm需短紋');
      hits.push({
        source: '底紙',
        rule: '<250gsm需短紋',
        action: params.value.short_grain_ok ? 'OK' : 'CONSULT',
        reason: `short_grain_ok=${!!params.value.short_grain_ok}`,
        score: 0.2,
      });
      if (!params.value.short_grain_ok) canMachine = '需諮詢';
    }

    // 滚筒裱选择规则（盒類裱紙專用規則）
    let preferRoll = false;
    const isBoxType = params.value.mounting_type === 'box'; // 盒類裱紙
    const isBoard = params.value.bottom_type === '板紙';
    const isBookPaperNoPrint = params.value.is_book_paper === true && !params.value.printed;
    const isDuiKaiOrAbove = params.value.open_type === '對開及以上';

    // 盒類裱紙規則1：板紙對開或以上開法且出紙數量≥2令，用滾筒裱
    if (isBoxType && isBoard && isBookPaperNoPrint && isDuiKaiOrAbove && Number(params.value.reams || 0) >= 2) {
      preferRoll = true;
      hits.push({
        source: '滾筒傾向',
        rule: '盒類裱紙：書紙(非印)+板紙 對開≥2令',
        action: 'PREFER_ROLL',
        reason: '',
        score: 0.1,
      });
    }

    // 盒類裱紙規則2：板紙原開，出紙數量≥10令，用滾筒裱
    if (isBoxType && isBoard && params.value.open_type === '原開' && Number(params.value.reams || 0) >= 10) {
      preferRoll = true;
      hits.push({
        source: '滾筒傾向',
        rule: '盒類裱紙：板紙原開≥10令',
        action: 'PREFER_ROLL',
        reason: '',
        score: 0.1,
      });
    }

    // 滾筒裱紙機最小尺寸：558×200mm（非入機邊）
    const nonFeedEdgeOKForRoll = shortSide >= 558;
    
    // 盒類裱紙規則3：當板紙尺寸(非入機邊)<558mm，用平張裱
    if (isBoxType && !nonFeedEdgeOKForRoll && preferRoll) {
      preferRoll = false;
      hits.push({
        source: '尺寸限制',
        rule: '盒類裱紙：板紙尺寸(非入機邊)<558mm，用平張裱',
        action: 'PREFER_FLAT',
        reason: `當前非入機邊：${shortSide}mm`,
        score: 0.2,
      });
    }

    // 盒類裱紙規則4：三層對裱，第三層為非印刷且為薄紙與板紙對裱，必須使用滾筒裱，此情況不受石數限制
    if (
      params.value.mounting_layers === '3' &&
      params.value.third_layer_combo === '薄紙+板紙' &&
      !params.value.third_layer_printed
    ) {
      preferRoll = true;
      hits.push({
        source: '滾筒剛性',
        rule: '盒類裱紙：三層對裱(第三層為非印刷且為薄紙與板紙對裱)必須滾筒，不受石數限制',
        action: 'FORCE_ROLL',
        reason: '',
        score: 0.3,
      });
    }

    // 不屬於以上兩種情況，用平張裱（僅限盒類裱紙）
    if (isBoxType && !preferRoll && params.value.mounting_layers !== '3') {
      hits.push({
        source: '平張傾向',
        rule: '盒類裱紙：不屬於滾筒裱條件，用平張裱',
        action: 'PREFER_FLAT',
        reason: '',
        score: 0.05,
      });
    }

    // 上機尺寸限制（最大1000×1200mm，最小305×505mm）
    if (longSide > 1000 || shortSide > 1200) {
      reasons.push('尺寸超出最大範圍');
      hits.push({
        source: '尺寸',
        rule: '上機最大尺寸：1000×1200mm',
        action: 'SET_DIFFICULTY',
        reason: `當前：${longSide}×${shortSide}mm`,
        score: 0.3,
      });
    }
    if (longSide < 305 || shortSide < 505) {
      reasons.push('尺寸小於最小範圍');
      hits.push({
        source: '尺寸',
        rule: '上機最小尺寸：305×505mm',
        action: 'SET_DIFFICULTY',
        reason: `當前：${longSide}×${shortSide}mm`,
        score: 0.3,
      });
    }

    // 滾筒裱紙機最小尺寸檢查（通用規則）
    if (!nonFeedEdgeOKForRoll && preferRoll && !isBoxType) {
      reasons.push('尺寸不適合滾筒裱');
      hits.push({
        source: '尺寸',
        rule: '滾筒裱最小尺寸：558×200mm（非入機邊）',
        action: 'PREFER_FLAT',
        reason: `當前非入機邊：${shortSide}mm`,
        score: 0.2,
      });
      // 如果尺寸不適合滾筒，改用平張裱
      if (shortSide < 558) preferRoll = false;
    }

    const isZhengdu = Math.abs(L - W) <= 76.2;
    if (isZhengdu) {
      reasons.push('正度紙');
      hits.push({
        source: '尺寸',
        rule: '長寬差≤76.2mm（正度紙）',
        action: 'SET_DIFFICULTY',
        reason: '正度紙',
        score: 0.1,
      });
    }

    if (L > 950 || L <= 550) {
      hits.push({
        source: '尺寸',
        rule: '長邊極值(≤550或>950)',
        action: 'SET_DIFFICULTY',
        reason: `L=${L}`,
        score: 0.2,
      });
    }

    if (W > 800 || W <= 350) {
      hits.push({
        source: '尺寸',
        rule: '寬邊極值(≤350或>800)',
        action: 'SET_DIFFICULTY',
        reason: `W=${W}`,
        score: 0.2,
      });
    }

    resource = preferRoll ? 'WC10-MPP008 / 滾筒裱紙機' : 'WC10-MPP009 / 平張裱紙機';

    if (params.value.mounting_layers === '3') {
      reasons.push('三層對裱');
      hits.push({
        source: '結構',
        rule: '三層',
        action: 'SET_DIFFICULTY',
        reason: '',
        score: 0.3,
      });
    }

    if (params.value.printed) {
      reasons.push('夾色/有印色');
      hits.push({
        source: '對位',
        rule: '有印色',
        action: 'SET_DIFFICULTY',
        reason: '',
        score: 0.2,
      });
    }

    // 難度分級規則（根據圖片規則）
    // 特難：160g ≤ 底紙克重 ≤ 190g（紙薄難走紙）
    if (params.value.bottom_gsm !== undefined) {
      const bottomGsm = Number(params.value.bottom_gsm);
      if (bottomGsm >= 160 && bottomGsm <= 190) {
        reasons.push('底紙160-190g（特難）');
        hits.push({
          source: '底紙',
          rule: '160g≤底紙克重≤190g（特難，紙薄難走紙）',
          action: 'SET_DIFFICULTY',
          reason: `bottom_gsm=${bottomGsm}`,
          score: 0.5,
        });
      }
    }

    // 難：板紙厚度≥2.8mm、190g<底紙克重<300g、尺寸限制等
    if (
      params.value.board_thickness_mm !== undefined &&
      params.value.board_thickness_mm >= 2.8
    ) {
      reasons.push('板紙厚度≥2.8mm（難）');
      hits.push({
        source: '板厚',
        rule: '板紙厚度≥2.8mm（難）',
        action: 'SET_DIFFICULTY',
        reason: `board_thickness=${params.value.board_thickness_mm}mm`,
        score: 0.3,
      });
    }

    // 難：190g<底紙克重<300g
    if (params.value.bottom_gsm !== undefined) {
      const bottomGsm = Number(params.value.bottom_gsm);
      if (bottomGsm > 190 && bottomGsm < 300) {
        reasons.push('底紙190-300g（難）');
        hits.push({
          source: '底紙',
          rule: '190g<底紙克重<300g（難）',
          action: 'SET_DIFFICULTY',
          reason: `bottom_gsm=${bottomGsm}`,
          score: 0.2,
        });
      }
    }

    // 難：駁圖/夾色/裱後雙面有印色
    if (params.value.printed) {
      reasons.push('駁圖/夾色/裱後雙面有印色（難）');
      hits.push({
        source: '對位',
        rule: '駁圖/夾色/裱後雙面有印色（難）',
        action: 'SET_DIFFICULTY',
        reason: '',
        score: 0.2,
      });
    }

    // 中：2mm<板紙厚度<2.8mm
    if (params.value.board_thickness_mm !== undefined) {
      const boardThickness = Number(params.value.board_thickness_mm);
      if (boardThickness > 2 && boardThickness < 2.8) {
        reasons.push('板紙厚度2-2.8mm（中）');
        hits.push({
          source: '板厚',
          rule: '2mm<板紙厚度<2.8mm（中）',
          action: 'OK',
          reason: `board_thickness=${boardThickness}mm`,
          score: 0,
        });
      }
    }

    // 中：550mm<長邊<750mm 且 350mm<寬邊<580mm
    if (L > 550 && L < 750 && W > 350 && W < 580) {
      reasons.push('尺寸中檔（中）');
      hits.push({
        source: '尺寸',
        rule: '550mm<長邊<750mm 且 350mm<寬邊<580mm（中）',
        action: 'OK',
        reason: `L=${L}mm，W=${W}mm`,
        score: 0,
      });
    }

    // 易：需要同時滿足3個條件
    // 條件1：底紙克重≥300g 且 750mm≤長邊≤950mm 且 580mm≤寬邊≤800mm
    const easyCondition1 = params.value.bottom_gsm !== undefined &&
                           Number(params.value.bottom_gsm) >= 300 &&
                           L >= 750 && L <= 950 &&
                           W >= 580 && W <= 800;

    // 條件2：板紙厚度≤2mm 且 750mm≤長邊≤950mm 且 580mm≤寬邊≤800mm
    const easyCondition2 = params.value.board_thickness_mm !== undefined &&
                           Number(params.value.board_thickness_mm) <= 2 &&
                           L >= 750 && L <= 950 &&
                           W >= 580 && W <= 800;

    if (easyCondition1 || easyCondition2) {
      reasons.push('易');
      hits.push({
        source: '易',
        rule: easyCondition1 ? '底紙≥300g且尺寸750-950×580-800mm（易）' : '板紙≤2mm且尺寸750-950×580-800mm（易）',
        action: 'OK',
        reason: '',
        score: -0.1,
      });
    } else {
      // 如果不能同時滿足3個條件，難度升一級（即中）
      if ((params.value.bottom_gsm !== undefined && Number(params.value.bottom_gsm) >= 300) ||
          (params.value.board_thickness_mm !== undefined && Number(params.value.board_thickness_mm) <= 2) ||
          (L >= 750 && L <= 950 && W >= 580 && W <= 800)) {
        // 只滿足部分條件，難度升一級
        if (!reasons.some(r => r.includes('中') || r.includes('難') || r.includes('特難'))) {
          reasons.push('不能同時滿足3個易條件（升一級）');
          hits.push({
            source: '難度',
            rule: '不能同時滿足3個易條件，難度升一級（即中）',
            action: 'SET_DIFFICULTY',
            reason: '',
            score: 0.1,
          });
        }
      }
    }

    // 注：裱板紙的印件，用板紙厚度區分難易度
    if (params.value.bottom_type === '板紙' && params.value.printed) {
      reasons.push('裱板紙印件（用板紙厚度區分難易度）');
      hits.push({
        source: '板紙印件',
        rule: '裱板紙的印件，用板紙厚度區分難易度',
        action: 'OK',
        reason: '',
        score: 0,
      });
    }
  }

  if (tab.value === 'lamination') {
    // 前工序檢查（特難條件）
    if (params.value.lamination_pre_process) {
      const preProcessMap: Record<string, { name: string; difficulty: string }> = {
        'gold_powder': { name: '掃金粉後過膠', difficulty: '特難' },
        'large_hotfoil': { name: '大實地燙金後過膠', difficulty: '特難' },
        'emboss': { name: '壓紋加工', difficulty: '特難' },
        'deboss': { name: '擊凹凸加工', difficulty: '特難' },
        'metallic_ink': { name: '金屬油墨', difficulty: '特難' },
        'other': { name: '其他前工序', difficulty: '高難' },
      };
      const preProcess = preProcessMap[params.value.lamination_pre_process];
      if (preProcess) {
        reasons.push(`前工序：${preProcess.name}（${preProcess.difficulty}）`);
        hits.push({
          source: '前工序',
          rule: `前工序：${preProcess.name}`,
          action: 'SET_DIFFICULTY',
          reason: `${preProcess.difficulty}`,
          score: preProcess.difficulty === '特難' ? 0.5 : 0.3,
        });
        // 特殊处理：金屬油墨也是前工序，属于特難
        if (params.value.lamination_pre_process === 'metallic_ink') {
          reasons.push('金屬油墨（特難）');
        }
      }
    }

    // 過膠類型檢查
    if (params.value.lamination_type) {
      const laminationTypeMap: Record<string, string> = {
        '光膠': '光膠',
        '啞膠': '啞膠',
        '觸感膜': '觸感膜',
        '抗刮膜': '抗刮膜',
        '立體膜': '立體膜',
        '其他': '其他',
      };
      reasons.push(`過膠類型：${laminationTypeMap[params.value.lamination_type] || params.value.lamination_type}`);
      hits.push({
        source: '過膠類型',
        rule: `過膠類型：${params.value.lamination_type}`,
        action: 'OK',
        reason: '',
        score: 0,
      });
    }

    // 過膠面數檢查（根據圖片規則）
    if (params.value.lamination_sides === 'double') {
      reasons.push('雙面過膠（難）');
      hits.push({
        source: '過膠面數',
        rule: '雙面過膠（難）',
        action: 'SET_DIFFICULTY',
        reason: '',
        score: 0.3,
      });
    }

    // 雙面印刷 → 難
    // 这个条件需要从其他参数判断，暂时先检查是否有双面相关的选项
    // 可以在后续添加"雙面印刷"选项

    // 材質檢查（根據圖片規則）
    if (params.value.material) {
      reasons.push(`材質：${params.value.material}`);
      hits.push({
        source: '材質',
        rule: `材質：${params.value.material}`,
        action: 'OK',
        reason: '',
        score: 0,
      });

      const gsm = params.value.lamination_gsm !== undefined ? Number(params.value.lamination_gsm) : undefined;

      // 雙粉紙規則
      if (params.value.material === '雙粉紙' && gsm !== undefined) {
        if (gsm <= 128) {
          reasons.push('雙粉紙≤128g（特難）');
          hits.push({
            source: '材質',
            rule: '雙粉紙≤128g（特難）',
            action: 'SET_DIFFICULTY',
            reason: `gsm=${gsm}`,
            score: 0.5,
          });
        } else if (gsm > 128 && gsm <= 157) {
          reasons.push('雙粉紙128-157g（高難）');
          hits.push({
            source: '材質',
            rule: '128g<雙粉紙≤157g（高難）',
            action: 'SET_DIFFICULTY',
            reason: `gsm=${gsm}`,
            score: 0.4,
          });
        }
      }

      // 書紙規則
      if (params.value.material === '書紙' && gsm !== undefined) {
        if (gsm <= 90) {
          reasons.push('書紙≤90g（特難）');
          hits.push({
            source: '材質',
            rule: '書紙≤90g（特難）',
            action: 'SET_DIFFICULTY',
            reason: `gsm=${gsm}`,
            score: 0.5,
          });
        } else if (gsm > 90 && gsm <= 128) {
          reasons.push('書紙90-128g（高難）');
          hits.push({
            source: '材質',
            rule: '90g<書紙≤128g（高難）',
            action: 'SET_DIFFICULTY',
            reason: `gsm=${gsm}`,
            score: 0.4,
          });
        }
      }

      // 單粉紙規則
      if (params.value.material === '單粉紙' || params.value.material.includes('單粉')) {
        if (gsm !== undefined) {
          if (gsm >= 100 && gsm <= 141) {
            reasons.push('單粉紙100-141g（難）');
            hits.push({
              source: '材質',
              rule: '100g≤單粉紙≤141g（難）',
              action: 'SET_DIFFICULTY',
              reason: `gsm=${gsm}`,
              score: 0.3,
            });
          } else if (gsm >= 140 && gsm < 157) {
            reasons.push('單粉紙140-157g（中）');
            hits.push({
              source: '材質',
              rule: '140g≤單粉紙<157g（中）',
              action: 'OK',
              reason: `gsm=${gsm}`,
              score: 0,
            });
          } else if (gsm >= 157 && gsm < 200) {
            reasons.push('單粉紙157-200g（易）');
            hits.push({
              source: '材質',
              rule: '157g≤單粉紙<200g（易）',
              action: 'OK',
              reason: `gsm=${gsm}`,
              score: -0.1,
            });
          }
        }
        
        // 單粉紙特殊處理（適用特定過膠線）
        reasons.push('單粉紙（適用特定過膠線）');
        hits.push({
          source: '材質',
          rule: '單粉紙適用特定過膠線',
          action: 'SELECT_MACHINE',
          reason: '單粉紙',
          score: 0.2,
        });
      }

      // 咭紙規則
      if (params.value.material === '咭紙' && gsm !== undefined) {
        if (gsm < 200) {
          reasons.push('咭紙<200g（中）');
          hits.push({
            source: '材質',
            rule: '咭紙<200g（中）',
            action: 'OK',
            reason: `gsm=${gsm}`,
            score: 0,
          });
        } else if (gsm >= 200) {
          reasons.push('咭紙≥200g（難）');
          hits.push({
            source: '材質',
            rule: '用紙≥200g（難）',
            action: 'SET_DIFFICULTY',
            reason: `gsm=${gsm}`,
            score: 0.3,
          });
        }
      }

      // 其他特殊材質
      if (params.value.material === '布紋紙' || params.value.material === '花紙') {
        reasons.push(`${params.value.material}（特難）`);
        hits.push({
          source: '材質',
          rule: `${params.value.material}（特難）`,
          action: 'SET_DIFFICULTY',
          reason: '',
          score: 0.5,
        });
      }

      if (params.value.material === '牛皮紙' || params.value.material?.includes('牛皮')) {
        reasons.push('原色牛皮紙過膠（特難）');
        hits.push({
          source: '材質',
          rule: '原色牛皮紙過膠（特難）',
          action: 'SET_DIFFICULTY',
          reason: '',
          score: 0.5,
        });
      }
    }

    // 膜型檢查（根據圖片規則）
    if (params.value.film_type) {
      reasons.push(`膜型：${params.value.film_type}`);
      hits.push({
        source: '膜型',
        rule: `膜型：${params.value.film_type}`,
        action: 'OK',
        reason: '',
        score: 0,
      });

      // 深色過柔感啞膠 → 特難
      if (params.value.lamination_type === '啞膠' && params.value.film_type === '柔感膜') {
        // 需要检查是否有深色（黑、紅），这里需要用户输入
        // 暂时标记，后续可以添加深色选项
        reasons.push('深色過柔感啞膠（特難）');
        hits.push({
          source: '膜型',
          rule: '深色(黑、紅)過柔感啞膠（特難）',
          action: 'SET_DIFFICULTY',
          reason: '深色過柔感啞膠',
          score: 0.5,
        });
      }

      // 過柔感啞膠 → 高難（如果不是深色）
      if (params.value.lamination_type === '啞膠' && params.value.film_type === '柔感膜') {
        // 如果不是深色，则为高難
        if (!reasons.some(r => r.includes('深色過柔感啞膠'))) {
          reasons.push('過柔感啞膠（高難）');
          hits.push({
            source: '膜型',
            rule: '過柔感啞膠（高難）',
            action: 'SET_DIFFICULTY',
            reason: '',
            score: 0.4,
          });
        }
      }

      // 大實地深色過啞膠 → 高難
      if (params.value.lamination_type === '啞膠' && 
          params.value.lamination_area_ratio !== undefined && 
          Number(params.value.lamination_area_ratio) >= 80) {
        reasons.push('大實地深色過啞膠（高難）');
        hits.push({
          source: '膜型',
          rule: '大實地深色過啞膠（高難）',
          action: 'SET_DIFFICULTY',
          reason: `面積=${params.value.lamination_area_ratio}%`,
          score: 0.4,
        });
      }

      // 大實地深色過光膠 → 難
      if (params.value.lamination_type === '光膠' && 
          params.value.lamination_area_ratio !== undefined && 
          Number(params.value.lamination_area_ratio) >= 80) {
        reasons.push('大實地深色過光膠（難）');
        hits.push({
          source: '膜型',
          rule: '大實地深色過光膠（難）',
          action: 'SET_DIFFICULTY',
          reason: `面積=${params.value.lamination_area_ratio}%`,
          score: 0.3,
        });
      }
    }

    // 其他過膠條件檢查（根據圖片規則）
    // 無粉面過膠 → 難
    if (params.value.no_powder_side) {
      reasons.push('無粉面過膠（難）');
      hits.push({
        source: '過膠條件',
        rule: '無粉面過膠（難）',
        action: 'SET_DIFFICULTY',
        reason: '',
        score: 0.3,
      });
    }

    // 虛度到紙邊<5mm → 高難（除皮殼外）
    if (params.value.bleed_edge_mm !== undefined && params.value.bleed_edge_mm !== null) {
      const bleedMm = Number(params.value.bleed_edge_mm);
      if (bleedMm < 5) {
        reasons.push(`虛度到紙邊${bleedMm}mm<5mm（高難）`);
        hits.push({
          source: '過膠條件',
          rule: `虛度到紙邊${bleedMm}mm<5mm（高難）`,
          action: 'SET_DIFFICULTY',
          reason: `${bleedMm}mm`,
          score: 0.4,
        });
      } else {
        reasons.push(`虛度到紙邊${bleedMm}mm`);
        hits.push({
          source: '過膠條件',
          rule: `虛度到紙邊${bleedMm}mm`,
          action: 'OK',
          reason: `${bleedMm}mm`,
          score: 0,
        });
      }
    }

    // 印張開長紋紙 → 高難
    if (params.value.long_grain_paper) {
      reasons.push('印張開長紋紙（高難）');
      hits.push({
        source: '過膠條件',
        rule: '印張開長紋紙（高難）',
        action: 'SET_DIFFICULTY',
        reason: '',
        score: 0.4,
      });
    }

    // LDP產品 → 高難
    if (params.value.ldp_product) {
      reasons.push('LDP產品（高難）');
      hits.push({
        source: '過膠條件',
        rule: 'LDP產品（高難）',
        action: 'SET_DIFFICULTY',
        reason: '',
        score: 0.4,
      });
    }

    // 雙面印刷 → 難
    if (params.value.double_side_print) {
      reasons.push('雙面印刷（難）');
      hits.push({
        source: '過膠條件',
        rule: '雙面印刷（難）',
        action: 'SET_DIFFICULTY',
        reason: '',
        score: 0.3,
      });
    }

    // 金/銀銻紙 → 高難（從适用界面中檢查）
    if (params.value.material === '金/銀銻紙') {
      reasons.push('金/銀銻紙（高難）');
      hits.push({
        source: '适用界面',
        rule: '金/銀銻紙（高難）',
        action: 'SET_DIFFICULTY',
        reason: '',
        score: 0.4,
      });
    }

    // 光柵片裱PP膜 → 特難（水性過膠機生產）（從膜型中檢查）
    if (params.value.film_type === '光柵片裱PP膜') {
      reasons.push('光柵片裱PP膜（特難）');
      hits.push({
        source: '膜型',
        rule: '光柵片裱PP膜(水性過膠機生產)（特難）',
        action: 'SET_DIFFICULTY',
        reason: '',
        score: 0.5,
      });
      // 自動選擇水性過膠機
      if (!resource) resource = 'WC04-LAM014 / 水性過膠機';
    }

    // 後工序檢查
    if (params.value.lamination_post_process) {
      const postProcessMap: Record<string, { name: string; difficulty?: string }> = {
        'none': { name: '無任何加工', difficulty: undefined },
        'deboss_emboss': { name: '擊凹/凸、絲印UV+擊凹/凸', difficulty: '高難' },
        'diecut_fold': { name: '啤折切、絲印UV+啤折切', difficulty: '高難' },
        'creasing': { name: '踩坑位、絲印UV+踩坑位', difficulty: '高難' },
        'texture': { name: '壓紋', difficulty: '難' },
      };
      const postProcess = postProcessMap[params.value.lamination_post_process];
      if (postProcess) {
        reasons.push(`後工序：${postProcess.name}`);
        if (postProcess.difficulty) {
          hits.push({
            source: '後工序',
            rule: `後工序：${postProcess.name}`,
            action: 'SET_DIFFICULTY',
            reason: postProcess.difficulty,
            score: postProcess.difficulty === '高難' ? 0.4 : 0.3,
          });
        } else {
          hits.push({
            source: '後工序',
            rule: `後工序：${postProcess.name}`,
            action: 'OK',
            reason: '',
            score: 0,
          });
        }
      }
    }

    // 印張任一邊≤310mm → 難
    if (params.value.len_mm !== undefined && params.value.wid_mm !== undefined) {
      const L = Number(params.value.len_mm);
      const W = Number(params.value.wid_mm);
      if (L <= 310 || W <= 310) {
        reasons.push('印張任一邊≤310mm（難）');
        hits.push({
          source: '尺寸',
          rule: '印張任一邊≤310mm（難）',
          action: 'SET_DIFFICULTY',
          reason: `L=${L}mm，W=${W}mm`,
          score: 0.3,
        });
      }
    }

    // 紙張克重檢查
    if (params.value.lamination_gsm !== undefined) {
      const gsm = Number(params.value.lamination_gsm);
      if (gsm < 100 || gsm > 500) {
        reasons.push(`紙張克重${gsm}gsm（需評估）`);
        hits.push({
          source: '紙張克重',
          rule: `紙張克重：${gsm}gsm`,
          action: 'CONSULT',
          reason: `建議範圍：100~500gsm`,
          score: 0.2,
        });
      } else {
        hits.push({
          source: '紙張克重',
          rule: `紙張克重：${gsm}gsm（適用）`,
          action: 'OK',
          reason: '',
          score: 0,
        });
      }
    }

    if (params.value.silk_screen) {
      reasons.push('絲印疊加工藝');
      hits.push({
        source: '疊加工藝',
        rule: '絲印+過膠',
        action: 'SET_DIFFICULTY',
        reason: '',
        score: 0.4,
      });
    }

    if (params.value.double_side_oil || params.value.double_side_uv) {
      reasons.push('雙面過油/UV');
      hits.push({
        source: '疊加工藝',
        rule: '雙面',
        action: 'SET_DIFFICULTY',
        reason: '',
        score: 0.2,
      });
    }

    if (
      params.value.lamination_area_ratio !== undefined &&
      params.value.lamination_area_ratio > 80
    ) {
      reasons.push('過膠面積>80%');
      hits.push({
        source: '面積',
        rule: '>80%',
        action: 'SET_DIFFICULTY',
        reason: '',
        score: 0.2,
      });
    }

    // 資源組選擇（根據材質/膜型/過膠類型細分）
    resource = 'WC04 / 過膠（按材質/膜型細分）';

    // 光柵片+雙面膠 → 專用機器
    if (params.value.material === '光柵片' && params.value.double_side_oil) {
      resource = 'WC04-LAM017 / 裱雙面膠機';
      hits.push({
        source: '材質',
        rule: '光柵片+雙面膠',
        action: 'SELECT_MACHINE',
        reason: '',
        score: 0.2,
      });
    }
    // 單粉紙 → 特定過膠線
    else if (params.value.material === '單粉紙' || params.value.material.includes('單粉')) {
      resource = 'WC04-LAM001 / 過膠線（單粉紙專用）';
      hits.push({
        source: '材質',
        rule: '單粉紙適用特定過膠線',
        action: 'SELECT_MACHINE',
        reason: '單粉紙',
        score: 0.2,
      });
    }
    // 根據過膠類型細分
    else if (params.value.lamination_type) {
      if (params.value.lamination_type === '光膠') {
        resource = 'WC04-LAM002 / 光膠過膠線';
        hits.push({
          source: '過膠類型',
          rule: '光膠適用專用過膠線',
          action: 'SELECT_MACHINE',
          reason: '',
          score: 0.1,
        });
      } else if (params.value.lamination_type === '啞膠') {
        resource = 'WC04-LAM003 / 啞膠過膠線';
        hits.push({
          source: '過膠類型',
          rule: '啞膠適用專用過膠線',
          action: 'SELECT_MACHINE',
          reason: '',
          score: 0.1,
        });
      } else if (['觸感膜', '抗刮膜', '立體膜'].includes(params.value.lamination_type)) {
        resource = 'WC04-LAM004 / 特種膜過膠線';
        hits.push({
          source: '過膠類型',
          rule: `${params.value.lamination_type}適用特種膜過膠線`,
          action: 'SELECT_MACHINE',
          reason: '',
          score: 0.1,
        });
      }
    }

    if (params.value.special_material) {
      canMachine = '需諮詢';
      hits.push({
        source: '材質',
        rule: '特殊材質需評估',
        action: 'CONSULT',
        reason: '',
        score: 0.3,
      });
    }
  }

  if (tab.value === 'diecut') {
    const dept_ok = (() => {
      if (!params.value.dept) return false;
      const stones = Number(params.value.stones || 0);
      if (params.value.dept === '咭書') return stones >= 10000;
      if (['精平裝', '綜合', '賀卡'].includes(params.value.dept)) return stones >= 5000;
      return false;
    })();

    const gsm_ok =
      params.value.sheet_gsm === undefined
        ? false
        : params.value.sheet_gsm >= 140 &&
          params.value.sheet_gsm <= 500 &&
          !params.value.is_sticker;

    const clean_outer_ok = !!params.value.clean_outer;
    const pin_ok =
      params.value.pin_spacing_mm === undefined
        ? false
        : params.value.pin_spacing_mm >= 25;

    const size_ok =
      params.value.len_mm && params.value.wid_mm
        ? params.value.len_mm >= 400 &&
          params.value.len_mm <= 1060 &&
          params.value.wid_mm >= 350 &&
          params.value.wid_mm <= 750
        : false;

    if (!dept_ok)
      hits.push({
        source: '部門/石數',
        rule: '咭書≥10000；精平裝/綜合/賀卡≥5000',
        action: 'HAND_FALLBACK',
        reason: `dept=${params.value.dept},stones=${params.value.stones}`,
        score: 0.3,
      });

    if (!gsm_ok)
      hits.push({
        source: '用紙',
        rule: '140~500gsm且非貼紙',
        action: 'HAND_FALLBACK',
        reason: `gsm=${params.value.sheet_gsm},貼紙=${params.value.is_sticker ? 'Y' : 'N'}`,
        score: 0.3,
      });

    if (!clean_outer_ok)
      hits.push({
        source: '工藝',
        rule: '需有清外圍',
        action: 'HAND_FALLBACK',
        reason: '無清外圍',
        score: 0.2,
      });

    if (!pin_ok)
      hits.push({
        source: '工裝',
        rule: '清廢針距≥25mm',
        action: 'HAND_FALLBACK',
        reason: `pin=${params.value.pin_spacing_mm}`,
        score: 0.3,
      });

    if (!size_ok)
      hits.push({
        source: '幅面',
        rule: '400×350~1060×750',
        action: 'HAND_FALLBACK',
        reason: `${params.value.len_mm}×${params.value.wid_mm}`,
        score: 0.3,
      });

    const auto_clear_all = dept_ok && gsm_ok && clean_outer_ok && pin_ok && size_ok;
    if (auto_clear_all) {
      canMachine = '可上機';
      resource = 'WC08-DCT029 / 全自動清廢';
      score += 0.2;
    } else {
      canMachine = '需手落';
      resource = 'WC08-DCT030 / 手落';
      score -= 0.1;
    }

    // 结构难度
    if (params.value.hole_size_mm !== undefined && params.value.hole_size_mm < 20) {
      reasons.push('清廢窿<20mm');
      hits.push({
        source: '結構',
        rule: '窿<20mm',
        action: 'SET_DIFFICULTY',
        reason: '',
        score: 0.4,
      });
    }

    if (params.value.paper_density_level === '實') {
      reasons.push('啤實度高');
      hits.push({
        source: '紙度',
        rule: '實',
        action: 'SET_DIFFICULTY',
        reason: '',
        score: 0.2,
      });
    }

    if (['風琴袋', '立體書', '多層件'].includes(params.value.structure_type)) {
      reasons.push(`結構複雜(${params.value.structure_type})`);
      hits.push({
        source: '結構',
        rule: '複雜結構',
        action: 'SET_DIFFICULTY',
        reason: '',
        score: 0.4,
      });
    }

    if (
      params.value.clean_area_ratio !== undefined &&
      params.value.clean_area_ratio > 60
    ) {
      reasons.push('清潔面積>60%');
      hits.push({
        source: '面積',
        rule: '>60%',
        action: 'SET_DIFFICULTY',
        reason: '',
        score: 0.2,
      });
    }

    if (params.value.fold_bag) {
      reasons.push('有風琴袋');
      hits.push({
        source: '結構',
        rule: '風琴袋',
        action: 'SET_DIFFICULTY',
        reason: '',
        score: 0.2,
      });
    }

    if (params.value.popup_ratio !== undefined && params.value.popup_ratio > 30) {
      reasons.push('立體書散件>30%');
      hits.push({
        source: '結構',
        rule: '散件>30%',
        action: 'SET_DIFFICULTY',
        reason: '',
        score: 0.4,
      });
    }

    if (params.value.edge_margin_mm !== undefined && params.value.edge_margin_mm < 5) {
      reasons.push('紙邊<5mm');
      hits.push({
        source: '尺寸',
        rule: '邊<5mm',
        action: 'SET_DIFFICULTY',
        reason: '',
        score: 0.2,
      });
    }

    if (params.value.joint_diagram) {
      reasons.push('有駁圖');
      hits.push({
        source: '圖面',
        rule: '有駁圖',
        action: 'SET_DIFFICULTY',
        reason: '',
        score: 0.2,
      });
    }
  }

  if (tab.value === 'silkscreen') {
    // ========== 絲印完整評估邏輯（根據圖片規則） ==========
    // 映射合併字段到原始字段（用於評估邏輯）
    if (params.value.silk_special_conditions) {
      params.value.need_registration = params.value.silk_special_conditions === 'need_registration';
      params.value.single_silk_area_gt_a4 = params.value.silk_special_conditions === 'single_silk_area_gt_a4';
      params.value.process_line_width_1mm = params.value.silk_special_conditions === 'process_line_width_1mm';
      params.value.silk_overlap_hotfoil = params.value.silk_special_conditions === 'silk_overlap_hotfoil';
      params.value.silk_ink_with_solvent = params.value.silk_special_conditions === 'silk_ink_with_solvent';
      params.value.japanese_stationery = params.value.silk_special_conditions === 'japanese_stationery';
      params.value.ldp_product_silk = params.value.silk_special_conditions === 'ldp_product_silk';
      params.value.fluorescent_color = params.value.silk_special_conditions === 'fluorescent_color';
      params.value.multi_turn_silk = params.value.silk_special_conditions === 'multi_turn_silk';
      params.value.card_sticking_layout = params.value.silk_special_conditions === 'card_sticking_layout';
    } else {
      // 重置所有特殊條件
      params.value.need_registration = false;
      params.value.single_silk_area_gt_a4 = false;
      params.value.process_line_width_1mm = false;
      params.value.silk_overlap_hotfoil = false;
      params.value.silk_ink_with_solvent = false;
      params.value.japanese_stationery = false;
      params.value.ldp_product_silk = false;
      params.value.fluorescent_color = false;
      params.value.multi_turn_silk = false;
      params.value.card_sticking_layout = false;
    }
    
    // 映射UV/金粉處理字段
    if (params.value.uv_gold_process) {
      const uvTypeMap: Record<string, string> = {
        'normal_uv': '普通UV',
        'frosted_uv': '磨砂UV',
        'rock_uv': '岩石UV',
        'orange_peel_uv': '橘皮UV',
        'colored_uv': '彩色UV',
        'wrinkled_uv': '皺紋UV',
        'uv_oven': '過UV烘箱',
        'quv': 'QUV',
        'leak_channel_gold': '漏/通道金粉',
      };
      params.value.uv_type = uvTypeMap[params.value.uv_gold_process] || '';
      params.value.double_side_uv_channel_gold = params.value.uv_gold_process === 'double_side_uv_channel_gold';
      params.value.single_uv_oven_pass = params.value.uv_gold_process === 'single_uv_oven';
    } else {
      params.value.uv_type = '';
      params.value.double_side_uv_channel_gold = false;
      params.value.single_uv_oven_pass = false;
    }
    
    // 映射前工序中的擊凸加工
    if (params.value.silk_pre_process === 'emboss_245_400') {
      params.value.emboss_gsm_range = '245-400';
    } else if (params.value.silk_pre_process === 'emboss_unlimited') {
      params.value.emboss_gsm_range = 'unlimited';
    } else {
      params.value.emboss_gsm_range = '';
    }
    
    const orderQty = params.value.silk_order_quantity !== undefined ? Number(params.value.silk_order_quantity) : undefined;
    const gsm = params.value.sheet_gsm !== undefined ? Number(params.value.sheet_gsm) : undefined;
    const areaRatio = params.value.silk_area_ratio !== undefined ? Number(params.value.silk_area_ratio) : undefined;
    const longSide = params.value.len_mm !== undefined ? Number(params.value.len_mm) : 0;
    const shortSide = params.value.wid_mm !== undefined ? Number(params.value.wid_mm) : 0;

    // 判斷應該使用哪種機器
    let machineType = ''; // 'auto' | 'manual' | 'glitter' | 'diecut'

    // ========== 1. 單只閃粉機 (WC06SSPO18) 判斷 ==========
    if (params.value.fluorescent_color) {
      // 熒光色不能用于單只閃粉機，應排除
      reasons.push('熒光色不能用于單只閃粉機（需排除）');
      hits.push({
        source: '機器選擇',
        rule: '熒光色不能用于單只閃粉機',
        action: 'EXCLUDE_MACHINE',
        reason: '單只閃粉機',
        score: -0.5,
      });
    } else if (gsm !== undefined && gsm >= 150 && gsm <= 450) {
      // 150g≤用紙克重≤450g → 單只閃粉機（特難）
      // 檢查尺寸限制：最大229×454mm，最小165.1×254mm
      if (longSide <= 229 && shortSide <= 454 && longSide >= 165.1 && shortSide >= 254) {
        machineType = 'glitter';
        difficulty = '特難';
        resource = 'WC06SSPO18 / 單只閃粉機';
        reasons.push('單只閃粉機適用（150-450g，尺寸符合）');
        hits.push({
          source: '機器選擇',
          rule: '單只閃粉機（150-450g）',
          action: 'SELECT_MACHINE',
          reason: `gsm=${gsm}, 尺寸=${longSide}×${shortSide}mm`,
          score: 0.3,
        });
      } else {
        reasons.push(`單只閃粉機尺寸限制：最大229×454mm，最小165.1×254mm，當前${longSide}×${shortSide}mm（超出限制）`);
        hits.push({
          source: '尺寸',
          rule: '單只閃粉機尺寸限制',
          action: 'CONSULT',
          reason: `超出限制`,
          score: 0.3,
        });
      }
    }

    // ========== 2. 絲印連自動啤機 (WC06SSPO19) 判斷 ==========
    if (machineType === '' && orderQty !== undefined && orderQty > 20000) {
      // 印件數量>20,000石 → 絲印連自動啤機（特難）
      // （淡季1-3月、9-12月，機器數量條件降至5,000石或以上）
      // 檢查尺寸限制：最大1050×750mm，最小400×360mm
      if (longSide <= 1050 && shortSide <= 750 && longSide >= 400 && shortSide >= 360) {
        machineType = 'diecut';
        difficulty = '特難';
        resource = 'WC06SSPO19 / 絲印連自動啤機';
        reasons.push(`絲印連自動啤機適用（訂單數量=${orderQty}石，尺寸符合）`);
        hits.push({
          source: '機器選擇',
          rule: '絲印連自動啤機（訂單數量>20,000石）',
          action: 'SELECT_MACHINE',
          reason: `數量=${orderQty}石`,
          score: 0.3,
        });
      } else {
        reasons.push(`絲印連自動啤機尺寸限制：最大1050×750mm，最小400×360mm，當前${longSide}×${shortSide}mm（超出限制）`);
        hits.push({
          source: '尺寸',
          rule: '絲印連自動啤機尺寸限制',
          action: 'CONSULT',
          reason: `超出限制`,
          score: 0.3,
        });
      }
    } else if (machineType === '' && gsm !== undefined && gsm >= 180 && gsm <= 450) {
      // 180g≤用紙克重≤450g → 絲印連自動啤機（特難）
      // 檢查尺寸限制
      if (longSide <= 1050 && shortSide <= 750 && longSide >= 400 && shortSide >= 360) {
        machineType = 'diecut';
        difficulty = '特難';
        resource = 'WC06SSPO19 / 絲印連自動啤機';
        reasons.push(`絲印連自動啤機適用（克重${gsm}g，尺寸符合）`);
        hits.push({
          source: '機器選擇',
          rule: '絲印連自動啤機（180-450g）',
          action: 'SELECT_MACHINE',
          reason: `gsm=${gsm}`,
          score: 0.3,
        });
      } else {
        reasons.push(`絲印連自動啤機尺寸限制：最大1050×750mm，最小400×360mm，當前${longSide}×${shortSide}mm（超出限制）`);
        hits.push({
          source: '尺寸',
          rule: '絲印連自動啤機尺寸限制',
          action: 'CONSULT',
          reason: `超出限制`,
          score: 0.3,
        });
      }
    }

    // ========== 3. 自動絲印機 (WC06SSPO16) vs 手落絲印機 (WC06SSPO17) 判斷 ==========
    if (machineType === '') {
      // 判斷條件：
      // - 自動絲印機：≥2000石，單只絲印面積>A4
      // - 手落絲印機：<2000石，或特定特殊工藝
      
      if (orderQty !== undefined && orderQty >= 2000 && params.value.single_silk_area_gt_a4) {
        machineType = 'auto';
        resource = 'WC06SSPO16 / 自動絲印機';
        reasons.push('自動絲印機適用（≥2000石，單只面積>A4）');
      } else if (orderQty !== undefined && orderQty < 2000) {
        machineType = 'manual';
        resource = 'WC06SSPO17 / 手落絲印機';
        reasons.push('手落絲印機適用（<2000石）');
      } else if (params.value.multi_turn_silk) {
        // 多轉絲印：第1轉用自動機，第2轉或以上用手落機
        machineType = 'manual';
        resource = 'WC06SSPO17 / 手落絲印機';
        reasons.push('手落絲印機適用（多轉絲印第2轉以上）');
      } else {
        // 默認使用自動絲印機
        machineType = 'auto';
        resource = 'WC06SSPO16 / 自動絲印機';
      }
    }

    // ========== 4. 難度判斷（根據選定的機器類型） ==========
    if (machineType === 'auto') {
      // 自動絲印機難度判斷
      // 特難條件
      if (orderQty !== undefined && orderQty >= 2000 && params.value.single_silk_area_gt_a4) {
        if (difficulty !== '特難') difficulty = '特難';
        reasons.push('自動絲印≥2000石，單只面積>A4（特難）');
      }
      if (params.value.emboss_gsm_range === '245-400') {
        if (difficulty !== '特難') difficulty = '特難';
        reasons.push('擊凸245-400g（特難）');
      }
      if (params.value.uv_type === '磨砂UV' || params.value.uv_type === '岩石UV' || params.value.uv_type === '過UV烘箱') {
        if (difficulty !== '特難') difficulty = '特難';
        reasons.push(`${params.value.uv_type}（特難）`);
      }
      if (params.value.silk_pre_process === 'lamination_120g' && gsm !== undefined && gsm >= 120) {
        if (difficulty !== '特難') difficulty = '特難';
        reasons.push('過膠後印件≥120g（特難）');
      }
      if (gsm !== undefined && (gsm < 90 || gsm > 450)) {
        if (difficulty !== '特難') difficulty = '特難';
        reasons.push(`用紙${gsm < 90 ? '<90g' : '>450g'}（需諮詢處理部門主管）（特難）`);
      }

      // 高難條件
      if (difficulty !== '特難') {
        if (params.value.silk_paper_type === '銀銻紙' && gsm !== undefined && gsm <= 157) {
          if (difficulty === '' || difficulty === '中' || difficulty === '難' || difficulty === '易') {
            difficulty = '高難';
          }
          reasons.push('銀銻紙≤157g（高難）');
        }
        if (params.value.gold_powder_turns !== undefined && params.value.gold_powder_turns >= 2) {
          if (difficulty === '' || difficulty === '中' || difficulty === '難' || difficulty === '易') {
            difficulty = '高難';
          }
          reasons.push(`金粉≥2轉（高難）`);
        }
        if (params.value.ldp_product_silk) {
          if (difficulty === '' || difficulty === '中' || difficulty === '難' || difficulty === '易') {
            difficulty = '高難';
          }
          reasons.push('所有LDP產品（高難）');
        }
        if (params.value.uv_type === '過UV烘箱' && params.value.silk_ink_with_solvent) {
          if (difficulty === '' || difficulty === '中' || difficulty === '難' || difficulty === '易') {
            difficulty = '高難';
          }
          reasons.push('絲印機生產擊凸粉加工印張（高難）');
        }
        if (params.value.silk_pre_process === 'soft_matte_lamination') {
          if (difficulty === '' || difficulty === '中' || difficulty === '難' || difficulty === '易') {
            difficulty = '高難';
          }
          reasons.push('過柔感啞膠後絲印（高難）');
        }
      }

      // 難條件
      if (difficulty !== '特難' && difficulty !== '高難') {
        if (areaRatio !== undefined && areaRatio >= 80) {
          difficulty = '難';
          reasons.push(`絲印面積≥80%（難）`);
        }
        if (params.value.solid_silk_layout_count !== undefined && params.value.solid_silk_layout_count > 2) {
          difficulty = '難';
          reasons.push(`實心絲印≥100×100mm布局>2（難）`);
        }
        if (longSide > 760 && longSide < 910 && shortSide >= 600) {
          difficulty = '難';
          reasons.push('印張760mm<長邊<910mm，寬邊≥600mm（難）');
        }
        if (params.value.other_side_process === 'lamination' || params.value.other_side_process === 'oiling') {
          difficulty = '難';
          reasons.push('另一面過膠/過油（難）');
        }
        if (params.value.silk_pre_process === 'deboss_emboss') {
          difficulty = '難';
          reasons.push('擊凸加工/自製擊凸紙（難）');
        }
        if (params.value.silk_overlap_hotfoil) {
          difficulty = '難';
          reasons.push('絲印與燙金圖案重疊（難）');
        }
        if (params.value.process_line_width_1mm) {
          difficulty = '難';
          reasons.push('處理線寬≤1mm（難）');
        }
        if (params.value.silk_book_name && ['CB-小狗警察', 'CB-小貓', 'CB-空軍上校'].includes(params.value.silk_book_name)) {
          difficulty = '難';
          reasons.push(`書名：${params.value.silk_book_name}（難）`);
        }
        if (orderQty !== undefined && orderQty <= 2500) {
          difficulty = '難';
          reasons.push(`工程數量≤2500石（難）`);
        }
        if (params.value.full_gold_powder_ratio === '2:1' || params.value.full_gold_powder_ratio === '1:1') {
          difficulty = '難';
          reasons.push(`全金粉比例${params.value.full_gold_powder_ratio}（難）`);
        }
      }

      // 中條件
      if (difficulty !== '特難' && difficulty !== '高難' && difficulty !== '難') {
        if (params.value.silk_paper_type === '銀銻紙' && gsm !== undefined && gsm > 157) {
          difficulty = '中';
          reasons.push('銀銻紙>157g（中）');
        }
        if (areaRatio !== undefined && areaRatio > 20 && areaRatio < 50 && !params.value.need_registration) {
          difficulty = '中';
          reasons.push('絲印面積20-50%，無需對位（中）');
        }
        if (areaRatio !== undefined && areaRatio >= 50 && areaRatio < 80) {
          difficulty = '中';
          reasons.push('絲印面積50-80%（中）');
        }
        if (params.value.solid_silk_layout_count !== undefined && params.value.solid_silk_layout_count <= 2) {
          difficulty = '中';
          reasons.push(`實心絲印≥100×100mm布局≤2（中）`);
        }
        if (params.value.need_registration) {
          difficulty = '中';
          reasons.push('需對位印件（中）');
        }
        if (params.value.silk_pre_process === 'deboss_emboss') {
          difficulty = '中';
          reasons.push('擊凹凸/掃金粉後絲印（中）');
        }
        if (['橘皮UV', '岩石UV', '磨砂UV', '普通UV', '彩色UV', '漏/通道金粉'].includes(params.value.uv_type)) {
          difficulty = '中';
          reasons.push(`${params.value.uv_type}（高UV能量，強油墨粘性）（中）`);
        }
        if (params.value.japanese_stationery) {
          difficulty = '中';
          reasons.push('日/德/韓/俄版本產品（中）');
        }
      }

      // 易條件（除涉及中/難/高難/特難）
      if (difficulty !== '特難' && difficulty !== '高難' && difficulty !== '難' && difficulty !== '中') {
        if (areaRatio !== undefined && areaRatio <= 20 && !params.value.need_registration) {
          difficulty = '易';
          reasons.push('局部UV面積≤20%，無需對位（易）');
        } else if (!params.value.need_registration) {
          difficulty = '易';
          reasons.push('絲印無需對位（易）');
        } else if (params.value.single_uv_oven_pass) {
          difficulty = '易';
          reasons.push('單UV烘箱過（易）');
        } else {
          difficulty = '中';
        }
      }

      // 自動絲印機尺寸限制
      // 最大生產尺寸：1020mm×710mm
      // 最小生產尺寸：370mm×330mm
      if (longSide > 1020 || shortSide > 710) {
        reasons.push(`自動絲印機最大尺寸1020×710mm，當前${longSide}×${shortSide}mm（超出限制）`);
        hits.push({
          source: '尺寸',
          rule: '自動絲印機最大尺寸限制',
          action: 'CONSULT',
          reason: `超出1020×710mm`,
          score: 0.3,
        });
      }
      if (longSide < 370 || shortSide < 330) {
        reasons.push(`自動絲印機最小尺寸370×330mm，當前${longSide}×${shortSide}mm（超出限制）`);
        hits.push({
          source: '尺寸',
          rule: '自動絲印機最小尺寸限制',
          action: 'CONSULT',
          reason: `小於370×330mm`,
          score: 0.3,
        });
      }
      
      // 短邊≥650MM → 特難（根據圖片規則）
      if (shortSide >= 650) {
        if (difficulty !== '特難') difficulty = '特難';
        reasons.push(`短邊≥650MM（特難）`);
        hits.push({
          source: '尺寸',
          rule: '短邊≥650MM（特難）',
          action: 'SET_DIFFICULTY',
          reason: `短邊=${shortSide}mm`,
          score: 0.5,
        });
      }

    } else if (machineType === 'manual') {
      // 手落絲印機難度判斷
      // 特難條件
      if (orderQty !== undefined && orderQty < 2000) {
        if (difficulty !== '特難') difficulty = '特難';
        reasons.push('手落絲印<2000石（特難）');
      }
      if (params.value.emboss_gsm_range === 'unlimited') {
        if (difficulty !== '特難') difficulty = '特難';
        reasons.push('擊凸（不限克重）（特難）');
      }
      if (params.value.multi_turn_silk) {
        if (difficulty !== '特難') difficulty = '特難';
        reasons.push('執行兩轉或以上絲印處理（特難）');
      }
      if (['瓦楞紙', '牛油紙', '塑料片', '紋理紙', '布紋紙'].includes(params.value.silk_paper_type)) {
        if (difficulty !== '特難') difficulty = '特難';
        reasons.push(`特殊材質：${params.value.silk_paper_type}（特難）`);
      }
      if (params.value.special_process && ['water_varnish_gold', 'foaming_paste', 'foaming_paste_gold', 'hotfoil_base_oil', 'pearl_ink', 'water_visible_transparent', 'colored_emboss_oil'].includes(params.value.special_process)) {
        if (difficulty !== '特難') difficulty = '特難';
        reasons.push(`特殊工藝：${params.value.special_process}（特難）`);
      }
      if (params.value.japanese_stationery) {
        if (difficulty !== '特難') difficulty = '特難';
        reasons.push('日本文具（特難）');
      }

      // 高難條件
      if (difficulty !== '特難') {
        if (params.value.silk_paper_type === '銀銻紙' && gsm !== undefined && gsm <= 157) {
          difficulty = '高難';
          reasons.push('銀銻紙≤157g（易磨損）（高難）');
        }
        if (areaRatio !== undefined && areaRatio >= 80) {
          difficulty = '高難';
          reasons.push('絲印面積≥80%（全頁對位困難）（高難）');
        }
        if (longSide >= 910 && longSide < 1185) {
          difficulty = '高難';
          reasons.push('910mm（約36英寸）≤長邊<1185mm（約46英寸）（高難）');
        }
        if (params.value.uv_leak_gold_area_sqin !== undefined && params.value.uv_leak_gold_area_sqin > 95) {
          difficulty = '高難';
          reasons.push(`UV漏/通道金粉總面積>95平方英寸（約A4尺寸）（慢開機，會粘合）（高難）`);
        }
        if (params.value.uv_type === '彩色UV' || params.value.uv_type === 'QUV') {
          difficulty = '高難';
          reasons.push(`${params.value.uv_type}（高UV能量，強油墨粘性）（高難）`);
        }
        if (params.value.silk_ink_with_solvent) {
          difficulty = '高難';
          reasons.push('絲印油墨（含溶劑）（高難）');
        }
        if (params.value.japanese_stationery) {
          difficulty = '高難';
          reasons.push('日本文具（高品質要求）（日本版本）（高難）');
        }
      }

      // 難條件
      if (difficulty !== '特難' && difficulty !== '高難') {
        if (areaRatio !== undefined && areaRatio >= 50 && areaRatio < 80) {
          difficulty = '難';
          reasons.push('絲印面積50-80%（難）');
        }
        if (params.value.special_process === 'foaming_paste' || params.value.special_process === 'water_visible_transparent') {
          difficulty = '難';
          reasons.push(`特殊工藝：${params.value.special_process}（難）`);
        }
        if (longSide >= 1185) {
          difficulty = '難';
          reasons.push(`長邊≥1185mm（難）`);
        }
        if (params.value.gold_powder_turns !== undefined && params.value.gold_powder_turns >= 2) {
          difficulty = '難';
          reasons.push(`金粉≥2轉（難）`);
        }
        if (params.value.ldp_product_silk) {
          difficulty = '難';
          reasons.push('所有LDP產品（難）');
        }
        if (params.value.full_gold_powder_ratio === '1.5:1' || params.value.full_gold_powder_ratio === '1:1') {
          difficulty = '難';
          reasons.push(`全金粉比例${params.value.full_gold_powder_ratio}（難）`);
        }
      }

      // 中條件
      if (difficulty !== '特難' && difficulty !== '高難' && difficulty !== '難') {
        if (gsm !== undefined && gsm > 100 && gsm < 157) {
          difficulty = '中';
          reasons.push('100g<用紙<157g（中）');
        }
        if (gsm !== undefined && gsm > 350) {
          difficulty = '中';
          reasons.push('用紙>350g（中）');
        }
        // 如果沒有觸發其他條件，默認為中
        if (difficulty === '' || difficulty === '易') {
          difficulty = '中';
        }
      }

      // 易條件（除涉及中/難/高難/特難）
      if (difficulty !== '特難' && difficulty !== '高難' && difficulty !== '難' && difficulty !== '中') {
        if (!params.value.need_registration) {
          difficulty = '易';
          reasons.push('絲印無需對位（易）');
        }
        if (params.value.single_uv_oven_pass) {
          difficulty = '易';
          reasons.push('UV烘箱過（易）');
        } else {
          difficulty = '中';
        }
      }
      
      // 手落絲印機最大機器生產面積：600mm×900mm
      if (longSide > 600 || shortSide > 900) {
        reasons.push(`手落絲印機最大機器生產面積600×900mm，當前${longSide}×${shortSide}mm（超出限制）`);
        hits.push({
          source: '尺寸',
          rule: '手落絲印機最大機器生產面積限制',
          action: 'CONSULT',
          reason: `超出600×900mm`,
          score: 0.3,
        });
      }

      // 手落絲印機尺寸限制
      const isThinPaper = gsm !== undefined && gsm <= 90;
      const maxLong = isThinPaper ? 535 : 711;
      const maxShort = isThinPaper ? 785 : 1000;
      if (longSide > maxLong || shortSide > maxShort) {
        reasons.push(`手落絲印機最大尺寸${maxLong}×${maxShort}mm，當前${longSide}×${shortSide}mm（超出限制）`);
        hits.push({
          source: '尺寸',
          rule: '手落絲印機最大尺寸限制',
          action: 'CONSULT',
          reason: `超出${maxLong}×${maxShort}mm`,
          score: 0.3,
        });
      }
      if (longSide < 50.8 || shortSide < 127) {
        reasons.push(`手落絲印機最小尺寸50.8×127mm，當前${longSide}×${shortSide}mm（超出限制）`);
        hits.push({
          source: '尺寸',
          rule: '手落絲印機最小尺寸限制',
          action: 'CONSULT',
          reason: `小於50.8×127mm`,
          score: 0.3,
        });
      }
    }

    // 雙面UV通道金粉特殊處理
    if (params.value.double_side_uv_channel_gold) {
      reasons.push('雙面UV通道金粉（自動機只能選一面，優先正面，另一面用手落機）');
      hits.push({
        source: '特殊工藝',
        rule: '雙面UV通道金粉需手落機配合',
        action: 'SELECT_MACHINE',
        reason: '另一面使用手落絲印機',
        score: 0.2,
      });
      // 如果當前是自動機，需要提醒另一面用手落機
      if (machineType === 'auto') {
        reasons.push('注意：雙面UV通道金粉，自動機只能選一面（優先正面），另一面需使用手落絲印機');
      }
    }

    // 絲印連自動啤機特殊條件檢查
    if (machineType === 'diecut') {
      // 不適用機器類型：
      // A: 音樂卡片、日本版本卡片
      // B: 印張多轉閃粉加工
      if (params.value.silk_book_name && params.value.silk_book_name.includes('音樂')) {
        reasons.push('音樂卡片不適用絲印連自動啤機（需手落機）');
        hits.push({
          source: '機器限制',
          rule: '音樂卡片不適用絲印連自動啤機',
          action: 'CONSULT',
          reason: '需改用其他機器',
          score: -0.3,
        });
      }
      if (params.value.japanese_stationery) {
        reasons.push('日本版本卡片不適用絲印連自動啤機（需手落機）');
        hits.push({
          source: '機器限制',
          rule: '日本版本卡片不適用絲印連自動啤機',
          action: 'CONSULT',
          reason: '需改用其他機器',
          score: -0.3,
        });
      }
      if (params.value.gold_powder_turns !== undefined && params.value.gold_powder_turns > 1) {
        reasons.push('多轉閃粉加工不適用絲印連自動啤機');
        hits.push({
          source: '機器限制',
          rule: '多轉閃粉加工不適用絲印連自動啤機',
          action: 'CONSULT',
          reason: `金粉轉數=${params.value.gold_powder_turns}`,
          score: -0.3,
        });
      }
      // 適用機器類型：
      // A: 絲印表面處理，啤機表面啤切（UV漏、UV通道）
      // B: UV通道閃粉需要印張220g或以上
      // C: 卡片粘貼布局面積不超過已排版印張面積的一半（獨立卡片粘貼布局不建議）
      if (params.value.uv_type === '漏/通道金粉' && gsm !== undefined && gsm < 220) {
        reasons.push('UV通道閃粉需要印張220g或以上（當前gsm可能不足）');
        hits.push({
          source: '機器條件',
          rule: 'UV通道閃粉≥220g',
          action: 'CONSULT',
          reason: `當前gsm=${gsm}`,
          score: 0.2,
        });
      }
      if (params.value.card_sticking_layout) {
        reasons.push('卡片粘貼布局面積不超過已排版印張面積的一半（獨立卡片粘貼布局不建議）');
        hits.push({
          source: '機器條件',
          rule: '卡片粘貼布局限制',
          action: 'OK',
          reason: '',
          score: 0,
        });
      }
    }

    // 設置資源組
    if (!resource) {
      resource = machineType === 'auto' ? 'WC06SSPO16 / 自動絲印機' : 
                 machineType === 'manual' ? 'WC06SSPO17 / 手落絲印機' :
                 machineType === 'glitter' ? 'WC06SSPO18 / 單只閃粉機' :
                 machineType === 'diecut' ? 'WC06SSPO19 / 絲印連自動啤機' :
                 'WC06 / 絲印（按規則選用）';
    }
  }

  if (tab.value === 'hotfoil') {
    // 燙金规则
    const material = MATERIALS.find((m) => m.code === params.value.material_code);
    if (!material) {
      // 如果没有选择物料，直接返回
      ruleHits.value = hits;
      metrics.value = {};
      candidates.value = [];
      recommended.value = null;
      return;
    }

    let baseScore = 1.0;

    // 材料窗口/温度检查
    if (params.value.job_temp_c !== undefined && params.value.job_temp_c !== null) {
      const [tmin, tmax] = material.temp_c;
      if (Number(params.value.job_temp_c) < tmin || Number(params.value.job_temp_c) > tmax) {
        reasons.push('溫度越窗');
        hits.push({
          source: '材料窗口',
          rule: `溫度越窗(${tmin}~${tmax}℃)`,
          action: 'SET_DIFFICULTY',
          reason: `job=${params.value.job_temp_c}℃`,
          score: 0.35,
        });
        baseScore -= 0.1;
      }
    }

    // 线宽检查
    if (params.value.line_thickness_mm !== undefined && Number(params.value.line_thickness_mm) < material.min_line_mm) {
      reasons.push('線寬極限');
      hits.push({
        source: '線寬',
        rule: `線寬極限≥${material.min_line_mm}mm`,
        action: 'SET_DIFFICULTY',
        reason: `job=${params.value.line_thickness_mm}mm`,
        score: 0.35,
      });
      baseScore -= 0.1;
    }

    // 面积检查
    if (params.value.area_ratio !== undefined && Number(params.value.area_ratio) > material.max_area_pct) {
      reasons.push('大面積');
      hits.push({
        source: '面積',
        rule: `大面積>${material.max_area_pct}%`,
        action: 'SET_DIFFICULTY',
        reason: `job=${params.value.area_ratio}%`,
        score: 0.3,
      });
      baseScore -= 0.1;
    }

    // 纸张匹配检查
    const paperTypeMap: Record<string, string> = {
      'coated': '銅版紙',
      'art': '藝術紙',
      'book': '書紙',
      'card': '咭紙',
      'special': '特種紙',
      'cloth': '布紋紙',
      'greaseproof': '牛油紙',
      'texture': '有紋理/壓紋',
      'other': '其他',
    };
    const paperTypeName = paperTypeMap[params.value.paper_type] || '';
    const hasTexture = params.value.paper_type === 'texture';
    const isClothPaper = params.value.paper_type === 'cloth';
    const isGreaseproofPaper = params.value.paper_type === 'greaseproof';
    
    // 纸张匹配检查（排除布纹纸、牛油纸、有纹理/压纹等特殊纸张）
    if (params.value.paper_type && paperTypeName && !['cloth', 'greaseproof', 'texture', 'other'].includes(params.value.paper_type)) {
      if (!material.papers.includes(paperTypeName)) {
        reasons.push('紙張不匹配');
        hits.push({
          source: '紙張',
          rule: '紙張不匹配',
          action: 'CONSULT',
          reason: paperTypeName,
          score: 0.25,
        });
        baseScore -= 0.05;
      }
    }
    
    // 有紋理/壓紋单独检查
    if (hasTexture) {
      reasons.push('紙張不匹配');
      hits.push({
        source: '紙張',
        rule: '有紋理/壓紋',
        action: 'CONSULT',
        reason: '有紋理/壓紋',
        score: 0.25,
      });
      baseScore -= 0.05;
    }

    // 对位检查
    if (params.value.registration) {
      reasons.push('需對位');
      hits.push({
        source: '對位',
        rule: '需與印刷對位',
        action: 'SET_DIFFICULTY',
        reason: '',
        score: 0.2,
      });
    }

    // 多次燙检查
    if (params.value.multi_pass) {
      reasons.push('多次燙');
      hits.push({
        source: '工藝',
        rule: '多次燙',
        action: 'SET_DIFFICULTY',
        reason: '',
        score: 0.25,
      });
    }


    // 幅面检查
    if (longSide > 1000 || shortSide > 700) {
      reasons.push('超幅面');
      hits.push({
        source: '幅面',
        rule: '超幅面(≤1000×700)',
        action: 'SET_DIFFICULTY',
        reason: `${params.value.len_mm}×${params.value.wid_mm}`,
        score: 0.2,
      });
    }

    // 尺寸限制检查（上機紙度生產最大/最小尺寸）
    // 滾筒機尺寸限制（根據圖片規則）
    let rollerMaxSize = { long: 640, short: 860 }; // 非賀咭
    let rollerMinSize = { long: 290, short: 400 }; // 非賀咭
    if (params.value.is_greeting_card) {
      // 賀咭類：最大609.6×889mm（24×35"），薄紙低於157gsm：450×600mm，最小292.1×406.4mm
      const isThinPaper = params.value.sheet_gsm !== undefined && params.value.sheet_gsm < 157;
      rollerMaxSize = isThinPaper ? { long: 450, short: 600 } : { long: 609.6, short: 889 };
      rollerMinSize = { long: 292.1, short: 406.4 };
    }

    // 一般機台尺寸限制（1060×760mm最大，450×360mm最小）
    const maxSize = longSide > 1060 || shortSide > 760;
    const minSize = longSide < 450 || shortSide < 360;
    
    // 滾筒機尺寸檢查
    const rollerMaxOK = longSide <= rollerMaxSize.long && shortSide <= rollerMaxSize.short;
    const rollerMinOK = longSide >= rollerMinSize.long && shortSide >= rollerMinSize.short;
    
    if (!rollerMaxOK || !rollerMinOK) {
      const cardType = params.value.is_greeting_card ? '賀咭' : '非賀咭';
      reasons.push(`尺寸超出滾筒機${cardType}範圍`);
      hits.push({
        source: '幅面',
        rule: `滾筒機${cardType}：最大${rollerMaxSize.long}×${rollerMaxSize.short}mm，最小${rollerMinSize.long}×${rollerMinSize.short}mm`,
        action: 'SET_DIFFICULTY',
        reason: `當前：${params.value.len_mm}×${params.value.wid_mm}mm`,
        score: 0.3,
      });
    }

    if (maxSize) {
      reasons.push('超最大尺寸');
      hits.push({
        source: '幅面',
        rule: '上機最大尺寸≤1060×760mm',
        action: 'SET_DIFFICULTY',
        reason: `${params.value.len_mm}×${params.value.wid_mm}`,
        score: 0.3,
      });
    }
    if (minSize) {
      reasons.push('小於最小尺寸');
      hits.push({
        source: '幅面',
        rule: '上機最小尺寸≥450×360mm',
        action: 'SET_DIFFICULTY',
        reason: `${params.value.len_mm}×${params.value.wid_mm}`,
        score: 0.3,
      });
    }

    // 手落機尺寸限制
    const manualMaxSize = 711.2 * 1016; // 最大生產尺寸
    const currentSize = longSide * shortSide;
    if (currentSize > manualMaxSize) {
      reasons.push('超出手落機最大尺寸');
      hits.push({
        source: '幅面',
        rule: '手落機最大生產尺寸711.2×1016mm',
        action: 'SET_DIFFICULTY',
        reason: `${params.value.len_mm}×${params.value.wid_mm}mm`,
        score: 0.4,
      });
    }

    // 拉金長度規則（1. 拉金長度）
    if (params.value.foil_length_mm !== undefined && params.value.foil_length_mm > 0) {
      const length = Number(params.value.foil_length_mm);
      if (length >= 580) {
        reasons.push('拉金長度≥580mm');
        hits.push({
          source: '拉金長度',
          rule: '拉金長度≥580mm（特難）',
          action: 'SET_DIFFICULTY',
          reason: `length=${length}mm`,
          score: 0.5,
        });
      } else if (length >= 451 && length < 580) {
        reasons.push('拉金長度451-580mm');
        hits.push({
          source: '拉金長度',
          rule: '拉金長度451-580mm（高難）',
          action: 'SET_DIFFICULTY',
          reason: `length=${length}mm`,
          score: 0.4,
        });
      } else if (length >= 301 && length < 451) {
        reasons.push('拉金長度301-450mm');
        hits.push({
          source: '拉金長度',
          rule: '拉金長度301-450mm（難）',
          action: 'SET_DIFFICULTY',
          reason: `length=${length}mm`,
          score: 0.3,
        });
      } else if (length >= 201 && length < 301) {
        reasons.push('拉金長度201-300mm');
        hits.push({
          source: '拉金長度',
          rule: '拉金長度201-300mm（中）',
          action: 'SET_DIFFICULTY',
          reason: `length=${length}mm`,
          score: 0.2,
        });
      } else if (length <= 200) {
        reasons.push('拉金長度≤200mm');
        hits.push({
          source: '拉金長度',
          rule: '拉金長度≤200mm（易）',
          action: 'OK',
          reason: `length=${length}mm`,
          score: 0,
        });
      }
    }

    // 紙克數規則（6. 紙克數）- 根據表格規則
    // 滾筒機適用紙張：80~550g，其他機台：100~1500g
    if (params.value.sheet_gsm !== undefined) {
      const gsm = Number(params.value.sheet_gsm);
      
      // 滾筒機適用範圍檢查（80~550g）
      const canUseRoller = gsm >= 80 && gsm <= 550;
      if (!canUseRoller && (gsm < 80 || gsm > 550)) {
        reasons.push('紙張超出滾筒機範圍');
        hits.push({
          source: '用紙',
          rule: '滾筒機適用80~550g',
          action: 'SET_DIFFICULTY',
          reason: `gsm=${gsm}，不適用滾筒機`,
          score: 0.2,
        });
      }

      // 一般適用範圍檢查（100~1500g）
      if (gsm < 100) {
        reasons.push('用紙<100gsm');
        hits.push({
          source: '用紙',
          rule: '最小100gsm（非滾筒機）',
          action: 'SET_DIFFICULTY',
          reason: `gsm=${gsm}`,
          score: 0.3,
        });
      } else if (gsm > 1500) {
        reasons.push('用紙>1500gsm');
        hits.push({
          source: '用紙',
          rule: '最大1500gsm',
          action: 'SET_DIFFICULTY',
          reason: `gsm=${gsm}`,
          score: 0.3,
        });
      } else {
        // 根據紙克數判斷難度（表格規則6）
        if (gsm <= 99) {
          reasons.push('紙克數≤99g');
          hits.push({
            source: '紙克數',
            rule: '紙克數≤99g（高難）',
            action: 'SET_DIFFICULTY',
            reason: `gsm=${gsm}`,
            score: 0.4,
          });
        } else if (gsm >= 100 && gsm <= 130) {
          reasons.push('紙克數100-130g');
          hits.push({
            source: '紙克數',
            rule: '紙克數100-130g（難）',
            action: 'SET_DIFFICULTY',
            reason: `gsm=${gsm}`,
            score: 0.3,
          });
        } else if (gsm >= 131 && gsm <= 160) {
          reasons.push('紙克數131-160g');
          hits.push({
            source: '紙克數',
            rule: '紙克數131-160g（中）',
            action: 'SET_DIFFICULTY',
            reason: `gsm=${gsm}`,
            score: 0.2,
          });
        } else if (gsm >= 161 && gsm <= 300) {
          reasons.push('紙克數161-300g');
          hits.push({
            source: '紙克數',
            rule: '紙克數161-300g（易）',
            action: 'OK',
            reason: `gsm=${gsm}`,
            score: 0,
          });
        } else if (gsm >= 301) {
          reasons.push('紙克數≥301g');
          hits.push({
            source: '紙克數',
            rule: '紙克數≥301g（高難）',
            action: 'SET_DIFFICULTY',
            reason: `gsm=${gsm}`,
            score: 0.4,
          });
        }
      }
    }

    // 數量限制（≥2000石）- 一般工程
    // 手落機數量限制：<2000石（手落機規則8）
    if (params.value.order_quantity !== undefined) {
      const qty = Number(params.value.order_quantity);
      
      // 手落機條件：數量<2000石
      const needManual = params.value.special_condition === 'film_production' || 
                         params.value.pre_process === 'after_mounting_3d' ||
                         ['after_mounting', 'special_material', 'japanese_stationery', 'flocked_surface'].includes(params.value.special_condition);
      
      if (needManual) {
        if (qty < 2000) {
          reasons.push('手落機數量<2000石');
          hits.push({
            source: '數量',
            rule: '手落機適用：<2000石',
            action: 'OK',
            reason: `${qty}石`,
            score: 0,
          });
        } else {
          reasons.push('數量≥2000石不適用手落機');
          hits.push({
            source: '數量',
            rule: '手落機僅適用<2000石工程',
            action: 'SET_DIFFICULTY',
            reason: `當前：${qty}石`,
            score: 0.3,
          });
        }
      } else {
        // 一般工程：≥2000石
        if (qty < 2000) {
          reasons.push('數量<2000石');
          hits.push({
            source: '數量',
            rule: '≥2000石的工程',
            action: 'SET_DIFFICULTY',
            reason: `當前：${qty}石，要求：≥2000石`,
            score: 0.2,
          });
        } else {
          hits.push({
            source: '數量',
            rule: '訂單數量符合要求',
            action: 'OK',
            reason: `${qty}石`,
            score: 0,
          });
        }
      }
    }

    // 前工序規則（整合為下拉框）
    if (params.value.pre_process === 'emboss_deboss') {
      reasons.push('壓紋/擊紋後燙金');
      hits.push({
        source: '前工序',
        rule: '壓紋/擊紋後燙金',
        action: 'SET_DIFFICULTY',
        reason: '',
        score: 0.3,
      });
    } else if (params.value.pre_process === 'texture') {
      reasons.push('擊紋');
      hits.push({
        source: '前工序',
        rule: '擊紋',
        action: 'SET_DIFFICULTY',
        reason: '',
        score: 0.3,
      });
    } else if (params.value.pre_process === 'deboss_print') {
      reasons.push('擊凹凸（純印張面）');
      hits.push({
        source: '前工序',
        rule: '擊凹凸（純印張面）',
        action: 'SET_DIFFICULTY',
        reason: '',
        score: 0.3,
      });
    }

    // 布紋紙（合併到适用界面）
    if (params.value.paper_type === 'cloth') {
      reasons.push('布紋紙');
      hits.push({
        source: '材質',
        rule: '布紋紙',
        action: 'SET_DIFFICULTY',
        reason: '',
        score: 0.3,
      });
    }

    // 有紋理/壓紋（合併到适用界面）
    if (params.value.paper_type === 'texture') {
      reasons.push('有紋理/壓紋');
      hits.push({
        source: '材質',
        rule: '有紋理/壓紋',
        action: 'SET_DIFFICULTY',
        reason: '',
        score: 0.3,
      });
    }

    // 燙金類型檢查（整合為下拉框）
    // 平面燙和立體燙的A5加工面積規則
    if (params.value.a5_count !== undefined && params.value.a5_count > 0) {
      const a5Count = Number(params.value.a5_count);
      const lineWidth = params.value.line_thickness_mm !== undefined ? Number(params.value.line_thickness_mm) : undefined;

      // 平面燙規則（根據線寬和A5數量判斷難度）
      if (params.value.foil_type !== '3d' && params.value.foil_type !== '') {
        if (a5Count >= 4 && lineWidth !== undefined && lineWidth >= 51) {
          reasons.push('平面燙≥4只A5且線寬≥51mm');
          hits.push({
            source: '平面燙',
            rule: '≥4只A5加工面積，燙金線寬≥51mm',
            action: 'SET_DIFFICULTY',
            reason: `A5=${a5Count}只，線寬=${lineWidth}mm`,
            score: 0.4,
          });
        } else if (a5Count >= 1 && a5Count <= 3 && lineWidth !== undefined && lineWidth >= 51) {
          reasons.push('平面燙1-3只A5且線寬≥51mm');
          hits.push({
            source: '平面燙',
            rule: '1-3只A5加工面積，燙金線寬≥51mm',
            action: 'SET_DIFFICULTY',
            reason: `A5=${a5Count}只，線寬=${lineWidth}mm`,
            score: 0.3,
          });
        } else if (a5Count >= 1 && a5Count <= 4 && lineWidth !== undefined && lineWidth >= 11 && lineWidth <= 50) {
          reasons.push('平面燙1-4只A5且線寬11-50mm');
          hits.push({
            source: '平面燙',
            rule: '1-4只A5加工面積，燙金線寬11-50mm',
            action: 'SET_DIFFICULTY',
            reason: `A5=${a5Count}只，線寬=${lineWidth}mm`,
            score: 0.2,
          });
        } else if (a5Count >= 1 && a5Count <= 4 && lineWidth !== undefined && lineWidth >= 2 && lineWidth <= 10) {
          reasons.push('平面燙1-4只A5且線寬2-10mm');
          hits.push({
            source: '平面燙',
            rule: '1-4只A5加工面積，燙金線寬2-10mm',
            action: 'SET_DIFFICULTY',
            reason: `A5=${a5Count}只，線寬=${lineWidth}mm`,
            score: 0.1,
          });
        } else if (a5Count >= 1 && a5Count <= 4 && lineWidth !== undefined && lineWidth < 2) {
          reasons.push('平面燙1-4只A5且線寬<2mm');
          hits.push({
            source: '平面燙',
            rule: '1-4只A5加工面積，燙金線寬<2mm（易）',
            action: 'OK',
            reason: `A5=${a5Count}只，線寬=${lineWidth}mm`,
            score: 0,
          });
        }
      }

      // 立體燙規則（根據單個面積和A5數量判斷難度）
      if (params.value.foil_type === '3d' && params.value.single_area_ratio) {
        const singleArea = params.value.single_area_ratio;
        
        if (a5Count >= 4 && (singleArea === '1_2_to_1' || singleArea === '1_4_to_1_2')) {
          reasons.push('立體燙≥4只A5且單個面積1/2-1A5');
          hits.push({
            source: '立體燙',
            rule: '≥4只A5加工面積，單個燙金面積1/2-1A5（特難）',
            action: 'SET_DIFFICULTY',
            reason: `A5=${a5Count}只，單個=${singleArea}`,
            score: 0.5,
          });
        } else if (a5Count >= 1 && a5Count <= 3 && (singleArea === '1_2_to_1' || singleArea === '1_4_to_1_2')) {
          reasons.push('立體燙1-3只A5且單個面積1/2-1A5');
          hits.push({
            source: '立體燙',
            rule: '1-3只A5加工面積，單個燙金面積1/2-1A5（高難）',
            action: 'SET_DIFFICULTY',
            reason: `A5=${a5Count}只，單個=${singleArea}`,
            score: 0.4,
          });
        } else if (a5Count >= 1 && a5Count <= 4 && (singleArea === '1_8_to_1_4' || singleArea === '1_4_to_1_2')) {
          reasons.push('立體燙1-4只A5且單個面積1/8-1/2A5');
          hits.push({
            source: '立體燙',
            rule: '1-4只A5加工面積，單個燙金面積1/8-1/2A5（難）',
            action: 'SET_DIFFICULTY',
            reason: `A5=${a5Count}只，單個=${singleArea}`,
            score: 0.3,
          });
        } else if (a5Count >= 1 && a5Count <= 4 && (singleArea === '1_16_to_1_8' || singleArea === '1_8_to_1_4')) {
          reasons.push('立體燙1-4只A5且單個面積1/16-1/8A5');
          hits.push({
            source: '立體燙',
            rule: '1-4只A5加工面積，單個燙金面積1/16-1/8A5（中）',
            action: 'SET_DIFFICULTY',
            reason: `A5=${a5Count}只，單個=${singleArea}`,
            score: 0.2,
          });
        } else if (a5Count >= 1 && a5Count <= 4 && singleArea === '1_16') {
          reasons.push('立體燙1-4只A5且單個面積≤1/16A5');
          hits.push({
            source: '立體燙',
            rule: '1-4只A5加工面積，單個燙金面積≤1/16A5（易）',
            action: 'OK',
            reason: `A5=${a5Count}只，單個=${singleArea}`,
            score: 0,
          });
        }
      }
    }

    if (params.value.foil_type === 'laser') {
      reasons.push('雷射');
      hits.push({
        source: '箔型',
        rule: '雷射',
        action: 'SET_DIFFICULTY',
        reason: '',
        score: 0.25,
      });
    }

    if (params.value.foil_type === 'emboss' || params.value.foil_type === 'full_foil') {
      if (params.value.emboss_depth_mm !== undefined && Number(params.value.emboss_depth_mm) > 0) {
        const depth = Number(params.value.emboss_depth_mm);
        if (depth > 0.5) {
          reasons.push('浮雕>0.5mm');
          hits.push({
            source: '結構',
            rule: '浮雕>0.5mm',
            action: 'SET_DIFFICULTY',
            reason: `depth=${params.value.emboss_depth_mm}mm`,
            score: 0.4,
          });
        } else {
          reasons.push('浮雕需求');
          hits.push({
            source: '結構',
            rule: '浮雕需求',
            action: 'SET_DIFFICULTY',
            reason: `depth=${params.value.emboss_depth_mm}mm`,
            score: 0.2,
          });
        }
      }
    }

    // 過膠後燙金規則 - 合併到前工序
    // 滾筒機適用條件（根據圖片規則）：
    // 3. 過膠後燙金線寬>5mm（非賀咭）
    // 4. 過膠後燙金線寬<=5mm且面積>A5（非賀咭）
    if (params.value.pre_process === 'after_lamination') {
      reasons.push('過膠後燙金');
      const lineWidth = params.value.line_thickness_mm !== undefined ? Number(params.value.line_thickness_mm) : undefined;
      const areaRatio = params.value.area_ratio !== undefined ? Number(params.value.area_ratio) : undefined;
      
      if (!params.value.is_greeting_card) {
        // 非賀咭類：線寬>5mm適用滾筒機
        if (lineWidth !== undefined && lineWidth > 5) {
          hits.push({
            source: '過膠後',
            rule: '過膠後燙金線寬>5mm（適用滾筒機）',
            action: 'PREFER_ROLLER',
            reason: `line=${lineWidth}mm`,
            score: 0.1,
          });
          if (!resource) resource = 'WC06-HOT004 / 滾筒燙金';
        }
        // 非賀咭類：線寬<=5mm且面積>A5適用滾筒機
        else if (lineWidth !== undefined && lineWidth <= 5 && areaRatio !== undefined && areaRatio > 0) {
          // A5大約是10-15%面積（假設），面積>A5應該是大於一定比例
          if (areaRatio > 15) {
            hits.push({
              source: '過膠後',
              rule: '過膠後燙金線寬≤5mm且面積>A5（適用滾筒機）',
              action: 'PREFER_ROLLER',
              reason: `line=${lineWidth}mm，area=${areaRatio}%`,
              score: 0.1,
            });
            if (!resource) resource = 'WC06-HOT004 / 滾筒燙金';
          } else {
            // 線寬<=5mm且面積<=A5，難度較低（平壓機）
            hits.push({
              source: '過膠後',
              rule: '過膠後燙金線寬≤5mm且面積≤A5（非賀咭）',
              action: 'OK',
              reason: `line=${lineWidth}mm，area=${areaRatio}%`,
              score: -0.1,
            });
          }
        } else {
          reasons.push('過膠後燙金條件不符');
          hits.push({
            source: '過膠後',
            rule: '過膠後燙金需明確線寬和面積',
            action: 'SET_DIFFICULTY',
            reason: '',
            score: 0.3,
          });
        }
      }
    }

    // 未過膠面燙金線寬>10mm（非賀咭）適用滾筒機（滾筒機規則5）
    if (!params.value.is_greeting_card && params.value.pre_process !== 'after_lamination') {
      if (params.value.line_thickness_mm !== undefined && Number(params.value.line_thickness_mm) > 10) {
        hits.push({
          source: '未過膠面',
          rule: '未過膠面燙金線寬>10mm（適用滾筒機）',
          action: 'PREFER_ROLLER',
          reason: `line=${params.value.line_thickness_mm}mm`,
          score: 0.1,
        });
        if (!resource) resource = 'WC06-HOT004 / 滾筒燙金';
      }
    }

    // 純紙面/水油面,燙金後無燙金科工序外的加工且線寬<10mm且不用套位
    if (params.value.post_process === 'none' && !params.value.registration) {
      if (params.value.line_thickness_mm !== undefined && Number(params.value.line_thickness_mm) < 10) {
        hits.push({
          source: '純紙面/水油面',
          rule: '燙金後無其他加工且線寬<10mm且不用套位（非賀咭）',
          action: 'OK',
          reason: '',
          score: -0.1,
        });
      }
    }

    // 燙金後過膠/過UV/過水油且線寬<10mm且金紙為緊身金紙且不用套位
    if (params.value.post_process === 'lamination' || params.value.post_process === 'uv' || params.value.post_process === 'oil') {
      if (params.value.line_thickness_mm !== undefined && Number(params.value.line_thickness_mm) < 10 && params.value.tight_foil && !params.value.registration) {
        hits.push({
          source: '燙金後加工',
          rule: '燙金後過膠/UV/水油且線寬<10mm且緊身金紙且不用套位（非賀咭）',
          action: 'OK',
          reason: '',
          score: -0.1,
        });
      } else {
        reasons.push('燙金後加工條件不符');
        hits.push({
          source: '燙金後加工',
          rule: '需線寬<10mm且緊身金紙且不用套位',
          action: 'SET_DIFFICULTY',
          reason: '',
          score: 0.2,
        });
      }
    }

    // 咭書類產品特殊規則（整合產品圖案）
    if (params.value.is_cardbook) {
      const hasReversePattern = params.value.product_pattern === 'solid_reverse' || params.value.product_pattern === 'reverse_line' || params.value.product_pattern === 'reverse_dot';
      if (hasReversePattern && params.value.reverse_line_dot_mm !== undefined && Number(params.value.reverse_line_dot_mm) <= 0.3) {
        const patternName = params.value.product_pattern === 'solid_reverse' ? '實地反白' : params.value.product_pattern === 'reverse_line' ? '反白線條' : '反白點';
        reasons.push(`咭書類${patternName}且線條/點≤0.3mm`);
        hits.push({
          source: '咭書類',
          rule: `${patternName}且線條/點≤0.3mm需滾筒機`,
          action: 'SELECT_MACHINE',
          reason: `line/dot=${params.value.reverse_line_dot_mm}mm`,
          score: 0.3,
        });
        // 需要滾筒機（咭書類實地反白且反白線條/點≤0.3mm）
        resource = 'WC06-HOT004 / 滾筒燙金機';
      } else if (hasReversePattern) {
        const patternName = params.value.product_pattern === 'solid_reverse' ? '實地反白' : params.value.product_pattern === 'reverse_line' ? '反白線條' : '反白點';
        reasons.push(`咭書類${patternName}`);
        hits.push({
          source: '咭書類',
          rule: `${patternName}`,
          action: 'SET_DIFFICULTY',
          reason: params.value.reverse_line_dot_mm ? `line/dot=${params.value.reverse_line_dot_mm}mm` : '',
          score: 0.2,
        });
      } else {
        hits.push({
          source: '咭書類',
          rule: '咭書類優選平壓/平',
          action: 'PREFER_FLAT',
          reason: '',
          score: 0.1,
        });
      }
    }
    
    // 產品圖案檢查（非咭書類）
    if (!params.value.is_cardbook && params.value.product_pattern && params.value.product_pattern !== '') {
      const patternName = params.value.product_pattern === 'solid_reverse' ? '實地反白' : params.value.product_pattern === 'reverse_line' ? '反白線條' : '反白點';
      reasons.push(`產品圖案：${patternName}`);
      hits.push({
        source: '產品圖案',
        rule: patternName,
        action: 'SET_DIFFICULTY',
        reason: params.value.reverse_line_dot_mm ? `line/dot=${params.value.reverse_line_dot_mm}mm` : '',
        score: 0.2,
      });
    }

    // 先燙金後印刷（合併到燙金後加工）
    if (params.value.post_process === 'print_after') {
      reasons.push('先燙金後印刷');
      hits.push({
        source: '燙金後加工',
        rule: '先燙金後印刷需精確壓力控制（難度+1級）',
        action: 'SET_DIFFICULTY',
        reason: '',
        score: 0.3,
      });
    }

    // 牛油紙規則（合併到适用界面）
    if (params.value.paper_type === 'greaseproof') {
      if (params.value.line_thickness_mm !== undefined && Number(params.value.line_thickness_mm) < 5) {
        reasons.push('牛油紙線寬<5mm需平壓平');
        hits.push({
          source: '牛油紙',
          rule: '牛油紙線寬<5mm落平壓平',
          action: 'SELECT_MACHINE',
          reason: `line=${params.value.line_thickness_mm}mm`,
          score: 0.2,
        });
      } else if (params.value.line_thickness_mm !== undefined && Number(params.value.line_thickness_mm) >= 5) {
        reasons.push('牛油紙線寬≥5mm');
        hits.push({
          source: '牛油紙',
          rule: '牛油紙線寬≥5mm',
          action: 'SET_DIFFICULTY',
          reason: `line=${params.value.line_thickness_mm}mm`,
          score: 0.3,
        });
      }
    }

    // 亞面燙金（備註1：相同條件下難度增加1級）
    if (params.value.foil_type === 'matte') {
      reasons.push('亞面燙金');
      hits.push({
        source: '燙金類型',
        rule: '亞面燙金（難度+1級）',
        action: 'SET_DIFFICULTY',
        reason: '',
        score: 0.3,
      });
    }

    // 備註2：前工序導致紙張彎曲/變形（難度+1級）
    const hasPreDeformation = (params.value.pre_process !== '' && params.value.pre_process !== 'after_lamination' && params.value.pre_process !== 'after_mounting_3d') || params.value.paper_type === 'texture';
    if (hasPreDeformation) {
      reasons.push('前工序導致變形');
      hits.push({
        source: '前工序',
        rule: '前工序導致紙張變形（難度+1級）',
        action: 'SET_DIFFICULTY',
        reason: '',
        score: 0.3,
      });
    }

    // 備註3：先燙金後印刷（難度+1級）- 已在上面處理

    // 備註4：處理面積增加（根據面積比例增加難度）
    if (params.value.area_ratio !== undefined) {
      const area = Number(params.value.area_ratio);
      if (area > 60) {
        reasons.push('大面積>60%');
        hits.push({
          source: '面積',
          rule: '大面積>60%（難度+1級）',
          action: 'SET_DIFFICULTY',
          reason: `area=${area}%`,
          score: 0.3,
        });
      }
    }

    // 色位限制規則
    if (params.value.has_color_limit) {
      reasons.push('有色位限制');
      hits.push({
        source: '色位限制',
        rule: '有色位限制（需選擇合適機台）',
        action: 'SET_DIFFICULTY',
        reason: '手落機不適用，需使用其他機台',
        score: 0.2,
      });
    }

    // 特殊條件（9. 雙面印刷內文/內頁，10. 需對菲林生產，書名等）
    if (params.value.special_condition === 'double_sided') {
      reasons.push('雙面印刷內文/內頁');
      hits.push({
        source: '特殊條件',
        rule: '雙面印刷內文/內頁（印張鬆散度下降，難吸紙或雙張）（高難）',
        action: 'SET_DIFFICULTY',
        reason: '',
        score: 0.4,
      });
    }

    if (params.value.special_condition === 'film_production') {
      reasons.push('需對菲林生產');
      hits.push({
        source: '特殊條件',
        rule: '需對菲林生產（僅限手落機）（現場判斷，產品工藝科不適用）',
        action: 'SET_DIFFICULTY',
        reason: '',
        score: 0.4,
      });
      // 需要手落機
      resource = 'WC06-HOT005 / 手落燙金機';
    }

    // 手落機特殊條件
    if (params.value.special_condition === 'after_mounting') {
      reasons.push('裱後燙金/連續裱後燙金');
      hits.push({
        source: '特殊條件',
        rule: '裱後燙金/連續裱後燙金（需手落機）',
        action: 'SELECT_MACHINE',
        reason: '',
        score: 0.4,
      });
      resource = 'WC06-HOT005 / 手落燙金機';
    }

    if (params.value.special_condition === 'special_material') {
      reasons.push('特殊材料');
      hits.push({
        source: '特殊條件',
        rule: '特殊材料（PU皮、布紋紙、皮革、氈等）（需手落機）',
        action: 'SELECT_MACHINE',
        reason: '',
        score: 0.4,
      });
      resource = 'WC06-HOT005 / 手落燙金機';
    }

    if (params.value.special_condition === 'japanese_stationery') {
      reasons.push('日本文具產品/GHD客戶');
      hits.push({
        source: '特殊條件',
        rule: '日本文具產品（非賀咭）、GHD客戶（需手落機）',
        action: 'SELECT_MACHINE',
        reason: '',
        score: 0.3,
      });
      resource = 'WC06-HOT005 / 手落燙金機';
    }

    if (params.value.special_condition === 'flocked_surface') {
      reasons.push('植毛面燙金');
      hits.push({
        source: '特殊條件',
        rule: '植毛面燙金（需手落機）',
        action: 'SELECT_MACHINE',
        reason: '',
        score: 0.4,
      });
      resource = 'WC06-HOT005 / 手落燙金機';
    }

    // 前工序：裱後擊凸/壓紋/3D燙金（需手落機）
    if (params.value.pre_process === 'after_mounting_3d') {
      reasons.push('裱後擊凸/壓紋/3D燙金');
      hits.push({
        source: '前工序',
        rule: '裱後擊凸/壓紋/3D燙金（需手落機）',
        action: 'SELECT_MACHINE',
        reason: '',
        score: 0.4,
      });
      resource = 'WC06-HOT005 / 手落燙金機';
    }

    // 燙金類型：雙面燙金（正反兩面重疊）（適用滾筒機）
    if (params.value.foil_type === 'double_side_hotfoil') {
      reasons.push('雙面燙金（正反兩面重疊）');
      hits.push({
        source: '燙金類型',
        rule: '雙面燙金（正反兩面重疊）（適用滾筒機）',
        action: 'PREFER_ROLLER',
        reason: '',
        score: 0.2,
      });
      // 傾向使用滾筒機
      if (!resource) resource = 'WC06-HOT004 / 滾筒燙金';
    }

    // 适用界面：膠片/透明膠片底紙（適用滾筒機）
    if (params.value.paper_type === 'film_base') {
      reasons.push('膠片/透明膠片底紙');
      hits.push({
        source: '适用界面',
        rule: '膠片/透明膠片底紙（適用滾筒機）',
        action: 'PREFER_ROLLER',
        reason: '',
        score: 0.2,
      });
      // 傾向使用滾筒機
      if (!resource) resource = 'WC06-HOT004 / 滾筒燙金';
    }

    if (params.value.special_condition === 'mesh_pattern') {
      reasons.push('網紋圖案');
      hits.push({
        source: '特殊條件',
        rule: '網紋圖案（使用緊身金紙）（適用滾筒機）',
        action: 'PREFER_ROLLER',
        reason: '',
        score: 0.2,
      });
      // 傾向使用滾筒機，且需要緊身金紙
      if (!params.value.tight_foil) {
        reasons.push('網紋圖案需緊身金紙');
        hits.push({
          source: '網紋圖案',
          rule: '網紋圖案需使用緊身金紙',
          action: 'REQUIRE_TIGHT_FOIL',
          reason: '',
          score: 0.1,
        });
      }
      if (!resource) resource = 'WC06-HOT004 / 滾筒燙金';
    }

    // 書名特殊規則
    if (params.value.special_condition === 'meixin_box') {
      reasons.push('美心月餅盒（書名）');
      hits.push({
        source: '書名',
        rule: '美心月餅盒（特難）',
        action: 'SET_DIFFICULTY',
        reason: '',
        score: 0.5,
      });
    }

    if (params.value.special_condition === 'cb_little_sister') {
      reasons.push('CB-小妹妹系列（書名）');
      hits.push({
        source: '書名',
        rule: 'CB-小妹妹系列（難）',
        action: 'SET_DIFFICULTY',
        reason: '',
        score: 0.3,
      });
    }

    if (params.value.special_condition === 'other_book' && params.value.book_name) {
      reasons.push(`書名：${params.value.book_name}`);
      hits.push({
        source: '書名',
        rule: `書名：${params.value.book_name}`,
        action: 'SET_DIFFICULTY',
        reason: '需現場判斷',
        score: 0.3,
      });
    }

    // 机台匹配
    const cands: any[] = [];
    MACHINES.forEach((m) => {
      let ok = true;

      // 幅面检查
      if (!(longSide <= m.bed[0] && shortSide <= m.bed[1])) ok = false;

      // 压力等级检查
      if (!m.pressure.includes(material.pressure)) ok = false;

      // 面积/线宽检查
      if (params.value.area_ratio !== undefined && Number(params.value.area_ratio) > m.max_area_pct) ok = false;
      if (params.value.line_thickness_mm !== undefined && Number(params.value.line_thickness_mm) < m.min_line_mm) ok = false;

      // 能力检查
      if (params.value.registration && !m.registration_ok) ok = false;
      if (params.value.emboss_depth_mm !== undefined && Number(params.value.emboss_depth_mm) > 0 && !m.emboss_support) ok = false;
      if (params.value.multi_pass && !m.multipass_ok) ok = false;
      
      // 色位限制檢查（如果有色位限制，某些機台可能不適用）
      if (params.value.has_color_limit) {
        // 手落機通常不適合有色位限制的工單
        if (m.type === 'manual') {
          ok = false;
        }
      }

      // 咭書類特殊要求：實地反白且反白線條/點≤0.3mm需要滾筒機
      const hasReversePattern = params.value.product_pattern === 'solid_reverse' || params.value.product_pattern === 'reverse_line' || params.value.product_pattern === 'reverse_dot';
      if (params.value.is_cardbook && hasReversePattern && params.value.reverse_line_dot_mm !== undefined && Number(params.value.reverse_line_dot_mm) <= 0.3) {
        if (m.type !== 'roll') ok = false;
      }


      // 需對菲林生產需要手落機
      if (params.value.special_condition === 'film_production') {
        if (m.type !== 'manual') ok = false;
      }

      // 手落機特殊條件（前工序：裱後擊凸/壓紋/3D燙金）
      if (params.value.pre_process === 'after_mounting_3d') {
        if (m.type !== 'manual') ok = false;
      }

      // 手落機特殊條件（特殊條件：裱後燙金、特殊材料、日本文具、植毛面）
      const manualConditions = ['after_mounting', 'special_material', 'japanese_stationery', 'flocked_surface'];
      if (manualConditions.includes(params.value.special_condition)) {
        if (m.type !== 'manual') ok = false;
      }

      // 滾筒機不能用于：壓紋燙金、有紋理紙、布紋紙、3D效果印刷（根據圖片規則）
      if (m.type === 'roll') {
        // 不能用于壓紋燙金（局部浮雕、全版覆燙）
        if (params.value.foil_type === 'emboss' || params.value.foil_type === 'full_foil') {
          ok = false;
        }
        // 不能用于有紋理紙、布紋紙
        if (params.value.paper_type === 'texture' || params.value.paper_type === 'cloth') {
          ok = false;
        }
        // 不能用于3D效果印刷（立體燙金）
        if (params.value.foil_type === '3d') {
          ok = false;
        }
      }

      // 滾筒機適用條件：膠片/透明膠片底紙、雙面燙金
      if (m.type === 'roll') {
        // 膠片/透明膠片底紙適用滾筒機
        if (params.value.paper_type === 'film_base') {
          // 適用，優先選擇
        }
        // 雙面燙金適用滾筒機
        if (params.value.foil_type === 'double_side_hotfoil') {
          // 適用，優先選擇
        }
      }

      // 滾筒機適用條件檢查
      if (m.type === 'roll') {
        // 適用紙張：80~550g
        if (params.value.sheet_gsm !== undefined) {
          const gsm = Number(params.value.sheet_gsm);
          if (gsm < 80 || gsm > 550) ok = false;
        }
        
        // 最大上機生產面積（版尺寸）：560×822mm
        // 檢查當前面積是否在滾筒機範圍內
        // 560×822mm約為對開面積，最大面積≤90%（已在max_area_pct中定義）
        
        // 滾筒機適用條件：燙金後印刷（非賀咭類）
        if (params.value.post_process === 'print_after' && !params.value.is_greeting_card) {
          // 適用滾筒機，優先選擇
        }
      }

      // 牛油紙線寬>=5mm適用滾筒機（滾筒機規則10）
      if (params.value.paper_type === 'greaseproof' && params.value.line_thickness_mm !== undefined) {
        const lineWidth = Number(params.value.line_thickness_mm);
        if (lineWidth >= 5) {
          // 適用滾筒機
          if (m.type !== 'roll') ok = false;
        } else {
          // 線寬<5mm需要平壓平
          if (m.type !== 'flat') ok = false;
        }
      }

      if (ok) {
        let machineScore = baseScore + m.bias;
        if ((params.value.foil_type === 'emboss' || params.value.foil_type === 'full_foil') && m.emboss_support) machineScore += 0.1;
        if (params.value.multi_pass && m.multipass_ok) machineScore += 0.05;
        if (params.value.registration && m.registration_ok) machineScore += 0.05;

        cands.push({
          code: m.code,
          name: m.name,
          type: m.type,
          width: `${m.bed[0]}×${m.bed[1]}`,
          history: m.hist,
          cost: m.cost,
          score: Number(machineScore.toFixed(2)),
        });
      }
    });

    cands.sort((a, b) => b.score - a.score);
    const best = cands[0]?.code ?? null;

    // 保存烫金候选资源组（在其他标签页评估前保存）
    if (tab.value === 'hotfoil') {
      candidates.value = cands;
      recommended.value = best;
    }
    score = baseScore;
  }

  // 计算难度等级
  // 裱紙優先檢查特難條件（160g≤底紙克重≤190g）
  if (tab.value === 'mounting' && params.value.bottom_gsm !== undefined) {
    const bottomGsm = Number(params.value.bottom_gsm);
    if (bottomGsm >= 160 && bottomGsm <= 190) {
      difficulty = '特難';
    }
  }

  // 過膠優先檢查特難條件（根據圖片規則）
  if (tab.value === 'lamination') {
    // 前工序特難條件
    if (params.value.lamination_pre_process && ['gold_powder', 'large_hotfoil', 'emboss', 'deboss', 'metallic_ink'].includes(params.value.lamination_pre_process)) {
      difficulty = '特難';
    }
    // 雙粉紙≤128g → 特難
    else if (params.value.material === '雙粉紙' && params.value.lamination_gsm !== undefined && Number(params.value.lamination_gsm) <= 128) {
      difficulty = '特難';
    }
    // 書紙≤90g → 特難
    else if (params.value.material === '書紙' && params.value.lamination_gsm !== undefined && Number(params.value.lamination_gsm) <= 90) {
      difficulty = '特難';
    }
    // 深色過柔感啞膠 → 特難
    else if (params.value.lamination_type === '啞膠' && params.value.film_type === '柔感膜') {
      // 需要检查是否有深色，这里先标记
      const hasDarkColor = true; // 需要从其他参数判断，暂时标记为特難
      if (hasDarkColor) difficulty = '特難';
    }
    // 原色牛皮紙過膠 → 特難
    else if (params.value.material === '牛皮紙' || params.value.material?.includes('牛皮')) {
      difficulty = '特難';
    }
    // 布紋紙、花紙 → 特難
    else if (params.value.material === '布紋紙' || params.value.material === '花紙') {
      difficulty = '特難';
    }
    // 短邊≥650MM → 特難（根據圖片規則）
    if (params.value.len_mm !== undefined && params.value.wid_mm !== undefined) {
      const shortSide = Math.min(Number(params.value.len_mm), Number(params.value.wid_mm));
      if (shortSide >= 650) {
        difficulty = '特難';
        reasons.push('短邊≥650MM（特難）');
        hits.push({
          source: '尺寸',
          rule: '短邊≥650MM（特難）',
          action: 'SET_DIFFICULTY',
          reason: `短邊=${shortSide}mm`,
          score: 0.5,
        });
      }
    }
  }
  
  const hardTriggers = ['UV油墨', '漸變', '金粉/銀粉', '三層', '清廢窿<20', '結構複雜', '散件>30', '板紙>2.8', '只能手裱', '多次燙', '浮雕>0.5', '雷射', '溫度越窗', '線寬極限', '超幅面', '立體燙金', '壓紋/擊紋後', '擊凹凸', '牛油紙線寬≥5', '拉金長度≥580', '紙克數≤99', '紙克數≥301', '平面燙≥4只A5且線寬≥51mm', '美心月餅盒', '雙面印刷', '需對菲林', '底紙160-190g（特難）', '掃金粉後過膠', '大實地燙金後過膠', '壓紋加工', '擊凹凸加工', '金屬油墨', '雙粉紙≤128g', '書紙≤90g', '深色過柔感啞膠', '原色牛皮紙過膠', '布紋紙', '花紙', '短邊≥650MM'];
  
  // 如果已經確定為特難，不再重新計算
  if (difficulty !== '特難') {
    if (reasons.some((r) => hardTriggers.some((h) => r.includes(h)))) {
      difficulty = '特難';
    } else if (hits.filter((h) => String(h.action).includes('SET_DIFFICULTY')).length >= 3) {
      difficulty = '高難';
    } else if (hits.filter((h) => String(h.action).includes('SET_DIFFICULTY')).length >= 1) {
      difficulty = '難';
    } else {
      // 檢查是否有易的條件（裱紙）
      if (tab.value === 'mounting' && reasons.some(r => r === '易')) {
        difficulty = '易';
      } else {
        difficulty = '中';
      }
    }
  }

  // 产能与准备时
  let prepMin = 20; // 默认
  let capacityPH = 2000; // 默认

  if (tab.value === 'diecut') {
    capacityPH = resource?.includes('DCT029') ? 3700 : 1200;
    const prepMap: any = { 易: 20, 中: 35, 難: 47, 高難: 70, 特難: 90 };
    prepMin = prepMap[difficulty] || 20;
  }
  
  if (tab.value === 'mounting') {
    // 裱紙產能（根據圖片規則）
    const prepMap: any = { 易: 15, 中: 20.4, 難: 30, 高難: 0, 特難: 60 }; // 分鐘數（易=0.25小時=15分鐘，中=0.34小時=20.4分鐘，難=0.5小時=30分鐘，特難=1小時=60分鐘）
    const capacityMap: any = { 易: 2400, 中: 2280, 難: 2100, 高難: 0, 特難: 720 }; // 每小時件數
    
    prepMin = prepMap[difficulty] || 20;
    capacityPH = capacityMap[difficulty] || 2280;
    
    // 根據資源組微調（如果有）
    // 滚筒和平张的產能可能略有不同，但圖片規則中沒有區分，保持統一
  }
  
  if (tab.value === 'lamination') {
    capacityPH = 2400;
    const prepMap: any = { 易: 20, 中: 35, 難: 47, 高難: 70, 特難: 90 };
    prepMin = prepMap[difficulty] || 20;
  }
  if (tab.value === 'silkscreen') {
    // 絲印產能（根據圖片規則）
    // 自動絲印機 (WC06SSPO16)
    if (resource?.includes('WC06SSPO16') || resource?.includes('自動絲印機')) {
      const prepMap: any = { 
        易: 0.08 * 60, // 0.08小時 = 4.8分鐘
        中: 0.08 * 60, // 0.08小時 = 4.8分鐘
        難: 0.13 * 60, // 0.13小時 = 7.8分鐘
        高難: 0.13 * 60, // 0.13小時 = 7.8分鐘
        特難: 0.13 * 60 // 0.13小時 = 7.8分鐘
      };
      const capacityMap: any = { 
        易: 2580,
        中: 2400,
        難: 2229,
        高難: 2023,
        特難: 1715
      };
      prepMin = prepMap[difficulty] || 8;
      capacityPH = capacityMap[difficulty] || 2400;
    }
    // 手落絲印機 (WC06SSPO17)
    else if (resource?.includes('WC06SSPO17') || resource?.includes('手落絲印機')) {
      const prepMap: any = { 
        易: 0.25 * 60, // 0.25小時 = 15分鐘
        中: 0.33 * 60, // 0.33小時 = 19.8分鐘
        難: 0.37 * 60, // 0.37小時 = 22.2分鐘
        高難: 0.4 * 60, // 0.4小時 = 24分鐘
        特難: 0.5 * 60 // 0.5小時 = 30分鐘
      };
      const capacityMap: any = { 
        易: 864,
        中: 700,
        難: 610,
        高難: 550,
        特難: 420
      };
      prepMin = prepMap[difficulty] || 20;
      capacityPH = capacityMap[difficulty] || 700;
    }
    // 單只閃粉機 (WC06SSPO18)
    else if (resource?.includes('WC06SSPO18') || resource?.includes('單只閃粉機')) {
      prepMin = 0.83 * 60; // 0.83小時 = 49.8分鐘（特難）
      capacityPH = 2500; // 特難
      difficulty = '特難';
    }
    // 絲印連自動啤機 (WC06SSPO19)
    else if (resource?.includes('WC06SSPO19') || resource?.includes('絲印連自動啤機')) {
      prepMin = 0.33 * 60; // 0.33小時 = 19.8分鐘（中）
      capacityPH = 2100; // 中（但實際上根據規則可能是特難）
      if (difficulty !== '特難') {
        // 根據規則，絲印連自動啤機通常是特難
        difficulty = '特難';
        capacityPH = 2500; // 根據圖片最後的判斷：特難2500
        prepMin = 0.83 * 60;
      }
    }
    // 默認值
    else {
      prepMin = 20;
      capacityPH = 2000;
    }
  }
  if (tab.value === 'hotfoil') {
    // 根据难度调整产能
    if (difficulty === '特難') capacityPH = 900;
    else if (difficulty === '高難') capacityPH = 1100;
    else if (difficulty === '難') capacityPH = 1300;
    else capacityPH = 1600;
  }

  metrics.value = { difficulty, prepMin, capacityPH };
  ruleHits.value = hits;

  // 候选资源组
  const cands: any[] = [];

  if (tab.value === 'diecut') {
    cands.push({
      code: 'WC08-DCT029',
      name: '全自動清廢啤機',
      type: 'auto',
      width: 1060,
      history: '90%',
      cost: 1.0,
      score: (score + 0.2).toFixed(2),
    });
    cands.push({
      code: 'WC08-DCT030',
      name: '手落',
      type: 'manual',
      width: 1060,
      history: '80%',
      cost: 0.95,
      score: (score - 0.1).toFixed(2),
    });
  } else if (tab.value === 'mounting') {
    cands.push({
      code: 'WC10-MPP009',
      name: '平張裱紙機',
      type: 'flat',
      width: 1200,
      history: '88%',
      cost: 1.05,
      score: (score + (resource?.includes('MPP009') ? 0.2 : 0)).toFixed(2),
    });
    cands.push({
      code: 'WC10-MPP008',
      name: '滾筒裱紙機',
      type: 'roll',
      width: 1200,
      history: '85%',
      cost: 1.0,
      score: (score + (resource?.includes('MPP008') ? 0.2 : 0)).toFixed(2),
    });
  } else if (tab.value === 'lamination') {
    // 根據資源組選擇顯示對應的候選資源組
    // 光柵片裱PP膜 → 水性過膠機（從膜型中檢查）
    if (params.value.film_type === '光柵片裱PP膜' || resource?.includes('LAM014')) {
      cands.push({
        code: 'WC04-LAM014',
        name: '水性過膠機',
        type: 'lam',
        width: '1200×800',
        history: '88%',
        cost: 1.05,
        score: (score + 0.25).toFixed(2),
      });
    } else if (resource?.includes('LAM017')) {
      // 光柵片+雙面膠
      cands.push({
        code: 'WC04-LAM017',
        name: '裱雙面膠機',
        type: 'lam',
        width: '1200×800',
        history: '86%',
        cost: 1.1,
        score: (score + 0.15).toFixed(2),
      });
    } else if (resource?.includes('LAM015') && (params.value.material === '單粉紙' || params.value.material?.includes('單粉'))) {
      // 單粉紙專用
      cands.push({
        code: 'WC04-LAM015',
        name: '過膠線（單粉紙專用）',
        type: 'lam',
        width: '1200×800',
        history: '92%',
        cost: 1.0,
        score: (score + 0.2).toFixed(2),
      });
    } else if (resource?.includes('LAM002')) {
      // 光膠過膠線
      cands.push({
        code: 'WC04-LAM002',
        name: '光膠過膠線',
        type: 'lam',
        width: '1200×800',
        history: '90%',
        cost: 1.0,
        score: (score + 0.1).toFixed(2),
      });
    } else if (resource?.includes('LAM003')) {
      // 啞膠過膠線
      cands.push({
        code: 'WC04-LAM003',
        name: '啞膠過膠線',
        type: 'lam',
        width: '1200×800',
        history: '90%',
        cost: 1.0,
        score: (score + 0.1).toFixed(2),
      });
    } else if (resource?.includes('LAM004')) {
      // 特種膜過膠線
      cands.push({
        code: 'WC04-LAM004',
        name: '特種膜過膠線',
        type: 'lam',
        width: '1200×800',
        history: '88%',
        cost: 1.05,
        score: (score + 0.1).toFixed(2),
      });
    } else {
      // 默認過膠線
      cands.push({
        code: 'WC04-LAM001',
        name: '過膠線',
        type: 'lam',
        width: '1200×800',
        history: '92%',
        cost: 1.0,
        score: (score + 0.1).toFixed(2),
      });
    }

    // 如果有其他適用的資源組，也可以添加
    // 例如：根據不同材質推薦不同的過膠線
    if (params.value.material === '單粉紙' || params.value.material?.includes('單粉')) {
      // 單粉紙已經在上面處理，這裡確保顯示
      if (!cands.find(c => c.code === 'WC04-LAM015')) {
        cands.unshift({
          code: 'WC04-LAM015',
          name: '過膠線（單粉紙專用）',
          type: 'lam',
          width: '1200×800',
          history: '92%',
          cost: 1.0,
          score: (score + 0.2).toFixed(2),
        });
      }
    }
    // 光柵片裱PP膜專用機器（從膜型中檢查）
    if (params.value.film_type === '光柵片裱PP膜' && !cands.find(c => c.code === 'WC04-LAM014')) {
      cands.unshift({
        code: 'WC04-LAM014',
        name: '水性過膠機',
        type: 'lam',
        width: '1200×800',
        history: '88%',
        cost: 1.05,
        score: (score + 0.25).toFixed(2),
      });
    }
  } else if (tab.value === 'silkscreen') {
    // 根據評估結果顯示對應的候選資源組
    if (resource?.includes('WC06SSPO16') || resource?.includes('自動絲印機')) {
      // 自動絲印機
      cands.push({
        code: 'WC06SSPO16',
        name: '自動絲印機',
        type: 'auto',
        width: '1020×710',
        history: '90%',
        cost: 1.0,
        score: (score + 0.15).toFixed(2),
      });
    } else if (resource?.includes('WC06SSPO17') || resource?.includes('手落絲印機')) {
      // 手落絲印機
      cands.push({
        code: 'WC06SSPO17',
        name: '手落絲印機',
        type: 'manual',
        width: '711×1000',
        history: '85%',
        cost: 1.05,
        score: (score + 0.1).toFixed(2),
      });
    } else if (resource?.includes('WC06SSPO18') || resource?.includes('單只閃粉機')) {
      // 單只閃粉機
      cands.push({
        code: 'WC06SSPO18',
        name: '單只閃粉機',
        type: 'glitter',
        width: '229×454',
        history: '88%',
        cost: 1.1,
        score: (score + 0.2).toFixed(2),
      });
    } else if (resource?.includes('WC06SSPO19') || resource?.includes('絲印連自動啤機')) {
      // 絲印連自動啤機
      cands.push({
        code: 'WC06SSPO19',
        name: '絲印連自動啤機',
        type: 'diecut',
        width: '1050×750',
        history: '92%',
        cost: 1.0,
        score: (score + 0.25).toFixed(2),
      });
    } else {
      // 默認顯示自動絲印機
      cands.push({
        code: 'WC06SSPO16',
        name: '自動絲印機',
        type: 'auto',
        width: '1020×710',
        history: '90%',
        cost: 1.0,
        score: (score + 0.1).toFixed(2),
      });
    }
  } else if (tab.value === 'hotfoil') {
    // 燙金候選資源組已經在評估邏輯中的機台匹配部分生成
    // 如果已經有候選資源組，直接使用；否則提供示例
    if (candidates.value.length > 0) {
      // 使用已生成的候選資源組
      cands.push(...candidates.value);
    } else {
      // 提供默認示例候選資源組（用於演示）
      cands.push({
        code: 'WC06-HOT001',
        name: '平平台燙金',
        type: 'flat',
        width: '1000×700',
        history: '90%',
        cost: 1.0,
        score: (score + 0.1).toFixed(2),
      });
      cands.push({
        code: 'WC06-HOT003',
        name: '多工位燙金',
        type: 'multi-pass',
        width: '1000×700',
        history: '82%',
        cost: 1.2,
        score: (score + 0.05).toFixed(2),
      });
      cands.push({
        code: 'WC06-HOT004',
        name: '滾筒燙金',
        type: 'roll',
        width: '1200×850',
        history: '78%',
        cost: 1.25,
        score: (score + 0.05).toFixed(2),
      });
    }
  }

  candidates.value = cands;
  const best = cands.length
    ? cands.reduce((a, b) => (Number(b.score) > Number(a.score) ? b : a), cands[0])
    : null;
  recommended.value = best ? best.code : null;
};

// 載入示例數據（用於演示）
const loadDemoExample = () => {
  if (tab.value === 'hotfoil') {
    // 燙金示例：設置一個常見的燙金場景
    params.value.paper_type = 'coated';
    params.value.sheet_gsm = 250;
    params.value.foil_type = 'gold_silver';
    params.value.area_ratio = 30;
    params.value.line_thickness_mm = 0.5;
    params.value.len_mm = 420;
    params.value.wid_mm = 297;
    params.value.registration = false;
    params.value.is_greeting_card = false;
    params.value.has_color_limit = false;
    params.value.material_code = MATERIALS[0]?.code || '';
    params.value.order_quantity = 5000;
    
    // 自動運行評估
    setTimeout(() => evaluate(), 100);
  } else if (tab.value === 'mounting') {
    // 裱紙示例
    params.value.bottom_type = '咭紙';
    params.value.open_type = '對開及以上';
    params.value.paper_density_level = '中';
    params.value.structure_type = '普通';
    setTimeout(() => evaluate(), 100);
  } else if (tab.value === 'diecut') {
    // 啤機示例
    params.value.structure_type = '普通';
    params.value.clear_hole_size = 30;
    setTimeout(() => evaluate(), 100);
  } else if (tab.value === 'lamination') {
    // 過膠示例
    params.value.lamination_type = '光膠';
    params.value.lamination_area_ratio = 50;
    params.value.lamination_sides = 'single';
    params.value.material = '銅版紙';
    params.value.lamination_gsm = 250;
    setTimeout(() => evaluate(), 100);
  } else if (tab.value === 'silkscreen') {
    // 絲印示例：設置一個常見的絲印場景（自動絲印機，中難度）
    params.value.silk_order_quantity = 5000; // 5000石
    params.value.sheet_gsm = 250;
    params.value.silk_paper_type = '銅版紙';
    params.value.silk_area_ratio = 40;
    params.value.solid_silk_layout_count = 1;
    params.value.uv_gold_process = 'single_uv_oven'; // 單UV烘箱過
    params.value.gold_powder_turns = undefined;
    params.value.full_gold_powder_ratio = '';
    params.value.silk_pre_process = '';
    params.value.other_side_process = '';
    params.value.special_process = '';
    params.value.silk_special_conditions = ''; // 無特殊條件
    params.value.silk_book_name = '';
    params.value.uv_leak_gold_area_sqin = undefined;
    
    // 設置尺寸
    params.value.len_mm = 420;
    params.value.wid_mm = 297;
    
    // 自動運行評估
    setTimeout(() => evaluate(), 100);
  }
};

const getRecommendationText = (code: string) => {
  if (code.includes('DCT029')) return '全自動清廢啤機';
  if (code.includes('MPP008')) return '滾筒裱紙機';
  if (code.includes('MPP009')) return '平張裱紙機';
  if (code.includes('LAM017')) return '裱雙面膠機';
  if (code.includes('WC06SSPO16')) return '自動絲印機';
  if (code.includes('WC06SSPO17')) return '手落絲印機';
  if (code.includes('WC06SSPO18')) return '單只閃粉機';
  if (code.includes('WC06SSPO19')) return '絲印連自動啤機';
  if (code.includes('SILK002')) return '高精度絲印機型';
  if (code.includes('SILK001')) return '標準絲印機型';
  if (code.includes('HOT001')) return '平平台燙金';
  if (code.includes('HOT002')) return '浮雕燙金';
  if (code.includes('HOT003')) return '多工位燙金';
  if (code.includes('HOT004')) return '滾筒燙金';
  if (code.includes('HOT005')) return '手落燙金';
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

.btn-secondary {
  border: 1px solid #d1d5db;
  border-radius: 12px;
  padding: 8px 14px;
  background: #fff;
  color: #374151;
  cursor: pointer;
}

.btn-secondary:hover {
  background: #f9fafb;
  border-color: #9ca3af;
}
</style>

