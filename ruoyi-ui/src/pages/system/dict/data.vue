<template>
  <t-card>
    <t-space direction="vertical" style="width: 100%">
      <t-form v-show="showSearch" ref="queryRef" :data="queryParams" layout="inline">
        <t-form-item label="字典名称" name="dictType">
          <t-select v-model="queryParams.dictType" style="width: 200px">
            <t-option v-for="item in typeOptions" :key="item.dictId" :label="item.dictName" :value="item.dictType" />
          </t-select>
        </t-form-item>
        <t-form-item label="字典标签" name="dictLabel">
          <t-input
            v-model="queryParams.dictLabel"
            placeholder="请输入字典标签"
            clearable
            style="width: 200px"
            @enter="handleQuery"
          />
        </t-form-item>
        <t-form-item label="状态" name="status">
          <t-select v-model="queryParams.status" placeholder="数据状态" clearable style="width: 200px">
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

      <t-table
        v-model:column-controller-visible="columnControllerVisible"
        hover
        :loading="loading"
        row-key="dictCode"
        :data="dataList"
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
              <t-button v-hasPermi="['system:dict:add']" theme="primary" @click="handleAdd">
                <template #icon> <add-icon /></template>
                新增
              </t-button>
              <t-button
                v-hasPermi="['system:dict:edit']"
                theme="default"
                variant="outline"
                :disabled="single"
                @click="handleUpdate()"
              >
                <template #icon> <edit-icon /> </template>
                修改
              </t-button>
              <t-button
                v-hasPermi="['system:dict:remove']"
                theme="danger"
                variant="outline"
                :disabled="multiple"
                @click="handleDelete()"
              >
                <template #icon> <delete-icon /> </template>
                删除
              </t-button>
              <t-button v-hasPermi="['system:dict:export']" theme="default" variant="outline" @click="handleExport">
                <template #icon> <download-icon /> </template>
                导出
              </t-button>
              <t-button theme="warning" variant="outline" @click="handleClose">
                <template #icon> <close-icon /> </template>
                关闭
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
        <template #dictLabel="{ row }">
          <span v-if="row.listClass === '' || row.listClass === 'default'">{{ row.dictLabel }}</span>
          <t-tag v-else :theme="row.listClass" variant="light">{{ row.dictLabel }}</t-tag>
        </template>
        <template #status="{ row }">
          <dict-tag :options="sys_normal_disable" :value="row.status" />
        </template>
        <template #operation="{ row }">
          <t-space :size="8">
            <t-link v-hasPermi="['system:dict:edit']" theme="primary" hover="color" @click.stop="handleUpdate(row)">
              <edit-icon />修改
            </t-link>
            <t-link v-hasPermi="['system:dict:remove']" theme="danger" hover="color" @click.stop="handleDelete(row)">
              <delete-icon />删除
            </t-link>
          </t-space>
        </template>
      </t-table>
    </t-space>

    <!-- 添加或修改参数配置对话框 -->
    <t-dialog
      v-model:visible="open"
      :close-on-overlay-click="false"
      :header="title"
      width="500px"
      attach="body"
      @confirm="onConfirm"
    >
      <t-loading :loading="eLoading">
        <t-form ref="dataRef" label-align="right" :data="form" :rules="rules" label-width="80px" @submit="submitForm">
          <t-form-item label="字典类型">
            <t-input v-model="form.dictType" :disabled="true" />
          </t-form-item>
          <t-form-item label="数据标签" name="dictLabel">
            <t-input v-model="form.dictLabel" placeholder="请输入数据标签" />
          </t-form-item>
          <t-form-item label="数据键值" name="dictValue">
            <t-input v-model="form.dictValue" placeholder="请输入数据键值" />
          </t-form-item>
          <t-form-item label="样式属性" name="cssClass">
            <t-input v-model="form.cssClass" placeholder="请输入样式属性" />
          </t-form-item>
          <t-form-item label="显示排序" name="dictSort">
            <t-input-number v-model="form.dictSort" :min="0" />
          </t-form-item>
          <t-form-item label="回显样式" name="listClass">
            <t-select v-model="form.listClass">
              <t-option
                v-for="item in listClassOptions"
                :key="item.value"
                :label="item.label + '(' + item.value + ')'"
                :value="item.value"
              ></t-option>
            </t-select>
          </t-form-item>
          <t-form-item label="状态" name="status">
            <t-radio-group v-model="form.status">
              <t-radio v-for="dict in sys_normal_disable" :key="dict.value" :value="dict.value">{{
                dict.label
              }}</t-radio>
            </t-radio-group>
          </t-form-item>
          <t-form-item label="备注" name="remark">
            <t-textarea v-model="form.remark" placeholder="请输入备注"></t-textarea>
          </t-form-item>
        </t-form>
      </t-loading>
    </t-dialog>
  </t-card>
