<template>
  <t-dialog
    v-model:visible="visible"
    :header="title"
    :width="width"
    :height="height"
    attach="body"
    :close-on-overlay-click="false"
  >
    <div v-if="multiInstance === 'add'" class="p-2">
      <t-row :gutter="20">
        <!-- 部门树 -->
        <t-col :lg="2" :xs="12" style="">
          <t-card hover-shadow>
            <t-input v-model="deptName" placeholder="请输入部门名称" clearable>
              <template #prefix-icon><search-icon /></template>
            </t-input>
            <t-loading :loading="loadingDept" size="small">
              <t-tree
                ref="deptTreeRef"
                v-model:actived="deptActived"
                v-model:expanded="expandedDept"
                class="t-tree--block-node"
                :data="deptOptions"
                :keys="{ value: 'id', label: 'label', children: 'children' }"
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
          </t-card>
        </t-col>
        <t-col :lg="10" :xs="12">
          <t-space direction="vertical" style="width: 100%">
            <t-card hover-shadow>
              <t-form
                v-show="showSearch"
                ref="queryRef"
                :data="queryParams"
                layout="inline"
                label-width="calc(4em + 12px)"
              >
                <t-form-item label="用户名称" name="userName">
                  <t-input v-model="queryParams.userName" placeholder="请输入用户名称" clearable @enter="handleQuery" />
                </t-form-item>
                <t-form-item label="用户昵称" name="nickName">
                  <t-input v-model="queryParams.nickName" placeholder="请输入用户昵称" clearable @enter="handleQuery" />
                </t-form-item>
                <t-form-item label="手机号码" name="phonenumber">
                  <t-input
                    v-model="queryParams.phonenumber"
                    placeholder="请输入手机号码"
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
            </t-card>
            <t-card hover-shadow header-bordered>
              <template #header>
                <t-space break-line size="4px">
                  <t-tag
                    v-for="(user, index) in chooseUserList"
                    :key="user.userId"
                    theme="primary"
                    variant="light"
                    closable
                    class="mb-2px"
                    @close="handleCloseTag(user, index)"
                  >
                    {{ user.userName }}
                  </t-tag>
                </t-space>
              </template>
              <t-table
                v-model:column-controller-visible="columnControllerVisible"
                hover
                :loading="loading"
                row-key="userId"
                :data="userList"
                :columns="columns"
                :selected-row-keys="userIds"
                select-on-row-click
                :pagination="pagination"
                :column-controller="{
                  hideTriggerButton: true,
                }"
                @select-change="handleSelectionChange"
              >
                <template #topContent>
                  <t-row>
                    <t-col flex="auto"> </t-col>
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
              </t-table>
            </t-card>
          </t-space>
        </t-col>
      </t-row>
    </div>
    <div v-if="multiInstance === 'delete'" class="p-2">
      <t-table
        :loading="loading"
        :data="taskList"
        :columns="[
          { colKey: 'row-select', type: 'multiple', width: 50, align: 'center' },
          { title: `任务名称`, colKey: 'name', align: 'center', ellipsis: true },
          { title: `办理人`, colKey: 'assigneeName', align: 'center', ellipsis: true },
        ]"
        @selection-change="handleTaskSelection"
      >
      </t-table>
    </div>
    <template #footer>
      <div class="dialog-footer">
        <t-button theme="primary" @click="submitFileForm">确 定</t-button>
        <t-button variant="outline" @click="visible = false">取 消</t-button>
      </div>
    </template>
  </t-dialog>
</template>

<script setup lang="ts">
defineOptions({
  name: 'User',
});

import { RefreshIcon, SearchIcon, SettingIcon } from 'tdesign-icons-vue-next';
import type { PageInfo, PrimaryTableCol, TableProps, TreeNodeModel } from 'tdesign-vue-next';
import { computed, ref } from 'vue';

