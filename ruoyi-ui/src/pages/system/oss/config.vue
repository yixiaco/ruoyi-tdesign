<template>
  <t-card>
    <t-space direction="vertical">
      <t-form v-show="showSearch" ref="queryRef" :data="queryParams" layout="inline">
        <t-form-item label="配置key" name="configKey">
          <t-input
            v-model="queryParams.configKey"
            placeholder="配置key"
            clearable
            style="width: 200px"
            @keyup.enter="handleQuery"
          />
        </t-form-item>
        <t-form-item label="桶名称" name="bucketName">
          <t-input
            v-model="queryParams.bucketName"
            placeholder="请输入桶名称"
            clearable
            style="width: 200px"
            @keyup.enter="handleQuery"
          />
        </t-form-item>
        <t-form-item label="是否默认" name="status">
          <t-select v-model="queryParams.status" placeholder="请选择状态" clearable style="width: 200px">
            <t-option key="0" label="是" value="0" />
            <t-option key="1" label="否" value="1" />
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
        :loading="loading"
        row-key="ossConfigId"
        :data="ossConfigList"
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
              <t-button v-hasPermi="['system:ossConfig:add']" theme="primary" @click="handleAdd">
                <template #icon> <add-icon /></template>
                新增
              </t-button>
              <t-button
                v-hasPermi="['system:ossConfig:edit']"
                theme="default"
                variant="outline"
                :disabled="single"
                @click="handleUpdate"
              >
                <template #icon> <edit-icon /> </template>
                修改
              </t-button>
              <t-button
                v-hasPermi="['system:ossConfig:remove']"
                theme="danger"
                variant="outline"
                :disabled="multiple"
                @click="handleDelete"
              >
                <template #icon> <delete-icon /> </template>
                删除
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
        <template #accessPolicy="{ row }">
          <t-tag v-if="row.accessPolicy === '0'" type="warning">private</t-tag>
          <t-tag v-if="row.accessPolicy === '1'" type="success">public</t-tag>
          <t-tag v-if="row.accessPolicy === '2'" type="info">custom</t-tag>
        </template>
        <template #status="{ row }">
          <t-switch
            v-model="row.status"
            :custom-value="['0', '1']"
            @change="handleStatusChange(row)"
            @click.stop
          ></t-switch>
        </template>
        <template #operation="{ row }">
          <t-space :size="8">
            <t-link
              v-hasPermi="['system:ossConfig:edit']"
              theme="primary"
              hover="color"
              @click.stop="handleUpdate(row)"
            >
              <edit-icon />修改
            </t-link>
            <t-link
              v-hasPermi="['system:ossConfig:remove']"
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

    <!-- 添加或修改对象存储配置对话框 -->
    <t-dialog
      v-model:visible="open"
      :close-on-overlay-click="false"
      :header="title"
      width="800px"
      attach="body"
      :confirm-btn="{
        content: '确 定',
        theme: 'primary',
        loading: buttonLoading,
      }"
      @confirm="onConfirm"
    >
      <t-form ref="ossConfigRef" :data="form" :rules="rules" label-width="120px" @submit="submitForm">
        <t-form-item label="配置key" name="configKey">
          <t-input v-model="form.configKey" placeholder="请输入配置key" />
        </t-form-item>
        <t-form-item label="访问站点" name="endpoint">
          <t-input v-model="form.endpoint" placeholder="请输入访问站点" />
        </t-form-item>
        <t-form-item label="自定义域名" name="domain">
          <t-input v-model="form.domain" placeholder="请输入自定义域名" />
        </t-form-item>
        <t-form-item label="accessKey" name="accessKey">
          <t-input v-model="form.accessKey" placeholder="请输入accessKey" />
        </t-form-item>
        <t-form-item label="secretKey" name="secretKey">
          <t-input v-model="form.secretKey" placeholder="请输入秘钥" show-password />
        </t-form-item>
        <t-form-item label="桶名称" name="bucketName">
          <t-input v-model="form.bucketName" placeholder="请输入桶名称" />
        </t-form-item>
        <t-form-item label="前缀" name="prefix">
          <t-input v-model="form.prefix" placeholder="请输入前缀" />
        </t-form-item>
        <t-form-item label="是否HTTPS">
          <t-radio-group v-model="form.isHttps">
            <t-radio v-for="dict in sys_yes_no" :key="dict.value" :value="dict.value">{{ dict.label }}</t-radio>
          </t-radio-group>
        </t-form-item>
        <t-form-item label="桶权限类型">
          <t-radio-group v-model="form.accessPolicy">
            <t-radio value="0">private</t-radio>
            <t-radio value="1">public</t-radio>
            <t-radio value="2">custom</t-radio>
          </t-radio-group>
        </t-form-item>
        <t-form-item label="域" name="region">
          <t-input v-model="form.region" placeholder="请输入域" />
        </t-form-item>
        <t-form-item label="备注" name="remark">
          <t-textarea v-model="form.remark" placeholder="请输入内容" />
        </t-form-item>
      </t-form>
    </t-dialog>
  </t-card>
