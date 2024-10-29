<template>
  <t-dialog
    v-model="visible"
    header="审批记录"
    :width="props.width"
    :height="props.height"
    attach="body"
    :close-on-overlay-click="false"
  >
    <t-loading :loading="loading">
      <div style="width: 100%; height: 300px; overflow: auto; position: relative">
        <div
          v-for="(graphic, index) in graphicInfoVos"
          :key="index"
          :style="{
            position: 'absolute',
            left: `${graphic.x}px`,
            top: `${graphic.y}px`,
            width: `${graphic.width}px`,
            height: `${graphic.height}px`,
            cursor: 'pointer',
            zIndex: 99,
          }"
          @mouseover="handleMouseOver(graphic)"
          @mouseleave="handleMouseLeave()"
        ></div>
        <!-- 弹出的 div 元素 -->
        <div
          v-show="popupVisible"
          class="triangle"
          :style="{
            position: 'absolute',
            left: `${graphicX}px`,
            top: `${graphicY}px`,
            backgroundColor: '#fff',
            padding: '10px',
            zIndex: 100,
          }"
        >
          <p>审批人员: {{ nodeInfo.nickName }}</p>
          <p>节点状态：{{ nodeInfo.status }}</p>
          <p>开始时间：{{ nodeInfo.startTime }}</p>
          <p>结束时间：{{ nodeInfo.endTime }}</p>
          <p>审批耗时：{{ nodeInfo.runDuration }}</p>
        </div>
        <t-image :src="src" />
      </div>
      <div>
        <t-table :data="historyList" row-key="id" :columns="columns" style="width: 100%" max-height="570px">
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
      </div>
    </t-loading>
  </t-dialog>
</template>
<script lang="ts" setup>
import type { PrimaryTableCol } from 'tdesign-vue-next';
import { ref } from 'vue';

import { getHistoryImage, getHistoryRecord } from '@/api/workflow/processInstance';

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
const src = ref('');
const visible = ref(false);
const historyList = ref<Array<any>>([]);
const deleteReason = ref<string>('');
const graphicInfoVos = ref<Array<any>>([]);
const nodeListInfo = ref<Array<any>>([]);
const popupVisible = ref(false);
const nodeInfo = ref<any>({});
const graphicX = ref<number | string>(0);
const graphicY = ref<number | string>(0);

// 列显隐信息
const columns = computed<Array<PrimaryTableCol>>(() => [
  { title: `序号`, colKey: 'serial-number', width: 70 },
  { title: `任务名称`, colKey: 'name', align: 'center' },
  { title: `办理人`, colKey: 'nickName', align: 'center' },
  { title: `状态`, colKey: 'statusName', align: 'center' },
  { title: `审批意见`, colKey: 'comment', align: 'center' },
  { title: `附件`, colKey: 'attachmentList', align: 'center' },
  { title: `开始时间`, colKey: 'startTime', align: 'center', width: '10%', minWidth: 112 },
  { title: `结束时间`, colKey: 'endTime', align: 'center', width: '10%', minWidth: 112 },
  { title: `运行时长`, colKey: 'runDuration', align: 'center' },
]);
// 列显隐信息
const attachmentListColumns = computed<Array<PrimaryTableCol>>(() => [
  { title: `附件名称`, colKey: 'name', align: 'center', width: 202, ellipsis: true },
  { title: `操作`, colKey: 'name', align: 'center', width: 80 },
]);

// 初始化查询审批记录
const init = async (processInstanceId: string) => {
  visible.value = true;
  loading.value = true;
  historyList.value = [];
  graphicInfoVos.value = [];
  getHistoryImage(processInstanceId).then((res) => {
    src.value = `data:image/png;base64,${res.data}`;
  });
  getHistoryRecord(processInstanceId).then((response) => {
    historyList.value = response.data.historyRecordList;
    graphicInfoVos.value = response.data.graphicInfoVos;
    nodeListInfo.value = response.data.nodeListInfo;
    deleteReason.value = response.data.deleteReason;
    loading.value = false;
  });
};
// 悬浮事件
const handleMouseOver = async (graphic: any) => {
  graphicX.value = graphic.x + graphic.width + 10;
  graphicY.value = graphic.y - graphic.height + -10;
  nodeInfo.value = {};
  if (nodeListInfo.value && nodeListInfo.value.length > 0) {
    const info = nodeListInfo.value.find((e: any) => e.taskDefinitionKey == graphic.nodeId);
    if (info) {
      nodeInfo.value = {
        nickName: info.nickName,
        status: info.status,
        startTime: info.startTime,
        endTime: info.endTime,
        runDuration: info.runDuration,
      };
      popupVisible.value = true;
    }
  }
};
// 关闭
const handleMouseLeave = async () => {
  popupVisible.value = false;
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
<style scoped>
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
</style>