import type { TreeModel } from '@/api/model/resultModel';
import type { SysUserVo } from '@/api/system/model/userModel';
import { deptTreeSelect } from '@/api/system/user';
import { addMultiInstanceExecution, deleteMultiInstanceExecution } from '@/api/workflow/task';
import type { TaskVo } from '@/api/workflow/task/types';
import { getListByDeleteMultiInstance, getPageByAddMultiInstance, getUserListByIds } from '@/api/workflow/workflowUser';

const { proxy } = getCurrentInstance();

const props = defineProps({
  // 宽
  width: {
    type: String,
    default: '70%',
  },
  // 高
  height: {
    type: String,
    default: '100%',
  },
  // 标题
  title: {
    type: String,
    default: '加签人员',
  },
  // 是否多选
  multiple: {
    type: Boolean,
    default: true,
  },
  // 回显用户id
  userIdList: {
    type: Array as PropType<Array<number | string>>,
    default: () => [],
  },
});
const loadingDept = ref(false);
const expandedDept = ref<number[]>([]);
const deptActived = ref<number[]>([]);
const columnControllerVisible = ref(false);

const userList = ref<SysUserVo[]>();
const taskList = ref<TaskVo[]>();
const loading = ref(true);
const showSearch = ref(true);
const selectionTask = ref<Array<any>[]>();
const visible = ref(false);
const total = ref(0);
const deptName = ref('');
const deptOptions = ref<Array<TreeModel<number>>>([]);
const chooseUserList = ref<SysUserVo[]>([]);
const userIds = computed<number[]>(() => chooseUserList.value.map((item) => item.userId));
// 加签或者减签
const multiInstance = ref('');
const queryParams = ref<Record<string, any>>({
  pageNum: 1,
  pageSize: 10,
  userName: '',
  nickName: '',
  taskId: '',
});

// 列显隐信息
const columns = computed<Array<PrimaryTableCol>>(() => [
  { title: `选择列`, colKey: 'row-select', type: props.multiple ? 'multiple' : 'single', width: 50, align: 'center' },
  { title: `用户编号`, colKey: 'userId', align: 'center', ellipsis: true },
  { title: `用户名称`, colKey: 'userName', align: 'center', ellipsis: true },
  { title: `用户昵称`, colKey: 'nickName', align: 'center', ellipsis: true },
  { title: `手机号码`, colKey: 'phonenumber', align: 'center' },
  { title: `创建时间`, colKey: 'createTime', align: 'center', sorter: true, width: '10%', minWidth: 112 },
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
      getList();
    },
  };
});

/** 查询用户列表 */
const getAddMultiInstanceList = async (taskId: string, userIdList: Array<number | string>) => {
  getTreeSelect().then(() => triggerExpandedDept());
  multiInstance.value = 'add';
  visible.value = true;
  queryParams.value.deptId = deptActived.value.at(0);
  queryParams.value.taskId = taskId;
  loading.value = true;
  const res = await getPageByAddMultiInstance(queryParams.value);
  loading.value = false;
  userList.value = res.rows;
  total.value = res.total;
  await initSelectUser(userIdList);
};

const getList = async () => {
  loading.value = true;
  const res = await getPageByAddMultiInstance(queryParams.value);
  loading.value = false;
  userList.value = res.rows;
  total.value = res.total;
};

async function initSelectUser(userIdList: Array<number | string>) {
  userIdList ??= props.userIdList;
  if (userIdList.length > 0) {
    const data = await getUserListByIds(props.userIdList);
    if (data.data && data.data.length > 0) {
      chooseUserList.value = data.data;
    }
  } else {
    chooseUserList.value = [];
  }
}

const getDeleteMultiInstanceList = async (taskId: string) => {
  deptOptions.value = [];
  loading.value = true;
  queryParams.value.taskId = taskId;
  multiInstance.value = 'delete';
  visible.value = true;
  const res = await getListByDeleteMultiInstance(taskId);
  taskList.value = res.data;
  loading.value = false;
};
/** 搜索按钮操作 */
const handleQuery = () => {
  queryParams.value.pageNum = 1;
  getAddMultiInstanceList(queryParams.value.taskId, userIds.value);
};

