import request from '../request';
import type { LoginParams, RegisterParams, UserInfo, ApiResponse } from '../types/auth';

export const authApi = {
  login(params: LoginParams) {
    return request({
      url: '/users/login',
      method: 'post',
      data: params
    });
  },

  register(params: RegisterParams) {
    return request({
      url: '/users/register',
      method: 'post',
      data: params
    });
  }
};