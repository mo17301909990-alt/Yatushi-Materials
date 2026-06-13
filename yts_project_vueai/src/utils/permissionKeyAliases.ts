/**
 * 與後端 PermissionLegacyKeys 對齊：庫裡曾用駝峰/舊菜單鍵時，前端路由與 v-permission 仍使用規範鍵校驗。
 * 用戶只要擁有規範鍵或下列任一「舊鍵」即視為具備該權限。
 * tech:management:view 故意不映射 process:management，避免僅勾總菜單「工藝配置」時頂欄仍顯示技術管理。
 */
export const PERMISSION_KEY_ALTERNATIVES: Record<string, readonly string[]> = {
  'material:hot-stamping-material:view': ['material:hotstamping:view'],
  'material:product-type:view': ['material:productType:view'],
  'material:hot-stamping-type:view': ['material:hotstampingType:view'],
  'material:pre-processing-interface:view': ['material:preProcessing:view'],
  'material:applicable-interface:view': ['material:applicableInterface:view'],
  'material:smart-compatibility:view': ['material:smartCompatibility:view'],
  'material:hot-stamping-pattern:view': ['material:hotstampingPattern:view'],
  'material:hot-stamping-pattern-area:view': ['material:hotstampingPatternArea:view'],
  'material:post-processing:view': ['material:postProcessing:view'],
  'material:post-processing-print:view': ['material:postProcessingPrint:view'],
  'material:post-processing-uv:view': ['material:postProcessingPrintUv:view'],
  'material:post-processing-leduvglitter:view': ['material:postProcessingLeduvglitter:view'],
  'material:embossing-common-interface:view': ['material:commonInterfaceMapping:view'],
  'material:embossing-wear-resistant-gold-paper:view': ['material:wearResistantGoldPaperMapping:view'],
  'material:laminating:view': ['material:lamination:view'],
  'match:preference:view': ['matchPreference:view', 'matchPreference:management'],
  'permission:manage:view': ['permission:view'],
  'matching:tag:select': ['matching:management'],
  'matching:tag:remove': ['matching:management'],
  'material:rule:view': ['material:management'],
  'process:hotstamping:view': ['process:management', 'matching:hotstamping:view'],
  'matching:hotstamping:view': ['process:hotstamping:view', 'process:management'],
  'process:other:view': ['process:management'],
  'matching:printing:view': ['process:other:view'],
  'matching:lamination:view': ['process:other:view'],
  'matching:silkscreen:view': ['process:other:view']
}

export function userHasPermissionKey(userKeys: string[], requiredKey: string): boolean {
  if (!requiredKey) return false
  if (userKeys.includes(requiredKey)) return true
  const alts = PERMISSION_KEY_ALTERNATIVES[requiredKey]
  if (alts?.some((a) => userKeys.includes(a))) return true
  return false
}
