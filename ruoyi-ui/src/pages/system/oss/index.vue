<template>
  <div class="app-container">
    <t-space direction="vertical">
      <t-form v-show="showSearch" ref="queryRef" :data="queryParams" layout="inline">
        <t-form-item label="文件名" name="fileName">
          <t-input
            v-model="queryParams.fileName"
            placeholder="请输入文件名"
            clearable
            style="width: 200px"
            @keyup.enter="handleQuery"
          />
        </t-form-item>
        <t-form-item label="原名" name="originalName">
          <t-input
            v-model="queryParams.originalName"
            placeholder="请输入原名"
            clearable
            style="width: 200px"
            @keyup.enter="handleQuery"
          />
        </t-form-item>
        <t-form-item label="文件后缀" name="fileSuffix">
          <t-input
            v-model="queryParams.fileSuffix"
            placeholder="请输入文件后缀"
            clearable
            style="width: 200px"
            @keyup.enter="handleQuery"
          />
        </t-form-item>
        <t-form-item label="创建时间">
          <t-date-range-picker
            v-model="daterangeCreateTime"
            allow-input
            clearable
            separator="-"
            :placeholder="['开始日期', '结束日期']"
          />
        </t-form-item>
        <t-form-item label="上传人" name="createBy">
          <t-input
            v-model="queryParams.createBy"
            placeholder="请输入上传人"
            clearable
            style="width: 200px"
            @keyup.enter="handleQuery"
          />
        </t-form-item>
        <t-form-item label="服务商" name="service">
          <t-input
            v-model="queryParams.service"
            placeholder="请输入服务商"
            clearable
            style="width: 200px"
            @keyup.enter="handleQuery"
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
        row-key="ossId"
        :data="ossList"
        :columns="columns"
        :selected-row-keys="ids"
        select-on-row-click
        :pagination="pagination"
        :column-controller="{
          hideTriggerButton: true,
        }"
        :sort="sort"
        :show-sort-column-bg-color="true"
        @sort-change="handleSortChange"
        @select-change="handleSelectionChange"
      >
        <template #topContent>
          <t-row>
            <t-col flex="auto">
              <t-button v-hasPermi="['system:oss:upload']" theme="primary" @click="handleFile">
                <template #icon> <add-icon /></template>
                上传文件
              </t-button>
              <t-button v-hasPermi="['system:oss:upload']" theme="primary" @click="handleImage">
                <template #icon> <add-icon /></template>
                上传图片
              </t-button>
              <t-button
                v-hasPermi="['system:oss:remove']"
                theme="danger"
                variant="outline"
                :disabled="multiple"
                @click="handleDelete"
              >
                <template #icon> <delete-icon /> </template>
                删除
              </t-button>
              <t-button
                v-hasPermi="['system:oss:edit']"
                :theme="previewListResource ? 'danger' : 'warning'"
                variant="outline"
                @click="handlePreviewListResource(!previewListResource)"
              >
                预览开关 : {{ previewListResource ? '禁用' : '启用' }}
              </t-button>
              <t-button v-hasPermi="['system:oss:list']" theme="default" variant="outline" @click="handleOssConfig">
                <template #icon> <setting-icon /> </template>
                配置管理
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

        <template #url="{ row }">
          <image-preview
            v-if="previewListResource && checkFileSuffix(row.fileSuffix)"
            :src="row.url"
            width="80px"
            height="80px"
          ></image-preview>
          <span v-if="!checkFileSuffix(row.fileSuffix) || !previewListResource" v-text="row.url" />
        </template>
        <template #operation="{ row }">
          <t-space :size="8">
            <t-link
              v-hasPermi="['system:oss:download']"
              theme="primary"
              hover="color"
              @click.stop="handleDownload(row)"
            >
              <download-icon />下载
            </t-link>
            <t-link v-hasPermi="['system:oss:remove']" theme="danger" hover="color" @click.stop="handleDelete(row)">
              <delete-icon />删除
            </t-link>
          </t-space>
        </template>
      </t-table>
    </t-space>

    <!-- 添加或修改OSS对象存储对话框 -->
    <t-dialog
      v-model:visible="open"
      :close-on-overlay-click="false"
      :header="title"
      width="700px"
      attach="body"
      @confirm="submitForm"
    >
      <t-form ref="ossRef" :data="form" label-align="right" :rules="rules" label-width="80px">
        <t-form-item label="文件名">
          <file-upload v-if="type === 0" v-model="form.file" theme="file-flow" />
          <image-upload v-if="type === 1" v-model="form.file" theme="image-flow" />
        </t-form-item>
      </t-form>
    </t-dialog>
  </div>
</template>
<script lang="ts">
export default {
  name: 'Oss',
};
</script>
<script lang="ts" setup>
import { AddIcon, DeleteIcon, DownloadIcon, RefreshIcon, SearchIcon, SettingIcon } from 'tdesign-icons-vue-next';
import { computed, getCurrentInstance, reactive, ref, toRefs } from 'vue';
import { useRouter } from 'vue-router';
import { listOss, delOss } from '@/api/system/oss';
import ImagePreview from '@/components/image-preview/index.vue';
import FileUpload from '@/components/file-upload/index.vue';
import ImageUpload from '@/components/image-upload/index.vue';

