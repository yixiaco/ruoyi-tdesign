<template>
  <t-card v-show="showMode === 'tabs'">
    <t-tabs :default-value="1">
      <t-tab-panel :value="1" label="系统配置" :destroy-on-hide="false">
        <system-config />
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

const showMode = ref<'tabs' | 'list'>('tabs');
</script>
