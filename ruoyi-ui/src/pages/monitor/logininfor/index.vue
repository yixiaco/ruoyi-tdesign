<template>
  <t-card>
    <t-space direction="vertical">
      <t-form v-show="showSearch" ref="queryRef" :data="queryParams" layout="inline" label-width="68px">
        <t-form-item label="登录地址" name="ipaddr">
          <t-input
            v-model="queryParams.ipaddr"
            placeholder="请输入登录地址"
            clearable
            style="width: 240px"
            @enter="handleQuery"
          />
        </t-form-item>
        <t-form-item label="用户名称" name="userName">
          <t-input
            v-model="queryParams.userName"
            placeholder="请输入用户名称"
            clearable
            style="width: 240px"
            @enter="handleQuery"
          />
        </t-form-item>
        <t-form-item label="状态" name="status">
          <t-select v-model="queryParams.status" placeholder="登录状态" clearable style="width: 240px">
            <t-option v-for="dict in sys_common_status" :key="dict.value" :label="dict.label" :value="dict.value" />
          </t-select>
        </t-form-item>
        <t-form-item label="登录时间" style="width: 308px">
          <t-date-range-picker
            v-model="dateRange"
            allow-input
            clearable
            separator="-"
            :placeholder="['开始日期', '结束日期']"
          />
        </t-form-item>
        <t-form-item label-width="0px">
          <t-button theme="primary" @click="handleQuery">
            <template #icon> <search-icon /></template>
            搜索
          </t-button>
          <t-button theme="default" @click="resetQuery">
            <template #icon> <refresh-icon /></template>
            重置
          </t-button>
        </t-form-item>
      </t-form>

      <t-table
        v-model:column-controller-visible="columnControllerVisible"
        :loading="loading"
        row-key="infoId"
        :data="logininforList"
        :columns="columns"
        :selected-row-keys="ids"
        select-on-row-click
        :pagination="pagination"
        :column-controller="{
          hideTriggerButton: true,
        }"
        :sort="sort"
        :show-sort-column-bg-color="true"
        @select-change="handleSelectionChange"
        @sort-change="handleSortChange"
      >
        <template #topContent>
          <t-row>
            <t-col flex="auto">
              <t-button
                v-hasPermi="['monitor:logininfor:remove']"
                theme="danger"
                variant="outline"
                :disabled="multiple"
                @click="handleDelete"
              >
                <template #icon> <delete-icon /> </template>
                删除
              </t-button>
              <t-button
                v-hasPermi="['monitor:logininfor:remove']"
                theme="danger"
                variant="outline"
                @click="handleClean"
              >
                <template #icon> <delete-icon /> </template>
                清空
              </t-button>
              <t-button
                v-hasPermi="['monitor:logininfor:unlock']"
                theme="default"
                variant="outline"
                :disabled="single"
                @click="handleUnlock"
              >
                <template #icon> <lock-off-icon /> </template>
                解锁
              </t-button>
              <t-button v-hasPermi="['monitor:operlog:export']" theme="default" variant="outline" @click="handleExport">
                <template #icon> <download-icon /> </template>
                导出
              </t-button>
            </t-col>
            <t-col flex="none">
              <t-button theme="default" shape="square" variant="outline" @click="showSearch = !showSearch">
                <template #icon> <search-icon /> </template>
              </t-button>
              <t-button theme="default" variant="outline" @click="columnControllerVisible = true">
                <template #icon> <setting-icon /> </template>
                列配置
              </t-button>
            </t-col>
          </t-row>
        </template>
        <template #status="{ row }">
          <dict-tag :options="sys_common_status" :value="row.status" />
        </template>
        <template #loginTime="{ row }">
          <span>{{ parseTime(row.loginTime) }}</span>
        </template>
      </t-table>
    </t-space>
  </t-card>
</template>
<script lang="ts">
export default {
  name: 'Logininfor',
};
</script>
<script lang="ts" setup>
import { DeleteIcon, DownloadIcon, LockOffIcon, RefreshIcon, SearchIcon, SettingIcon } from 'tdesign-icons-vue-next';
import { PrimaryTableCol } from 'tdesign-vue-next';
import { computed, getCurrentInstance, ref } from 'vue';

