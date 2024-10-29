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
        <t-form-item label="请假天数">
          <t-range-input v-model="rangeLeaveDays" placeholder="请输入请假天数" clearable @enter="handleQuery" />
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
        :data="leaveList"
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
              <t-button v-hasPermi="['demo:leave:add']" theme="primary" @click="handleAdd">
                <template #icon> <add-icon /></template>
                新增
              </t-button>
              <t-button
                v-hasPermi="['demo:leave:edit']"
                theme="default"
                variant="outline"
                :disabled="single"
                @click="handleUpdate()"
              >
                <template #icon> <edit-icon /> </template>
                修改
              </t-button>
              <t-button
                v-hasPermi="['demo:leave:remove']"
                theme="danger"
                variant="outline"
                :disabled="multiple"
                @click="handleDelete()"
              >
                <template #icon> <delete-icon /> </template>
                删除
              </t-button>
              <t-button v-hasPermi="['demo:leave:export']" theme="default" variant="outline" @click="handleExport">
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
        <template #leaveType="{ row }">
          <t-tag variant="light">{{ options.find((e) => e.value === row.leaveType)?.label }}</t-tag>
        </template>
        <template #startDate="{ row }">{{ parseTime(row.startDate, '{y}-{m}-{d}') }}</template>
        <template #endDate="{ row }">{{ parseTime(row.endDate, '{y}-{m}-{d}') }}</template>
        <template #businessStatusName="{ row }">
          <t-tag theme="success" variant="light">{{ row.processInstanceVo.businessStatusName }}</t-tag>
        </template>
        <template #operation="{ row }">
          <t-space :size="8" break-line>
            <t-link
              v-hasPermi="['demo:leave:query']"
              size="small"
              theme="primary"
              hover="color"
              @click.stop="handleDetail(row)"
            >
              <template #prefix-icon><browse-icon /></template>详情
            </t-link>
            <t-link
              v-if="
                row.processInstanceVo.businessStatus === 'draft' ||
                row.processInstanceVo.businessStatus === 'cancel' ||
                row.processInstanceVo.businessStatus === 'back'
              "
              v-hasPermi="['demo:leave:edit']"
              size="small"
              theme="primary"
              hover="color"
              @click.stop="handleUpdate(row)"
            >
              <template #prefix-icon><edit-icon /></template>修改
            </t-link>
            <t-link
              v-if="
                row.processInstanceVo.businessStatus === 'draft' ||
                row.processInstanceVo.businessStatus === 'cancel' ||
                row.processInstanceVo.businessStatus === 'back'
              "
              v-hasPermi="['demo:leave:remove']"
              theme="danger"
              hover="color"
              size="small"
              @click.stop="handleDelete(row)"
            >
              <template #prefix-icon><delete-icon /></template>删除
            </t-link>
            <t-link
              v-if="row.processInstanceVo.businessStatus === 'waiting'"
              theme="warning"
              hover="color"
              size="small"
              @click.stop="handleCancelProcessApply(row.processInstanceVo.id)"
            >
              <template #prefix-icon><rollback-icon /></template>撤销
            </t-link>
            <t-link
              v-if="
                row.processInstanceVo.businessStatus === 'waiting' ||
                row.processInstanceVo.businessStatus === 'finish' ||
                row.processInstanceVo.businessStatus === 'termination' ||
                row.processInstanceVo.businessStatus === 'invalid'
              "
              theme="primary"
              hover="color"
              size="small"
              @click.stop="handleApprovalRecord(row.processInstanceVo.id)"
            >
              <template #prefix-icon><root-list-icon /></template>审批记录
            </t-link>
          </t-space>
        </template>
      </t-table>
    </t-space>

    <!-- 添加或修改请假申请对话框 -->
    <t-dialog
      v-model:visible="open"
      :header="title"
      destroy-on-close
      :close-on-overlay-click="false"
      width="min(500px, 100%)"
      attach="body"
    >
      <t-loading :loading="buttonLoading" size="small">
        <t-form
          ref="leaveRef"
          label-align="right"
          :data="form"
          :rules="rules"
          label-width="calc(4em + 41px)"
          scroll-to-first-error="smooth"
          @submit="submitForm"
        >
          <t-form-item label="请假类型" name="leaveType">
            <t-select v-model="form.leaveType" placeholder="请选择请假类型" clearable>
              <t-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value" />
            </t-select>
          </t-form-item>
          <t-form-item label="请假时间">
            <t-date-range-picker
              v-model="leaveTime"
              allow-input
              clearable
              separator="至"
              :placeholder="['开始日期', '结束日期']"
              @change="changeLeaveTime()"
            />
          </t-form-item>
          <t-form-item label="请假天数" name="leaveDays">
            <t-input-number v-model="form.leaveDays" theme="normal" disabled placeholder="请输入" />
          </t-form-item>
          <t-form-item label="请假原因" name="remark">
            <t-textarea v-model="form.remark" placeholder="请输入请假原因" />
          </t-form-item>
        </t-form>
      </t-loading>
      <template #footer>
        <div class="dialog-footer">
          <t-button :loading="buttonLoading" theme="default" @click="handleOnSubmit('draft')">暂 存</t-button>
          <t-button :loading="buttonLoading" theme="primary" @click="handleOnSubmit('submit')">提 交</t-button>
          <t-button variant="outline" @click="open = false">取 消</t-button>
        </div>
      </template>
    </t-dialog>

    <!-- 请假申请详情 -->
    <t-dialog
      v-model:visible="openView"
      header="请假申请详情"
      placement="center"
      width="min(700px, 100%)"
      attach="body"
      :footer="false"
    >
      <my-descriptions :loading="openViewLoading">
        <t-descriptions-item label="请假类型">{{ form.leaveType }}</t-descriptions-item>
        <t-descriptions-item label="开始时间">{{ parseTime(form.startDate) }}</t-descriptions-item>
        <t-descriptions-item label="结束时间">{{ parseTime(form.endDate) }}</t-descriptions-item>
        <t-descriptions-item label="请假天数">{{ form.leaveDays }}</t-descriptions-item>
        <t-descriptions-item label="请假原因" :span="2">{{ form.remark }}</t-descriptions-item>
        <t-descriptions-item label="创建时间">{{ parseTime(form.createTime) }}</t-descriptions-item>
        <t-descriptions-item label="更新时间">{{ parseTime(form.updateTime) }}</t-descriptions-item>
      </my-descriptions>
    </t-dialog>
    <!-- 提交组件 -->
    <submit-verify ref="submitVerifyRef" :task-variables="taskVariables" @submit-callback="submitCallback" />
    <!-- 审批记录 -->
    <approval-record ref="approvalRecordRef" />
  </t-card>
