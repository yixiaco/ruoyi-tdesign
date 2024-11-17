<template>
  <div>
    <t-form ref="formRef" size="default" :data="formData" :rules="formRules" label-width="100px">
      <t-collapse v-model="currentCollapseItem" expand-icon-placement="right" borderless>
        <t-collapse-panel value="1">
          <template #header>
            <div class="collapse__title">
              <info-circle-filled-icon />
              常规
            </div>
          </template>
          <div>
            <t-form-item name="id" label="节点 ID">
              <t-input v-model="formData.id" @change="idChange($event as string)"> </t-input>
            </t-form-item>
            <t-form-item name="name" label="节点名称">
              <t-input v-model="formData.name" @change="nameChange($event as string)"> </t-input>
            </t-form-item>
            <t-form-item v-if="showConfig.skipExpression" name="skipExpression" label="跳过表达式">
              <t-input v-model="formData.skipExpression" @change="skipExpressionChange($event as string)"> </t-input>
            </t-form-item>
            <t-loading :loading="formManageListLoading">
              <t-form-item name="formKey" label="表单地址">
                <t-select
                  v-model="formData.formKey"
                  clearable
                  filterable
                  placeholder="请选择表单"
                  style="width: 260px"
                  @change="formKeyChange($event as string)"
                >
                  <t-option
                    v-for="item in formManageList"
                    :key="item.id"
                    :label="item.formTypeName + ':' + item.formName"
                    :value="item.formType + ':' + item.id"
                  />
                </t-select>
              </t-form-item>
            </t-loading>
          </div>
        </t-collapse-panel>
        <t-divider style="margin: 5px 0" />

        <t-collapse-panel value="2">
          <template #header>
            <div class="collapse__title">
              <task-icon />
              任务
            </div>
          </template>
          <div>
            <t-form-item v-if="showConfig.async" name="sync" label="是否异步">
              <t-switch v-model="formData.async" :label="['是', '否']" @change="syncChange" />
            </t-form-item>

            <t-tabs placement="left" class="demo-tabs" default-value="1">
              <t-tab-panel label="身份存储" value="1">
                <t-form-item label="分配人员">
                  <t-input v-model="formData.assignee" @blur="blurAssignee(formData.assignee)">
                    <template #suffixIcon>
                      <search-icon class="cursor-pointer" @click="openSingleUserSelect" />
                    </template>
                  </t-input>
                </t-form-item>
                <t-form-item label="候选人员">
                  <t-badge :count="selectUserLength" size="small" show-zero :max-count="99">
                    <t-button size="small" theme="primary" @click="openUserSelect">选择人员</t-button>
                  </t-badge>
                </t-form-item>
                <t-form-item label="候选组">
                  <t-badge :count="selectRoleLength" size="small" show-zero :max-count="99">
                    <t-button size="small" theme="primary" @click="openRoleSelect">选择组</t-button>
                  </t-badge>
                </t-form-item>
              </t-tab-panel>

              <!--              <t-tab-panel label="固定值" value="2">
                <t-form-item name="auditUserType" label="分配类型">
                  <t-select v-model="formData.allocationType">
                    <t-option
                      v-for="item in AllocationTypeSelect"
                      :key="item.id"
                      :value="item.value"
                      :label="item.label"
                    >
                    </t-option>
                  </t-select>
                </t-form-item>
                <t-form-item v-if="formData.allocationType === AllocationTypeEnum.USER" label="分配人员">
                  <t-input v-model="formData.assignee>
                    <template #suffixIcon>
                      &lt;!&ndash;                      <search-icon class="cursor-pointer" @click="openSingleUserSelect" />&ndash;&gt;
                    </template>
                  </t-input>
                </t-form-item>
                <div v-if="formData.allocationType === AllocationTypeEnum.CANDIDATE">
                  <t-form-item label="候选人员">
                    <t-badge :count="selectUserLength" size="small" show-zero :max-count="99">
                      <t-button size="small" theme="primary" @click="openUserSelect">选择人员</t-button>
                    </t-badge>
                  </t-form-item>
                  <t-form-item label="候选组">
                    <t-badge :count="selectRoleLength" size="small" show-zero :max-count="99">
                      <t-button size="small" theme="primary" @click="openRoleSelect">选择组</t-button>
                    </t-badge>
                  </t-form-item>
                </div>
                <t-form-item
                  v-if="formData.allocationType === AllocationTypeEnum.SPECIFY && showConfig.specifyDesc"
                  style=""
                >
                  <t-radio-group v-model="formData.specifyDesc" class="ml-4">
                    <t-radio v-for="item in SpecifyDesc" :key="item.id" :value="item.value" size="large">
                      {{ item.label }}
                    </t-radio>
                  </t-radio-group>
                </t-form-item>
              </t-tab-panel>-->
            </t-tabs>

            <t-form-item v-if="showConfig.dueDate" name="dueDate" label="到期时间">
              <t-input
                v-model="formData.dueDate"
                clearable
                @change="dueDateChange($event as string)"
                @click="openDueDate"
              >
                <template #suffixIcon>
                  <search-icon class="cursor-pointer" @click="openDueDate" />
                </template>
              </t-input>
            </t-form-item>
            <t-form-item v-if="showConfig.priority" name="priority" label="优先级">
              <t-input-number v-model="formData.priority" :min="0" @change="priorityChange"> </t-input-number>
            </t-form-item>
          </div>
        </t-collapse-panel>
        <t-divider style="margin: 5px 0" />

        <t-collapse-panel value="3">
          <template #header>
            <div class="collapse__title">
              <tab-icon />
              多实例
            </div>
          </template>
          <div>
            <t-form-item label="多实例类型">
              <t-select v-model="formData.multiInstanceType" @change="multiInstanceTypeChange">
                <t-option
                  v-for="item in constant.MultiInstanceType"
                  :key="item.id"
                  :value="item.value"
                  :label="item.label"
                >
                </t-option>
              </t-select>
            </t-form-item>

            <div v-if="formData.multiInstanceType !== MultiInstanceTypeEnum.NONE">
              <t-form-item label="集合">
                <template #label>
                  <span>
                    集合
                    <t-tooltip placement="top">
                      <help-circle-filled-icon />
                      <template #content>
                        属性会作为表达式进行解析。如果表达式解析为字符串而不是一个集合，<br />
                        不论是因为本身配置的就是静态字符串值，还是表达式计算结果为字符串，<br />
                        这个字符串都会被当做变量名，并从流程变量中用于获取实际的集合。
                      </template>
                    </t-tooltip>
                  </span>
                </template>
                <t-input v-model="formData.collection" @change="collectionChange($event as string)"></t-input>
              </t-form-item>
              <t-form-item label="元素变量">
                <template #label>
                  <span>
                    元素变量
                    <t-tooltip placement="top">
                      <help-circle-filled-icon />
                      <template #content>
                        每创建一个用户任务前，先以该元素变量为label，集合中的一项为value，<br />
                        创建（局部）流程变量，该局部流程变量被用于指派用户任务。<br />
                        一般来说，该字符串应与指定人员变量相同。
                      </template>
                    </t-tooltip>
                  </span>
                </template>
                <t-input v-model="formData.elementVariable" @change="elementVariableChange($event as string)">
                </t-input>
              </t-form-item>
              <t-form-item label="完成条件">
                <template #label>
                  <span>
                    完成条件
                    <t-tooltip placement="top">
                      <help-circle-filled-icon />
                      <template #content>
                        多实例活动在所有实例都完成时结束，然而也可以指定一个表达式，在每个实例<br />
                        结束时进行计算。当表达式计算为true时，将销毁所有剩余的实例，并结束多实例<br />
                        活动，继续执行流程。例如 ${nrOfCompletedInstances/nrOfInstances >= 0.6 }，<br />
                        表示当任务完成60%时，该节点就算完成
                      </template>
                    </t-tooltip>
                  </span>
                </template>
                <t-input v-model="formData.completionCondition" @change="completionConditionChange($event as string)" />
              </t-form-item>
            </div>
          </div>
        </t-collapse-panel>
        <t-divider v-if="showConfig.taskListener" style="margin: 5px 0" />

        <t-collapse-panel v-if="showConfig.taskListener" value="4">
          <template #header>
            <div class="collapse__title">
              <time-filled-icon />
              任务监听器
            </div>
          </template>
          <div>
            <task-listener v-if="showConfig.taskListener" :element="element"></task-listener>
          </div>
        </t-collapse-panel>
        <t-divider v-if="showConfig.executionListener" style="margin: 5px 0" />

        <t-collapse-panel v-if="showConfig.executionListener" value="5">
          <template #header>
            <div class="collapse__title">
              <time-filled-icon />
              执行监听器
            </div>
          </template>
          <div>
            <execution-listener v-if="showConfig.executionListener" :element="element"></execution-listener>
          </div>
        </t-collapse-panel>

        <t-form-item v-if="showConfig.isForCompensation" name="isForCompensation" label="是否为补偿">
          <t-switch v-model="formData.isForCompensation" inline-prompt active-text="是" inactive-text="否" />
        </t-form-item>
        <t-form-item v-if="showConfig.triggerServiceTask" name="triggerServiceTask" label="服务任务可触发">
          <t-switch v-model="formData.triggerServiceTask" inline-prompt active-text="是" inactive-text="否" />
        </t-form-item>
        <t-form-item v-if="showConfig.autoStoreVariables" name="autoStoreVariables" label="自动存储变量">
          <t-switch v-model="formData.autoStoreVariables" inline-prompt active-text="是" inactive-text="否" />
        </t-form-item>
        <t-form-item v-if="showConfig.ruleVariablesInput" name="skipExpression" label="输入变量">
          <t-input v-model="formData.ruleVariablesInput"> </t-input>
        </t-form-item>
        <t-form-item v-if="showConfig.exclude" name="exclude" label="排除">
          <t-switch v-model="formData.exclude" inline-prompt active-text="是" inactive-text="否" />
        </t-form-item>
        <t-form-item v-if="showConfig.class" name="class" label="类">
          <t-input v-model="formData.class"> </t-input>
        </t-form-item>
      </t-collapse>
    </t-form>
    <user-select ref="userSelectRef" :data="formData.candidateUsers" @confirm-call-back="userSelectCallBack" />
    <user-select
      ref="singleUserSelectRef"
      :data="formData.assignee"
      :multiple="false"
      @confirm-call-back="singleUserSelectCallBack"
    />
    <role-select ref="roleSelectRef" :data="formData.candidateGroups" @confirm-call-back="roleSelectCallBack" />
    <due-date
      ref="dueDateRef"
      v-model="formData.dueDate"
      :data="formData.dueDate"
      @confirm-call-back="dueDateCallBack"
    />
  </div>
