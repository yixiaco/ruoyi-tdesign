<template>
  <!-- 参考文档：https://ckeditor.com/docs/ckeditor5/latest/installation/frameworks/vuejs-v3.html#direct-script-include -->
  <ckeditor :editor="Editor" v-bind="$attrs" :config="editorConfig"></ckeditor>
</template>

<script lang="ts">
export default {
  name: 'TEditor',
};
</script>
<script lang="ts" setup>
import CKEditor from '@ckeditor/ckeditor5-vue';
import Editor from 'ckeditor5-overall-build/dist/ckeditor.js';

const uploadUrl = `${import.meta.env.VITE_APP_BASE_API}/resource/oss/upload`;
const Ckeditor = CKEditor.component;
import { storeToRefs } from 'pinia';
import { getCurrentInstance, ref } from 'vue';

import { useUserStore } from '@/store';

const { proxy } = getCurrentInstance();
const { token } = storeToRefs(useUserStore());

const editorConfig = ref({
  mediaEmbed: { previewsInData: true },
  customUpload: {
    uploadUrl,
    withCredentials: true,
    headers: {
      Authorization: `Bearer ${token.value}`,
    },
    beforeUpload(data, file) {
      const formData = new FormData();
      formData.set('file', file);
      return formData;
    },
    success(response) {
      return { default: response.data.url };
    },
    error(response) {
      const message = response && response.error && response.error.message;
      proxy.$modal.msgError(message);
      return message;
    },
  },
});
</script>
<!-- eslint-disable-next-line vue-scoped-css/enforce-style-type -->
<style lang="less">
:root {
  --ck-z-default: 10000 !important;
}
</style>
