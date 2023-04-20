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
        <t-form-item :label="form[skinName].configName" :name="`['${skinName}'].configValue`">
          <t-select v-model="form[skinName].configValue" auto-width>
            <t-option label="蓝色" value="skin-blue" />
            <t-option label="绿色" value="skin-green" />
            <t-option label="紫色" value="skin-purple" />
            <t-option label="红色" value="skin-red" />
            <t-option label="黄色" value="skin-yellow" />
          </t-select>
        </t-form-item>
        <t-form-item :label="form[initPassword].configName" :name="`['${initPassword}'].configValue`">
          <t-input
            v-model="form[initPassword].configValue"
            placeholder="请输入账号初始密码"
            :maxlength="50"
            show-limit-number
          />
        </t-form-item>
        <t-form-item :label="form[sideTheme].configName" :name="`['${sideTheme}'].configValue`">
          <t-select v-model="form[sideTheme].configValue" auto-width>
            <t-option label="深色主题" value="theme-dark" />
            <t-option label="浅色主题" value="theme-light" />
          </t-select>
        </t-form-item>
        <t-form-item v-if="isSystem" :label="form[registerUser].configName" :name="`['${registerUser}'].configValue`">
          <t-select v-model="form[registerUser].configValue" auto-width>
            <t-option label="开启" value="true" />
            <t-option label="关闭" value="false" />
          </t-select>
        </t-form-item>
        <t-form-item
          v-if="isSystem"
          :label="form[previewListResource].configName"
          :name="`['${previewListResource}'].configValue`"
        >
          <t-select v-model="form[previewListResource].configValue" auto-width>
            <t-option label="开启" value="true" />
            <t-option label="关闭" value="false" />
          </t-select>
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
  name: 'SystemConfig',
};
</script>
<script lang="ts" setup>
import { FormRule } from 'tdesign-vue-next';
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
  isSystem: {
    type: Boolean,
    required: true,
  },
});

const isInit = ref(false);
const loading = ref(false);
const buttonLoading = ref(false);

// 主框架页-默认皮肤样式名称 key
const skinName = 'sys.index.skinName';
// 用户管理-账号初始密码 key
const initPassword = 'sys.user.initPassword';
// 主框架页-侧边栏主题 key
const sideTheme = 'sys.index.sideTheme';
// 账号自助-是否开启用户注册功能 key
const registerUser = 'sys.account.registerUser';
// OSS预览列表资源开关 key
const previewListResource = 'sys.oss.previewListResource';

const rules = ref<Record<string, Array<FormRule>>>({
  [`['${initPassword}'].configValue`]: [{ required: true, message: '账号初始密码不能为空', trigger: 'blur' }],
});

const form = ref<Record<string, SysConfigForm & SysConfigVo>>({
  [skinName]: {
    configKey: skinName,
    configName: '主框架页-默认皮肤样式名称',
    configType: 'Y',
    configValue: 'skin-blue',
    isGlobal: 0,
    remark: '蓝色 skin-blue、绿色 skin-green、紫色 skin-purple、红色 skin-red、黄色 skin-yellow',
  },
  [initPassword]: {
    configKey: initPassword,
    configName: '用户管理-账号初始密码',
    configType: 'Y',
    configValue: '123456',
    isGlobal: 0,
    remark: '初始化密码 123456',
  },
  [sideTheme]: {
    configKey: sideTheme,
    configName: '主框架页-侧边栏主题',
    configType: 'Y',
    configValue: 'theme-dark',
    isGlobal: 0,
    remark: '深色主题theme-dark，浅色主题theme-light',
  },
  [registerUser]: {
    configKey: registerUser,
    configName: '账号自助-是否开启用户注册功能',
    configType: 'Y',
    configValue: 'false',
    isGlobal: 1,
    remark: '是否开启注册用户功能（true开启，false关闭）',
  },
  [previewListResource]: {
    configKey: previewListResource,
    configName: 'OSS预览列表资源开关',
    configType: 'Y',
    configValue: 'true',
    isGlobal: 1,
    remark: 'true:开启, false:关闭',
  },
});

const { proxy } = getCurrentInstance();

/**
 * 提交配置
 * @param validateResult
 * @param firstError
 */
function submitForm({ validateResult, firstError }) {
  if (validateResult === true) {
    buttonLoading.value = true;
    const msgLoading = proxy.$modal.msgLoading('提交中...');
    updateConfigs(Object.values(form.value).filter((value) => value.isGlobal === 0 || props.isSystem))
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
