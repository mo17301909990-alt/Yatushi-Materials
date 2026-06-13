export interface GoldFoilProduct {
  name: string;
  modelNumber: string;
  materialNumber: string;
  color: string;
  size: string;
  tightness: string;
  temperatureRange: string;
  performance: string;
  flatHotStamping: string;
  embossedGoldStamping: string;
  refractiveHolographicPatternedTexturedHotStamping: string;
  postHotStampingEmbossingDebossing: string;
  price: number;
  companyNumber: string;
  gpNo: string;
  uvPrinting: string; // 添加燙後加工.印刷UV字段
  ledUvGlitter: string; // 添加燙後加工.絲印LED UV灑閃粉字段
  stampingPrinting: string; // 添加燙後加工.烫金+印刷字段
}