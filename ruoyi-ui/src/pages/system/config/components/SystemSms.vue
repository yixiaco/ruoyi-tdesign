<template>
  <div style="width: 600px; margin-top: 20px">
    <t-card header="短信平台配置">
      <t-loading :loading="loading">
        <t-form
          label-align="right"
          colon
          :data="form"
          :rules="rules"
          reset-type="initial"
          label-width="calc(15em + 24px)"
          scroll-to-first-error="smooth"
          :disabled="disabled"
          @submit="submitForm"
        >
          <t-form-item label="是否启用" name="enabled">
            <t-select v-model="form.enabled" auto-width>
              <t-option label="开启" value="true" />
              <t-option label="关闭" value="false" />
            </t-select>
          </t-form-item>
          <t-form-item label="平台" name="endpoint">
            <t-select v-model="form.endpoint" auto-width>
              <t-option label="阿里云" value="dysmsapi.aliyuncs.com" />
              <t-option label="腾讯云" value="sms.tencentcloudapi.com" />
            </t-select>
          </t-form-item>
          <t-form-item label="用户密钥对ID（accessKeyId）" name="accessKeyId">
            <t-input
              v-model="form.accessKeyId"
              placeholder="请输入用户密钥对ID"
              :maxlength="100"
              show-limit-number
            ></t-input>
          </t-form-item>
          <t-form-item label="密钥（accessKeySecret）" name="accessKeySecret">
            <t-input
              v-model="form.accessKeySecret"
              type="password"
              placeholder="请输入密钥"
              :maxlength="100"
              show-limit-number
            ></t-input>
            <template #help> {{ accessKeyDoc }} </template>
          </t-form-item>
          <t-form-item label="短信签名" name="signName">
            <t-input v-model="form.signName" placeholder="请输入短信签名" :maxlength="100" show-limit-number></t-input>
            <template #help>
              {{ signNameDoc }}
            </template>
          </t-form-item>
          <t-form-item v-if="form.endpoint === 'sms.tencentcloudapi.com'" label="短信应用ID" name="sdkAppId">
            <t-input
              v-model="form.sdkAppId"
              placeholder="请输入短信应用ID"
              :maxlength="100"
              show-limit-number
            ></t-input>
            <template #help>
              应用 ID 可前往 [短信控制台](https://console.cloud.tencent.com/smsv2/app-manage) 查看
            </template>
          </t-form-item>
          <t-form-item v-hasPermi="['system:config:edit']">
            <t-button theme="default" variant="outline" @click="init()">还原</t-button>
            <t-button theme="primary" type="submit" :loading="buttonLoading">保存</t-button>
            <t-button
              v-hasPermi="['system:config:remove']"
              theme="danger"
              variant="outline"
              @click="handleRefreshCache"
            >
              刷新缓存
            </t-button>
          </t-form-item>
        </t-form>
      </t-loading>
    </t-card>
    <br />
    <t-card header="短信模板配置">
      <system-sms-template :action="action" />
    </t-card>
  </div>
</template>

<script lang="ts">
export default {
  name: 'SystemSms',
};
</script>
<script lang="ts" setup>
import { getCurrentInstance, watch, reactive, ref, toRefs, computed } from 'vue';
import { getConfigByKeys, refreshCache, updateConfigs } from '@/api/system/config';
import { useHasPermission } from '@/utils/auth';
import SystemSmsTemplate from './SystemSmsTemplate.vue';

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
const key = 'sys.sms';
const data = reactive({
  form: {
    enabled: 'false',
    endpoint: 'dysmsapi.aliyuncs.com',
    accessKeyId: 'xxxxxxx',
    accessKeySecret: 'xxxxxxx',
    signName: '',
    sdkAppId: '',
  },
});

const rules = ref({
  accessKeyId: [{ required: true, message: 'accessKeyId不能为空', trigger: 'blur' }],
  accessKeySecret: [{ required: true, message: 'accessKeySecret不能为空', trigger: 'blur' }],
  signName: [{ required: true, message: '短信签名不能为空', trigger: 'blur' }],
  sdkAppId: [{ required: true, message: '短信应用ID为空', trigger: 'blur' }],
});

const { form } = toRefs(data);

const { proxy } = getCurrentInstance();
/// 密钥对文档说明
const accessKeyDoc = computed(() => {
  switch (form.value.endpoint) {
    case 'dysmsapi.aliyuncs.com':
      return '如何获取AccessKey，请查询: (https://help.aliyun.com/document_detail/116401.htm) 或\n (https://usercenter.console.aliyun.com/#/manage/ak)';
    case 'sms.tencentcloudapi.com':
      return 'SecretId、SecretKey 查询: https://console.cloud.tencent.com/cam/capi';
    default:
      return '';
  }
});
// 短信签名文档说明
const signNameDoc = computed(() => {
  switch (form.value.endpoint) {
    case 'dysmsapi.aliyuncs.com':
      return '签名信息可前往 https://dysms.console.aliyun.com/domestic/text/sign 的签名管理查看';
    case 'sms.tencentcloudapi.com':
      return (
        '签名信息可前往 [国内短信](https://console.cloud.tencent.com/smsv2/csms-sign) 或\n' +
        '[国际/港澳台短信](https://console.cloud.tencent.com/smsv2/isms-sign) 的签名管理查看'
      );
    default:
      return '';
  }
});

/**
 * 提交配置
 * @param validateResult
 * @param firstError
 */
function submitForm({ validateResult, firstError }) {
  if (validateResult === true) {
    buttonLoading.value = true;
    updateConfigs({ [key]: JSON.stringify(form.value) })
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
  getConfigByKeys(key).then((res) => {
    loading.value = false;
    let result = res.data[key];
    result = (result && JSON.parse(result)) || {};
    data.form = { ...data.form, ...result };
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