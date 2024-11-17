<template>
  <div>
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
    <my-scrollbar>
      <div class="head-container h70vh">
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
            @active="emit('active')"
          />
        </t-loading>
      </div>
    </my-scrollbar>
  </div>
</template>

<script setup lang="ts">
import { RefreshIcon, SearchIcon } from 'tdesign-icons-vue-next';
import type { TreeNodeModel } from 'tdesign-vue-next';
import { computed, ref } from 'vue';

import { listCategory } from '@/api/workflow/category';
import type { WfCategoryVo } from '@/api/workflow/model/categoryModel';

const { proxy } = getCurrentInstance();
const loadingTree = ref(false);
const categoryOptions = ref<WfCategoryVo[]>([]);
const categoryName = ref('');
const expandedTree = ref<string[]>([]);
const treeActived = defineModel<string[]>({
  default: () => [],
});
const emit = defineEmits(['active']);

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
async function getTreeselect() {
  return listCategory().then((response) => {
    categoryOptions.value = [
      { categoryCode: 'ALL', categoryName: '顶级节点', children: proxy.handleTree(response.data, 'id', 'parentId') },
    ];
  });
}

onMounted(() => {
  getTreeselect().then(() => triggerExpandedTree());
});
</script>

<style scoped lang="less"></style>
