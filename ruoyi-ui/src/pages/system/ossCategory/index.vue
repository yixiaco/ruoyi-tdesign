<template>
  <t-card>
    <t-row :gutter="20">
      <!--分类数据-->
      <t-col :sm="2" :xs="12">
        <div class="head-container">
          <t-input v-model="categoryName" placeholder="请输入分类名称" clearable style="margin-bottom: 20px">
            <template #prefixIcon>
              <search-icon />
            </template>
          </t-input>
        </div>
        <div class="head-container">
          <t-skeleton animation="gradient" :loading="loadingOptions">
            <t-tree
              ref="categoryTreeRef"
              v-model:actived="categoryActived"
              v-model:expanded="expandedCategoryArray"
              class="left-tree t-tree--block-node"
              :data="ossCategoryTree"
              :keys="{ value: 'ossCategoryId', label: 'categoryName', children: 'children' }"
              :filter="filterNode"
              activable
              hover
              line
              allow-fold-node-on-filter
              transition
            >
              <template #empty>
                <t-link
                  v-hasPermi="['system:ossCategory:add']"
                  theme="primary"
                  hover="color"
                  @click.stop="handleCategoryAdd()"
                >
                  新建分类
                </t-link>
              </template>
              <template #operations="{ node }">
                <t-dropdown placement="bottom" :max-column-width="120" :popup-props="{ showArrow: true }">
                  <t-button size="small" theme="primary" variant="text"> 操作 </t-button>
                  <t-dropdown-menu>
                    <t-dropdown-item
                      v-if="node.value !== 0"
                      content="查看详情"
                      @click="handleCategoryDetail(node.data)"
                    >
                      <template #prefix-icon><browse-icon /></template>
                    </t-dropdown-item>
                    <t-dropdown-item content="新增子分类" @click="handleCategoryAdd(node.data)">
                      <template #prefix-icon><add-icon /></template>
                    </t-dropdown-item>
                    <t-dropdown-item
                      v-if="node.value !== 0"
                      content="前插分类"
                      @click="handleCategoryAdd(node.getParent()?.data, node.data.orderNum - 1)"
                    >
                      <template #prefix-icon><arrow-up-icon /></template>
                    </t-dropdown-item>
                    <t-dropdown-item
                      v-if="node.value !== 0"
                      content="后插分类"
                      @click="handleCategoryAdd(node.getParent()?.data, node.data.orderNum + 1)"
                    >
                      <template #prefix-icon><arrow-down-icon /></template>
                    </t-dropdown-item>
                    <t-dropdown-item
                      v-if="node.value !== 0"
                      content="编辑分类"
                      @click="handleCategoryUpdate(node.data)"
                    >
                      <template #prefix-icon><edit-icon /></template>
                    </t-dropdown-item>
                    <t-dropdown-item
                      v-if="node.value !== 0"
                      content="删除分类"
                      theme="error"
                      @click="handleCategoryDelete(node.data)"
                    >
                      <template #prefix-icon><delete-icon /></template>
                    </t-dropdown-item>
                  </t-dropdown-menu>
                </t-dropdown>
              </template>
            </t-tree>
          </t-skeleton>
        </div>
      </t-col>
      <!-- 文件数据 -->
      <t-col :sm="10" :xs="12">
        <my-oss
          :category-id="categoryActived[0]"
          :suffixes="suffixes"
          @change="(selectValues) => emit('change', selectValues)"
        />
      </t-col>
    </t-row>

    <!-- 添加或修改OSS分类对话框 -->
    <t-dialog
      v-model:visible="open"
      :close-on-overlay-click="false"
      :header="title"
      width="500px"
      attach="body"
      :confirm-btn="{
        content: '确 定',
        theme: 'primary',
        loading: buttonLoading,
      }"
      @confirm="onConfirmCategory"
    >
      <t-form
        ref="ossCategoryRef"
        :data="form"
        :rules="rules"
        label-align="right"
        label-width="calc(5em + 24px)"
        scroll-to-first-error="smooth"
        @submit="submitCategoryForm"
      >
        <t-form-item label="分类名称" name="categoryName">
          <t-input v-model="form.categoryName" placeholder="请输入分类名称" clearable :maxlength="50" />
        </t-form-item>
        <t-form-item label="父级分类" name="parentId">
          <t-tree-select
            v-model="form.parentId"
            :data="ossCategoryOptions"
            :tree-props="{
              keys: { value: 'ossCategoryId', label: 'categoryName', children: 'children' },
              checkStrictly: true,
            }"
            placeholder="请选择父级分类"
          />
        </t-form-item>
        <t-form-item label="显示顺序" name="orderNum">
          <t-input-number v-model="form.orderNum" placeholder="请输入" />
        </t-form-item>
      </t-form>
    </t-dialog>

    <!-- OSS分类详情 -->
    <t-dialog v-model:visible="openView" header="OSS分类详情" width="700px" attach="body" :footer="false">
      <t-loading :loading="openViewLoading">
        <t-form label-align="right" colon label-width="calc(5em + 24px)">
          <t-row :gutter="[0, 20]">
            <t-col :span="6">
              <t-form-item label="oss分类id">{{ form.ossCategoryId }}</t-form-item>
            </t-col>
            <t-col :span="6">
              <t-form-item label="分类名称">{{ form.categoryName }}</t-form-item>
            </t-col>
            <t-col :span="6">
              <t-form-item label="父级分类id">{{ form.parentId }}</t-form-item>
            </t-col>
            <t-col :span="6">
              <t-form-item label="父级分类">{{ form.parentCategoryName }}</t-form-item>
            </t-col>
            <t-col :span="12">
              <t-form-item label="分类路径">{{ form.categoryPath }}</t-form-item>
            </t-col>
            <t-col :span="6">
              <t-form-item label="层级">{{ form.level }}</t-form-item>
            </t-col>
            <t-col :span="6">
              <t-form-item label="显示顺序">{{ form.orderNum }}</t-form-item>
            </t-col>
            <t-col :span="6">
              <t-form-item label="更新时间">{{ parseTime(form.updateTime) }}</t-form-item>
            </t-col>
            <t-col :span="6">
              <t-form-item label="创建时间">{{ parseTime(form.createTime) }}</t-form-item>
            </t-col>
          </t-row>
        </t-form>
      </t-loading>
    </t-dialog>
  </t-card>
