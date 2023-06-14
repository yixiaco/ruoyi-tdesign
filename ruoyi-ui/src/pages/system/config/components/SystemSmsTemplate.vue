<template>
  <div>
    <t-loading :loading="loading">
      <t-form
        label-align="right"
        colon
        :data="form"
        :rules="rules"
        reset-type="initial"
        label-width="calc(6em + 24px)"
        scroll-to-first-error="smooth"
        :disabled="disabled"
        @submit="submitForm"
      >
        <t-form-item :label="form[captchaTemplateId].configName" :name="`['${captchaTemplateId}'].configValue`">
          <t-input
            v-model="form[captchaTemplateId].configValue"
            placeholder="请输入登录模板ID"
            :maxlength="50"
            show-limit-number
          ></t-input>
          <template #help>
            需要验证码场景，例如：注册、登录、修改密码、修改绑定手机号（阿里云模板固定参数${code}）
          </template>
        </t-form-item>
        <t-form-item v-hasPermi="['system:config:edit']">
          <t-button theme="default" variant="outline" @click="init()">还原</t-button>
          <t-button theme="primary" type="submit" :loading="buttonLoading">保存</t-button>
          <t-button v-hasPermi="['system:config:remove']" theme="danger" variant="outline" @click="handleRefreshCache">
            刷新缓存
          </t-button>
        </t-form-item>
      </t-form>
    </t-loading>
  </div>
</template>

<script lang="ts">
export default {
  name: 'SystemSmsTemplate',
};
</script>
<script lang="ts" setup>
import { FormRule, SubmitContext } from 'tdesign-vue-next';
import { getCurrentInstance, ref, watch } from 'vue';

import { getConfigByKeys, refreshCache, updateConfigs } from '@/api/system/config';
import { SysConfigForm, SysConfigVo } from '@/api/system/model/configModel';

const props = defineProps({
  action: {
    type: Boolean,
    required: true,
    default: false,
  },
  disabled: {
    type: Boolean,
    required: true,
  },
});

// 登录模板ID key
const captchaTemplateId = 'sys.sms.captchaTemplateId';

const isInit = ref(false);
const loading = ref(false);
const buttonLoading = ref(false);
const form = ref<Record<string, SysConfigForm & SysConfigVo>>({
  [captchaTemplateId]: {
    configKey: captchaTemplateId,
    configName: '登录模板ID',
    configType: 'Y',
    configValue: '',
    isGlobal: 0,
    // eslint-disable-next-line no-template-curly-in-string
    remark: '需要验证码场景，例如：注册、登录、修改密码、修改绑定手机号（阿里云模板固定参数${code}）',
  },
});

const rules = ref<Record<string, Array<FormRule>>>({
  [`['${captchaTemplateId}'].configValue`]: [{ required: true, message: '登录模板ID不能为空', trigger: 'blur' }],
});

const { proxy } = getCurrentInstance();

/**
 * 提交配置
 * @param validateResult
 * @param firstError
 */
function submitForm({ validateResult, firstError }: SubmitContext) {
  if (validateResult === true) {
    buttonLoading.value = true;
    const msgLoading = proxy.$modal.msgLoading('提交中...');
    updateConfigs(Object.values(form.value))
      .then(() => {
        proxy.$modal.msgSuccess('更新成功');
      })
      .finally(() => {
        buttonLoading.value = false;
        proxy.$modal.msgClose(msgLoading);
      });
  } else {
    proxy.$modal.msgError(firstError);
  }
}

/**
 * 读取配置
 */
function init() {
  loading.value = true;
  const keys = Object.keys(form.value);
  getConfigByKeys(keys.join(',')).then((res) => {
    keys.forEach((key) => {
      form.value[key] = { ...form.value[key], ...res.data[key] };
    });
    loading.value = false;
  });
}

/** 刷新缓存按钮操作 */
function handleRefreshCache() {
  proxy.$modal.confirm(`是否确认刷新缓存？`, () => {
    return refreshCache().then(() => {
      proxy.$modal.msgSuccess('刷新缓存成功');
    });
  });
}

watch(
  () => props.action,
  (val) => {
    if (!isInit.value && val) {
      isInit.value = true;
      init();
    }
  },
  { immediate: true },
);
</script>

<style scoped></style>
