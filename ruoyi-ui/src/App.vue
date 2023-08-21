<template>
  <router-view :class="[mode]" />
</template>
<script lang="ts" setup>
import { computed } from 'vue';

import { useSettingStore } from '@/store';
import { useTitle } from '@/utils/doc';

const store = useSettingStore();

const mode = computed(() => {
  return store.displayMode;
});

useTitle();
</script>
<style lang="less" scoped>
#nprogress .bar {
  background: var(--td-brand-color) !important;
}
</style>
