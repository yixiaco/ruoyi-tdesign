<template>
  <t-image-viewer :key="props.src" :default-index="0" :images="realPreviewSrcList">
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
        :class="{ image_animation: animation }"
        :lazy="lazy"
        :gallery="realPreviewSrcList.length > 1"
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
  // 显示地址
  src: {
    type: String,
  },
  // 预览地址，为空则使用src显示地址
  previewSrc: {
    type: String,
  },
  width: {
    type: [Number, String],
    default: '',
  },
  height: {
    type: [Number, String],
    default: '',
  },
  // 是否懒加载
  lazy: {
    type: Boolean,
    default: true,
  },
  // 是否运行动画
  animation: {
    type: Boolean,
    default: true,
  },
});

const realSrc = ref('');
const realPreviewSrcList = ref([]);
const hover = ref(false);
const scale = computed(() => (hover.value ? 1.1 : 1));

watch(
  () => props.src,
  (value) => {
    if (value) {
      if (/^([0-9],?)+$/.test(value)) {
        const id = value.split(',')[0];
        // 使用id
        listByIds(id).then((res) => {
          realSrc.value = res.data?.length > 0 ? res.data[0].url : '';
        });
      } else {
        // http
        realSrc.value = value.split(/,(?=http)/)[0];
      }
    } else {
      realPreviewSrcList.value = [];
    }
  },
  { immediate: true },
);
watch(
  () => props.previewSrc || props.src,
  (value) => {
    if (value) {
      if (/^([0-9],?)+$/.test(value)) {
        // 使用id
        listByIds(value).then((res) => {
          realPreviewSrcList.value = res.data.map((item) => item.url);
        });
      } else {
        // http
        realPreviewSrcList.value = value.split(/,(?=http)/);
      }
    } else {
      realPreviewSrcList.value = [];
    }
  },
  { immediate: true },
);

const realWidth = computed(() => (typeof props.width === 'string' ? props.width : `${props.width}px`));

const realHeight = computed(() => (typeof props.height === 'string' ? props.height : `${props.height}px`));
</script>
<style lang="less" scoped>
.image_animation:deep {
  img,
  .overlay {
    transition: all 0.3s;
    cursor: pointer;
    transform: scale(v-bind(scale));
  }
}
</style>
