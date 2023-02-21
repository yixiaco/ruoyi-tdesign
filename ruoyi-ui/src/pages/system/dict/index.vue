<template>
  <t-card>
    <t-space direction="vertical">
      <t-form v-show="showSearch" ref="queryRef" :data="queryParams" layout="inline" label-width="68px">
        <t-form-item label="字典名称" name="dictName">
          <t-input
            v-model="queryParams.dictName"
            placeholder="请输入字典名称"
            clearable
            style="width: 240px"
            @keyup.enter="handleQuery"
          />
        </t-form-item>
        <t-form-item label="字典类型" name="dictType">
          <t-input
            v-model="queryParams.dictType"
            placeholder="请输入字典类型"
            clearable
            style="width: 240px"
            @keyup.enter="handleQuery"
          />
        </t-form-item>
        <t-form-item label="状态" name="status">
          <t-select v-model="queryParams.status" placeholder="字典状态" clearable style="width: 240px">
            <t-option v-for="dict in sys_normal_disable" :key="dict.value" :label="dict.label" :value="dict.value" />
          </t-select>
        </t-form-item>
        <t-form-item label="创建时间" style="width: 340px">
          <t-date-range-picker
            v-model="dateRange"
            allow-input
            clearable
            separator="-"
            :placeholder="['开始日期', '结束日期']"
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

      <t-table
        v-model:column-controller-visible="columnControllerVisible"
        :loading="loading"
        row-key="dictId"
        :data="typeList"
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
                @click="handleUpdate"
              >
                <template #icon> <edit-icon /> </template>
                修改
              </t-button>
              <t-button
                v-hasPermi="['system:dict:remove']"
                theme="danger"
                variant="outline"
                :disabled="multiple"
                @click="handleDelete"
              >
                <template #icon> <delete-icon /> </template>
                删除
              </t-button>
              <t-button v-hasPermi="['system:dict:export']" theme="default" variant="outline" @click="handleExport">
                <template #icon> <download-icon /> </template>
                导出
              </t-button>
              <t-button
                v-hasPermi="['system:dict:remove']"
                theme="danger"
                variant="outline"
                @click="handleRefreshCache"
              >
                <template #icon> <refresh-icon /> </template>
                刷新缓存
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
        <template #dictType="{ row }">
          <router-link :to="'/system/dict-data/index/' + row.dictId" class="link-type">
            <span>{{ row.dictType }}</span>
          </router-link>
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
        <t-form ref="dictRef" label-align="right" :data="form" :rules="rules" label-width="80px" @submit="submitForm">
          <t-form-item label="字典名称" name="dictName">
            <t-input v-model="form.dictName" placeholder="请输入字典名称" />
          </t-form-item>
          <t-form-item label="字典类型" name="dictType">
            <t-input v-model="form.dictType" placeholder="请输入字典类型" />
          </t-form-item>
          <t-form-item label="状态" name="status">
            <t-radio-group v-model="form.status">
              <t-radio v-for="dict in sys_normal_disable" :key="dict.value" :value="dict.value">{{
                dict.label
              }}</t-radio>
            </t-radio-group>
          </t-form-item>
          <t-form-item label="备注" name="remark">
            <t-textarea v-model="form.remark" placeholder="请输入内容"></t-textarea>
          </t-form-item>
        </t-form>
      </t-loading>
    </t-dialog>
  </t-card>
</template>
<script lang="ts">
export default {
  name: 'Dict',
};
</script>
<script lang="ts" setup>
import { computed, getCurrentInstance, ref } from 'vue';
import {
  AddIcon,
  DeleteIcon,
  DownloadIcon,
  EditIcon,
  RefreshIcon,
  SearchIcon,
  SettingIcon,
} from 'tdesign-icons-vue-next';
import { FormInstanceFunctions, FormRule, PrimaryTableCol } from 'tdesign-vue-next';
import useDictStore from '@/store/modules/dict';
import { listType, getType, delType, addType, updateType, refreshCache } from '@/api/system/dict/type';
import { SysDictType } from '@/api/system/model/dictModel';

const { proxy } = getCurrentInstance();
const { sys_normal_disable } = proxy.useDict('sys_normal_disable');

