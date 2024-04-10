<template>
  <t-space direction="vertical" style="width: 100%">
    <t-table :loading="loading" hover row-key="id" :data="socialList" :columns="columns" style="font-size: 12px">
      <template #avatar="{ row }">
        <image-preview :src="row.avatar" preview-text="" :width="45" :height="45" />
      </template>
      <template #operation="{ row }">
        <t-link theme="primary" hover="color" @click.stop="unlockAuth(row)"> 解绑 </t-link>
      </template>
    </t-table>

    <h3>你可以绑定以下第三方帐号</h3>
    <t-space>
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
      <t-button shape="circle" variant="outline" @click="authUrl('maxkey')">
        <template #icon>
          <max-key class="t-icon" />
        </template>
      </t-button>
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
    </t-space>
  </t-space>
</template>

<script lang="ts" setup>
defineOptions({
  name: 'ThirdParty',
});
import { LogoGithubFilledIcon, LogoQqIcon, LogoWechatIcon } from 'tdesign-icons-vue-next';
import type { PrimaryTableCol } from 'tdesign-vue-next';
import { getCurrentInstance, onMounted, ref } from 'vue';

import type { SysSocialVo } from '@/api/system/model/socialModel';
import { authBinding, authUnlock, getAuthList } from '@/api/system/social';
import GiteeSvg from '@/assets/icons/svg/gitee.svg?component';
import MaxKey from '@/assets/icons/svg/maxkey.svg?component';

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
  { title: `头像`, colKey: 'avatar', align: 'center', minWidth: 120 },
  { title: `登录账号`, colKey: 'userName', align: 'center', minWidth: 180 },
  { title: `创建时间`, colKey: 'createTime', align: 'center', minWidth: 180 },
  { title: `操作`, colKey: 'operation', align: 'center', minWidth: 80 },
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
      window.open(res.data, '_blank');
    } else {
      proxy?.$modal.msgError(res.msg);
    }
  });
}

onMounted(() => {
  getList();
});
</script>
