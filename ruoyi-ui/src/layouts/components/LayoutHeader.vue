<template>
  <l-header
    v-if="settingStore.showHeader"
    :show-logo="settingStore.showHeaderLogo"
    :theme="settingStore.displayMode"
    :layout="settingStore.layout"
    :is-fixed="settingStore.isHeaderFixed"
    :menu="headerMenu"
    :is-compact="settingStore.isSidebarCompact"
  />
</template>

<script setup lang="ts">
import { storeToRefs } from 'pinia';
import { computed } from 'vue';

import { usePermissionStore, useSettingStore } from '@/store';
import { ComplexRoute } from '@/types/interface';

import LHeader from './Header.vue';

const permissionStore = usePermissionStore();
const settingStore = useSettingStore();
const { topbarRouters: menuRouters } = storeToRefs(permissionStore);
const headerMenu = computed<ComplexRoute[]>(() => {
  if (settingStore.layout === 'mix') {
    if (settingStore.splitMenu) {
      return menuRouters.value.map((menu) => {
        const newMenu: ComplexRoute = {
          query: undefined,
          ...menu,
          children: [],
        };
        // 这里设置新的path与父value，如果是多级路由，则默认打开第一项
        if (menu.children && menu.children.length > 0 && menu.redirect?.toString().includes('noRedirect')) {
          newMenu.path = `${menu.path}/${menu.children[0].path}`;
          newMenu.meta.value = menu.path;
          newMenu.query = (menu.children[0] as any).query;
        }
        return newMenu;
      });
    }
    return [];
  }
  return menuRouters.value;
});
</script>