</template>
<script lang="ts" setup>
defineOptions({
  name: 'DictData',
});
import {
  AddIcon,
  CloseIcon,
  DeleteIcon,
  DownloadIcon,
  EditIcon,
  RefreshIcon,
  SearchIcon,
  SettingIcon,
} from 'tdesign-icons-vue-next';
import { FormInstanceFunctions, FormRule, PageInfo, PrimaryTableCol, SubmitContext } from 'tdesign-vue-next';
import { computed, getCurrentInstance, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { addData, delData, getData, listData, updateData } from '@/api/system/dict/data';
import { getType, optionselect as getDictOptionselect } from '@/api/system/dict/type';
import { SysDictDataForm, SysDictDataQuery, SysDictDataVo, SysDictTypeVo } from '@/api/system/model/dictModel';
import { useTabsRouterStore } from '@/store';
import useDictStore from '@/store/modules/dict';

const { proxy } = getCurrentInstance();
const { sys_normal_disable } = proxy.useDict('sys_normal_disable');

const dataList = ref<SysDictDataVo[]>([]);
const open = ref(false);
const loading = ref(false);
const eLoading = ref(false);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref('');
const defaultDictType = ref('');
const typeOptions = ref<SysDictTypeVo[]>([]);
const columnControllerVisible = ref(false);
const dataRef = ref<FormInstanceFunctions>(null);
const tabsRouterStore = useTabsRouterStore();
const route = useRoute();
const router = useRouter();
// 数据标签回显样式
const listClassOptions = ref([
  { value: 'default', label: '默认' },
  { value: 'primary', label: '主要' },
  { value: 'success', label: '成功' },
  { value: 'warning', label: '警告' },
  { value: 'danger', label: '危险' },
]);

const rules = ref<Record<string, Array<FormRule>>>({
  dictLabel: [{ required: true, message: '数据标签不能为空', trigger: 'blur' }],
  dictValue: [{ required: true, message: '数据键值不能为空', trigger: 'blur' }],
  dictSort: [{ required: true, message: '数据顺序不能为空', trigger: 'blur' }],
});
// 列显隐信息
const columns = ref<Array<PrimaryTableCol>>([
  { title: `选择列`, colKey: 'row-select', type: 'multiple', width: 50, align: 'center' },
  { title: `字典编码`, colKey: 'dictCode', align: 'center' },
  { title: `字典标签`, colKey: 'dictLabel', align: 'center' },
  { title: `字典键值`, colKey: 'dictValue', align: 'center' },
  { title: `字典排序`, colKey: 'dictSort', align: 'center' },
  { title: `状态`, colKey: 'status', align: 'center', ellipsis: true },
  { title: `备注`, colKey: 'remark', align: 'center', ellipsis: true },
  { title: `创建时间`, colKey: 'createTime', align: 'center', width: 180 },
  { title: `操作`, colKey: 'operation', align: 'center', width: 160 },
]);

const form = ref<SysDictDataForm & SysDictDataVo>({
  listClass: 'default',
  dictSort: 0,
  status: '1',
});
const queryParams = ref<SysDictDataQuery>({
  pageNum: 1,
  pageSize: 10,
  dictLabel: undefined,
  dictType: undefined,
  status: undefined,
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

/** 查询字典类型详细 */
function getTypes(dictId: string | number | string[]) {
  getType(dictId as number).then((response) => {
    queryParams.value.dictType = response.data.dictType;
    defaultDictType.value = response.data.dictType;
    getList();
  });
}

/** 查询字典类型列表 */
function getTypeList() {
  getDictOptionselect().then((response) => {
    typeOptions.value = response.data;
  });
}
/** 查询字典数据列表 */
function getList() {
  loading.value = true;
  listData(queryParams.value).then((response) => {
    dataList.value = response.rows;
    total.value = response.total;
    loading.value = false;
  });
}
/** 表单重置 */
function reset() {
  form.value = {
    dictCode: undefined,
    dictLabel: undefined,
    dictValue: undefined,
    dictType: undefined,
    cssClass: undefined,
    listClass: 'default',
    dictSort: 0,
    status: '1',
    remark: undefined,
  };
  proxy.resetForm('dataRef');
}
/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}
/** 返回按钮操作 */
function handleClose() {
  tabsRouterStore.removeCurrentTab(route, '/system/dict', router);
}
/** 重置按钮操作 */
function resetQuery() {
  proxy.resetForm('queryRef');
  queryParams.value.dictType = defaultDictType.value;
  handleQuery();
}
/** 新增按钮操作 */
function handleAdd() {
  reset();
  open.value = true;
  title.value = '添加字典数据';
  form.value.dictType = queryParams.value.dictType;
}
/** 多选框选中数据 */
function handleSelectionChange(selection: Array<string | number>) {
  ids.value = selection;
  single.value = selection.length !== 1;
  multiple.value = !selection.length;
}
/** 修改按钮操作 */
function handleUpdate(row?: SysDictDataVo) {
  reset();
  open.value = true;
  title.value = '修改字典数据';
  const dictCode = row?.dictCode || ids.value.at(0);
  eLoading.value = true;
  getData(dictCode).then((response) => {
    form.value = response.data;
    eLoading.value = false;
  });
}
function onConfirm() {
  dataRef.value.submit();
}
/** 提交按钮 */
function submitForm({ validateResult, firstError }: SubmitContext) {
  if (validateResult === true) {
    const msgLoading = proxy.$modal.msgLoading('提交中...');
    if (form.value.dictCode) {
      updateData(form.value)
        .then((response) => {
          useDictStore().removeDict(queryParams.value.dictType);
          proxy.$modal.msgSuccess('修改成功');
          open.value = false;
          getList();
        })
        .finally(() => proxy.$modal.msgClose(msgLoading));
    } else {
      addData(form.value)
        .then((response) => {
          useDictStore().removeDict(queryParams.value.dictType);
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
function handleDelete(row?: SysDictDataVo) {
  const dictCodes = row?.dictCode || ids.value;
  proxy.$modal.confirm(`是否确认删除字典编码为"${dictCodes}"的数据项？`, () => {
    const msgLoading = proxy.$modal.msgLoading('正在删除中...');
    return delData(dictCodes)
      .then(() => {
        if (!row) ids.value = [];
        getList();
        proxy.$modal.msgSuccess('删除成功');
        useDictStore().removeDict(queryParams.value.dictType);
      })
      .finally(() => proxy.$modal.msgClose(msgLoading));
  });
}
/** 导出按钮操作 */
function handleExport() {
  proxy.download(
    'system/dict/data/export',
    {
      ...queryParams.value,
    },
    `dict_data_${new Date().getTime()}.xlsx`,
  );
}

getTypes(route.params && route.params.dictId);
getTypeList();
</script>
