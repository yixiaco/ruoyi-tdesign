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
          <t-form v-show="showSearch" ref="queryRef" :data="queryParams" layout="inline" label-width="calc(4em + 12px)">
            <t-form-item label="模型名称" name="name">
              <t-input v-model="queryParams.name" placeholder="请输入模型名称" clearable @enter="handleQuery" />
            </t-form-item>
            <t-form-item label="模型KEY" name="key">
              <t-input v-model="queryParams.key" placeholder="请输入模型KEY" clearable @enter="handleQuery" />
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
            :data="modelList"
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
                  <t-button v-hasPermi="['workflow:model:add']" theme="primary" @click="handleAdd">
                    <template #icon> <add-icon /></template>
                    新增
                  </t-button>
                  <t-button
                    v-hasPermi="['workflow:model:edit']"
                    theme="default"
                    variant="outline"
                    :disabled="single"
                    @click="handleUpdate()"
                  >
                    <template #icon> <edit-icon /> </template>
                    修改
                  </t-button>
                  <t-button
                    v-hasPermi="['workflow:model:remove']"
                    theme="danger"
                    variant="outline"
                    :disabled="multiple"
                    @click="handleDelete()"
                  >
                    <template #icon> <delete-icon /> </template>
                    删除
                  </t-button>
                  <t-button
                    v-hasPermi="['workflow:model:export']"
                    theme="default"
                    variant="outline"
                    @click="clickExportZip()"
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
            <template #version="{ row }">v{{ row.version }}.0</template>
            <template #operation="{ row }">
              <t-space :size="8" break-line>
                <t-link
                  v-hasPermi="['workflow:model:design']"
                  size="small"
                  theme="primary"
                  hover="color"
                  @click.stop="clickDesign(row.id)"
                >
                  <template #prefix-icon><gesture-up-icon /></template>流程设计
                </t-link>
                <t-link
                  v-hasPermi="['workflow:model:export']"
                  size="small"
                  theme="primary"
                  hover="color"
                  @click.stop="clickExportZip(row)"
                >
                  <template #prefix-icon>
                    <download-1-icon />
                  </template>
                  导出
                </t-link>
                <t-link
                  v-hasPermi="['workflow:model:deploy']"
                  size="small"
                  theme="primary"
                  hover="color"
                  @click.stop="clickDeploy(row.id, row.key)"
                >
                  <template #prefix-icon><cloud-upload-icon /></template>流程部署
                </t-link>
                <t-link
                  v-hasPermi="['workflow:model:query']"
                  size="small"
                  theme="primary"
                  hover="color"
                  @click.stop="handleDetail(row)"
                >
                  <template #prefix-icon><browse-icon /></template>详情
                </t-link>
                <t-link
                  v-hasPermi="['workflow:model:edit']"
                  size="small"
                  theme="primary"
                  hover="color"
                  @click.stop="handleUpdate(row)"
                >
                  <template #prefix-icon><edit-icon /></template>修改
                </t-link>
                <t-link
                  v-hasPermi="['workflow:model:remove']"
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
      </t-col>
    </t-row>

    <!-- 添加或修改流程分类对话框 -->
    <t-dialog
      v-model:visible="open"
      :header="title"
      destroy-on-close
      :close-on-overlay-click="false"
      width="min(650px, 100%)"
      attach="body"
      :confirm-btn="{
        loading: buttonLoading,
      }"
      @confirm="formRef.submit()"
    >
      <t-loading :loading="buttonLoading" size="small">
        <t-form
          ref="formRef"
          :data="form"
          :rules="rules"
          label-align="right"
          label-width="calc(4em + 41px)"
          scroll-to-first-error="smooth"
          @submit="submitForm"
        >
          <t-form-item label="模型名称" name="name">
            <t-input v-model="form.name" placeholder="请输入模型名称" clearable />
          </t-form-item>
          <t-form-item label="模型KEY" name="key">
            <t-input v-model="form.key" placeholder="请输入模型KEY" clearable />
          </t-form-item>
          <t-form-item label="流程分类" name="categoryCode">
            <t-tree-select
              v-model="form.categoryCode"
              :data="categoryOptions"
              :tree-props="{
                keys: { value: 'categoryCode', label: 'categoryName', children: 'children' },
                checkStrictly: true,
              }"
              placeholder="请选择流程分类"
            />
          </t-form-item>
          <t-form-item label="备注" name="description">
            <t-textarea v-model="form.description" :maxlength="200" placeholder="请输入备注"></t-textarea>
          </t-form-item>
        </t-form>
      </t-loading>
    </t-dialog>

    <!-- 流程分类详情 -->
    <t-dialog
      v-model:visible="openView"
      header="流程分类详情"
      placement="center"
      width="min(700px, 100%)"
      attach="body"
      :footer="false"
    >
      <my-descriptions :loading="openViewLoading">
        <t-descriptions-item label="模型id">{{ form.id }}</t-descriptions-item>
        <t-descriptions-item label="模型名称">{{ form.name }}</t-descriptions-item>
        <t-descriptions-item label="模型KEY">{{ form.key }}</t-descriptions-item>
        <t-descriptions-item label="流程分类">
          {{ categoryDict.find((item) => item.categoryCode === form.categoryCode)?.categoryName }}
        </t-descriptions-item>
        <t-descriptions-item label="备注" :span="2">{{ form.description }}</t-descriptions-item>
        <t-descriptions-item label="创建时间">{{ parseTime(form.createTime) }}</t-descriptions-item>
        <t-descriptions-item label="更新时间">{{ parseTime(form.lastUpdateTime) }}</t-descriptions-item>
      </my-descriptions>
    </t-dialog>

    <!-- 设计流程开始 -->
    <model-design ref="designRef" @close-call-back="handleQuery" />
    <!-- 设计流程结束 -->
  </t-card>
