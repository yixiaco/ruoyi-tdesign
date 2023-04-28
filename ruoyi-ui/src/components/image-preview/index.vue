<template>
  <t-image-viewer :key="props.src" :default-index="0" :images="realSrcList">
    <template #trigger="{ open }">
      <t-image
        :key="realSrc"
        :src="realSrc"
        :style="{
          display: 'inline-block',
          width: realWidth,
          height: realHeight,
          'border-radius': 'var(--td-radius-default)',
        }"
        :lazy="true"
        :gallery="realSrcList.length > 1"
        overlay-trigger="hover"
        @mouseenter="hover = true"
        @mouseleave="hover = false"
      >
        <template #overlayContent>
          <div
            class="overlay"
            style="
              background: rgba(0, 0, 0, 0.4);
              color: #fff;
              height: 100%;
              display: flex;
              align-items: center;
              justify-content: center;
              border-radius: var(--td-radius-default);
            "
            @click.stop="open"
          >
            <t-tag shape="mark" theme="warning" style="border-radius: 3px; background: transparent; color: #fff">
              <browse-icon size="16" />预览
            </t-tag>
          </div>
        </template>
        <template #error>
          <image-error-icon />
        </template>
      </t-image>
    </template>
  </t-image-viewer>
</template>

<script lang="ts" setup>
import { BrowseIcon, ImageErrorIcon } from 'tdesign-icons-vue-next';
import { computed, ref, watch } from 'vue';

import { listByIds } from '@/api/system/oss';

const props = defineProps({
  src: {
    type: String,
    required: true,
  },
  width: {
    type: [Number, String],
    default: '',
  },
  height: {
    type: [Number, String],
    default: '',
  },
});

const realSrcList = ref([]);
const hover = ref(false);
const scale = computed(() => (hover.value ? 1.1 : 1));

watch(
  () => props.src,
  (value) => {
    if (value) {
      if (/^([0-9],?)+$/.test(value)) {
        // 使用id
        listByIds(value).then((res) => {
          realSrcList.value = res.data.map((item) => item.url);
        });
      } else {
        // http
        realSrcList.value = value.split(',');
      }
    } else {
      realSrcList.value = [];
    }
  },
  { immediate: true },
);

const realSrc = computed(() => {
  if (realSrcList.value.length === 0) {
    return '';
  }
  return realSrcList.value[0];
});

const realWidth = computed(() => (typeof props.width === 'string' ? props.width : `${props.width}px`));

const realHeight = computed(() => (typeof props.height === 'string' ? props.height : `${props.height}px`));
</script>
<!-- eslint-disable-next-line vue-scoped-css/enforce-style-type -->
<style lang="less">
.t-image__wrapper img,
.overlay {
  transition: all 0.3s;
  cursor: pointer;
  transform: scale(v-bind(scale));
}
</style>
