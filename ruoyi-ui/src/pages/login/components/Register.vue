<template>
  <t-form
    ref="form"
    :class="['item-container', `register-${type}`]"
    :data="formData"
    :rules="FORM_RULES"
    label-width="0"
    @submit="onSubmit"
  >
    <t-form-item name="tenantId">
      <t-select v-model="formData.tenantId" size="large" filterable placeholder="请选择/输入公司名称">
        <template #prefixIcon>
          <company class="t-icon" />
        </template>
        <t-option v-for="item in tenantList" :key="item.tenantId" :label="item.companyName" :value="item.tenantId" />
      </t-select>
    </t-form-item>
    <template v-if="type === 'phone'">
      <t-form-item name="phone">
        <t-input v-model="formData.phone" :maxlength="11" size="large" placeholder="请输入您的手机号">
          <template #prefix-icon>
            <t-icon name="user" />
          </template>
        </t-input>
      </t-form-item>
    </template>

    <template v-if="type === 'email'">
      <t-form-item name="email">
        <t-input v-model="formData.email" type="text" size="large" placeholder="请输入您的邮箱">
          <template #prefix-icon>
            <t-icon name="mail" />
          </template>
        </t-input>
      </t-form-item>
    </template>

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

    <template v-if="type === 'phone' || type === 'email'">
      <t-form-item class="verification-code" name="code">
        <t-input v-model="formData.code" size="large" placeholder="请输入验证码" />
        <t-button variant="outline" :disabled="countDown > 0" @click="handleCounter">
          {{ countDown === 0 ? '发送验证码' : `${countDown}秒后可重发` }}
        </t-button>
      </t-form-item>
    </template>

    <template v-if="type === 'password' && captchaEnabled">
      <t-form-item class="verification-code" name="code">
        <t-input v-model="formData.code" size="large" placeholder="请输入验证码">
          <template #label>
            <secured-icon />
          </template>
        </t-input>
        <div class="login-code">
          <img :src="codeUrl" class="login-code-img" @click="getCode" />
        </div>
      </t-form-item>
    </template>

    <t-form-item class="check-container" name="checked">
      <t-checkbox v-model="formData.checked">我已阅读并同意 </t-checkbox> <span>TDesign服务协议</span> 和
      <span>TDesign 隐私声明</span>
    </t-form-item>

    <t-form-item>
      <t-button :loading="loading" block size="large" type="submit"> {{ loading ? '注册中...' : '注册' }} </t-button>
    </t-form-item>

    <div class="switch-container">
      <span class="tip" @click="switchType(type === 'phone' ? 'email' : 'phone')">{{
        type === 'phone' ? '使用邮箱注册' : '使用手机号注册'
      }}</span>
    </div>
  </t-form>
</template>

<script setup lang="ts">
import { getCurrentInstance, ref } from 'vue';
import { MessagePlugin, FormRule } from 'tdesign-vue-next';
import { SecuredIcon } from 'tdesign-icons-vue-next';
import { useCounter } from '@/hooks';
import { RegisterBody, TenantListVo } from '@/api/model/loginModel';
import { getCodeImg, register, getTenantList } from '@/api/login';
import Company from '@/assets/icons/svg/company.svg?component';

const equalToPassword = (value) => {
  return formData.value.password === value;
};

const FORM_RULES: Record<string, FormRule[]> = {
  tenantId: [{ required: true, message: '请输入您的租户编号', type: 'error' }],
  phone: [{ required: true, message: '手机号必填', type: 'error' }],
  email: [
    { required: true, message: '邮箱必填', type: 'error' },
    { email: true, message: '请输入正确的邮箱', type: 'warning' },
  ],
  password: [{ required: true, message: '密码必填', type: 'error' }],
  confirmPassword: [
    { required: true, message: '请再次输入您的密码', trigger: 'blur' },
    { validator: equalToPassword, trigger: 'blur', message: '两次输入的密码不一致' },
  ],
  code: [{ required: true, message: '请输入验证码' }],
};

const loading = ref(false);
const type = ref('phone');
const codeUrl = ref('');
const form = ref();
const formData = ref({
  tenantId: '',
  phone: '',
  email: '',
  account: '',
  password: '',
  confirmPassword: '',
  code: '',
  uuid: '',
  userType: 'sys_user',
  checked: false,
});
const tenantList = ref<TenantListVo[]>([]);
// 验证码开关
const captchaEnabled = ref(true);
const showPsw = ref(false);

const [countDown, handleCounter] = useCounter();

const emit = defineEmits(['registerSuccess']);

const { proxy } = getCurrentInstance();

const onSubmit = ({ validateResult }) => {
  if (validateResult === true) {
    if (type.value === 'phone' || type.value === 'email') {
      MessagePlugin.warning('暂不支持手机号与邮箱注册');
      return;
    }
    if (!formData.value.checked) {
      MessagePlugin.error('请同意TDesign服务协议和TDesign 隐私声明');
      return;
    }

    loading.value = true;
    const registerForm: RegisterBody = {
      username: formData.value.account,
      code: formData.value.code,
      password: formData.value.password,
      userType: 'sys_user',
      uuid: formData.value.uuid,
    };
    register(registerForm)
      .then(() => {
        MessagePlugin.success('注册成功');
        emit('registerSuccess');
        const username = registerForm.username;
        proxy.$modal.alert({
          header: `系统提示`,
          body: `恭喜你，您的账号 ${username} 注册成功！`,
          theme: 'success',
        });
      })
      .catch(() => {
        loading.value = false;
        if (captchaEnabled.value) {
          getCode();
        }
      });
  }
};

const switchType = (val) => {
  form.value.reset();
  type.value = val;
};

function getCode() {
  getCodeImg().then((res) => {
    captchaEnabled.value = res.data.captchaEnabled === undefined ? true : res.data.captchaEnabled;
    if (captchaEnabled.value) {
      codeUrl.value = `data:image/gif;base64,${res.data.img}`;
      formData.value.uuid = res.data.uuid;
    }
  });
}

/**
 * 获取租户列表
 */
function initTenantList() {
  getTenantList().then((res) => {
    tenantList.value = res.data;
    if (tenantList.value != null && tenantList.value.length !== 0) {
      formData.value.tenantId = tenantList.value[0].tenantId;
    }
  });
}

getCode();
initTenantList();
</script>

<style lang="less" scoped>
@import url('../index.less');
</style>
