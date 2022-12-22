<template>
  <t-image-viewer :images="realSrcList">
    <template #trigger="{ open }">
      <t-image :src="realSrc" :style="{ width: realWidth, height: realHeight }" overlay-trigger="hover">
        <template #overlayContent>
          <div
            style="
              background: rgba(0, 0, 0, 0.4);
              color: #fff;
              height: 100%;
              display: flex;
              align-items: center;
              justify-content: center;
            "
            @click.stop="open"
          >
            <t-tag style="border-radius: 3px; background: transparent; color: #fff">
              <browse-icon size="16" /> 预览
            </t-tag>
          </div>
        </template>
      </t-image>
    </template>
  </t-image-viewer>
</template>

<script lang="ts" setup>
import { computed } from 'vue';
import { BrowseIcon } from 'tdesign-icons-vue-next';

const props = defineProps({
  src: {
    type: String,
    default: '',
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

const realSrc = computed(() => {
  if (!props.src) {
    return '';
  }
  return props.src.split(',')[0];
});

const realSrcList = computed(() => {
  if (!props.src) {
    return [];
  }
  return props.src.split(',');
});

const realWidth = computed(() => (typeof props.width === 'string' ? props.width : `${props.width}px`));

const realHeight = computed(() => (typeof props.height === 'string' ? props.height : `${props.height}px`));
</script>
