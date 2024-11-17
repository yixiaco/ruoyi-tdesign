<template>
  <div>
    <t-collapse v-model="currentCollapseItem" expand-icon-placement="right" borderless>
      <t-collapse-panel value="1">
        <template #header>
          <div class="collapse__title">
            <info-circle-filled-icon />
            常规
          </div>
        </template>
        <div>
          <t-form ref="formRef" :data="formData" :rules="formRules" labt-width="90px">
            <t-form-item name="id" label="节点 ID">
              <t-input v-model="formData.id" @change="idChange($event as string)"> </t-input>
            </t-form-item>
            <t-form-item name="name" label="节点名称">
              <t-input v-model="formData.name" @change="nameChange($event as string)"> </t-input>
            </t-form-item>
            <t-form-item name="conditionExpression" label="跳转条件">
              <t-input
                v-model="formData.conditionExpressionValue"
                @change="conditionExpressionChange($event as string)"
              >
              </t-input>
            </t-form-item>
            <t-form-item name="skipExpression" label="跳过表达式">
              <t-input v-model="formData.skipExpression" @change="skipExpressionChange($event as string)"> </t-input>
            </t-form-item>
          </t-form>
        </div>
      </t-collapse-panel>

      <t-divider style="margin: 5px 0" />

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
import type { SequenceFlowPanel } from 'bpmnDesign';
import { InfoCircleFilledIcon, TimeFilledIcon } from 'tdesign-icons-vue-next';
import type { FormProps } from 'tdesign-vue-next';

import useModelerStore from '@/store/modules/modeler';

import usePanel from '../hooks/usePanel';
import useParseElement from '../hooks/useParseElement';
import ExecutionListener from './property/ExecutionListener.vue';

interface PropType {
  element: ModdleElement;
}
const props = withDefaults(defineProps<PropType>(), {});
const { nameChange, idChange, updateProperties } = usePanel({
  element: toRaw(props.element),
});
const { parseData } = useParseElement({
  element: toRaw(props.element),
});
const moddle = useModelerStore().getModdle();
const currentCollapseItem = ref(['1', '2']);
const formData = ref(parseData<SequenceFlowPanel>());

const formRules = ref<FormProps['rules']>({
  processCategory: [{ required: true, message: '请选择' }],
  id: [{ required: true, message: '请输入' }],
  name: [{ required: true, message: '请输入' }],
});

const conditionExpressionChange = (val: string) => {
  if (val) {
    const newCondition = moddle.create('bpmn:FormalExpression', { body: val });
    updateProperties({ conditionExpression: newCondition });
  } else {
    updateProperties({ conditionExpression: null });
  }
};

const skipExpressionChange = (val: string) => {
  updateProperties({ 'flowable:skipExpression': val });
};

onBeforeMount(() => {
  if (formData.value.conditionExpression) {
    formData.value.conditionExpressionValue = formData.value.conditionExpression.body;
  }
});
</script>

<style lang="less" scoped></style>