</template>
<script setup lang="ts">
import type { ModdleElement } from 'bpmn';
import type { TaskPanel } from 'bpmnDesign';
import {
  HelpCircleFilledIcon,
  InfoCircleFilledIcon,
  SearchIcon,
  TabIcon,
  TaskIcon,
  TimeFilledIcon,
} from 'tdesign-icons-vue-next';
import type { FormProps } from 'tdesign-vue-next';

import type { SysRoleVo } from '@/api/system/model/roleModel';
import type { SysUserVo } from '@/api/system/model/userModel';
import { selectListFormManage } from '@/api/workflow/formManage';
import type { WfFormManageVo } from '@/api/workflow/model/formManageModel';
import RoleSelect from '@/components/role-select/index.vue';
import UserSelect from '@/components/user-select/index.vue';
import { AllocationTypeEnum, MultiInstanceTypeEnum, SpecifyDescEnum } from '@/enums/bpmn/IndexEnums';

import usePanel from '../hooks/usePanel';
import useParseElement from '../hooks/useParseElement';
import DueDate from './property/DueDate.vue';
import ExecutionListener from './property/ExecutionListener.vue';
import TaskListener from './property/TaskListener.vue';

const formManageList = ref<WfFormManageVo[]>([]);
const formManageListLoading = ref(false);
interface PropType {
  element: ModdleElement;
}
const props = withDefaults(defineProps<PropType>(), {});
const {
  showConfig,
  nameChange,
  formKeyChange,
  idChange,
  updateProperties,
  getExtensionElements,
  createModdleElement,
  constant,
} = usePanel({
  element: toRaw(props.element),
});
const { parseData } = useParseElement({
  element: toRaw(props.element),
});

