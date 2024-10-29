<template>
  <t-card>
    <t-space direction="vertical" style="width: 100%">
      <t-card class="text-center">
        <t-radio-group v-model="tab" variant="primary-filled" @change="changeTab(tab)">
          <t-radio-button value="waiting">待办任务</t-radio-button>
          <t-radio-button value="finish">已办任务</t-radio-button>
        </t-radio-group>
      </t-card>

      <t-form v-show="showSearch" ref="queryRef" :data="queryParams" layout="inline" label-width="calc(6em + 12px)">
        <t-form-item label="任务名称" name="name">
          <t-input v-model="queryParams.name" placeholder="请输入流程定义名称" clearable @enter="handleQuery" />
        </t-form-item>
        <t-form-item label="流程定义名称" name="processDefinitionName">
          <t-input
            v-model="queryParams.processDefinitionName"
            placeholder="请输入流程定义名称"
            clearable
            @enter="handleQuery"
          />
        </t-form-item>
        <t-form-item label="流程定义KEY" name="processDefinitionKey">
          <t-input
            v-model="queryParams.processDefinitionKey"
            placeholder="请输入流程定义KEY"
            clearable
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
        v-model:column-controller-visible="columnControllerVisible"
        hover
        :loading="loading"
        row-key="id"
        :data="taskList"
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
              <t-button theme="primary" variant="outline" :disabled="multiple" @click="handleUpdate">
                <template #icon> <edit-icon /> </template>
                修改办理人
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
        <template #assigneeName="{ row }">
          <template v-if="tab === 'waiting'">
            <template v-if="row.participantVo && row.assignee === null">
              <t-tag
                v-for="(item, index) in row.participantVo.candidateName"
                :key="index"
                variant="light"
                theme="success"
              >
                {{ item }}
              </t-tag>
            </template>
            <template v-else>
              <t-tag variant="light" theme="success">
                {{ row.assigneeName }}
              </t-tag>
            </template>
          </template>
          <template v-else-if="tab === 'finish'">
            <t-tag variant="light" theme="success">
              {{ row.assigneeName }}
            </t-tag>
          </template>
        </template>
        <template #businessStatusName="{ row }">
          <t-tag v-if="tab === 'waiting'" variant="light" theme="success">{{ row.businessStatusName }}</t-tag>
          <t-tag v-else variant="light" theme="success">已完成</t-tag>
        </template>
        <template #operation="{ row }">
          <t-space :size="8" break-line>
            <t-link theme="primary" hover="color" @click.stop="handleApprovalRecord(row)">
              <root-list-icon />审批记录
            </t-link>
            <t-link theme="primary" hover="color" @click.stop="addMultiInstanceUser(row)">
              <add-circle-icon />加签
            </t-link>
            <t-link theme="primary" hover="color" @click.stop="deleteMultiInstanceUser(row)">
              <minus-circle-icon />减签
            </t-link>
          </t-space>
        </template>
      </t-table>
    </t-space>
    <!-- 审批记录 -->
    <approval-record ref="approvalRecordRef" />
    <!-- 提交组件 -->
    <submit-verify ref="submitVerifyRef" :task-id="taskId" @submit-callback="handleQuery" />
    <!-- 加签组件 -->
    <multi-instance-user ref="multiInstanceUserRef" :title="title" @submit-callback="handleQuery" />
    <!-- 选人组件 -->
    <select-sys-user ref="selectSysUserRef" :multiple="true" @submit-callback="submitCallback" />
  </t-card>
</template>

<script lang="ts" setup>
import {
  AddCircleIcon,
  EditIcon,
  MinusCircleIcon,
  RefreshIcon,
  RootListIcon,
  SearchIcon,
  SettingIcon,
} from 'tdesign-icons-vue-next';
import type { PageInfo, PrimaryTableCol } from 'tdesign-vue-next';
import { computed, ref } from 'vue';

import type { SysUserVo } from '@/api/system/model/userModel';
import { getPageByAllTaskFinish, getPageByAllTaskWait, updateAssignee } from '@/api/workflow/task';
import type { TaskQuery, TaskVo } from '@/api/workflow/task/types';
import ApprovalRecord from '@/components/Process/approvalRecord.vue';
import MultiInstanceUser from '@/components/Process/multiInstanceUser.vue';
import SelectSysUser from '@/components/Process/selectSysUser.vue';
import SubmitVerify from '@/components/Process/submitVerify.vue';
// 提交组件
const submitVerifyRef = ref<InstanceType<typeof SubmitVerify>>();
// 审批记录组件
const approvalRecordRef = ref<InstanceType<typeof ApprovalRecord>>();
// 加签组件
const multiInstanceUserRef = ref<InstanceType<typeof MultiInstanceUser>>();
// 选人组件
const selectSysUserRef = ref<InstanceType<typeof SelectSysUser>>();

