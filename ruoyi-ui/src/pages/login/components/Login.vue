<template>
  <t-form
    ref="form"
    :class="['item-container', `login-${type}`]"
    :data="formData"
    :rules="FORM_RULES"
    label-width="0"
    @submit="onSubmit"
  >
    <template v-if="type === 'password'">
      <t-form-item name="account">
        <t-input v-model="formData.account" size="large" placeholder="请输入账号">
          <template #prefix-icon>
            <t-icon name="user" />
          </template>
        </t-input>
      </t-form-item>

      <t-form-item name="password">
        <t-input
          v-model="formData.password"
          size="large"
          :type="showPsw ? 'text' : 'password'"
          clearable
          placeholder="请输入登录密码"
        >
          <template #prefix-icon>
            <t-icon name="lock-on" />
          </template>
          <template #suffix-icon>
            <t-icon :name="showPsw ? 'browse' : 'browse-off'" @click="showPsw = !showPsw" />
          </template>
        </t-input>
      </t-form-item>

      <t-form-item class="verification-code" name="code">
        <t-input v-model="formData.code" size="large" placeholder="请输入验证码">
          <template #label>
            <secured-icon />
          </template>
        </t-input>
        <div class="login-code">
          <img :src="codeUrl" class="login-code-img" @click="getCode" />
        </div>
        <!--        <t-button variant="outline" :disabled="countDown > 0" @click="sendCode">-->
        <!--          {{ countDown === 0 ? '发送验证码' : `${countDown}秒后可重发` }}-->
        <!--        </t-button>-->
      </t-form-item>

      <div class="check-container remember-pwd">
        <t-checkbox v-model="formData.rememberMe">记住账号</t-checkbox>
        <span class="tip">忘记账号？</span>
      </div>
    </template>

    <!-- 扫码登陆 -->
    <template v-else-if="type === 'qrcode'">
      <div class="tip-container">
        <span class="tip">请使用微信扫一扫登录</span>
        <span class="refresh">刷新 <t-icon name="refresh" /> </span>
      </div>
      <qrcode-vue value="" :size="192" level="H" />
    </template>

    <!-- 手机号登陆 -->
    <template v-else>
      <t-form-item name="phone">
        <t-input v-model="formData.phone" size="large" placeholder="请输入手机号码">
          <template #prefix-icon>
            <t-icon name="mobile" />
          </template>
        </t-input>
      </t-form-item>

      <t-form-item class="verification-code" name="code">
        <t-input v-model="formData.code" size="large" placeholder="请输入验证码" />
        <t-button variant="outline" :disabled="countDown > 0" @click="sendCode">
          {{ countDown === 0 ? '发送验证码' : `${countDown}秒后可重发` }}
        </t-button>
      </t-form-item>
    </template>

    <t-form-item v-if="type !== 'qrcode'" class="btn-container">
      <t-button block size="large" type="submit" :loading="loading"> {{ loading ? '登录中...' : '登录' }} </t-button>
    </t-form-item>

    <div class="switch-container">
      <span v-if="type !== 'password'" class="tip" @click="switchType('password')">使用账号密码登录</span>
      <span v-if="type !== 'qrcode'" class="tip" @click="switchType('qrcode')">使用微信扫码登录</span>
      <span v-if="type !== 'phone'" class="tip" @click="switchType('phone')">使用手机号登录</span>
    </div>
  </t-form>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import QrcodeVue from 'qrcode.vue';
import { MessagePlugin } from 'tdesign-vue-next';
import type { FormInstanceFunctions, FormRule } from 'tdesign-vue-next';
import Cookies from 'js-cookie';
import { SecuredIcon } from 'tdesign-icons-vue-next';
import { useCounter } from '@/hooks';
import { useUserStore } from '@/store';
import { getCodeImg } from '@/api/login';
import { encrypt, decrypt } from '@/utils/jsencrypt';
import { LoginParam } from '@/api/model/loginModel';

const userStore = useUserStore();

const INITIAL_DATA = {
  phone: '',
  account: 'admin',
  password: 'admin123',
  rememberMe: false,
  code: '',
  uuid: '',
};

const FORM_RULES: Record<string, FormRule[]> = {
  phone: [{ required: true, message: '手机号必填', type: 'error' }],
  account: [{ required: true, message: '账号必填', type: 'error' }],
  password: [{ required: true, message: '密码必填', type: 'error' }],
  code: [{ required: true, message: '验证码必填', type: 'error' }],
};

const type = ref('password');

const form = ref<FormInstanceFunctions>();
const formData = ref({ ...INITIAL_DATA });
const showPsw = ref(false);
const codeUrl = ref('');
const loading = ref(false);
// 验证码开关
const captchaEnabled = ref(true);

const [countDown, handleCounter] = useCounter();

const switchType = (val: string) => {
  type.value = val;
};

const router = useRouter();
const route = useRoute();

function getCode() {
  getCodeImg().then((res) => {
    captchaEnabled.value = res.data.captchaEnabled === undefined ? true : res.data.captchaEnabled;
    if (captchaEnabled.value) {
      codeUrl.value = `data:image/gif;base64,${res.data.img}`;
      formData.value.uuid = res.data.uuid;
    }
  });
}

function getCookie() {
  const account = Cookies.get('account');
  const password = Cookies.get('password');
  const rememberMe = Cookies.get('rememberMe');
  formData.value = {
    account: account === undefined ? formData.value.account : account,
    phone: '',
    password: password === undefined ? formData.value.password : decrypt(password),
    rememberMe: rememberMe === undefined ? false : Boolean(rememberMe),
    code: '',
    uuid: '',
  };
}

/**
 * 发送验证码
 */
const sendCode = () => {
  form.value.validate({ fields: ['phone'] }).then((e) => {
    if (e === true) {
      handleCounter();
    }
  });
};

const onSubmit = async ({ validateResult }) => {
  if (validateResult === true) {
    try {
      loading.value = true;
      let username;
      switch (type.value) {
        case 'password':
          username = formData.value.account;
          break;
        case 'phone':
          username = formData.value.phone;
          break;
        default:
      }
      const loginParam: LoginParam = {
        username,
        password: formData.value.password,
        code: formData.value.code,
        uuid: formData.value.uuid,
      };
      await userStore.login(loginParam);
      // 勾选了需要记住密码设置在 cookie 中设置记住用户名和密码
      if (formData.value.rememberMe && type.value === 'password') {
        Cookies.set('account', formData.value.account, { expires: 30 });
        Cookies.set('password', encrypt(formData.value.password), { expires: 30 });
        Cookies.set('rememberMe', formData.value.rememberMe.toString(), { expires: 30 });
      } else {
        // 否则移除
        Cookies.remove('account');
        Cookies.remove('password');
        Cookies.remove('rememberMe');
      }

      MessagePlugin.success('登陆成功');
      const redirect = route.query.redirect as string;
      const redirectUrl = redirect ? decodeURIComponent(redirect) : '/';
      router.push(redirectUrl);
    } catch (e) {
      // 重新获取验证码
      if (captchaEnabled.value) {
        getCode();
      }
    } finally {
      loading.value = false;
    }
  }
};

getCode();
getCookie();
</script>

<style lang="less" scoped>
@import url('../index.less');
</style>
