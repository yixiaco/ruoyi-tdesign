<template>
  <t-card>
    <t-row :gutter="20">
      <!-- 流程分类树 -->
      <t-col :sm="2" :xs="12">
        <category-tree v-model="treeActived" @active="handleQuery" />
      </t-col>
      <t-col :lg="10" :xs="12">
        <t-space direction="vertical" style="width: 100%">
          <t-form v-show="showSearch" ref="queryRef" :data="queryParams" layout="inline" label-width="calc(6em + 12px)">
            <t-form-item label="流程定义名称" name="name">
              <t-input v-model="queryParams.name" placeholder="请输入流程定义名称" clearable @enter="handleQuery" />
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
            row-key="businessKey"
            :data="processInstanceList"
            :columns="columns"
            :selected-row-keys="businessKeys"
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
                  <span class="selected-count">已选 {{ businessKeys.length }} 项</span>
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
            <template #processDefinitionVersion="{ row }"> v{{ row.processDefinitionVersion }}.0 </template>
            <template #isSuspended="{ row }">
              <t-tag v-if="!row.isSuspended" theme="success" variant="light">激活</t-tag>
              <t-tag v-else theme="danger" variant="light">挂起</t-tag>
            </template>
            <template #businessStatus="{ row }">
              <dict-tag :options="wf_business_status" :value="row.businessStatus"></dict-tag>
            </template>
            <template #operation="{ row }">
              <t-space :size="8" break-line>
                <t-link
                  v-if="
                    row.businessStatus === 'draft' || row.businessStatus === 'cancel' || row.businessStatus === 'back'
                  "
                  theme="primary"
                  hover="color"
                  @click.stop="handleOpen(row, 'update')"
                >
                  <edit-icon />修改
                </t-link>
                <t-link
                  v-if="
                    row.businessStatus === 'draft' || row.businessStatus === 'cancel' || row.businessStatus === 'back'
                  "
                  theme="danger"
                  hover="color"
                  @click.stop="handleDelete(row)"
                >
                  <delete-icon />删除
                </t-link>
                <t-link theme="primary" hover="color" @click.stop="handleOpen(row, 'view')">
                  <browse-icon />查看
                </t-link>
                <t-link
                  v-if="row.businessStatus === 'waiting'"
                  theme="primary"
                  hover="color"
                  @click.stop="handleCancelProcessApply(row.businessKey)"
                >
                  <rollback-icon />撤销
                </t-link>
              </t-space>
            </template>
          </t-table>
        </t-space>
      </t-col>
    </t-row>
    <!-- 提交组件 -->
    <submit-verify @submit-callback="getList" />
  </t-card>
</template>

<script lang="ts" setup>
import {
  BrowseIcon,
  DeleteIcon,
  EditIcon,
  RefreshIcon,
  RollbackIcon,
  SearchIcon,
  SettingIcon,
} from 'tdesign-icons-vue-next';
import type { PageInfo, PrimaryTableCol } from 'tdesign-vue-next';
import { computed, ref } from 'vue';

import type { ProcessInstanceQuery, ProcessInstanceVo } from '@/api/workflow/model/processInstanceModel';
import { cancelProcessApply, deleteRunAndHisInstance, getPageByCurrent } from '@/api/workflow/processInstance';
import { useRouterJump } from '@/api/workflow/workflowCommon';
import type { RouterJumpVo } from '@/api/workflow/workflowCommon/types';
import SubmitVerify from '@/components/Process/submitVerify.vue';
import CategoryTree from '@/pages/workflow/category/CategoryTree.vue';

const routerJump = useRouterJump();
const { proxy } = getCurrentInstance();
const { wf_business_status } = proxy.useDict('wf_business_status');

