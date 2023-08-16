<template>
  <div>
    <div class="list-card-operation">
      <t-button> 新建产品 </t-button>
      <div class="search-input">
        <t-form v-show="showSearch" ref="queryRef" :data="queryParams" layout="inline">
          <t-form-item label-width="0px" name="originalName">
            <t-input v-model="queryParams.originalName" placeholder="请输入文件名" clearable @enter="handleQuery">
              <template #prefix-icon>
                <search-icon :style="{ cursor: 'pointer' }" size="var(--td-comp-size-xxxs)" @click="handleQuery" />
              </template>
            </t-input>
          </t-form-item>
        </t-form>
      </div>
    </div>

    <div class="list-card-items">
      <t-loading :loading="loading">
        <div class="list-card-gallery">
          <div
            v-for="oss in ossList"
            :key="oss.ossId"
            aria-checked="false"
            class="list-card-gallery-item"
            data-ckbox-gallery-item="true"
            data-ckbox-gallery-item-id="qbCNv7OaeH1H"
            role="checkbox"
            tabindex="0"
            @click="handleCheck(oss)"
          >
            <div
              class="list-card-gallery-item__label"
              :class="{ 'list-card-gallery-item__label--active': ids.includes(oss.ossId) }"
            >
              <figure class="list-card-gallery-figure" data-visible="true">
                <figcaption class="list-card-gallery-figure__caption">{{ oss.fileSuffix.substring(1) }}</figcaption>
                <div class="list-card-gallery-figure__content">
<!--                  <div class="gallery-doc-icon gallery-doc-icon&#45;&#45;unknown">
                    <archive-svg class="gallery-doc-icon__icon" />
                  </div>-->
                  <picture class="list-card-gallery-responsive-image list-card-gallery-responsive-image--fit">
                    <img
                      class="list-card-gallery-responsive-image__img--fit list-card-gallery-responsive-image__img--cover"
                      :src="oss.url"
                    />
                  </picture>
                </div>
              </figure>
              <div class="list-card-gallery-item__details">
                <span class="list-card-gallery-item__name" :title="oss.originalName">{{ oss.originalName }}</span>
                <button
                  aria-label="Select asset"
                  class="gallery-btn gallery-btn--neutral gallery-btn--plain gallery-btn--small gallery-btn--icon-only-small list-card-gallery-item__checkmark"
                  :class="{ 'list-card-gallery-item__checkmark--active': ids.includes(oss.ossId) }"
                  type="button"
                  tabindex="-1"
                >
                  <span>
                    <check-icon class="gallery-icon gallery-icon--base gallery-btn__icon" />
                  </span>
                </button>
              </div>
            </div>
          </div>
        </div>
      </t-loading>
    </div>
    <div class="list-card-pagination">
      <t-pagination
        v-model="pagination.current"
        v-model:page-size="pagination.pageSize"
        :total="pagination.total"
        :show-jumper="pagination.showJumper"
        :page-size-options="[10, 20, 50]"
        @change="pagination.onChange"
      />
    </div>

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
  name: 'MyOss',
};
</script>
<script lang="ts" setup>
import {
  AddIcon,
  CheckIcon,
  DeleteIcon,
  DownloadIcon,
  RefreshIcon,
  RelativityIcon,
  SearchIcon,
  SettingIcon,
} from 'tdesign-icons-vue-next';
import { FormRule, PageInfo, TableSort } from 'tdesign-vue-next';
import { computed, getCurrentInstance, ref } from 'vue';

import { getPreviewListResourceConfig } from '@/api/system/config';
import { SysOssQuery, SysOssVo } from '@/api/system/model/ossModel';
import { delOss, listOss } from '@/api/system/oss';
import ArchiveSvg from '@/assets/file-type/archive.svg?component';
import FileUpload from '@/components/file-upload/index.vue';
import ImagePreview from '@/components/image-preview/index.vue';
import ImageUpload from '@/components/image-upload/index.vue';

const { proxy } = getCurrentInstance();

const ossList = ref<SysOssVo[]>([]);
const open = ref(false);
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
const sort = ref<TableSort>(null);
// 密度
const gallerySize = ref('140px');

const rules = ref<Record<string, Array<FormRule>>>({
  file: [{ required: true, message: '文件不能为空', trigger: 'blur' }],
});

