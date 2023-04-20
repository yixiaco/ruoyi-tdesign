import uniq from 'lodash/uniq';
import { createRouter, createWebHistory, RouteRecordRaw, useRoute } from 'vue-router';

import Layout from '@/layouts/index.vue';

// 自动导入modules文件夹下所有ts文件
const modules = import.meta.globEager('./modules/**/*.ts');

// 路由暂存
const routeModuleList: Array<RouteRecordRaw> = [];

Object.keys(modules).forEach((key) => {
  // @ts-ignore
  const mod = modules[key].default || {};
  const modList = Array.isArray(mod) ? [...mod] : [mod];
  routeModuleList.push(...modList);
});

// 关于单层路由，meta 中设置 { single: true } 即可为单层路由，{ hidden: true } 即可在侧边栏隐藏该路由

// 存放动态路由
export const asyncRouterList: Array<RouteRecordRaw> = [...routeModuleList];

// 存放固定的路由
export const defaultRouterList: Array<RouteRecordRaw> = [
  {
    path: '/redirect',
    component: Layout,
    meta: {
      hidden: true,
    },
    children: [
      {
        path: '/redirect/:path(.*)',
        component: () => import('@/pages/redirect/index.vue'),
      },
    ],
  },
  {
    path: '/login',
    name: 'login',
    component: () => import('@/pages/login/index.vue'),
    meta: {
      hidden: true,
    },
  },
  {
    path: '/',
    redirect: '/index',
  },
  {
    path: '/:pathMatch(.*)*',
    component: () => import('@/pages/result/404/index.vue'),
    meta: {
      hidden: true,
    },
  },
  {
    path: '/401',
    component: () => import('@/pages/result/403/index.vue'),
    meta: {
      hidden: true,
    },
  },
  {
    path: '',
    component: Layout,
    redirect: '/index',
    children: [
      {
        path: '/index',
        component: () => import('@/pages/dashboard/base/index.vue'),
        name: 'Index',
        meta: { title: '首页', icon: 'dashboard', affix: true },
      },
    ],
  },
  {
    path: '/user',
    component: Layout,
    meta: {
      hidden: true,
    },
    children: [
      {
        path: 'profile',
        component: () => import('@/pages/system/user/profile/index.vue'),
        name: 'Profile',
        meta: { title: '个人中心', icon: 'user' },
      },
    ],
  },
];

// 动态路由，基于用户权限动态去加载
export const dynamicRoutes: Array<RouteRecordRaw> = [
  {
    path: '/system/user-auth',
    component: Layout,
    meta: {
      hidden: true,
      permissions: ['system:user:edit'],
    },
    children: [
      {
        path: 'role/:userId(\\d+)',
        component: () => import('@/pages/system/user/authRole.vue'),
        name: 'AuthRole',
        meta: { title: '分配角色', activeMenu: '/system/user' },
      },
    ],
  },
  {
    path: '/system/role-auth',
    component: Layout,
    meta: {
      hidden: true,
      permissions: ['system:role:edit'],
    },
    children: [
      {
        path: 'user/:roleId(\\d+)',
        component: () => import('@/pages/system/role/authUser.vue'),
        name: 'AuthUser',
        meta: { title: '分配用户', activeMenu: '/system/role' },
      },
    ],
  },
  {
    path: '/system/dict-data',
    component: Layout,
    meta: {
      hidden: true,
      permissions: ['system:dict:list'],
    },
    children: [
      {
        path: 'index/:dictId(\\d+)',
        component: () => import('@/pages/system/dict/data.vue'),
        name: 'Data',
        meta: { title: '字典数据', activeMenu: '/system/dict' },
      },
    ],
  },
  {
    path: '/system/oss-config',
    component: Layout,
    meta: {
      hidden: true,
      permissions: ['system:ossConfig:list'],
    },
    children: [
      {
        path: 'index',
        component: () => import('@/pages/system/oss/config.vue'),
        name: 'OssConfig',
        meta: { title: '配置管理', activeMenu: '/system/oss' },
      },
    ],
  },
  {
    path: '/tool/gen-edit',
    component: Layout,
    meta: {
      hidden: true,
      permissions: ['tool:gen:edit'],
    },
    children: [
      {
        path: 'index/:tableId(\\d+)',
        component: () => import('@/pages/tool/gen/editTable.vue'),
        name: 'GenEdit',
        meta: { title: '修改生成配置', activeMenu: '/tool/gen' },
      },
    ],
  },
];

export const allRoutes = [...defaultRouterList, ...asyncRouterList];

export const getRoutesExpanded = () => {
  const expandedRoutes = [];

  allRoutes.forEach((item) => {
    if (item.meta && item.meta.expanded) {
      expandedRoutes.push(item.path);
    }
    if (item.children && item.children.length > 0) {
      item.children
        .filter((child) => child.meta && child.meta.expanded)
        .forEach((child: RouteRecordRaw) => {
          expandedRoutes.push(item.path);
          expandedRoutes.push(`${item.path}/${child.path}`);
        });
    }
  });
  return uniq(expandedRoutes);
};

export const getActive = (maxLevel = 3): string => {
  const route = useRoute();
  if (!route.path) {
    return '';
  }
  return route.path
    .split('/')
    .filter((_item: string, index: number) => index <= maxLevel && index > 0)
    .map((item: string) => `/${item}`)
    .join('');
};

const router = createRouter({
  history: createWebHistory(import.meta.env.VITE_APP_CONTEXT_PATH),
  routes: defaultRouterList,
  scrollBehavior(to, from, savedPosition) {
    if (savedPosition) {
      return savedPosition;
    }
    return { top: 0 };
  },
  // scrollBehavior() {
  //   return {
  //     el: '#app',
  //     top: 0,
  //     behavior: 'smooth',
  //   };
  // },
});

export default router;