const treeActived = ref<string[]>([]);
const columnControllerVisible = ref(false);
// 遮罩层
const loading = ref(true);
// 选中数组
const businessKeys = ref<Array<any>>([]);
// 非单个禁用
const single = ref(true);
// 非多个禁用
const multiple = ref(true);
// 显示搜索条件
const showSearch = ref(true);
// 总条数
const total = ref(0);
// 模型定义表格数据
const processInstanceList = ref<ProcessInstanceVo[]>([]);

const tab = ref('running');
// 查询参数
const queryParams = ref<ProcessInstanceQuery>({
  pageNum: 1,
  pageSize: 10,
  name: undefined,
  categoryCode: undefined,
});

// 列显隐信息
const columns = computed<Array<PrimaryTableCol>>(() => {
  return (
    [
      { colKey: 'row-select', type: 'multiple', width: 30, align: 'center' },
      { title: `序号`, colKey: 'serial-number', width: 70 },
      { title: `id`, colKey: 'id', ellipsis: true, align: 'center' },
      { title: `流程定义名称`, colKey: 'processDefinitionName', ellipsis: true, align: 'center' },
      { title: `流程定义KEY`, colKey: 'processDefinitionKey', align: 'center' },
      { title: `版本号`, colKey: 'processDefinitionVersion', align: 'center' },
      { title: `状态`, colKey: 'isSuspended', align: 'center' },
      { title: `流程状态`, colKey: 'businessStatusName', align: 'center' },
      { title: `启动时间`, colKey: 'startTime', align: 'center', width: '10%', minWidth: 112 },
      { title: `结束时间`, colKey: 'endTime', align: 'center', width: '10%', minWidth: 112 },
      { title: `操作`, colKey: 'operation', align: 'center', fixed: 'right' },
    ] as PrimaryTableCol[]
  ).filter((item) => {
    if (item.colKey === 'isSuspended') {
      return tab.value === 'running';
    }
    if (item.colKey === 'endTime') {
      return tab.value === 'finish';
    }
    return true;
  });
});

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

onMounted(() => {
  getList();
});

/** 搜索按钮操作 */
const handleQuery = () => {
  getList();
};
/** 重置按钮操作 */
const resetQuery = () => {
  proxy.resetForm('queryRef');
  queryParams.value.pageNum = 1;
  treeActived.value = [];
  handleQuery();
};
// 多选框选中数据
const handleSelectionChange = (selection: Array<string | number>) => {
  businessKeys.value = selection as string[];
  single.value = selection.length !== 1;
  multiple.value = !selection.length;
};
// 分页
const getList = () => {
  loading.value = true;
  queryParams.value.categoryCode = treeActived.value.at(0);
  getPageByCurrent(queryParams.value).then((resp) => {
    processInstanceList.value = resp.rows;
    total.value = resp.total;
    loading.value = false;
  });
};

/** 删除按钮操作 */
const handleDelete = async (row: ProcessInstanceVo) => {
  const businessKey = row.businessKey || businessKeys.value;
  proxy?.$modal.confirm(`是否确认删除id为【${businessKey}】的数据项？`, async () => {
    loading.value = true;
    if (tab.value === 'running') {
      await deleteRunAndHisInstance(businessKey).finally(() => (loading.value = false));
      getList();
    }
    await proxy?.$modal.msgSuccess('删除成功');
  });
};

/** 撤销按钮操作 */
const handleCancelProcessApply = async (businessKey: string) => {
  proxy?.$modal.confirm('是否确认撤销当前单据？', async () => {
    loading.value = true;
    if (tab.value === 'running') {
      await cancelProcessApply(businessKey).finally(() => (loading.value = false));
      getList();
    }
    await proxy?.$modal.msgSuccess('撤销成功');
  });
};

// 办理
const handleOpen = async (row: ProcessInstanceVo, type: string) => {
  const routerJumpVo = reactive<RouterJumpVo>({
    wfDefinitionConfigVo: row.wfDefinitionConfigVo,
    wfNodeConfigVo: row.wfNodeConfigVo,
    businessKey: row.businessKey,
    taskId: row.id,
    type,
  });
  routerJump(routerJumpVo);
};
</script>
