<template>
  <div>
    <template v-for="(item, index) in rowOptions">
      <span
        v-if="!theme && !variant && (item.tagType === 'default' || !item.tagType)"
        :key="item.value"
        :class="item.tagClass"
      >
        {{ item.label }}
      </span>
      <t-tag
        v-else
        :key="item.value + ''"
        :index="index"
        :theme="theme || item.tagType || 'default'"
        :variant="variant || 'light'"
        :class="item.tagClass"
      >
        {{ item.label }}
      </t-tag>
      <template v-if="($slots.separator || separator) && rowOptions.length - 1 !== index">
        <template v-if="$slots.separator"><slot name="separator" /></template>
        <template v-else>{{ separator }}</template>
      </template>
    </template>
  </div>
</template>

<script lang="ts" setup>
import { isNumber } from 'lodash';
import type { PropType } from 'vue';
import { computed } from 'vue';

import type { DictModel } from '@/utils/dict';

const props = defineProps({
  // 数据
  options: {
    type: Array as PropType<Array<DictModel>>,
    default: null,
  },
  // 当前的值
  value: [Number, String, Array] as PropType<number | string | Array<string | number>>,
  // 多个值时，使用的分隔符。支持使用slot方式
  separator: [String],
  // 覆盖字典默认组件风格
  theme: {
    type: String as PropType<'default' | 'warning' | 'danger' | 'success' | 'primary'>,
  },
  // 标签风格变体, 默认值light
  variant: {
    type: String as PropType<'outline' | 'dark' | 'light' | 'light-outline'>,
  },
});

const values = computed<Array<number | string>>(() => {
  if (props.value !== null && typeof props.value !== 'undefined') {
    if (Array.isArray(props.value)) {
      return props.value;
    }
    if (isNumber(props.value)) {
      return [props.value];
    }
    return String(props.value).split(',');
  }
  return [];
});
// 有效选项
const rowOptions = computed<Array<DictModel>>(() => {
  if (!props.options) {
    return [];
  }
  return props.options.filter((option) => {
    return values.value.includes(option.value);
  });
});
</script>

<style scoped>
.t-tag + .t-tag {
  margin-left: 10px;
}
</style>
