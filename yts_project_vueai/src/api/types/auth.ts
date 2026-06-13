// 登录请求参数
export interface LoginParams {
    username: string;
    password: string;
  }

  // 注册请求参数
  export interface RegisterParams {
    username: string;
    password: string;
    email: string;
  }

  // 用户信息接口
  export interface UserInfo {
    id?: number;
    username: string;
    email: string;
    role?: string;
    is_active?: boolean;
    created_at?: string;
    // 其他用户信息字段
  }

  // API响应格式
  export interface ApiResponse<T> {
    data: T;
    message?: string;
    code?: number;
  }