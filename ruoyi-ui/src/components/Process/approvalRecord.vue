<template>
  <div class="container">
    <t-dialog
      v-model="visible"
      header="审批记录"
      :width="props.width"
      :height="props.height"
      :close-on-overlay-click="false"
    >
      <t-tabs v-model="tabActiveName" class="demo-tabs">
        <t-tab-panel label="流程图" value="bpmn">
          <bpmn-view ref="bpmnViewRef"></bpmn-view>
        </t-tab-panel>
        <t-tab-panel label="审批信息" value="info">
          <t-loading :loading="loading">
            <t-table :data="historyList" row-key="id" :columns="columns" style="width: 100%">
              <template #statusName="{ row }">
                <t-tag variant="outline" theme="success">{{ row.statusName }}</t-tag>
              </template>
              <template #attachmentList="{ row }">
                <t-popup
                  v-if="row.attachmentList && row.attachmentList.length > 0"
                  placement="right"
                  :overlay-style="{ width: '310px' }"
                  trigger="click"
                >
                  <t-button style="margin-right: 16px">附件</t-button>
                  <template #content>
                    <t-table :data="row.attachmentList" :columns="attachmentListColumns">
                      <template #name="scope">
                        <t-button variant="text" @click="handleDownload(scope.row.contentId)">下载</t-button>
                      </template>
                    </t-table>
                  </template>
                </t-popup>
              </template>
            </t-table>
          </t-loading>
        </t-tab-panel>
      </t-tabs>
    </t-dialog>
  </div>
</template>
<script lang="ts" setup>
import type { PrimaryTableCol } from 'tdesign-vue-next';
import { ref } from 'vue';

import processApi from '@/api/workflow/processInstance';
import BpmnView from '@/components/BpmnView/index.vue';

const { proxy } = getCurrentInstance();

const props = defineProps({
  width: {
    type: String,
    default: '70%',
  },
  height: {
    type: String,
    default: '100%',
  },
});
const loading = ref(false);
const visible = ref(false);
const historyList = ref<Array<any>>([]);
const deleteReason = ref<string>('');
const tabActiveName = ref('bpmn');

const bpmnViewRef = ref<InstanceType<typeof BpmnView>>();

// 列显隐信息
const columns = computed<Array<PrimaryTableCol>>(() => [
  { title: `序号`, colKey: 'serial-number', width: 70 },
  { title: `任务名称`, colKey: 'name', align: 'center' },
  { title: `办理人`, colKey: 'nickName', align: 'center' },
  { title: `状态`, colKey: 'statusName', align: 'center' },
  { title: `审批意见`, colKey: 'comment', align: 'center' },
  { title: `开始时间`, colKey: 'startTime', align: 'center', width: '10%', minWidth: 112 },
  { title: `结束时间`, colKey: 'endTime', align: 'center', width: '10%', minWidth: 112 },
  { title: `运行时长`, colKey: 'runDuration', align: 'center' },
  { title: `附件`, colKey: 'attachmentList', align: 'center' },
]);
// 列显隐信息
const attachmentListColumns = computed<Array<PrimaryTableCol>>(() => [
  { title: `附件名称`, colKey: 'name', align: 'center', width: 202, ellipsis: true },
  { title: `操作`, colKey: 'name', align: 'center', width: 80 },
]);

// 初始化查询审批记录
const init = async (instanceId: string) => {
  visible.value = true;
  loading.value = true;
  tabActiveName.value = 'bpmn';
  historyList.value = [];
  processApi.getHistoryRecord(instanceId).then((resp) => {
    historyList.value = resp.data.historyRecordList;
    deleteReason.value = resp.data.deleteReason;
    loading.value = false;
  });
  await nextTick(() => {
    bpmnViewRef.value.init(instanceId);
  });
};

/** 下载按钮操作 */
const handleDownload = (ossId: string) => {
  proxy?.$download.oss(ossId);
};
/**
 * 对外暴露子组件方法
 */
defineExpose({
  init,
});
</script>
<style lang="less" scoped>
.triangle {
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.3);
  border-radius: 6px;
}

.triangle::after {
  content: ' ';
  position: absolute;
  top: 8em;
  right: 215px;
  border: 15px solid;
  border-color: transparent #fff transparent transparent;
}

.container {
  :deep(.t-dialog__ctx .t-dialog__body) {
    max-height: calc(100vh - 170px) !important;
    min-height: calc(100vh - 170px) !important;
  }
}
</style>
