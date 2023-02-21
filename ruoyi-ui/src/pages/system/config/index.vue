<template>
  <t-card>
    <t-tabs v-model="action">
      <t-tab-panel :value="1" label="系统配置" :destroy-on-hide="false">
        <div class="panel-content">
          <system-config :action="action === 1" :disabled="disabled" />
        </div>
      </t-tab-panel>
      <t-tab-panel :value="2" label="邮箱配置" :destroy-on-hide="false">
        <div class="panel-content">
          <system-mail :action="action === 2" :disabled="disabled" />
        </div>
      </t-tab-panel>
      <t-tab-panel :value="3" label="短信配置" :destroy-on-hide="false">
        <div class="panel-content">
          <system-sms :action="action === 3" :disabled="disabled" />
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
import { ref } from 'vue';
import SystemConfig from './components/SystemConfig.vue';
import SystemMail from '@/pages/system/config/components/SystemMail.vue';
import SystemSms from '@/pages/system/config/components/SystemSms.vue';
import { useHasPermission } from '@/utils/auth';

const action = ref(1);
// 表单禁用
const disabled = !useHasPermission(['system:config:edit']);
</script>
<style lang="less" scoped>
.panel-content {
  max-width: 650px;
  margin-top: 20px;
}
</style>
