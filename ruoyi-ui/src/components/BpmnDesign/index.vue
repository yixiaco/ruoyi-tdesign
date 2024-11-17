<template>
  <div class="design">
    <t-dialog v-model:visible="visible" width="100%" mode="full-screen" :header="title">
      <div class="modeler">
        <bpmn-design ref="bpmnDesignRef" @save-call-back="saveCallBack"></bpmn-design>
      </div>
    </t-dialog>
  </div>
</template>

<script lang="ts" setup>
defineOptions({
  name: 'Design',
});
import { editModelXml, getModelInfo } from '@/api/workflow/model';

const { proxy } = getCurrentInstance();

import type { ModelForm } from '@/api/workflow/model/types';
import BpmnDesign from '@/bpmn/index.vue';
import useDialog from '@/hooks/useDialog';

const bpmnDesignRef = ref<InstanceType<typeof BpmnDesign>>();
const modelForm = ref<ModelForm>();
const emit = defineEmits(['closeCallBack']);
const { visible, title } = useDialog({
  title: '编辑流程',
});
const modelId = ref('');
const open = async (id: string) => {
  visible.value = true;
  modelId.value = id;
  const { data } = await getModelInfo(id);
  modelForm.value = data;
  bpmnDesignRef.value.initDiagram(modelForm.value.xml);
};
// 保存模型
const saveCallBack = async (data: any) => {
  proxy?.$modal.confirm('是否确认保存？', () => {
    data.loading.value = true;
    modelForm.value.id = modelId.value;
    modelForm.value.xml = data.xml;
    modelForm.value.svg = data.svg;
    modelForm.value.key = data.key;
    modelForm.value.name = data.name;
    editModelXml(modelForm.value).then((res) => {
      if (res.code === 200) {
        visible.value = false;
        proxy?.$modal.msgSuccess('保存成功');
        emit('closeCallBack', data);
      }
    });
    data.loading.value = false;
  });
};

/**
 * 对外暴露子组件方法
 */
defineExpose({
  open,
});
</script>

<style lang="less" scoped>
.design {
  :deep(.t-dialog__ctx .t-dialog__body) {
    max-height: 100% !important;
    min-height: calc(100vh - 80px);
    padding: 10px 0 10px 0 !important;
  }
  :deep(.t-dialog__header) {
    padding: 0 0 5px 0 !important;
  }
}
</style>
