<template>
  <t-card>
    <t-space direction="vertical">
      <t-form v-show="showSearch" ref="queryRef" :data="queryParams" layout="inline" label-width="68px">
        <t-form-item label="套餐名称" name="packageName">
          <t-input v-model="queryParams.packageName" placeholder="请输入套餐名称" clearable @enter="handleQuery" />
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
        row-key="packageId"
        :data="tenantPackageList"
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
              <t-button v-hasPermi="['system:tenantPackage:add']" theme="primary" @click="handleAdd">
                <template #icon> <add-icon /></template>
                新增
              </t-button>
              <t-button
                v-hasPermi="['system:tenantPackage:edit']"
                theme="default"
                variant="outline"
                :disabled="single"
                @click="handleUpdate"
              >
                <template #icon> <edit-icon /> </template>
                修改
              </t-button>
              <t-button
                v-hasPermi="['system:tenantPackage:remove']"
                theme="danger"
                variant="outline"
                :disabled="multiple"
                @click="handleDelete"
              >
                <template #icon> <delete-icon /> </template>
                删除
              </t-button>
              <t-button
                v-hasPermi="['system:tenantPackage:export']"
                theme="default"
                variant="outline"
                @click="handleExport"
              >
                <template #icon> <download-icon /> </template>
                导出
              </t-button>
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
        <template #status="{ row }">
          <t-switch v-model="row.status" :custom-value="['1', '0']" @click.stop @change="handleStatusChange(row)" />
        </template>
        <template #operation="{ row }">
          <t-space :size="8">
            <t-link
              v-hasPermi="['system:tenantPackage:query']"
              theme="primary"
              hover="color"
              @click.stop="handleDetail(row)"
            >
              <browse-icon />详情
            </t-link>
            <t-link
              v-hasPermi="['system:tenantPackage:edit']"
              theme="primary"
              hover="color"
              @click.stop="handleUpdate(row)"
            >
              <edit-icon />修改
            </t-link>
            <t-link
              v-hasPermi="['system:tenantPackage:remove']"
              theme="danger"
              hover="color"
              @click.stop="handleDelete(row)"
            >
              <delete-icon />删除
            </t-link>
          </t-space>
        </template>
      </t-table>
    </t-space>

    <!-- 添加或修改租户套餐对话框 -->
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
      @confirm="onConfirm"
    >
      <t-form
        ref="tenantPackageRef"
        label-align="right"
        :data="form"
        :rules="rules"
        label-width="calc(5em + 24px)"
        scroll-to-first-error="smooth"
        @submit="submitForm"
      >
        <t-form-item label="套餐名称" name="packageName">
          <t-input v-model="form.packageName" placeholder="请输入套餐名称" />
        </t-form-item>
        <t-form-item label="关联菜单" name="menuIds">
          <t-space direction="vertical">
            <t-space>
              <t-checkbox v-model="menuExpand" @change="handleCheckedTreeExpand($event, 'menu')">展开/折叠</t-checkbox>
              <t-checkbox v-model="menuNodeAll" @change="handleCheckedTreeNodeAll($event, 'menu')"
                >全选/全不选</t-checkbox
              >
              <t-checkbox v-model="form.menuCheckStrictly">父子联动</t-checkbox>
            </t-space>
            <t-tree
              ref="menuRef"
              v-model="menuIds"
              class="tree-border"
              :data="menuOptions"
              checkable
              :expanded="menuExpandNode"
              :check-strictly="!form.menuCheckStrictly"
              empty="加载中，请稍候"
              :keys="{ value: 'id', label: 'label', children: 'children' }"
              @expand="onExpand('menu', $event)"
            ></t-tree>
          </t-space>
        </t-form-item>
        <t-form-item label="备注" name="remark">
          <t-input v-model="form.remark" placeholder="请输入备注" />
        </t-form-item>
      </t-form>
    </t-dialog>

    <!-- 租户套餐详情 -->
    <t-dialog v-model:visible="openView" header="租户套餐详情" width="700px" attach="body" :footer="false">
      <t-loading :loading="openViewLoading">
        <t-form label-align="right" colon :data="form" label-width="calc(5em + 24px)">
          <t-row :gutter="[0, 20]">
            <t-col :span="6">
              <t-form-item label="租户套餐id">{{ form.packageId }}</t-form-item>
            </t-col>
            <t-col :span="6">
              <t-form-item label="套餐名称">{{ form.packageName }}</t-form-item>
            </t-col>
            <t-col :span="6">
              <t-form-item label="备注">{{ form.remark }}</t-form-item>
            </t-col>
            <t-col :span="6">
              <t-form-item label="状态">
                <dict-tag :options="sys_normal_disable" :value="form.status" />
              </t-form-item>
            </t-col>
            <t-col :span="6">
              <t-form-item label="创建时间">{{ parseTime(form.createTime) }}</t-form-item>
            </t-col>
            <t-col :span="6">
              <t-form-item label="更新时间">{{ parseTime(form.updateTime) }}</t-form-item>
            </t-col>
          </t-row>
        </t-form>
      </t-loading>
    </t-dialog>
  </t-card>