import { cleanLogininfor, delLogininfor, list, unlockLogininfor } from '@/api/monitor/logininfor';
import { SysLogininforBo, SysLogininforVo } from '@/api/monitor/model/logininforModel';

const { proxy } = getCurrentInstance();
const { sys_common_status } = proxy.useDict('sys_common_status');

const logininforList = ref<SysLogininforVo[]>([]);
const loading = ref(false);
const columnControllerVisible = ref(false);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const selectName = ref('');
const total = ref(0);
const dateRange = ref([]);
const defaultSort = ref({ sortBy: 'loginTime', descending: true });
const sort = ref({ ...defaultSort.value });

// 查询参数
const queryParams = ref<SysLogininforBo>({
  pageNum: 1,
  pageSize: 10,
  ipaddr: undefined,
  userName: undefined,
  status: undefined,
  orderByColumn: undefined,
  isAsc: undefined,
});
// 列显隐信息
const columns = ref<Array<PrimaryTableCol>>([
  { title: `选择列`, colKey: 'row-select', type: 'multiple', width: 50, align: 'center' },
  { title: `访问编号`, colKey: 'infoId', align: 'center' },
  { title: `用户名称`, colKey: 'userName', align: 'center' },
  { title: `地址`, colKey: 'ipaddr', align: 'center', ellipsis: true },
  { title: `登录地点`, colKey: 'loginLocation', align: 'center', ellipsis: true },
  { title: `操作系统`, colKey: 'os', align: 'center', sorter: true, ellipsis: true },
  { title: `浏览器`, colKey: 'browser', align: 'center', ellipsis: true },
  { title: `登录状态`, colKey: 'status', align: 'center' },
  { title: `描述`, colKey: 'msg', align: 'center' },
  { title: `访问时间`, colKey: 'loginTime', align: 'center', width: 180, sorter: true },
]);

// 分页
const pagination = computed(() => {
  return {
    current: queryParams.value.pageNum,
    pageSize: queryParams.value.pageSize,
    total: total.value,
    showJumper: true,
    onChange: (pageInfo) => {
      queryParams.value.pageNum = pageInfo.current;
      queryParams.value.pageSize = pageInfo.pageSize;
      getList();
    },
  };
});

/** 查询登录日志列表 */
function getList() {
  loading.value = true;
  list(proxy.addDateRange(queryParams.value, dateRange.value)).then((response) => {
    logininforList.value = response.rows;
    total.value = response.total;
    loading.value = false;
  });
}
/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}
/** 重置按钮操作 */
function resetQuery() {
  dateRange.value = [];
  proxy.resetForm('queryRef');
  queryParams.value.pageNum = 1;
  handleSortChange({ ...defaultSort.value });
}
/** 多选框选中数据 */
function handleSelectionChange(selection, { selectedRowData }) {
  ids.value = selection;
  multiple.value = !selection.length;
  single.value = selection.length !== 1;
  selectName.value = selectedRowData.map((item) => item.userName);
}
/** 排序触发事件 */
function handleSortChange(value) {
  sort.value = value;
  queryParams.value.orderByColumn = value.sortBy;
  queryParams.value.isAsc = value.descending ? 'descending' : 'ascending';
  getList();
}
/** 删除按钮操作 */
function handleDelete(row) {
  const infoIds = row.infoId || ids.value;
  proxy.$modal.confirm(`是否确认删除访问编号为"${infoIds}"的数据项?`, () => {
    return delLogininfor(infoIds).then(() => {
      getList();
      proxy.$modal.msgSuccess('删除成功');
    });
  });
}
/** 清空按钮操作 */
function handleClean() {
  proxy.$modal.confirm('是否确认清空所有登录日志数据项?', () => {
    return cleanLogininfor().then(() => {
      getList();
      proxy.$modal.msgSuccess('清空成功');
    });
  });
}
/** 解锁按钮操作 */
function handleUnlock() {
  const username = selectName.value;
  proxy.$modal.confirm(`是否确认解锁用户"${username}"数据项?`, () => {
    return unlockLogininfor(username).then(() => {
      proxy.$modal.msgSuccess(`用户${username}解锁成功`);
    });
  });
}
/** 导出按钮操作 */
function handleExport() {
  proxy.download(
    'monitor/logininfor/export',
    {
      ...queryParams.value,
    },
    `config_${new Date().getTime()}.xlsx`,
  );
}

getList();
</script>
