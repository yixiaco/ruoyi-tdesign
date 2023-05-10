<template>
  <div class="upload-file">
    <t-upload
      ref="fileUpload"
      v-model="fileList"
      multiple
      :accept="accept"
      :theme="theme"
      :action="uploadFileUrl"
      :before-upload="handleBeforeUpload"
      :max="limit"
      :on-fail="handleUploadError"
      :headers="headers"
      :draggable="draggable"
      :size-limit="{ size: fileSize, unit: 'MB', message: '上传文件大小不能超过 {sizeLimit} MB!' }"
      :tips="isShowTip ? `请上传大小不超过 ${fileSize}MB 格式为 ${fileType.join('/')} 的文件` : ''"
      :disabled="disabled"
      @one-file-success="handleOneUploadSuccess"
      @success="handleUploadSuccess"
      @remove="handleDelete"
      @validate="onValidate"
    >
      <!-- 上传按钮 -->
      <t-button v-if="theme === 'custom'" theme="primary">选取文件</t-button>
    </t-upload>
  </div>
</template>

<script lang="ts" setup>
import { SuccessContext, UploadFile, UploadRemoveContext, UploadValidateType } from 'tdesign-vue-next';
import { computed, getCurrentInstance, PropType, ref, watch } from 'vue';

import { delOss, listByIds, listByUrls } from '@/api/system/oss';
import { getToken } from '@/utils/auth';

const props = defineProps({
  modelValue: [String, Object, Array],
  // 数量限制
  limit: {
    type: Number,
    default: 5,
  },
  draggable: {
    type: Boolean,
    default: false,
  },
  // 大小限制(MB)
  fileSize: {
    type: Number,
    default: 5,
  },
  // 文件类型, 例如['png', 'jpg', 'jpeg']
  fileType: {
    type: Array as PropType<Array<string>>,
    default: () => ['doc', 'docx', 'xlsx', 'xls', 'csv', 'ppt', 'pptx', 'txt', 'pdf'],
  },
  // 是否显示提示
  isShowTip: {
    type: Boolean,
    default: true,
  },
  theme: {
    type: String as PropType<'custom' | 'file' | 'file-input' | 'file-flow'>,
    default: 'file',
  },
  // 模式，id模式返回ossId，url模式返回url链接
  mode: {
    type: String as PropType<'id' | 'url'>,
    default: 'url',
  },
  disabled: {
    type: Boolean,
    default: false,
  },
});

const { proxy } = getCurrentInstance();
const emit = defineEmits(['update:modelValue', 'change']);
const baseUrl = import.meta.env.VITE_APP_BASE_API;
const uploadFileUrl = ref(`${baseUrl}/resource/oss/upload`); // 上传文件服务器地址
const headers = ref({ Authorization: `Bearer ${getToken()}` });
const fileList = ref([]);
const accept = computed(() => {
  return props.fileType.map((value) => `.${value}`).join(',');
});

watch(
  () => props.modelValue,
  async (val) => {
    if (val) {
      let temp = 1;
      // 首先将值转为数组
      let list;
      if (Array.isArray(val)) {
        list = val;
      } else if (props.mode === 'url') {
        await listByUrls(val as string).then((res) => {
          list = res.data.map((oss) => {
            return {
              name: oss.originalName,
              status: 'success',
              size: oss.size,
              uploadTime: oss.createTime,
              url: oss.url,
              ossId: oss.ossId,
            };
          });
        });
      } else if (props.mode === 'id') {
        await listByIds(val as string).then((res) => {
          list = res.data.map((oss) => {
            return {
              name: oss.originalName,
              status: 'success',
              size: oss.size,
              uploadTime: oss.createTime,
              url: oss.url,
              ossId: oss.ossId,
            };
          });
        });
      }
      // 然后将数组转为对象数组
      fileList.value = list.map((item) => {
        item = {
          name: item.name,
          status: item.status,
          size: item.size,
          uploadTime: item.uploadTime,
          url: item.url,
          ossId: item.ossId,
        };
        item.uid = item.uid || new Date().getTime() + temp++;
        return item;
      });
    } else {
      fileList.value = [];
    }
  },
  { deep: true, immediate: true },
);

// 上传前校检格式和大小
function handleBeforeUpload(file: UploadFile) {
  // 校检文件类型
  if (props.fileType.length) {
    const fileName = file.name.split('.');
    const fileExt = fileName[fileName.length - 1];
    const isTypeOk = props.fileType.indexOf(fileExt) >= 0;
    if (!isTypeOk) {
      proxy.$modal.msgError(`文件格式不正确, 请上传${props.fileType.join('/')}格式文件!`);
      return false;
    }
  }
  return true;
}

// 有文件数量超出时会触发，文件大小超出限制、文件同名时会触发等场景。注意如果设置允许上传同名文件，则此事件不会触发
const onValidate = (params: { type: UploadValidateType; files: UploadFile[] }) => {
  const { files, type } = params;
  if (type === 'FILE_OVER_SIZE_LIMIT') {
    files.map((t) => t.name).join('、');
    proxy.$modal.msgWarning(`${files.map((t) => t.name).join('、')} 等文件大小超出限制，已自动过滤`, 5000);
  } else if (type === 'FILES_OVER_LENGTH_LIMIT') {
    proxy.$modal.msgWarning('文件数量超出限制，仅上传未超出数量的文件');
  } else if (type === 'FILTER_FILE_SAME_NAME') {
    // 如果希望支持上传同名文件，请设置 allowUploadDuplicateFile={true}
    proxy.$modal.msgWarning('不允许上传同名文件');
  }
};

// 上传失败
function handleUploadError() {
  proxy.$modal.msgError('上传文件失败');
}

// 单上传成功回调，多选回调多次
function handleOneUploadSuccess(context) {
  if (context.response.code !== 200) {
    proxy.$modal.msgError(context.response.msg);
  } else {
    proxy.$modal.msgSuccess(`文件【${context.response.data.fileName}】上传成功！`);
  }
}

// 上传成功回调
function handleUploadSuccess(context: SuccessContext) {
  const uploadList = context.results
    .filter((value) => value.response.code === 200)
    .map((value) => {
      return { name: value.response.data.fileName, url: value.response.data.url, ossId: value.response.data.ossId };
    });
  uploadedSuccessfully(uploadList);
}

// 删除文件
function handleDelete({ file }: UploadRemoveContext) {
  const { ossId, name } = file;
  delOss(ossId).then(() => {
    proxy.$modal.msgSuccess(`文件【${name}】删除成功！`);
  });
  const value = listToString(fileList.value);
  emit('update:modelValue', value);
  emit('change', value);
}

// 上传结束处理
function uploadedSuccessfully(uploadList) {
  fileList.value = fileList.value.filter((f) => f.url !== undefined).concat(uploadList);
  const value = listToString(fileList.value);
  emit('update:modelValue', value);
  emit('change', value);
}

// 对象转成指定字符串分隔
function listToString(list, separator?) {
  let strs = '';
  separator = separator || ',';
  for (const i in list) {
    if (props.mode === 'url') {
      if (list[i].url) {
        strs += list[i].url + separator;
      }
    } else if (props.mode === 'id') {
      if (list[i].ossId) {
        strs += list[i].ossId + separator;
      }
    }
  }
  return strs !== '' ? strs.substring(0, strs.length - 1) : '';
}
</script>
