<template>
  <div
    v-if="scrollbar"
    class="my-content-scrollbar"
    :style="{
      height: getUnit(height),
      minHeight: getUnit(minHeight),
      maxHeight: getUnit(maxHeight),
    }"
    v-bind="$attrs"
  >
    <slot />
  </div>
  <template v-else>
    <slot />
  </template>
</template>
<script setup lang="ts">
defineOptions({
  name: 'MyScrollbar',
});

defineProps({
  // 是否显示滚动条
  scrollbar: {
    type: Boolean,
    default: true,
  },
  // 设置高度，设置后最小高度与最大高度无效，内容必须具有高度限制，否则滚动条不会出现
  height: {
    type: [Number, String],
  },
  // 设置最大高度，内容必须具有高度限制，否则滚动条不会出现
  minHeight: {
    type: [Number, String],
  },
  // 设置最小高度，内容必须具有高度限制，否则滚动条不会出现
  maxHeight: {
    type: [Number, String],
  },
  // 设置滚动条滑块的厚度
  thumbThickness: {
    type: String,
    default: '6px',
  },
  // 设置滚动条滑块的颜色
  thumbColor: {
    type: String,
    default: 'var(--td-scrollbar-color)',
  },
  // 设置滚动条滑块的悬浮颜色
  thumbHoverColor: {
    type: String,
    default: 'var(--td-scrollbar-hover-color)',
  },
  // 设置滚动条轨道的厚度
  trackThickness: {
    type: String,
    default: '12px',
  },
  // 设置滚动条轨道的颜色
  trackColor: {
    type: String,
    default: 'var(--td-scroll-track-color)',
  },
  // 设置滚动条轨道的悬浮颜色
  trackHoverColor: {
    type: String,
    default: 'var(--td-scroll-track-color)',
  },
});

/**
 * 得到单位
 * @param unit 单位
 */
function getUnit(unit: number | string) {
  if (typeof unit === 'number') {
    return `${unit}px`;
  }
  return unit;
}
</script>
<style scoped lang="less">
@thumbThickness: v-bind(thumbThickness);
@trackThickness: v-bind(trackThickness);
.my-content-scrollbar {
  overflow: scroll;

  &::-webkit-scrollbar {
    width: @trackThickness; /* 滚动条的宽度 */
    height: @trackThickness; /* 横向滚动条的高度 */
    background-color: transparent;
  }

  /* 滚动条轨道 */
  &::-webkit-scrollbar-track {
    background-color: v-bind(trackColor); /* 轨道的背景色 */
    border-radius: @trackThickness; /* 轨道的圆角 */
  }

  &::-webkit-scrollbar-track:hover {
    background-color: v-bind(trackColor); /* 轨道的背景色 */
  }

  &::-webkit-scrollbar-corner {
    width: 0;
  }

  /* 滚动条滑块 */
  &::-webkit-scrollbar-thumb {
    border-radius: calc(@trackThickness / 2); /* 滑块的圆角 */
    border: calc((@trackThickness - @thumbThickness) / 2) solid transparent;
    background-clip: padding-box;
    background-color: v-bind(thumbColor); /* 滑块的背景色 */
  }

  /* 滑块在悬停时的样式 */
  &::-webkit-scrollbar-thumb:hover {
    background-color: v-bind(thumbHoverColor); /* 滑块悬停时的背景色 */
  }
}
</style>
