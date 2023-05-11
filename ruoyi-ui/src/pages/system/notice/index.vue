<template>
  <t-card>
    <t-space direction="vertical">
      <t-form v-show="showSearch" ref="queryRef" :data="queryParams" layout="inline">
        <t-form-item label="公告标题" name="noticeTitle">
          <t-input
            v-model="queryParams.noticeTitle"
            placeholder="请输入公告标题"
            clearable
            style="width: 200px"
            @enter="handleQuery"
          />
        </t-form-item>
        <t-form-item label="操作人员" name="createByName">
          <t-input
            v-model="queryParams.createByName"
            placeholder="请输入操作人员"
            clearable
            style="width: 200px"
            @enter="handleQuery"
          />
        </t-form-item>
        <t-form-item label="类型" name="noticeType">
          <t-select v-model="queryParams.noticeType" placeholder="公告类型" clearable style="width: 200px">
            <t-option v-for="dict in sys_notice_type" :key="dict.value" :label="dict.label" :value="dict.value" />
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
        hover
        :loading="loading"
        row-key="noticeId"
        :data="noticeList"
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
              <t-button v-hasPermi="['system:notice:add']" theme="primary" @click="handleAdd">
                <template #icon> <add-icon /></template>
                新增
              </t-button>
              <t-button
                v-hasPermi="['system:notice:edit']"
                theme="default"
                variant="outline"
                :disabled="single"
                @click="handleUpdate"
              >
                <template #icon> <edit-icon /> </template>
                修改
              </t-button>
              <t-button
                v-hasPermi="['system:notice:remove']"
                theme="danger"
                variant="outline"
                :disabled="multiple"
                @click="handleDelete"
              >
                <template #icon> <delete-icon /> </template>
                删除
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
        <template #noticeType="{ row }">
          <dict-tag :options="sys_notice_type" :value="row.noticeType" />
        </template>
        <template #status="{ row }">
          <dict-tag :options="sys_notice_status" :value="row.status" />
        </template>
        <template #operation="{ row }">
          <t-space :size="8">
            <t-link v-hasPermi="['system:notice:edit']" theme="primary" hover="color" @click.stop="handleUpdate(row)">
              <edit-icon />修改
            </t-link>
            <t-link v-hasPermi="['system:notice:remove']" theme="danger" hover="color" @click.stop="handleDelete(row)">
              <delete-icon />删除
            </t-link>
          </t-space>
        </template>
      </t-table>
    </t-space>

    <!-- 添加或修改公告对话框 -->
    <t-dialog
      v-model:visible="open"
      :close-on-overlay-click="false"
      :header="title"
      width="780px"
      attach="body"
      @confirm="onConfirm"
    >
      <t-loading :loading="eLoading">
        <t-form
          ref="noticeRef"
          label-align="right"
          :data="form"
          :rules="rules"
          label-width="calc(5em + 24px)"
          @submit="submitForm"
        >
          <t-row :gutter="[0, 20]">
            <t-col :span="6">
              <t-form-item label="公告标题" name="noticeTitle">
                <t-input v-model="form.noticeTitle" placeholder="请输入公告标题" />
              </t-form-item>
            </t-col>
            <t-col :span="6">
              <t-form-item label="公告类型" name="noticeType">
                <t-select v-model="form.noticeType" placeholder="请选择">
                  <t-option
                    v-for="dict in sys_notice_type"
                    :key="dict.value"
                    :label="dict.label"
                    :value="dict.value"
                  ></t-option>
                </t-select>
              </t-form-item>
            </t-col>
            <t-col :span="12">
              <t-form-item label="状态">
                <t-radio-group v-model="form.status">
                  <t-radio v-for="dict in sys_notice_status" :key="dict.value" :value="dict.value">{{
                    dict.label
                  }}</t-radio>
                </t-radio-group>
              </t-form-item>
            </t-col>
            <t-col :span="12">
              <t-form-item label="内容">
                <t-textarea
                  v-model="form.noticeContent"
                  :autosize="{ minRows: 2, maxRows: 6 }"
                  placeholder="请输入内容"
                />
              </t-form-item>
            </t-col>
          </t-row>
        </t-form>
      </t-loading>
    </t-dialog>
  </t-card>
