<template>
  <div class="app-container">
    <t-space direction="vertical">
      <t-form v-show="showSearch" ref="queryRef" :data="queryParams" layout="inline" label-width="68px">
        <t-form-item label="参数名称" name="configName">
          <t-input
            v-model="queryParams.configName"
            placeholder="请输入参数名称"
            clearable
            style="width: 240px"
            @keyup.enter="handleQuery"
          />
        </t-form-item>
        <t-form-item label="参数键名" name="configKey">
          <t-input
            v-model="queryParams.configKey"
            placeholder="请输入参数键名"
            clearable
            style="width: 240px"
            @keyup.enter="handleQuery"
          />
        </t-form-item>
        <t-form-item label="系统内置" name="configType">
          <t-select v-model="queryParams.configType" placeholder="系统内置" clearable>
            <t-option v-for="dict in sys_yes_no" :key="dict.value" :label="dict.label" :value="dict.value" />
          </t-select>
        </t-form-item>
        <t-form-item label="创建时间" style="width: 308px">
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
        row-key="configId"
        :data="configList"
        :columns="columns"
        :selected-row-keys="ids"
        select-on-row-click
        :pagination="pagination"
        :column-controller="{
          hideTriggerButton: true,
        }"
        @select-change="handleSelectionChange"
      >
        <template #topContent>
          <t-row>
            <t-col flex="auto">
              <t-button v-hasPermi="['system:config:add']" theme="primary" @click="handleAdd">
                <template #icon> <add-icon /></template>
                新增
              </t-button>
              <t-button
                v-hasPermi="['system:config:edit']"
                theme="default"
                variant="outline"
                :disabled="single"
                @click="handleUpdate"
              >
                <template #icon> <edit-icon /> </template>
                修改
              </t-button>
              <t-button
                v-hasPermi="['system:config:remove']"
                theme="danger"
                variant="outline"
                :disabled="multiple"
                @click="handleDelete"
              >
                <template #icon> <delete-icon /> </template>
                删除
              </t-button>
              <t-button v-hasPermi="['system:config:export']" theme="default" variant="outline" @click="handleExport">
                <template #icon> <download-icon /> </template>
                导出
              </t-button>
              <t-button
                v-hasPermi="['system:config:remove']"
                theme="danger"
                variant="outline"
                @click="handleRefreshCache"
              >
                <template #icon> <refresh-icon /> </template>
                刷新缓存
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
        <template #configType="{ row }">
          <dict-tag :options="sys_yes_no" :value="row.configType" />
        </template>
        <template #operation="{ row }">
          <t-space :size="8">
            <t-link v-hasPermi="['system:config:edit']" theme="primary" hover="color" @click.stop="handleUpdate(row)">
              <edit-icon />修改
            </t-link>
            <t-link v-hasPermi="['system:config:remove']" theme="danger" hover="color" @click.stop="handleDelete(row)">
              <delete-icon />删除
            </t-link>
          </t-space>
        </template>
      </t-table>
    </t-space>

    <!-- 添加或修改参数配置对话框 -->
    <t-dialog
      v-model:visible="open"
      :close-on-overlay-click="false"
      :header="title"
      width="500px"
      attach="body"
      @confirm="onConfirm"
    >
      <t-loading :loading="eLoading">
        <t-form ref="configRef" label-align="right" :data="form" :rules="rules" label-width="80px" @submit="submitForm">
          <t-form-item label="参数名称" name="configName">
            <t-input v-model="form.configName" placeholder="请输入参数名称" />
          </t-form-item>
          <t-form-item label="参数键名" name="configKey">
            <t-input v-model="form.configKey" placeholder="请输入参数键名" />
          </t-form-item>
          <t-form-item label="参数键值" name="configValue">
            <t-input v-model="form.configValue" placeholder="请输入参数键值" />
          </t-form-item>
          <t-form-item label="系统内置" name="configType">
            <t-radio-group v-model="form.configType">
              <t-radio v-for="dict in sys_yes_no" :key="dict.value" :value="dict.value">{{ dict.label }}</t-radio>
            </t-radio-group>
          </t-form-item>
          <t-form-item label="备注" name="remark">
            <t-textarea v-model="form.remark" placeholder="请输入内容" />
          </t-form-item>
        </t-form>
      </t-loading>
    </t-dialog>
  </div>
</template>
<script lang="ts">
export default {
  name: 'Config',
};
</script>
<script lang="ts" setup>
import { computed, getCurrentInstance, reactive, ref, toRefs } from 'vue';
import {
  AddIcon,
  DeleteIcon,
  DownloadIcon,
  EditIcon,
  RefreshIcon,
  SearchIcon,
  SettingIcon,
} from 'tdesign-icons-vue-next';
import { listConfig, getConfig, delConfig, addConfig, updateConfig, refreshCache } from '@/api/system/config';

