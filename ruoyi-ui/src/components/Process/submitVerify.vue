<template>
  <t-dialog
    v-model:visible="dialog.visible"
    :header="dialog.title"
    width="50%"
    :close-on-overlay-click="false"
    placement="center"
    @close="cancel"
  >
    <t-loading :loading="loading">
      <t-form :data="form" label-width="120px">
        <t-form-item label="消息提醒">
          <t-checkbox-group v-model="form.messageType">
            <t-checkbox label="1" name="type" disabled>站内信</t-checkbox>
            <t-checkbox label="2" name="type">邮件</t-checkbox>
            <t-checkbox label="3" name="type">短信</t-checkbox>
          </t-checkbox-group>
        </t-form-item>
        <t-form-item v-if="task.businessStatus === 'waiting'" label="附件">
          <fileUpload
            v-model="form.fileId"
            :file-type="['doc', 'xls', 'ppt', 'txt', 'pdf', 'xlsx', 'docx', 'zip']"
            :file-size="20"
          />
        </t-form-item>
        <t-form-item label="抄送">
          <t-button theme="primary" shape="circle" @click="openUserSelectCopy">
            <template #icon><add-icon /></template>
          </t-button>
          <t-tag
            v-for="user in selectCopyUserList"
            :key="user.userId"
            closable
            style="margin: 2px"
            @close="handleCopyCloseTag(user)"
          >
            {{ user.userName }}
          </t-tag>
        </t-form-item>
        <t-form-item v-if="task.businessStatus === 'waiting'" label="审批意见">
          <t-textarea v-model="form.message" />
        </t-form-item>
      </t-form>
    </t-loading>
    <template #footer>
      <span class="dialog-footer">
        <t-button :loading="buttonLoading" theme="primary" @click="handleCompleteTask"> 提交 </t-button>
        <t-button
          v-if="task.businessStatus === 'waiting' && task.multiInstance"
          :loading="buttonLoading"
          theme="primary"
          @click="openTransferTask"
        >
          转办
        </t-button>
        <t-button
          v-if="task.businessStatus === 'waiting' && task.multiInstance"
          :loading="buttonLoading"
          theme="primary"
          @click="addMultiInstanceUser"
        >
          加签
        </t-button>
        <t-button
          v-if="task.businessStatus === 'waiting' && task.multiInstance"
          :loading="buttonLoading"
          theme="primary"
          @click="deleteMultiInstanceUser"
        >
          减签
        </t-button>
        <t-button
          v-if="task.businessStatus === 'waiting'"
          :loading="buttonLoading"
          theme="danger"
          @click="handleBackProcess"
        >
          退回
        </t-button>
        <t-button :loading="buttonLoading" variant="outline" @click="cancel">取消</t-button>
      </span>
    </template>
    <!-- 抄送 -->
    <user-select
      ref="userSelectCopyRef"
      :multiple="userMultiple"
      :data="selectCopyUserIds"
      @confirm-call-back="userSelectCopyCallBack"
    />
    <!-- 转办 -->
    <user-select ref="transferTaskRef" :multiple="userMultiple" @confirm-call-back="handleTransferTask"></user-select>
    <!-- 加签组件 -->
    <multi-instance-user ref="multiInstanceUserRef" :title="title" @submit-callback="closeDialog" />
  </t-dialog>
</template>

<script lang="ts" setup>
import { AddIcon } from 'tdesign-icons-vue-next';
import { ref } from 'vue';

import type { SysUserVo } from '@/api/system/model/userModel';
import { backProcess, completeTask, getTaskById, transferTask } from '@/api/workflow/task';
import type { TaskVo } from '@/api/workflow/task/types';
import MultiInstanceUser from '@/components/Process/multiInstanceUser.vue';
import UserSelect from '@/components/user-select/index.vue';

const { proxy } = getCurrentInstance();

const userSelectCopyRef = ref<InstanceType<typeof UserSelect>>();
const transferTaskRef = ref<InstanceType<typeof UserSelect>>();
// 加签组件
const multiInstanceUserRef = ref<InstanceType<typeof MultiInstanceUser>>();

const props = defineProps({
  taskVariables: {
    type: Object as PropType<Record<string, any>>,
    default: () => {},
  },
});
// 遮罩层
const loading = ref(true);
// 按钮
const buttonLoading = ref(true);
// 任务id
const taskId = ref<string>('');
// 抄送人
const selectCopyUserList = ref<SysUserVo[]>([]);
// 抄送人id
const selectCopyUserIds = ref<string>(undefined);
// 是否多选人员
const userMultiple = ref(false);