</template>
<script lang="ts">
export default {
  name: 'OssConfig',
};
</script>
<script lang="ts" setup>
import { AddIcon, DeleteIcon, EditIcon, RefreshIcon, SearchIcon, SettingIcon } from 'tdesign-icons-vue-next';
import { FormInstanceFunctions, FormRule, PrimaryTableCol } from 'tdesign-vue-next';
import { computed, getCurrentInstance, ref } from 'vue';

import { SysOssConfigForm, SysOssConfigQuery, SysOssConfigVo } from '@/api/system/model/ossConfigModel';
import {
  addOssConfig,
  changeOssConfigStatus,
  delOssConfig,
  getOssConfig,
  listOssConfig,
  updateOssConfig,
} from '@/api/system/ossConfig';

const { proxy } = getCurrentInstance();
const { sys_yes_no } = proxy.useDict('sys_yes_no');

const ossConfigList = ref<SysOssConfigVo[]>([]);
const open = ref(false);
const buttonLoading = ref(false);
const loading = ref(false);
const columnControllerVisible = ref(false);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref('');
const ossConfigRef = ref<FormInstanceFunctions>(null);

// 列显隐信息
const columns = ref<Array<PrimaryTableCol>>([
  { title: `选择列`, colKey: 'row-select', type: 'multiple', width: 50, align: 'center' },
  { title: `主建`, colKey: 'ossConfigId', align: 'center' },
  { title: `配置key`, colKey: 'configKey', align: 'center' },
  { title: `访问站点`, colKey: 'endpoint', align: 'center', width: 200 },
  { title: `自定义域名`, colKey: 'domain', align: 'center', width: 200 },
  { title: `桶名称`, colKey: 'bucketName', align: 'center' },
  { title: `前缀`, colKey: 'prefix', align: 'center' },
  { title: `域`, colKey: 'region', align: 'center' },
  { title: `桶权限类型`, colKey: 'accessPolicy', align: 'center' },
  { title: `是否默认`, colKey: 'status', align: 'center' },
  { title: `操作`, colKey: 'operation', align: 'center' },
]);

const rules = ref<Record<string, Array<FormRule>>>({
  configKey: [{ required: true, message: 'configKey不能为空', trigger: 'blur' }],
  accessKey: [
    { required: true, message: 'accessKey不能为空', trigger: 'blur' },
    {
      min: 2,
      max: 200,
      message: 'accessKey长度必须介于 2 和 100 之间',
      trigger: 'blur',
    },
  ],
  secretKey: [
    { required: true, message: 'secretKey不能为空', trigger: 'blur' },
    {
      min: 2,
      max: 100,
      message: 'secretKey长度必须介于 2 和 100 之间',
      trigger: 'blur',
    },
  ],
  bucketName: [
    { required: true, message: 'bucketName不能为空', trigger: 'blur' },
    {
      min: 2,
      max: 100,
      message: 'bucketName长度必须介于 2 和 100 之间',
      trigger: 'blur',
    },
  ],
  endpoint: [
    { required: true, message: 'endpoint不能为空', trigger: 'blur' },
    {
      min: 2,
      max: 100,
      message: 'endpoint名称长度必须介于 2 和 100 之间',
      trigger: 'blur',
    },
  ],
  accessPolicy: [{ required: true, message: 'accessPolicy不能为空', trigger: 'blur' }],
});