</template>
<script lang="ts">
export default {
  name: 'TenantPackage',
};
</script>
<script lang="ts" setup>
import {
  AddIcon,
  BrowseIcon,
  DeleteIcon,
  DownloadIcon,
  EditIcon,
  RefreshIcon,
  SearchIcon,
  SettingIcon,
} from 'tdesign-icons-vue-next';
import {
  FormInstanceFunctions,
  FormRule,
  PageInfo,
  PrimaryTableCol,
  SubmitContext,
  TreeInstanceFunctions,
} from 'tdesign-vue-next';
import { computed, getCurrentInstance, ref } from 'vue';

import { TreeModel } from '@/api/model/resultModel';
import { tenantPackageMenuTreeselect, treeselect as menuTreeselect } from '@/api/system/menu';
import { SysTenantPackageForm, SysTenantPackageQuery, SysTenantPackageVo } from '@/api/system/model/tenantPackageModel';
import {
  addTenantPackage,
  changePackageStatus,
  delTenantPackage,
  getTenantPackage,
  listTenantPackage,
  updateTenantPackage,
} from '@/api/system/tenantPackage';

const { proxy } = getCurrentInstance();
const { sys_normal_disable } = proxy.useDict('sys_normal_disable');

const tenantPackageList = ref<SysTenantPackageVo[]>([]);
const tenantPackageRef = ref<FormInstanceFunctions>(null);
const open = ref(false);
const openView = ref(false);
const openViewLoading = ref(false);
const buttonLoading = ref(false);
const loading = ref(false);
const columnControllerVisible = ref(false);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref('');
const menuRef = ref<TreeInstanceFunctions>(null);
const menuIds = ref<number[]>([]);
const menuExpand = ref(false);
const menuNodeAll = ref(false);
const menuOptions = ref<TreeModel<number>[]>([]);
const menuExpandNode = ref<number[]>([]);

// 校验规则
const rules = ref<Record<string, Array<FormRule>>>({
  packageName: [{ required: true, message: '套餐名称不能为空', trigger: 'blur' }],
});
// 列显隐信息
const columns = ref<Array<PrimaryTableCol>>([
  { title: `选择列`, colKey: 'row-select', type: 'multiple', width: 50, align: 'center' },
  { title: `套餐名称`, colKey: 'packageName', align: 'center' },
  { title: `备注`, colKey: 'remark', align: 'center' },
  { title: `状态`, colKey: 'status', align: 'center' },
  { title: `创建时间`, colKey: 'createTime', align: 'center', width: 180 },
  { title: `更新时间`, colKey: 'updateTime', align: 'center', width: 180 },
  { title: `操作`, colKey: 'operation', align: 'center', width: 180 },
]);
// 提交表单对象
const form = ref<SysTenantPackageVo & SysTenantPackageForm>({
  menuIds: [],
});
// 查询对象
const queryParams = ref<SysTenantPackageQuery>({
  pageNum: 1,
  pageSize: 10,
  params: {},
  packageName: undefined,
});

// 分页
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

/** 查询菜单树结构 */
function getMenuTreeselect() {
  menuTreeselect().then((response) => {
    menuOptions.value = response.data;
  });
}

/** 根据租户套餐ID查询菜单树结构 */
function getPackageMenuTreeselect(packageId: number) {
  return tenantPackageMenuTreeselect(packageId).then((response) => {
    menuOptions.value = response.data.menus;
    return response;
  });
}

/** 查询租户套餐列表 */
function getList() {
  loading.value = true;
  listTenantPackage(queryParams.value).then((response) => {
    tenantPackageList.value = response.rows;
    total.value = response.total;
    loading.value = false;
  });
}

function onExpand(type: string, value: number[]) {
  if (type === 'menu') {
    menuExpandNode.value = value;
  }
}

