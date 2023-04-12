<template>
  <t-card>
    <t-space direction="vertical">
      <t-form v-show="showSearch" ref="queryRef" :data="queryParams" layout="inline">
        <t-form-item label="菜单名称" name="menuName">
          <t-input
            v-model="queryParams.menuName"
            placeholder="请输入菜单名称"
            clearable
            style="width: 200px"
            @keyup.enter="handleQuery"
          />
        </t-form-item>
        <t-form-item label="状态" name="status">
          <t-select v-model="queryParams.status" placeholder="菜单状态" clearable style="width: 200px">
            <t-option v-for="dict in sys_normal_disable" :key="dict.value" :label="dict.label" :value="dict.value" />
          </t-select>
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

      <t-enhanced-table
        ref="tableRef"
        v-model:column-controller-visible="columnControllerVisible"
        :loading="loading"
        :data="menuList"
        row-key="menuId"
        :columns="columns"
        :column-controller="{
          hideTriggerButton: true,
        }"
        :tree="{ childrenKey: 'children', treeNodeColumnIndex: 0, expandTreeNodeOnClick: true }"
      >
        <template #topContent>
          <t-row>
            <t-col flex="auto">
              <t-button v-hasPermi="['system:menu:add']" theme="primary" @click="handleAdd">
                <template #icon> <add-icon /></template>
                新增
              </t-button>
              <t-button theme="default" variant="outline" @click="toggleExpandAll">
                <template #icon> <unfold-more-icon /> </template>
                展开/折叠
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
        <template #icon="{ row }">
          <t-icon :name="row.icon" />
        </template>
        <template #status="{ row }">
          <dict-tag :options="sys_normal_disable" :value="row.status" />
        </template>
        <template #operation="{ row }">
          <t-space :size="8">
            <t-link v-hasPermi="['system:menu:edit']" theme="primary" hover="color" @click.stop="handleUpdate(row)">
              <edit-icon />修改
            </t-link>
            <t-link v-hasPermi="['system:menu:add']" theme="primary" hover="color" @click.stop="handleAdd(row)">
              <add-icon />新增
            </t-link>
            <t-link v-hasPermi="['system:menu:remove']" theme="danger" hover="color" @click.stop="handleDelete(row)">
              <delete-icon />删除
            </t-link>
          </t-space>
        </template>
      </t-enhanced-table>
    </t-space>

    <!-- 添加或修改菜单对话框 -->
    <t-dialog
      v-model:visible="open"
      :close-on-overlay-click="false"
      :header="title"
      width="680px"
      attach="body"
      @confirm="onConfirm"
    >
      <t-loading :loading="eLoading">
        <t-form
          ref="menuRef"
          label-align="right"
          label-width="calc(6em + 24px)"
          scroll-to-first-error="smooth"
          :data="form"
          :rules="rules"
          @submit="submitForm"
        >
          <t-row :gutter="[0, 20]">
            <t-col :span="12">
              <t-form-item label="上级菜单">
                <t-tree-select
                  v-model="form.parentId"
                  :data="menuOptions"
                  :tree-props="{
                    keys: { value: 'menuId', label: 'menuName', children: 'children' },
                    checkStrictly: true,
                  }"
                  placeholder="选择上级菜单"
                />
              </t-form-item>
            </t-col>
            <t-col :span="12">
              <t-form-item label="菜单类型" name="menuType">
                <t-radio-group v-model="form.menuType">
                  <t-radio value="M">目录</t-radio>
                  <t-radio value="C">菜单</t-radio>
                  <t-radio value="F">按钮</t-radio>
                </t-radio-group>
              </t-form-item>
            </t-col>
            <t-col v-if="form.menuType !== 'F'" :span="12">
              <t-form-item label="菜单图标" name="icon">
                <icon-select v-model="form.icon" />
              </t-form-item>
            </t-col>
            <t-col :span="6">
              <t-form-item label="菜单名称" name="menuName">
                <t-input v-model="form.menuName" placeholder="请输入菜单名称" />
              </t-form-item>
            </t-col>
            <t-col :span="6">
              <t-form-item label="显示排序" name="orderNum">
                <t-input-number v-model="form.orderNum" :min="0" />
              </t-form-item>
            </t-col>
            <t-col v-if="form.menuType !== 'F'" :span="6">
              <t-form-item>
                <template #label>
                  <span>
                    <t-tooltip content="选择是外链则路由地址需要以`http(s)://`开头" placement="top">
                      <help-circle-filled-icon /></t-tooltip
                    >是否外链
                  </span>
                </template>
                <t-radio-group v-model="form.isFrame">
                  <t-radio :value="0">是</t-radio>
                  <t-radio :value="1">否</t-radio>
                </t-radio-group>
              </t-form-item>
            </t-col>
            <t-col v-if="form.menuType !== 'F'" :span="6">
              <t-form-item name="path">
                <template #label>
                  <span>
                    <t-tooltip
                      content="访问的路由地址，如：`user`，如外网地址需内链访问则以`http(s)://`开头"
                      placement="top"
                    >
                      <help-circle-filled-icon />
                    </t-tooltip>
                    路由地址
                  </span>
                </template>
                <t-input v-model="form.path" placeholder="请输入路由地址" />
              </t-form-item>
            </t-col>
            <t-col v-if="form.menuType === 'C'" :span="6">
              <t-form-item name="component">
                <template #label>
                  <span>
                    <t-tooltip content="访问的组件路径，如：`system/user/index`，默认在`views`目录下" placement="top">
                      <help-circle-filled-icon />
                    </t-tooltip>
                    组件路径
                  </span>
                </template>
                <t-input v-model="form.component" placeholder="请输入组件路径" />
              </t-form-item>
            </t-col>
            <t-col v-if="form.menuType !== 'M'" :span="6">
              <t-form-item>
                <t-input v-model="form.perms" placeholder="请输入权限标识" :maxlength="100" />
                <template #label>
                  <span>
                    <t-tooltip
                      content="控制器中定义的权限字符，如：@SaCheckPermission('system:user:list')"
                      placement="top"
                    >
                      <help-circle-filled-icon />
                    </t-tooltip>
                    权限字符
                  </span>
                </template>
              </t-form-item>
            </t-col>
            <t-col v-if="form.menuType === 'C'" :span="6">
              <t-form-item>
                <t-input v-model="form.queryParam" placeholder="请输入路由参数" :maxlength="255" />
                <template #label>
                  <span>
                    <t-tooltip content='访问路由的默认传递参数，如：`{"id": 1, "name": "ry"}`' placement="top">
                      <help-circle-filled-icon />
                    </t-tooltip>
                    路由参数
                  </span>
                </template>
              </t-form-item>
            </t-col>
            <t-col v-if="form.menuType === 'C'" :span="6">
              <t-form-item>
                <template #label>
                  <span>
                    <t-tooltip
                      content="选择是则会被`keep-alive`缓存，需要匹配组件的`name`和地址保持一致"
                      placement="top"
                    >
                      <help-circle-filled-icon />
                    </t-tooltip>
                    是否缓存
                  </span>
                </template>
                <t-radio-group v-model="form.isCache">
                  <t-radio :value="0">缓存</t-radio>
                  <t-radio :value="1">不缓存</t-radio>
                </t-radio-group>
              </t-form-item>
            </t-col>
            <t-col v-if="form.menuType !== 'F'" :span="6">
              <t-form-item>
                <template #label>
                  <span>
                    <t-tooltip content="选择隐藏则路由将不会出现在侧边栏，但仍然可以访问" placement="top">
                      <help-circle-filled-icon />
                    </t-tooltip>
                    显示状态
                  </span>
                </template>
                <t-radio-group v-model="form.visible">
                  <t-radio v-for="dict in sys_show_hide" :key="dict.value" :value="dict.value">{{
                    dict.label
                  }}</t-radio>
                </t-radio-group>
              </t-form-item>
            </t-col>
            <t-col v-if="form.menuType !== 'F'" :span="6">
              <t-form-item>
                <template #label>
                  <span>
                    <t-tooltip content="选择停用则路由将不会出现在侧边栏，也不能被访问" placement="top">
                      <help-circle-filled-icon />
                    </t-tooltip>
                    菜单状态
                  </span>
                </template>
                <t-radio-group v-model="form.status">
                  <t-radio v-for="dict in sys_normal_disable" :key="dict.value" :value="dict.value">{{
                    dict.label
                  }}</t-radio>
                </t-radio-group>
              </t-form-item>
            </t-col>
          </t-row>
        </t-form>
      </t-loading>
    </t-dialog>
  </t-card>