const { proxy } = getCurrentInstance();
const { sys_yes_no } = proxy.useDict('sys_yes_no');

const configList = ref([]);
const open = ref(false);
const loading = ref(false);
const eLoading = ref(false);
const columnControllerVisible = ref(false);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref('');
const dateRange = ref([]);
const configRef = ref(null);

const formInitValue = {
  configId: undefined,
  configName: undefined,
  configKey: undefined,
  configValue: undefined,
  configType: 'Y',
  remark: undefined,
};
const data = reactive({
  form: { ...formInitValue },
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    configName: undefined,
    configKey: undefined,
    configType: undefined,
  },
  rules: {
    configName: [{ required: true, message: '参数名称不能为空', trigger: 'blur' }],
    configKey: [{ required: true, message: '参数键名不能为空', trigger: 'blur' }],
    configValue: [{ required: true, message: '参数键值不能为空', trigger: 'blur' }],
  },
});

// 列显隐信息
const columns = ref([
  { title: `选择列`, colKey: 'row-select', type: 'multiple', width: 50, align: 'center' },
  { title: `参数主键`, colKey: 'configId', align: 'center' },
  { title: `参数名称`, colKey: 'configName', align: 'center', ellipsis: true },
  { title: `参数键名`, colKey: 'configKey', align: 'center', ellipsis: true },
  { title: `参数键值`, colKey: 'configValue', align: 'center' },
  { title: `系统内置`, colKey: 'configType', align: 'center' },
  { title: `备注`, colKey: 'remark', align: 'center', ellipsis: true },
  { title: `创建时间`, colKey: 'createTime', align: 'center', width: 180 },
  { title: `操作`, colKey: 'operation', align: 'center', width: 150 },
]);

// 分页
const pagination = computed(() => {
  return {
    current: queryParams.value.pageNum,
    pageSize: queryParams.value.pageSize,
    total: total.value,
    showJumper: true,
    onChange: (pageInfo) => {
      data.queryParams.pageNum = pageInfo.current;
      data.queryParams.pageSize = pageInfo.pageSize;
      getList();
    },
  };
});

const { queryParams, form, rules } = toRefs(data);

/** 查询参数列表 */
function getList() {
  loading.value = true;
  listConfig(proxy.addDateRange(queryParams.value, dateRange.value)).then((response) => {
    configList.value = response.rows;
    total.value = response.total;
    loading.value = false;
  });
}
/** 表单重置 */
function reset() {
  form.value = { ...formInitValue };
  proxy.resetForm('configRef');
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
  handleQuery();
}
/** 多选框选中数据 */
function handleSelectionChange(selection) {
  ids.value = selection;
  single.value = selection.length !== 1;
  multiple.value = !selection.length;
}
/** 新增按钮操作 */
function handleAdd() {
  reset();
  open.value = true;
  title.value = '添加参数';
}
/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  open.value = true;
  title.value = '修改参数';
  const configId = row.configId || ids.value;
  eLoading.value = true;
  getConfig(configId).then((response) => {
    form.value = response.data;
    eLoading.value = false;
  });
}
function onConfirm() {
  configRef.value.submit();
}
/** 提交按钮 */
function submitForm({ validateResult }) {
  if (validateResult) {
    if (form.value.configId) {
      updateConfig(form.value).then(() => {
        proxy.$modal.msgSuccess('修改成功');
        open.value = false;
        getList();
      });
    } else {
      addConfig(form.value).then(() => {
        proxy.$modal.msgSuccess('新增成功');
        open.value = false;
        getList();
      });
    }
  }
}
/** 删除按钮操作 */
function handleDelete(row) {
  const configIds = row.configId || ids.value;
  proxy.$modal.confirm(`是否确认删除参数编号为"${configIds}"的数据项？`, () => {
    return delConfig(configIds).then(() => {
      getList();
      proxy.$modal.msgSuccess('删除成功');
    });
  });
}
/** 导出按钮操作 */
function handleExport() {
  proxy.download(
    'system/config/export',
    {
      ...queryParams.value,
    },
    `config_${new Date().getTime()}.xlsx`,
  );
}
/** 刷新缓存按钮操作 */
function handleRefreshCache() {
  proxy.$modal.confirm(`是否确认刷新缓存？`, () => {
    return refreshCache().then(() => {
      proxy.$modal.msgSuccess('刷新缓存成功');
    });
  });
}

getList();
</script>
