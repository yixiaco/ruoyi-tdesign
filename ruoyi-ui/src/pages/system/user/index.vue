<template>
  <t-card>
    <t-row :gutter="20">
      <!--部门数据-->
      <t-col :sm="2" :xs="12">
        <div class="head-container">
          <t-input v-model="deptName" placeholder="请输入部门名称" clearable style="margin-bottom: 20px">
            <template #prefixIcon>
              <search-icon />
            </template>
          </t-input>
        </div>
        <div class="head-container">
          <t-tree
            ref="deptTreeRef"
            :data="deptOptions"
            :keys="{ value: 'id', label: 'label', children: 'children' }"
            :filter="filterNode"
            activable
            expand-all
            hover
            check-strictly
            @click="handleNodeClick"
          />
        </div>
      </t-col>
      <!--用户数据-->
      <t-col :sm="10" :xs="12">
        <t-space direction="vertical">
          <t-form v-show="showSearch" ref="queryRef" :data="queryParams" layout="inline" label-width="68px">
            <t-form-item label="用户名称" name="userName">
              <t-input
                v-model="queryParams.userName"
                placeholder="请输入用户名称"
                clearable
                style="width: 240px"
                @enter="handleQuery"
              />
            </t-form-item>
            <t-form-item label="用户昵称" name="nickName">
              <t-input v-model="queryParams.nickName" placeholder="请输入用户昵称" clearable @enter="handleQuery" />
            </t-form-item>
            <t-form-item label="手机号码" name="phonenumber">
              <t-input
                v-model="queryParams.phonenumber"
                placeholder="请输入手机号码"
                clearable
                style="width: 240px"
                @enter="handleQuery"
              />
            </t-form-item>
            <t-form-item label="用户邮箱" name="email">
              <t-input v-model="queryParams.email" placeholder="请输入用户邮箱" clearable @enter="handleQuery" />
            </t-form-item>
            <t-form-item label="状态" name="status">
              <t-select v-model="queryParams.status" placeholder="用户状态" clearable style="width: 240px">
                <t-option
                  v-for="dict in sys_normal_disable"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </t-select>
            </t-form-item>
            <t-form-item label="创建时间" style="width: 308px">
              <t-date-range-picker
                v-model="dateRange"
                allow-input
                clearable
                separator="-"
                :placeholder="['开始日期', '结束日期']"
              />
            </t-form-item>
            <t-form-item label-width="0px">
              <t-button theme="primary" @click="handleQuery">
                <template #icon> <search-icon /></template>
                搜索
              </t-button>
              <t-button theme="default" @click="resetQuery">
                <template #icon> <refresh-icon /></template>
                重置
              </t-button>
            </t-form-item>
          </t-form>

          <t-table
            v-model:column-controller-visible="columnControllerVisible"
            :loading="loading"
            row-key="userId"
            :data="userList"
            :columns="columns"
            :selected-row-keys="ids"
            select-on-row-click
            :pagination="pagination"
            :column-controller="{
              hideTriggerButton: true,
            }"
            @select-change="handleSelectionChange"
          >
            <template #topContent>
              <t-row>
                <t-col flex="auto">
                  <t-button v-hasPermi="['system:user:add']" theme="primary" @click="handleAdd">
                    <template #icon> <add-icon /></template>
                    新增
                  </t-button>
                  <t-button
                    v-hasPermi="['system:user:edit']"
                    theme="default"
                    variant="outline"
                    :disabled="single"
                    @click="handleUpdate"
                  >
                    <template #icon> <edit-icon /> </template>
                    修改
                  </t-button>
                  <t-button
                    v-hasPermi="['system:user:remove']"
                    theme="danger"
                    variant="outline"
                    :disabled="multiple"
                    @click="handleDelete"
                  >
                    <template #icon> <delete-icon /> </template>
                    删除
                  </t-button>
                  <t-button v-hasPermi="['system:user:import']" theme="default" variant="outline" @click="handleImport">
                    <template #icon> <upload-icon /> </template>
                    导入
                  </t-button>
                  <t-button v-hasPermi="['system:user:export']" theme="default" variant="outline" @click="handleExport">
                    <template #icon> <download-icon /> </template>
                    导出
                  </t-button>
                </t-col>
                <t-col flex="none">
                  <t-button theme="default" shape="square" variant="outline" @click="showSearch = !showSearch">
                    <template #icon> <search-icon /> </template>
                  </t-button>
                  <t-button theme="default" variant="outline" @click="columnControllerVisible = true">
                    <template #icon> <setting-icon /> </template>
                    列配置
                  </t-button>
                </t-col>
              </t-row>
            </template>
            <template #status="{ row }">
              <t-switch
                v-model="row.status"
                :custom-value="['0', '1']"
                @click.stop
                @change="handleStatusChange(row)"
              ></t-switch>
            </template>
            <template #operation="{ row }">
              <t-space :size="8">
                <t-tooltip content="详情" placement="top">
                  <t-link
                    v-hasPermi="['system:user:query']"
                    theme="primary"
                    hover="color"
                    @click.stop="handleDetail(row)"
                  >
                    <browse-icon />
                  </t-link>
                </t-tooltip>
                <t-tooltip v-if="row.userId !== 1" content="修改" placement="top">
                  <t-link
                    v-hasPermi="['system:user:edit']"
                    theme="primary"
                    hover="color"
                    @click.stop="handleUpdate(row)"
                  >
                    <edit-icon />
                  </t-link>
                </t-tooltip>
                <t-tooltip v-if="row.userId !== 1" content="删除" placement="top">
                  <t-link
                    v-hasPermi="['system:user:remove']"
                    theme="danger"
                    hover="color"
                    @click.stop="handleDelete(row)"
                  >
                    <delete-icon />
                  </t-link>
                </t-tooltip>
                <t-tooltip v-if="row.userId !== 1" content="重置密码" placement="top">
                  <t-link
                    v-hasPermi="['system:user:resetPwd']"
                    theme="primary"
                    hover="color"
                    @click.stop="handleResetPwd(row)"
                  >
                    <refresh-icon />
                  </t-link>
                </t-tooltip>
                <t-tooltip v-if="row.userId !== 1" content="分配角色" placement="top">
                  <t-link
                    v-hasPermi="['system:user:edit']"
                    theme="primary"
                    hover="color"
                    @click.stop="handleAuthRole(row)"
                  >
                    <check-circle-icon />
                  </t-link>
                </t-tooltip>
              </t-space>
            </template>
          </t-table>
        </t-space>
      </t-col>
    </t-row>

    <!-- 添加或修改用户配置对话框 -->
    <t-dialog
      v-model:visible="open"
      :close-on-overlay-click="false"
      :header="title"
      width="650px"
      attach="body"
      @close="cancel"
      @confirm="submitForm"
    >
      <t-loading :loading="dLoading" show-overlay>
        <t-form
          ref="userRef"
          label-align="right"
          :data="form"
          :rules="rules"
          label-width="calc(5em + 24px)"
          scroll-to-first-error="smooth"
          @submit="onSubmit"
        >
          <t-row :gutter="[0, 20]">
            <t-col :span="6">
              <t-form-item label="用户昵称" name="nickName">
                <t-input v-model="form.nickName" placeholder="请输入用户昵称" :maxlength="30" />
              </t-form-item>
            </t-col>
            <t-col :span="6">
              <t-form-item label="归属部门" name="deptId">
                <t-tree-select
                  v-model="form.deptId"
                  :data="deptOptions"
                  :tree-props="{
                    keys: { value: 'id', label: 'label', children: 'children' },
                    checkStrictly: true,
                  }"
                  placeholder="请选择归属部门"
                />
              </t-form-item>
            </t-col>
            <t-col :span="6">
              <t-form-item label="手机号码" name="phonenumber">
                <t-input v-model="form.phonenumber" placeholder="请输入手机号码" :maxlength="11" />
              </t-form-item>
            </t-col>
            <t-col :span="6">
              <t-form-item label="邮箱" name="email">
                <t-input v-model="form.email" placeholder="请输入邮箱" :maxlength="50" />
              </t-form-item>
            </t-col>
            <t-col v-if="!form.userId" :span="6">
              <t-form-item label="用户名称" name="userName">
                <t-input v-model="form.userName" placeholder="请输入用户名称" :maxlength="30" />
              </t-form-item>
            </t-col>
            <t-col v-if="!form.userId" :span="6">
              <t-form-item label="用户密码" name="password">
                <t-input v-model="form.password" placeholder="请输入用户密码" type="password" :maxlength="20" />
              </t-form-item>
            </t-col>
            <t-col :span="6">
              <t-form-item label="用户性别">
                <t-select v-model="form.sex" placeholder="请选择">
                  <t-option
                    v-for="dict in sys_user_sex"
                    :key="dict.value"
                    :label="dict.label"
                    :value="dict.value"
                  ></t-option>
                </t-select>
              </t-form-item>
            </t-col>
            <t-col :span="6">
              <t-form-item label="状态">
                <t-radio-group v-model="form.status">
                  <t-radio v-for="dict in sys_normal_disable" :key="dict.value" :value="dict.value">
                    {{ dict.label }}
                  </t-radio>
                </t-radio-group>
              </t-form-item>
            </t-col>
            <t-col :span="6">
              <t-form-item label="岗位">
                <t-select v-model="form.postIds" clearable multiple placeholder="请选择">
                  <t-option
                    v-for="item in postOptions"
                    :key="item.postId"
                    :label="item.postName"
                    :value="item.postId"
                    :disabled="item.status === 1"
                  ></t-option>
                </t-select>
              </t-form-item>
            </t-col>
            <t-col :span="6">
              <t-form-item label="角色">
                <t-select v-model="form.roleIds" clearable multiple placeholder="请选择">
                  <t-option
                    v-for="item in roleOptions"
                    :key="item.roleId"
                    :label="item.roleName"
                    :value="item.roleId"
                    :disabled="item.status === 1"
                  ></t-option>
                </t-select>
              </t-form-item>
            </t-col>
            <t-col :span="12">
              <t-form-item label="备注">
                <t-textarea v-model="form.remark" placeholder="请输入内容"></t-textarea>
              </t-form-item>
            </t-col>
          </t-row>
        </t-form>
      </t-loading>
    </t-dialog>

    <!-- 用户导入对话框 -->
    <t-dialog
      v-model:visible="upload.open"
      :close-on-overlay-click="false"
      width="410px"
      :header="upload.title"
      attach="body"
      :footer="null"
    >
      <t-upload
        ref="uploadRef"
        :limit="1"
        accept=".xlsx, .xls"
        :headers="upload.headers"
        :action="upload.url + '?updateSupport=' + upload.updateSupport"
        :disabled="upload.isUploading"
        :on-progress="handleFileUploadProgress"
        :on-success="handleFileSuccess"
        :auto-upload="false"
        theme="file"
        draggable
        tips="仅允许导入xls、xlsx格式文件。"
      >
      </t-upload>
      <div><t-checkbox v-model="upload.updateSupport" />是否更新已经存在的用户数据</div>
      <t-link hover="color" style="font-size: 12px; vertical-align: baseline" @click="importTemplate">下载模板</t-link>
    </t-dialog>

    <!-- 用户信息详情 -->
    <t-dialog v-model:visible="openView" header="用户信息详情" width="700px" attach="body" :footer="false">
      <t-loading :loading="openViewLoading">
        <t-form label-align="right" colon label-width="calc(5em + 24px)">
          <t-row :gutter="[0, 20]">
            <t-col :span="6">
              <t-form-item label="用户ID">{{ formView.userId }}</t-form-item>
            </t-col>
            <t-col :span="6">
              <t-form-item label="部门">{{ formView.dept?.deptName }}</t-form-item>
            </t-col>
            <t-col :span="6">
              <t-form-item label="用户账号">{{ formView.userName }}</t-form-item>
            </t-col>
            <t-col :span="6">
              <t-form-item label="用户昵称">{{ formView.nickName }}</t-form-item>
            </t-col>
            <t-col :span="6">
              <t-form-item label="用户类型">{{ formView.userType }}</t-form-item>
            </t-col>
            <t-col :span="6">
              <t-form-item label="用户邮箱">{{ formView.email }}</t-form-item>
            </t-col>
            <t-col :span="6">
              <t-form-item label="手机号码">{{ formView.phonenumber }}</t-form-item>
            </t-col>
            <t-col :span="6">
              <t-form-item label="用户性别">
                <dict-tag :options="sys_user_sex" :value="formView.sex" />
              </t-form-item>
            </t-col>
            <t-col :span="6">
              <t-form-item label="头像地址">
                <image-preview :src="formView.avatar" :width="60" :height="60" />
              </t-form-item>
            </t-col>
            <t-col :span="6">
              <t-form-item label="帐号状态">
                <dict-tag :options="sys_normal_disable" :value="formView.status" />
              </t-form-item>
            </t-col>
            <t-col :span="6">
              <t-form-item label="最后登录IP">{{ formView.loginIp }}</t-form-item>
            </t-col>
            <t-col :span="6">
              <t-form-item label="最后登录时间">{{ parseTime(formView.loginDate) }}</t-form-item>
            </t-col>
            <t-col :span="6">
              <t-form-item label="创建时间">{{ parseTime(formView.createTime) }}</t-form-item>
            </t-col>
            <t-col :span="6">
              <t-form-item label="更新时间">{{ parseTime(formView.updateTime) }}</t-form-item>
            </t-col>
            <t-col :span="12">
              <t-form-item label="备注">{{ formView.remark }}</t-form-item>
            </t-col>
          </t-row>
        </t-form>
      </t-loading>
    </t-dialog>
  </t-card>
