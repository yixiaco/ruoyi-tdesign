<template>
  <t-loading :loading="loading" size="small">
    <my-scrollbar class="dialog-content-max-height" :scrollbar="scrollbar">
      <t-descriptions
        :bordered="bordered"
        :colon="colon"
        :column="column"
        :item-layout="itemLayout"
        :items="items"
        :layout="layout"
        :size="size"
        :title="title"
        :label-style="effectiveLabelStyle"
        :content-style="contentStyle"
      >
        <slot />
      </t-descriptions>
    </my-scrollbar>
  </t-loading>
</template>
<script setup lang="ts">
import type { TdDescriptionItemProps } from 'tdesign-vue-next';
import type { PropType } from 'vue';
import { computed } from 'vue';

defineOptions({
  name: 'MyDescriptions',
});

const props = defineProps({
  // 字段名右侧是否携带冒号“：”
  colon: {
    type: Boolean,
    default: false,
  },
  // 是否显示加载中状态
  loading: {
    type: Boolean,
    default: false,
  },
  // 描述列表的列数
  column: {
    type: Number,
    default: 2,
  },
  // 是否显示边框
  bordered: {
    type: Boolean,
    default: true,
  },
  // 描述列表的布局方式
  itemLayout: {
    type: String as PropType<'horizontal' | 'vertical'>,
    default: 'horizontal',
  },
  // 描述项的列表。
  items: {
    type: Array as PropType<Array<TdDescriptionItemProps>>,
    default: () => null,
  },
  // 排列方向
  layout: {
    type: String as PropType<'horizontal' | 'vertical'>,
    default: 'horizontal',
  },
  // 描述列表的尺寸
  size: {
    type: String as PropType<'small' | 'medium' | 'large'>,
    default: 'medium',
  },
  // 描述列表的标题
  title: {
    type: String,
    default: '',
  },
  // 标签是否自动换行
  labelBreakLine: {
    type: Boolean,
    default: false,
  },
  // 标签对齐方式
  labelAlign: {
    type: String as PropType<'left' | 'right' | 'center'>,
    default: 'left',
  },
  // 自定义描述项标签的样式。
  labelStyle: {
    type: Object,
  },
  // 自定义描述项内容的样式。
  contentStyle: {
    type: Object,
  },
  // 是否显示滚动条
  scrollbar: {
    type: Boolean,
    default: true,
  },
});

const effectiveLabelStyle = computed(() => {
  const labelStyle: Record<string, string> = {};
  if (!props.labelBreakLine) {
    labelStyle['white-space'] = 'nowrap';
  }
  labelStyle['text-align'] = props.labelAlign;
  return { ...(props.labelStyle ?? {}), ...labelStyle };
});
</script>
