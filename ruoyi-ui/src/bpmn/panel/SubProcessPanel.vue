<template>
  <div>
    <t-form ref="formRef" :data="formData" :rules="formRules" label-width="90px">
      <t-collapse v-model="currentCollapseItem">
        <t-collapse-panel value="1">
          <template #header>
            <div class="collapse__title">
              <info-circle-filled-icon />
              常规
            </div>
          </template>
          <div>
            <t-form-item name="id" label="节点 ID">
              <t-input v-model="formData.id" @change="idChange($event as string)"> </t-input>
            </t-form-item>
            <t-form-item name="name" label="节点名称">
              <t-input v-model="formData.name" @change="nameChange($event as string)"> </t-input>
            </t-form-item>
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
        <t-collapse-panel value="3">
          <template #title>
            <div class="collapse__title">
              <tab-icon />
              多实例
            </div>
          </template>
          <div>
            <t-form-item label="多实例类型">
              <t-select v-model="formData.multiInstanceType" @change="multiInstanceTypeChange">
                <t-option
                  v-for="item in constant.MultiInstanceType"
                  :key="item.id"
                  :value="item.value"
                  :label="item.label"
                >
                </t-option>
              </t-select>
            </t-form-item>

            <div v-if="formData.multiInstanceType !== MultiInstanceTypeEnum.NONE">
              <t-form-item label="集合">
                <template #label>
                  <span>
                    集合
                    <t-tooltip placement="top">
                      <help-circle-filled-icon />
                      <template #content>
                        属性会作为表达式进行解析。如果表达式解析为字符串而不是一个集合，<br />
                        不论是因为本身配置的就是静态字符串值，还是表达式计算结果为字符串，<br />
                        这个字符串都会被当做变量名，并从流程变量中用于获取实际的集合。
                      </template>
                    </t-tooltip>
                  </span>
                </template>
                <t-input v-model="formData.collection" @change="collectionChange"></t-input>
              </t-form-item>
              <t-form-item label="元素变量">
                <template #label>
                  <span>
                    元素变量
                    <t-tooltip placement="top">
                      <help-circle-filled-icon />
                      <template #content>
                        每创建一个用户任务前，先以该元素变量为label，集合中的一项为value，<br />
                        创建（局部）流程变量，该局部流程变量被用于指派用户任务。<br />
                        一般来说，该字符串应与指定人员变量相同。
                      </template>
                    </t-tooltip>
                  </span>
                </template>
                <t-input v-model="formData.elementVariable" @change="elementVariableChange"> </t-input>
              </t-form-item>
              <t-form-item label="完成条件">
                <template #label>
                  <span>
                    完成条件
                    <t-tooltip placement="top">
                      <help-circle-filled-icon />
                      <template #content>
                        多实例活动在所有实例都完成时结束，然而也可以指定一个表达式，在每个实例<br />
                        结束时进行计算。当表达式计算为true时，将销毁所有剩余的实例，并结束多实例<br />
                        活动，继续执行流程。例如 ${nrOfCompletedInstances/nrOfInstances >= 0.6 }，<br />
                        表示当任务完成60%时，该节点就算完成
                      </template>
                    </t-tooltip>
                  </span>
                </template>
                <t-input v-model="formData.completionCondition" @change="completionConditionChange"> </t-input>
              </t-form-item>
            </div>
          </div>
        </t-collapse-panel>
      </t-collapse>
    </t-form>
  </div>
</template>
<script setup lang="ts">
import type { ModdleElement } from 'bpmn';
import type { SubProcessPanel } from 'bpmnDesign';
import { HelpCircleFilledIcon, InfoCircleFilledIcon, TabIcon, TimeFilledIcon } from 'tdesign-icons-vue-next';
import type { FormProps } from 'tdesign-vue-next';

import { MultiInstanceTypeEnum } from '@/enums/bpmn/IndexEnums';

import usePanel from '../hooks/usePanel';
import useParseElement from '../hooks/useParseElement';
import ExecutionListener from './property/ExecutionListener.vue';

interface PropType {
  element: ModdleElement;
}
const props = withDefaults(defineProps<PropType>(), {});
const { nameChange, idChange, updateProperties, createModdleElement, constant } = usePanel({
  element: toRaw(props.element),
});
const { parseData } = useParseElement({
  element: toRaw(props.element),
});

const formData = ref(parseData<SubProcessPanel>());
const currentCollapseItem = ref(['1', '2', '3']);

const multiInstanceTypeChange = (newVal) => {
  if (newVal !== MultiInstanceTypeEnum.NONE) {
    let loopCharacteristics = props.element.businessObject.get('loopCharacteristics');
    if (!loopCharacteristics) {
      loopCharacteristics = createModdleElement(
        'bpmn:MultiInstanceLoopCharacteristics',
        {},
        props.element.businessObject,
      );
    }
    loopCharacteristics.isSequential = newVal === MultiInstanceTypeEnum.SERIAL;
    updateProperties({ loopCharacteristics });
  } else {
    updateProperties({ loopCharacteristics: undefined });
  }
};
const collectionChange = (newVal) => {
  let loopCharacteristics = props.element.businessObject.get('loopCharacteristics');
  if (!loopCharacteristics) {
    loopCharacteristics = createModdleElement(
      'bpmn:MultiInstanceLoopCharacteristics',
      {},
      props.element.businessObject,
    );
  }
  loopCharacteristics.collection = newVal && newVal.length > 0 ? newVal : undefined;
  updateProperties({ loopCharacteristics });
};
const elementVariableChange = (newVal) => {
  let loopCharacteristics = props.element.businessObject.get('loopCharacteristics');
  if (!loopCharacteristics) {
    loopCharacteristics = createModdleElement(
      'bpmn:MultiInstanceLoopCharacteristics',
      {},
      props.element.businessObject,
    );
  }
  loopCharacteristics.elementVariable = newVal && newVal.length > 0 ? newVal : undefined;
  updateProperties({ loopCharacteristics });
};
const completionConditionChange = (newVal) => {
  let loopCharacteristics = props.element.businessObject.get<ModdleElement>('loopCharacteristics');
  if (!loopCharacteristics) {
    loopCharacteristics = createModdleElement(
      'bpmn:MultiInstanceLoopCharacteristics',
      {},
      props.element.businessObject,
    );
  }
  if (newVal && newVal.length > 0) {
    if (!loopCharacteristics.completionCondition) {
      loopCharacteristics.completionCondition = createModdleElement(
        'bpmn:Expression',
        { body: newVal },
        loopCharacteristics,
      );
    } else {
      loopCharacteristics.completionCondition.body = newVal;
    }
  } else {
    loopCharacteristics.completionCondition = undefined;
  }
  updateProperties({ loopCharacteristics });
};

onBeforeMount(() => {
  if (formData.value.loopCharacteristics) {
    const loopCharacteristics = formData.value.loopCharacteristics;
    formData.value.collection = loopCharacteristics.collection || '';
    formData.value.elementVariable = loopCharacteristics.elementVariable || '';
    formData.value.completionCondition = loopCharacteristics.completionCondition?.body || '';
    formData.value.multiInstanceType = loopCharacteristics.isSequential
      ? MultiInstanceTypeEnum.SERIAL
      : MultiInstanceTypeEnum.PARALLEL;
  }
});

const formRules = ref<FormProps['rules']>({
  id: [{ required: true, message: '请输入' }],
  name: [{ required: true, message: '请输入' }],
});
</script>

<style lang="less" scoped></style>
