<template>
  <t-card>
    <t-space direction="vertical">
      <t-form ref="queryRef" :data="queryParams" layout="inline">
        <t-form-item label="登录地址" name="ipaddr">
          <t-input
            v-model="queryParams.ipaddr"
            placeholder="请输入登录地址"
            clearable
            style="width: 200px"
            @keyup.enter="handleQuery"
          />
        </t-form-item>
        <t-form-item label="用户名称" name="userName">
          <t-input
            v-model="queryParams.userName"
            placeholder="请输入用户名称"
            clearable
            style="width: 200px"
            @keyup.enter="handleQuery"
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
        :loading="loading"
        row-key="tokenId"
        :data="onlineList"
        :columns="columns"
        :pagination="pagination"
        :column-controller="{
          placement: 'top-right',
        }"
        style="width: 100%"
      >
        <template #loginTime="{ row }">
          <span>{{ parseTime(row.loginTime) }}</span>
        </template>
        <template #operation="{ row }">
          <t-link
            v-hasPermi="['monitor:online:forceLogout']"
            theme="danger"
            hover="color"
            @click.stop="handleForceLogout(row)"
          >
            <delete-icon />强退
          </t-link>
        </template>
      </t-table>
    </t-space>
  </t-card>
</template>
<script lang="ts">
export default {
  name: 'Online',
};
</script>
<script lang="ts" setup>
import { DeleteIcon, RefreshIcon, SearchIcon } from 'tdesign-icons-vue-next';
import { computed, getCurrentInstance, ref } from 'vue';
import { forceLogout, list as initData } from '@/api/monitor/online';

const { proxy } = getCurrentInstance();

const onlineList = ref([]);
const loading = ref(false);
const total = ref(0);
const pageNum = ref(1);
const pageSize = ref(10);

const queryParams = ref({
  ipaddr: undefined,
  userName: undefined,
});

// 列显隐信息
const columns = ref([
  { title: '序号', colKey: 'serial-number', width: 80, align: 'center' },
  { title: `会话编号`, colKey: 'tokenId', align: 'center', ellipsis: true },
  { title: `登录名称`, colKey: 'userName', align: 'center', ellipsis: true },
  { title: `所属部门`, colKey: 'deptName', align: 'center', ellipsis: true },
  { title: `主机`, colKey: 'ipaddr', align: 'center', ellipsis: true },
  { title: `登录地点`, colKey: 'loginLocation', align: 'center', ellipsis: true },
  { title: `操作系统`, colKey: 'os', align: 'center', ellipsis: true },
  { title: `浏览器`, colKey: 'browser', align: 'center', ellipsis: true },
  { title: `登录时间`, colKey: 'loginTime', align: 'center', width: 180 },
  { title: `操作`, colKey: 'operation', align: 'center' },
]);

// 分页
const pagination = computed(() => {
  return {
    current: pageNum.value,
    pageSize: pageSize.value,
    total: total.value,
    showJumper: true,
    onChange: (pageInfo) => {
      pageNum.value = pageInfo.current;
      pageSize.value = pageInfo.pageSize;
    },
  };
});

/** 查询登录日志列表 */
function getList() {
  loading.value = true;
  initData(queryParams.value).then((response) => {
    onlineList.value = response.rows;
    total.value = response.total;
    loading.value = false;
  });
}
/** 搜索按钮操作 */
function handleQuery() {
  pageNum.value = 1;
  getList();
}
/** 重置按钮操作 */
function resetQuery() {
  proxy.resetForm('queryRef');
  handleQuery();
}
/** 强退按钮操作 */
function handleForceLogout(row) {
  proxy.$modal.confirm(`是否确认强退名称为"${row.userName}"的用户?`, () => {
    return forceLogout(row.tokenId).then(() => {
      getList();
      proxy.$modal.msgSuccess('删除成功');
    });
  });
}

getList();
</script>
