<template>
  <KeepAlive :max="100">
    <component :is="rowComponent" v-bind="$attrs" />
  </KeepAlive>
</template>
<script lang="ts" setup>
defineOptions({
  name: 'RIcon',
});
import * as Icons from 'tdesign-icons-vue-next/lib/icons';
import { computed } from 'vue';

const components = new Map();
Object.entries(Icons).forEach((value) => {
  // 将驼峰命名的组件名称转为横杠分割的组件名称，例如：Add12Icon => add-12-icon
  const componentName = value[0].replace(/([0-9]+|[A-Z])/g, (match, p1, offset) => {
    return offset === 0 ? p1.toLowerCase() : `-${p1.toLowerCase()}`;
  });
  components.set(componentName, value[1]);
});

const props = defineProps({
  name: {
    type: String,
  },
});

const rowComponent = computed(() => {
  if (!props.name) {
    return null;
  }
  return components.get(`${props.name}-icon`);
});
</script>
