<template>
  <t-table
    ref="tableRef"
    hover
    row-key="id"
    size="small"
    max-height="300px"
    :data="tableData"
    :columns="columns"
    :column-controller="{
      hideTriggerButton: true,
    }"
  >
    <template #topContent>
      <div class="mt-4 mb-1">
        <t-button size="small" theme="primary" @click="insertRow">
          <template #icon><add-icon /></template>
          新增
        </t-button>
      </div>
    </template>
    <template #operation="{ rowIndex }">
      <t-space :size="8" break-line>
        <t-tooltip content="删除" placement="top">
          <t-link theme="danger" hover="color" @click.stop="removeRow(rowIndex)">
            <delete-icon />
          </t-link>
        </t-tooltip>
      </t-space>
    </template>
  </t-table>
</template>

<script setup lang="ts">
import type { ParamVO } from 'bpmnDesign';
import { AddIcon, DeleteIcon } from 'tdesign-icons-vue-next';
import { Input, type PrimaryTableCol, type PrimaryTableInstanceFunctions, Select } from 'tdesign-vue-next';

import useDialog from '@/hooks/useDialog';

interface PropType {
  height?: string;
}

const { proxy } = getCurrentInstance();

withDefaults(defineProps<PropType>(), {
  height: '200px',
});
const tableData = defineModel<ParamVO[]>('tableData', {
  default: () => [] as ParamVO[],
});

const { title, visible, openDialog, closeDialog } = useDialog({
  title: '监听器参数',
});
const typeSelect = [
  { id: '742fdeb7-23b4-416b-ac66-cd4ec8b901b7', label: '字符串', value: 'stringValue' },
  { id: '660c9c46-8fae-4bae-91a0-0335420019dc', label: '表达式', value: 'expression' },
];

// 列显隐信息
const columns = ref<Array<PrimaryTableCol>>([
  { colKey: 'serial-number', width: '60px' },
  {
    title: `类型`,
    colKey: 'type',
    align: 'center',
    cell: (h, { row }) => typeSelect.find((t) => t.value === row.type)?.label,
    edit: {
      component: Select,
      // props, 透传全部属性到 Select 组件
      props: {
        clearable: true,
        options: typeSelect,
        size: 'small',
      },
      // 除了点击非自身元素退出编辑态之外，还有哪些事件退出编辑态
      // abortEditOnEvent: ['onChange'],
      // 编辑完成，退出编辑态后触发
      onEdited: (context) => {
        tableData.value[context.rowIndex].type = context.newRowData.type;
      },
      rules: [{ required: true, message: '请选择' }],
      // 默认是否为编辑状态
      defaultEditable: true,
    },
  },
  {
    title: `名称`,
    colKey: 'name',
    align: 'center',
    // 编辑状态相关配置，全部集中在 edit
    edit: {
      // 1. 支持任意组件。需保证组件包含 `value` 和 `onChange` 两个属性，且 onChange 的第一个参数值为 new value。
      // 2. 如果希望支持校验，组件还需包含 `status` 和 `tips` 属性。具体 API 含义参考 Input 组件
      component: Input,
      // props, 透传全部属性到 Input 组件。可以是一个函数，不同行有不同的 props 属性 时，使用 Function）
      props: {
        clearable: true,
        autofocus: true,
        size: 'small',
      },
      // 触发校验的时机（when to validate)
      validateTrigger: 'change',
      // 除了点击非自身元素退出编辑态之外，还有哪些事件退出编辑态
      abortEditOnEvent: ['onEnter'],
      // 编辑完成，退出编辑态后触发
      onEdited: (context) => {
        tableData.value[context.rowIndex].name = context.newRowData.name;
      },
      // 校验规则，此处同 Form 表单。https://tdesign.tencent.com/vue-next/components/form
      rules: [{ required: true, message: '请输入' }],
      // 默认是否为编辑状态
      defaultEditable: true,
    },
  },
  {
    title: `值`,
    colKey: 'value',
    align: 'center',
    // 编辑状态相关配置，全部集中在 edit
    edit: {
      // 1. 支持任意组件。需保证组件包含 `value` 和 `onChange` 两个属性，且 onChange 的第一个参数值为 new value。
      // 2. 如果希望支持校验，组件还需包含 `status` 和 `tips` 属性。具体 API 含义参考 Input 组件
      component: Input,
      // props, 透传全部属性到 Input 组件。可以是一个函数，不同行有不同的 props 属性 时，使用 Function）
      props: {
        clearable: true,
        autofocus: true,
        size: 'small',
      },
      // 触发校验的时机（when to validate)
      validateTrigger: 'change',
      // 除了点击非自身元素退出编辑态之外，还有哪些事件退出编辑态
      abortEditOnEvent: ['onEnter'],
      // 编辑完成，退出编辑态后触发
      onEdited: (context) => {
        tableData.value[context.rowIndex].value = context.newRowData.value;
      },
      // 校验规则，此处同 Form 表单。https://tdesign.tencent.com/vue-next/components/form
      rules: [{ required: true, message: '请输入' }],
      // 默认是否为编辑状态
      defaultEditable: true,
    },
  },
  { title: `操作`, colKey: 'operation', align: 'center' },
]);

const tableRef = ref<PrimaryTableInstanceFunctions>();

const insertRow = async () => {
  // 插入一条数据并触发校验
  tableData.value.push({ id: new Date().getTime().toString(), name: '', type: '', value: '' });
  await tableRef.value.validateTableData();
};

const removeRow = async (rowIndex: number) => {
  tableData.value.splice(rowIndex, 1);
};

const validate = async () => {
  return tableRef.value.validateTableData();
};

defineExpose({
  closeDialog,
  openDialog,
  validate,
});
</script>

<style scoped lang="less"></style>
