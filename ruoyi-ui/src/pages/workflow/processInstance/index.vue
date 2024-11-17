<template>
  <t-card>
    <t-row :gutter="20">
      <!-- 流程分类树 -->
      <t-col :sm="2" :xs="12">
        <category-tree v-model="treeActived" @active="handleQuery" />
      </t-col>

      <t-col :lg="10" :xs="12">
        <t-space direction="vertical" style="width: 100%">
          <t-card class="text-center">
            <t-radio-group v-model="tab" variant="primary-filled" @change="changeTab(tab)">
              <t-radio-button value="running">运行中</t-radio-button>
              <t-radio-button value="finish">已完成</t-radio-button>
            </t-radio-group>
          </t-card>

          <t-form v-show="showSearch" ref="queryRef" :data="queryParams" layout="inline" label-width="calc(6em + 12px)">
            <t-form-item label="流程定义名称" name="name">
              <t-input v-model="queryParams.name" placeholder="请输入流程定义名称" clearable @enter="handleQuery" />
            </t-form-item>
            <t-form-item label="流程定义KEY" name="key">
              <t-input v-model="queryParams.key" placeholder="请输入流程定义KEY" clearable @enter="handleQuery" />
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
            :data="processInstanceList"
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
                  <t-button theme="danger" variant="outline" :disabled="multiple" @click="handleDelete">
                    <template #icon> <delete-icon /> </template>
                    删除
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
            <template #processDefinitionVersion="{ row }"> v{{ row.processDefinitionVersion }}.0 </template>
            <template #isSuspended="{ row }">
              <t-tag v-if="!row.isSuspended" theme="success" variant="light">激活</t-tag>
              <t-tag v-else theme="danger" variant="light">挂起</t-tag>
            </template>
            <template #businessStatus="{ row }">
              <dict-tag :options="wf_business_status" :value="row.businessStatus"></dict-tag>
            </template>
            <template #operation="{ row, rowIndex }">
              <t-space :size="8" break-line>
                <t-popup
                  :ref="`popoverRef${rowIndex}`"
                  trigger="click"
                  placement="left"
                  :overlay-style="{ width: '300px' }"
                >
                  <t-link theme="primary" hover="color" @click.stop="handleInvalid(row)">
                    <close-circle-icon />作废
                  </t-link>
                  <template #content>
                    <t-textarea
                      v-model="deleteReason"
                      :autosize="{ minRows: 3, maxRows: 3 }"
                      placeholder="请输入作废原因"
                    />
                    <div style="text-align: right; margin: 5px 0 0 0">
                      <t-button size="small" variant="text" @click="cancelPopover(rowIndex)">取消</t-button>
                      <t-button size="small" theme="primary" @click="handleInvalid(row)">确认</t-button>
                    </div>
                  </template>
                </t-popup>
                <t-link theme="danger" hover="color" @click.stop="handleDelete(row)"> <delete-icon />删除 </t-link>
                <t-link theme="primary" hover="color" @click.stop="handleView(row)"> <browse-icon />查看 </t-link>
              </t-space>
            </template>
          </t-table>
        </t-space>
      </t-col>
    </t-row>
    <t-dialog
      v-if="processDefinitionDialog.visible"
      v-model:visible="processDefinitionDialog.visible"
      :header="processDefinitionDialog.title"
      width="70%"
    >
      <t-table :loading="loading" :columns="definitionColumns" :data="processDefinitionHistoryList">
        <template #version="{ row }"> v{{ row.version }}.0 </template>
        <template #suspensionState="{ row }">
          <t-tag v-if="row.suspensionState == 1" variant="outline" theme="success">激活</t-tag>
          <t-tag v-else variant="outline" theme="danger">挂起</t-tag>
        </template>
        <template #operation="{ row }">
          <t-space :size="8" break-line>
            <t-link theme="primary" size="small" hover="color" @click.stop="handleChange(row.id)">
              <swap-icon />切换
            </t-link>
          </t-space>
        </template>
      </t-table>
    </t-dialog>
  </t-card>
</template>

<script lang="ts" setup>
import {
  BrowseIcon,
  CloseCircleIcon,
  DeleteIcon,
  RefreshIcon,
  SearchIcon,
  SettingIcon,
  SwapIcon,
} from 'tdesign-icons-vue-next';
import type { PageInfo, PrimaryTableCol, TableProps } from 'tdesign-vue-next';
import { computed, ref } from 'vue';

import type { ProcessInstanceQuery, ProcessInstanceVo } from '@/api/workflow/model/processInstanceModel';
import { getListByKey, migrationDefinition } from '@/api/workflow/processDefinition';
import {
  deleteFinishAndHisInstance,
  deleteRunAndHisInstance,
  deleteRunInstance,
  getPageByFinish,
  getPageByRunning,
} from '@/api/workflow/processInstance';
import { useRouterJump } from '@/api/workflow/workflowCommon';
import type { RouterJumpVo } from '@/api/workflow/workflowCommon/types';
import CategoryTree from '@/pages/workflow/category/CategoryTree.vue';

const { proxy } = getCurrentInstance();
const { wf_business_status } = proxy.useDict('wf_business_status');
const routerJump = useRouterJump();