const initFormData = {
  id: '',
  name: '',
  dueDate: '',
  multiInstanceType: MultiInstanceTypeEnum.NONE,
  allocationType: AllocationTypeEnum.USER,
  specifyDesc: SpecifyDescEnum.SPECIFY_SINGLE,
};
const formData = ref({ ...initFormData, ...parseData<TaskPanel>() });
const assignee = ref<Partial<SysUserVo>>({
  userName: '',
});
const currentCollapseItem = ref(['1', '2']);
const userSelectRef = ref<InstanceType<typeof UserSelect>>();
const singleUserSelectRef = ref<InstanceType<typeof UserSelect>>();
const roleSelectRef = ref<InstanceType<typeof RoleSelect>>();
const dueDateRef = ref<InstanceType<typeof DueDate>>();

const openUserSelect = () => {
  userSelectRef.value.open();
};
const openSingleUserSelect = () => {
  if (formData.value.assignee?.includes('$')) {
    formData.value.assignee = '';
  }
  singleUserSelectRef.value.open();
};
const openRoleSelect = () => {
  roleSelectRef.value.open();
};
const openDueDate = (e) => {
  dueDateRef.value.openDialog();
};
const blurAssignee = (assignee: string) => {
  updateProperties({ 'flowable:assignee': assignee || undefined });
};
const singleUserSelectCallBack = (data: SysUserVo[]) => {
  const user: SysUserVo = data.length !== 0 ? data[0] : undefined;
  updateProperties({ 'flowable:assignee': user?.userId });
  assignee.value = user || { userName: '' };
  formData.value.assignee = String(user?.userId ?? '');
  let extensionElements = getExtensionElements();
  extensionElements.values = extensionElements.get('values').filter((item) => item.$type !== 'flowable:extAssignee');
  if (user) {
    const extAssigneeElement = createModdleElement('flowable:extAssignee', { body: '' }, extensionElements);
    extensionElements.get('values').push(extAssigneeElement);
    extAssigneeElement.body = JSON.stringify({ userName: user.userName, userId: user.userId });
  }
  if (extensionElements.values.length === 0) {
    extensionElements = undefined;
  }
  updateProperties({ extensionElements });
};
const userSelectCallBack = (data: SysUserVo[]) => {
  let extensionElements = getExtensionElements();
  extensionElements.values = extensionElements.values.filter((item) => item.$type !== 'flowable:extCandidateUsers');
  if (data.length === 0) {
    formData.value.candidateUsers = undefined;
    updateProperties({ 'flowable:candidateUsers': undefined });
  } else {
    const userIds = data.map((item) => item.userId).join(',');
    formData.value.candidateUsers = userIds;
    updateProperties({ 'flowable:candidateUsers': userIds });
    const extCandidateUsersElement = createModdleElement('flowable:extCandidateUsers', { body: '' }, extensionElements);
    extensionElements.values.push(extCandidateUsersElement);
    const users = data.map((item) => {
      return {
        userId: item.userId,
        userName: item.userName,
      };
    });
    extCandidateUsersElement.body = JSON.stringify(users);
  }
  if (extensionElements.values.length === 0) {
    extensionElements = undefined;
  }
  updateProperties({ extensionElements });
};
const roleSelectCallBack = (data: SysRoleVo[]) => {
  if (data.length === 0) {
    formData.value.candidateGroups = '';
    updateProperties({ 'flowable:candidateGroups': undefined });
  } else {
    const roleIds = data.map((item) => item.roleId).join(',');
    formData.value.candidateGroups = roleIds;
    updateProperties({ 'flowable:candidateGroups': roleIds });
  }
};
const dueDateCallBack = (data: string) => {
  updateProperties({ 'flowable:dueDate': data });
};

