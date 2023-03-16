<template>
  <t-card>
    <t-form
      :data="form"
      label-width="calc(8em + 24px)"
      :rules="rules"
      scroll-to-first-error="smooth"
      @submit="onSubmit"
    >
      <t-tabs v-model="activeName">
        <t-tab-panel label="基本信息" value="basic" :destroy-on-hide="false">
          <basic-info-form :info="info" />
        </t-tab-panel>
        <t-tab-panel label="字段信息" value="columnInfo" :destroy-on-hide="false">
          <t-table :data="columnsData" :columns="columns" row-key="columnId" :max-height="tableHeight">
            <template #columnComment="{ row }">
              <t-input v-model="row.columnComment"></t-input>
            </template>
            <template #javaType="{ row }">
              <t-select v-model="row.javaType">
                <t-option label="Long" value="Long" />
                <t-option label="String" value="String" />
                <t-option label="Integer" value="Integer" />
                <t-option label="Double" value="Double" />
                <t-option label="BigDecimal" value="BigDecimal" />
                <t-option label="Date" value="Date" />
                <t-option label="Boolean" value="Boolean" />
              </t-select>
            </template>
            <template #javaField="{ row }">
              <t-input v-model="row.javaField"></t-input>
            </template>
            <template #isInsert="{ row }">
              <t-custom-checkbox v-model="row.isInsert" true-label="1" false-label="0"></t-custom-checkbox>
            </template>
            <template #isEdit="{ row }">
              <t-custom-checkbox v-model="row.isEdit" true-label="1" false-label="0"></t-custom-checkbox>
            </template>
            <template #isList="{ row }">
              <t-custom-checkbox v-model="row.isList" true-label="1" false-label="0"></t-custom-checkbox>
            </template>
            <template #isDetail="{ row }">
              <t-custom-checkbox v-model="row.isDetail" true-label="1" false-label="0"></t-custom-checkbox>
            </template>
            <template #isQuery="{ row }">
              <t-custom-checkbox v-model="row.isQuery" true-label="1" false-label="0"></t-custom-checkbox>
            </template>
            <template #queryType="{ row }">
              <t-select v-model="row.queryType">
                <t-option label="=" value="EQ" />
                <t-option label="!=" value="NE" />
                <t-option label=">" value="GT" />
                <t-option label=">=" value="GE" />
                <t-option label="<" value="LT" />
                <t-option label="<=" value="LE" />
                <t-option label="LIKE" value="LIKE" />
                <t-option label="NOT LIKE" value="NOT_LIKE" />
                <t-option label="IS NULL" value="IS_NULL" />
                <t-option label="IS NOT NULL" value="IS_NOT_NULL" />
                <t-option label="IN" value="IN" />
                <t-option label="BETWEEN" value="BETWEEN" />
              </t-select>
            </template>
            <template #isRequired="{ row }">
              <t-custom-checkbox v-model="row.isRequired" true-label="1" false-label="0"></t-custom-checkbox>
            </template>
            <template #htmlType="{ row }">
              <t-select v-model="row.htmlType">
                <t-option label="文本框" value="input" />
                <t-option label="文本域" value="textarea" />
                <t-option label="下拉框" value="select" />
                <t-option label="单选框" value="radio" />
                <t-option label="复选框" value="checkbox" />
                <t-option label="日期控件" value="datetime" />
                <t-option label="图片上传" value="imageUpload" />
                <t-option label="文件上传" value="fileUpload" />
                <t-option label="富文本控件" value="editor" />
              </t-select>
            </template>
            <template #dictType="{ row }">
              <t-select
                v-model="row.dictType"
                clearable
                filterable
                placeholder="请选择"
                :popup-props="{ overlayInnerStyle: { width: '100%' } }"
              >
                <t-option
                  v-for="dict in dictOptions"
                  :key="dict.dictType"
                  :label="dict.dictName"
                  :value="dict.dictType"
                  class="gen-option"
                >
                  <span style="float: left">{{ dict.dictName }}</span>
                  <span style="float: right; color: #8492a6; font-size: 13px; margin-left: 5px">{{
                    dict.dictType
                  }}</span>
                </t-option>
              </t-select>
            </template>
          </t-table>
        </t-tab-panel>
        <t-tab-panel label="生成信息" value="genInfo" :destroy-on-hide="false">
          <gen-info-form :info="info" :tables="tables" />
        </t-tab-panel>
      </t-tabs>
      <div style="text-align: center; margin-left: -100px; margin-top: 10px">
        <t-button theme="primary" type="submit">提交</t-button>
        <t-button theme="default" variant="outline" @click="close()">返回</t-button>
      </div>
    </t-form>
  </t-card>
