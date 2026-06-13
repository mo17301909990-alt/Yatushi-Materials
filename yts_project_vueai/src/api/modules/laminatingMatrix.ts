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

export const laminatingMatrixApi = {
  exportExcel: (): Promise<Blob> => {
    return request.get('/laminating-matrix/export/excel', {
      responseType: 'blob',
      timeout: 120000,
    });
  },

  exportWord: (): Promise<Blob> => {
    return request.get('/laminating-matrix/export/word', {
      responseType: 'blob',
      timeout: 120000,
    });
  },

  importMatrix: (file: File, conflictStrategy: 'overwrite' | 'skip' = 'skip'): Promise<MatrixImportResult> => {
    const formData = new FormData();
    formData.append('file', file);
    formData.append('conflictStrategy', conflictStrategy);
    return request.post('/laminating-matrix/import', formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
      timeout: 120000,
    });
  },
};
