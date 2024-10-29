<template>
  <t-card>
    <t-row :gutter="20">
      <!--模型分类-->
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
      <!--模型数据-->
      <t-col :sm="10" :xs="12">
        <t-space direction="vertical" style="width: 100%">
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
            :data="processDefinitionList"
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
                  <t-button theme="primary" @click="uploadDialog.visible = true">
                    <template #icon> <cloud-upload-icon /></template>
                    部署流程文件
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
            <template #version="{ row }">v{{ row.version }}.0</template>
            <template #resourceName="{ row }">
              <t-link theme="primary" hover="color" @click.stop="clickPreviewXML(row.id)">
                {{ row.resourceName }}
              </t-link>
            </template>
            <template #diagramResourceName="{ row }">
              <t-link theme="primary" hover="color" @click.stop="clickPreviewImg(row.id)">
                {{ row.resourceName }}
              </t-link>
            </template>
            <template #suspensionState="{ row }">
              <t-tag v-if="row.suspensionState === 1" theme="success" variant="light">激活</t-tag>
              <t-tag v-else theme="danger" variant="light">挂起</t-tag>
            </template>
            <template #operation="{ row }">
              <t-space :size="8" break-line>
                <t-link theme="primary" hover="color" size="small" @click.stop="handleProcessDefState(row)">
                  <lock-on-icon v-if="row.suspensionState === 1" />
                  <lock-off-icon v-else />
                  {{ row.suspensionState === 1 ? '挂起流程' : '激活流程' }}
                </t-link>
                <t-link theme="danger" size="small" hover="color" @click.stop="handleDelete(row)">
                  <template #prefix-icon><delete-icon /></template>删除
                </t-link>
                <t-link theme="primary" size="small" hover="color" @click.stop="handleConvertToModel(row)">
                  <template #prefix-icon><swap-icon /></template>转换模型
                </t-link>
                <t-link
                  theme="primary"
                  size="small"
                  hover="color"
                  @click.stop="getProcessDefinitionHistoryList(row.id, row.key)"
                >
                  <template #prefix-icon><history-icon /></template>历史版本
                </t-link>
              </t-space>
            </template>
          </t-table>
        </t-space>
      </t-col>
    </t-row>
    <!-- 预览图片或xml -->
    <process-preview ref="previewRef" />

    <!-- 部署文件 -->
    <t-dialog
      v-if="uploadDialog.visible"
      v-model:visible="uploadDialog.visible"
      :header="uploadDialog.title"
      :close-on-overlay-click="false"
      width="30%"
    >
      <t-loading :loading="uploadDialogLoading">
        <t-upload
          class="upload-bpmn"
          :limit="1"
          accept="application/zip,application/xml,.bpmn"
          :request-method="handlerDeployProcessFile"
          theme="file"
          draggable
          tips="仅允许导入xls、xlsx格式文件。"
        >
          <template #tips>
            选择BPMN流程文件<br />
            仅支持 .zip、.bpmn20.xml、bpmn 格式文件<br />
            PS:如若部署请部署从本项目模型管理导出的数据
          </template>
        </t-upload>
      </t-loading>
    </t-dialog>

    <!-- 历史版本 -->
    <t-dialog
      v-if="processDefinitionDialog.visible"
      v-model:visible="processDefinitionDialog.visible"
      :header="processDefinitionDialog.title"
      width="70%"
    >
      <t-table
        :loading="loading"
        :columns="historyColumns"
        :data="processDefinitionHistoryList"
        @selection-change="handleSelectionChange"
      >
        <template #version="{ row }">v{{ row.version }}.0</template>
        <template #resourceName="{ row }">
          <t-link theme="primary" hover="color" @click.stop="clickPreviewXML(row.id)">
            {{ row.resourceName }}
          </t-link>
        </template>
        <template #diagramResourceName="{ row }">
          <t-link theme="primary" hover="color" @click.stop="clickPreviewImg(row.id)">
            {{ row.resourceName }}
          </t-link>
        </template>
        <template #suspensionState="{ row }">
          <t-tag v-if="row.suspensionState === 1" theme="success" variant="light">激活</t-tag>
          <t-tag v-else theme="danger" variant="light">挂起</t-tag>
        </template>
        <template #operation="{ row }">
          <t-space :size="8" break-line>
            <t-link theme="primary" hover="color" size="small" @click.stop="handleProcessDefState(row)">
              <lock-on-icon v-if="row.suspensionState === 1" />
              <lock-off-icon v-else />
              {{ row.suspensionState === 1 ? '挂起流程' : '激活流程' }}
            </t-link>
            <t-link theme="danger" size="small" hover="color" @click.stop="handleDelete(row)">
              <template #prefix-icon><delete-icon /></template>删除
            </t-link>
            <t-link theme="primary" size="small" hover="color" @click.stop="handleConvertToModel(row)">
              <template #prefix-icon><swap-icon /></template>转换模型
            </t-link>
          </t-space>
        </template>
      </t-table>
    </t-dialog>
  </t-card>