</template>
<script lang="ts">
export default {
  name: 'GenEdit',
};
</script>
<script lang="ts" setup>
import { useRoute, useRouter } from 'vue-router';
import { getCurrentInstance, reactive, ref, toRefs } from 'vue';
import { FormRule, PrimaryTableCol } from 'tdesign-vue-next';
import { getGenTable, updateGenTable } from '@/api/tool/gen';
import { optionselect as getDictOptionselect } from '@/api/system/dict/type';
import BasicInfoForm from './basicInfoForm.vue';
import GenInfoForm from './genInfoForm.vue';
import TCustomCheckbox from './components/checkbox.vue';
import { useTabsRouterStore } from '@/store';
import { GenTable, GenTableColumn } from '@/api/tool/model/genModel';

const tabsRouterStore = useTabsRouterStore();
const route = useRoute();
const router = useRouter();
const { proxy } = getCurrentInstance();

const activeName = ref('columnInfo');
const tableHeight = ref(`${document.documentElement.scrollHeight - 245}px`);
const dictOptions = ref([]);
const columns = ref<Array<PrimaryTableCol<GenTableColumn>>>([
  { title: '序号', colKey: 'serial-number', width: '5%', align: 'center' },
  { title: `字段列名`, colKey: 'columnName', align: 'center', ellipsis: true, width: '10%' },
  { title: `字段描述`, colKey: 'columnComment', align: 'center', width: '10%' },
  { title: `物理类型`, colKey: 'columnType', align: 'center', ellipsis: true, width: '10%' },
  { title: `Java类型`, colKey: 'javaType', align: 'center', width: '10%' },
  { title: `java属性`, colKey: 'javaField', align: 'center', width: '10%' },
  { title: `插入`, colKey: 'isInsert', align: 'center', width: '5%' },
  { title: `编辑`, colKey: 'isEdit', align: 'center', width: '5%' },
  { title: `列表`, colKey: 'isList', align: 'center', width: '5%' },
  { title: `详情`, colKey: 'isDetail', align: 'center', width: '5%' },
  { title: `查询`, colKey: 'isQuery', align: 'center', width: '5%' },
  { title: `查询方式`, colKey: 'queryType', align: 'center', width: '10%' },
  { title: `必填`, colKey: 'isRequired', align: 'center', width: '5%' },
  { title: `显示类型`, colKey: 'htmlType', align: 'center', width: '10%' },
  { title: `字典类型`, colKey: 'dictType', align: 'center', width: '12%' },
]);

const rules = ref<Record<string, Array<FormRule>>>({
  tplCategory: [{ required: true, message: '请选择生成模板', trigger: 'blur' }],
  packageName: [{ required: true, message: '请输入生成包路径', trigger: 'blur' }],
  moduleName: [{ required: true, message: '请输入生成模块名', trigger: 'blur' }],
  businessName: [{ required: true, message: '请输入生成业务名', trigger: 'blur' }],
  functionName: [{ required: true, message: '请输入生成功能名', trigger: 'blur' }],
  tableName: [{ required: true, message: '请输入表名称', trigger: 'blur' }],
  tableComment: [{ required: true, message: '请输入表描述', trigger: 'blur' }],
  className: [{ required: true, message: '请输入实体类名称', trigger: 'blur' }],
  functionAuthor: [{ required: true, message: '请输入作者', trigger: 'blur' }],
});

const form = reactive<{
  columnsData: GenTableColumn[];
  info: GenTable;
  tables: GenTable[];
}>({
  columnsData: [],
  info: {},
  tables: [],
});

const { tables, columnsData, info } = toRefs(form);

function onSubmit({ validateResult, firstError }) {
  if (validateResult === true) {
    const genTable = { ...info.value };
    genTable.columns = columnsData.value;
    genTable.params = {
      treeCode: info.value.treeCode,
      treeName: info.value.treeName,
      treeParentCode: info.value.treeParentCode,
      parentMenuId: info.value.parentMenuId,
      isUseQuery: info.value.isUseQuery,
      isUseBO: info.value.isUseBO,
      isUseVO: info.value.isUseVO,
    };
    updateGenTable(genTable).then((res) => {
      proxy.$modal.msgSuccess(res.msg);
      if (res.code === 200) {
        close();
      }
    });
  } else {
    const basicInfo = ['info.tableName', 'info.tableComment', 'info.className', 'info.functionAuthor'];
    const isBasic = Object.keys(validateResult).some((value) => basicInfo.includes(value));
    if (isBasic) {
      activeName.value = 'basic';
    } else {
      activeName.value = 'genInfo';
    }
    proxy.$modal.msgError(firstError);
  }
}
function close() {
  tabsRouterStore.removeCurrentTab(route, '/tool/gen', router);
}

(() => {
  const tableId = route.params && route.params.tableId;
  if (tableId) {
    // 获取表详细信息
    getGenTable(String(tableId)).then((res) => {
      columnsData.value = res.data.rows;
      info.value = res.data.info;
      tables.value = res.data.tables;
    });
    /** 查询字典下拉列表 */
    getDictOptionselect().then((response) => {
      dictOptions.value = response.data;
    });
  }
})();
</script>
