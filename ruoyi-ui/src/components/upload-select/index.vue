<template>
  <t-dialog
    v-model:visible="visible"
    :close-on-overlay-click="false"
    :header="title"
    :width="width"
    attach="body"
    :confirm-btn="{
      content: '确 定',
      theme: 'primary',
      loading: buttonLoading,
    }"
    placement="center"
    @confirm="onConfirm"
  >
    <t-form
      v-if="visible"
      ref="formRef"
      label-align="right"
      :data="form"
      :rules="rules"
      label-width="calc(4em + 31px)"
      scroll-to-first-error="smooth"
      @submit="submitForm"
    >
      <t-tabs v-model="activeTab">
        <t-tab-panel label="我的文件" value="myOss" :destroy-on-hide="false">
          <oss-category :suffixes="suffixes" @change="handleSelectChange" />
        </t-tab-panel>
        <t-tab-panel label="外链地址" value="url">
          <br />
          <t-form-item
            v-show="activeTab === 'url'"
            label="外链地址"
            name="url"
            help="支持多张外链图片，每个图片连接占一行"
          >
            <t-textarea v-model="form.url" />
          </t-form-item>
        </t-tab-panel>
      </t-tabs>
    </t-form>
  </t-dialog>
</template>
<script setup lang="ts">
import type { FormInstanceFunctions, FormRule, SubmitContext } from 'tdesign-vue-next';
import type { PropType } from 'vue';
import { computed, getCurrentInstance, ref, watch } from 'vue';

import type { SysOssVo } from '@/api/system/model/ossModel';
import OssCategory from '@/pages/system/ossCategory/index.vue';

export interface SelectFile {
  url: string;
  name: string;
  ossId?: number;
  size?: number;
}

defineOptions({
  name: 'UploadSelect',
});

const props = defineProps({
  // 标题
  title: String,
  // 支持选择文件
  supportSelectFile: {
    type: Boolean,
    default: true,
  },
  // 支持手动输入url
  supportUrl: {
    type: Boolean,
    default: true,
  },
  onSubmit: {
    type: Function as PropType<(values: SelectFile[]) => boolean>,
  },
  suffixes: {
    type: Array as PropType<string[]>,
    default: () => [],
  },
});

// 显示隐藏窗口
const visible = defineModel('visible', {
  type: Boolean,
  default: false,
});
const { proxy } = getCurrentInstance();

const form = ref({
  url: '',
});
const buttonLoading = ref(false);
const formRef = ref<FormInstanceFunctions>();
const activeTab = ref('myOss');
const selectValues = ref<SysOssVo[]>([]);
watch(
  () => [props.supportSelectFile, props.supportUrl],
  () => {
    if (!props.supportSelectFile) {
      activeTab.value = 'url';
    }
    if (!props.supportUrl) {
      activeTab.value = 'myOss';
    }
  },
  { immediate: true },
);

const rules = ref<Record<string, Array<FormRule>>>({
  url: [{ required: true, message: '外链地址不能为空' }],
});
const width = computed(() => {
  if (activeTab.value === 'myOss') {
    return 'calc(min(1200px, 100vw))';
  }
  return '500px';
});

function handleSelectChange(values: SysOssVo[]) {
  selectValues.value = values;
}
/** 表单提交按钮 */
function onConfirm() {
  formRef.value.submit();
}
/** 提交表单 */
function submitForm({ validateResult, firstError }: SubmitContext) {
  if (validateResult === true) {
    if (activeTab.value === 'myOss') {
      const values = selectValues.value.map((value) => ({
        url: value.url,
        name: value.originalName,
        ossId: value.ossId,
        size: value.size,
      }));
      const result = props.onSubmit.call(this, values);
      if (result) {
        visible.value = false;
      }
    } else {
      // TODO: 处理手动输入的url
      visible.value = false;
    }
  } else {
    proxy.$modal.msgError(firstError);
  }
}
</script>
