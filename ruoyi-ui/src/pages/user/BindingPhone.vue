<template>
  <span v-if="!modify">{{ phone }}</span>
  <template v-else>
    <t-form ref="formRef" :data="form" :rules="rules" label-width="calc(4em + 41px)" @submit="submit">
      <t-form-item label="手机号码" name="phonenumber">
        <t-input v-model="form.phonenumber" :maxlength="11" />
      </t-form-item>
      <t-form-item>
        <t-button theme="primary" type="submit">保存</t-button>
      </t-form-item>
    </t-form>
  </template>
</template>
<script setup lang="ts">
defineOptions({
  name: 'BindingPhone',
});

import type { FormRule, SubmitContext } from 'tdesign-vue-next';

import { updateUserPhonenumber } from '@/api/system/profile';

const { proxy } = getCurrentInstance();
import { getCurrentInstance, ref } from 'vue';

defineProps({
  phone: {
    type: String,
    required: true,
  },
  modify: {
    type: Boolean,
    default: false,
  },
});

const rules = ref<Record<string, Array<FormRule>>>({
  phonenumber: [{ pattern: /^1[3456789][0-9]\d{8}$/, message: '请输入正确的手机号码' }],
});

const form = ref({
  phonenumber: undefined,
});

/** 提交按钮 */
function submit({ validateResult }: SubmitContext) {
  if (validateResult === true) {
    const msgLoading = proxy.$modal.msgLoading('提交中...');
    updateUserPhonenumber(form.value.phonenumber)
      .then(() => {
        proxy.$modal.msgSuccess('修改成功');
      })
      .finally(() => proxy.$modal.msgClose(msgLoading));
  }
}
</script>
