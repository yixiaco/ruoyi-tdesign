<template>
  <t-card>
    <t-tabs v-model="action" @change="handleChange">
      <t-tab-panel :value="1" label="系统配置" :destroy-on-hide="false">
        <div class="panel-content">
          <system-config :action="action === 1" :disabled="disabled" :is-system="isSystem" />
        </div>
      </t-tab-panel>
    </t-tabs>
  </t-card>
</template>
<script lang="ts">
export default {
  name: 'SysConfig',
};
</script>
<script lang="ts" setup>
import { computed, ref, toRefs } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useUserStore } from '@/store';
import { useHasPermission } from '@/utils/auth';

import SystemConfig from './components/SystemConfig.vue';

const route = useRoute();
const router = useRouter();
const action = ref(Number(route.query.action) || 1);
const { tenantId } = toRefs(useUserStore());
const isSystem = computed(() => tenantId.value === '000000');
// 表单禁用
const disabled = !useHasPermission(['system:config:edit']);

function handleChange(value: number) {
  router.push({ path: route.path, query: { action: value } });
}
</script>
<style lang="less" scoped>
.panel-content {
  max-width: 650px;
  margin-top: 20px;
}
</style>
