<template>
  <div class="app-container">
    <t-row :gutter="10">
      <t-col :span="4">
        <t-card>
          <template #header>
            <span>缓存列表</span>
            <t-link style="float: right; padding: 3px 0" theme="primary" hover="color" @click.stop="refreshCacheNames">
              <refresh-icon />
            </t-link>
          </template>
          <t-table
            :loading="loading"
            :data="cacheNames"
            row-key="cacheName"
            :columns="cacheNameColumns"
            :height="tableHeight"
            style="width: 100%"
            @row-click="getCacheKeys"
          >
            <template #cacheName="{ row }">
              <span>{{ nameFormatter(row) }}</span>
            </template>
            <template #operation="{ row }">
              <t-link theme="danger" hover="color" @click.stop="handleClearCacheName(row)">
                <delete-icon />
              </t-link>
            </template>
          </t-table>
        </t-card>
      </t-col>

      <t-col :span="4">
        <t-card>
          <template #header>
            <span>键名列表</span>
            <t-link style="float: right; padding: 3px 0" theme="primary" hover="color" @click.stop="refreshCacheKeys">
              <refresh-icon />
            </t-link>
          </template>
          <t-table
            :loading="subLoading"
            :data="cacheKeys"
            row-key="cacheKey"
            :columns="cacheKeysColumns"
            :height="tableHeight"
            style="width: 100%"
            @row-click="handleCacheValue"
          >
            <template #cacheKey="{ row }">
              <span>{{ keyFormatter(row.cacheKey) }}</span>
            </template>
            <template #operation="{ row }">
              <t-link theme="danger" hover="color" @click.stop="handleClearCacheKey(row)">
                <delete-icon />
              </t-link>
            </template>
          </t-table>
        </t-card>
      </t-col>

      <t-col :span="4">
        <t-card style="height: calc(100vh - 250px)">
          <template #header>
            <span>缓存内容</span>
            <t-link
              style="float: right; padding: 3px 0"
              theme="primary"
              hover="color"
              @click.stop="handleClearCacheAll"
            >
              <refresh-icon />清理全部
            </t-link>
          </template>
          <t-form :data="cacheForm">
            <t-row :gutter="[0, 32]">
              <t-col :offset="0.5" :span="11">
                <t-form-item label="缓存名称:" prop="cacheName">
                  <t-input v-model="cacheForm.cacheName" :read-only="true" />
                </t-form-item>
              </t-col>
              <t-col :offset="0.5" :span="11">
                <t-form-item label="缓存键名:" prop="cacheKey">
                  <t-input v-model="cacheForm.cacheKey" :read-only="true" />
                </t-form-item>
              </t-col>
              <t-col :offset="0.5" :span="11">
                <t-form-item label="缓存内容:" prop="cacheValue">
                  <t-textarea v-model="cacheForm.cacheValue" :autosize="{ minRows: 8, maxRows: 8 }" :read-only="true" />
                </t-form-item>
              </t-col>
            </t-row>
          </t-form>
        </t-card>
      </t-col>
    </t-row>
  </div>
</template>
<script lang="ts">
export default {
  name: 'CacheList',
};
</script>
<script lang="ts" setup>
import { getCurrentInstance, ref } from 'vue';
import { DeleteIcon, RefreshIcon } from 'tdesign-icons-vue-next';
import {
  listCacheName,
  listCacheKey,
  getCacheValue,
  clearCacheName,
  clearCacheKey,
  clearCacheAll,
} from '@/api/monitor/cache';

const { proxy } = getCurrentInstance();

const cacheNames = ref([]);
const cacheKeys = ref([]);
const cacheForm = ref({});
const loading = ref(true);
const subLoading = ref(false);
const nowCacheName = ref('');
const tableHeight = ref(window.innerHeight - 340);

const cacheNameColumns = ref([
  { title: '序号', colKey: 'serial-number', width: 80, align: 'center' },
  { title: `缓存名称`, colKey: 'cacheName', align: 'center', ellipsis: true },
  { title: `备注`, colKey: 'remark', align: 'center', ellipsis: true },
  { title: `操作`, colKey: 'operation', align: 'center' },
]);

const cacheKeysColumns = ref([
  { title: '序号', colKey: 'serial-number', width: 80, align: 'center' },
  { title: `缓存键名`, colKey: 'cacheKey', align: 'center', ellipsis: true },
  { title: `操作`, colKey: 'operation', align: 'center' },
]);

/** 查询缓存名称列表 */
function getCacheNames() {
  loading.value = true;
  listCacheName().then((response) => {
    cacheNames.value = response.data;
    loading.value = false;
  });
}

/** 刷新缓存名称列表 */
function refreshCacheNames() {
  getCacheNames();
  proxy.$modal.msgSuccess('刷新缓存列表成功');
}

/** 清理指定名称缓存 */
function handleClearCacheName(row) {
  clearCacheName(row.cacheName).then((response) => {
    proxy.$modal.msgSuccess(`清理缓存名称[${nowCacheName.value}]成功`);
    getCacheKeys();
  });
}

/** 查询缓存键名列表 */
function getCacheKeys(context?) {
  const row = context?.row;
  const cacheName = row !== undefined ? row.cacheName : nowCacheName.value;
  if (cacheName === '') {
    return;
  }
  subLoading.value = true;
  listCacheKey(cacheName).then((response) => {
    cacheKeys.value = response.data.map((value) => {
      return { cacheKey: value };
    });
    subLoading.value = false;
    nowCacheName.value = cacheName;
  });
}

/** 刷新缓存键名列表 */
function refreshCacheKeys() {
  getCacheKeys();
  proxy.$modal.msgSuccess('刷新键名列表成功');
}

/** 清理指定键名缓存 */
function handleClearCacheKey(cacheKey) {
  clearCacheKey(nowCacheName.value, cacheKey).then((response) => {
    proxy.$modal.msgSuccess(`清理缓存键名[${cacheKey}]成功`);
    getCacheKeys();
  });
}

/** 列表前缀去除 */
function nameFormatter(row) {
  return row.cacheName.replace(':', '');
}

/** 键名前缀去除 */
function keyFormatter(cacheKey) {
  return cacheKey.replace(nowCacheName.value, '');
}

/** 查询缓存内容详细 */
function handleCacheValue(context) {
  const { cacheKey } = context.row;
  getCacheValue(nowCacheName.value, cacheKey).then((response) => {
    cacheForm.value = response.data;
  });
}

/** 清理全部缓存 */
function handleClearCacheAll() {
  clearCacheAll().then((response) => {
    proxy.$modal.msgSuccess('清理全部缓存成功');
  });
}

getCacheNames();
</script>
