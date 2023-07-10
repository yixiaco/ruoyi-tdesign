<template>
  <t-card>
    <t-space direction="vertical">
      <t-form v-show="showSearch" ref="queryRef" :data="queryParams" layout="inline" label-width="68px">
        <t-form-item label="消息key" name="messageKey">
          <t-input v-model="queryParams.messageKey" placeholder="请输入消息key" clearable @enter="handleQuery" />
        </t-form-item>
        <t-form-item label="模板名称" name="messageTemplateName">
          <t-input
            v-model="queryParams.messageTemplateName"
            placeholder="请输入模板名称"
            clearable
            @enter="handleQuery"
          />
        </t-form-item>
        <t-form-item label="消息类型" name="messageType">
          <t-select v-model="queryParams.messageType" placeholder="请选择消息类型" clearable>
            <t-option v-for="dict in sys_message_type" :key="dict.value" :label="dict.label" :value="dict.value" />
          </t-select>
        </t-form-item>
        <t-form-item label="模板类型" name="templateMode">
          <t-select v-model="queryParams.templateMode" placeholder="请选择模板类型" clearable>
            <t-option
              v-for="dict in sys_message_template_mode"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            />
          </t-select>
        </t-form-item>
        <t-form-item label="发送账号" name="account">
          <t-input v-model="queryParams.account" placeholder="请输入发送账号" clearable @enter="handleQuery" />
        </t-form-item>
        <t-form-item label="模板ID" name="templateId">
          <t-input v-model="queryParams.templateId" placeholder="请输入模板ID" clearable @enter="handleQuery" />
        </t-form-item>
        <t-form-item label="平台标识" name="supplierType">
          <t-select v-model="queryParams.supplierType" placeholder="请选择平台标识" clearable>
            <t-option
              v-for="dict in sys_message_supplier_type"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            />
          </t-select>
        </t-form-item>
        <t-form-item label="是否成功" name="isSuccess">
          <t-select v-model="queryParams.isSuccess" placeholder="请选择是否成功" clearable>
            <t-option v-for="dict in sys_common_status" :key="dict.value" :label="dict.label" :value="dict.value" />
          </t-select>
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
        hover
        row-key="messageLogId"
        :data="messageLogList"
        :columns="columns"
        :selected-row-keys="ids"
        select-on-row-click
        :pagination="pagination"
        :column-controller="{
          hideTriggerButton: true,
        }"
        :sort="sort"
        show-sort-column-bg-color
        @sort-change="handleSortChange"
        @select-change="handleSelectionChange"
      >
        <template #topContent>
          <t-row>
            <t-col flex="auto">
              <t-button
                v-hasPermi="['system:messageLog:remove']"
                theme="danger"
                variant="outline"
                :disabled="multiple"
                @click="handleDelete()"
              >
                <template #icon> <delete-icon /> </template>
                删除
              </t-button>
              <t-button v-hasPermi="['system:messageLog:remove']" theme="danger" variant="outline" @click="handleClear">
                <template #icon> <delete-icon /> </template>
                清空记录
              </t-button>
              <t-button
                v-hasPermi="['system:messageLog:export']"
                theme="default"
                variant="outline"
                @click="handleExport"
              >
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
        <template #messageType="{ row }">
          <dict-tag :options="sys_message_type" :value="row.messageType" />
        </template>
        <template #templateMode="{ row }">
          <dict-tag :options="sys_message_template_mode" :value="row.templateMode" />
        </template>
        <template #supplierType="{ row }">
          <dict-tag :options="sys_message_supplier_type" :value="row.supplierType" />
        </template>
        <template #isSuccess="{ row }">
          <dict-tag :options="sys_common_status" :value="row.isSuccess" />
        </template>
        <template #operation="{ row }">
          <t-space :size="8" break-line>
            <t-link
              v-hasPermi="['system:messageLog:query']"
              theme="primary"
              hover="color"
              @click.stop="handleDetail(row)"
            >
              <browse-icon />详情
            </t-link>
            <t-link
              v-hasPermi="['system:messageLog:remove']"
              theme="danger"
              hover="color"
              @click.stop="handleDelete(row)"
            >
              <delete-icon />删除
            </t-link>
          </t-space>
        </template>
      </t-table>
    </t-space>

    <!-- 消息发送记录详情 -->
    <t-dialog v-model:visible="openView" header="消息发送记录详情" top="3%" width="700px" attach="body" :footer="false">
      <t-loading :loading="openViewLoading">
        <t-form label-align="right" colon label-width="calc(7em + 24px)">
          <t-row :gutter="[0, 20]">
            <t-col :span="6">
              <t-form-item label="消息发送记录id">{{ form.messageLogId }}</t-form-item>
            </t-col>
            <t-col :span="6">
              <t-form-item label="消息模板id">{{ form.messageTemplateId }}</t-form-item>
            </t-col>
            <t-col :span="6">
              <t-form-item label="消息key">{{ form.messageKey }}</t-form-item>
            </t-col>
            <t-col :span="6">
              <t-form-item label="模板名称">{{ form.messageTemplateName }}</t-form-item>
            </t-col>
            <t-col :span="6">
              <t-form-item label="消息类型">
                <dict-tag :options="sys_message_type" :value="form.messageType" />
              </t-form-item>
            </t-col>
            <t-col :span="6">
              <t-form-item label="模板类型">
                <dict-tag :options="sys_message_template_mode" :value="form.templateMode" />
              </t-form-item>
            </t-col>
            <t-col :span="6">
              <t-form-item label="发送账号">{{ form.account }}</t-form-item>
            </t-col>
            <t-col :span="6">
              <t-form-item label="标题">{{ form.title }}</t-form-item>
            </t-col>
            <t-col :span="6">
              <t-form-item label="模板id">{{ form.templateId }}</t-form-item>
            </t-col>
            <t-col :span="12">
              <t-form-item label="发送内容"><div v-html="form.content"></div></t-form-item>
            </t-col>
            <t-col :span="6">
              <t-form-item label="消息配置标题">{{ form.messageConfigTitle }}</t-form-item>
            </t-col>
            <t-col :span="6">
              <t-form-item label="平台标识">
                <dict-tag :options="sys_message_supplier_type" :value="form.supplierType" />
              </t-form-item>
            </t-col>
            <t-col :span="6">
              <t-form-item label="是否成功">
                <dict-tag :options="sys_common_status" :value="form.isSuccess" />
              </t-form-item>
            </t-col>
            <t-col :span="6">
              <t-form-item label="错误码">{{ form.errorCode }}</t-form-item>
            </t-col>
            <t-col :span="12">
              <t-form-item label="错误消息">{{ form.errorMessage }}</t-form-item>
            </t-col>
            <t-col :span="12">
              <t-form-item label="回执消息id">{{ form.bizId }}</t-form-item>
            </t-col>
            <t-col :span="12">
              <t-form-item label="返回消息">{{ form.message }}</t-form-item>
            </t-col>
            <t-col :span="6">
              <t-form-item label="记录时间">{{ parseTime(form.logTime) }}</t-form-item>
            </t-col>
          </t-row>
        </t-form>
      </t-loading>
    </t-dialog>
  </t-card>