const columnControllerVisible = ref(false);

const { proxy } = getCurrentInstance();
// 遮罩层
const loading = ref(true);
// 选中数组
const ids = ref<Array<any>>([]);
// 非单个禁用
const single = ref(true);
// 非多个禁用
const multiple = ref(true);
// 显示搜索条件
const showSearch = ref(true);
// 总条数
const total = ref(0);
// 模型定义表格数据
const taskList = ref([]);
// 任务id
const taskId = ref('');
const title = ref('');
// 查询参数
const queryParams = ref<TaskQuery>({
  pageNum: 1,
  pageSize: 10,
  name: undefined,
  processDefinitionName: undefined,
  processDefinitionKey: undefined,
});
const tab = ref('waiting');

// 列显隐信息
const columns = ref<Array<PrimaryTableCol>>([
  { colKey: 'row-select', type: 'multiple', width: 30, align: 'center' },
  { title: `序号`, colKey: 'serial-number', width: 70 },
  { title: `流程定义名称`, colKey: 'processDefinitionName', ellipsis: true, align: 'center' },
  { title: `流程定义KEY`, colKey: 'processDefinitionKey', align: 'center' },
  { title: `任务名称`, colKey: 'name', align: 'center' },
  { title: `办理人`, colKey: 'assigneeName', align: 'center' },
  { title: `流程状态`, colKey: 'businessStatusName', align: 'center' },
  { title: `创建时间`, colKey: 'startTime', align: 'center', width: '10%', minWidth: 112 },
  { title: `结束时间`, colKey: 'endTime', align: 'center', width: '10%', minWidth: 112 },
  { title: `操作`, colKey: 'operation', align: 'center', fixed: 'right' },
]);

const pagination = computed(() => {
  return {
    current: queryParams.value.pageNum,
    pageSize: queryParams.value.pageSize,
    total: total.value,
    showJumper: true,
    onChange: (pageInfo: PageInfo) => {
      queryParams.value.pageNum = pageInfo.current;
      queryParams.value.pageSize = pageInfo.pageSize;
      getWaitingList();
    },
  };
});

onMounted(() => {
  getWaitingList();
});
// 审批记录
const handleApprovalRecord = (row: TaskVo) => {
  if (approvalRecordRef.value) {
    approvalRecordRef.value.init(row.processInstanceId);
  }
};
// 加签
const addMultiInstanceUser = (row: TaskVo) => {
  if (multiInstanceUserRef.value) {
    title.value = '加签人员';
    multiInstanceUserRef.value.getAddMultiInstanceList(row.id, []);
  }
};
// 减签
const deleteMultiInstanceUser = (row: TaskVo) => {
  if (multiInstanceUserRef.value) {
    title.value = '减签人员';
    multiInstanceUserRef.value.getDeleteMultiInstanceList(row.id);
  }
};
/** 搜索按钮操作 */
const handleQuery = () => {
  if (tab.value === 'waiting') {
    getWaitingList();
  } else {
    getFinishList();
  }
};
/** 重置按钮操作 */
const resetQuery = () => {
  proxy.resetForm('queryRef');
  queryParams.value.pageNum = 1;
  handleQuery();
};
// 多选框选中数据
const handleSelectionChange = (selection: Array<string | number>) => {
  ids.value = selection;
  single.value = selection.length !== 1;
  multiple.value = !selection.length;
};
const changeTab = async (data: string) => {
  queryParams.value.pageNum = 1;
  if (data === 'waiting') {
    getWaitingList();
  } else {
    getFinishList();
  }
};
// 分页
const getWaitingList = () => {
  loading.value = true;
  getPageByAllTaskWait(queryParams.value).then((resp) => {
    taskList.value = resp.rows;
    total.value = resp.total;
    loading.value = false;
  });
};
const getFinishList = () => {
  loading.value = true;
  getPageByAllTaskFinish(queryParams.value).then((resp) => {
    taskList.value = resp.rows;
    total.value = resp.total;
    loading.value = false;
  });
};
const handleUpdate = () => {
  if (selectSysUserRef.value) {
    selectSysUserRef.value.getUserList([]);
  }
};
// 修改办理人
const submitCallback = (data: SysUserVo[]) => {
  if (data && data.length > 0) {
    updateAssignee(ids.value, data[0].userId).then(() => {
      selectSysUserRef.value.close();
      proxy?.$modal.msgSuccess('操作成功');
      handleQuery();
    });
  }
};
</script>
