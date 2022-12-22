import { defineStore } from 'pinia';
import auth from '@/plugins/auth';
import router, { defaultRouterList, dynamicRoutes } from '@/router';
import { store } from '@/store';
import { getRouters } from '@/api/menu';
import Layout from '@/layouts/index.vue';
import ParentView from '@/components/ParentView/index.vue';
import InnerLink from '@/layouts/components/InnerLink.vue';

const modules = import.meta.glob('./../../pages/**/*.vue');

export const usePermissionStore = defineStore('permission', {
  state: () => ({
    whiteListRouters: ['/login', '/auth-redirect', '/bind', '/register'],
    routers: [],
    addRoutes: [],
    defaultRoutes: [],
    topbarRouters: [],
    sidebarRouters: [],
  }),
  actions: {
    setRouters(routers) {
      this.addRoutes = routers;
      this.routers = defaultRouterList.concat(routers);
    },
    setDefaultRoutes(routes) {
      this.defaultRoutes = defaultRouterList.concat(routes);
    },
    setTopbarRoutes(routes) {
      this.topbarRouters = routes;
    },
    setSidebarRouters(routes) {
      this.sidebarRouters = routes;
    },
    async initRoutes() {
      // 向后端请求路由数据
      const res = await getRouters();
      const sdata = JSON.parse(JSON.stringify(res.data));
      const rdata = JSON.parse(JSON.stringify(res.data));
      const defaultData = JSON.parse(JSON.stringify(res.data));
      const sidebarRoutes = filterAsyncRouter(sdata);
      const rewriteRoutes = filterAsyncRouter(rdata, true);
      const defaultRoutes = filterAsyncRouter(defaultData);
      const asyncRoutes = filterDynamicRoutes(dynamicRoutes);
      asyncRoutes.forEach((route) => {
        router.addRoute(route);
      });
      this.setRouters(rewriteRoutes);
      this.setSidebarRouters(defaultRouterList.concat(sidebarRoutes));
      this.setDefaultRoutes(sidebarRoutes);
      this.setTopbarRoutes(defaultRoutes);
      return rewriteRoutes;
    },
  },
});

// 遍历后台传来的路由字符串，转换为组件对象
function filterAsyncRouter(asyncRouterMap, type = false) {
  return asyncRouterMap.filter((route) => {
    if (type && route.children) {
      route.children = filterChildren(route.children);
    }
    if (route.component) {
      // Layout ParentView 组件特殊处理
      if (route.component === 'Layout') {
        route.component = Layout;
      } else if (route.component === 'ParentView') {
        route.component = ParentView;
      } else if (route.component === 'InnerLink') {
        route.component = InnerLink;
      } else {
        route.component = loadView(route.component);
      }
    }
    if (route.children && route.children.length) {
      route.children = filterAsyncRouter(route.children, type);
    } else {
      delete route.children;
      delete route.redirect;
    }
    return true;
  });
}

function filterChildren(childrenMap, lastRouter: any = false) {
  let children = [];
  childrenMap.forEach((el) => {
    if (el.children && el.children.length) {
      if (el.component === 'ParentView' && !lastRouter) {
        el.children.forEach((c) => {
          c.path = `${el.path}/${c.path}`;
          if (c.children && c.children.length) {
            children = children.concat(filterChildren(c.children, c));
            return;
          }
          children.push(c);
        });
        return;
      }
    }
    if (lastRouter) {
      el.path = `${lastRouter.path}/${el.path}`;
    }
    children = children.concat(el);
  });
  return children;
}

// 动态路由遍历，验证是否具备权限
export function filterDynamicRoutes(routes) {
  const res = [];
  routes.forEach((route) => {
    if (route.meta.permissions) {
      if (auth.hasPermiOr(route.meta.permissions)) {
        res.push(route);
      }
    } else if (route.roles) {
      if (auth.hasRoleOr(route.roles)) {
        res.push(route);
      }
    }
  });
  return res;
}

export const loadView = (view) => {
  let res;
  for (const path in modules) {
    const dir = path.split('pages/')[1].split('.vue')[0];
    if (dir === view) {
      res = () => modules[path]();
    }
  }
  return res;
};

export function getPermissionStore() {
  return usePermissionStore(store);
}