</template>
<script lang="ts">
export default {
  name: 'Menus',
};
</script>
<script lang="ts" setup>
import {
  AddIcon,
  DeleteIcon,
  EditIcon,
  HelpCircleFilledIcon,
  RefreshIcon,
  SearchIcon,
  SettingIcon,
  UnfoldMoreIcon,
} from 'tdesign-icons-vue-next';
import { getCurrentInstance, ref } from 'vue';
import { EnhancedTableInstanceFunctions, FormInstanceFunctions, FormRule, PrimaryTableCol } from 'tdesign-vue-next';
import { addMenu, delMenu, getMenu, listMenu, updateMenu } from '@/api/system/menu';
import IconSelect from '@/components/icon-select/index.vue';
import { SysMenuForm, SysMenuQuery, SysMenuVo } from '@/api/system/model/menuModel';

const { proxy } = getCurrentInstance();
const { sys_show_hide, sys_normal_disable } = proxy.useDict('sys_show_hide', 'sys_normal_disable');

const menuList = ref<SysMenuVo[]>([]);
const open = ref(false);
const loading = ref(true);
const eLoading = ref(true);
const showSearch = ref(true);
const columnControllerVisible = ref(false);
const title = ref('');
const menuOptions = ref([]);
const isExpandAll = ref(false);
const tableRef = ref<EnhancedTableInstanceFunctions>(null);
const menuRef = ref<FormInstanceFunctions>(null);

