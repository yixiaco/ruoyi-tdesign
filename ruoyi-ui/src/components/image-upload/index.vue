<template>
  <div class="component-upload-image">
    <t-upload
      ref="imageUpload"
      v-model="fileList"
      multiple
      :action="uploadImgUrl"
      :before-upload="handleBeforeUpload"
      :max="limit"
      :on-fail="handleUploadError"
      :headers="headers"
      :theme="theme"
      :draggable="draggable"
      :accept="rawAccept"
      :size-limit="{ size: fileSize, unit: 'MB', message: '上传图片大小不能超过 {sizeLimit} MB!' }"
      :tips="isShowTip ? `请上传大小不超过 ${fileSize}MB 格式为 ${fileType.join('/')} 的文件` : ''"
      :disabled="disabled"
      @one-file-success="handleOneUploadSuccess"
      @success="handleUploadSuccess"
      @remove="handleDelete"
      @validate="onValidate"
    >
      <slot v-if="theme === 'custom'" />
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
  // 图片数量限制
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
  // 接受上传的文件类型
  accept: {
    type: Array as PropType<
      Array<
        | 'image/gif'
        | 'image/jpeg'
        | 'image/png'
        | 'image/svg+xml'
        | 'image/tiff'
        | 'image/vnd.wap.wbmp'
        | 'image/webp'
        | 'image/x-icon'
        | 'image/x-jng'
        | 'image/x-ms-bmp'
        | 'image/*'
      >
    >,
  },
  // 文件类型, 例如['png', 'jpg', 'jpeg']
  fileType: {
    type: Array as PropType<Array<string>>,
    default: () => ['png', 'jpg', 'jpeg', 'gif', 'webp', 'bmp'],
  },
  // 是否显示提示
  isShowTip: {
    type: Boolean,
    default: true,
  },
  theme: {
    type: String as PropType<'custom' | 'image' | 'image-flow'>,
    default: 'image',
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
const uploadImgUrl = ref(`${baseUrl}/resource/oss/upload`); // 上传的图片服务器地址
const headers = ref({ Authorization: `Bearer ${getToken()}` });
const fileList = ref([]);
const rawAccept = computed(() => {
  return props.accept || props.fileType.map((value) => `.${value}`).join(',');
});

watch(
  () => props.modelValue,
  async (val) => {
    if (val) {
      // 首先将值转为数组
      let list;
      if (Array.isArray(val)) {
        list = val;
      } else if (props.mode === 'url') {
        await listByUrls(val).then((res) => {
          list = res.data;
        });
      } else if (props.mode === 'id') {
        await listByIds(val).then((res) => {
          list = res.data;
        });
      }
      // 然后将数组转为对象数组
      fileList.value = list.map((item) => {
        // 字符串回显处理 如果此处存的是url可直接回显 如果存的是id需要调用接口查出来
        if (typeof item === 'string') {
          item = { name: item, status: 'success', url: item };
        } else {
          // 此处name使用ossId 防止删除出现重名
          item = {
            name: item.ossId,
            status: 'success',
            size: item.size,
            uploadTime: item.createTime,
            url: item.url,
            ossId: item.ossId,
          };
        }
        return item;
      });
    } else {
      fileList.value = [];
      return [];
    }
  },
  { deep: true, immediate: true },
);

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

// 上传前loading加载
function handleBeforeUpload(file) {
  let isImg: boolean;
  if (props.fileType.length) {
    let fileExtension = '';
    if (file.name.lastIndexOf('.') > -1) {
      fileExtension = file.name.slice(file.name.lastIndexOf('.') + 1);
    }
    isImg = props.fileType.some((type) => {
      if (file.type.indexOf(type) > -1) return true;
      return fileExtension && fileExtension.indexOf(type) > -1;
    });
  } else {
    isImg = file.type.indexOf('image') > -1;
  }
  if (!isImg) {
    proxy.$modal.msgError(`文件格式不正确, 请上传${props.fileType.join('/')}图片格式文件!`);
    return false;
  }
  return true;
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

// 删除图片
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

// 上传失败
function handleUploadError() {
  proxy.$modal.msgError('上传图片失败');
}

// 对象转成指定字符串分隔
function listToString(list, separator?) {
  let strs = '';
  separator = separator || ',';
  for (const i in list) {
    if (props.mode === 'url') {
      if (list[i].url && list[i].url.indexOf('blob:') !== 0) {
        strs += list[i].url + separator;
      }
    } else if (props.mode === 'id') {
      if (list[i].ossId && list[i].url.indexOf('blob:') !== 0) {
        strs += list[i].ossId + separator;
      }
    }
  }
  return strs !== '' ? strs.substring(0, strs.length - 1) : '';
}
</script>
