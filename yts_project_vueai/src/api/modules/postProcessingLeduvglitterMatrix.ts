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
 * 燙金 + 絲印LED UV灑閃粉 矩陣導出/導入（配對燙金紙型號）
 * 對應加工重疊組合「燙金 + 絲印LED UV灑閃粉」與燙金界面的兼容性矩陣。
 */
export const postProcessingLeduvglitterMatrixApi = {
  exportExcel: (): Promise<Blob> => {
    return request.get('/post-processing-leduvglitter/matrix/export/excel', {
      responseType: 'blob',
      timeout: 120000,
    });
  },

  exportWord: (): Promise<Blob> => {
    return request.get('/post-processing-leduvglitter/matrix/export/word', {
      responseType: 'blob',
      timeout: 120000,
    });
  },

  importMatrix: (file: File, conflictStrategy: 'overwrite' | 'skip' = 'skip'): Promise<MatrixImportResult> => {
    const formData = new FormData();
    formData.append('file', file);
    formData.append('conflictStrategy', conflictStrategy);
    return request.post('/post-processing-leduvglitter/matrix/import', formData, {
      headers: { 'Content-Type': 'multipart/form-data' },
      timeout: 120000,
    });
  },
};
