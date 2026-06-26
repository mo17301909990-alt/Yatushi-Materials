import { describe, it, expect, beforeEach } from 'vitest';
import { setActivePinia, createPinia } from 'pinia';
import { useHotStampingStore } from '@/stores/hotStamping';
import { useMatchingStore } from '@/stores/matchingStore';
import type { Product } from '@/stores/hotStamping';

describe('hotStamping store', () => {
  beforeEach(() => {
    setActivePinia(createPinia());
  });

  /* ================================================================
   * 1. Initial state
   * ================================================================ */
  describe('initial state', () => {
    it('searchResults 初始为空数组', () => {
      const store = useHotStampingStore();
      expect(store.searchResults).toEqual([]);
    });

    it('secondMatchResults 初始为空数组', () => {
      const store = useHotStampingStore();
      expect(store.secondMatchResults).toEqual([]);
    });

    it('thirdMatchResults 初始为空数组', () => {
      const store = useHotStampingStore();
      expect(store.thirdMatchResults).toEqual([]);
    });

    it('paginationInfo 初始为 null', () => {
      const store = useHotStampingStore();
      expect(store.getPaginationInfo()).toBeNull();
    });

    it('lastQueryParams 初始为 null', () => {
      const store = useHotStampingStore();
      expect(store.getLastQueryParams()).toBeNull();
    });

    it('bestMatch 初始为 null（当 matchingStore 无匹配时）', () => {
      const store = useHotStampingStore();
      expect(store.bestMatch).toBeNull();
    });
  });

  /* ================================================================
   * 2. Local state management — searchResults
   * ================================================================ */
  describe('setSearchResults', () => {
    it('设置搜索结果数组', () => {
      const store = useHotStampingStore();
      const results = [
        { id: 'MAT001', name: '普通金纸A' },
        { id: 'MAT002', name: '普通耐磨金纸A' },
      ];
      store.setSearchResults(results);
      expect(store.searchResults).toHaveLength(2);
      expect(store.searchResults[0].id).toBe('MAT001');
      expect(store.searchResults[1].name).toBe('普通耐磨金纸A');
    });

    it('设置为空数组', () => {
      const store = useHotStampingStore();
      store.setSearchResults([{ id: 'MAT001', name: '金纸A' }]);
      store.setSearchResults([]);
      expect(store.searchResults).toEqual([]);
    });

    it('多次设置覆盖旧值', () => {
      const store = useHotStampingStore();
      store.setSearchResults([{ id: 'MAT001' }]);
      store.setSearchResults([{ id: 'MAT002' }, { id: 'MAT003' }]);
      expect(store.searchResults).toHaveLength(2);
      expect(store.searchResults[0].id).toBe('MAT002');
    });
  });

  /* ================================================================
   * 3. Local state management — paginationInfo
   * ================================================================ */
  describe('paginationInfo', () => {
    it('setPaginationInfo 设置分页信息', () => {
      const store = useHotStampingStore();
      const info = { total: 100, pageSize: 10, currentPage: 1 };
      store.setPaginationInfo(info);
      expect(store.getPaginationInfo()).toEqual(info);
    });

    it('setPaginationInfo 设置含 totalPages 的分页信息', () => {
      const store = useHotStampingStore();
      const info = { total: 100, pageSize: 10, currentPage: 1, totalPages: 10 };
      store.setPaginationInfo(info);
      expect(store.getPaginationInfo()?.totalPages).toBe(10);
    });

    it('getPaginationInfo 获取未设置时返回 null', () => {
      const store = useHotStampingStore();
      expect(store.getPaginationInfo()).toBeNull();
    });

    it('多次设置覆盖旧值', () => {
      const store = useHotStampingStore();
      store.setPaginationInfo({ total: 50, pageSize: 5, currentPage: 1 });
      store.setPaginationInfo({ total: 200, pageSize: 20, currentPage: 3 });
      expect(store.getPaginationInfo()?.total).toBe(200);
      expect(store.getPaginationInfo()?.pageSize).toBe(20);
      expect(store.getPaginationInfo()?.currentPage).toBe(3);
    });

    it('边界值：total 为 0', () => {
      const store = useHotStampingStore();
      store.setPaginationInfo({ total: 0, pageSize: 10, currentPage: 0 });
      expect(store.getPaginationInfo()?.total).toBe(0);
      expect(store.getPaginationInfo()?.currentPage).toBe(0);
    });
  });

  /* ================================================================
   * 4. Local state management — lastQueryParams
   * ================================================================ */
  describe('lastQueryParams', () => {
    it('setLastQueryParams 设置查询参数', () => {
      const store = useHotStampingStore();
      const params = { surfaceType: '淨紙面', area: '中小面积', pattern: '普通' };
      store.setLastQueryParams(params);
      expect(store.getLastQueryParams()).toEqual(params);
    });

    it('setLastQueryParams 设置为 null', () => {
      const store = useHotStampingStore();
      store.setLastQueryParams({ key: 'value' });
      store.setLastQueryParams(null);
      expect(store.getLastQueryParams()).toBeNull();
    });

    it('getLastQueryParams 未设置时返回 null', () => {
      const store = useHotStampingStore();
      expect(store.getLastQueryParams()).toBeNull();
    });
  });

  /* ================================================================
   * 5. Delegated getters — matchingStore proxy
   * ================================================================ */
  describe('delegated matchingStore getters', () => {
    it('matchingParams 委托到 matchingStore', () => {
      const store = useHotStampingStore();
      const matchingStore = useMatchingStore();
      expect(store.matchingParams).toBe(matchingStore.matchingParams);
    });

    it('firstMatch 委托到 matchingStore', () => {
      const store = useHotStampingStore();
      const matchingStore = useMatchingStore();
      expect(store.firstMatch).toBe(matchingStore.firstMatch);
    });

    it('secondMatch 委托到 matchingStore', () => {
      const store = useHotStampingStore();
      const matchingStore = useMatchingStore();
      expect(store.secondMatch).toBe(matchingStore.secondMatch);
    });

    it('thirdMatch 委托到 matchingStore', () => {
      const store = useHotStampingStore();
      const matchingStore = useMatchingStore();
      expect(store.thirdMatch).toBe(matchingStore.thirdMatch);
    });

    it('otherMatches 委托到 matchingStore', () => {
      const store = useHotStampingStore();
      const matchingStore = useMatchingStore();
      expect(store.otherMatches).toEqual(matchingStore.otherMatches);
    });

    it('firstMatchParams 委托到 matchingStore', () => {
      const store = useHotStampingStore();
      const matchingStore = useMatchingStore();
      expect(store.firstMatchParams).toBe(matchingStore.firstMatchParams);
    });

    it('secondMatchParams 委托到 matchingStore', () => {
      const store = useHotStampingStore();
      const matchingStore = useMatchingStore();
      expect(store.secondMatchParams).toBe(matchingStore.secondMatchParams);
    });

    it('thirdMatchParams 委托到 matchingStore', () => {
      const store = useHotStampingStore();
      const matchingStore = useMatchingStore();
      expect(store.thirdMatchParams).toBe(matchingStore.thirdMatchParams);
    });

    it('searchMatchResult getter 委托到 matchingStore', () => {
      const store = useHotStampingStore();
      const matchingStore = useMatchingStore();
      expect(store.searchMatchResult).toBe(matchingStore.searchMatchResult);
    });
  });

  /* ================================================================
   * 6. searchMatchResult setter
   * ================================================================ */
  describe('searchMatchResult setter', () => {
    it('设置 searchMatchResult 委托到 matchingStore.setSearchMatchResult', () => {
      const store = useHotStampingStore();
      const matchingStore = useMatchingStore();
      const result = {
        name: '普通金纸A',
        model: 'PG-001',
        id: 'MAT001',
        score: 95,
        features: ['普通烫金'],
        scenarios: ['贺卡'],
        temperature: '110~130℃',
        width: '640mm',
        length: '120m',
        tension: '标准',
        costIndex: 1,
      };
      store.searchMatchResult = result;
      expect(matchingStore.searchMatchResult).toEqual(result);
      expect(store.searchMatchResult).toEqual(result);
    });

    it('设置 searchMatchResult 为 null', () => {
      const store = useHotStampingStore();
      store.searchMatchResult = null;
      expect(store.searchMatchResult).toBeNull();
    });
  });

  /* ================================================================
   * 7. Delegated actions
   * ================================================================ */
  describe('delegated actions', () => {
    it('updateMatchingParams 委托到 matchingStore', () => {
      const store = useHotStampingStore();
      const matchingStore = useMatchingStore();
      store.updateMatchingParams({ surfaceType: '淨紙面', area: '中小面积' });
      expect(matchingStore.matchingParams.surfaceType).toBe('淨紙面');
      expect(matchingStore.matchingParams.area).toBe('中小面积');
    });

    it('clearSearchResult 委托到 matchingStore', () => {
      const store = useHotStampingStore();
      const matchingStore = useMatchingStore();
      matchingStore.setSearchMatchResult({
        name: '测试',
        model: 'T-001',
        id: 'T001',
        score: 90,
        features: [],
        scenarios: [],
        temperature: '',
        width: '',
        length: '',
        tension: '',
        costIndex: 0,
      });
      store.clearSearchResult();
      expect(matchingStore.searchMatchResult).toBeNull();
    });

    it('setSecondMatchResult 委托到 matchingStore', () => {
      const store = useHotStampingStore();
      const matchingStore = useMatchingStore();
      const result = {
        name: '第二次匹配',
        model: 'SEC-001',
        id: 'SEC001',
        score: 80,
        features: [],
        scenarios: [],
        temperature: '',
        width: '',
        length: '',
        tension: '',
        costIndex: 0,
      };
      store.setSecondMatchResult(result);
      expect(matchingStore.secondMatchResult).toEqual(result);
    });

    it('setFirstMatchParams 委托到 matchingStore', () => {
      const store = useHotStampingStore();
      const matchingStore = useMatchingStore();
      store.setFirstMatchParams({ paramName: 'test', paramValue: 'value' });
      expect(matchingStore.firstMatchParams).toEqual({ paramName: 'test', paramValue: 'value' });
    });

    it('setSecondMatchParams 委托到 matchingStore', () => {
      const store = useHotStampingStore();
      const matchingStore = useMatchingStore();
      store.setSecondMatchParams({ paramName: 'second', paramValue: 'sec-val' });
      expect(matchingStore.secondMatchParams).toEqual({ paramName: 'second', paramValue: 'sec-val' });
    });

    it('setThirdMatchParams 委托到 matchingStore', () => {
      const store = useHotStampingStore();
      const matchingStore = useMatchingStore();
      store.setThirdMatchParams({ paramName: 'third', paramValue: 'third-val' });
      expect(matchingStore.thirdMatchParams).toEqual({ paramName: 'third', paramValue: 'third-val' });
    });

    it('setThirdMatchResult 委托到 matchingStore', () => {
      const store = useHotStampingStore();
      const matchingStore = useMatchingStore();
      const result = {
        name: '第三次匹配',
        model: 'THIRD-001',
        id: 'THIRD001',
        score: 70,
        features: [],
        scenarios: [],
        temperature: '',
        width: '',
        length: '',
        tension: '',
        costIndex: 0,
      };
      store.setThirdMatchResult(result);
      expect(matchingStore.thirdMatchResult).toEqual(result);
    });

    it('clearSecondMatchResult 委托到 matchingStore', () => {
      const store = useHotStampingStore();
      const matchingStore = useMatchingStore();
      matchingStore.setSecondMatchResult({
        name: '测试',
        model: 'T-001',
        id: 'T001',
        score: 90,
        features: [],
        scenarios: [],
        temperature: '',
        width: '',
        length: '',
        tension: '',
        costIndex: 0,
      });
      store.clearSecondMatchResult();
      expect(matchingStore.secondMatchResult).toBeNull();
    });
  });

  /* ================================================================
   * 8. Delegated actions — matchingStore firstMatch computed
   * ================================================================ */
  describe('firstMatch computed (via matchingStore)', () => {
    it('searchMatchResult 设置后 firstMatch 返回该结果', () => {
      const store = useHotStampingStore();
      const matchingStore = useMatchingStore();
      const result = {
        name: '普通金纸A',
        model: 'PG-001',
        id: 'MAT001',
        score: 95,
        features: ['普通烫金'],
        scenarios: ['贺卡'],
        temperature: '110~130℃',
        width: '640mm',
        length: '120m',
        tension: '标准',
        costIndex: 1,
      };
      matchingStore.setSearchMatchResult(result);
      expect(store.firstMatch).toEqual(result);
    });
  });

  /* ================================================================
   * 9. resetAllParams
   * ================================================================ */
  describe('resetAllParams', () => {
    it('重置所有本地状态和 matchingStore 状态', () => {
      const store = useHotStampingStore();
      const matchingStore = useMatchingStore();

      // 先设置一些值
      store.setSearchResults([{ id: 'MAT001', name: '金纸A' }]);
      store.setPaginationInfo({ total: 50, pageSize: 10, currentPage: 1 });
      store.setLastQueryParams({ surfaceType: '淨紙面' });
      matchingStore.updateMatchingParams({ surfaceType: '淨紙面', area: '中小面积' });
      matchingStore.setSearchMatchResult({
        name: '测试',
        model: 'T-001',
        id: 'T001',
        score: 90,
        features: [],
        scenarios: [],
        temperature: '',
        width: '',
        length: '',
        tension: '',
        costIndex: 0,
      });
      matchingStore.setSecondMatchResult({
        name: '第二次',
        model: 'S-001',
        id: 'S001',
        score: 80,
        features: [],
        scenarios: [],
        temperature: '',
        width: '',
        length: '',
        tension: '',
        costIndex: 0,
      });
      matchingStore.setFirstMatchParams({ paramName: 'first', paramValue: 'f' });

      // 执行重置
      store.resetAllParams();

      // 验证本地搜索结果被清空
      expect(store.searchResults).toEqual([]);
      expect(store.secondMatchResults).toEqual([]);
      expect(store.thirdMatchResults).toEqual([]);

      // 注意：resetAllParams 使用 setTimeout 延迟清空 searchResults
      // 但第一个立即赋值已发生，所以 searchResults 已经是 []
      // 验证 matchingStore 状态被重置
      expect(matchingStore.searchMatchResult).toBeNull();
      expect(matchingStore.secondMatchResult).toBeNull();
      expect(matchingStore.firstMatchParams).toBeNull();
      expect(matchingStore.matchingParams.surfaceType).toBe('');
      expect(matchingStore.matchingParams.area).toBe('');

      // firstMatch computed 应该返回 null
      expect(store.firstMatch).toBeNull();
    });

    it('重置空状态不报错', () => {
      const store = useHotStampingStore();
      expect(() => store.resetAllParams()).not.toThrow();
      expect(store.searchResults).toEqual([]);
    });
  });

  /* ================================================================
   * 10. getPapersByType
   * ================================================================ */
  describe('getPapersByType', () => {
    it('返回 "normal" 类型的产品列表', () => {
      const store = useHotStampingStore();
      const papers = store.getPapersByType('normal');
      expect(papers).toHaveLength(1);
      expect(papers[0].name).toBe('普通金纸A');
      expect(papers[0].model).toBe('PG-001');
    });

    it('返回 "normalWearResistant" 类型的产品列表', () => {
      const store = useHotStampingStore();
      const papers = store.getPapersByType('normalWearResistant');
      expect(papers).toHaveLength(1);
      expect(papers[0].name).toBe('普通耐磨金纸A');
      expect(papers[0].type).toBe('普通耐磨');
    });

    it('返回 "highWearResistant" 类型的产品列表', () => {
      const store = useHotStampingStore();
      const papers = store.getPapersByType('highWearResistant');
      expect(papers).toHaveLength(1);
      expect(papers[0].name).toBe('高耐磨金纸A');
      expect(papers[0].costIndex).toBe(3);
    });

    it('未知类型返回空数组', () => {
      const store = useHotStampingStore();
      const papers = store.getPapersByType('nonexistent');
      expect(papers).toEqual([]);
    });

    it('空字符串返回空数组', () => {
      const store = useHotStampingStore();
      const papers = store.getPapersByType('');
      expect(papers).toEqual([]);
    });
  });

  /* ================================================================
   * 11. updatePaper
   * ================================================================ */
  describe('updatePaper', () => {
    it('更新已存在的产品', () => {
      const store = useHotStampingStore();
      const updated: Product = {
        name: '普通金纸A-更新版',
        model: 'PG-001-UPDATED',
        id: 'MAT001',
        type: '普通金纸',
        temperature: '120~140℃',
        tension: '紧身',
        width: '640mm',
        length: '120m',
        scenarios: ['贺卡', '包装', '精平装'],
        features: ['普通烫金', '适合细线条'],
        validSurfaces: ['淨紙面', '單粉紙', '雙粉紙'],
        validAreas: ['中小面积', '中面积'],
        costIndex: 2,
        laminatingCompatibility: { status: 'all', compatibleTypes: ['普通预涂膜', 'UV过胶'] },
        uvPrintingCompatible: true,
        ledUvGlitterCompatible: true,
        printingCompatible: true,
        flatStampingCompatible: true,
        reliefStampingCompatible: true,
        matteStampingCompatible: true,
        glitterStampingCompatible: false,
      };
      store.updatePaper('normal', updated);
      const papers = store.getPapersByType('normal');
      expect(papers).toHaveLength(1);
      expect(papers[0].name).toBe('普通金纸A-更新版');
      expect(papers[0].costIndex).toBe(2);
    });

    it('未知类型不报错也不更新', () => {
      const store = useHotStampingStore();
      const updated: Product = {
        name: '不存在',
        model: 'N-A',
        id: 'N999',
        type: '未知',
        temperature: '',
        tension: '',
        width: '',
        length: '',
        scenarios: [],
        features: [],
        validSurfaces: [],
        validAreas: [],
        costIndex: 0,
        laminatingCompatibility: { status: 'none' },
        uvPrintingCompatible: false,
        ledUvGlitterCompatible: false,
        printingCompatible: false,
        flatStampingCompatible: false,
        reliefStampingCompatible: false,
        matteStampingCompatible: false,
        glitterStampingCompatible: false,
      };
      expect(() => store.updatePaper('nonexistent', updated)).not.toThrow();
      // 其他类型不受影响
      expect(store.getPapersByType('normal')).toHaveLength(1);
    });

    it('更新不存在的 ID 不报错', () => {
      const store = useHotStampingStore();
      const updated: Product = {
        name: '不存在的产品',
        model: 'N-A',
        id: 'IDONOTEXIST',
        type: '普通金纸',
        temperature: '',
        tension: '',
        width: '',
        length: '',
        scenarios: [],
        features: [],
        validSurfaces: [],
        validAreas: [],
        costIndex: 0,
        laminatingCompatibility: { status: 'none' },
        uvPrintingCompatible: false,
        ledUvGlitterCompatible: false,
        printingCompatible: false,
        flatStampingCompatible: false,
        reliefStampingCompatible: false,
        matteStampingCompatible: false,
        glitterStampingCompatible: false,
      };
      expect(() => store.updatePaper('normal', updated)).not.toThrow();
      // 原产品不变
      const papers = store.getPapersByType('normal');
      expect(papers[0].id).toBe('MAT001');
      expect(papers[0].name).toBe('普通金纸A');
    });
  });

  /* ================================================================
   * 12. Edge cases — cross-store interaction
   * ================================================================ */
  describe('cross-store interaction', () => {
    it('修改 matchingStore 值通过 hotStampingStore getter 可见', () => {
      const store = useHotStampingStore();
      const matchingStore = useMatchingStore();

      matchingStore.updateMatchingParams({ surfaceType: '雙粉紙', area: '大面积' });
      expect(store.matchingParams.surfaceType).toBe('雙粉紙');
      expect(store.matchingParams.area).toBe('大面积');
    });

    it('修改 hotStampingStore delegated action 影响 matchingStore', () => {
      const store = useHotStampingStore();
      const matchingStore = useMatchingStore();

      store.setFirstMatchParams({ paramName: 'companyNumber', paramValue: 'CN001' });
      expect(matchingStore.firstMatchParams.paramName).toBe('companyNumber');
      expect(matchingStore.firstMatchParams.paramValue).toBe('CN001');
    });
  });

  /* ================================================================
   * 13. Edge cases — null / undefined values
   * ================================================================ */
  describe('edge cases — null / undefined', () => {
    it('setSearchMatchResult null 能正确清除', () => {
      const store = useHotStampingStore();
      const matchingStore = useMatchingStore();
      matchingStore.setSearchMatchResult({
        name: 'test',
        model: 'T-001',
        id: 'T001',
        score: 90,
        features: [],
        scenarios: [],
        temperature: '',
        width: '',
        length: '',
        tension: '',
        costIndex: 0,
      });
      expect(store.searchMatchResult).not.toBeNull();
      store.searchMatchResult = null;
      expect(store.searchMatchResult).toBeNull();
    });

    it('setSecondMatchResult null 清除第二次匹配', () => {
      const store = useHotStampingStore();
      store.setSecondMatchResult({
        name: 'test',
        model: 'T-001',
        id: 'T001',
        score: 90,
        features: [],
        scenarios: [],
        temperature: '',
        width: '',
        length: '',
        tension: '',
        costIndex: 0,
      });
      expect(store.secondMatch).not.toBeNull();
      store.setSecondMatchResult(null);
      expect(store.secondMatch).toBeNull();
    });
  });
});