// 表单重置
function reset() {
  menuIds.value = [];
  menuExpand.value = false;
  menuNodeAll.value = false;
  form.value = {
    status: '1',
    menuIds: [],
  };
  proxy.resetForm('tenantPackageRef');
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}

/** 重置按钮操作 */
function resetQuery() {
  proxy.resetForm('queryRef');
  handleQuery();
}

// 多选框选中数据
function handleSelectionChange(selection: Array<string | number>) {
  ids.value = selection;
  single.value = selection.length !== 1;
  multiple.value = !selection.length;
}

// 租户套餐状态修改
function handleStatusChange(row: SysTenantPackageVo) {
  const data = tenantPackageList.value.find((value) => value.packageId === row.packageId);
  const text = data.status === '1' ? '启用' : '停用';
  proxy.$modal.confirm(
    `确认要"${text}""${data.packageName}"套餐吗？`,
    () => {
      const msgLoading = proxy.$modal.msgLoading('提交中...');
      return changePackageStatus(data.packageId, data.status)
        .then(() => {
          getList();
          proxy.$modal.msgSuccess(`${text}成功`);
        })
        .catch(() => {
          data.status = data.status === '0' ? '1' : '0';
        })
        .finally(() => proxy.$modal.msgClose(msgLoading));
    },
    () => {
      data.status = data.status === '0' ? '1' : '0';
    },
  );
}

/** 新增按钮操作 */
function handleAdd() {
  reset();
  open.value = true;
  title.value = '添加租户套餐';
  getMenuTreeselect();
}

/** 详情按钮操作 */
function handleDetail(row: SysTenantPackageVo) {
  reset();
  openView.value = true;
  openViewLoading.value = true;
  const packageId = row.packageId || ids.value.at(0);
  getTenantPackage(packageId).then((response) => {
    form.value = response.data;
    openViewLoading.value = false;
  });
}

/** 修改按钮操作 */
function handleUpdate(row: SysTenantPackageVo) {
  buttonLoading.value = true;
  reset();
  open.value = true;
  title.value = '修改租户套餐';
  const packageId = row.packageId || ids.value.at(0);
  getPackageMenuTreeselect(packageId).then((res) => {
    menuIds.value = res.data.checkedKeys;
  });
  getTenantPackage(packageId).then((response) => {
    buttonLoading.value = false;
    form.value = response.data;
  });
}

/** 树权限（展开/折叠） */
function handleCheckedTreeExpand(value: boolean, type: string) {
  if (type === 'menu') {
    if (value) {
      menuExpandNode.value = menuOptions.value.map((row) => row.id);
    } else {
      menuExpandNode.value = [];
    }
  }
}
/** 树权限（全选/全不选） */
function handleCheckedTreeNodeAll(value: boolean, type: string) {
  if (type === 'menu') {
    if (value) {
      menuIds.value = menuRef.value.getItems().map((item) => item.value as number);
    } else {
      menuIds.value = [];
    }
  }
}
/** 所有菜单节点数据 */
function getMenuAllCheckedKeys() {
  const items = menuRef.value.getItems();
  return items.filter((item) => item.checked || item.indeterminate).map((item) => item.value as number);
}

/** 提交按钮 */
function onConfirm() {
  tenantPackageRef.value.submit();
}

/** 提交表单 */
function submitForm({ validateResult, firstError }: SubmitContext) {
  if (validateResult === true) {
    buttonLoading.value = true;
    const msgLoading = proxy.$modal.msgLoading('提交中...');
    if (form.value.packageId) {
      form.value.menuIds = getMenuAllCheckedKeys();
      updateTenantPackage(form.value)
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
      form.value.menuIds = getMenuAllCheckedKeys();
      addTenantPackage(form.value)
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

/** 删除按钮操作 */
function handleDelete(row: SysTenantPackageVo) {
  const packageIds = row.packageId || ids.value;
  proxy.$modal.confirm(`是否确认删除租户套餐编号为${packageIds}的数据项？`, () => {
    loading.value = true;
    const msgLoading = proxy.$modal.msgLoading('正在删除中...');
    return delTenantPackage(packageIds)
      .then(() => {
        getList();
        proxy.$modal.msgSuccess('删除成功');
      })
      .finally(() => {
        loading.value = false;
        proxy.$modal.msgClose(msgLoading);
      });
  });
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download(
    'system/tenant/package/export',
    {
      ...queryParams.value,
    },
    `tenantPackage_${new Date().getTime()}.xlsx`,
  );
}

getList();
</script>
