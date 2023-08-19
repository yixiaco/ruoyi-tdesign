<template>
  <div>
    <t-space direction="vertical" style="width: 100%">
      <t-space>
        <t-button v-hasPermi="['system:oss:upload']" theme="primary" @click="handleUpload">
          <template #icon> <cloud-upload-icon /></template>
          上传
        </t-button>
        <t-button v-hasPermi="['system:oss:remove']" theme="danger" :disabled="!ids.length" @click="handleDelete()">
          <template #icon> <delete-icon /> </template>
          删除
        </t-button>
        <t-button v-hasPermi="['system:oss:download']" :disabled="ids.length !== 1" @click="handleDownload()">
          <template #icon> <download-icon /> </template>
          下载
        </t-button>
        <t-image-viewer v-model:index="imagePreviewIndex" :images="previewList">
          <template #trigger="{ open }">
            <t-button v-hasPermi="['system:oss:query']" :disabled="previewList.length === 0" @click="open">
              <template #icon> <browse-icon /> </template>
              预览
            </t-button>
          </template>
        </t-image-viewer>
        <t-button v-hasPermi="['system:oss:update']" :disabled="ids.length !== 1" @click="handleUpdate()">
          <template #icon> <edit-icon /> </template>
          编辑
        </t-button>
        <t-button v-hasPermi="['system:oss:update']" :disabled="ids.length === 0" @click="handleMove()">
          <template #icon> <swap-icon /> </template>
          移动到
        </t-button>
        <t-form v-show="showSearch" ref="queryRef" :data="queryParams" layout="inline">
          <t-form-item label-width="0px" name="originalName">
            <t-input v-model="queryParams.originalName" placeholder="搜索文件名" @enter="handleQuery">
              <template #suffix-icon>
                <search-icon :style="{ cursor: 'pointer' }" size="var(--td-comp-size-xxxs)" @click="handleQuery" />
              </template>
            </t-input>
          </t-form-item>
        </t-form>
      </t-space>
      <t-loading :loading="loading">
        <div v-if="ossList.length === 0" class="t-table__empty">暂无数据</div>
        <rect-select
          v-else
          class="list-card-gallery"
          :disabled="!multiple"
          @change="handleRectChange"
          @keydown.ctrl.a.stop.prevent="handleCheckedAll"
        >
          <div
            v-for="(oss, index) in effectiveOssList"
            :key="oss.ossId"
            :aria-checked="oss.active ? 'true' : 'false'"
            class="list-card-gallery-item"
            role="checkbox"
            :tabindex="index"
            :title="oss.originalName"
            @mousedown.stop
            @click.exact.stop="handleClick(oss)"
            @click.ctrl.exact.stop="handleCtrlClick(oss)"
            @click.shift.exact.stop="handleShiftClick(oss)"
            @click.ctrl.shift.stop="handleShiftClick(oss, true)"
          >
            <div class="list-card-gallery-item__label" :class="{ 'list-card-gallery-item__label--active': oss.active }">
              <figure class="list-card-gallery-figure" data-visible="true">
                <figcaption class="list-card-gallery-figure__caption">
                  {{ oss.fileSuffix.substring(1) }}
                  <template v-if="thumbnailSize >= 100"> ({{ bytesToSize(oss.size).replace(' ', '') }}) </template>
                </figcaption>
                <div class="list-card-gallery-figure__content">
                  <div
                    v-if="getMediaType(oss) !== 'image'"
                    :class="`gallery-doc-icon gallery-doc-icon--${getMediaType(oss)}`"
                  >
                    <component :is="getMediaType(oss) + '-svg'" class="gallery-doc-icon__icon" />
                  </div>
                  <picture v-else class="list-card-gallery-responsive-image list-card-gallery-responsive-image--fit">
                    <img
                      draggable="false"
                      class="list-card-gallery-responsive-image__img--fit list-card-gallery-responsive-image__img--cover"
                      :src="oss.url"
                      :alt="oss.originalName"
                    />
                  </picture>
                </div>
              </figure>
              <div class="list-card-gallery-item__details">
                <span class="list-card-gallery-item__name">{{ oss.originalName }}</span>
                <button
                  class="gallery-btn gallery-btn--neutral gallery-btn--plain gallery-btn--small gallery-btn--icon-only-small list-card-gallery-item__checkmark"
                  :class="{ 'list-card-gallery-item__checkmark--active': oss.active }"
                  type="button"
                  @click.stop="buttonChecked(oss)"
                >
                  <span>
                    <check-icon class="gallery-icon gallery-icon--base gallery-btn__icon" />
                  </span>
                </button>
              </div>
            </div>
          </div>
        </rect-select>
      </t-loading>
      <div class="list-card-pagination">
        <t-pagination
          v-model="pagination.current"
          v-model:page-size="pagination.pageSize"
          :total="pagination.total"
          :show-jumper="pagination.showJumper"
          :page-size-options="[10, 20, 50]"
          @change="pagination.onChange"
        >
          <template #totalContent>
            <div class="t-pagination__total">
              共 {{ pagination.total }} 项数据
              <template v-if="ids.length !== 0">&nbsp;&nbsp;选中 {{ ids.length }} 项</template>
            </div>
          </template>
        </t-pagination>
      </div>
    </t-space>

    <!-- 上传OSS对象存储对话框 -->
    <t-dialog
      v-model:visible="openUpload"
      :close-on-overlay-click="false"
      :header="uploadTitle"
      width="700px"
      attach="body"
      @confirm="submitUploadForm"
    >
      <t-form :data="uploadForm" label-align="right" label-width="80px">
        <t-form-item label="上传类型">
          <t-radio-group v-model="type" variant="default-filled">
            <t-radio-button :value="0">上传文件</t-radio-button>
            <t-radio-button :value="1">上传图片</t-radio-button>
          </t-radio-group>
        </t-form-item>
        <t-form-item label="选择文件">
          <file-upload v-if="type === 0" v-model="uploadForm.file" theme="file-flow" />
          <image-upload v-if="type === 1" v-model="uploadForm.file" theme="image-flow" />
        </t-form-item>
      </t-form>
    </t-dialog>
    <!-- 添加或修改OSS对象存储对话框 -->
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
        ref="ossRef"
        label-align="right"
        :data="form"
        :rules="rules"
        label-width="calc(5em + 24px)"
        scroll-to-first-error="smooth"
        @submit="submitForm"
      >
        <t-form-item label="原名" name="originalName">
          <t-input v-model="form.originalName" placeholder="请输入原名" clearable />
        </t-form-item>
        <t-form-item label="分类" name="ossCategoryId">
          <t-tree-select
            v-model="form.ossCategoryId"
            :data="ossCategoryOptions"
            :tree-props="{
              keys: { value: 'ossCategoryId', label: 'categoryName', children: 'children' },
              checkStrictly: true,
            }"
            placeholder="请选择分类"
          />
        </t-form-item>
        <t-form-item label="锁定状态" name="isLock">
          <t-switch v-model="form.isLock" :custom-value="[1, 0]" />
        </t-form-item>
        <t-form-item label="URL" name="url">
          <t-input v-model="form.url" :readonly="true">
            <template #suffix-icon>
              <file-copy-icon style="cursor: pointer" @click="copyText(form.url)" />
            </template>
          </t-input>
        </t-form-item>
      </t-form>
    </t-dialog>
    <!-- 移动OSS对象存储对话框 -->
    <t-dialog
      v-model:visible="openMove"
      :close-on-overlay-click="false"
      :header="title"
      width="500px"
      attach="body"
      :confirm-btn="{
        content: '确 定',
        theme: 'primary',
        loading: buttonLoading,
      }"
      @confirm="onConfirmMove"
    >
      <t-form
        ref="ossMoveRef"
        label-align="right"
        :data="form"
        :rules="rules"
        label-width="calc(5em + 24px)"
        scroll-to-first-error="smooth"
        @submit="submitMoveForm"
      >
        <t-form-item label="分类" name="ossCategoryId">
          <t-tree-select
            v-model="form.ossCategoryId"
            :data="ossCategoryOptions"
            :tree-props="{
              keys: { value: 'ossCategoryId', label: 'categoryName', children: 'children' },
              checkStrictly: true,
            }"
            placeholder="请选择分类"
          />
        </t-form-item>
      </t-form>
    </t-dialog>
  </div>