const taskTabClick = (e) => {
  formData.value.candidateGroups = '';
  formData.value.candidateUsers = '';
  formData.value.assignee = '';
  // formData.value.fixedAssignee = '';
  assignee.value = {};
};

const syncChange = (newVal: any) => {
  updateProperties({ 'flowable:async': newVal });
};
const skipExpressionChange = (newVal: string) => {
  updateProperties({ 'flowable:skipExpression': newVal && newVal.length > 0 ? newVal : undefined });
};
const priorityChange = (newVal: string | number) => {
  updateProperties({ 'flowable:priority': newVal });
};
const fixedAssigneeChange = (newVal) => {
  updateProperties({ 'flowable:assignee': newVal && newVal.length > 0 ? newVal : undefined });
};
const multiInstanceTypeChange = (newVal: string) => {
  if (newVal !== MultiInstanceTypeEnum.NONE) {
    let loopCharacteristics = props.element.businessObject.get('loopCharacteristics');
    if (!loopCharacteristics) {
      loopCharacteristics = createModdleElement(
        'bpmn:MultiInstanceLoopCharacteristics',
        {},
        props.element.businessObject,
      );
    }
    loopCharacteristics.isSequential = newVal === MultiInstanceTypeEnum.SERIAL;
    updateProperties({ loopCharacteristics });
  } else {
    updateProperties({ loopCharacteristics: undefined });
  }
};
const collectionChange = (newVal: string) => {
  let loopCharacteristics = props.element.businessObject.get('loopCharacteristics');
  if (!loopCharacteristics) {
    loopCharacteristics = createModdleElement(
      'bpmn:MultiInstanceLoopCharacteristics',
      {},
      props.element.businessObject,
    );
  }
  loopCharacteristics.collection = newVal && newVal.length > 0 ? newVal : undefined;
  updateProperties({ loopCharacteristics });
};
const elementVariableChange = (newVal: string) => {
  let loopCharacteristics = props.element.businessObject.get('loopCharacteristics');
  if (!loopCharacteristics) {
    loopCharacteristics = createModdleElement(
      'bpmn:MultiInstanceLoopCharacteristics',
      {},
      props.element.businessObject,
    );
  }
  loopCharacteristics.elementVariable = newVal && newVal.length > 0 ? newVal : undefined;
  updateProperties({ loopCharacteristics });
};
const completionConditionChange = (newVal: string) => {
  let loopCharacteristics = props.element.businessObject.get<ModdleElement>('loopCharacteristics');
  if (!loopCharacteristics) {
    loopCharacteristics = createModdleElement(
      'bpmn:MultiInstanceLoopCharacteristics',
      {},
      props.element.businessObject,
    );
  }
  if (newVal && newVal.length > 0) {
    if (!loopCharacteristics.completionCondition) {
      loopCharacteristics.completionCondition = createModdleElement(
        'bpmn:Expression',
        { body: newVal },
        loopCharacteristics,
      );
    } else {
      loopCharacteristics.completionCondition.body = newVal;
    }
  } else {
    loopCharacteristics.completionCondition = undefined;
  }
  updateProperties({ loopCharacteristics });
};
const dueDateChange = (newVal: string) => {
  updateProperties({ 'flowable:dueDate': newVal && newVal.length > 0 ? newVal : undefined });
};
const selectUserLength = computed(() => {
  if (formData.value.candidateUsers) {
    return formData.value.candidateUsers.split(',').length;
  }
  return 0;
});
const selectRoleLength = computed(() => {
  if (formData.value.candidateGroups) {
    return formData.value.candidateGroups.split(',').length;
  }
  return 0;
});

