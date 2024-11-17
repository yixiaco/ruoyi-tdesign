<template>
  <div>
    <t-collapse v-model="currentCollapseItem">
      <t-collapse-panel value="1">
        <template #header>
          <div class="collapse__title">
            <info-circle-filled-icon />
            常规
          </div>
        </template>
        <div>
          <t-form ref="formRef" :data="formData" :rules="formRules" label-width="90px">
            <t-form-item name="id" label="节点 ID">
              <t-input v-model="formData.id" @change="idChange($event as string)"></t-input>
            </t-form-item>
            <t-form-item name="name" label="节点名称">
              <t-input v-model="formData.name" @change="nameChange($event as string)"></t-input>
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
import type { ParticipantPanel } from 'bpmnDesign';
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

const formData = ref(parseData<ParticipantPanel>());
const currentCollapseItem = ref(['1', '2']);
const formRules = ref<FormProps['rules']>({
  id: [{ required: true, message: '请输入', trigger: 'blur' }],
  name: [{ required: true, message: '请输入', trigger: 'blur' }],
});
</script>

<style lang="less" scoped></style>