</template>
<script lang="ts" setup>
defineOptions({
  name: 'Leave',
});

import {
  AddIcon,
  BrowseIcon,
  DeleteIcon,
  DownloadIcon,
  EditIcon,
  RefreshIcon,
  RollbackIcon,
  RootListIcon,
  SearchIcon,
  SettingIcon,
} from 'tdesign-icons-vue-next';
import type { FormInstanceFunctions, FormRule, PageInfo, PrimaryTableCol, SubmitContext } from 'tdesign-vue-next';
import { computed, getCurrentInstance, ref } from 'vue';

import { addLeave, delLeave, getLeave, listLeave, updateLeave } from '@/api/workflow/leave';
import type { TestLeaveForm, TestLeaveQuery, TestLeaveVo } from '@/api/workflow/model/leaveModel';
import { cancelProcessApply } from '@/api/workflow/processInstance';
import { startWorkFlow } from '@/api/workflow/task';
import type { StartProcessBo } from '@/api/workflow/task/types';
import type ApprovalRecord from '@/components/Process/approvalRecord.vue';
import type SubmitVerify from '@/components/Process/submitVerify.vue';
import { ArrayOps } from '@/utils/array';

const { proxy } = getCurrentInstance();

const openView = ref(false);
const openViewLoading = ref(false);
const leaveRef = ref<FormInstanceFunctions>();
const open = ref(false);
const buttonLoading = ref(false);
const title = ref('');
const leaveList = ref<TestLeaveVo[]>([]);
const loading = ref(false);
const columnControllerVisible = ref(false);
const showSearch = ref(true);
const total = ref(0);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const leaveTime = ref<Array<string>>([]);
const status = ref<'draft' | 'submit'>('submit');
const options = [
  {
    value: '1',
    label: '事假',
  },
  {
    value: '2',
    label: '调休',
  },
  {
    value: '3',
    label: '病假',
  },
  {
    value: '4',
    label: '婚假',
  },
];
// 提交组件
const submitVerifyRef = ref<InstanceType<typeof SubmitVerify>>();
// 审批记录组件
const approvalRecordRef = ref<InstanceType<typeof ApprovalRecord>>();
const taskVariables = ref<Record<string, any>>({});

