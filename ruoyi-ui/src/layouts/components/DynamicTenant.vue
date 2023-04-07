<template>
  <t-select
    v-if="userId === 1"
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
import { dynamicClear, dynamicTenant } from '@/api/system/tenant';
import { useTabsRouterStore, useUserStore } from '@/store';
import Company from '@/assets/icons/svg/company.svg?component';

const { userId } = toRefs(useUserStore());
const tenantId = ref(undefined);
const tenantList = ref([]);
const tabsRouterStore = useTabsRouterStore();
const router = useRouter();

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
    tenantList.value = res.data;
  });
}

onMounted(() => {
  initTenantList();
});
</script>

<style scoped></style>