</template>
<script lang="ts" setup>
import { PropType } from 'vue/dist/vue';

defineOptions({
  name: 'OssCategory',
});
import {
  AddIcon,
  ArrowDownIcon,
  ArrowUpIcon,
  BrowseIcon,
  DeleteIcon,
  EditIcon,
  SearchIcon,
} from 'tdesign-icons-vue-next';
import { FormInstanceFunctions, FormRule, SubmitContext, TreeNodeModel, TreeNodeValue } from 'tdesign-vue-next';
import { getCurrentInstance, ref } from 'vue';

import { SysOssCategoryForm, SysOssCategoryVo } from '@/api/system/model/ossCategoryModel';
import { SysOssVo } from '@/api/system/model/ossModel';
import {
  addOssCategory,
  delOssCategory,
  getOssCategory,
  listOssCategory,
  updateOssCategory,
} from '@/api/system/ossCategory';

import MyOss from './components/myOss.vue';

defineProps({
  suffixes: {
    type: Array as PropType<string[]>,
    default: () => [],
  },
});

const { proxy } = getCurrentInstance();

const openView = ref(false);
const openViewLoading = ref(false);
const loadingOptions = ref(false);
const ossCategoryTree = ref<SysOssCategoryVo[]>([]);
const ossCategoryOptions = ref<SysOssCategoryVo[]>([]);
const expandedCategoryArray = ref<TreeNodeValue[]>([]);
const open = ref(false);
const buttonLoading = ref(false);
const title = ref('');
const ossCategoryRef = ref<FormInstanceFunctions>(null);
const categoryName = ref('');
const categoryActived = ref<number[]>([]);

const rules = ref<Record<string, Array<FormRule>>>({
  categoryName: [
    { required: true, message: '分类名称不能为空', trigger: 'blur' },
    { pattern: /^[^/%_*]*$/, message: '分类名不能包含下列任何字符：/%_*' },
  ],
  parentId: [{ required: true, message: '父级分类不能为空', trigger: 'blur' }],
  orderNum: [{ required: true, message: '显示顺序不能为空', trigger: 'blur' }],
});
// 提交表单对象
const form = ref<SysOssCategoryVo & SysOssCategoryForm>({});
const emit = defineEmits<{
  (e: 'change', selectValues: SysOssVo[]): void;
}>();

