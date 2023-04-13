<template>
  <div>
    <template v-for="(item, index) in options">
      <template v-if="values.includes(item.value)">
        <span
          v-if="item.elTagType === 'default' || item.elTagType === ''"
          :key="item.value"
          :index="index"
          :class="item.elTagClass"
          >{{ item.label }}</span
        >
        <t-tag
          v-else
          :key="item.value + ''"
          :index="index"
          :theme="item.elTagType || 'default'"
          variant="light"
          :class="item.elTagClass"
          >{{ item.label }}</t-tag
        >
      </template>
    </template>
  </div>
</template>

<script lang="ts" setup>
import { computed, PropType } from 'vue';

import { DictModel } from '@/utils/dict';

const props = defineProps({
  // 数据
  options: {
    type: Array as PropType<Array<DictModel>>,
    default: null,
  },
  // 当前的值
  value: [Number, String, Array],
});

const values = computed(() => {
  if (props.value !== null && typeof props.value !== 'undefined') {
    return Array.isArray(props.value) ? props.value : [String(props.value)];
  }
  return [];
});
</script>

<style scoped>
.t-tag + .t-tag {
  margin-left: 10px;
}
</style>
