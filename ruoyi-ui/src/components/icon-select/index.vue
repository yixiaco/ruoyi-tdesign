<template>
  <t-select
    :value="modelValue"
    placeholder="请选择"
    :style="{ width: '400px' }"
    :popup-props="{ overlayInnerStyle: { width: '400px' } }"
    @change="onChange"
  >
    <t-option v-for="item in options" :key="item.stem" :value="item.stem" class="overlay-options">
      <div>
        <t-icon :name="item.stem" />
      </div>
    </t-option>
    <template #valueDisplay><t-icon :name="modelValue" :style="{ marginRight: '8px' }" />{{ modelValue }}</template>
  </t-select>
</template>
<script lang="ts">
export default {
  name: 'IconSelect',
};
</script>
<script lang="ts" setup>
import { manifest } from 'tdesign-icons-vue-next/lib/manifest';
import { ref } from 'vue';

defineProps({
  modelValue: String,
});
const emit = defineEmits(['update:modelValue', 'change']);
// 获取全部图标的列表
const options = ref(manifest);
const onChange = (value) => {
  emit('update:modelValue', value);
  emit('change', value);
};
</script>
