<template>
  <t-form ref="pwdRef" :data="user" :rules="rules" label-width="80px" @submit="submit">
    <t-form-item label="旧密码" name="oldPassword">
      <t-input v-model="user.oldPassword" placeholder="请输入旧密码" type="password" />
    </t-form-item>
    <t-form-item label="新密码" name="newPassword">
      <t-input v-model="user.newPassword" placeholder="请输入新密码" type="password" />
    </t-form-item>
    <t-form-item label="确认密码" name="confirmPassword">
      <t-input v-model="user.confirmPassword" placeholder="请确认新密码" type="password" />
    </t-form-item>
    <t-form-item>
      <t-button theme="primary" type="submit">保存</t-button>
      <t-button theme="danger" @click="close">关闭</t-button>
    </t-form-item>
  </t-form>
</template>

<script lang="ts" setup>
import { getCurrentInstance, reactive, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { FormRule } from 'tdesign-vue-next';
import { updateUserPwd } from '@/api/system/user';
import { useTabsRouterStore } from '@/store';

const tabsRouterStore = useTabsRouterStore();
const route = useRoute();
const router = useRouter();
const { proxy } = getCurrentInstance();

const user = reactive({
  oldPassword: undefined,
  newPassword: undefined,
  confirmPassword: undefined,
});

const equalToPassword = (value) => {
  return user.newPassword === value;
};
const rules = ref<Record<string, Array<FormRule>>>({
  oldPassword: [{ required: true, message: '旧密码不能为空', trigger: 'blur' }],
  newPassword: [
    { required: true, message: '新密码不能为空', trigger: 'blur' },
    { min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur' },
  ],
  confirmPassword: [
    { required: true, message: '确认密码不能为空', trigger: 'blur' },
    { validator: equalToPassword, trigger: 'blur', message: '两次输入的密码不一致' },
  ],
});

/** 提交按钮 */
function submit({ validateResult }) {
  if (validateResult === true) {
    updateUserPwd(user.oldPassword, user.newPassword).then(() => {
      proxy.$modal.msgSuccess('修改成功');
    });
  }
}
/** 关闭按钮 */
function close() {
  tabsRouterStore.removeCurrentTab(route, null, router);
}
</script>
