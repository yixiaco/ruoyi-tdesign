<template>
  <t-card>
    <t-tabs v-model="action" @change="handleChange">
      <t-tab-panel :value="1" label="系统配置" :destroy-on-hide="false" lazy>
        <div class="panel-content">
          <system-config :disabled="disabled" />
        </div>
      </t-tab-panel>
      <t-tab-panel v-if="isSystem" :value="2" label="全局配置" :destroy-on-hide="false" lazy>
        <div class="panel-content">
          <system-global-config :disabled="disabled" />
        </div>
      </t-tab-panel>
    </t-tabs>
  </t-card>
</template>
<script lang="ts" setup>
defineOptions({
  name: 'SysConfig',
});

import type { TabValue } from 'tdesign-vue-next';
import { computed, ref, toRefs } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { DEFAULT_TENANT_ID } from '@/constants';
import { useUserStore } from '@/store';
import { useHasPermission } from '@/utils/auth';

import SystemConfig from './components/SystemConfig.vue';
import SystemGlobalConfig from './components/SystemGlobalConfig.vue';

const route = useRoute();
const router = useRouter();
const action = ref(Number(route.query.action) || 1);
const { tenantId } = toRefs(useUserStore());
const isSystem = computed(() => tenantId.value === DEFAULT_TENANT_ID);
// 表单禁用
const disabled = !useHasPermission(['system:config:edit']);

function handleChange(value: TabValue) {
  router.push({ path: route.path, query: { action: value } });
}
</script>
<style lang="less" scoped>
.panel-content {
  max-width: 650px;
  margin-top: 20px;
}
</style>
