<template>
  <div>
    <t-collapse v-model="currentCollapseItem" expand-icon-placement="right">
      <t-collapse-panel value="1">
        <template #header>
          <div class="collapse__title">
            <info-circle-filled-icon />
            常规
          </div>
        </template>
        <div>
          <t-form ref="formRef" :data="formData" :rules="formRules" label-width="80px">
            <t-form-item name="id" label="节点 ID">
              <t-input v-model="formData.id" @change="idChange($event as string)"> </t-input>
            </t-form-item>
            <t-form-item name="name" label="节点名称">
              <t-input v-model="formData.name" @change="nameChange($event as string)"> </t-input>
            </t-form-item>
          </t-form>
        </div>
      </t-collapse-panel>

      <t-collapse-panel value="2">
        <template #header>
          <div class="collapse__title">
            <time-filled-icon />
            执行监听器
          </div>
        </template>
        <div>
          <execution-listener :element="element"></execution-listener>
        </div>
      </t-collapse-panel>
    </t-collapse>
  </div>
</template>
<script setup lang="ts">
import type { ModdleElement } from 'bpmn';
import type { GatewayPanel } from 'bpmnDesign';
import { InfoCircleFilledIcon, TimeFilledIcon } from 'tdesign-icons-vue-next';
import type { FormProps } from 'tdesign-vue-next';

import usePanel from '../hooks/usePanel';
import useParseElement from '../hooks/useParseElement';
import ExecutionListener from './property/ExecutionListener.vue';

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
const currentCollapseItem = ref(['1', '2']);
const formData = ref(parseData<GatewayPanel>());

const formRules = ref<FormProps['rules']>({
  processCategory: [{ required: true, message: '请选择' }],
  id: [{ required: true, message: '请输入' }],
  name: [{ required: true, message: '请输入' }],
});
</script>

<style lang="less" scoped></style>
