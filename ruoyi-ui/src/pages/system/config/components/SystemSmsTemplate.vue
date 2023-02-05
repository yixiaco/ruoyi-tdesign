<template>
  <div style="width: 500px; margin-top: 20px">
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
        <t-form-item label="登录模板ID" name="captchaTemplateId">
          <t-input
            v-model="form['sys.sms.captchaTemplateId']"
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
import { getCurrentInstance, reactive, ref, toRefs, watch } from 'vue';
import { getConfigByKeys, refreshCache, updateConfigs } from '@/api/system/config';
import { useHasPermission } from '@/utils/auth';

const props = defineProps({
  action: {
    type: Boolean,
    required: true,
    default: false,
  },
});

const isInit = ref(false);
// 表单禁用
const disabled = !useHasPermission(['system:config:edit']);
const loading = ref(false);
const buttonLoading = ref(false);
const data = reactive({
  form: {
    'sys.sms.captchaTemplateId': '',
  },
});

const rules = ref({});

const { form } = toRefs(data);

const { proxy } = getCurrentInstance();

/**
 * 提交配置
 * @param validateResult
 * @param firstError
 */
function submitForm({ validateResult, firstError }) {
  if (validateResult === true) {
    buttonLoading.value = true;
    updateConfigs(form.value)
      .then(() => {
        proxy.$modal.msgSuccess('更新成功');
      })
      .finally(() => {
        buttonLoading.value = false;
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
  getConfigByKeys(Object.keys(form.value).join(',')).then((res) => {
    data.form = { ...data.form, ...res.data };
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
