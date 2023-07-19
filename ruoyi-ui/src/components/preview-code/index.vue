<template>
  <pre :class="'language-' + language + `${isShowLineNumbers ? ' line-numbers' : ''}`">
<code ref="html" :class="'language-'+ language" v-text="code" />
  </pre>
</template>

<script setup lang="ts">
import Prism from 'prismjs';
import { onMounted, onUpdated, ref } from 'vue';

const html = ref<HTMLElement>();

defineProps({
  // 代码
  code: {
    type: String,
    default: '',
  },
  // 语言
  language: {
    type: String,
    default: 'html',
  },
  // 是否显示行号
  isShowLineNumbers: {
    type: Boolean,
    default: true,
  },
});

onMounted(() => {
  Prism.highlightElement(html.value); // 切换菜单重新渲染
});

onUpdated(() => {
  Prism.highlightElement(html.value); // 切换菜单重新渲染
});
</script>