const form = ref<SysOssConfigVo & SysOssConfigForm>({});
// 查询参数
const queryParams = ref<SysOssConfigQuery>({
  pageNum: 1,
  pageSize: 10,
  configKey: undefined,
  bucketName: undefined,
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

/** 查询对象存储配置列表 */
function getList() {
  loading.value = true;
  listOssConfig(queryParams.value).then((response) => {
    ossConfigList.value = response.rows;
    total.value = response.total;
    loading.value = false;
  });
}
/** 表单重置 */
function reset() {
  form.value = {
    ossConfigId: undefined,
    configKey: undefined,
    accessKey: undefined,
    secretKey: undefined,
    bucketName: undefined,
    prefix: undefined,
    endpoint: undefined,
    domain: undefined,
    isHttps: 'N',
    accessPolicy: '1',
    region: undefined,
    status: '1',
    remark: undefined,
  };
  proxy.resetForm('ossConfigRef');
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
/** 选择条数  */
function handleSelectionChange(selection) {
  ids.value = selection;
  single.value = selection.length !== 1;
  multiple.value = !selection.length;
}
/** 新增按钮操作 */
function handleAdd() {
  reset();
  open.value = true;
  title.value = '添加对象存储配置';
}
/** 修改按钮操作 */
function handleUpdate(row) {
  loading.value = true;
  reset();
  const ossConfigId = row.ossConfigId || ids.value;
  getOssConfig(ossConfigId).then((response) => {
    loading.value = false;
    form.value = response.data;
    open.value = true;
    title.value = '修改对象存储配置';
  });
}
function onConfirm() {
  ossConfigRef.value.submit();
}
/** 提交按钮 */
function submitForm({ validateResult, firstError }) {
  if (validateResult === true) {
    buttonLoading.value = true;
    const msgLoading = proxy.$modal.msgLoading('提交中...');
    if (form.value.ossConfigId) {
      updateOssConfig(form.value)
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
      addOssConfig(this.form)
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
/** 用户状态修改  */
function handleStatusChange(row) {
  const text = row.status === '0' ? '启用' : '停用';
  proxy.$modal.confirm(
    `确认要"${text}""${row.configKey}"配置吗?`,
    () => {
      const msgLoading = proxy.$modal.msgLoading('提交中...');
      return changeOssConfigStatus(row.ossConfigId, row.status, row.configKey)
        .then(() => {
          getList();
          proxy.$modal.msgSuccess(`${text}成功`);
        })
        .catch(() => {
          row.status = row.status === '0' ? '1' : '0';
        })
        .finally(() => proxy.$modal.msgClose(msgLoading));
    },
    () => {
      row.status = row.status === '0' ? '1' : '0';
    },
  );
}
/** 删除按钮操作 */
function handleDelete(row) {
  const ossConfigIds = row.ossConfigId || ids.value;
  proxy.$modal.confirm(`是否确认删除OSS配置编号为"${ossConfigIds}"的数据项?`, () => {
    loading.value = true;
    const msgLoading = proxy.$modal.msgLoading('正在删除中...');
    return delOssConfig(ossConfigIds)
      .then(() => {
        loading.value = false;
        getList();
        proxy.$modal.msgSuccess('删除成功');
      })
      .finally(() => {
        loading.value = false;
        proxy.$modal.msgClose(msgLoading);
      });
  });
}

getList();
</script>
