<template>
  <t-card>
    <t-space direction="vertical" style="width: 100%">
      <t-form
        v-show="showSearch"
        ref="queryRef"
        :data="queryParams"
        layout="inline"
        reset-type="initial"
        label-width="calc(4em + 12px)"
      >
        <t-form-item label="表单名称" name="formName">
          <t-input v-model="queryParams.formName" placeholder="请输入表单名称" clearable @enter="handleQuery" />
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
        row-key="id"
        :data="formManageList"
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
              <t-button v-hasPermi="['workflow:formManage:add']" theme="primary" @click="handleAdd">
                <template #icon> <add-icon /></template>
                新增
              </t-button>
              <t-button
                v-hasPermi="['workflow:formManage:edit']"
                theme="default"
                variant="outline"
                :disabled="single"
                @click="handleUpdate()"
              >
                <template #icon> <edit-icon /> </template>
                修改
              </t-button>
              <t-button
                v-hasPermi="['workflow:formManage:remove']"
                theme="danger"
                variant="outline"
                :disabled="multiple"
                @click="handleDelete()"
              >
                <template #icon> <delete-icon /> </template>
                删除
              </t-button>
              <t-button
                v-hasPermi="['workflow:formManage:export']"
                theme="default"
                variant="outline"
                @click="handleExport"
              >
                <template #icon> <download-icon /> </template>
                导出
              </t-button>
              <span class="selected-count">已选 {{ ids.length }} 项</span>
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
        <template #formType="{ row }">
          <dict-tag :options="wf_form_type" :value="row.formType" />
        </template>
        <template #operation="{ row }">
          <t-space :size="8" break-line>
            <t-link
              v-hasPermi="['workflow:formManage:query']"
              size="small"
              theme="primary"
              hover="color"
              @click.stop="handleDetail(row)"
            >
              <template #prefix-icon><browse-icon /></template>详情
            </t-link>
            <t-link
              v-hasPermi="['workflow:formManage:edit']"
              size="small"
              theme="primary"
              hover="color"
              @click.stop="handleUpdate(row)"
            >
              <template #prefix-icon><edit-icon /></template>修改
            </t-link>
            <t-link
              v-hasPermi="['workflow:formManage:remove']"
              size="small"
              theme="danger"
              hover="color"
              @click.stop="handleDelete(row)"
            >
              <template #prefix-icon><delete-icon /></template>删除
            </t-link>
          </t-space>
        </template>
      </t-table>
    </t-space>

    <!-- 添加或修改单管理对话框 -->
    <t-dialog
      v-model:visible="open"
      :header="title"
      destroy-on-close
      :close-on-overlay-click="false"
      width="min(500px, 100%)"
      attach="body"
      :confirm-btn="{
        loading: buttonLoading,
      }"
      @confirm="formManageRef.submit()"
    >
      <t-loading :loading="buttonLoading" size="small">
        <t-form
          ref="formManageRef"
          label-align="right"
          :data="form"
          :rules="rules"
          label-width="calc(7em + 41px)"
          scroll-to-first-error="smooth"
          @submit="submitForm"
        >
          <t-form-item label="表单名称" name="formName">
            <t-input v-model="form.formName" placeholder="请输入表单名称" clearable />
          </t-form-item>
          <t-form-item label="表单类型" name="formType">
            <t-select v-model="form.formType" placeholder="请选择表单类型" clearable>
              <t-option v-for="dict in wf_form_type" :key="dict.value" :label="dict.label" :value="dict.value" />
            </t-select>
          </t-form-item>
          <t-form-item label="路由地址/表单ID" name="router">
            <t-input v-model="form.router" placeholder="请输入路由地址/表单ID" clearable />
          </t-form-item>
          <t-form-item label="备注" name="remark">
            <t-textarea v-model="form.remark" placeholder="请输入备注" />
          </t-form-item>
        </t-form>
      </t-loading>
    </t-dialog>

    <!-- 单管理详情 -->
    <t-dialog
      v-model:visible="openView"
      header="单管理详情"
      placement="center"
      width="min(700px, 100%)"
      attach="body"
      :footer="false"
    >
      <my-descriptions :loading="openViewLoading">
        <t-descriptions-item label="表单名称">{{ form.formName }}</t-descriptions-item>
        <t-descriptions-item label="表单类型">
          <dict-tag :options="wf_form_type" :value="form.formType" />
        </t-descriptions-item>
        <t-descriptions-item label="路由地址/表单ID">{{ form.router }}</t-descriptions-item>
        <t-descriptions-item label="备注" :span="2">{{ form.remark }}</t-descriptions-item>
        <t-descriptions-item label="创建时间">{{ parseTime(form.createTime) }}</t-descriptions-item>
        <t-descriptions-item label="更新时间">{{ parseTime(form.updateTime) }}</t-descriptions-item>
      </my-descriptions>
    </t-dialog>
  </t-card>
</template>
<script lang="ts" setup>
defineOptions({
  name: 'FormManage',
});
import {
  AddIcon,
  BrowseIcon,
  DeleteIcon,
  DownloadIcon,
  EditIcon,
  RefreshIcon,
  SearchIcon,
  SettingIcon,
} from 'tdesign-icons-vue-next';
import type { FormInstanceFunctions, FormRule, PageInfo, PrimaryTableCol, SubmitContext } from 'tdesign-vue-next';
import { computed, getCurrentInstance, ref } from 'vue';

