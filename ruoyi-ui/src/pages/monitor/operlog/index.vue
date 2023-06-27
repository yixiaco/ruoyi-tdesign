<template>
  <t-card>
    <t-space direction="vertical">
      <t-form v-show="showSearch" ref="queryRef" :data="queryParams" layout="inline" label-width="68px">
        <t-form-item label="系统模块" name="title">
          <t-input
            v-model="queryParams.title"
            placeholder="请输入系统模块"
            clearable
            style="width: 240px"
            @enter="handleQuery"
          />
        </t-form-item>
        <t-form-item label="操作人员" name="operName">
          <t-input
            v-model="queryParams.operName"
            placeholder="请输入操作人员"
            clearable
            style="width: 240px"
            @enter="handleQuery"
          />
        </t-form-item>
        <t-form-item label="类型" name="businessType">
          <t-select v-model="queryParams.businessType" placeholder="操作类型" clearable style="width: 240px">
            <t-option v-for="dict in sys_oper_type" :key="dict.value" :label="dict.label" :value="dict.value" />
          </t-select>
        </t-form-item>
        <t-form-item label="状态" name="status">
          <t-select v-model="queryParams.status" placeholder="操作状态" clearable style="width: 240px">
            <t-option v-for="dict in sys_common_status" :key="dict.value" :label="dict.label" :value="dict.value" />
          </t-select>
        </t-form-item>
        <t-form-item label="操作时间" style="width: 308px">
          <t-date-range-picker
            v-model="dateRangeOperTime"
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
        hover
        :loading="loading"
        row-key="operId"
        :data="operlogList"
        :columns="columns"
        :selected-row-keys="ids"
        select-on-row-click
        :pagination="pagination"
        :column-controller="{
          hideTriggerButton: true,
        }"
        :sort="sort"
        :show-sort-column-bg-color="true"
        @select-change="handleSelectionChange"
        @sort-change="handleSortChange"
      >
        <template #topContent>
          <t-row>
            <t-col flex="auto">
              <t-button
                v-hasPermi="['system:notice:remove']"
                theme="danger"
                variant="outline"
                :disabled="multiple"
                @click="handleDelete"
              >
                <template #icon> <delete-icon /> </template>
                删除
              </t-button>
              <t-button v-hasPermi="['system:notice:remove']" theme="danger" variant="outline" @click="handleClean">
                <template #icon> <delete-icon /> </template>
                清空
              </t-button>
              <t-button v-hasPermi="['monitor:operlog:export']" theme="default" variant="outline" @click="handleExport">
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
        <template #businessType="{ row }">
          <dict-tag :options="sys_oper_type" :value="row.businessType" />
        </template>
        <template #status="{ row }">
          <dict-tag :options="sys_common_status" :value="row.status" />
        </template>
        <template #costTime="{ row }"> {{ row.costTime }}毫秒 </template>
        <template #operation="{ row, index }">
          <t-space :size="8">
            <t-link
              v-hasPermi="['monitor:operlog:query']"
              theme="primary"
              hover="color"
              @click.stop="handleView(row, index)"
            >
              <browse-icon />详细
            </t-link>
          </t-space>
        </template>
      </t-table>
    </t-space>

    <!-- 操作日志详细 -->
    <t-dialog v-model:visible="open" header="操作日志详细" width="700px" attach="body">
      <t-form label-align="right" colon :data="form" label-width="calc(5em + 24px)">
        <t-row :gutter="[0, 20]">
          <t-col :span="6">
            <t-form-item label="操作模块">{{ form.title }} / {{ typeFormat(form) }}</t-form-item>
            <t-form-item label="登录信息">
              {{ form.operName }} / {{ form.operIp }} / {{ form.operLocation }}
            </t-form-item>
          </t-col>
          <t-col :span="6">
            <t-form-item label="请求地址">{{ form.operUrl }}</t-form-item>
            <t-form-item label="请求方式">{{ form.requestMethod }}</t-form-item>
          </t-col>
          <t-col :span="6">
            <t-form-item label="操作类别">{{ form.operatorType }}</t-form-item>
          </t-col>
          <t-col :span="6">
            <t-form-item label="业务类型"><dict-tag :options="sys_oper_type" :value="form.businessType" /></t-form-item>
          </t-col>
          <t-col :span="12">
            <t-form-item label="操作方法">{{ form.method }}</t-form-item>
          </t-col>
          <t-col :span="12">
            <t-form-item label="请求参数">{{ form.operParam }}</t-form-item>
          </t-col>
          <t-col :span="12">
            <t-form-item label="返回参数">{{ form.jsonResult }}</t-form-item>
          </t-col>
          <t-col :span="6">
            <t-form-item label="操作状态">
              <dict-tag :options="sys_common_status" :value="form.status" />
            </t-form-item>
          </t-col>
          <t-col :span="6">
            <t-form-item label="操作时间">{{ parseTime(form.operTime) }}</t-form-item>
          </t-col>
          <t-col :span="6">
            <t-form-item label="消耗时间">{{ form.costTime }}毫秒</t-form-item>
          </t-col>
          <t-col :span="12">
            <t-form-item v-if="form.status === 0" label="异常信息">{{ form.errorMsg }}</t-form-item>
          </t-col>
        </t-row>
      </t-form>
    </t-dialog>
  </t-card>
