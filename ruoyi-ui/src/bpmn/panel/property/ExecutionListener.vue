<template>
  <div>
    <t-table
      ref="tableRef"
      hover
      row-key="id"
      size="small"
      max-height="200px"
      :data="tableData"
      :columns="columns"
      :column-controller="{
        hideTriggerButton: true,
      }"
      @row-dblclick="cellDBLClickEvent"
    >
      <template #topContent>
        <div class="mb-1">
          <t-button size="small" theme="primary" @click="insertEvent"> 新增 </t-button>
        </div>
      </template>
      <template #event="{ row }">
        <span>{{ eventSelect.find((e) => e.value === row.event)?.label }}</span>
      </template>
      <template #type="{ row }">
        <span>{{ typeSelect.find((e) => e.value === row.type)?.label }}</span>
      </template>
      <template #operation="{ rowIndex }">
        <t-space :size="8" break-line>
          <t-link theme="danger" hover="color" @click.stop="removeSelectRowEvent(rowIndex)">
            <delete-icon />
          </t-link>
        </t-space>
      </template>
    </t-table>

    <t-dialog
      v-model:visible="formDialog.visible.value"
      :header="formDialog.title.value"
      width="600px"
      placement="center"
      :close-on-overlay-click="false"
      :close-on-esc-keydown="false"
      :close-btn="false"
      attach="body"
    >
      <t-form ref="formRef" :data="formData" :rules="tableRules" label-width="100px">
        <t-form-item label="事件" name="event">
          <t-select v-model="formData.event">
            <t-option v-for="item in eventSelect" :key="item.id" :value="item.value" :label="item.label"></t-option>
          </t-select>
        </t-form-item>
        <t-form-item label="类型" name="type">
          <template #label>
            <span>
              类型
              <t-tooltip placement="top">
                <help-circle-filled-icon />
                <template #content>
                  类：示例 com.company.MyCustomListener，自定义类必须实现 org.flowable.engine.delegate.TaskListener
                  接口<br />
                  表达式：示例 ${myObject.callMethod(task, task.eventName)}<br />
                  委托表达式：示例 ${myListenerSpringBean} ，该 springBean 需要实现
                  org.flowable.engine.delegate.TaskListener 接口
                </template>
              </t-tooltip>
            </span>
          </template>
          <t-select v-model="formData.type">
            <t-option v-for="item in typeSelect" :key="item.id" :value="item.value" :label="item.label"></t-option>
          </t-select>
        </t-form-item>
        <t-form-item
          :label="
            typeSelect.filter((e) => e.value === formData.type)[0]
              ? typeSelect.filter((e) => e.value === formData.type)[0]?.label
              : '表达式'
          "
          name="className"
        >
          <t-input v-model="formData.className" type="text"></t-input>
        </t-form-item>
      </t-form>
      <t-tabs type="border-card" default-value="1">
        <t-tab-panel label="参数" value="1">
          <listener-param ref="listenerParamRef" v-model:table-data="formData.params" />
        </t-tab-panel>
      </t-tabs>
      <template #footer>
        <div class="dialog-footer">
          <t-button variant="outline" @click="formDialog.closeDialog">取 消</t-button>
          <t-button theme="primary" @click="submitEvent">确 定</t-button>
        </div>
      </template>
    </t-dialog>
  </div>
</template>
<script setup lang="ts">
import type { ModdleElement } from 'bpmn';
import type { ExecutionListenerVO } from 'bpmnDesign';
import cloneDeep from 'lodash/cloneDeep';
import { DeleteIcon, HelpCircleFilledIcon } from 'tdesign-icons-vue-next';
import type { FormInstanceFunctions, FormProps, PrimaryTableCol, TableProps } from 'tdesign-vue-next';

import useDialog from '@/hooks/useDialog';
import useModelerStore from '@/store/modules/modeler';

import usePanel from '../../hooks/usePanel';
import ListenerParam from './ListenerParam.vue';

const emit = defineEmits(['close']);
interface PropType {
  element: ModdleElement;
}
const props = withDefaults(defineProps<PropType>(), {});

const { proxy } = getCurrentInstance();

const selectRow = ref<ExecutionListenerVO | null>();
const formDialog = useDialog({
  title: selectRow.value ? '编辑&保存' : '新增&保存',
});

const { showConfig, elementType, updateProperties } = usePanel({
  element: toRaw(props.element),
});
const { getModdle } = useModelerStore();
const moddle = getModdle();

const listenerParamRef = ref<InstanceType<typeof ListenerParam>>();
const tableRef = ref<ExecutionListenerVO[]>();
const formRef = ref<FormInstanceFunctions>();