</template>

<script lang="ts" setup>
import {
  BrowseIcon,
  CheckIcon,
  CloudUploadIcon,
  DeleteIcon,
  DownloadIcon,
  EditIcon,
  FileCopyIcon,
  SearchIcon,
  SwapIcon,
} from 'tdesign-icons-vue-next';
import { FormInstanceFunctions, FormRule, PageInfo, SubmitContext } from 'tdesign-vue-next';
import { computed, getCurrentInstance, ref, watch } from 'vue';
import useClipboard from 'vue-clipboard3';

import { SysOssCategoryVo } from '@/api/system/model/ossCategoryModel';
import { SysOssActiveVo, SysOssForm, SysOssQuery, SysOssVo } from '@/api/system/model/ossModel';
import { delOss, getOss, listMyOss, moveOss, updateOss } from '@/api/system/oss';
import { listOssCategory } from '@/api/system/ossCategory';
import FileUpload from '@/components/file-upload/index.vue';
import ImageUpload from '@/components/image-upload/index.vue';
import RectSelect from '@/components/rect-select/index.vue';

const props = defineProps({
  /** 分类id */
  categoryId: [Number],
  /** 启用多选 */
  multiple: {
    type: Boolean,
    default: true,
  },
  thumbnailSize: {
    type: Number,
    default: 120,
  },
});
watch(
  () => props.categoryId,
  (value) => getList(),
);
watch(
  () => props.multiple,
  (value, oldValue, onCleanup) => {
    if (oldValue) {
      // 从多选变更多单选，需要清理多余的选项
      ids.value.splice(1);
    }
  },
);