const router = useRouter();
const { proxy } = getCurrentInstance();

const ossList = ref([]);
const open = ref(false);
const columnControllerVisible = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref('');
const type = ref(0);
const previewListResource = ref(true);
const daterangeCreateTime = ref([]);
// 默认排序
const defaultSort = ref({ sortBy: 'createTime', descending: false });
const sort = ref({ ...defaultSort.value });

const data = reactive({
  form: {},
  // 查询参数
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    fileName: undefined,
    originalName: undefined,
    fileSuffix: undefined,
    url: undefined,
    createTime: undefined,
    createBy: undefined,
    service: undefined,
    orderByColumn: undefined,
    isAsc: undefined,
  },
  rules: {
    file: [{ required: true, message: '文件不能为空', trigger: 'blur' }],
  },
});

// 列显隐信息
const columns = ref([
  { title: `选择列`, colKey: 'row-select', type: 'multiple', width: 50, align: 'center' },
  { title: `文件名`, colKey: 'fileName', align: 'center', ellipsis: true },
  { title: `原名`, colKey: 'originalName', align: 'center', ellipsis: true },
  { title: `文件后缀`, colKey: 'fileSuffix', align: 'center' },
  { title: `文件展示`, colKey: 'url', align: 'center', ellipsis: true },
  { title: `创建时间`, colKey: 'createTime', align: 'center', width: 180 },
  { title: `上传人`, colKey: 'createBy', align: 'center' },
  { title: `服务商`, colKey: 'service', align: 'center' },
  { title: `操作`, colKey: 'operation', align: 'center' },
]);

// 分页
const pagination = computed(() => {
  return {
    current: queryParams.value.pageNum,
    pageSize: queryParams.value.pageSize,
    total: total.value,
    showJumper: true,
    onChange: (pageInfo) => {
      data.queryParams.pageNum = pageInfo.current;
      data.queryParams.pageSize = pageInfo.pageSize;
      getList();
    },
  };
});

const { queryParams, form, rules } = toRefs(data);

/** 查询OSS对象存储列表 */
function getList() {
  loading.value = true;
  proxy.getConfigKey('sys.oss.previewListResource').then((response) => {
    previewListResource.value = response.data === undefined ? true : response.data === 'true';
  });
  listOss(proxy.addDateRange(queryParams.value, daterangeCreateTime.value, 'CreateTime')).then((response) => {
    ossList.value = response.rows;
    total.value = response.total;
    loading.value = false;
  });
}
function checkFileSuffix(fileSuffix) {
  const arr = ['png', 'jpg', 'jpeg'];
  return arr.some((type) => {
    return fileSuffix.indexOf(type) > -1;
  });
}
/** 表单重置 */
function reset() {
  form.value = {
    file: undefined,
  };
  proxy.resetForm('ossRef');
}
/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}
/** 重置按钮操作 */
function resetQuery() {
  daterangeCreateTime.value = [];
  proxy.resetForm('queryRef');
  handleSortChange({ ...defaultSort.value });
}
/** 选择条数  */
function handleSelectionChange(selection) {
  ids.value = selection;
  single.value = selection.length !== 1;
  multiple.value = !selection.length;
}
/** 排序触发事件 */
function handleSortChange(value) {
  sort.value = value;
  queryParams.value.orderByColumn = value.sortBy;
  queryParams.value.isAsc = value.descending ? 'descending' : 'ascending';
  getList();
}
/** oss配置列表查询 */
function handleOssConfig() {
  router.push('/system/oss-config/index');
}
/** 文件按钮操作 */
function handleFile() {
  reset();
  open.value = true;
  title.value = '上传文件';
  type.value = 0;
}
/** 图片按钮操作 */
function handleImage() {
  reset();
  open.value = true;
  title.value = '上传图片';
  type.value = 1;
}
/** 提交按钮 */
function submitForm() {
  open.value = false;
  reset();
  getList();
}
/** 下载按钮操作 */
function handleDownload(row) {
  proxy.$download.oss(row.ossId);
}
/** 用户状态修改  */
function handlePreviewListResource(previewListResource) {
  const text = previewListResource ? '启用' : '停用';
  proxy.$modal.confirm(`确认要"${text}""预览列表图片"配置吗?`, () => {
    return proxy
      .updateConfigByKey('sys.oss.previewListResource', previewListResource)
      .then(() => {
        getList();
        proxy.$modal.msgSuccess(`${text}成功`);
      })
      .catch(() => {
        previewListResource.value = previewListResource.value !== true;
      });
  });
}
/** 删除按钮操作 */
function handleDelete(row) {
  const ossIds = row.ossId || ids.value;
  proxy.$modal.confirm(`是否确认删除OSS对象存储编号为"${ossIds}"的数据项?`, () => {
    loading.value = true;
    return delOss(ossIds)
      .then(() => {
        loading.value = false;
        getList();
        proxy.$modal.msgSuccess('删除成功');
      })
      .finally(() => {
        loading.value = false;
      });
  });
}

getList();
</script>