import {
  addFormManage,
  delFormManage,
  getFormManage,
  listFormManage,
  updateFormManage,
} from '@/api/workflow/formManage';
import type { WfFormManageForm, WfFormManageQuery, WfFormManageVo } from '@/api/workflow/model/formManageModel';
import { ArrayOps } from '@/utils/array';

const { proxy } = getCurrentInstance();
const { wf_form_type } = proxy.useDict('wf_form_type');

const openView = ref(false);
const openViewLoading = ref(false);
const formManageRef = ref<FormInstanceFunctions>();
const open = ref(false);
const buttonLoading = ref(false);
const title = ref('');
const formManageList = ref<WfFormManageVo[]>([]);
const loading = ref(false);
const columnControllerVisible = ref(false);
const showSearch = ref(true);
const total = ref(0);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);

// 校验规则
const rules = ref<Record<string, Array<FormRule>>>({
  formName: [
    { required: true, message: '表单名称不能为空' },
    { max: 255, message: '表单名称不能超过255个字符' },
  ],
  formType: [
    { required: true, message: '表单类型不能为空' },
    { max: 255, message: '表单类型不能超过255个字符' },
  ],
  router: [
    { required: true, message: '路由地址/表单ID不能为空' },
    { max: 255, message: '路由地址/表单ID不能超过255个字符' },
  ],
  remark: [{ max: 500, message: '备注不能超过500个字符' }],
});

// 列显隐信息
const columns = ref<Array<PrimaryTableCol>>([
  { title: `选择列`, colKey: 'row-select', type: 'multiple', width: 50, align: 'center' },
  { title: `表单名称`, colKey: 'formName', align: 'center' },
  { title: `表单类型`, colKey: 'formType', align: 'center' },
  { title: `路由地址/表单ID`, colKey: 'router', align: 'center' },
  { title: `备注`, colKey: 'remark', align: 'center', ellipsis: true },
  { title: `创建时间`, colKey: 'createTime', align: 'center', minWidth: 112, width: 180 },
  { title: `更新时间`, colKey: 'updateTime', align: 'center', minWidth: 112, width: 180 },
  { title: `操作`, colKey: 'operation', align: 'center', width: 180 },
]);
// 提交表单对象
const form = ref<WfFormManageVo & WfFormManageForm>({});
// 查询对象
const queryParams = ref<WfFormManageQuery>({
  pageNum: 1,
  pageSize: 10,
  formName: undefined,
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

/** 查询单管理列表 */
function getList() {
  loading.value = true;
  listFormManage(queryParams.value)
    .then((response) => {
      formManageList.value = response.rows;
      total.value = response.total;
    })
    .finally(() => (loading.value = false));
}

// 表单重置
function reset() {
  form.value = {};
  proxy.resetForm('formManageRef');
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
function handleSelectionChange(selection: Array<string | number>) {
  ids.value = selection;
  single.value = selection.length !== 1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */
function handleAdd() {
  reset();
  open.value = true;
  title.value = '添加单管理';
}

/** 详情按钮操作 */
function handleDetail(row: WfFormManageVo) {
  reset();
  openView.value = true;
  openViewLoading.value = true;
  const id = row.id;
  getFormManage(id).then((response) => {
    form.value = response.data;
    openViewLoading.value = false;
  });
}

/** 修改按钮操作 */
function handleUpdate(row?: WfFormManageVo) {
  buttonLoading.value = true;
  reset();
  open.value = true;
  title.value = '修改单管理';
  const id = row?.id || ids.value.at(0);
  getFormManage(id).then((response) => {
    buttonLoading.value = false;
    form.value = response.data;
  });
}

/** 提交表单 */
function submitForm({ validateResult, firstError }: SubmitContext) {
  if (validateResult === true) {
    buttonLoading.value = true;
    const msgLoading = proxy.$modal.msgLoading('提交中...');
    if (form.value.id) {
      updateFormManage(form.value)
        .then(() => {
          proxy.$modal.msgSuccess('修改成功');
          open.value = false;
          getList();
        })
        .finally(() => {
          buttonLoading.value = false;
          proxy.$modal.msgClose(msgLoading);
        });
    } else {
      addFormManage(form.value)
        .then(() => {
          proxy.$modal.msgSuccess('新增成功');
          open.value = false;
          getList();
        })
        .finally(() => {
          buttonLoading.value = false;
          proxy.$modal.msgClose(msgLoading);
        });
    }
  } else {
    proxy.$modal.msgError(firstError);
  }
}

/** 删除按钮操作 */
function handleDelete(row?: WfFormManageVo) {
  const $ids = row?.id || ids.value;
  proxy.$modal.confirm(`是否确认删除单管理编号为${$ids}的数据项？`, () => {
    const msgLoading = proxy.$modal.msgLoading('正在删除中...');
    return delFormManage($ids)
      .then(() => {
        ids.value = ArrayOps.fastDeleteElement(ids.value, $ids);
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
    'workflow/formManage/export',
    {
      ...queryParams.value,
    },
    `formManage_${new Date().getTime()}.xlsx`,
  );
}

getList();
</script>