const rules = ref<Record<string, Array<FormRule>>>({
  menuName: [{ required: true, message: '菜单名称不能为空', trigger: 'blur' }],
  orderNum: [{ required: true, message: '菜单顺序不能为空', trigger: 'blur' }],
  path: [{ required: true, message: '路由地址不能为空', trigger: 'blur' }],
});
// 列显隐信息
const columns = ref<Array<PrimaryTableCol>>([
  { title: `菜单名称`, colKey: 'menuName', align: 'left', ellipsis: true },
  { title: `图标`, colKey: 'icon', align: 'center', width: 100 },
  { title: `排序`, colKey: 'orderNum', align: 'center', width: 60 },
  { title: `权限标识`, colKey: 'perms', align: 'center', ellipsis: true },
  { title: `组件路径`, colKey: 'component', align: 'center', ellipsis: true },
  { title: `状态`, colKey: 'status', align: 'center', width: 80 },
  { title: `创建时间`, colKey: 'createTime', align: 'center' },
  { title: `操作`, colKey: 'operation', align: 'center', width: 180 },
]);

const form = ref<SysMenuForm & SysMenuVo>({
  menuId: undefined,
  parentId: 0,
  menuName: undefined,
  icon: undefined,
  menuType: 'M',
  orderNum: 0,
  isFrame: 1,
  isCache: 0,
  visible: '0',
  status: '0',
});

const queryParams = ref<SysMenuQuery>({
  menuName: undefined,
  visible: undefined,
});

/** 查询菜单列表 */
function getList() {
  loading.value = true;
  listMenu(queryParams.value).then((response) => {
    menuList.value = proxy.handleTree(response.data, 'menuId');
    tableRef.value.resetData(menuList.value);
    loading.value = false;
    refreshExpandAll();
  });
}
/** 查询菜单下拉树结构 */
function getTreeselect() {
  menuOptions.value = [];
  return listMenu().then((response) => {
    const menu = { menuId: 0, menuName: '主类目', children: [] };
    menu.children = proxy.handleTree(response.data, 'menuId');
    menuOptions.value = [menu];
  });
}
/** 表单重置 */
function reset() {
  form.value = {
    menuId: undefined,
    parentId: 0,
    menuName: undefined,
    icon: undefined,
    menuType: 'M',
    orderNum: 0,
    isFrame: 1,
    isCache: 0,
    visible: '0',
    status: '0',
  };
  proxy.resetForm('menuRef');
}
/** 搜索按钮操作 */
function handleQuery() {
  getList();
}
/** 重置按钮操作 */
function resetQuery() {
  proxy.resetForm('queryRef');
  handleQuery();
}
/** 新增按钮操作 */
function handleAdd(row) {
  reset();
  open.value = true;
  title.value = '添加菜单';
  eLoading.value = true;
  getTreeselect().then(() => {
    if (row != null && row.menuId) {
      form.value.parentId = row.menuId;
    } else {
      form.value.parentId = 0;
    }
    eLoading.value = false;
  });
}
/** 展开/折叠操作 */
function toggleExpandAll() {
  isExpandAll.value = !isExpandAll.value;
  refreshExpandAll();
}

/** 刷新展开状态 */
function refreshExpandAll() {
  proxy.$nextTick(() => {
    if (isExpandAll.value) {
      tableRef.value.expandAll();
    } else {
      tableRef.value.foldAll();
    }
  });
}
/** 修改按钮操作 */
async function handleUpdate(row) {
  reset();
  await getTreeselect();
  title.value = '修改菜单';
  open.value = true;
  eLoading.value = true;
  getMenu(row.menuId).then((response) => {
    form.value = response.data;
    eLoading.value = false;
  });
}
const onConfirm = () => {
  menuRef.value.submit();
};
/** 提交按钮 */
function submitForm({ validateResult, firstError }) {
  if (validateResult === true) {
    const msgLoading = proxy.$modal.msgLoading('提交中...');
    if (form.value.menuId) {
      updateMenu(form.value)
        .then(() => {
          proxy.$modal.msgSuccess('修改成功');
          open.value = false;
          getList();
        })
        .finally(() => proxy.$modal.msgClose(msgLoading));
    } else {
      addMenu(form.value)
        .then(() => {
          proxy.$modal.msgSuccess('新增成功');
          open.value = false;
          getList();
        })
        .finally(() => proxy.$modal.msgClose(msgLoading));
    }
  } else {
    proxy.$modal.msgError(firstError);
  }
}
/** 删除按钮操作 */
function handleDelete(row) {
  proxy.$modal.confirm(`是否确认删除名称为"${row.menuName}"的数据项?`, () => {
    const msgLoading = proxy.$modal.msgLoading('正在删除中...');
    return delMenu(row.menuId)
      .then(() => {
        getList();
        proxy.$modal.msgSuccess('删除成功');
      })
      .finally(() => proxy.$modal.msgClose(msgLoading));
  });
}

getList();
</script>