onBeforeMount(() => {
  const extensionElements = getExtensionElements(false);
  if (extensionElements && extensionElements.get('values')) {
    const extAssigneeElement = extensionElements.get('values').find((item) => item.$type === 'flowable:extAssignee');
    if (extAssigneeElement) {
      assignee.value = JSON.parse(extAssigneeElement.body);
    }
  }

  if (formData.value.loopCharacteristics) {
    const loopCharacteristics = formData.value.loopCharacteristics;
    formData.value.collection = loopCharacteristics.collection || '';
    formData.value.elementVariable = loopCharacteristics.elementVariable || '';
    formData.value.completionCondition = loopCharacteristics.completionCondition?.body || '';
    formData.value.multiInstanceType = loopCharacteristics.isSequential
      ? MultiInstanceTypeEnum.SERIAL
      : MultiInstanceTypeEnum.PARALLEL;
  }

  if (formData.value.assignee) {
    formData.value.fixedAssignee = formData.value.assignee;
  }
});

const formRules = ref<FormProps['rules']>({
  id: [{ required: true, message: '请输入', trigger: 'blur' }],
  name: [{ required: true, message: '请输入', trigger: 'blur' }],
});

const AllocationTypeSelect = [
  { id: 'b9cdf970-dd91-47c0-819f-42a7010ca2a6', label: '指定人员', value: AllocationTypeEnum.USER },
  { id: '3f7ccbcd-c464-4602-bb9d-e96649d10585', label: '候选人员', value: AllocationTypeEnum.CANDIDATE },
  { id: 'c49065e0-7f2d-4c09-aedb-ab2d47d9a454', label: '发起人自己', value: AllocationTypeEnum.YOURSELF },
  { id: '6ef40a03-7e9a-4898-89b2-c88fe9064542', label: '发起人指定', value: AllocationTypeEnum.SPECIFY },
];
const SpecifyDesc = [
  { id: 'fa253b34-4335-458c-b1bc-b039e2a2b7a6', label: '指定一个人', value: 'specifySingle' },
  { id: '7365ff54-2e05-4312-9bfb-0b8edd779c5b', label: '指定多个人', value: 'specifyMultiple' },
];

const listFormManage = async () => {
  formManageListLoading.value = true;
  const res = await selectListFormManage();
  formManageList.value = res.data;
  formManageListLoading.value = false;
};
onMounted(() => {
  nextTick(() => {
    listFormManage();
  });
});
</script>

<style lang="less" scoped></style>