</template>
<script lang="ts">
export default {
  name: 'User',
};
</script>
<script lang="ts" setup>
import {
  AddIcon,
  BrowseIcon,
  CheckCircleIcon,
  DeleteIcon,
  DownloadIcon,
  EditIcon,
  RefreshIcon,
  SearchIcon,
  SettingIcon,
  UploadIcon,
} from 'tdesign-icons-vue-next';
import { FormInstanceFunctions, FormRule, PrimaryTableCol, UploadInstanceFunctions } from 'tdesign-vue-next';
import { computed, createVNode, getCurrentInstance, reactive, ref } from 'vue';
import { useRouter } from 'vue-router';

import { SysUserForm, SysUserQuery, SysUserVo } from '@/api/system/model/userModel';
import {
  addUser,
  changeUserStatus,
  delUser,
  deptTreeSelect,
  getUser,
  listUser,
  resetUserPwd,
  updateUser,
} from '@/api/system/user';
import { getToken } from '@/utils/auth';

const router = useRouter();
const { proxy } = getCurrentInstance();
const { sys_normal_disable, sys_user_sex } = proxy.useDict('sys_normal_disable', 'sys_user_sex');

const uploadRef = ref<UploadInstanceFunctions>(null);
const userRef = ref<FormInstanceFunctions>(null);
const userList = ref<SysUserVo[]>([]);
const open = ref(false);
const openView = ref(false);
const openViewLoading = ref(false);
const loading = ref(true);
const dLoading = ref(false);
const showSearch = ref(true);
const columnControllerVisible = ref(false);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref('');
const dateRange = ref([]);
const deptName = ref('');
const deptOptions = ref(undefined);
const initPassword = ref(undefined);
const postOptions = ref([]);
const roleOptions = ref([]);
/** * 用户导入参数 */
const upload = reactive({
  // 是否显示弹出层（用户导入）
  open: false,
  // 弹出层标题（用户导入）
  title: '',
  // 是否禁用上传
  isUploading: false,
  // 是否更新已经存在的用户数据
  updateSupport: false,
  // 设置上传的请求头部
  headers: { Authorization: `Bearer ${getToken()}` },
  // 上传的地址
  url: `${import.meta.env.VITE_APP_BASE_API}/system/user/importData`,
});
// 列显隐信息
const columns = ref<Array<PrimaryTableCol>>([
  { title: `选择列`, colKey: 'row-select', type: 'multiple', width: 50, align: 'center' },
  { title: `用户编号`, colKey: 'userId', align: 'center' },
  { title: `用户名称`, colKey: 'userName', ellipsis: true, align: 'center' },
  { title: `用户昵称`, colKey: 'nickName', ellipsis: true, align: 'center' },
  { title: `部门`, colKey: 'dept.deptName', align: 'center' },
  { title: `手机号码`, colKey: 'phonenumber', width: 130, align: 'center' },
  { title: `状态`, colKey: 'status', align: 'center' },
  { title: `创建时间`, colKey: 'createTime', align: 'center' },
  { title: `操作`, colKey: 'operation', align: 'center' },
]);

