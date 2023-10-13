<template>
  <t-checkbox v-model="value" v-bind="$attrs"></t-checkbox>
</template>

<script lang="ts" setup>
defineOptions({
  name: 'TCustomCheckbox',
});
import { computed } from 'vue';

const props = defineProps({
  modelValue: {
    type: [Object, String, Number, Boolean],
  },
  trueLabel: {
    type: [Object, String, Number, Boolean],
  },
  falseLabel: {
    type: [Object, String, Number, Boolean],
  },
});
const emit = defineEmits(['update:modelValue']);
const value = computed<boolean>({
  get() {
    if (typeof props.modelValue === 'boolean') {
      return props.modelValue as boolean;
    }
    return props.modelValue === props.trueLabel;
  },
  set(val) {
    emit('update:modelValue', val ? props.trueLabel || val : props.falseLabel || val);
  },
});
</script>

<style scoped></style>