</template>
<script lang="ts">
export default {
  name: 'Notice',
};
</script>
<script lang="ts" setup>
import { AddIcon, DeleteIcon, EditIcon, RefreshIcon, SearchIcon, SettingIcon } from 'tdesign-icons-vue-next';
import { FormInstanceFunctions, FormRule, PrimaryTableCol } from 'tdesign-vue-next';
import { computed, getCurrentInstance, ref } from 'vue';

import { SysNoticeForm, SysNoticeQuery, SysNoticeVo } from '@/api/system/model/noticeModel';
import { addNotice, delNotice, getNotice, listNotice, updateNotice } from '@/api/system/notice';

const { proxy } = getCurrentInstance();
const { sys_notice_status, sys_notice_type } = proxy.useDict('sys_notice_status', 'sys_notice_type');

const noticeList = ref<SysNoticeVo[]>([]);
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
const noticeRef = ref<FormInstanceFunctions>(null);

const rules = ref<Record<string, Array<FormRule>>>({
  noticeTitle: [{ required: true, message: '公告标题不能为空', trigger: 'blur' }],
  noticeType: [{ required: true, message: '公告类型不能为空', trigger: 'change' }],
});
// 列显隐信息
const columns = ref<Array<PrimaryTableCol>>([
  { title: `选择列`, colKey: 'row-select', type: 'multiple', width: 50, align: 'center' },
  { title: `序号`, colKey: 'noticeId', align: 'center', width: 100, ellipsis: true },
  { title: `公告标题`, colKey: 'noticeTitle', align: 'center', ellipsis: true },
  { title: `公告类型`, colKey: 'noticeType', align: 'center', width: 100 },
  { title: `状态`, colKey: 'status', align: 'center', width: 100 },
  { title: `创建人`, colKey: 'createByName', align: 'center', width: 100 },
  { title: `更新人`, colKey: 'updateByName', align: 'center', width: 100 },
  { title: `创建时间`, colKey: 'createTime', align: 'center', width: 180 },
  { title: `操作`, colKey: 'operation', align: 'center' },
]);

const form = ref<SysNoticeVo & SysNoticeForm>({
  status: '0',
});
const queryParams = ref<SysNoticeQuery>({
  pageNum: 1,
  pageSize: 10,
  noticeTitle: undefined,
  createByName: undefined,
  status: undefined,
});

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

/** 查询公告列表 */
function getList() {
  loading.value = true;
  listNotice(queryParams.value).then((response) => {
    noticeList.value = response.rows;
    total.value = response.total;
    loading.value = false;
  });
}
/** 表单重置 */
function reset() {
  form.value = {
    status: '0',
  };
  proxy.resetForm('noticeRef');
}
/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}
/** 重置按钮操作 */
function resetQuery() {
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
  title.value = '添加公告';
}
/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  open.value = true;
  title.value = '修改公告';
  eLoading.value = true;
  const noticeId = row.noticeId || ids.value;
  getNotice(noticeId).then((response) => {
    form.value = response.data;
    eLoading.value = false;
  });
}
function onConfirm() {
  noticeRef.value.submit();
}
/** 提交按钮 */
function submitForm({ validateResult, firstError }) {
  if (validateResult === true) {
    const msgLoading = proxy.$modal.msgLoading('提交中...');
    if (form.value.noticeId) {
      updateNotice(form.value)
        .then((response) => {
          proxy.$modal.msgSuccess('修改成功');
          open.value = false;
          getList();
        })
        .finally(() => proxy.$modal.msgClose(msgLoading));
    } else {
      addNotice(form.value)
        .then((response) => {
          proxy.$modal.msgSuccess('新增成功');
          open.value = false;
          getList();
        })
        .finally(() => proxy.$modal.msgClose(msgLoading));
    }
  } else {
    proxy.$modal.msgError(firstError);
  }
}
/** 删除按钮操作 */
function handleDelete(row) {
  const noticeIds = row.noticeId || ids.value;
  proxy.$modal.confirm(`是否确认删除公告编号为"${noticeIds}"的数据项？`, () => {
    const msgLoading = proxy.$modal.msgLoading('正在删除中...');
    return delNotice(noticeIds)
      .then(() => {
        getList();
        proxy.$modal.msgSuccess('删除成功');
      })
      .finally(() => proxy.$modal.msgClose(msgLoading));
  });
}

getList();
</script>