const rules = ref<Record<string, Array<FormRule>>>({
  userName: [
    { required: true, message: '用户名称不能为空', trigger: 'blur' },
    { min: 2, max: 20, message: '用户名称长度必须介于 2 和 20 之间', trigger: 'blur' },
  ],
  nickName: [{ required: true, message: '用户昵称不能为空', trigger: 'blur' }],
  password: [
    { required: true, message: '用户密码不能为空', trigger: 'blur' },
    { min: 5, max: 20, message: '用户密码长度必须介于 5 和 20 之间', trigger: 'blur' },
  ],
  email: [{ email: true, message: '请输入正确的邮箱地址', trigger: 'change' }],
  phonenumber: [{ pattern: /^1[3456789][0-9]\d{8}$/, message: '请输入正确的手机号码', trigger: 'blur' }],
});
const form = ref<SysUserForm & SysUserVo>({
  status: '0',
  postIds: [],
  roleIds: [],
});
const formView = ref<SysUserVo>({});
const queryParams = ref<SysUserQuery>({
  pageNum: 1,
  pageSize: 10,
  userName: undefined,
  nickName: undefined,
  phonenumber: undefined,
  email: undefined,
  status: undefined,
  deptId: undefined,
});

const pagination = computed(() => {
  return {
    current: queryParams.value.pageNum,
    pageSize: queryParams.value.pageSize,
    total: total.value,
    showJumper: true,
    onChange: (pageInfo) => {
      queryParams.value.pageNum = pageInfo.current;
      queryParams.value.pageSize = pageInfo.pageSize;
      getList();
    },
  };
});