</template>

<script lang="ts" setup>
import {
  CloudUploadIcon,
  DeleteIcon,
  HistoryIcon,
  LockOffIcon,
  LockOnIcon,
  RefreshIcon,
  SearchIcon,
  SettingIcon,
  SwapIcon,
} from 'tdesign-icons-vue-next';

defineOptions({
  name: 'ProcessDefinition',
});
import type { PageInfo, PrimaryTableCol, TreeNodeModel, UploadProps } from 'tdesign-vue-next';
import { computed, ref } from 'vue';

import { listCategory } from '@/api/workflow/category';
import type { WfCategoryVo } from '@/api/workflow/model/categoryModel';
import {
  convertToModel,
  definitionImage,
  definitionXml,
  deleteProcessDefinition,
  deployProcessFile,
  getListByKey,
  listProcessDefinition,
  updateDefinitionState,
} from '@/api/workflow/processDefinition';
import type { ProcessDefinitionQuery, ProcessDefinitionVo } from '@/api/workflow/processDefinition/types';

import ProcessPreview from './components/processPreview.vue';

const { proxy } = getCurrentInstance();

const previewRef = ref<InstanceType<typeof ProcessPreview>>();

const loadingTree = ref(false);
const treeActived = ref<string[]>([]);
const expandedTree = ref<string[]>([]);
const columnControllerVisible = ref(false);

const loading = ref(true);
const ids = ref<Array<string | number>>([]);
const single = ref(true);
const multiple = ref(true);
const showSearch = ref(true);
const total = ref(0);
const uploadDialogLoading = ref(false);
const processDefinitionList = ref<ProcessDefinitionVo[]>([]);
const processDefinitionHistoryList = ref<ProcessDefinitionVo[]>([]);
const url = ref<string[]>([]);
const categoryOptions = ref<WfCategoryVo[]>([]);
const categoryName = ref('');

const uploadDialog = reactive({
  visible: false,
  title: '部署流程文件',
});

const processDefinitionDialog = reactive({
  visible: false,
  title: '历史版本',
});

// 列显隐信息
const columns = ref<Array<PrimaryTableCol>>([
  { title: `选择列`, colKey: 'row-select', type: 'multiple', width: 30, align: 'center' },
  { title: `序号`, colKey: 'serial-number', width: 60 },
  { title: `流程定义名称`, colKey: 'name', ellipsis: true, align: 'center' },
  { title: `标识Key`, colKey: 'key', ellipsis: true, align: 'center' },
  { title: `版本号`, colKey: 'version', align: 'center' },
  { title: `流程XML`, colKey: 'resourceName', align: 'center', ellipsis: true },
  { title: `流程图片`, colKey: 'diagramResourceName', align: 'center', ellipsis: true },
  { title: `状态`, colKey: 'suspensionState', align: 'center' },
  { title: `部署时间`, colKey: 'deploymentTime', align: 'center', width: '10%', minWidth: 112 },
  { title: `操作`, colKey: 'operation', align: 'center', fixed: 'right' },
]);

// 历史版本列显隐信息
const historyColumns = ref<Array<PrimaryTableCol>>([
  // { title: `选择列`, colKey: 'row-select', type: 'multiple', width: 30, align: 'center' },
  { title: `序号`, colKey: 'serial-number', width: 60 },
  { title: `流程定义名称`, colKey: 'name', ellipsis: true, align: 'center' },
  { title: `标识Key`, colKey: 'key', ellipsis: true, align: 'center' },
  { title: `版本号`, colKey: 'version', align: 'center' },
  { title: `流程XML`, colKey: 'resourceName', align: 'center', ellipsis: true },
  { title: `流程图片`, colKey: 'diagramResourceName', align: 'center', ellipsis: true },
  { title: `状态`, colKey: 'suspensionState', align: 'center' },
  { title: `部署时间`, colKey: 'deploymentTime', align: 'center', sorter: true, width: '10%', minWidth: 112 },
  { title: `操作`, colKey: 'operation', align: 'center', fixed: 'right' },
]);

// 查询参数
const queryParams = ref<ProcessDefinitionQuery>({
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
      getList();
    },
  };
});

