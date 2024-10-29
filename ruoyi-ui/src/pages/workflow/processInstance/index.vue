<template>
  <t-card>
    <t-row :gutter="20">
      <!-- 流程分类树 -->
      <t-col :sm="2" :xs="12">
        <div class="head-container">
          <t-row style="width: 100%" :gutter="20">
            <t-col :span="10">
              <t-input v-model="categoryName" placeholder="请输入流程分类名称" clearable style="margin-bottom: 20px">
                <template #prefixIcon>
                  <search-icon />
                </template>
              </t-input>
            </t-col>
            <t-col :span="2">
              <t-button shape="square" variant="outline" @click="getTreeselect">
                <template #icon><refresh-icon /></template>
              </t-button>
            </t-col>
          </t-row>
        </div>
        <div class="head-container">
          <t-loading :loading="loadingTree" size="small">
            <t-tree
              ref="deptTreeRef"
              v-model:actived="treeActived"
              v-model:expanded="expandedTree"
              class="t-tree--block-node"
              :data="categoryOptions"
              :keys="{ value: 'categoryCode', label: 'categoryName', children: 'children' }"
              :filter="filterNode"
              activable
              hover
              line
              check-strictly
              allow-fold-node-on-filter
              transition
              @active="handleQuery"
            />
          </t-loading>
        </div>
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
            <template #processDefinitionVersion="{ row }"> v{{ row.processDefinitionVersion }}.0 </template>
            <template #isSuspended="{ row }">
              <t-tag v-if="!row.isSuspended" theme="success" variant="light">激活</t-tag>
              <t-tag v-else theme="danger" variant="light">挂起</t-tag>
            </template>
            <template #businessStatusName="{ row }">
              <t-tag theme="success" variant="light">{{ row.businessStatusName }}</t-tag>
            </template>
            <template #operation="{ row, rowIndex }">
              <t-space :size="8" break-line>
                <t-link theme="primary" hover="color" @click.stop="handleApprovalRecord(row)">
                  <root-list-icon />审批记录
                </t-link>
                <t-link
                  theme="primary"
                  hover="color"
                  @click.stop="getProcessDefinitionHitoryList(row.processDefinitionId, row.processDefinitionKey)"
                >
                  <swap-icon />切换版本
                </t-link>
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
                    <div style="text-align: right; margin: 5px 0px 0px 0px">
                      <t-button size="small" variant="text" @click="cancelPopover(rowIndex)">取消</t-button>
                      <t-button size="small" theme="primary" @click="handleInvalid(row)">确认</t-button>
                    </div>
                  </template>
                </t-popup>
                <t-link theme="danger" hover="color" @click.stop="handleDelete(row)"> <delete-icon />删除 </t-link>
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
    <!-- 审批记录 -->
    <approval-record ref="approvalRecordRef" />
  </t-card>
</template>

<script lang="ts" setup>
import {
  CloseCircleIcon,
  DeleteIcon,
  RefreshIcon,
  RootListIcon,
  SearchIcon,
  SettingIcon,
  SwapIcon,
} from 'tdesign-icons-vue-next';
import type { PageInfo, PrimaryTableCol, TreeNodeModel } from 'tdesign-vue-next';
import { computed, ref } from 'vue';

import { listCategory } from '@/api/workflow/category';
import type { WfCategoryVo } from '@/api/workflow/model/categoryModel';
import type { ProcessInstanceQuery, ProcessInstanceVo } from '@/api/workflow/model/processInstanceModel';
import { getListByKey, migrationDefinition } from '@/api/workflow/processDefinition';
import {
  deleteFinishAndHisInstance,
  deleteRunAndHisInstance,
  deleteRunInstance,
  getPageByFinish,
  getPageByRunning,
} from '@/api/workflow/processInstance';
import ApprovalRecord from '@/components/Process/approvalRecord.vue';
// 审批记录组件
const approvalRecordRef = ref<InstanceType<typeof ApprovalRecord>>();
const { proxy } = getCurrentInstance();

const loadingTree = ref(false);
const treeActived = ref<string[]>([]);
const expandedTree = ref<string[]>([]);
const columnControllerVisible = ref(false);
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
// 流程定义id
const processDefinitionId = ref<string>('');
// 模型定义表格数据
const processInstanceList = ref<ProcessInstanceVo[]>([]);
const processDefinitionHistoryList = ref<Array<any>>([]);
const categoryOptions = ref<WfCategoryVo[]>([]);
const categoryName = ref('');

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

onMounted(() => {
  getProcessInstanceRunningList();
  getTreeselect().then(() => triggerExpandedTree());
});

function triggerExpandedTree() {
  expandedTree.value = categoryOptions.value
    .flatMap((value) => value.children?.concat([value]) ?? [value])
    .map((value) => value.categoryCode);
}

/** 通过条件过滤节点  */
const filterNode = computed(() => {
  const value = categoryName.value;
  return (node: TreeNodeModel) => {
    if (!node.value || !value) return true;
    return node.label.indexOf(value) >= 0;
  };
});

/** 查询流程分类下拉树结构 */
const getTreeselect = async () => {
  return listCategory().then((response) => {
    categoryOptions.value = [
      { categoryCode: 'ALL', categoryName: '顶级节点', children: proxy.handleTree(response.data, 'id', 'parentId') },
    ];
  });
};

// 审批记录
const handleApprovalRecord = (row: any) => {
  if (approvalRecordRef.value) {
    approvalRecordRef.value.init(row.id);
  }
};
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
// 多选框选中数据
const handleSelectionChange = (selection: Array<string | number>) => {
  ids.value = selection as string[];
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
  const id = row.id || ids.value;
  proxy?.$modal.confirm(`是否确认删除id为【${id}】的数据项？`, async () => {
    loading.value = true;
    if (tab.value === 'running') {
      await deleteRunAndHisInstance(id).finally(() => (loading.value = false));
      getProcessInstanceRunningList();
    } else {
      await deleteFinishAndHisInstance(id).finally(() => (loading.value = false));
      getProcessInstanceFinishList();
    }
    await proxy?.$modal.msgSuccess('删除成功');
  });
};
const changeTab = async (data: string) => {
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
        processInstanceId: row.id,
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
const getProcessDefinitionHitoryList = (id: string, key: string) => {
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
</script>
