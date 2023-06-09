<template>
  <div>
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
        <t-form-item label="SMTP服务器域名" name="host">
          <t-input v-model="form.host" placeholder="请输入SMTP服务器域名" :maxlength="50" show-limit-number></t-input>
        </t-form-item>
        <t-form-item label="SMTP服务端口" name="port">
          <t-input-number
            v-model="form.port"
            :allow-input-over-limit="false"
            theme="normal"
            :max="65535"
            :min="0"
            :decimal-places="0"
          ></t-input-number>
        </t-form-item>
        <t-form-item
          label="发送方"
          name="from"
          help="邮件中显示的发件人姓名，遵循RFC-822标准。支持以下形式：1.user@xxx.xx 2.name <user@xxx.xx>"
        >
          <t-input v-model="form.from" placeholder="请输入发送方" :maxlength="100" show-limit-number></t-input>
        </t-form-item>
        <t-form-item label="是否需要用户名密码验证" name="auth">
          <t-select v-model="form.auth" auto-width>
            <t-option label="是" value="true" />
            <t-option label="否" value="false" />
          </t-select>
        </t-form-item>
        <t-form-item
          v-if="form.auth === 'true'"
          label="用户名"
          name="user"
          help="发件人邮箱地址（如果使用foxmail邮箱，此处为qq号）"
        >
          <t-input v-model="form.user" placeholder="请输入用户名" :maxlength="100" show-limit-number></t-input>
        </t-form-item>
        <t-form-item
          v-if="form.auth === 'true'"
          label="密码"
          name="pass"
          help="注意，某些邮箱需要为SMTP服务单独设置密码，详情查看相关帮助"
        >
          <t-input
            v-model="form.pass"
            type="password"
            placeholder="请输入密码"
            :maxlength="100"
            show-limit-number
          ></t-input>
        </t-form-item>
        <t-form-item
          label="STARTTLS安全连接"
          name="starttlsEnable"
          help="使用 STARTTLS安全连接，STARTTLS是对纯文本通信协议的扩展。它将纯文本连接升级为加密连接（TLS或SSL）， 而不是使用一个单独的加密通信端口。"
        >
          <t-select v-model="form.starttlsEnable" auto-width>
            <t-option label="使用" value="true" />
            <t-option label="不使用" value="false" />
          </t-select>
        </t-form-item>
        <t-form-item label="SSL安全连接" name="sslEnable">
          <t-select v-model="form.sslEnable" auto-width>
            <t-option label="启用" value="true" />
            <t-option label="关闭" value="false" />
          </t-select>
        </t-form-item>
        <t-form-item label="SMTP超时时长" name="timeout" help="SMTP超时时长，单位毫秒，缺省值0不超时">
          <t-input-number
            v-model="form.timeout"
            :allow-input-over-limit="false"
            theme="normal"
            suffix="毫秒"
            :min="0"
            :decimal-places="0"
          ></t-input-number>
        </t-form-item>
        <t-form-item label="Socket连接超时值" name="connectionTimeout" help="Socket连接超时值，单位毫秒，缺省值0不超时">
          <t-input-number
            v-model="form.connectionTimeout"
            :allow-input-over-limit="false"
            theme="normal"
            suffix="毫秒"
            :min="0"
            :decimal-places="0"
          ></t-input-number>
        </t-form-item>
        <t-form-item v-hasPermi="['system:config:edit']">
          <t-button theme="default" variant="outline" @click="init()">还原</t-button>
          <t-button theme="primary" type="submit" :loading="buttonLoading">保存</t-button>
          <t-button v-hasPermi="['system:config:remove']" theme="danger" variant="outline" @click="handleRefreshCache">
            刷新缓存
          </t-button>
          <t-button v-if="form.enabled === 'true'" @click="openSendTestMail()">发送测试邮件</t-button>
        </t-form-item>
      </t-form>
    </t-loading>
  </div>
</template>

<script lang="ts">
export default {
  name: 'SystemMail',
};
</script>
<script lang="ts" setup>
import { FormRule, SubmitContext } from 'tdesign-vue-next';
import { getCurrentInstance, ref, watch } from 'vue';

import { getConfigByKeys, refreshCache, updateConfigs } from '@/api/system/config';
import { sendTestMail } from '@/api/system/mail';
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

const isInit = ref(false);
const loading = ref(false);
const buttonLoading = ref(false);
const key = 'sys.mail';
const form = ref<Record<string, string>>({
  enabled: 'false',
  host: 'smtp.163.com',
  port: '465',
  auth: 'true',
  from: 'xxx@163.com',
  user: 'xxx@163.com',
  pass: 'xxxxxxxxxx',
  starttlsEnable: 'true',
  sslEnable: 'true',
  timeout: '0',
  connectionTimeout: '0',
});
const configs = ref<Record<string, SysConfigForm & SysConfigVo>>({
  [key]: {
    configKey: key,
    configName: '邮箱配置',
    configType: 'Y',
    configValue: JSON.stringify(form.value),
    isGlobal: 1,
  },
});

const rules = ref<Record<string, Array<FormRule>>>({
  host: [{ required: true, message: 'SMTP服务器域名不能为空', trigger: 'blur' }],
  port: [{ required: true, message: 'SMTP服务端口不能为空', trigger: 'blur' }],
  from: [
    { required: true, message: '发送方不能为空', trigger: 'change' },
    {
      pattern:
        /^([^ \f\n\r\t\v<]+@[^ \f\n\r\t\v<>]+\.[^ \f\n\r\t\v>]+)|(\S+ <[^ \f\n\r\t\v<]+@[^ \f\n\r\t\v<>]+\.[^ \f\n\r\t\v>]+>)$/,
      message: '发送方格式错误',
      trigger: 'change',
    },
  ],
  user: [{ required: true, message: '用户名不能为空', trigger: 'blur' }],
  pass: [{ required: true, message: '密码不能为空', trigger: 'blur' }],
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
    configs.value[key].configValue = JSON.stringify(form.value);
    updateConfigs(Object.values(configs.value))
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
  const keys = Object.keys(configs.value);
  getConfigByKeys(keys.join(',')).then((res) => {
    loading.value = false;
    keys.forEach((key) => {
      configs.value[key] = { ...configs.value[key], ...res.data[key] };
    });
    const result = res.data[key];
    if (result) {
      form.value = JSON.parse(result.configValue);
    }
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

/** 发送测试邮件 */
function openSendTestMail() {
  proxy.$modal.prompt(`电子邮件地址（例如：test@example.com）`, '发送测试邮件', {
    theme: 'primary',
    confirmButtonText: '发送',
    cancelButtonText: '取消',
    onConfirm: (value) => {
      return sendTestMail(value).then(() => {
        proxy.$modal.msgSuccess(`测试邮件已经发送至 '${value}'。`);
      });
    },
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
