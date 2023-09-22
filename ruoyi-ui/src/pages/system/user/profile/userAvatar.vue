<template>
  <div>
    <t-image
      :src="options.img"
      :style="{ width: '160px', height: '160px', margin: '0 auto' }"
      shape="circle"
      overlay-trigger="hover"
      @click="editCropper()"
    >
      <template #overlayContent>
        <div style="background: rgba(0, 0, 0, 0.4); color: #fff; height: 100%; border-radius: 50%; line-height: 160px">
          <camera-icon size="2em" />
        </div>
      </template>
    </t-image>
    <t-dialog
      v-model:visible="open"
      :close-on-overlay-click="false"
      :header="title"
      width="800px"
      attach="body"
      :footer="null"
      @opened="modalOpened"
      @close="closeDialog"
    >
      <t-row>
        <t-col :xs="12" :md="6" :style="{ height: '350px' }">
          <vue-cropper
            v-if="visible"
            ref="cropperRef"
            :img="options.img"
            :info="true"
            :auto-crop="options.autoCrop"
            :auto-crop-width="options.autoCropWidth"
            :auto-crop-height="options.autoCropHeight"
            :fixed-box="options.fixedBox"
            :output-type="options.outputType"
            @real-time="realTime"
          />
        </t-col>
        <t-col :xs="12" :md="6" :style="{ height: '350px' }">
          <div class="avatar-upload-preview">
            <img :src="previews.url" :style="previews.img" />
          </div>
        </t-col>
      </t-row>
      <br />
      <t-row>
        <t-col :sm="2" :xs="3">
          <t-upload accept=".jpg,.jpeg,.png" action="#" :show-file-list="false" :before-upload="beforeUpload">
            <t-button>
              <template #icon><upload-icon /></template>
              选择
            </t-button>
          </t-upload>
        </t-col>
        <t-col :sm="6" :xs="7">
          <t-tooltip content="放大">
            <t-button @click="changeScale(1)">
              <template #icon><zoom-in-icon /></template>
            </t-button>
          </t-tooltip>
          <t-tooltip content="缩小">
            <t-button @click="changeScale(-1)">
              <template #icon><zoom-out-icon /></template>
            </t-button>
          </t-tooltip>
          <t-tooltip content="旋转">
            <t-button @click="rotateLeft()">
              <template #icon><anticlockwise-icon /></template>
            </t-button>
          </t-tooltip>
          <t-tooltip content="缩放到裁剪框大小">
            <t-button @click="cropScale()">
              <template #icon><fullscreen1-icon /></template>
            </t-button>
          </t-tooltip>
          <t-tooltip content="还原">
            <t-button @click="refresh()">
              <template #icon><refresh-icon /></template>
            </t-button>
          </t-tooltip>
        </t-col>
        <t-col
          :sm="{ span: 3, offset: 1, order: 0, pull: 0, push: 0 }"
          :xs="{ span: 7, offset: 5, order: 0, pull: 0, push: 0 }"
        >
          <t-button theme="primary" @click="uploadImg()">提 交</t-button>
        </t-col>
      </t-row>
    </t-dialog>
  </div>
</template>

<script lang="ts" setup>
import 'vue-cropper/dist/index.css';

import {
  AnticlockwiseIcon,
  CameraIcon,
  Fullscreen1Icon,
  RefreshIcon,
  UploadIcon,
  ZoomInIcon,
  ZoomOutIcon,
} from 'tdesign-icons-vue-next';
import type { UploadFile } from 'tdesign-vue-next';
import { getCurrentInstance, reactive, ref } from 'vue';
import { VueCropper } from 'vue-cropper';

import { uploadAvatar } from '@/api/system/user';
import { useUserStore } from '@/store/modules/user';

const userStore = useUserStore();
const { proxy } = getCurrentInstance();

const open = ref(false);
const cropperRef = ref(null);
const visible = ref(false);
const title = ref('修改头像');

// 图片裁剪数据
const options = reactive({
  img: userStore.avatar, // 裁剪图片的地址
  autoCrop: true, // 是否默认生成截图框
  autoCropWidth: 200, // 默认生成截图框宽度
  autoCropHeight: 200, // 默认生成截图框高度
  fixedBox: true, // 固定截图框大小 不允许改变
  outputType: 'png', // 默认生成截图为PNG格式
  filename: '',
});
// 预览数据
const previews = ref<Record<string, any>>({});

/** 编辑头像 */
function editCropper() {
  open.value = true;
}
/** 打开弹出层结束时的回调 */
function modalOpened() {
  visible.value = true;
}
/** 向右旋转 */
function rotateLeft() {
  cropperRef.value.rotateLeft();
}
/** 缩放到指定大小 */
function cropScale() {
  const size = Math.min(cropperRef.value.trueWidth, cropperRef.value.trueHeight);
  const cropSize = Math.min(cropperRef.value.cropW, cropperRef.value.cropH);
  cropperRef.value.scale = cropSize / size;
}
/** 图片缩放 */
function changeScale(num: number) {
  num = num || 1;
  cropperRef.value.changeScale(num);
}
/** 重置 */
function refresh() {
  cropperRef.value.refresh();
}
/** 上传预处理 */
function beforeUpload(file: UploadFile) {
  if (file.type.indexOf('image/') === -1) {
    proxy.$modal.msgError('文件格式错误，请上传图片类型,如：JPG，PNG后缀的文件。');
  } else {
    const reader = new FileReader();
    reader.readAsDataURL(file.raw);
    reader.onload = () => {
      options.img = reader.result.toString();
      options.filename = file.name;
    };
  }
  return false;
}
/** 上传图片 */
function uploadImg() {
  cropperRef.value.getCropBlob((data: any) => {
    const formData = new FormData();
    formData.append('avatarfile', data, options.filename);
    const msgLoading = proxy.$modal.msgLoading('上传中...');
    uploadAvatar(formData)
      .then((response) => {
        open.value = false;
        options.img = response.data.imgUrl;
        userStore.avatar = options.img;
        proxy.$modal.msgSuccess('修改成功');
        visible.value = false;
      })
      .finally(() => proxy.$modal.msgClose(msgLoading));
  });
}
/** 实时预览 */
function realTime(data: any) {
  previews.value = data;
}
/** 关闭窗口 */
function closeDialog() {
  options.img = userStore.avatar;
}
</script>

<style lang="less" scoped>
.avatar-upload-preview {
  position: absolute;
  top: 50%;
  transform: translate(50%, -50%);
  width: 200px;
  height: 200px;
  border-radius: 50%;
  box-shadow: 0 0 4px #ccc;
  overflow: hidden;
}
</style>
