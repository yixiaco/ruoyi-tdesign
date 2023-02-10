<template>
  <t-card>
    <t-tabs v-model="action">
      <t-tab-panel :value="1" label="系统配置" :destroy-on-hide="false">
        <div style="max-width: 650px; margin-top: 20px">
          <system-config :action="action === 1" />
        </div>
      </t-tab-panel>
      <t-tab-panel :value="2" label="邮箱配置" :destroy-on-hide="false">
        <div style="max-width: 650px; margin-top: 20px">
          <system-mail :action="action === 2" />
        </div>
      </t-tab-panel>
      <t-tab-panel :value="3" label="短信配置" :destroy-on-hide="false">
        <div style="max-width: 650px; margin-top: 20px">
          <system-sms :action="action === 3" />
        </div>
      </t-tab-panel>
    </t-tabs>
  </t-card>
</template>
<script lang="ts">
export default {
  name: 'Config',
};
</script>
<script lang="ts" setup>
import { ref } from 'vue';
import SystemConfig from './components/SystemConfig.vue';
import SystemMail from '@/pages/system/config/components/SystemMail.vue';
import SystemSms from '@/pages/system/config/components/SystemSms.vue';

const action = ref(1);
</script>