/** 重置按钮操作 */
const resetQuery = () => {
  proxy.resetForm('queryRef');
  queryParams.value.pageNum = 1;
  deptActived.value = [];
  handleQuery();
};

/** 选择条数  */
const handleSelectionChange: TableProps['onSelectChange'] = (selection, options) => {
  if (!props.multiple) {
    // 单选
    chooseUserList.value = userList.value.filter((value) => selection.includes(value.userId));
    return;
  }
  if (options.type === 'check' && options.currentRowKey === 'CHECK_ALL_BOX') {
    // 全选
    userList.value
      .filter((value) => !userIds.value.includes(value.userId))
      .forEach((value) => {
        chooseUserList.value.push(value);
      });
  } else if (options.type === 'check') {
    // 选中
    chooseUserList.value.push(options.currentRowData as SysUserVo);
  } else if (options.type === 'uncheck' && options.currentRowKey === 'CHECK_ALL_BOX') {
    // 取消全选
    const ids = userList.value.map((value) => value.userId);
    chooseUserList.value = chooseUserList.value.filter((value) => !ids.includes(value.userId));
  } else {
    // 取消选中
    chooseUserList.value = chooseUserList.value.filter((value) => value.userId !== options.currentRowData.userId);
  }
};

/** 选择条数  */
const handleTaskSelection = (selection: any) => {
  selectionTask.value = selection;
};

/** 查询部门下拉树结构 */
const getTreeSelect = async () => {
  loadingDept.value = true;
  return deptTreeSelect()
    .then((response) => {
      deptOptions.value = response.data;
    })
    .finally(() => (loadingDept.value = false));
};
function triggerExpandedDept() {
  expandedDept.value = deptOptions.value
    .flatMap((value) => value.children?.concat([value]) ?? [value])
    .map((value) => value.id);
}

/** 通过条件过滤节点  */
const filterNode = computed(() => {
  const value = deptName.value;
  return (node: TreeNodeModel) => {
    if (!node.value || !value) return true;
    return node.label.indexOf(value) >= 0;
  };
});
// 删除tag
const handleCloseTag = (user: SysUserVo, index: any) => {
  chooseUserList.value.splice(index, 1);
};
const submitFileForm = async () => {
  if (multiInstance.value === 'add') {
    if (chooseUserList.value && chooseUserList.value.length > 0) {
      loading.value = true;
      const userIds = chooseUserList.value.map((item) => {
        return item.userId;
      });
      const nickNames = chooseUserList.value.map((item) => {
        return item.nickName;
      });
      const params = {
        taskId: queryParams.value.taskId,
        assignees: userIds,
        assigneeNames: nickNames,
      };
      await addMultiInstanceExecution(params);
      emits('submitCallback');
      loading.value = false;
      await proxy?.$modal.msgSuccess('操作成功');
      visible.value = false;
    }
  } else if (selectionTask.value && selectionTask.value.length > 0) {
    loading.value = true;
    const taskIds = selectionTask.value.map((item: any) => {
      return item.id;
    });
    const executionIds = selectionTask.value.map((item: any) => {
      return item.executionId;
    });
    const assigneeIds = selectionTask.value.map((item: any) => {
      return item.assignee;
    });
    const assigneeNames = selectionTask.value.map((item: any) => {
      return item.assigneeName;
    });
    const params = {
      taskId: queryParams.value.taskId,
      taskIds,
      executionIds,
      assigneeIds,
      assigneeNames,
    };
    await deleteMultiInstanceExecution(params);
    emits('submitCallback');
    loading.value = false;
    await proxy?.$modal.msgSuccess('操作成功');
    visible.value = false;
  }
};
// 事件
const emits = defineEmits(['submitCallback']);

/**
 * 对外暴露子组件方法
 */
defineExpose({
  getAddMultiInstanceList,
  getDeleteMultiInstanceList,
});
</script>
