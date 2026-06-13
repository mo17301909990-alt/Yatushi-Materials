import { Directive } from 'vue';

// 临时禁用权限指令 - 所有元素都显示
export const vPermission: Directive = {
  mounted(el, binding) {
    // 临时禁用权限检查，所有元素都显示
    el.style.display = '';
  },
  
  updated(el, binding) {
    // 临时禁用权限检查，所有元素都显示
    el.style.display = '';
  }
};

// 临时禁用角色指令 - 所有元素都显示
export const vRole: Directive = {
  mounted(el, binding) {
    // 临时禁用角色检查，所有元素都显示
    el.style.display = '';
  },
  
  updated(el, binding) {
    // 临时禁用角色检查，所有元素都显示
    el.style.display = '';
  }
};

// 临时禁用管理员指令 - 所有元素都显示
export const vAdmin: Directive = {
  mounted(el, binding) {
    // 临时禁用管理员检查，所有元素都显示
    el.style.display = '';
  },
  
  updated(el, binding) {
    // 临时禁用管理员检查，所有元素都显示
    el.style.display = '';
  }
};