const { proxy } = getCurrentInstance();
const { toClipboard } = useClipboard();

const ossRef = ref<FormInstanceFunctions>(null);
const ossMoveRef = ref<FormInstanceFunctions>(null);
const ossList = ref<SysOssVo[]>([]);
const ossCategoryOptions = ref<SysOssCategoryVo[]>([]);
const openUpload = ref(false);
const openMove = ref(false);
const open = ref(false);
const buttonLoading = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const total = ref(0);
const type = ref(0);
const title = ref('');
const shiftId = ref<number>();
const daterangeCreateTime = ref([]);
// 预览图下标
const imagePreviewIndex = ref(0);
// 缩略图大小尺寸
const thumbnailSizePixel = computed(() => `${props.thumbnailSize}px`);
// 文件格式
const fileType = {
  image: 'jpg,jpeg,bmp,gif,png,svg,ico,tif,tiff,webp,pic,tga,jng,mng,jbg,jpe,heic,lbm'.split(','),
  archive: 'zip,7z,rar,gz,jar,lzh,taz,arj,z,ace,tar,xz,bz2,win'.split(','),
  excel: ['xls', 'xlsx', 'csv'],
  pdf: ['pdf'],
  ppt: ['ppt', 'pptx'],
  radio: ['wav', 'aif', 'au', 'mp3', 'ram', 'wma', 'mmf', 'amr', 'aac', 'flac', 'aifc', 'awr', 'cda', 'cdr', 'dig'],
  text: 'txt,sql,ini,xml,html,htm,css,scss,less,js,ts,java,inf,py,json'.split(','),
  video: 'avi,mp4,m4v,mkv,webm,flv,mov,wmv,3gp,3g2,mpg,vob,ogg,swf,dxr,fla,rm,mpe,mpeg,ts'.split(','),
  word: ['doc', 'docx', 'wps', 'rtf'],
};

const rules = ref<Record<string, Array<FormRule>>>({
  originalName: [{ required: true, message: '原名不能为空', trigger: 'blur' }],
  ossCategoryId: [{ required: true, message: '分类不能为空', trigger: 'blur' }],
  isLock: [{ required: true, message: '是否锁定状态不能为空', trigger: 'blur' }],
});

const uploadForm = ref<{ file?: any }>({});
// 提交表单对象
const form = ref<SysOssVo & SysOssForm>({
  isLock: 0,
});
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

