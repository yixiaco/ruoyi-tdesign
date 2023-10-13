<template>
  <t-card>
    <t-row :gutter="20">
      <t-col :xl="3" :sm="5" :xs="10">
        <t-card class="box-card" shadow>
          <template #header>
            <div class="clearfix">
              <span>个人信息</span>
            </div>
          </template>
          <div>
            <div class="text-center">
              <user-avatar :user="state.user" />
            </div>
            <ul class="list-group list-group-striped">
              <li class="list-group-item">
                <user-icon />用户名称
                <div class="pull-right">{{ state.user.userName }}</div>
              </li>
              <li class="list-group-item">
                <mobile-icon />手机号码
                <div class="pull-right">{{ state.user.phonenumber }}</div>
              </li>
              <li class="list-group-item">
                <mail-icon />用户邮箱
                <div class="pull-right">{{ state.user.email }}</div>
              </li>
              <li class="list-group-item">
                <tree-square-dot-icon />所属部门
                <div v-if="state.user.dept" class="pull-right">
                  {{ state.user.dept.deptName }} / {{ state.postGroup }}
                </div>
              </li>
              <li class="list-group-item">
                <secured-icon />所属角色
                <div class="pull-right">{{ state.roleGroup }}</div>
              </li>
              <li class="list-group-item">
                <calendar-icon />创建日期
                <div class="pull-right">{{ state.user.createTime }}</div>
              </li>
            </ul>
          </div>
        </t-card>
      </t-col>
      <t-col :xl="9" :sm="7" :xs="12">
        <t-card shadow>
          <template #header>
            <div class="clearfix">
              <span>基本资料</span>
            </div>
          </template>
          <t-tabs v-model="activeTab">
            <t-tab-panel label="基本资料" value="userinfo">
              <user-info :user="state.user" />
            </t-tab-panel>
            <t-tab-panel label="修改密码" value="resetPwd">
              <reset-pwd />
            </t-tab-panel>
            <t-tab-panel label="第三方应用" value="thirdParty" :destroy-on-hide="false">
              <third-party />
            </t-tab-panel>
          </t-tabs>
        </t-card>
      </t-col>
    </t-row>
  </t-card>
</template>
<script lang="ts" setup>
defineOptions({
  name: 'Profile',
});
import {
  CalendarIcon,
  MailIcon,
  MobileIcon,
  SecuredIcon,
  TreeSquareDotIcon,
  UserIcon
} from 'tdesign-icons-vue-next';
import { reactive, ref } from 'vue';

import type { ProfileVo } from '@/api/system/model/userModel';
import { getUserProfile } from '@/api/system/user';
import ResetPwd from '@/pages/system/user/profile/resetPwd.vue';
import ThirdParty from '@/pages/system/user/profile/thirdParty.vue';
import UserAvatar from '@/pages/system/user/profile/userAvatar.vue';
import UserInfo from '@/pages/system/user/profile/userInfo.vue';

const activeTab = ref('userinfo');
const state = reactive<ProfileVo>({
  user: {},
  roleGroup: '',
  postGroup: '',
});

function getUser() {
  getUserProfile().then((response) => {
    state.user = response.data.user;
    state.roleGroup = response.data.roleGroup;
    state.postGroup = response.data.postGroup;
  });
}

getUser();
</script>
<style lang="less" scoped>
:deep .t-tabs__content {
  margin-top: 24px;
}
</style>
