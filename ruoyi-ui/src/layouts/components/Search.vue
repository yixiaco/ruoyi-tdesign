<template>
  <div v-if="layout === 'side'" class="header-menu-search">
    <t-auto-complete
      v-model="searchData"
      :class="['header-search', { 'hover-active': isSearchFocus }]"
      placeholder="请输入搜索内容"
      :options="options"
      @blur="changeSearchFocus(false)"
      @focus="changeSearchFocus(true)"
    >
      <template #prefix-icon>
        <search-icon class="icon" size="16" />
      </template>
      <template #option="{ option }">
        <t-highlight-option :content="option.text" :keyword="searchData" @click="() => $router.push(option.value)" />
      </template>
    </t-auto-complete>
  </div>

  <div v-else class="header-menu-search-left">
    <t-button
      :class="{ 'search-icon-hide': isSearchFocus }"
      theme="default"
      shape="square"
      variant="text"
      @click="changeSearchFocus(true)"
    >
      <search-icon />
    </t-button>
    <t-auto-complete
      v-model="searchData"
      :class="['header-search', { 'width-zero': !isSearchFocus }]"
      placeholder="输入要搜索内容"
      :autofocus="isSearchFocus"
      :options="options"
      @blur="changeSearchFocus(false)"
    >
      <template #prefix-icon>
        <search-icon size="16" />
      </template>
      <template #option="{ option }">
        <t-highlight-option :content="option.text" :keyword="searchData" @click="() => $router.push(option.value)" />
      </template>
    </t-auto-complete>
  </div>
</template>

<script setup lang="ts">
import { storeToRefs } from 'pinia';
import { SearchIcon } from 'tdesign-icons-vue-next';
import { HighlightOption as THighlightOption } from 'tdesign-vue-next';
import { computed, ref } from 'vue';

import { usePermissionStore } from '@/store';
import { MenuRoute } from '@/types/interface';

defineProps({
  layout: String,
});

const permissionStore = usePermissionStore();
const { defaultRoutes: menuRouters } = storeToRefs(permissionStore);

const isSearchFocus = ref(false);
const searchData = ref('');
const changeSearchFocus = (value: boolean) => {
  if (!value && searchData.value) {
    setTimeout(() => {
      searchData.value = '';
    }, 100);
  }
  isSearchFocus.value = value;
};
const menus = computed(() => {
  const newMenuRouters = menuRouters.value as Array<MenuRoute>;
  return getLeftMenus(newMenuRouters)
    .filter((value) => value?.title && !value?.meta?.hidden)
    .map((value) => ({ text: value.title, value: value.path }));
});
const options = computed(() => {
  if (!searchData.value) {
    return [];
  }
  return menus.value;
});

/** 获取叶子节点的菜单结构 */
function getLeftMenus(menus: Array<MenuRoute>, parent?: MenuRoute): MenuRoute[] {
  return menus.flatMap((value) => {
    // 浅克隆对象
    value = { ...value };
    function getTitle(title: string) {
      if (title && parent?.title) {
        return `${parent.title} / ${title}`;
      }
      return title;
    }
    if (value?.meta?.title) {
      value.title = getTitle(value.meta.title);
    }
    value.path = parent?.path && !value.path.startsWith('http') ? `${parent?.path}/${value.path}` : value.path;
    if (value.children && value.children.length > 0) {
      return getLeftMenus(value.children, value);
    }
    return value;
  });
}
</script>
<style lang="less" scoped>
.header-menu-search {
  display: flex;
  margin-left: 16px;
  .hover-active {
    background: var(--td-bg-color-secondarycontainer);
  }

  .t-icon {
    color: var(--td-text-color-primary) !important;
  }
  .header-search {
    :deep(.t-input) {
      border: none;
      outline: none;
      box-shadow: none;
      transition: background @anim-duration-base linear;
      .t-input__inner {
        transition: background @anim-duration-base linear;
      }
      .t-input__inner {
        background: none;
      }
      &:hover {
        background: var(--td-bg-color-secondarycontainer);
        .t-input__inner {
          background: var(--td-bg-color-secondarycontainer);
        }
      }
    }
  }
}

.t-button {
  margin: 0 8px;
  transition: opacity @anim-duration-base @anim-time-fn-easing;

  .t-icon {
    font-size: 20px;
    &.general {
      margin-right: 16px;
    }
  }
}
.search-icon-hide {
  opacity: 0;
}
.header-menu-search-left {
  display: flex;
  align-items: center;

  .header-search {
    width: 200px;
    transition: width @anim-duration-base @anim-time-fn-easing;
    :deep(.t-input) {
      border: 0;
      &:focus {
        box-shadow: none;
      }
    }
    &.width-zero {
      width: 0;
      opacity: 0;
    }
  }
}
</style>
