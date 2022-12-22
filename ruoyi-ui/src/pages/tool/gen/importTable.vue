<template>
  <!-- 导入表 -->
  <t-dialog
    v-model:visible="visible"
    :close-on-overlay-click="false"
    title="导入表"
    width="800px"
    top="5vh"
    attach="body"
    @opened="handleOpen"
    @confirm="handleImportTable"
  >
    <t-form ref="queryRef" :data="queryParams" layout="inline" label-width="70px">
      <t-form-item label="表名称" name="tableName">
        <t-input
          v-model="queryParams.tableName"
          style="width: 150px"
          placeholder="请输入表名称"
          clearable
          @keyup.enter="handleQuery"
        />
      </t-form-item>
      <t-form-item label="表描述" name="tableComment">
        <t-input
          v-model="queryParams.tableComment"
          style="width: 150px"
          placeholder="请输入表描述"
          clearable
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
    <t-row>
      <t-table
        ref="table"
        height="260px"
        :loading="loading"
        row-key="tableName"
        :data="dbTableList"
        :columns="columns"
        :selected-row-keys="tables"
        select-on-row-click
        :pagination="pagination"
        @select-change="handleSelectionChange"
      >
      </t-table>
    </t-row>
  </t-dialog>
</template>

<script lang="ts" setup>
import { computed, getCurrentInstance, reactive, ref } from 'vue';
import { RefreshIcon, SearchIcon } from 'tdesign-icons-vue-next';
import { listDbTable, importTable } from '@/api/tool/gen';

const total = ref(0);
const loading = ref(false);
const visible = ref(false);
const tables = ref([]);
const dbTableList = ref([]);
const { proxy } = getCurrentInstance();

const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  tableName: undefined,
  tableComment: undefined,
});
// 列显隐信息
const columns = ref([
  { title: `选择列`, colKey: 'row-select', type: 'multiple', width: 55, align: 'center' },
  { title: `表名称`, colKey: 'tableName', align: 'center', ellipsis: true },
  { title: `表描述`, colKey: 'tableComment', align: 'center', ellipsis: true },
  { title: `创建时间`, colKey: 'createTime', align: 'center' },
  { title: `更新时间`, colKey: 'updateTime', align: 'center' },
]);

// 分页
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
const emit = defineEmits(['ok']);

/** 查询参数列表 */
function show() {
  getList();
  visible.value = true;
}
/** 多选框选中数据 */
function handleSelectionChange(selection) {
  tables.value = selection;
}
/** 查询表数据 */
function getList() {
  loading.value = true;
  listDbTable(queryParams).then((res) => {
    dbTableList.value = res.rows;
    total.value = res.total;
    loading.value = false;
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
/** 处理打开弹窗事件 */
function handleOpen() {
  tables.value = [];
}
/** 导入按钮操作 */
function handleImportTable() {
  const tableNames = tables.value.join(',');
  if (!tableNames) {
    proxy.$modal.msgError('请选择要导入的表');
    return;
  }
  importTable({ tables: tableNames }).then((res) => {
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
