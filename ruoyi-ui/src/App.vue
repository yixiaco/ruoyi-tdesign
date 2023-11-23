<template>
  <t-config-provider :global-config="getComponentsLocale">
    <router-view :key="locale" :class="[mode]" />
  </t-config-provider>
</template>
<script lang="ts" setup>
import { computed } from 'vue';

import { useLocale } from '@/locales/useLocale';
import { useSettingStore } from '@/store';
import { useTitle } from '@/utils/doc';

const store = useSettingStore();

const mode = computed(() => {
  return store.displayMode;
});

const { getComponentsLocale, locale } = useLocale();
useTitle();
</script>
<style lang="less" scoped>
#nprogress .bar {
  background: var(--td-brand-color) !important;
}
</style>
