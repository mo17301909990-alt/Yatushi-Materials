import request from '../request';
import type { GoldFoilProduct } from '@/types/goldFoil';

// 定义适用界面查询参数接口
export interface InterfaceQueryParams {
  oil6812Glossy33013440Dumb?: string; // 6812光水油3301/3440啞水油
  gvLedUvGlossMatte?: string; // GV LED UV 光油/啞油
  oilBasedGlossMattePowderPaper?: string; // 油性 光油
  oilBasedGlossMatteNonPowderPaper?: string; // 油性 哑油
  standardFuronghui22d?: string; // 普通-福融輝22D
  preCoatedHy120665?: string; // 預塗-HY1206/65
  highTackPreCoatedHy40?: string; // 超粘預塗HY40
  preCoatedEconomicalHighWearResistantYt008a?: string; // 預塗平價高耐磨YT008A
  preCoatedHighWearResistantTn008?: string; // 預塗高耐磨-TN008
  preCoatedStandardWearResistantKvmbF?: string; // 預塗普通耐磨-KVMB-F
  softTouchMatteLaminate6015a?: string; // 柔感啞膠-6015A
}

// 产品类型枚举
export enum ProductType {
  FineBindingCardBook = 1, // 精平裝/咭書
  GreetingCardPaperBag = 2, // 賀咭/紙袋
  Packaging = 3 // 包裝
}

// 烫金图案类型枚举
export enum PatternType {
  ThinLines = 1, // 4.1 淨線條/字母≪0.5mm
  MediumLines = 2, // 4.2 淨線條/字母0.5mm<X≪5mm
  ThickLines = 3, // 4.3 淨線條/字母5mm<X≪10mm
  SmallSolid = 4, // 4.4 淨實城10mm<X≪20mm
  MixedThinLines = 5, // 4.5 混合圖案≪0.5mm線條+實城
  MixedMediumLines = 6, // 4.6 混合圖案>0.5mm線條+≪20mm實城
  MediumSolid = 7, // 4.7 淨實城>10mm
  MixedThickLines = 8, // 4.8 混合圖案>0.5mm線條+實城
  LargeSolid = 9, // 4.9 淨實城>20mm
  MixedLargeLines = 10, // 4.10 混合圖案>0.5mm線條+>20mm實城
  NoDistinction = 11 // 4.11 不作區分
}

export interface ParseAndMatchResult {
  products: {
    items: GoldFoilProduct[]
    total: number
    pageSize: number
    currentPage: number
  }
  notices: any[]
}

export const goldFoilApi = {

  /**
   *
   * @returns 所有烫金纸产品数据
   */
  getAllProducts() {
    console.log('Calling getAllProducts API'); // 调试日志
    // request 的 baseURL 已是 /api，此处不要再写 /api/...，否则会变成 /api/api/...（仅靠代理偶然修正，易部署失败）
    return request.get('/gold-foil/products', { timeout: 120000 });
  },

  /**
   * 匹配查询：使用 GoldFoilQueryParams 参数进行查询
   * 这个方法支持更灵活的查询，特别是对于产品类型和烫金图案类型的处理
   * 可用于第一次匹配、第二次匹配和未来的第三次匹配
   *
   * @param params 查询参数，包含公司编号、产品类型、烫金图案类型等
   * @returns 匹配的烫金纸产品列表
   */
  getMatchProducts(params: any) {
    console.log('Calling getMatchProducts API with params:', params);

    // 打印请求信息
    console.log('Match API request:', {
      url: '/gold-foil/match',
      body: params
    });

    // baseURL 已为 /api，见 getAllProducts 注释
    // 使用 POST 方法发送请求，将参数作为请求体传递
    return request.post('/gold-foil/match', params);
  },
  /**
   * 获取去重的公司编号和型号列表（用于输入下拉建议）
   */
  getDistinctValues() {
    return request.get('/gold-foil/distinct-values');
  },
  /** AI 自然语言解析 + 匹配：用户输入自然语言，后端调用 AI 提取参数并执行匹配 */
  parseAndMatch(message: string) {
    return request.post<ParseAndMatchResult>('/ai/parse-and-match', { message });
  },
};
