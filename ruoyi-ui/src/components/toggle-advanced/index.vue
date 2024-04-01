<template>
  <t-divider style="width: 100%">
    <t-link theme="primary" hover="color" @click="toggleAdvanced">
      {{ isAdvancedOpenEffective ? collapseText : unfoldText }}{{ moreText }}
    </t-link>
  </t-divider>
  <!-- 这里放置高级选项的内容 -->
  <slot v-if="isAdvancedOpenEffective" />
</template>
<script setup lang="ts">
import { computed, ref } from 'vue';

defineOptions({
  name: 'ToggleAdvanced',
});

defineProps({
  unfoldText: {
    type: String,
    default: '展开',
  },
  collapseText: {
    type: String,
    default: '收起',
  },
  moreText: {
    type: String,
    default: '高级选项',
  },
});

const modelValue = defineModel({
  type: Boolean,
  default: null,
});

const isAdvancedOpen = ref(false);

const isAdvancedOpenEffective = computed({
  get() {
    if (modelValue.value !== null) {
      return modelValue.value;
    }
    return isAdvancedOpen.value;
  },
  set(val) {
    if (modelValue.value !== null) {
      modelValue.value = val;
    }
    isAdvancedOpen.value = val;
  },
});

function toggleAdvanced() {
  isAdvancedOpenEffective.value = !isAdvancedOpenEffective.value;
}
</script>
