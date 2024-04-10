<template>
  <t-loading :loading="loading" size="small">
    <t-timeline mode="same">
      <t-timeline-item v-for="item in list" :key="item.infoId" :label="item.loginTime">
        <t-space direction="horizontal">
          {{ item.msg }} <dict-tag :options="sys_device_type" :value="item.deviceType" />
        </t-space>
      </t-timeline-item>
    </t-timeline>
    <t-pagination-mini :disabled="pageDisabled" style="text-align: center" @change="onChange($event.trigger)" />
  </t-loading>
</template>
<script setup lang="ts">
import type { JumperDisabledConfig } from 'tdesign-vue-next';
import { computed, getCurrentInstance, ref } from 'vue';

import type { SysLogininforQuery, SysLogininforVo } from '@/api/monitor/model/logininforModel';
import { loginLogList } from '@/api/system/profile';

const { proxy } = getCurrentInstance();
const { sys_device_type } = proxy.useDict('sys_device_type');

const list = ref<SysLogininforVo[]>([]);
const total = ref(0);
const loading = ref(false);
const pageDisabled = computed<JumperDisabledConfig>(() => {
  return {
    prev: queryParams.value.pageNum <= 1,
    current: false,
    next:
      queryParams.value.pageNum >=
      Math.max(Math.min(total.value && Math.ceil(total.value / queryParams.value.pageSize)), 1),
  };
});
// 查询参数
const queryParams = ref<SysLogininforQuery>({
  pageNum: 1,
  pageSize: 8,
});

function onChange(trigger: 'prev' | 'current' | 'next') {
  switch (trigger) {
    case 'prev':
      queryParams.value.pageNum = Math.max(queryParams.value.pageNum - 1, 1);
      break;
    case 'next':
      queryParams.value.pageNum = Math.max(
        Math.min(queryParams.value.pageNum + 1, total.value && Math.ceil(total.value / queryParams.value.pageSize)),
        1,
      );
      break;
    default:
  }
  getLoginLogList();
}

function getLoginLogList() {
  loading.value = true;
  loginLogList(queryParams.value)
    .then((res) => {
      list.value = res.rows;
      total.value = res.total;
    })
    .finally(() => (loading.value = false));
}

getLoginLogList();
</script>
