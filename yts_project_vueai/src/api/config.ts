import axios, { AxiosHeaders } from 'axios';

const request = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || 'http://localhost:8092',
  timeout: 30000, // 增加到30秒，避免权限初始化等请求超时
  withCredentials: true,
  headers: {
    'Content-Type': 'application/json',
    'Accept': 'application/json'
  }
});

// 请求拦截器
request.interceptors.request.use(
  (config) => {
    // 不再手动设置 Origin 头，让浏览器自动处理
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// 响应拦截器
request.interceptors.response.use(
  (response) => {
    return response.data;
  },
  (error) => {
    if (error.response) {
      switch (error.response.status) {
        case 403:
          console.error('权限不足或跨域错误:', error);
          break;
        case 401:
          console.error('未授权，请重新登录');
          break;
        default:
          console.error(`请求错误 ${error.response.status}:`, error.message);
      }
    } else {
      console.error('网络错误:', error.message);
    }
    return Promise.reject(error);
  }
);

export default request;