</template>

<script lang="ts" setup>
import {
  AddIcon,
  BrowseIcon,
  CloudUploadIcon,
  DeleteIcon,
  Download1Icon,
  DownloadIcon,
  EditIcon,
  GestureUpIcon,
  RefreshIcon,
  SearchIcon,
  SettingIcon,
} from 'tdesign-icons-vue-next';

defineOptions({
  name: 'ActReModel',
});
import type {
  FormInstanceFunctions,
  FormRule,
  PageInfo,
  PrimaryTableCol,
  SubmitContext,
  TreeNodeModel,
} from 'tdesign-vue-next';
import { computed, ref } from 'vue';

import { listCategory } from '@/api/workflow/category';
import { addModel, delModel, getModelInfo, listModel, modelDeploy, updateModel } from '@/api/workflow/model';
import type { WfCategoryVo } from '@/api/workflow/model/categoryModel';
import type { ModelForm, ModelQuery, ModelVO } from '@/api/workflow/model/types';
import { ArrayOps } from '@/utils/array';

import ModelDesign from './design.vue';

const { proxy } = getCurrentInstance();

const formRef = ref<FormInstanceFunctions>();
const designRef = ref<InstanceType<typeof ModelDesign>>();

const open = ref(false);
const title = ref('');
const openView = ref(false);
const openViewLoading = ref(false);
const buttonLoading = ref(false);
const loadingTree = ref(false);
const loading = ref(false);
const ids = ref<string[]>([]);
const single = ref(true);
const multiple = ref(true);
const showSearch = ref(true);
const columnControllerVisible = ref(false);
const total = ref(0);
const modelList = ref<ModelVO[]>([]);
const categoryDict = ref<WfCategoryVo[]>([]);
const categoryOptions = ref<WfCategoryVo[]>([]);
const categoryName = ref('');
const treeActived = ref<string[]>([]);
const expandedTree = ref<string[]>([]);

const initFormData: ModelForm = {
  id: '',
  name: '',
  key: '',
  categoryCode: '',
  xml: '',
  svg: '',
  description: '',
};
// 提交表单对象
const form = ref<Partial<ModelVO & ModelForm>>({});
// 查询对象
const queryParams = ref<ModelQuery>({
  pageNum: 1,
  pageSize: 10,
  name: '',
  key: '',
  categoryCode: '',
});