const treeActived = ref<string[]>([]);
const columnControllerVisible = ref(false);
// 遮罩层
const loading = ref(true);
// 选中数组
const ids = ref<Array<any>>([]);
// 选中业务id数组
const businessKeys = ref<Array<any>>([]);
// 非单个禁用
const single = ref(true);
// 非多个禁用
const multiple = ref(true);
// 显示搜索条件
const showSearch = ref(true);
// 总条数
const total = ref(0);
// 流程定义id
const processDefinitionId = ref<string>('');
// 模型定义表格数据
const processInstanceList = ref<ProcessInstanceVo[]>([]);
const processDefinitionHistoryList = ref<Array<any>>([]);

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
      { title: `流程状态`, colKey: 'businessStatus', align: 'center' },
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
// 流程定义列显隐信息
const definitionColumns = computed<Array<PrimaryTableCol>>(() => [
  { title: `序号`, colKey: 'serial-number', width: 70 },
  { title: `流程定义名称`, colKey: 'name', align: 'center' },
  { title: `标识Key`, colKey: 'key', align: 'center' },
  { title: `版本号`, colKey: 'version', align: 'center' },
  { title: `状态`, colKey: 'suspensionState', align: 'center' },
  { title: `部署时间`, colKey: 'deploymentTime', align: 'center', width: '10%', minWidth: 112, ellipsis: true },
  { title: `操作`, colKey: 'operation', align: 'center', fixed: 'right' },
]);

const processDefinitionDialog = reactive({
  visible: false,
  title: '流程定义',
});

const tab = ref('running');
// 作废原因
const deleteReason = ref('');
// 查询参数
const queryParams = ref<ProcessInstanceQuery>({
  pageNum: 1,
  pageSize: 10,
  name: undefined,
  key: undefined,
  categoryCode: undefined,
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
      getProcessInstanceRunningList();
    },
  };
});

/** 搜索按钮操作 */
const handleQuery = () => {
  if (tab.value === 'running') {
    getProcessInstanceRunningList();
  } else {
    getProcessInstanceFinishList();
  }
};
/** 重置按钮操作 */
const resetQuery = () => {
  proxy.resetForm('queryRef');
  queryParams.value.pageNum = 1;
  treeActived.value = [];
  handleQuery();
};
const selectList = ref<any[]>();
// 多选框选中数据
const handleSelectionChange: TableProps['onSelectChange'] = (selection, options) => {
  if (options.type === 'uncheck' && options.currentRowKey === 'CHECK_ALL_BOX') {
    // 取消全选. 注：此处组件有bug，无法获取到取消全选的数据，只能通过获取到全部数据再做对比
    const ids = processInstanceList.value.map((value) => value.id);
    selectList.value = selectList.value.filter((value) => !ids.includes(value.id));
  } else {
    selectList.value = options.selectedRowData;
  }
  ids.value = selection as string[];
  businessKeys.value = selectList.value.map((item) => item.businessKey);
  single.value = selection.length !== 1;
  multiple.value = !selection.length;
};
// 分页
const getProcessInstanceRunningList = () => {
  loading.value = true;
  queryParams.value.categoryCode = treeActived.value.at(0);
  getPageByRunning(queryParams.value).then((resp) => {
    processInstanceList.value = resp.rows;
    total.value = resp.total;
    loading.value = false;
  });
};
// 分页
const getProcessInstanceFinishList = () => {
  loading.value = true;
  getPageByFinish(queryParams.value).then((resp) => {
    processInstanceList.value = resp.rows;
    total.value = resp.total;
    loading.value = false;
  });
};

/** 删除按钮操作 */
const handleDelete = async (row: any) => {
  const businessKey = row.businessKey || businessKeys.value;
  proxy?.$modal.confirm(`是否确认删除id为【${businessKey}】的数据项？`, async () => {
    loading.value = true;
    if (tab.value === 'running') {
      await deleteRunAndHisInstance(businessKey).finally(() => (loading.value = false));
      getProcessInstanceRunningList();
    } else {
      await deleteFinishAndHisInstance(businessKey).finally(() => (loading.value = false));
      getProcessInstanceFinishList();
    }
    await proxy?.$modal.msgSuccess('删除成功');
  });
};
const changeTab = async (data: string) => {
  processInstanceList.value = [];
  queryParams.value.pageNum = 1;
  if (data === 'running') {
    getProcessInstanceRunningList();
  } else {
    getProcessInstanceFinishList();
  }
};
/** 作废按钮操作 */
const handleInvalid = async (row: ProcessInstanceVo) => {
  proxy?.$modal.confirm(`是否确认作废业务id为【${row.businessKey}】的数据项？`, async () => {
    loading.value = true;
    if (tab.value === 'running') {
      const param = {
        businessKey: row.businessKey,
        deleteReason: deleteReason.value,
      };
      await deleteRunInstance(param).finally(() => (loading.value = false));
      getProcessInstanceRunningList();
      await proxy?.$modal.msgSuccess('操作成功');
    }
  });
};
const cancelPopover = async (index: any) => {
  (proxy?.$refs[`popoverRef${index}`] as any).hide(); // 关闭弹窗
};
// 获取流程定义
const getProcessDefinitionHistoryList = (id: string, key: string) => {
  processDefinitionDialog.visible = true;
  processDefinitionId.value = id;
  loading.value = true;
  getListByKey(key).then((resp) => {
    if (resp.data && resp.data.length > 0) {
      processDefinitionHistoryList.value = resp.data.filter((item: any) => item.id !== id);
    }
    loading.value = false;
  });
};
// 切换流程版本
const handleChange = async (id: string) => {
  proxy?.$modal.confirm('是否确认切换？', () => {
    loading.value = true;
    migrationDefinition(processDefinitionId.value, id).then(() => {
      proxy?.$modal.msgSuccess('操作成功');
      getProcessInstanceRunningList();
      processDefinitionDialog.visible = false;
      loading.value = false;
    });
  });
};

/** 查看按钮操作 */
const handleView = (row: ProcessInstanceVo) => {
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
  getProcessInstanceRunningList();
});
</script>