// 列显隐信息
const columns = ref<Array<PrimaryTableCol>>([
  { colKey: 'serial-number' },
  { title: `事件`, colKey: 'event', align: 'center' },
  { title: `类型`, colKey: 'type', align: 'center' },
  { title: `Java 类名`, colKey: 'className', align: 'center' },
  { title: `操作`, colKey: 'operation', align: 'center' },
]);

const initData: ExecutionListenerVO = {
  id: '',
  event: '',
  type: '',
  className: '',
  params: [],
};
const formData = ref<ExecutionListenerVO>({ ...initData });
const tableData = ref<ExecutionListenerVO[]>([]);
const tableRules = ref<FormProps['rules']>({
  event: [{ required: true, message: '请选择' }],
  type: [{ required: true, message: '请选择' }],
  className: [{ required: true, message: '请输入' }],
});

const submitEvent = async () => {
  const validate0 = await listenerParamRef.value.validate();
  await formRef.value.validate().then((validate) => {
    if (validate === true && Object.keys(validate0.result).length === 0) {
      const $table = tableRef.value;
      if ($table) {
        if (selectRow.value) {
          Object.assign(selectRow.value, formData.value);
        } else {
          tableData.value.push({ ...formData.value });
        }
        updateElement();
        formDialog.closeDialog();
      }
    }
  });
};

const removeSelectRowEvent = async (rowIndex: number) => {
  tableData.value.splice(rowIndex, 1);
  updateElement();
};
const insertEvent = async () => {
  formData.value = cloneDeep(initData);
  formData.value.id = new Date().getTime().toString();
  selectRow.value = null;
  formDialog.openDialog();
};

const editEvent = (row: ExecutionListenerVO) => {
  Object.assign(formData.value, row);
  selectRow.value = cloneDeep(row);
  formDialog.openDialog();
};

const updateElement = () => {
  // 清除旧值
  let extensionElements = props.element.businessObject.extensionElements;
  if (extensionElements) {
    extensionElements.values =
      extensionElements.values?.filter((item) => item.$type !== 'flowable:ExecutionListener') ?? [];
  }
  if (tableData.value) {
    if (!extensionElements) {
      extensionElements = moddle.create('bpmn:ExtensionElements');
    }
    tableData.value.forEach((item) => {
      const executionListener = moddle.create('flowable:ExecutionListener');
      executionListener.event = item.event;
      executionListener[item.type] = item.className;
      executionListener.id = item.id;
      if (item.params && item.params.length) {
        item.params.forEach((field) => {
          const fieldElement = moddle.create('flowable:Field');
          fieldElement.name = field.name;
          fieldElement[field.type] = field.value;
          executionListener.get('fields').push(fieldElement);
        });
      }
      extensionElements.get('values').push(executionListener);
    });
    updateProperties({ extensionElements });
  }
};

const cellDBLClickEvent: TableProps['onRowDblclick'] = ({ row }) => {
  editEvent(row);
};

const initTableData = () => {
  tableData.value =
    props.element.businessObject.extensionElements?.values
      .filter((item) => item.$type === 'flowable:ExecutionListener')
      .map((item) => {
        let type;
        if ('class' in item) type = 'class';
        if ('expression' in item) type = 'expression';
        if ('delegateExpression' in item) type = 'delegateExpression';
        return {
          id: item.id,
          event: item.event,
          type,
          className: item[type],
          params:
            item.fields?.map((field) => {
              let fieldType;
              if ('stringValue' in field) fieldType = 'stringValue';
              if ('expression' in field) fieldType = 'expression';
              return {
                name: field.name,
                type: fieldType,
                value: field[fieldType],
              };
            }) ?? [],
        };
      }) ?? [];
};

onMounted(() => {
  initTableData();
});

const typeSelect = [
  { id: '742fdeb7-23b4-416b-ac66-cd4ec8b901b7', label: '类', value: 'class' },
  { id: '660c9c46-8fae-4bae-91a0-0335420019dc', label: '表达式', value: 'expression' },
  { id: '4b8135ab-6bc3-4a0f-80be-22f58bc6c5fd', label: '委托表达式', value: 'delegateExpression' },
];
const eventSelect = [
  { id: 'e6e0a51a-2d5d-4dc4-b847-b5c14f43a6ab', label: '开始', value: 'start' },
  { id: '6da97c1e-15fc-4445-8943-75d09f49778e', label: '结束', value: 'end' },
  { id: '6a2cbcec-e026-4f11-bef7-fff0b5c871e2', label: '启用', value: 'take' },
];
</script>

<style scoped lang="less">
.el-badge {
  :deep(.el-badge__content) {
    top: 10px;
  }
}
</style>
