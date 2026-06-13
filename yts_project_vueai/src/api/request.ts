import axios from 'axios';

const request = axios.create({
  baseURL: '/api',
  timeout: 30000, // 增加到30秒，避免权限初始化等请求超时
  withCredentials: true,
  headers: {
    'Content-Type': 'application/json',
    'Accept': 'application/json'
  }
});

// 添加请求拦截器
request.interceptors.request.use(
  config => {
    // 确保请求头包含正确的 Content-Type
    if (config.method !== 'get') {
      config.headers['Content-Type'] = 'application/json';
    }

    // 打印请求详情，包括 URL
    console.log(`Request: ${config.method?.toUpperCase()} ${config.baseURL}${config.url}`, config);

    return config;
  },
  error => {
    console.error('Request error:', error);
    return Promise.reject(error);
  }
);

// 添加响应拦截器
request.interceptors.response.use(
  response => {
    console.log('响应成功:', response.config.url, response);

    // 如果是登录请求，特殊处理
    if (response.config.url?.includes('/users/login')) {
      console.log('登录请求响应数据:', response.data);
      // 后端应该总是返回用户信息，不需要创建默认值
    }

    return response.data;
  },
  error => {
    if (error.response) {
      console.error('Response error:', error.response);
      // 处理特定状态码
      if (error.response.status === 403) {
        console.error('权限不足或跨域错误:', error.response);
        return Promise.reject({
          ...error.response.data,
          message: '权限不足或跨域错误，请检查身份验证或联系管理员'
        });
      }
      return Promise.reject(error.response.data || { message: `服务器错误: ${error.response.status}` });
    } else if (error.request) {
      console.error('Request error (no response):', error.request);
      return Promise.reject({ message: '服务器未响应，请检查网络连接或服务器状态' });
    } else {
      console.error('Network error:', error);
      return Promise.reject({ message: `网络错误: ${error.message || '未知错误'}` });
    }
  }
);

export default request;