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
        <template #processDefinitionName="{ row }">
          <span>{{ row.processDefinitionName }}v{{ row.processDefinitionVersion }}.0</span>
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
                {{ row.assigneeName || '无' }}
              </t-tag>
            </template>
          </template>
          <template v-else-if="tab === 'finish'">
            <t-tag variant="light" theme="success">
              {{ row.assigneeName || '无' }}
            </t-tag>
          </template>
        </template>
        <template #businessStatus="{ row }">
          <dict-tag v-if="tab === 'waiting'" :options="wf_business_status" :value="row.businessStatus"></dict-tag>
          <t-tag v-else variant="light" theme="success">已完成</t-tag>
        </template>
        <template #operation="{ row }">
          <t-space :size="8" break-line>
            <t-link theme="primary" hover="color" size="small" @click.stop="handleView(row)">
              <template #prefix-icon><browse-icon /></template>查看
            </t-link>
            <t-link
              v-if="tab === 'waiting'"
              theme="primary"
              hover="color"
              size="small"
              @click.stop="handleInstanceVariable(row)"
            >
              <template #prefix-icon><calculator1-icon /></template>流程变量
            </t-link>
            <template v-if="row.multiInstance">
              <t-link theme="primary" hover="color" size="small" @click.stop="deleteMultiInstanceUser(row)">
                <template #prefix-icon><minus-circle-icon /></template>减签
              </t-link>
              <t-link theme="primary" hover="color" size="small" @click.stop="addMultiInstanceUser(row)">
                <template #prefix-icon><add-circle-icon /></template>加签
              </t-link>
            </template>
          </t-space>
        </template>
      </t-table>
    </t-space>
    <!-- 加签组件 -->
    <multi-instance-user ref="multiInstanceUserRef" :title="title" @submit-callback="handleQuery" />
    <!-- 选人组件 -->
    <user-select ref="userSelectRef" :multiple="false" @confirm-call-back="submitCallback"></user-select>
    <!-- 流程变量开始 -->
    <t-dialog v-model:visible="variableVisible" header="流程变量" width="60%" :close-on-overlay-click="false">
      <t-loading :loading="variableLoading">
        <t-card class="box-card">
          <template #header>
            <div class="clearfix">
              <span>
                流程定义名称：
                <t-tag variant="light" theme="primary">{{ processDefinitionName }}</t-tag>
              </span>
            </div>
          </template>
          <div v-for="(v, index) in variableList" :key="index">
            <t-form v-if="v.key !== '_FLOWABLE_SKIP_EXPRESSION_ENABLED'" label-align="right" label-width="150px">
              <t-form-item :label="v.key + '：'">
                {{ v.value }}
              </t-form-item>
            </t-form>
          </div>
        </t-card>
      </t-loading>
    </t-dialog>
    <!-- 流程变量结束 -->
  </t-card>
</template>

<script lang="ts" setup>
import {
  AddCircleIcon,
  BrowseIcon,
  Calculator1Icon,
  EditIcon,
  MinusCircleIcon,
  RefreshIcon,
  SearchIcon,
  SettingIcon,
} from 'tdesign-icons-vue-next';
import type { PageInfo, PrimaryTableCol } from 'tdesign-vue-next';
import { computed, ref } from 'vue';

import type { SysUserVo } from '@/api/system/model/userModel';
import { getInstanceVariable, getPageByAllTaskFinish, getPageByAllTaskWait, updateAssignee } from '@/api/workflow/task';
import type { TaskQuery, TaskVo, VariableVo } from '@/api/workflow/task/types';
import { useRouterJump } from '@/api/workflow/workflowCommon';
import type { RouterJumpVo } from '@/api/workflow/workflowCommon/types';
import MultiInstanceUser from '@/components/Process/multiInstanceUser.vue';
import UserSelect from '@/components/user-select/index.vue';
// 加签组件
const multiInstanceUserRef = ref<InstanceType<typeof MultiInstanceUser>>();
// 选人组件
const userSelectRef = ref<InstanceType<typeof UserSelect>>();

const columnControllerVisible = ref(false);

const { proxy } = getCurrentInstance();
const routerJump = useRouterJump();
const { wf_business_status } = proxy.useDict('wf_business_status');

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
const taskList = ref<TaskVo[]>([]);
const title = ref('');
// 流程变量是否显示
const variableVisible = ref(false);
const variableLoading = ref(true);
// 流程变量
const variableList = ref<VariableVo[]>([]);
// 流程定义名称
const processDefinitionName = ref();
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
const columns = computed<Array<PrimaryTableCol>>(
  () =>
    [
      { colKey: 'row-select', type: 'multiple', width: 30, align: 'center' },
      { title: `序号`, colKey: 'serial-number', width: 70 },
      { title: `流程定义名称`, colKey: 'processDefinitionName', ellipsis: true, align: 'center' },
      { title: `流程定义KEY`, colKey: 'processDefinitionKey', align: 'center' },
      { title: `任务名称`, colKey: 'name', align: 'center' },
      { title: `办理人`, colKey: 'assigneeName', align: 'center' },
      { title: `流程状态`, colKey: 'businessStatus', align: 'center' },
      { title: `创建时间`, colKey: 'createTime', align: 'center', width: '10%', minWidth: 112 },
      { title: `创建时间`, colKey: 'startTime', align: 'center', width: '10%', minWidth: 112 },
      { title: `结束时间`, colKey: 'endTime', align: 'center', width: '10%', minWidth: 112 },
      { title: `操作`, colKey: 'operation', align: 'center', fixed: 'right' },
    ].filter((item) => {
      if (item.colKey === 'createTime') {
        return tab.value === 'waiting';
      }
      if (item.colKey === 'startTime') {
        return tab.value === 'finish';
      }
      return true;
    }) as PrimaryTableCol[],
);

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
  taskList.value = [];
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
// 打开修改选人
const handleUpdate = () => {
  userSelectRef.value.open();
};
// 修改办理人
const submitCallback = async (data: SysUserVo[]) => {
  if (data && data.length > 0) {
    proxy?.$modal.confirm('是否确认提交？', async () => {
      loading.value = true;
      await updateAssignee(ids.value, data[0].userId);
      handleQuery();
      proxy?.$modal.msgSuccess('操作成功');
    });
  } else {
    proxy?.$modal.msgWarning('请选择用户！');
  }
};
// 查询流程变量
const handleInstanceVariable = async (row: TaskVo) => {
  variableLoading.value = true;
  variableVisible.value = true;
  processDefinitionName.value = row.processDefinitionName;
  const data = await getInstanceVariable(row.id);
  variableList.value = data.data;
  variableLoading.value = false;
};

/** 查看按钮操作 */
const handleView = (row: TaskVo) => {
  const routerJumpVo = reactive<RouterJumpVo>({
    wfDefinitionConfigVo: row.wfDefinitionConfigVo,
    wfNodeConfigVo: row.wfNodeConfigVo,
    businessKey: row.businessKey,
    taskId: row.id,
    type: 'view',
  });
  routerJump(routerJumpVo);
};

onMounted(() => {
  getWaitingList();
});
</script>