const typeList = ref<SysDictType[]>([]);
const open = ref(false);
const loading = ref(false);
const eLoading = ref(false);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref('');
const dateRange = ref([]);
const columnControllerVisible = ref(false);
const dictRef = ref<FormInstanceFunctions>(null);
const rules = ref<Record<string, Array<FormRule>>>({
  dictName: [{ required: true, message: '字典名称不能为空', trigger: 'blur' }],
  dictType: [{ required: true, message: '字典类型不能为空', trigger: 'blur' }],
});

// 列显隐信息
const columns = ref<Array<PrimaryTableCol>>([
  { title: `选择列`, colKey: 'row-select', type: 'multiple', width: 50, align: 'center' },
  { title: `字典编号`, colKey: 'dictId', align: 'center' },
  { title: `字典名称`, colKey: 'dictName', align: 'center', ellipsis: true },
  { title: `字典类型`, colKey: 'dictType', align: 'center', ellipsis: true },
  { title: `状态`, colKey: 'status', align: 'center' },
  { title: `备注`, colKey: 'remark', align: 'center', ellipsis: true },
  { title: `创建时间`, colKey: 'createTime', align: 'center', width: 180 },
  { title: `操作`, colKey: 'operation', align: 'center', width: 160 },
]);

const form = ref<SysDictType>({
  status: '0',
});
const queryParams = ref<SysDictType>({
  pageNum: 1,
  pageSize: 10,
  dictName: undefined,
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
    onChange: (pageInfo) => {
      queryParams.value.pageNum = pageInfo.current;
      queryParams.value.pageSize = pageInfo.pageSize;
      getList();
    },
  };
});

/** 查询字典类型列表 */
function getList() {
  loading.value = true;
  listType(proxy.addDateRange(queryParams.value, dateRange.value)).then((response) => {
    typeList.value = response.rows;
    total.value = response.total;
    loading.value = false;
  });
}
/** 表单重置 */
function reset() {
  form.value = {
    dictId: undefined,
    dictName: undefined,
    dictType: undefined,
    status: '0',
    remark: undefined,
  };
  proxy.resetForm('dictRef');
}
/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}
/** 重置按钮操作 */
function resetQuery() {
  dateRange.value = [];
  proxy.resetForm('queryRef');
  handleQuery();
}
/** 新增按钮操作 */
function handleAdd() {
  reset();
  open.value = true;
  title.value = '添加字典类型';
}
/** 多选框选中数据 */
function handleSelectionChange(selection) {
  ids.value = selection;
  single.value = selection.length !== 1;
  multiple.value = !selection.length;
}
/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  open.value = true;
  title.value = '修改字典类型';
  eLoading.value = true;
  const dictId = row.dictId || ids.value;
  getType(dictId).then((response) => {
    form.value = response.data;
    eLoading.value = false;
  });
}
function onConfirm() {
  dictRef.value.submit();
}
/** 提交按钮 */
function submitForm({ validateResult, firstError }) {
  if (validateResult === true) {
    if (form.value.dictId) {
      updateType(form.value).then((response) => {
        proxy.$modal.msgSuccess('修改成功');
        open.value = false;
        getList();
      });
    } else {
      addType(form.value).then((response) => {
        proxy.$modal.msgSuccess('新增成功');
        open.value = false;
        getList();
      });
    }
  } else {
    proxy.$modal.msgError(firstError);
  }
}
/** 删除按钮操作 */
function handleDelete(row) {
  const dictIds = row.dictId || ids.value;
  proxy.$modal.confirm(`是否确认删除字典编号为"${dictIds}"的数据项？`, () => {
    return delType(dictIds).then(() => {
      getList();
      proxy.$modal.msgSuccess('删除成功');
    });
  });
}
/** 导出按钮操作 */
function handleExport() {
  proxy.download(
    'system/dict/type/export',
    {
      ...queryParams.value,
    },
    `dict_${new Date().getTime()}.xlsx`,
  );
}
/** 刷新缓存按钮操作 */
function handleRefreshCache() {
  proxy.$modal.confirm(`是否确认刷新字典缓存？`, () => {
    return refreshCache().then(() => {
      proxy.$modal.msgSuccess('刷新成功');
      useDictStore().cleanDict();
    });
  });
}

getList();
</script>