onMounted(() => {
  getList();
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

/** 搜索按钮操作 */
const handleQuery = () => {
  queryParams.value.pageNum = 1;
  getList();
};
/** 重置按钮操作 */
const resetQuery = () => {
  proxy.resetForm('queryRef');
  treeActived.value = [];
  handleQuery();
};
// 多选框选中数据
const handleSelectionChange = (selection: any) => {
  ids.value = selection.map((item: any) => item.id);
  single.value = selection.length !== 1;
  multiple.value = !selection.length;
};
// 分页
const getList = async () => {
  loading.value = true;
  const resp = await listProcessDefinition(queryParams.value);
  processDefinitionList.value = resp.rows;
  total.value = resp.total;
  loading.value = false;
};
// 获取历史流程定义
const getProcessDefinitionHistoryList = async (id: string, key: string) => {
  processDefinitionDialog.visible = true;
  loading.value = true;
  const resp = await getListByKey(key);
  if (resp.data && resp.data.length > 0) {
    processDefinitionHistoryList.value = resp.data.filter((item: any) => item.id !== id);
  }
  loading.value = false;
};

// 预览图片
const clickPreviewImg = async (id: string) => {
  loading.value = true;
  const resp = await definitionImage(id);
  if (previewRef.value) {
    url.value = [];
    url.value.push(`data:image/png;base64,${resp.data}`);
    loading.value = false;
    previewRef.value.openDialog(url.value, 'png');
  }
};
// 预览xml
const clickPreviewXML = async (id: string) => {
  loading.value = true;
  const resp = await definitionXml(id);
  if (previewRef.value) {
    url.value = [];
    url.value = resp.data.xml;
    loading.value = false;
    previewRef.value.openDialog(url.value, 'xml');
  }
};
/** 删除按钮操作 */
const handleDelete = async (row: ProcessDefinitionVo) => {
  proxy?.$modal.confirm(`是否确认删除流程定义key为【${row.key}】的数据项？`, async () => {
    loading.value = true;
    await deleteProcessDefinition(row.deploymentId, row.id).finally(() => (loading.value = false));
    await getList();
    await proxy?.$modal.msgSuccess('删除成功');
  });
};
/** 挂起/激活 */
const handleProcessDefState = async (row: ProcessDefinitionVo) => {
  let msg: string;
  if (row.suspensionState === 1) {
    msg = `暂停后，此流程下的所有任务都不允许往后流转，您确定挂起【${row.name || row.key}】吗？`;
  } else {
    msg = `启动后，此流程下的所有任务都允许往后流转，您确定激活【${row.name || row.key}】吗？`;
  }
  proxy?.$modal.confirm(msg, async () => {
    loading.value = true;
    await updateDefinitionState(row.id).finally(() => (loading.value = false));
    await getList();
    proxy?.$modal.msgSuccess('操作成功');
  });
};
/** 流程定义转换为模型 */
const handleConvertToModel = async (row: ProcessDefinitionVo) => {
  proxy?.$modal.confirm(`是否确认转换流程定义key为【${row.key}】的数据项？`, async () => {
    await convertToModel(row.id).finally(() => (loading.value = false));
    getList();
    await proxy?.$modal.msgSuccess('操作成功');
  });
};

// 部署文件
const handlerDeployProcessFile: UploadProps['requestMethod'] = (files) => {
  return new Promise((resolve) => {
    const file = Array.isArray(files) ? files.at(0) : files;
    const formData = new FormData();
    if (queryParams.value.categoryCode === 'ALL') {
      proxy?.$modal.msgError('顶级节点不可作为分类！');
      resolve({ status: 'fail', error: '顶级节点不可作为分类！', response: {} });
      return;
    }
    if (!queryParams.value.categoryCode) {
      proxy?.$modal.msgError('请选择左侧要上传的分类！');
      resolve({ status: 'fail', error: '请选择左侧要上传的分类！', response: {} });
      return;
    }
    uploadDialogLoading.value = true;
    formData.append('file', file.raw);
    formData.append('categoryCode', queryParams.value.categoryCode);
    deployProcessFile(formData)
      .then(() => {
        uploadDialog.visible = false;
        proxy?.$modal.msgSuccess('部署成功');
        uploadDialogLoading.value = false;
        handleQuery();
        resolve({ status: 'success', response: {} });
      })
      .catch((e) => {
        resolve({ status: 'fail', error: e, response: {} });
      });
  });
};
</script>
<style lang="less" scoped>
:global(.upload-bpmn .t-upload__dragger) {
  width: 100%;
}
</style>