</template>
<script lang="ts">
export default {
  name: 'Operlog',
};
</script>
<script lang="ts" setup>
import { BrowseIcon, DeleteIcon, DownloadIcon, RefreshIcon, SearchIcon, SettingIcon } from 'tdesign-icons-vue-next';
import { PageInfo, PrimaryTableCol } from 'tdesign-vue-next';
import { computed, getCurrentInstance, ref } from 'vue';

import { SysOperLogBo, SysOperLogVo } from '@/api/monitor/model/operlogModel';
import { cleanOperlog, delOperlog, list } from '@/api/monitor/operlog';
import { TableSort } from '@/types/interface';

const { proxy } = getCurrentInstance();
const { sys_oper_type, sys_common_status } = proxy.useDict('sys_oper_type', 'sys_common_status');

const operlogList = ref<SysOperLogVo[]>([]);
const open = ref(false);
const loading = ref(false);
const columnControllerVisible = ref(false);
const showSearch = ref(true);
const ids = ref([]);
const multiple = ref(true);
const total = ref(0);
const dateRangeOperTime = ref([]);
const defaultSort = ref({ sortBy: 'operTime', descending: true });
const sort = ref({ ...defaultSort.value });

// 列显隐信息
const columns = ref<Array<PrimaryTableCol>>([
  { title: `选择列`, colKey: 'row-select', type: 'multiple', width: 50, align: 'center' },
  { title: `日志编号`, colKey: 'operId', align: 'center' },
  { title: `系统模块`, colKey: 'title', align: 'center' },
  { title: `操作类型`, colKey: 'businessType', align: 'center' },
  { title: `请求方式`, colKey: 'requestMethod', align: 'center' },
  { title: `操作人员`, colKey: 'operName', align: 'center', ellipsis: true, width: 100, sorter: true },
  { title: `主机`, colKey: 'operIp', align: 'center', ellipsis: true, width: 130 },
  { title: `操作状态`, colKey: 'status', align: 'center' },
  { title: `消耗时间`, colKey: 'costTime', align: 'center', sorter: true },
  { title: `操作日期`, colKey: 'operTime', align: 'center', sorter: true },
  { title: `操作`, colKey: 'operation', align: 'center' },
]);

const form = ref<SysOperLogBo & SysOperLogVo>({});
const queryParams = ref<SysOperLogBo>({
  pageNum: 1,
  pageSize: 10,
  title: undefined,
  operName: undefined,
  businessType: undefined,
  status: undefined,
  orderByColumn: undefined,
  isAsc: undefined,
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

/** 查询操作日志 */
function getList() {
  loading.value = true;
  proxy.addDateRange(queryParams.value, dateRangeOperTime.value, 'OperTime');
  list(queryParams.value).then((response) => {
    operlogList.value = response.rows;
    total.value = response.total;
    loading.value = false;
  });
}
/** 操作日志类型字典翻译 */
function typeFormat(row: SysOperLogVo) {
  return proxy.selectDictLabel(sys_oper_type.value, row.businessType);
}
/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}
/** 重置按钮操作 */
function resetQuery() {
  dateRangeOperTime.value = [];
  proxy.resetForm('queryRef');
  queryParams.value.pageNum = 1;
  handleSortChange({ ...defaultSort.value });
}
/** 多选框选中数据 */
function handleSelectionChange(selection: Array<string | number>) {
  ids.value = selection;
  multiple.value = !selection.length;
}
/** 排序触发事件 */
function handleSortChange(value: TableSort) {
  sort.value = value;
  queryParams.value.orderByColumn = value.sortBy;
  queryParams.value.isAsc = value.descending ? 'descending' : 'ascending';
  getList();
}
/** 详细按钮操作 */
function handleView(row: SysOperLogVo, index: number) {
  open.value = true;
  form.value = row;
}
/** 删除按钮操作 */
function handleDelete(row: SysOperLogVo) {
  const operIds = row.operId || ids.value;
  proxy.$modal.confirm(`是否确认删除日志编号为"${operIds}"的数据项?`, () => {
    return delOperlog(operIds).then(() => {
      getList();
      proxy.$modal.msgSuccess('删除成功');
    });
  });
}
/** 清空按钮操作 */
function handleClean() {
  proxy.$modal.confirm('是否确认清空所有操作日志数据项?', () => {
    return cleanOperlog().then(() => {
      getList();
      proxy.$modal.msgSuccess('清空成功');
    });
  });
}
/** 导出按钮操作 */
function handleExport() {
  proxy.download(
    'monitor/operlog/export',
    {
      ...queryParams.value,
    },
    `config_${new Date().getTime()}.xlsx`,
  );
}

getList();
</script>
