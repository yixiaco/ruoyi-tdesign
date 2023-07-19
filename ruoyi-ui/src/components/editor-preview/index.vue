<template>
  <div class="editor_preview line-numbers">
    <div ref="html" v-html="htmlText"></div>
  </div>
</template>
<script setup lang="ts">
import Prism from 'prismjs';
import { nextTick, onMounted, onUpdated, ref } from 'vue';

defineProps({
  htmlText: {
    type: String,
    required: true,
  },
});

const html = ref<HTMLElement>();

/**
 * 渲染元素
 */
function renderHtml() {
  nextTick(() => {
    html.value?.querySelectorAll('pre[class*="language-"] code')?.forEach((el) => {
      Prism.highlightElement(el, false);
    });
    html.value.querySelectorAll('pre[class*="language-"]').forEach((value) => {
      if (!value.classList.contains('line-numbers')) {
        value.classList.add('line-numbers');
      }
    });
  });
}

onMounted(() => {
  renderHtml();
});
onUpdated(() => {
  renderHtml();
});
</script>

<style scoped lang="less">
.editor_preview :deep(div) {
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, Cantarell, 'Open Sans',
    'Helvetica Neue', sans-serif;
  line-height: 1.4;
  margin: 1rem;
  pre.line-numbers {
    white-space: pre-wrap;
  }
  table {
    border-collapse: collapse;
  }
  table:not([cellpadding]) td,
  table:not([cellpadding]) th {
    padding: 0.4rem;
  }
  table[border]:not([border='0']):not([style*='border-width']) td,
  table[border]:not([border='0']):not([style*='border-width']) th {
    border-width: 1px;
  }
  table[border]:not([border='0']):not([style*='border-style']) td,
  table[border]:not([border='0']):not([style*='border-style']) th {
    border-style: solid;
  }
  table[border]:not([border='0']):not([style*='border-color']) td,
  table[border]:not([border='0']):not([style*='border-color']) th {
    border-color: #ccc;
  }
  figure {
    display: table;
    margin: 1rem auto;
  }
  figure figcaption {
    color: #999;
    display: block;
    margin-top: 0.25rem;
    text-align: center;
  }
  hr {
    border-color: #ccc;
    border-style: solid;
    border-width: 1px 0 0 0;
  }
  code {
    border-radius: 3px;
    padding: 0.1rem 0.2rem;
  }
  blockquote p:not([dir='rtl']) {
    border-left: 2px solid #ccc;
    margin-left: 1.5rem;
    padding-left: 1rem;
  }
  blockquote p[dir='rtl'] {
    border-right: 2px solid #ccc;
    margin-right: 1.5rem;
    padding-right: 1rem;
  }
}
</style>