</template>
<script lang="ts">
export default {
  name: 'MessageLog',
};
</script>
<script lang="ts" setup>
import { BrowseIcon, DeleteIcon, DownloadIcon, RefreshIcon, SearchIcon, SettingIcon } from 'tdesign-icons-vue-next';
import { PageInfo, PrimaryTableCol, TableSort } from 'tdesign-vue-next';
import { computed, getCurrentInstance, ref } from 'vue';

import { clearMessageLog, delMessageLog, getMessageLog, listMessageLog } from '@/api/system/messageLog';
import { SysMessageLogQuery, SysMessageLogVo } from '@/api/system/model/messageLogModel';

const { proxy } = getCurrentInstance();
const { sys_message_template_mode, sys_common_status, sys_message_type, sys_message_supplier_type } = proxy.useDict(
  'sys_message_template_mode',
  'sys_common_status',
  'sys_message_type',
  'sys_message_supplier_type',
);

const messageLogList = ref<SysMessageLogVo[]>([]);
const openView = ref(false);
const openViewLoading = ref(false);
const loading = ref(false);
const columnControllerVisible = ref(false);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const sort = ref<TableSort>(null);

// 列显隐信息
const columns = ref<Array<PrimaryTableCol>>([
  { title: `选择列`, colKey: 'row-select', type: 'multiple', width: 50, align: 'center' },
  { title: `消息key`, colKey: 'messageKey', align: 'center' },
  { title: `模板名称`, colKey: 'messageTemplateName', align: 'center' },
  { title: `消息类型`, colKey: 'messageType', align: 'center' },
  { title: `模板类型`, colKey: 'templateMode', align: 'center' },
  { title: `发送账号`, colKey: 'account', align: 'center' },
  { title: `发送内容`, colKey: 'content', align: 'center', ellipsis: true },
  { title: `平台标识`, colKey: 'supplierType', align: 'center' },
  { title: `是否成功`, colKey: 'isSuccess', align: 'center' },
  { title: `返回消息`, colKey: 'message', align: 'center' },
  { title: `记录时间`, colKey: 'logTime', align: 'center', width: 180, sorter: true },
  { title: `操作`, colKey: 'operation', align: 'center', width: 180 },
]);
// 提交表单对象
const form = ref<SysMessageLogVo>({});
// 查询对象
const queryParams = ref<SysMessageLogQuery>({
  pageNum: 1,
  pageSize: 10,
  messageKey: undefined,
  messageTemplateName: undefined,
  messageType: undefined,
  templateMode: undefined,
  account: undefined,
  templateId: undefined,
  supplierType: undefined,
  isSuccess: undefined,
});

