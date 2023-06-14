<template>
  <div>
    <template v-for="item in list" :key="item.path">
      <template v-if="!item.children || !item.children.length || item.meta?.single">
        <t-menu-item v-if="getHref(item)" :name="item.path" :value="item.meta.value ?? item.path" @click="openHref(getHref(item)[0])">
          <template #icon>
            <component :is="menuIcon(item)" class="t-icon"></component>
          </template>
          {{ item.title }}
        </t-menu-item>
        <t-menu-item v-else :name="item.path" :value="item.meta.value ?? item.path" :to="item">
          <template #icon>
            <component :is="menuIcon(item)" class="t-icon"></component>
          </template>
          {{ item.title }}
        </t-menu-item>
      </template>
      <t-submenu v-else :name="item.path" :value="item.meta.value ?? item.path" :title="item.meta?.title">
        <template #icon>
          <component :is="menuIcon(item)" class="t-icon"></component>
        </template>
        <menu-content v-if="item.children" :nav-data="item.children" />
      </t-submenu>
    </template>
  </div>
</template>
<script setup lang="tsx">
import { computed } from 'vue';
import type { PropType } from 'vue';
import type { MenuRoute } from '@/types/interface';
import { getActive } from '@/router';
import { ComplexRoute } from "@/types/interface";

type ListItemType = MenuRoute & { icon?: string };

const props = defineProps({
  navData: {
    type: Array as PropType<ComplexRoute[]>,
    default: () => [],
  },
});
const active = computed(() => getActive());

const list = computed(() => {
  const { navData } = props;
  return getMenuList(navData);
});

const menuIcon = (item: ListItemType) => {
  if (typeof item.icon === 'string') return <t-icon name={item.icon} />;
  const RenderIcon = item.icon;
  return RenderIcon;
};

const getMenuList = (list: ComplexRoute[], basePath?: string): ListItemType[] => {
  if (!list || list.length === 0) {
    return [];
  }
  // 如果meta中有orderNo则按照从小到大排序
  list.sort((a, b) => {
    return (a.meta?.orderNo || 0) - (b.meta?.orderNo || 0);
  });
  return list
    .map<ListItemType>((item: MenuRoute) => {
      const path = basePath && !item.path.includes(basePath) ? `${basePath}/${item.path}` : item.path;

      return {
        path,
        title: item.meta?.title,
        icon: item.meta?.icon,
        children: getMenuList(item.children, path),
        meta: item.meta,
        redirect: item.redirect,
        query: item.query,
      };
    })
    .filter((item) => item.meta && item.meta.hidden !== true);
};

const getHref = (item: MenuRoute) => {
  const { path } = item;
  const { frameSrc, frameBlank } = item.meta;
  if (frameSrc && frameBlank) {
    return frameSrc.match(/(http|https):\/\/([\w.]+\/?)\S*/);
  }
  return path.match(/(http|https):\/\/([\w.]+\/?)\S*/);
};

const getPath = (item: ListItemType) => {
  const activeLevel = active.value.split('/').length;
  const pathLevel = item.path.split('/').length;
  if (activeLevel > pathLevel && active.value.startsWith(item.path)) {
    return active.value;
  }

  if (active.value === item.path) {
    return active.value;
  }

  return item.meta?.single ? item.redirect : item.path;
};

const openHref = (url: string) => {
  window.open(url);
};
</script>
