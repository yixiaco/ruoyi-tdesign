<template>
  <l-side-nav
    v-if="settingStore.showSidebar"
    :show-logo="settingStore.showSidebarLogo"
    :layout="settingStore.layout"
    :is-fixed="settingStore.isSidebarFixed"
    :menu="sideMenu"
    theme="dark"
    :is-compact="settingStore.isSidebarCompact"
  />
</template>

<script setup lang="ts">
import { storeToRefs } from 'pinia';
import { computed } from 'vue';
import { useRoute } from 'vue-router';

import { usePermissionStore, useSettingStore } from '@/store';

import LSideNav from './SideNav.vue';

const route = useRoute();
const permissionStore = usePermissionStore();
const settingStore = useSettingStore();
const { sidebarRouters: menuRouters } = storeToRefs(permissionStore);

const sideMenu = computed(() => {
  const { layout, splitMenu } = settingStore;
  let newMenuRouters = menuRouters.value;
  if (layout === 'mix' && splitMenu) {
    newMenuRouters.forEach((menu) => {
      if (route.path.indexOf(menu.path) === 0) {
        if (menu.children) {
          newMenuRouters = menu.children.map((subMenu) => ({ ...subMenu, path: `${menu.path}/${subMenu.path}` }));
        } else {
          newMenuRouters = [];
        }
      }
    });
  }
  return newMenuRouters;
});
</script>
