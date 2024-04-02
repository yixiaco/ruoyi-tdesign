import type { RouteLocationNormalizedLoaded } from 'vue-router';

declare module 'vue-router' {
  interface RouteMeta {
    // 标题名称
    title?: string;
    // 激活的菜单。例如字典数据激活的菜单是字典类型菜单，/system/dict
    activeMenu?: string;
    // tdesign-icons图标名称。
    icon?: string;
    // 外链地址
    link?: string;
    // 是否在菜单栏中隐藏
    hidden?: boolean;
    // 固定
    affix?: boolean;
    // 是否缓存组件。优先级比noCache高
    keepAlive?: boolean;
    // 是否不缓存组件。优先级比keepAlive低
    noCache?: boolean;
    // 菜单权限
    permissions?: string[];
    // 组件key
    key?: string | ((route: RouteLocationNormalizedLoaded) => string);
    // 隐藏路由面包屑路径
    hiddenBreadcrumb?: boolean;
    // 面包屑重定向地址
    breadcrumbRedirect?: string;
    // 组件名称
    componentName?: string;
  }
}
