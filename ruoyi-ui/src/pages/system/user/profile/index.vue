<template>
  <div class="app-container">
    <t-row :gutter="20">
      <t-col :sm="3" :xs="12">
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
                <usergroup-icon />所属部门
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
      <t-col :sm="9" :xs="12">
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
          </t-tabs>
        </t-card>
      </t-col>
    </t-row>
  </div>
</template>
<script lang="ts">
export default {
  name: 'Profile',
};
</script>
<script lang="ts" setup>
import { reactive, ref } from 'vue';
import { CalendarIcon, MailIcon, MobileIcon, SecuredIcon, UsergroupIcon, UserIcon } from 'tdesign-icons-vue-next';
import { getUserProfile } from '@/api/system/user';
import UserInfo from '@/pages/system/user/profile/userInfo.vue';
import ResetPwd from '@/pages/system/user/profile/resetPwd.vue';
import UserAvatar from '@/pages/system/user/profile/userAvatar.vue';

const activeTab = ref('userinfo');
const state = reactive({
  user: {},
  roleGroup: {},
  postGroup: {},
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