/** 通过条件过滤节点  */
function filterNode(node: TreeNodeModel) {
  if (!node.value || !categoryName.value) return true;
  return node.label.indexOf(categoryName.value) >= 0;
}

// 表单重置
function reset() {
  form.value = {
    orderNum: 0,
  };
  proxy.resetForm('ossCategoryRef');
}

/** 新增分类按钮操作 */
function handleCategoryAdd(row?: SysOssCategoryVo, orderNum?: number) {
  reset();
  getCategoryOptions();
  if (row != null && row.ossCategoryId) {
    form.value.parentId = row.ossCategoryId;
  } else {
    form.value.parentId = 0;
  }
  if (orderNum) {
    form.value.orderNum = orderNum;
  }
  open.value = true;
  title.value = '添加文件分类';
}

/** 分类详情按钮操作 */
function handleCategoryDetail(row: SysOssCategoryVo) {
  reset();
  openView.value = true;
  openViewLoading.value = true;
  const ossCategoryId = row.ossCategoryId;
  getOssCategory(ossCategoryId).then((response) => {
    form.value = response.data;
    openViewLoading.value = false;
  });
}

/** 修改分类按钮操作 */
async function handleCategoryUpdate(row?: SysOssCategoryVo) {
  buttonLoading.value = true;
  reset();
  open.value = true;
  title.value = '修改文件分类';
  await getCategoryOptions();
  if (row != null) {
    form.value.parentId = row.ossCategoryId;
  }
  getOssCategory(row.ossCategoryId).then((response) => {
    buttonLoading.value = false;
    form.value = response.data;
  });
}

/** 查询OSS分类下拉树选项结构 */
async function getCategoryOptions() {
  const response = await listOssCategory();
  ossCategoryOptions.value = [
    {
      ossCategoryId: 0,
      categoryName: '根分类',
      children: proxy.handleTree(response.data, 'ossCategoryId', 'parentId'),
    },
  ];
}

/** 查询OSS分类树结构 */
function getCategoryTree() {
  loadingOptions.value = true;
  listOssCategory()
    .then((response) => {
      ossCategoryTree.value = proxy.handleTree(response.data, 'ossCategoryId', 'parentId');
    })
    .finally(() => (loadingOptions.value = false));
}

/** 提交分类按钮 */
function onConfirmCategory() {
  ossCategoryRef.value.submit();
}

/** 提交分类表单 */
function submitCategoryForm({ validateResult, firstError }: SubmitContext) {
  if (validateResult === true) {
    buttonLoading.value = true;
    const msgLoading = proxy.$modal.msgLoading('提交中...');
    if (form.value.ossCategoryId) {
      updateOssCategory(form.value)
        .then(() => {
          proxy.$modal.msgSuccess('修改成功');
          open.value = false;
          getCategoryTree();
        })
        .finally(() => {
          buttonLoading.value = false;
          proxy.$modal.msgClose(msgLoading);
        });
    } else {
      addOssCategory(form.value)
        .then(() => {
          proxy.$modal.msgSuccess('新增成功');
          open.value = false;
          getCategoryTree();
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

/** 删除分类按钮操作 */
function handleCategoryDelete(row: SysOssCategoryVo) {
  proxy.$modal.confirm(`是否确认删除【${row.categoryName}】分类？`, () => {
    const msgLoading = proxy.$modal.msgLoading('正在删除中...');
    return delOssCategory(row.ossCategoryId)
      .then(() => {
        categoryActived.value = categoryActived.value.filter((value) => value !== row.ossCategoryId);
        getCategoryTree();
        proxy.$modal.msgSuccess('删除成功');
      })
      .finally(() => {
        proxy.$modal.msgClose(msgLoading);
      });
  });
}

getCategoryTree();
</script>
<style lang="less" scoped>
.left-tree {
  :deep(.t-tree__empty) {
    display: flex;
    justify-content: center;
    min-height: 200px;
  }
}
</style>
