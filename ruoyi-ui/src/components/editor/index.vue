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
import Editor from 'ckeditor5-overall-build/dist/ckeditor.js';
import { ref } from 'vue';

const editorConfig = ref({});
</script>
