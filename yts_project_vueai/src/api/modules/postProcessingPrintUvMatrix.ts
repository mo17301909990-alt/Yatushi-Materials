import request from '../request';

export interface MatrixImportResult {
  totalRows: number;
  created?: number;
  updated?: number;
  skipped?: number;
  errors?: number;
  errorMessages?: string[];
  message?: string;
}

/**
 * 燙金 + 印刷UV 矩陣導出/導入（對應《燙後加工－「燙金 + 印刷UV」指引書》）
 * 底層使用 post_processing_print_uv 的導出/導入接口。
 */
export const postProcessingPrintUvMatrixApi = {
  /** 導出為 Excel（與 UV 印刷配置管理同一數據源） */
  exportExcel: (): Promise<Blob> => {
    return request.get('/post-processing-print-uv/export', {
      responseType: 'blob',
      timeout: 120000,
    });
  },

  /** 導出為 Word（复刻指引書模版：普通全纸/镭射金纸/客人指定金纸 三塊表格）；POI 生成較慢，超時 5 分鐘 */
  exportWord: (): Promise<Blob> => {
    return request.get('/post-processing-print-uv/export/word', {
      responseType: 'blob',
      timeout: 300000,
    });
  },

  /**
   * 導入 Excel，並將後端返回格式適配為矩陣頁所需的 MatrixImportResult。
   * 後端返回：{ success, totalCount, successCount, failCount, message, errorMessages? }
   */
  importMatrix: async (
    file: File,
    _conflictStrategy: 'overwrite' | 'skip' = 'skip'
  ): Promise<MatrixImportResult> => {
    const formData = new FormData();
    formData.append('file', file);
    const res = await request.post<{
      success?: boolean;
      totalCount?: number;
      successCount?: number;
      failCount?: number;
      message?: string;
      errorMessages?: string[];
    }>('/post-processing-print-uv/import', formData, {
      headers: { 'Content-Type': 'multipart/form-data' },
      timeout: 120000,
    });
    const data = res.data ?? {};
    return {
      totalRows: data.totalCount ?? 0,
      created: data.successCount ?? 0,
      updated: 0,
      skipped: 0,
      errors: data.failCount ?? 0,
      errorMessages: data.errorMessages ?? [],
      message: data.message,
    } as MatrixImportResult;
  },
};
