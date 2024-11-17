<template>
  <t-dialog v-model:visible="data.visible" title="预览" width="70%" attach="body" :close-on-overlay-click="false">
    <div v-if="data.type === 'bpmn' && data.xmlStr">
      <bpmn-viewer ref="bpmnViewerRef"></bpmn-viewer>
    </div>
    <div v-if="data.type === 'xml' && data.xmlStr">
      <preview-code language="xml" code="data.xmlStr" />
    </div>
    <template #footer>
      <span v-if="data.type === 'xml'" class="dialog-footer"> </span>
    </template>
  </t-dialog>
</template>

<script setup lang="ts">
import BpmnViewer from '@/components/BpmnView/index.vue';

const data = reactive({
  visible: false,
  type: '',
  xmlStr: '',
});

const bpmnViewerRef = ref<InstanceType<typeof BpmnViewer>>();
type PreviewType = 'xml' | 'bpmn';
// 打开
const openDialog = (xmlStr: string, type: PreviewType) => {
  data.visible = true;
  data.xmlStr = xmlStr;
  data.type = type;
  /** 流程图 */
  if (type === 'bpmn') {
    /** 必须放在nextTick 否则第一次打开为空 */
    nextTick(() => {
      bpmnViewerRef.value?.initXml(data.xmlStr);
    });
  }
};
/**
 * 对外暴露子组件方法
 */
defineExpose({
  openDialog,
});
</script>
