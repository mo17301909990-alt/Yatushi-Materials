const PREFIX = '[YTS]'
export const logger = {
  info: (msg: string) => console.log(`${PREFIX} ${msg}`),
  warn: (msg: string) => console.warn(`${PREFIX} ${msg}`),
  error: (msg: string) => console.error(`${PREFIX} ${msg}`),
}
