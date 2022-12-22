<template>
  <t-form ref="userRef" :data="user" :rules="rules" label-width="80px" @submit="submit">
    <t-form-item label="用户昵称" name="nickName">
      <t-input v-model="user.nickName" :maxlength="30" />
    </t-form-item>
    <t-form-item label="手机号码" name="phonenumber">
      <t-input v-model="user.phonenumber" :maxlength="11" />
    </t-form-item>
    <t-form-item label="邮箱" name="email">
      <t-input v-model="user.email" :maxlength="50" />
    </t-form-item>
    <t-form-item label="性别">
      <t-radio-group v-model="user.sex">
        <t-radio value="0">男</t-radio>
        <t-radio value="1">女</t-radio>
      </t-radio-group>
    </t-form-item>
    <t-form-item>
      <t-button theme="primary" type="submit">保存</t-button>
      <t-button theme="danger" @click="close">关闭</t-button>
    </t-form-item>
  </t-form>
</template>

<script lang="ts" setup>
import { getCurrentInstance, ref, toRefs } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { updateUserProfile } from '@/api/system/user';
import { useTabsRouterStore } from '@/store';

const tabsRouterStore = useTabsRouterStore();
const route = useRoute();
const router = useRouter();
const props = defineProps({
  user: {
    type: Object,
  },
});
const { user } = toRefs(props);

const { proxy } = getCurrentInstance();

const rules = ref({
  nickName: [{ required: true, message: '用户昵称不能为空', trigger: 'blur' }],
  email: [
    { required: true, message: '邮箱地址不能为空', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱地址', trigger: ['blur', 'change'] },
  ],
  phonenumber: [
    { required: true, message: '手机号码不能为空', trigger: 'blur' },
    { pattern: /^1[3456789][0-9]\d{8}$/, message: '请输入正确的手机号码', trigger: 'blur' },
  ],
});

/** 提交按钮 */
function submit({ validateResult }) {
  if (validateResult) {
    updateUserProfile(user.value).then(() => {
      proxy.$modal.msgSuccess('修改成功');
    });
  }
}
/** 关闭按钮 */
function close() {
  tabsRouterStore.removeCurrentTab(route, null, router);
}
</script>
