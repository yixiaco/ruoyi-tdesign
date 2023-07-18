<template>
  <pre ref="html" :class="'language-' + language + `${isShowLineNumbers ? ' line-numbers' : ''}`">
<code :class="'language-'+ language" v-html="Prism.highlight(code, Prism.languages[language], language)" />
  </pre>
</template>

<script setup lang="ts">
import Prism from 'prismjs';
import { ref } from 'vue';

const html = ref<HTMLElement>();

defineProps({
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

// onMounted(() => {
//   Prism.highlightElement(html.value); // 切换菜单重新渲染
// });
</script>
