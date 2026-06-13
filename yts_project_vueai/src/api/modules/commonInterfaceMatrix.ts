import request from '../request';

export interface MatrixImportResult {
  totalRows: number;
  created: number;
  updated: number;
  skipped: number;
  errors: number;
  errorMessages?: string[];
}

export const commonInterfaceMatrixApi = {
  /** 导出矩阵为 Excel (.xlsx)，超时 2 分钟 */
  exportExcel: (): Promise<Blob> => {
    return request.get('/common-interface-matrix/export/excel', {
      responseType: 'blob',
      timeout: 120000,
    });
  },

  /** 导出矩阵为 Word (.docx)，超时 2 分钟 */
  exportWord: (): Promise<Blob> => {
    return request.get('/common-interface-matrix/export/word', {
      responseType: 'blob',
      timeout: 120000,
    });
  },

  /** 导入矩阵文件（支持 .xlsx / .docx），超时 5 分钟（大文件或复杂表头解析较慢） */
  importMatrix: (file: File, conflictStrategy: 'overwrite' | 'skip' = 'skip'): Promise<MatrixImportResult> => {
    const formData = new FormData();
    formData.append('file', file);
    formData.append('conflictStrategy', conflictStrategy);
    return request.post('/common-interface-matrix/import', formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
      timeout: 300000,
    });
  },
};
