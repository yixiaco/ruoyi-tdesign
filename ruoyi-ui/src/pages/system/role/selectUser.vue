<template>
  <!-- 授权用户 -->
  <t-dialog
    v-model:visible="visible"
    :close-on-overlay-click="false"
    header="选择用户"
    width="800px"
    top="5vh"
    attach="body"
    @confirm="handleSelectUser"
  >
    <t-space direction="vertical">
      <t-form ref="queryRef" :data="queryParams" layout="inline">
        <t-form-item label="用户名称" name="userName">
          <t-input
            v-model="queryParams.userName"
            placeholder="请输入用户名称"
            clearable
            style="width: 200px"
            @enter="handleQuery"
          />
        </t-form-item>
        <t-form-item label="手机号码" name="phonenumber">
          <t-input
            v-model="queryParams.phonenumber"
            placeholder="请输入手机号码"
            clearable
            style="width: 200px"
            @enter="handleQuery"
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
        ref="refTable"
        hover
        :loading="loading"
        row-key="userId"
        :data="userList"
        :columns="columns"
        :selected-row-keys="userIds"
        select-on-row-click
        :pagination="pagination"
        @select-change="handleSelectionChange"
      >
        <template #status="{ row }">
          <dict-tag :options="sys_normal_disable" :value="row.status" />
        </template>
      </t-table>
    </t-space>
  </t-dialog>
</template>
<script lang="ts">
export default {
  name: 'SelectUser',
};
</script>
<script lang="ts" setup>
import { RefreshIcon, SearchIcon } from 'tdesign-icons-vue-next';
import { PrimaryTableCol } from 'tdesign-vue-next';
import { computed, getCurrentInstance, reactive, ref } from 'vue';

import { SysUserVo } from '@/api/system/model/userModel';
import { authUserSelectAll, unallocatedUserList } from '@/api/system/role';

const props = defineProps({
  roleId: {
    type: [Number, String],
  },
});

const { proxy } = getCurrentInstance();
const { sys_normal_disable } = proxy.useDict('sys_normal_disable');

const userList = ref<SysUserVo[]>([]);
const visible = ref(false);
const loading = ref(false);
const total = ref(0);
const userIds = ref([]);

const columns = ref<Array<PrimaryTableCol>>([
  { title: `选择列`, colKey: 'row-select', type: 'multiple', width: 50, align: 'center' },
  { title: `用户名称`, colKey: 'userName', align: 'center', ellipsis: true },
  { title: `用户昵称`, colKey: 'nickName', ellipsis: true, align: 'center' },
  { title: `邮箱`, colKey: 'email', ellipsis: true, align: 'center' },
  { title: `手机`, colKey: 'phonenumber', align: 'center', ellipsis: true },
  { title: `状态`, colKey: 'status', align: 'center', ellipsis: true },
  { title: `创建时间`, colKey: 'createTime', align: 'center', width: '180' },
]);
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  roleId: undefined,
  userName: undefined,
  phonenumber: undefined,
});

const pagination = computed(() => {
  return {
    current: queryParams.pageNum,
    pageSize: queryParams.pageSize,
    total: total.value,
    showJumper: true,
    onChange: (pageInfo) => {
      queryParams.pageNum = pageInfo.current;
      queryParams.pageSize = pageInfo.pageSize;
      getList();
    },
  };
});
// 显示弹框
function show() {
  queryParams.roleId = props.roleId;
  getList();
  visible.value = true;
}
// 多选框选中数据
function handleSelectionChange(selection) {
  userIds.value = selection;
}
// 查询表数据
function getList() {
  loading.value = true;
  unallocatedUserList(queryParams).then((res) => {
    userList.value = res.rows;
    total.value = res.total;
    loading.value = false;
    handleSelectionChange([]);
  });
}
/** 搜索按钮操作 */
function handleQuery() {
  queryParams.pageNum = 1;
  getList();
}
/** 重置按钮操作 */
function resetQuery() {
  proxy.resetForm('queryRef');
  handleQuery();
}
const emit = defineEmits(['ok']);
/** 选择授权用户操作 */
function handleSelectUser() {
  const { roleId } = queryParams;
  const uIds = userIds.value.join(',');
  if (!uIds) {
    proxy.$modal.msgError('请选择要分配的用户');
    return;
  }
  authUserSelectAll({ roleId, userIds: uIds }).then((res) => {
    proxy.$modal.msgSuccess(res.msg);
    if (res.code === 200) {
      visible.value = false;
      emit('ok');
    }
  });
}

defineExpose({
  show,
});
</script>