// 分页
const pagination = computed(() => {
  return {
    current: queryParams.value.pageNum,
    pageSize: queryParams.value.pageSize,
    total: total.value,
    showJumper: true,
    onChange: (pageInfo: PageInfo) => {
      queryParams.value.pageNum = pageInfo.current;
      queryParams.value.pageSize = pageInfo.pageSize;
      getList();
    },
  };
});

/** 查询消息发送记录列表 */
function getList() {
  loading.value = true;
  listMessageLog(queryParams.value)
    .then((response) => {
      messageLogList.value = response.rows;
      total.value = response.total;
    })
    .finally(() => (loading.value = false));
}

// 表单重置
function reset() {
  form.value = {};
  proxy.resetForm('messageLogRef');
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}

/** 重置按钮操作 */
function resetQuery() {
  proxy.resetForm('queryRef');
  queryParams.value.pageNum = 1;
  handleSortChange(null);
}

/** 排序触发事件 */
function handleSortChange(value?: TableSort) {
  sort.value = value;
  if (Array.isArray(value)) {
    queryParams.value.orderByColumn = value.map((item) => item.sortBy).join(',');
    queryParams.value.isAsc = value.map((item) => (item.descending ? 'descending' : 'ascending')).join(',');
  } else {
    queryParams.value.orderByColumn = value?.sortBy;
    queryParams.value.isAsc = value?.descending ? 'descending' : 'ascending';
  }
  getList();
}

/** 多选框选中数据 */
function handleSelectionChange(selection: Array<string | number>) {
  ids.value = selection;
  single.value = selection.length !== 1;
  multiple.value = !selection.length;
}

/** 详情按钮操作 */
function handleDetail(row: SysMessageLogVo) {
  reset();
  openView.value = true;
  openViewLoading.value = true;
  const messageLogId = row.messageLogId || ids.value.at(0);
  getMessageLog(messageLogId).then((response) => {
    form.value = response.data;
    openViewLoading.value = false;
  });
}

/** 删除按钮操作 */
function handleDelete(row?: SysMessageLogVo) {
  const messageLogIds = row?.messageLogId || ids.value;
  proxy.$modal.confirm(`是否确认删除消息发送记录编号为${messageLogIds}的数据项？`, () => {
    const msgLoading = proxy.$modal.msgLoading('正在删除中...');
    return delMessageLog(messageLogIds)
      .then(() => {
        getList();
        proxy.$modal.msgSuccess('删除成功');
      })
      .finally(() => {
        proxy.$modal.msgClose(msgLoading);
      });
  });
}

/** 清空记录按钮操作 */
function handleClear(row: SysMessageLogVo) {
  proxy.$modal.confirm(`是否确认删除所有消息发送记录？`, () => {
    const msgLoading = proxy.$modal.msgLoading('正在删除中...');
    return clearMessageLog()
      .then(() => {
        getList();
        proxy.$modal.msgSuccess('删除成功');
      })
      .finally(() => {
        proxy.$modal.msgClose(msgLoading);
      });
  });
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download(
    'system/messageLog/export',
    {
      ...queryParams.value,
    },
    `messageLog_${new Date().getTime()}.xlsx`,
  );
}

getList();
</script>
