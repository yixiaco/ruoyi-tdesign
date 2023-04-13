<template>
  <t-select
    v-if="userId === 1 && tenantEnabled"
    v-model="tenantId"
    clearable
    filterable
    reserve-keyword
    placeholder="请选择租户"
    @change="dynamicTenantEvent"
    @clear="dynamicClearEvent"
  >
    <template #prefixIcon>
      <company class="t-icon" />
    </template>
    <t-option v-for="item in tenantList" :key="item.tenantId" :label="item.companyName" :value="item.tenantId" />
  </t-select>
</template>

<script lang="ts" setup>
import { onMounted, ref, toRefs } from 'vue';
import { useRouter } from 'vue-router';

import { getTenantList } from '@/api/login';
import { TenantListVo } from '@/api/model/loginModel';
import { dynamicClear, dynamicTenant } from '@/api/system/tenant';
import Company from '@/assets/icons/svg/company.svg?component';
import { useTabsRouterStore, useUserStore } from '@/store';

const { userId } = toRefs(useUserStore());
const tenantId = ref(undefined);
const tabsRouterStore = useTabsRouterStore();
const router = useRouter();
// 租户列表
const tenantList = ref<TenantListVo[]>([]);
// 租户开关
const tenantEnabled = ref(false);

const emit = defineEmits(['dynamicChange']);

/**
 * 动态切换租户
 */
function dynamicTenantEvent() {
  if (tenantId.value != null && tenantId.value !== '') {
    dynamicTenant(tenantId.value).then(() => {
      emit('dynamicChange', true);
      tabsRouterStore.removeTabRouterList();
      router.push('/');
    });
  }
}

/**
 * 清理动态租户
 */
function dynamicClearEvent() {
  dynamicClear().then(() => {
    emit('dynamicChange', false);
    tabsRouterStore.removeTabRouterList();
    router.push('/');
  });
}

// 租户列表
function initTenantList() {
  getTenantList().then((res) => {
    const vo = res.data;
    tenantEnabled.value = !!vo.tenantEnabled;
    if (tenantEnabled.value) {
      tenantList.value = vo.voList;
    }
  });
}

onMounted(() => {
  initTenantList();
});
</script>

<style scoped></style>
