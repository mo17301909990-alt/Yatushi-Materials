import axios from 'axios'

const API_BASE_URL = '/api'

/** 與後端 SystemContentServiceImpl.KEY_HOT_STAMPING_MATERIAL_GUIDE 一致 */
export const HOT_STAMPING_MATERIAL_GUIDE_KEY = 'hot_stamping_material_guide'

export interface SystemContentDto {
  id?: number
  contentKey?: string
  title?: string
  bodyHtml?: string
  updatedAt?: string
  updatedBy?: number
}

export const systemContentApi = {
  getByKey: async (contentKey: string): Promise<SystemContentDto> => {
    const response = await axios.get(`${API_BASE_URL}/system-content/${encodeURIComponent(contentKey)}`)
    return response.data
  },

  updateByKey: async (
    contentKey: string,
    payload: { title: string; bodyHtml: string }
  ): Promise<SystemContentDto> => {
    const response = await axios.put(
      `${API_BASE_URL}/system-content/${encodeURIComponent(contentKey)}`,
      payload
    )
    return response.data
  }
}
