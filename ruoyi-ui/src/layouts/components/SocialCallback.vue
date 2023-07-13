<template>
  <t-loading :loading="loading">
    <div class="social-callback"></div>
  </t-loading>
</template>

<script lang="ts" setup>
import { storeToRefs } from 'pinia';
import { MessagePlugin } from 'tdesign-vue-next';
import { nextTick, onMounted, ref } from 'vue';
import { useRoute } from 'vue-router';

import { callback, login } from '@/api/login';
import { LoginData } from '@/api/model/loginModel';
import { useUserStore } from '@/store';

const route = useRoute();
const loading = ref(true);
const { token, tenantId } = storeToRefs(useUserStore());

/**
 * 接收Route传递的参数
 * @param {Object} route.query.
 */
const code = route.query.code as string;
const state = route.query.state as string;
const source = route.query.source as string;

const processResponse = async (res: any) => {
  if (res.code !== 200) {
    throw new Error(res.msg);
  }
  if (res.data !== null) {
    token.value = res.data.access_token;
  }
  MessagePlugin.success(res.msg);
  window.location.href = `${import.meta.env.VITE_APP_CONTEXT_PATH}index`;
};

const handleError = (error: any) => {
  MessagePlugin.error(error.message);
  window.location.href = `${import.meta.env.VITE_APP_CONTEXT_PATH}index`;
};

const callbackByCode = async (data: LoginData) => {
  try {
    const res = await callback(data);
    await processResponse(res);
    loading.value = false;
  } catch (error) {
    handleError(error);
  }
};

const loginByCode = async (data: LoginData) => {
  try {
    const res = await login(data);
    await processResponse(res);
    loading.value = false;
  } catch (error) {
    handleError(error);
  }
};

const init = async () => {
  const data: LoginData = {
    socialCode: code,
    socialState: state,
    tenantId: tenantId.value,
    source,
    clientId: import.meta.env.VITE_CLIENT_ID,
    grantType: 'social',
  };

  if (!token.value) {
    await loginByCode(data);
  } else {
    await callbackByCode(data);
  }
};

onMounted(() => {
  nextTick(() => {
    init();
  });
});
</script>
