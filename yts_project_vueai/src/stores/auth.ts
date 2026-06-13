import { defineStore } from 'pinia';
import { authApi } from '@/api/modules/auth';
import type { LoginParams, RegisterParams, UserInfo } from '@/api/types/auth';

// 登录状态过期时间（单位：毫秒）
// 6小时 = 6 * 60 * 60 * 1000 = 21600000毫秒
const LOGIN_EXPIRATION_TIME = 6 * 60 * 60 * 1000;

// 当距离过期还有多少时间时自动延长登录状态（单位：毫秒）
// 30分钟 = 30 * 60 * 1000 = 1800000毫秒
const AUTO_REFRESH_THRESHOLD = 30 * 60 * 1000;

export const useAuthStore = defineStore('auth', {
  state: () => ({
    userInfo: null as UserInfo | null,
    isLoggedIn: false,
  }),

  actions: {
    async login(params: LoginParams) {
      try {
        console.log('开始登录请求:', params.username);
        const response = await authApi.login(params);
        console.log('登录请求响应:', response);

        // 检查响应是否有效
        if (!response) {
          throw new Error('服务器响应为空');
        }

        // 检查响应数据是否有效（后端应该总是返回用户信息）
        if (!response || !response.id) {
          console.error('登录响应数据无效:', response);
          throw new Error('登录失败：服务器返回的用户信息无效');
        }

        // 使用后端返回的真实用户数据
        const userData = response;

        console.log('处理的用户数据:', userData);

        this.userInfo = userData;
        this.isLoggedIn = true;

        // 计算登录过期时间
        const loginTime = Date.now();
        const expirationTime = loginTime + LOGIN_EXPIRATION_TIME;

        // 保存用户信息、登录状态和过期时间
        const userInfoStr = JSON.stringify(this.userInfo);
        localStorage.setItem('userInfo', userInfoStr);
        localStorage.setItem('isLoggedIn', 'true');
        localStorage.setItem('loginTime', loginTime.toString());
        localStorage.setItem('expirationTime', expirationTime.toString());

        console.log('已保存到localStorage:', {
          userInfo: userInfoStr,
          isLoggedIn: 'true',
          loginTime,
          expirationTime
        });

        console.log(`登录成功，过期时间：${new Date(expirationTime).toLocaleString()}`);
        console.log('当前登录状态:', this.isLoggedIn);

        return userData;
      } catch (error: any) {
        console.error('登录失败:', error);
        this.clearLoginState(); // 确保在登录失败时清除状态
        throw error.response?.data?.message || error.message || '登录失败';
      }
    },

    async register(params: RegisterParams) {
      try {
        const response = await authApi.register(params);
        return response.data;
      } catch (error: any) {
        console.error('Auth store register error:', error);
        if (error.response?.data?.message) {
          throw new Error(error.response.data.message);
        }
        throw new Error('注册失败，请重试');
      }
    },

    logout() {
      this.userInfo = null;
      this.isLoggedIn = false;

      // 清除所有登录相关的本地存储
      localStorage.removeItem('userInfo');
      localStorage.removeItem('isLoggedIn');
      localStorage.removeItem('loginTime');
      localStorage.removeItem('expirationTime');

      console.log('用户已注销');
    },

    initUserState() {
      console.log('initUserState 被调用');
      try {
        const userInfo = localStorage.getItem('userInfo');
        const isLoggedIn = localStorage.getItem('isLoggedIn');
        const expirationTimeStr = localStorage.getItem('expirationTime');

        console.log('localStorage 状态:', {
          userInfo: userInfo ? '存在' : '不存在',
          isLoggedIn,
          expirationTimeStr
        });

        // 检查登录状态是否有效
        if (userInfo && userInfo !== 'undefined' && isLoggedIn === 'true') {
          // 检查登录是否过期
          if (expirationTimeStr) {
            const expirationTime = parseInt(expirationTimeStr);
            const currentTime = Date.now();

            console.log('当前时间:', new Date(currentTime).toLocaleString());
            console.log('过期时间:', new Date(expirationTime).toLocaleString());

            if (currentTime < expirationTime) {
              // 登录未过期，恢复登录状态
              try {
                this.userInfo = JSON.parse(userInfo);
                this.isLoggedIn = true;

                const remainingTime = Math.round((expirationTime - currentTime) / 1000 / 60);
                console.log(`登录状态有效，还剩 ${remainingTime} 分钟过期`);
                console.log('当前登录状态:', this.isLoggedIn);
                console.log('用户信息:', this.userInfo);

                // 如果距离过期时间小于阈值，自动延长登录状态
                if (expirationTime - currentTime < AUTO_REFRESH_THRESHOLD) {
                  this.refreshLoginState();
                }

                return;
              } catch (parseError) {
                console.error('解析用户信息失败:', parseError);
                // 如果解析失败，清除登录状态
                this.clearLoginState();
                return;
              }
            } else {
              // 登录已过期，清除登录状态
              console.log('登录已过期，需要重新登录');
            }
          } else {
            console.log('没有过期时间信息');
          }
        } else {
          console.log('用户信息或登录状态无效');
        }

        // 如果数据无效或登录已过期，清除localStorage
        this.clearLoginState();
        console.log('清除登录状态后:', { isLoggedIn: this.isLoggedIn, userInfo: this.userInfo });
      } catch (error) {
        console.error('Error initializing user state:', error);
        // 发生错误时，清除localStorage并重置状态
        this.clearLoginState();
      }
    },

    // 刷新登录状态，延长过期时间
    refreshLoginState() {
      if (this.isLoggedIn && this.userInfo) {
        // 计算新的过期时间
        const loginTime = Date.now();
        const expirationTime = loginTime + LOGIN_EXPIRATION_TIME;

        // 更新本地存储
        localStorage.setItem('loginTime', loginTime.toString());
        localStorage.setItem('expirationTime', expirationTime.toString());

        console.log(`登录状态已刷新，新的过期时间：${new Date(expirationTime).toLocaleString()}`);
      }
    },

    // 清除登录状态
    clearLoginState() {
      localStorage.removeItem('userInfo');
      localStorage.removeItem('isLoggedIn');
      localStorage.removeItem('loginTime');
      localStorage.removeItem('expirationTime');
      this.userInfo = null;
      this.isLoggedIn = false;
    }
  }
});