const submitFormData = ref<StartProcessBo>({
  businessKey: '',
  processKey: '',
  variables: {},
});

// 校验规则
const rules = ref<Record<string, Array<FormRule>>>({
  leaveType: [{ required: true, message: '请假类型不能为空' }],
  leaveDays: [{ required: true, message: '请假天数不能为空' }],
  remark: [{ max: 255, message: '请假原因不能超过255个字符}' }],
});

// 列显隐信息
const columns = ref<Array<PrimaryTableCol>>([
  { title: `选择列`, colKey: 'row-select', type: 'multiple', width: 50, align: 'center' },
  { title: `请假类型`, colKey: 'leaveType', align: 'center' },
  { title: `开始时间`, colKey: 'startDate', align: 'center', minWidth: 112, width: 180 },
  { title: `结束时间`, colKey: 'endDate', align: 'center', minWidth: 112, width: 180 },
  { title: `请假天数`, colKey: 'leaveDays', align: 'center' },
  { title: `请假原因`, colKey: 'remark', align: 'center', ellipsis: true },
  { title: `流程状态`, colKey: 'businessStatusName', align: 'center' },
  { title: `操作`, colKey: 'operation', align: 'center', width: 180 },
]);
// 提交表单对象
const form = ref<TestLeaveVo & TestLeaveForm>({});
// 查询对象
const queryParams = ref<TestLeaveQuery>({
  pageNum: 1,
  pageSize: 10,
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

const rangeLeaveDays = computed({
  get() {
    return [queryParams.value.startLeaveDays, queryParams.value.endLeaveDays];
  },
  set(value) {
    queryParams.value.startLeaveDays = value[0];
    queryParams.value.endLeaveDays = value[1];
  },
});

/** 查询请假申请列表 */
function getList() {
  loading.value = true;
  listLeave(queryParams.value)
    .then((response) => {
      leaveList.value = response.rows;
      total.value = response.total;
    })
    .finally(() => (loading.value = false));
}

// 表单重置
function reset() {
  form.value = {};
  leaveTime.value = [];
  submitFormData.value = {
    businessKey: '',
    processKey: '',
    variables: {},
  };
  proxy.resetForm('leaveRef');
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}

/** 重置按钮操作 */
function resetQuery() {
  rangeLeaveDays.value = [];
  proxy.resetForm('queryRef');
  handleQuery();
}

const changeLeaveTime = () => {
  const startDate = new Date(leaveTime.value[0]);
  startDate.setHours(0, 0, 0, 0);
  const startTime = startDate.getTime();
  const endDate = new Date(leaveTime.value[1]);
  endDate.setHours(0, 0, 0, 0);
  const endTime = endDate.getTime();
  const diffInMilliseconds = endTime - startTime;
  form.value.leaveDays = Math.floor(diffInMilliseconds / (1000 * 60 * 60 * 24)) + 1;
  form.value.startDate = leaveTime.value[0];
  form.value.endDate = leaveTime.value[1];
};

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
  title.value = '添加请假申请';
}

/** 详情按钮操作 */
function handleDetail(row: TestLeaveVo) {
  reset();
  openView.value = true;
  openViewLoading.value = true;
  const id = row.id;
  getLeave(id).then((response) => {
    form.value = response.data;
    openViewLoading.value = false;
  });
}

/** 修改按钮操作 */
function handleUpdate(row?: TestLeaveVo) {
  buttonLoading.value = true;
  reset();
  open.value = true;
  title.value = '修改请假申请';
  const id = row?.id || ids.value.at(0);
  getLeave(id).then((response) => {
    buttonLoading.value = false;
    form.value = response.data;
    leaveTime.value = [];
    leaveTime.value.push(form.value.startDate);
    leaveTime.value.push(form.value.endDate);
  });
}

function handleOnSubmit(type: 'draft' | 'submit') {
  if (leaveTime.value.length === 0) {
    proxy?.$modal.msgError('请假时间不能为空');
    return;
  }
  status.value = type;
  leaveRef.value.submit();
}

/** 提交表单 */
async function submitForm({ validateResult, firstError }: SubmitContext) {
  if (validateResult === true) {
    buttonLoading.value = true;
    const msgLoading = proxy.$modal.msgLoading('提交中...');
    try {
      if (form.value.id) {
        const res = await updateLeave(form.value);
        await saveAndNext(res.data);
        open.value = false;
      } else {
        const res = await addLeave(form.value);
        await saveAndNext(res.data);
        open.value = false;
      }
    } finally {
      buttonLoading.value = false;
      proxy.$modal.msgClose(msgLoading);
    }
  } else {
    await proxy.$modal.msgError(firstError);
  }
}

async function saveAndNext(data: TestLeaveVo) {
  if (status.value === 'draft') {
    buttonLoading.value = false;
    open.value = false;
    await proxy?.$modal.msgSuccess('暂存成功');
    getList();
  } else {
    await handleStartWorkFlow(data);
    await proxy?.$modal.msgSuccess('提交成功');
  }
}

/** 删除按钮操作 */
function handleDelete(row?: TestLeaveVo) {
  const $ids = row?.id || ids.value;
  proxy.$modal.confirm(`是否确认删除请假申请编号为${$ids}的数据项？`, () => {
    const msgLoading = proxy.$modal.msgLoading('正在删除中...');
    return delLeave($ids)
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

// 提交申请
const handleStartWorkFlow = async (data: TestLeaveVo) => {
  submitFormData.value.processKey = 'leave1';
  submitFormData.value.businessKey = String(data.id);
  // 流程变量
  taskVariables.value = {
    entity: data,
    leaveDays: data.leaveDays,
    userList: [1, 2],
    userList2: [1, 2],
  };
  submitFormData.value.variables = taskVariables.value;
  const resp = await startWorkFlow(submitFormData.value);
  if (submitVerifyRef.value) {
    buttonLoading.value = false;
    submitVerifyRef.value.openDialog(resp.data.taskId);
  }
};
// 审批记录
const handleApprovalRecord = (id: string) => {
  if (approvalRecordRef.value) {
    approvalRecordRef.value.init(id);
  }
};
// 提交回调
const submitCallback = async () => {
  open.value = false;
  handleQuery();
};
/** 撤销按钮操作 */
const handleCancelProcessApply = async (id: string) => {
  proxy?.$modal.confirm('是否确认撤销当前单据？', async () => {
    loading.value = true;
    await cancelProcessApply(id).finally(() => (loading.value = false));
    getList();
    await proxy?.$modal.msgSuccess('撤销成功');
  });
};

/** 导出按钮操作 */
function handleExport() {
  proxy.download(
    'workflow/leave/export',
    {
      ...queryParams.value,
    },
    `leave_${new Date().getTime()}.xlsx`,
  );
}

getList();
</script>
