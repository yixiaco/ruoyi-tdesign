<template>
  <div ref="boxRef" v-bind="$attrs" @mousedown.left="handleMousedown($event)">
    <slot />
    <Teleport to="body">
      <div
        v-if="effectiveActive"
        class="rect-select"
        :class="{ 'rect-select-animate': borderAnimate }"
        :style="{
          width: `${rect.width}px`,
          height: `${rect.height}px`,
          top: `${rect.top}px`,
          left: `${rect.left}px`,
        }"
      />
    </Teleport>
  </div>
</template>
<script lang="ts" setup>
defineOptions({
  name: 'RectSelect',
});
import { computed, onMounted, onUnmounted, ref } from 'vue';

const props = defineProps({
  borderAnimate: {
    type: Boolean,
    default: false,
  },
  disabled: {
    type: Boolean,
    default: false,
  },
});
const rect = computed(() => {
  return {
    width: Math.abs(current.value[0] - start.value[0]),
    height: Math.abs(current.value[1] - start.value[1]),
    top: Math.min(start.value[1], current.value[1]),
    left: Math.min(start.value[0], current.value[0]),
  };
});
const domRects = ref([]);
const active = ref(false);
const start = ref([0, 0]);
const current = ref([0, 0]);
const boxRef = ref<HTMLElement>(null);
// rect: left top right height
const boxRect = ref([0, 0, Number.MAX_VALUE, Number.MAX_VALUE]);
const emit = defineEmits<{
  (e: 'change', checkedIndexes: number[]): void;
}>();
const effectiveActive = computed(() => {
  return active.value && !props.disabled;
});

/**
 * 鼠标按下事件
 * @param event
 */
function handleMousedown(event: MouseEvent) {
  if (props.disabled) {
    return;
  }
  initBoxRect();
  // 启用区域选择时，获取一次dom元素的区域
  initChildrenRects();
  active.value = true;
  start.value[0] = event.clientX;
  start.value[1] = event.clientY;
  handleMouseMove(event);
}

/**
 * 鼠标移动事件
 * @param event
 */
function handleMouseMove(event: MouseEvent) {
  if (effectiveActive.value) {
    current.value[0] = range(event.clientX, boxRect.value[0], boxRect.value[2]);
    current.value[1] = range(event.clientY, boxRect.value[1], boxRect.value[3]);
    triggerChange();
  }
}

/** 限制一个数在范围内 */
function range(num: number, min: number, max: number) {
  if (num < min) {
    return min;
  }
  if (num > max) {
    return max;
  }
  return num;
}

/**
 * 鼠标松开事件
 * @param event
 */
function handleMouseUp(event: MouseEvent) {
  active.value = false;
}

/**
 * 触发变更事件
 */
function triggerChange() {
  const rec1 = [
    rect.value.left,
    rect.value.top,
    rect.value.left + rect.value.width,
    rect.value.top + rect.value.height,
  ];
  const checked: number[] = [];
  domRects.value.forEach((rec2, index) => {
    if (isRectangleOverlap(rec1, rec2)) {
      checked.push(index);
    }
  });
  emit('change', checked);
}

/** 初始化子元素的区域 */
function initChildrenRects() {
  const rects = [];
  const children: HTMLCollection = boxRef.value!.children;
  for (let i = 0; i < children.length; i++) {
    const element = children.item(i);
    const domRect = element.getBoundingClientRect();
    const rec = [domRect.left, domRect.top, domRect.right, domRect.bottom];
    rects.push(rec);
  }
  domRects.value = rects;
}

/** 初始化可选区的区域 */
function initBoxRect() {
  const clientRect = boxRef.value.getBoundingClientRect();
  boxRect.value[0] = clientRect.left;
  boxRect.value[1] = clientRect.top;
  boxRect.value[2] = clientRect.right;
  boxRect.value[3] = clientRect.bottom;
}

/**
 * 判断两个矩形是否重叠
 * 参数类型[x1,y1,x2,y2]，表示左上角与右下角坐标
 * @param rec1
 * @param rec2
 */
function isRectangleOverlap(rec1: number[], rec2: number[]) {
  // 判断矩形区域长度不小于0
  if (rec1[0] === rec1[2] || rec1[1] === rec1[3] || rec2[0] === rec2[2] || rec2[1] === rec2[3]) {
    return false;
  }
  // 判断矩形1是否在矩形2的四周，如果不在四周则存在重叠区域
  return !(
    rec1[2] < rec2[0] || // left
    rec1[3] < rec2[1] || // bottom
    rec1[0] > rec2[2] || // right
    rec1[1] > rec2[3]
  ); // top
}

onMounted(() => {
  window.addEventListener('mouseup', handleMouseUp);
  window.addEventListener('mousemove', handleMouseMove);
});
onUnmounted(() => {
  window.removeEventListener('mouseup', handleMouseUp);
  window.removeEventListener('mousemove', handleMouseMove);
});
</script>

<style scoped lang="less">
.rect-select {
  z-index: 999999; //根据情况自己拿捏
  position: fixed;
  box-sizing: border-box;
  border: 1px solid var(--td-brand-color);
  background-color: var(--td-brand-color-hover);
  opacity: 0.3;

  /*边框虚线滚动动画特效*/
  &-animate {
    border: none;
    background:
      linear-gradient(90deg, var(--td-brand-color) 60%, transparent 60%) repeat-x left top/10px 1px,
      linear-gradient(0deg, var(--td-brand-color) 60%, transparent 60%) repeat-y right top/1px 10px,
      linear-gradient(90deg, var(--td-brand-color) 60%, transparent 60%) repeat-x right bottom/10px 1px,
      linear-gradient(0deg, var(--td-brand-color) 60%, transparent 60%) repeat-y left bottom/1px 10px,
      var(--td-brand-color-hover);

    animation: border-animate 0.382s infinite linear;
  }

  @keyframes border-animate {
    0% {
      background-position:
        left top,
        right top,
        right bottom,
        left bottom;
    }

    100% {
      background-position:
        left 10px top,
        right top 10px,
        right 10px bottom,
        left bottom 10px;
    }
  }
}
</style>