// 校验规则
const rules = ref<Record<string, Array<FormRule>>>({
  name: [{ required: true, message: '模型不能为空' }],
  key: [{ required: true, message: '模型KEY不能为空' }],
  categoryCode: [{ required: true, message: '流程分类不能为空' }],
});
// 列显隐信息
const columns = ref<Array<PrimaryTableCol>>([
  { title: `选择列`, colKey: 'row-select', type: 'multiple', width: 30, align: 'center' },
  { title: `模型名称`, colKey: 'name', ellipsis: true, align: 'center' },
  { title: `模型KEY`, colKey: 'key', ellipsis: true, align: 'center' },
  { title: `版本号`, colKey: 'version', align: 'center' },
  { title: `备注说明`, colKey: 'metaInfo', align: 'center' },
  { title: `更新时间`, colKey: 'lastUpdateTime', align: 'center', width: '10%', minWidth: 112 },
  { title: `创建时间`, colKey: 'createTime', align: 'center', width: '10%', minWidth: 112 },
  { title: `操作`, colKey: 'operation', align: 'center' },
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
const handleSelectionChange = (selection: Array<string | number>) => {
  ids.value = selection as string[];
  single.value = selection.length !== 1;
  multiple.value = !selection.length;
};
// 分页
const getList = async () => {
  loading.value = true;
  queryParams.value.categoryCode = treeActived.value.at(0);
  const resp = await listModel(queryParams.value);
  modelList.value = resp.rows;
  total.value = resp.total;
  loading.value = false;
};
/** 详情按钮操作 */
function handleDetail(row: ModelVO) {
  reset();
  openView.value = true;
  openViewLoading.value = true;
  const id = row.id;
  getModelInfo(id).then((response) => {
    form.value = response.data;
    openViewLoading.value = false;
  });
}
/** 删除按钮操作 */
const handleDelete = async (row?: ModelVO) => {
  const id = row?.id || ids.value;
  proxy?.$modal.confirm(`是否确认删除模型id为【${id}】的数据项？`, () => {
    const msgLoading = proxy.$modal.msgLoading('正在删除中...');
    return delModel(id)
      .then(() => {
        ids.value = ArrayOps.fastDeleteElement(ids.value, id);
        getList();
        proxy.$modal.msgSuccess('删除成功');
      })
      .finally(() => {
        proxy.$modal.msgClose(msgLoading);
      });
  });
};
// 流程部署
const clickDeploy = async (id: string, key: string) => {
  proxy?.$modal.confirm(`是否部署模型key为【${key}】流程？`, () => {
    const msgLoading = proxy.$modal.msgLoading('正在部署模型中...');
    modelDeploy(id)
      .then(() => {
        getList();
        proxy.$modal.msgSuccess('部署成功');
      })
      .finally(() => {
        proxy.$modal.msgClose(msgLoading);
      });
  });
};
const handleAdd = () => {
  reset();
  getTreeselect();
  open.value = true;
  title.value = '新增模型';
};
const handleUpdate = async (row?: ModelVO) => {
  buttonLoading.value = true;
  reset();
  open.value = true;
  title.value = '修改模型';
  const id = row?.id || ids.value.at(0);
  await getTreeselect();
  getModelInfo(id).then((response) => {
    buttonLoading.value = false;
    form.value = response.data;
  });
};

/** 提交表单 */
function submitForm({ validateResult, firstError }: SubmitContext) {
  if (validateResult === true) {
    buttonLoading.value = true;
    const msgLoading = proxy.$modal.msgLoading('提交中...');
    if (form.value.id) {
      updateModel(form.value)
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
      initXml(form.value.key, form.value.name);
      form.value.xml = xml.value;
      addModel(form.value)
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

/** 表单重置 */
const reset = () => {
  form.value = { ...initFormData };
  proxy.resetForm('formRef');
};

// 打开设计流程
const clickDesign = async (id: string) => {
  await designRef.value.open(id);
};
// 导出流程模型
const clickExportZip = (data?: ModelVO) => {
  proxy?.$download.zip(`/workflow/model/export/zip/${data?.id}`, `${data?.name}-${data?.key}`);
};

/** 查询流程分类下拉树结构 */
async function getTreeselect() {
  return listCategory().then((response) => {
    categoryDict.value = [
      { categoryCode: 'ALL', categoryName: '顶级节点' },
      ...response.data.map((item) => ({ categoryCode: item.categoryCode, categoryName: item.categoryName })),
    ];
    categoryOptions.value = [
      { categoryCode: 'ALL', categoryName: '顶级节点', children: proxy.handleTree(response.data, 'id', 'parentId') },
    ];
  });
}

const xml = ref<string>('');

const initXml = async (key: string, name: string) => {
  xml.value = `<?xml version="1.0" encoding="UTF-8"?>
<definitions xmlns="http://www.omg.org/spec/BPMN/20100524/MODEL" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:bpmndi="http://www.omg.org/spec/BPMN/20100524/DI" xmlns:omgdc="http://www.omg.org/spec/DD/20100524/DC" xmlns:bioc="http://bpmn.io/schema/bpmn/biocolor/1.0" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:flowable="http://flowable.org/bpmn" targetNamespace="http://www.flowable.org/processdef">
  <process id="${key}" name="${name}">
    <startEvent id="startNode1" name="开始" />
  </process>
  <bpmndi:BPMNDiagram id="BPMNDiagram_flow">
    <bpmndi:BPMNPlane id="BPMNPlane_flow" bpmnElement="T-2d89e7a3-ba79-4abd-9f64-ea59621c258c">
      <bpmndi:BPMNShape id="BPMNShape_startNode1" bpmnElement="startNode1" bioc:stroke="">
        <omgdc:Bounds x="240" y="200" width="30" height="30" />
        <bpmndi:BPMNLabel>
          <omgdc:Bounds x="242" y="237" width="23" height="14" />
        </bpmndi:BPMNLabel>
      </bpmndi:BPMNShape>
    </bpmndi:BPMNPlane>
  </bpmndi:BPMNDiagram>
</definitions>`;
  return xml;
};
</script>
