<template>
  <div>
    <t-table
      :loading="loading"
      hover
      row-key="id"
      :data="socialList"
      :columns="columns"
      style="width: 100%; height: 100%; font-size: 10px"
    >
      <template #avatar="{ row }">
        <image-preview :src="row.avatar" :width="45" :height="45" />
      </template>
      <template #operation="{ row }">
        <t-link theme="primary" hover="color" @click.stop="unlockAuth(row)"> 解绑 </t-link>
      </template>
    </t-table>

    <div id="git-user-binding">
      <h4 class="provider-desc">你可以绑定以下第三方帐号</h4>
      <t-space>
        <t-button shape="circle" variant="outline" @click="authUrl('gitee')">
          <template #icon>
            <gitee-svg class="t-icon" />
          </template>
        </t-button>
        <t-button shape="circle" variant="outline" @click="authUrl('github')">
          <template #icon>
            <logo-github-filled-icon />
          </template>
        </t-button>
        <t-button shape="circle" variant="outline" @click="authUrl('wechat_open')">
          <template #icon>
            <logo-wechat-icon />
          </template>
        </t-button>
        <t-button shape="circle" variant="outline" @click="authUrl('qq')">
          <template #icon>
            <logo-qq-icon />
          </template>
        </t-button>
      </t-space>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { LogoGithubFilledIcon, LogoQqIcon, LogoWechatIcon } from 'tdesign-icons-vue-next';
import { PrimaryTableCol } from 'tdesign-vue-next';
import { getCurrentInstance, onMounted, ref } from 'vue';

import { SysSocialVo } from '@/api/system/model/socialModel';
import { authBinding, authUnlock, getAuthList } from '@/api/system/social';
import GiteeSvg from '@/assets/icons/svg/gitee.svg?component';

const { proxy } = getCurrentInstance();
const loading = ref(false);
const socialList = ref<SysSocialVo[]>([]);

function getList() {
  getAuthList().then((res) => {
    socialList.value = res.data;
  });
}

// 列显隐信息
const columns = ref<Array<PrimaryTableCol>>([
  { title: `序号`, colKey: 'serial-number', width: 80, align: 'center' },
  { title: `绑定账号平台`, colKey: 'source', align: 'center' },
  { title: `头像`, colKey: 'avatar', align: 'center', width: 120 },
  { title: `登录账号`, colKey: 'userName', align: 'center', width: 180 },
  { title: `创建时间`, colKey: 'createTime', align: 'center', width: 180 },
  { title: `操作`, colKey: 'operation', align: 'center', width: 80 },
]);
// 取消授权
function unlockAuth(row: SysSocialVo) {
  proxy.$modal.confirm(`您确定要解除"${row.source}"的账号绑定吗？`, () => {
    return authUnlock(row.id).then((res) => {
      if (res.code === 200) {
        proxy.$modal.msgSuccess('解绑成功');
        getList();
      } else {
        proxy.$modal.msgError(res.msg);
      }
    });
  });
}
// 授权
function authUrl(source: string) {
  authBinding(source).then((res: any) => {
    if (res.code === 200) {
      window.location.href = res.data;
    } else {
      proxy?.$modal.msgError(res.msg);
    }
  });
}

onMounted(() => {
  getList();
});
</script>

<style lang="less" scoped>
.user-bind .third-app {
  display: -webkit-box;
  display: -ms-flexbox;
  display: flex;
  -webkit-box-orient: vertical;
  -webkit-box-direction: normal;
  -ms-flex-direction: column;
  flex-direction: column;
  -webkit-box-align: center;
  -ms-flex-align: center;
  align-items: center;
  min-width: 80px;
  float: left;
}

.user-bind {
  font-size: 1rem;
  text-align: start;
  height: 50px;
  margin-top: 10px;
}

.git-other-login-icon > img {
  height: 32px;
}

a {
  text-decoration: none;
  cursor: pointer;
  color: #005980;
}

.provider-desc {
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Helvetica, Arial, 'Apple Color Emoji', 'Segoe UI Emoji',
    'Segoe UI Symbol', 'Liberation Sans', 'PingFang SC', 'Microsoft YaHei', 'Hiragino Sans GB', 'Wenquanyi Micro Hei',
    'WenQuanYi Zen Hei', 'ST Heiti', SimHei, SimSun, 'WenQuanYi Zen Hei Sharp', sans-serif;
  font-size: 1.071rem;
}

td > img {
  height: 20px;
  width: 20px;
  display: inline-block;
  border-radius: 50%;
  margin-right: 5px;
}
</style>
