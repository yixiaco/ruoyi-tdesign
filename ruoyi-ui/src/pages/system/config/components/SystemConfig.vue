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
        <t-form-item label="主框架页-默认皮肤样式名称" name="sys.index.skinName">
          <t-select v-model="form['sys.index.skinName']" auto-width>
            <t-option label="蓝色" value="skin-blue" />
            <t-option label="绿色" value="skin-green" />
            <t-option label="紫色" value="skin-purple" />
            <t-option label="红色" value="skin-red" />
            <t-option label="黄色" value="skin-yellow" />
          </t-select>
        </t-form-item>
        <t-form-item label="用户管理-账号初始密码" name="sys.user.initPassword">
          <t-input
            v-model="form['sys.user.initPassword']"
            placeholder="请输入账号初始密码"
            :maxlength="50"
            show-limit-number
          ></t-input>
        </t-form-item>
        <t-form-item label="主框架页-侧边栏主题" name="sys.index.sideTheme">
          <t-select v-model="form['sys.index.sideTheme']" auto-width>
            <t-option label="深色主题" value="theme-dark" />
            <t-option label="浅色主题" value="theme-light" />
          </t-select>
        </t-form-item>
        <t-form-item label="账号自助-是否开启用户注册功能" name="sys.account.registerUser">
          <t-select v-model="form['sys.account.registerUser']" auto-width>
            <t-option label="开启" value="true" />
            <t-option label="关闭" value="false" />
          </t-select>
        </t-form-item>
        <t-form-item label="OSS预览列表资源开关" name="sys.oss.previewListResource">
          <t-select v-model="form['sys.oss.previewListResource']" auto-width>
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

const rules = ref<Record<string, Array<FormRule>>>({
  'sys.user.initPassword': [{ required: true, message: '账号初始密码不能为空', trigger: 'blur' }],
});

const form = ref<Record<string, string>>({
  'sys.index.skinName': 'skin-blue',
  'sys.user.initPassword': '123456',
  'sys.index.sideTheme': 'theme-dark',
  'sys.account.registerUser': 'false',
  'sys.oss.previewListResource': 'true',
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
    updateConfigs(form.value)
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
  getConfigByKeys(Object.keys(form.value).join(',')).then((res) => {
    form.value = res.data;
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
