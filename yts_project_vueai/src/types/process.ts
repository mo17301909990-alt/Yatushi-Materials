export type ProcessType = 'printing' | 'hotStamping' | 'laminating' | 'screenPrinting' | 'silicone' | 'recommended' | 'techManagement';

export interface Process {
  id: ProcessType;
  name: string;
  description: string;
}
