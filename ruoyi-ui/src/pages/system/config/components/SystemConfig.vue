<template>
  <div style="width: 500px; margin-top: 20px">
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
        <t-form-item label="账号自助-验证码开关" name="sys.account.captchaEnabled">
          <t-select v-model="form['sys.account.captchaEnabled']" auto-width>
            <t-option label="开启" value="true" />
            <t-option label="关闭" value="false" />
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
import { getCurrentInstance, reactive, ref, toRefs } from 'vue';
import { getConfigByKeys, refreshCache, updateConfigs } from '@/api/system/config';
import { useHasPermission } from '@/utils/auth';

// 表单禁用
const disabled = !useHasPermission(['system:config:edit']);
const loading = ref(false);
const buttonLoading = ref(false);
const data = reactive({
  form: {
    'sys.index.skinName': 'skin-blue',
    'sys.user.initPassword': '123456',
    'sys.index.sideTheme': 'theme-dark',
    'sys.account.captchaEnabled': 'true',
    'sys.account.registerUser': 'false',
    'sys.oss.previewListResource': 'true',
  },
});

const rules = ref({
  'sys.user.initPassword': [{ required: true, message: '账号初始密码不能为空', trigger: 'blur' }],
});

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

init();
</script>

<style scoped></style>
