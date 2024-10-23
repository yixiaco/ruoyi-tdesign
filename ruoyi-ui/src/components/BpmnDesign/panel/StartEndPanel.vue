<template>
  <div>
    <t-form ref="formRef" :data="formData" :rules="formRules" label-width="80px">
      <t-form-item name="id" label="节点 ID">
        <t-input v-model="formData.id" @change="idChange($event as string)"> </t-input>
      </t-form-item>
      <t-form-item name="name" label="节点名称">
        <t-input v-model="formData.name" @change="nameChange($event as string)"> </t-input>
      </t-form-item>
      <t-form-item label="执行监听器" style="margin-bottom: 0"> </t-form-item>
      <ExecutionListener :element="element"></ExecutionListener>
    </t-form>
  </div>
</template>
<script setup lang="ts">
import type { ModdleElement } from 'bpmn';
import type { StartEndPanel } from 'bpmnDesign';
import type { FormProps } from 'tdesign-vue-next';

import usePanel from '@/components/BpmnDesign/hooks/usePanel';
import useParseElement from '@/components/BpmnDesign/hooks/useParseElement';

interface PropType {
  element: ModdleElement;
}
const props = withDefaults(defineProps<PropType>(), {});
const { nameChange, idChange } = usePanel({
  element: toRaw(props.element),
});
const { parseData } = useParseElement({
  element: toRaw(props.element),
});

const formData = ref(parseData<StartEndPanel>());

const formRules = ref<FormProps['rules']>({
  id: [{ required: true, message: '请输入', trigger: 'blur' }],
  name: [{ required: true, message: '请输入', trigger: 'blur' }],
});
</script>

<style lang="less" scoped></style>