const uploadTitle = computed(() => {
  if (type.value === 0) {
    return '上传文件';
  }
  return '上传图片';
});
const effectiveOssList = computed(() => {
  const ossCollection: SysOssActiveVo[] = [];
  ossList.value.forEach((oss) => {
    ossCollection.push({
      ...oss,
      active: ids.value.includes(oss.ossId),
    });
  });
  return ossCollection;
});
const previewList = computed(() => {
  return effectiveOssList.value
    .filter((oss) => {
      return oss.active && getMediaType(oss) === 'image';
    })
    .map((value) => value.url);
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

function getMediaType(oss: SysOssVo) {
  const suffix = oss.fileSuffix?.substring(1)?.toLowerCase();
  if (suffix) {
    const entries = Object.entries(fileType);
    const type = entries.find((value) => {
      return value[1].includes(suffix);
    });
    if (type) {
      return type[0];
    }
  }
  return 'unknown';
}

/** 查询OSS对象存储列表 */
function getList() {
  loading.value = true;
  queryParams.value.ossCategoryId = props.categoryId;
  listMyOss(proxy.addDateRange(queryParams.value, daterangeCreateTime.value, 'CreateTime'))
    .then((response) => {
      ids.value = [];
      shiftId.value = null;
      ossList.value = response.rows;
      total.value = response.total;
      loading.value = false;
    })
    .finally(() => (loading.value = false));
}
/** 表单重置 */
function reset() {
  uploadForm.value = {
    file: undefined,
  };
  form.value = {
    isLock: 0,
    ossCategoryId: 0,
  };
  proxy.resetForm('ossRef');
}
/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}
/** 查询OSS分类下拉树选项结构 */
async function getCategoryOptions() {
  const response = await listOssCategory();
  ossCategoryOptions.value = [
    {
      ossCategoryId: 0,
      categoryName: '根分类',
      children: proxy.handleTree(response.data, 'ossCategoryId', 'parentId'),
    },
  ];
}
/** 上传按钮操作 */
function handleUpload() {
  reset();
  openUpload.value = true;
}
/** 修改按钮操作 */
async function handleUpdate(row?: SysOssVo) {
  buttonLoading.value = true;
  reset();
  open.value = true;
  title.value = '修改OSS对象存储';
  await getCategoryOptions();
  const ossId = row?.ossId || ids.value.at(0);
  getOss(ossId).then((response) => {
    buttonLoading.value = false;
    form.value = response.data;
  });
}
/** 移动到按钮操作 */
async function handleMove() {
  buttonLoading.value = true;
  reset();
  openMove.value = true;
  title.value = '移动OSS对象存储';
  await getCategoryOptions();
  buttonLoading.value = false;
}
/** 上传提交按钮 */
function submitUploadForm() {
  openUpload.value = false;
  reset();
  getList();
}
/** 编辑表单提交按钮 */
function onConfirmMove() {
  ossMoveRef.value.submit();
}
/** 提交表单 */
function submitMoveForm({ validateResult, firstError }: SubmitContext) {
  if (validateResult === true) {
    buttonLoading.value = true;
    const msgLoading = proxy.$modal.msgLoading('移动提交中...');
    moveOss(form.value.ossCategoryId, ids.value)
      .then(() => {
        proxy.$modal.msgSuccess('移动成功');
        openMove.value = false;
        getList();
      })
      .finally(() => {
        buttonLoading.value = false;
        proxy.$modal.msgClose(msgLoading);
      });
  } else {
    proxy.$modal.msgError(firstError);
  }
}
/** 编辑表单提交按钮 */
function onConfirm() {
  ossRef.value.submit();
}
/** 提交表单 */
function submitForm({ validateResult, firstError }: SubmitContext) {
  if (validateResult === true) {
    buttonLoading.value = true;
    const msgLoading = proxy.$modal.msgLoading('提交中...');
    if (form.value.ossId) {
      updateOss(form.value)
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
      proxy.$modal.msgError('不支持新增OSS对象存储');
    }
  } else {
    proxy.$modal.msgError(firstError);
  }
}
/** 复制成功 */
async function copyText(text: string) {
  await toClipboard(text);
  await proxy.$modal.msgSuccess('复制成功');
}
/** 下载按钮操作 */
function handleDownload() {
  proxy.$download.oss(ids.value.at(0));
}
/** 删除按钮操作 */
function handleDelete() {
  const ossIds = ids.value;
  const files = ossList.value.filter((value) => ids.value.includes(value.ossId));
  let content;
  if (files.length === 1) {
    content = `是否确认删除文件【"${files.at(0).originalName}"】?`;
  } else {
    content = `是否确认删除选中的${ossIds.length}项文件?`;
  }
  proxy.$modal.confirm(content, () => {
    const msgLoading = proxy.$modal.msgLoading('正在删除中...');
    return delOss(ossIds)
      .then(() => {
        ids.value = [];
        getList();
        proxy.$modal.msgSuccess('删除成功');
      })
      .finally(() => proxy.$modal.msgClose(msgLoading));
  });
}

/** 处理鼠标区域选择 */
function handleRectChange(checkedIndexes: number[]) {
  imagePreviewIndex.value = 0;
  ids.value = ossList.value.filter((value, index) => checkedIndexes.includes(index)).map((value) => value.ossId);
}

/** 处理单击事件 */
function handleClick(oss: SysOssVo) {
  imagePreviewIndex.value = 0;
  shiftId.value = oss.ossId;
  ids.value = [oss.ossId];
}

/** 处理ctrl + 单击事件 */
function handleCtrlClick(oss: SysOssVo) {
  shiftId.value = oss.ossId;
  imagePreviewIndex.value = 0;
  if (props.multiple) {
    const index = ids.value.indexOf(oss.ossId);
    if (index !== -1) {
      ids.value.splice(index, 1);
    } else {
      ids.value.push(oss.ossId);
    }
  } else {
    handleClick(oss);
  }
}

/** 处理shift + 单击 和 ctrl + shift + 单击事件 */
function handleShiftClick(oss: SysOssVo, append = false) {
  imagePreviewIndex.value = 0;
  if (props.multiple) {
    const arr = [];
    const ossId = shiftId.value ?? ossList.value.at(0).ossId;
    const startIndex = ossList.value.findIndex((value) => value.ossId === ossId) ?? 0;
    const endIndex = ossList.value.findIndex((value) => value.ossId === oss.ossId);
    let i = Math.min(startIndex, endIndex);
    const max = Math.max(startIndex, endIndex);
    for (; i <= max; i++) {
      const value = ossList.value[i];
      if (!append || (append && !ids.value.includes(value.ossId))) {
        arr.push(value.ossId);
      }
    }
    if (append) {
      ids.value = ids.value.concat(arr);
    } else {
      ids.value = arr;
    }
  } else {
    handleClick(oss);
  }
}

/** 按钮选中 */
function buttonChecked(oss: SysOssVo) {
  imagePreviewIndex.value = 0;
  const index = ids.value.indexOf(oss.ossId);
  if (index !== -1) {
    ids.value.splice(index, 1);
  } else if (props.multiple) {
    ids.value.push(oss.ossId);
  } else {
    ids.value = [oss.ossId];
  }
}

/** 选择全部 */
function handleCheckedAll() {
  ids.value = ossList.value.map((value) => value.ossId);
}

getList();
</script>
<script lang="ts">
import ArchiveSvg from '@/assets/file-type/archive.svg?component';
import ExcelSvg from '@/assets/file-type/excel.svg?component';
import PdfSvg from '@/assets/file-type/pdf.svg?component';
import PptSvg from '@/assets/file-type/ppt.svg?component';
import RadioSvg from '@/assets/file-type/radio.svg?component';
import TextSvg from '@/assets/file-type/text.svg?component';
import UnknownSvg from '@/assets/file-type/unknown.svg?component';
import VideoSvg from '@/assets/file-type/video.svg?component';
import WordSvg from '@/assets/file-type/word.svg?component';

export default {
  name: 'MyOss',
  components: { ArchiveSvg, ExcelSvg, PdfSvg, PptSvg, RadioSvg, TextSvg, UnknownSvg, VideoSvg, WordSvg },
};
</script>
<style lang="less" scoped>
@neutral-h: 220;
@neutral-s: 10%;
.list-card {
  height: 100%;

  &-gallery {
    padding: var(--td-pop-padding-xl);
    background-color: var(--td-bg-color-container-hover);
    display: grid;
    grid-gap: 13.5px;
    grid-template-columns: repeat(auto-fill, minmax(v-bind(thumbnailSizePixel), 1fr));
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
      height: v-bind(thumbnailSizePixel);
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
      color: var(--td-text-color-primary);
      box-sizing: border-box;
      min-width: v-bind(thumbnailSizePixel);
      &:focus-visible {
        outline: none;
      }
      &__label {
        background-color: var(--td-bg-color-container);
        box-shadow: var(--td-shadow-1);
        //box-shadow: 0 0 4px 2px var(--td-border-level-1-color);
        display: block;
        overflow: hidden;
        position: relative;
        outline: 1px solid rgba(0, 0, 0, 0);
        transition: outline 200ms;
        &--active {
          outline: 1px solid var(--td-brand-color);
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
          color: var(--td-brand-color);
          opacity: 1;
        }
      }
      &:hover &__checkmark {
        opacity: 1;
      }
    }
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
.gallery-doc-icon--image {
  background-color: #f5f5f5;
}
.gallery-doc-icon--video {
  background-color: #e5f5f7;
}
.gallery-doc-icon--radio {
  background-color: #fff6ea;
}
.gallery-doc-icon--pdf {
  background-color: #ffeae8;
}
.gallery-doc-icon--ppt {
  background-color: #f9ebe8;
}
.gallery-doc-icon--text {
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
