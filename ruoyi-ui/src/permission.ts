import NProgress from 'nprogress'; // progress bar
import 'nprogress/nprogress.css'; // progress bar style

import { getPermissionStore, getUserStore } from '@/store';
import router from '@/router';
import { isRelogin } from '@/utils/request';
import { isHttp } from '@/utils/validate';

NProgress.configure({ showSpinner: false });

router.beforeEach(async (to, from, next) => {
  NProgress.start();

  const userStore = getUserStore();
  const permissionStore = getPermissionStore();
  const { whiteListRouters } = permissionStore;

  const { token } = userStore;
  if (token) {
    if (to.path === '/login') {
      next();
      return;
    }

    const { roles } = userStore;

    if (roles && roles.length > 0) {
      next();
    } else {
      isRelogin.show = true;
      try {
        await userStore.getUserInfo();

        isRelogin.show = false;

        const accessRoutes = await permissionStore.initRoutes();
        // 根据roles权限生成可访问的路由表
        accessRoutes.forEach((route) => {
          if (!isHttp(route.path)) {
            router.addRoute(route); // 动态添加可访问路由表
          }
        });
        next({ ...to, replace: true }); // hack方法 确保addRoutes已完成
      } catch (error) {
        // await userStore.logout();
        next({
          path: '/login',
          query: { redirect: encodeURIComponent(to.fullPath) },
        });
        NProgress.done();
      }
    }
  } else {
    /* white list router */
    if (whiteListRouters.indexOf(to.path) !== -1) {
      next();
    } else {
      next({
        path: '/login',
        query: { redirect: encodeURIComponent(to.fullPath) },
      });
    }
    NProgress.done();
  }
});

router.afterEach((to) => {
  if (to.path === '/login') {
    const userStore = getUserStore();

    userStore.logout();
  }
  NProgress.done();
});