/** 通过条件过滤节点  */
const filterNode = (node) => {
  if (!node.value) return true;
  return node.label.indexOf(deptName.value) !== -1;
};
/** 查询部门下拉树结构 */
function getDeptTree() {
  deptTreeSelect().then((response) => {
    deptOptions.value = response.data;
  });
}
/** 查询用户列表 */
function getList() {
  loading.value = true;
  listUser(proxy.addDateRange(queryParams.value, dateRange.value)).then((res) => {
    loading.value = false;
    userList.value = res.rows;
    total.value = res.total;
    handleSelectionChange([]);
  });
}
/** 节点单击事件 */
function handleNodeClick({ node }) {
  if (!node.actived) {
    queryParams.value.deptId = node.value;
  } else {
    queryParams.value.deptId = undefined;
  }
  handleQuery();
}
/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}
/** 重置按钮操作 */
function resetQuery() {
  dateRange.value = [];
  proxy.resetForm('queryRef');
  queryParams.value.deptId = undefined;
  // proxy.$refs.tree.setCurrentKey(null);
  handleQuery();
}
/** 删除按钮操作 */
function handleDelete(row) {
  const userIds = row.userId || ids.value;
  proxy.$modal.confirm(`是否确认删除用户编号为"${userIds}"的数据项？`, () => {
    return delUser(userIds).then(() => {
      getList();
      proxy.$modal.msgSuccess('删除成功');
    });
  });
}
/** 导出按钮操作 */
function handleExport() {
  proxy.download(
    'system/user/export',
    {
      ...queryParams.value,
    },
    `user_${new Date().getTime()}.xlsx`,
  );
}
/** 用户状态修改  */
function handleStatusChange(row) {
  const user = userList.value.find((value) => value.userId === row.userId);
  const text = user.status === '0' ? '启用' : '停用';
  proxy.$modal.confirm(
    `确认要"${text}""${user.userName}"用户吗?`,
    () => {
      const msgLoading = proxy.$modal.msgLoading('提交中...');
      return changeUserStatus(user.userId, user.status)
        .then(() => {
          proxy.$modal.msgSuccess(`${text}成功`);
        })
        .catch(() => {
          user.status = user.status === '0' ? '1' : '0';
        })
        .finally(() => proxy.$modal.msgClose(msgLoading));
    },
    () => {
      user.status = user.status === '0' ? '1' : '0';
    },
  );
}
/** 跳转角色分配 */
function handleAuthRole(row) {
  const { userId } = row;
  router.push(`/system/user-auth/role/${userId}`);
}
/** 重置密码按钮操作 */
function handleResetPwd(row) {
  proxy.$modal.prompt(`请输入"${row.userName}"的新密码`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    closeOnClickModal: false,
    inputPattern: /^.{5,20}$/,
    inputErrorMessage: '用户密码长度必须介于 5 和 20 之间',
    onConfirm: ({ value }) => {
      const msgLoading = proxy.$modal.msgLoading('提交中...');
      return resetUserPwd(row.userId, value)
        .then((response) => {
          proxy.$modal.msgSuccess(`修改成功，新密码是：${value}`);
        })
        .finally(() => proxy.$modal.msgClose(msgLoading));
    },
  });
}
/** 选择条数  */
function handleSelectionChange(value) {
  ids.value = value;
  single.value = value.length !== 1;
  multiple.value = !value.length;
}
/** 导入按钮操作 */
function handleImport() {
  upload.title = '用户导入';
  upload.open = true;
}
/** 下载模板操作 */
function importTemplate() {
  proxy.download('system/user/importTemplate', {}, `user_template_${new Date().getTime()}.xlsx`);
}
/** 文件上传中处理 */
const handleFileUploadProgress = (event, file, fileList) => {
  upload.isUploading = true;
};
/** 文件上传成功处理 */
const handleFileSuccess = (context) => {
  upload.open = false;
  upload.isUploading = false;
  uploadRef.value.uploadFiles([]);
  proxy.$modal.alert({
    body: createVNode(
      'div',
      { style: 'overflow: auto;overflow-x: hidden;max-height: 70vh;padding: 10px 20px 0;' },
      context.response.msg,
    ),
    header: '导入结果',
  });
  getList();
};
/** 重置操作表单 */
function reset() {
  form.value = {
    userId: undefined,
    deptId: undefined,
    userName: undefined,
    nickName: undefined,
    password: undefined,
    phonenumber: undefined,
    email: undefined,
    sex: undefined,
    status: '0',
    remark: undefined,
    postIds: [],
    roleIds: [],
  };
  proxy.resetForm('userRef');
}
/** 取消按钮 */
function cancel() {
  open.value = false;
  reset();
}
/** 详情按钮操作 */
function handleDetail(row) {
  reset();
  openView.value = true;
  openViewLoading.value = true;
  const userId = row.userId || ids.value;
  getUser(userId).then((response) => {
    formView.value = response.data?.user;
    openViewLoading.value = false;
  });
}
/** 新增按钮操作 */
function handleAdd() {
  open.value = true;
  dLoading.value = true;
  reset();
  getDeptTree();
  getUser()
    .then((response) => {
      postOptions.value = response.data.posts;
      roleOptions.value = response.data.roles;
      title.value = '添加用户';
      form.value.password = initPassword.value;
    })
    .finally(() => {
      dLoading.value = false;
    });
}
/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const userId = row.userId || ids.value;
  open.value = true;
  dLoading.value = true;
  getDeptTree();
  getUser(userId)
    .then((response) => {
      Object.assign(form.value, response.data.user);
      postOptions.value = response.data.posts;
      roleOptions.value = response.data.roles;
      form.value.postIds = response.data.postIds;
      form.value.roleIds = response.data.roleIds;
      title.value = '修改用户';
      form.value.password = '';
    })
    .finally(() => {
      dLoading.value = false;
    });
}
/** 提交按钮 */
function submitForm() {
  userRef.value.submit();
}

const onSubmit = ({ validateResult, firstError }) => {
  if (validateResult === true) {
    dLoading.value = true;
    const msgLoading = proxy.$modal.msgLoading('提交中...');
    if (form.value.userId) {
      updateUser(form.value)
        .then((response) => {
          proxy.$modal.msgSuccess('修改成功');
          open.value = false;
          getList();
        })
        .finally(() => {
          dLoading.value = false;
          proxy.$modal.msgClose(msgLoading);
        });
    } else {
      addUser(form.value)
        .then((response) => {
          proxy.$modal.msgSuccess('新增成功');
          open.value = false;
          getList();
        })
        .finally(() => {
          dLoading.value = false;
          proxy.$modal.msgClose(msgLoading);
        });
    }
  } else {
    proxy.$modal.msgError(firstError);
  }
};

getDeptTree();
getList();
</script>