// 任务
const task = ref<TaskVo>({
  id: undefined,
  name: undefined,
  description: undefined,
  priority: undefined,
  owner: undefined,
  assignee: undefined,
  assigneeName: undefined,
  processInstanceId: undefined,
  executionId: undefined,
  taskDefinitionId: undefined,
  processDefinitionId: undefined,
  endTime: undefined,
  taskDefinitionKey: undefined,
  dueDate: undefined,
  category: undefined,
  parentTaskId: undefined,
  tenantId: undefined,
  claimTime: undefined,
  businessStatus: undefined,
  businessStatusName: undefined,
  processDefinitionName: undefined,
  processDefinitionKey: undefined,
  participantVo: undefined,
  multiInstance: undefined,
});
// 加签 减签标题
const title = ref('');
const dialog = reactive({
  visible: false,
  title: '提示',
});

const form = ref<Record<string, any>>({
  taskId: undefined,
  message: undefined,
  variables: {},
  messageType: ['1'],
  wfCopyList: [],
});
const closeDialog = () => {
  dialog.visible = false;
};
// 打开弹窗
const openDialog = (id?: string) => {
  selectCopyUserIds.value = undefined;
  selectCopyUserList.value = [];
  form.value.fileId = undefined;
  taskId.value = id;
  form.value.message = undefined;
  dialog.visible = true;
  loading.value = true;
  buttonLoading.value = true;
  nextTick(() => {
    getTaskById(taskId.value).then((response) => {
      task.value = response.data;
      loading.value = false;
      buttonLoading.value = false;
    });
  });
};

onMounted(() => {});
const emits = defineEmits(['submitCallback', 'cancelCallback']);

/** 办理流程 */
const handleCompleteTask = async () => {
  form.value.taskId = taskId.value;
  form.value.taskVariables = props.taskVariables;
  if (selectCopyUserList.value && selectCopyUserList.value.length > 0) {
    const wfCopyList: SysUserVo[] = [];
    selectCopyUserList.value.forEach((e) => {
      const copyUser = {
        userId: e.userId,
        userName: e.nickName,
      };
      wfCopyList.push(copyUser);
    });
    form.value.wfCopyList = wfCopyList;
  }
  proxy?.$modal.confirm('是否确认提交？', async () => {
    loading.value = true;
    buttonLoading.value = true;
    await completeTask(form.value).finally(() => (loading.value = false));
    dialog.visible = false;
    emits('submitCallback');
    await proxy?.$modal.msgSuccess('操作成功');
  });
};

/** 驳回流程 */
const handleBackProcess = async () => {
  form.value.taskId = taskId.value;
  proxy?.$modal.confirm('是否确认驳回到申请人？', async () => {
    loading.value = true;
    buttonLoading.value = true;
    await backProcess(form.value).finally(() => (loading.value = false));
    dialog.visible = false;
    emits('submitCallback');
    await proxy?.$modal.msgSuccess('操作成功');
  });
};
// 取消
const cancel = async () => {
  dialog.visible = false;
  buttonLoading.value = false;
  emits('cancelCallback');
};
// 打开抄送人员
const openUserSelectCopy = () => {
  userMultiple.value = true;
  userSelectCopyRef.value.open();
};
// 确认抄送人员
const userSelectCopyCallBack = (data: SysUserVo[]) => {
  if (data && data.length > 0) {
    selectCopyUserList.value = data;
    selectCopyUserIds.value = selectCopyUserList.value.map((item) => item.userId).join(',');
  }
};
// 删除抄送人员
const handleCopyCloseTag = (user: SysUserVo) => {
  const userId = user.userId;
  // 使用split删除用户
  const index = selectCopyUserList.value.findIndex((item) => item.userId === userId);
  selectCopyUserList.value.splice(index, 1);
  selectCopyUserIds.value = selectCopyUserList.value.map((item) => item.userId).join(',');
};
// 加签
const addMultiInstanceUser = () => {
  if (multiInstanceUserRef.value) {
    title.value = '加签人员';
    multiInstanceUserRef.value.getAddMultiInstanceList(taskId.value, []);
  }
};
// 减签
const deleteMultiInstanceUser = () => {
  if (multiInstanceUserRef.value) {
    title.value = '减签人员';
    multiInstanceUserRef.value.getDeleteMultiInstanceList(taskId.value);
  }
};
// 打开转办
const openTransferTask = () => {
  userMultiple.value = false;
  transferTaskRef.value.open();
};
// 转办
const handleTransferTask = async (data: SysUserVo[]) => {
  if (data && data.length > 0) {
    const params = {
      taskId: taskId.value,
      userId: data[0].userId,
      comment: form.value.message,
    };
    proxy?.$modal.confirm('是否确认提交？', async () => {
      loading.value = true;
      buttonLoading.value = true;
      await transferTask(params).finally(() => (loading.value = false));
      dialog.visible = false;
      emits('submitCallback');
      proxy?.$modal.msgSuccess('操作成功');
    });
  } else {
    proxy?.$modal.msgWarning('请选择用户！');
  }
};
/**
 * 对外暴露子组件方法
 */
defineExpose({
  openDialog,
});
</script>