const form = ref<{ file?: any }>({});
const queryParams = ref<SysOssQuery>({
  pageNum: 1,
  pageSize: 10,
  fileName: undefined,
  originalName: undefined,
  fileSuffix: undefined,
  url: undefined,
  createTime: undefined,
  createByName: undefined,
  service: undefined,
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

/** 查询OSS对象存储列表 */
function getList() {
  loading.value = true;
  getPreviewListResourceConfig().then((response) => {
    previewListResource.value = response.data === undefined ? true : response.data;
  });
  listOss(proxy.addDateRange(queryParams.value, daterangeCreateTime.value, 'CreateTime'))
    .then((response) => {
      ossList.value = response.rows;
      total.value = response.total;
      loading.value = false;
    })
    .finally(() => (loading.value = false));
}
function checkFileSuffix(fileSuffix: string | string[]) {
  const arr = ['png', 'jpg', 'jpeg', 'ico', 'bmp', 'webp', 'gif', 'svg'];
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
  queryParams.value.pageNum = 1;
  handleSortChange(null);
}
/** 选择条数  */
function handleSelectionChange(selection: Array<string | number>) {
  ids.value = selection;
  single.value = selection.length !== 1;
  multiple.value = !selection.length;
}
/** 排序触发事件 */
function handleSortChange(value?: TableSort) {
  sort.value = value;
  if (Array.isArray(value)) {
    queryParams.value.orderByColumn = value.map((item) => item.sortBy).join(',');
    queryParams.value.isAsc = value.map((item) => (item.descending ? 'descending' : 'ascending')).join(',');
  } else {
    queryParams.value.orderByColumn = value?.sortBy;
    queryParams.value.isAsc = value?.descending ? 'descending' : 'ascending';
  }
  getList();
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
/** 复制成功 */
function copyTextSuccess() {
  proxy.$modal.msgSuccess('复制成功');
}
/** 下载按钮操作 */
function handleDownload(row: SysOssVo) {
  proxy.$download.oss(row.ossId);
}
/** 预览状态修改  */
function handlePreviewListResource(preview: boolean) {
  const text = preview ? '启用' : '停用';
  proxy.$modal.confirm(`确认要"${text}""预览列表图片"配置吗?`, () => {
    const msgLoading = proxy.$modal.msgLoading('提交中...');
    return proxy
      .updateConfigByKey('sys.oss.previewListResource', `${preview}`, 1)
      .then(() => {
        getList();
        proxy.$modal.msgSuccess(`${text}成功`);
      })
      .catch(() => {
        previewListResource.value = previewListResource.value !== true;
      })
      .finally(() => proxy.$modal.msgClose(msgLoading));
  });
}
/** 删除按钮操作 */
function handleDelete(row?: SysOssVo) {
  const ossIds = row?.ossId || ids.value;
  proxy.$modal.confirm(`是否确认删除OSS对象存储编号为"${ossIds}"的数据项?`, () => {
    const msgLoading = proxy.$modal.msgLoading('正在删除中...');
    return delOss(ossIds)
      .then(() => {
        getList();
        proxy.$modal.msgSuccess('删除成功');
      })
      .finally(() => proxy.$modal.msgClose(msgLoading));
  });
}

function handleCheck(row: SysOssVo) {
  const index = ids.value.indexOf(row.ossId);
  if (index !== -1) {
    ids.value.splice(index, 1);
  } else {
    ids.value.push(row.ossId);
  }
}

getList();
</script>
<style lang="less" scoped>
@neutral-h: 220;
@neutral-s: 10%;
.list-card {
  height: 100%;

  &-operation {
    display: flex;
    justify-content: space-between;
    margin-bottom: var(--td-comp-margin-xxl);

    .search-input {
      width: 360px;
    }
  }

  &-item {
    padding: var(--td-comp-paddingTB-xl) var(--td-comp-paddingTB-xl);

    :deep(.t-card__header) {
      padding: 0;
    }

    :deep(.t-card__body) {
      padding: 0;
      margin-top: var(--td-comp-margin-xxl);
      margin-bottom: var(--td-comp-margin-xxl);
    }

    :deep(.t-card__footer) {
      padding: 0;
    }
  }
  &-gallery {
    display: grid;
    grid-gap: 13.5px;
    grid-template-columns: repeat(auto-fill, minmax(v-bind(gallerySize), 1fr));
    -webkit-user-select: none;
    user-select: none;
    outline: none;
    &-responsive-image--fit {
      width: 100%;
      height: 100%;
    }
    &-responsive-image {
      position: absolute;
      opacity: 1;
    }
    &-responsive-image__img--cover {
      object-fit: cover;
    }
    &-responsive-image__img--fit {
      width: 100%;
      height: 100%;
    }
    &-figure {
      height: v-bind(gallerySize);
      cursor: pointer;
      margin: 0;
      overflow: hidden;
      position: relative;
      &__caption {
        background-color: hsl(@neutral-h, @neutral-s, @neutral-s);
        color: hsl(@neutral-h, @neutral-s, 100%);
        font-size: 12px;
        left: 0;
        padding: 4px;
        position: absolute;
        text-transform: uppercase;
        line-height: 1;
        top: 0;
        z-index: 1;
      }
      &__content {
        margin: -1px;
        height: calc(100% + 1px);
        width: calc(100% + 1px);
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;
        position: relative;
      }
    }
    &-item {
      color: hsl(@neutral-h, @neutral-s, 10%);
      box-sizing: border-box;
      min-width: v-bind(gallerySize);
      &__label {
        background-color: hsl(@neutral-h, @neutral-s, 100%);
        box-shadow: 0 0 4px 2px hsl(@neutral-h @neutral-s 20% / 15%);
        display: block;
        overflow: hidden;
        position: relative;
        outline: 1px solid rgba(0, 0, 0, 0);
        transition: outline 200ms;
        &--active {
          outline: 1px solid hsl(214, 100%, 50%);
        }
      }
      &__details {
        border-top: 1px solid hsl(@neutral-h, @neutral-s, 90%);
        cursor: pointer;
        display: flex;
        align-items: center;
        padding: calc(8px * 0.7) 8px;
        position: relative;
        gap: 4px;
      }
      &__name {
        font-size: 11.2px;
        overflow: hidden;
        position: relative;
        text-overflow: ellipsis;
        white-space: nowrap;
        flex-grow: 1;
      }
      &__checkmark {
        flex-shrink: 0;
        opacity: 0;
        color: hsl(@neutral-h, @neutral-s, 70%);
        transition:
          color 200ms,
          opacity 200ms;
        isolation: isolate;
        &--active {
          color: hsl(214, 100%, 50%);
          opacity: 1;
        }
      }
      &:hover &__checkmark {
        opacity: 1;
      }
    }
  }

  &-pagination {
    padding: var(--td-comp-paddingTB-xl) var(--td-comp-paddingTB-xl);
  }

  &-loading {
    height: 100%;
    width: 100%;
    display: flex;
    align-items: center;
    justify-content: center;
  }
}

.gallery-btn--plain:hover {
  --btn-background-color: hsl(@neutral-h @neutral-s 60% / 10%);
}
.gallery-btn--plain:active {
  --btn-background-color: hsl(@neutral-h @neutral-s 60% /20%);
}
.gallery-btn--neutral {
  --btn-background-color-plain-hover: hsl(@neutral-h @neutral-s 60% / 10%);
}
.gallery-btn {
  --btn-border-color: transparent;
  --btn-padding-vertical: 8px;
  --btn-padding-horizontal: 12px;
  --btn-font-size: 13.6px;
  --btn-line-height: 20px;
  border-radius: 7px;
  cursor: pointer;
  display: inline-flex;
  justify-content: center;
  align-items: center;
  -webkit-user-select: none;
  user-select: none;
  gap: 4px;
  transition:
    background-color 200ms,
    opacity 200ms;
  font-weight: 500;
  padding: 2.4px 2.4px;
  background-color: var(--btn-background-color);
  border: 1px solid transparent;
  max-width: 100%;
}
.gallery-icon--base {
  height: 16px;
  min-width: 16px;
}
.gallery-icon {
  display: block;
  fill: currentColor;
  max-width: initial;
  transition: color 200ms;
}
.gallery-doc-icon--unknown {
  background-color: #f5f5f5;
}
.gallery-doc-icon--archive {
  background-color: #f4e9f5;
}
.gallery-doc-icon--empty {
  background-color: #fff;
}
.gallery-doc-icon--excel {
  background-color: #e8f5ef;
}
.gallery-doc-icon--img {
  background-color: #f5f5f5;
}
.gallery-doc-icon--movie {
  background-color: #e5f5f7;
}
.gallery-doc-icon--music {
  background-color: #fff6ea;
}
.gallery-doc-icon--pdf {
  background-color: #ffeae8;
}
.gallery-doc-icon--ppt {
  background-color: #f9ebe8;
}
.gallery-doc-icon--txt {
  background-color: #f8f8fb;
}
.gallery-doc-icon--word {
  background-color: #e9eff7;
}
.gallery-doc-icon {
  height: 100%;
  width: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
}
.gallery-doc-icon__icon {
  width: 56px;
  height: 66px;
}
</style>
