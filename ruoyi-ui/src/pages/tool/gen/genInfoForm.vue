<template>
  <t-row :gutter="[0, 20]">
    <t-col :span="6">
      <t-form-item name="info.tplCategory">
        <template #label>生成模板</template>
        <t-select v-model="info.tplCategory" @change="tplSelectChange">
          <t-option label="单表（增删改查）" value="crud" />
          <t-option label="树表（增删改查）" value="tree" />
        </t-select>
      </t-form-item>
    </t-col>

    <t-col :span="6">
      <t-form-item name="info.packageName">
        <template #label>
          生成包路径
          <t-tooltip content="生成在哪个java包下，例如 com.example.system" placement="top">
            <help-circle-filled-icon />
          </t-tooltip>
        </template>
        <t-input v-model="info.packageName" />
      </t-form-item>
    </t-col>

    <t-col :span="6">
      <t-form-item name="info.moduleName">
        <template #label>
          生成模块名
          <t-tooltip content="可理解为子系统名，例如 system" placement="top">
            <help-circle-filled-icon />
          </t-tooltip>
        </template>
        <t-input v-model="info.moduleName" />
      </t-form-item>
    </t-col>

    <t-col :span="6">
      <t-form-item name="info.businessName">
        <template #label>
          生成业务名
          <t-tooltip content="可理解为功能英文名，例如 user" placement="top">
            <help-circle-filled-icon />
          </t-tooltip>
        </template>
        <t-input v-model="info.businessName" />
      </t-form-item>
    </t-col>

    <t-col :span="6">
      <t-form-item name="info.functionName">
        <template #label>
          生成功能名
          <t-tooltip content="用作类描述，例如 用户" placement="top">
            <help-circle-filled-icon />
          </t-tooltip>
        </template>
        <t-input v-model="info.functionName" />
      </t-form-item>
    </t-col>

    <t-col :span="6">
      <t-form-item>
        <template #label>
          上级菜单
          <t-tooltip content="分配到指定菜单下，例如 系统管理" placement="top">
            <help-circle-filled-icon />
          </t-tooltip>
        </template>
        <t-tree-select
          v-model="info.parentMenuId"
          :data="menuOptions"
          :tree-props="{
            keys: { value: 'menuId', label: 'menuName', children: 'children' },
            checkStrictly: true,
          }"
          placeholder="请选择系统菜单"
        />
      </t-form-item>
    </t-col>

    <t-col :span="6">
      <t-form-item name="info.genType">
        <template #label>
          选择生成对象
          <t-tooltip placement="top">
            <template #content>
              <p>BO对象：保存和编辑时，会使用BO对象替代原始对象</p>
              <p>VO对象：列表查询、查看详情时，会使用VO对象替代原始对象</p>
              <p>Query对象：查询条件传递时，会使用Query对象替代原始对象</p>
            </template>
            <help-circle-filled-icon />
          </t-tooltip>
        </template>
        <t-space>
          <t-checkbox v-model="info.isUseQuery">使用Query对象</t-checkbox>
          <t-checkbox v-model="info.isUseBO">使用BO对象</t-checkbox>
          <t-checkbox v-model="info.isUseVO">使用VO对象</t-checkbox>
        </t-space>
      </t-form-item>
    </t-col>

    <t-col :span="6">
      <t-form-item name="info.genType">
        <template #label>
          生成代码方式
          <t-tooltip content="默认为zip压缩包下载，也可以自定义生成路径" placement="top">
            <help-circle-filled-icon />
          </t-tooltip>
        </template>
        <t-radio-group v-model="info.genType">
          <t-radio value="0">zip压缩包</t-radio>
          <t-radio value="1">自定义路径</t-radio>
        </t-radio-group>
      </t-form-item>
    </t-col>

    <t-col v-if="info.genType === '1'" :span="12">
      <t-form-item name="info.genPath">
        <template #label>
          自定义路径
          <t-tooltip content="填写磁盘绝对路径，若不填写，则生成到当前Web项目下" placement="top">
            <help-circle-filled-icon />
          </t-tooltip>
        </template>
        <t-input-adornment>
          <t-input v-model="info.genPath" />
          <template #append>
            <t-dropdown
              :max-column-width="300"
              :options="[{ content: '恢复默认的生成基础路径', value: 1 }]"
              @click="info.genPath = '/'"
            >
              <t-button theme="default" variant="outline"
                >最近路径快速选择
                <template #suffix>
                  <chevron-down-icon size="16" />
                </template>
              </t-button>
            </t-dropdown>
          </template>
        </t-input-adornment>
      </t-form-item>
    </t-col>
  </t-row>

  <template v-if="info.tplCategory === 'tree'">
    <t-divider align="left" dashed>其他信息</t-divider>
    <t-row v-show="info.tplCategory === 'tree'" :gutter="[0, 20]">
      <t-col :span="6">
        <t-form-item>
          <template #label>
            树编码字段
            <t-tooltip content="树显示的编码字段名， 如：dept_id" placement="top">
              <help-circle-filled-icon />
            </t-tooltip>
          </template>
          <t-select v-model="info.treeCode" placeholder="请选择">
            <t-option
              v-for="(column, index) in info.columns"
              :key="index"
              :label="column.columnName + '：' + column.columnComment"
              :value="column.columnName"
            ></t-option>
          </t-select>
        </t-form-item>
      </t-col>
      <t-col :span="6">
        <t-form-item>
          <template #label>
            树父编码字段
            <t-tooltip content="树显示的父编码字段名， 如：parent_Id" placement="top">
              <help-circle-filled-icon />
            </t-tooltip>
          </template>
          <t-select v-model="info.treeParentCode" placeholder="请选择">
            <t-option
              v-for="(column, index) in info.columns"
              :key="index"
              :label="column.columnName + '：' + column.columnComment"
              :value="column.columnName"
            ></t-option>
          </t-select>
        </t-form-item>
      </t-col>
      <t-col :span="6">
        <t-form-item>
          <template #label>
            树名称字段
            <t-tooltip content="树节点的显示名称字段名， 如：dept_name" placement="top">
              <help-circle-filled-icon />
            </t-tooltip>
          </template>
          <t-select v-model="info.treeName" placeholder="请选择">
            <t-option
              v-for="(column, index) in info.columns"
              :key="index"
              :label="column.columnName + '：' + column.columnComment"
              :value="column.columnName"
            ></t-option>
          </t-select>
        </t-form-item>
      </t-col>
    </t-row>
  </template>
</template>

<script lang="ts" setup>
import { ChevronDownIcon, HelpCircleFilledIcon } from 'tdesign-icons-vue-next';
import { getCurrentInstance, PropType, ref, toRefs, watch } from 'vue';

import { listMenu } from '@/api/system/menu';
import { SysMenuVo } from '@/api/system/model/menuModel';
import { GenTable } from '@/api/tool/model/genModel';

const subColumns = ref([]);
const menuOptions = ref<SysMenuVo[]>([]);
const { proxy } = getCurrentInstance();

const props = defineProps({
  info: {
    type: Object as PropType<GenTable>,
    default: null,
  },
  tables: {
    type: Array as PropType<GenTable[]>,
    default: null,
  },
});
const { info } = toRefs(props);

function tplSelectChange(value: string) {
  if (value !== 'sub') {
    info.value.subTableName = '';
    info.value.subTableFkName = '';
  }
}
function setSubTableColumns(value: string) {
  for (const item in props.tables) {
    const name = props.tables[item].tableName;
    if (value === name) {
      subColumns.value = props.tables[item].columns;
      break;
    }
  }
}
/** 查询菜单下拉树结构 */
function getMenuTreeselect() {
  listMenu().then((response) => {
    menuOptions.value = proxy.handleTree(response.data, 'menuId');
  });
}

watch(
  () => props.info.subTableName,
  (val) => {
    setSubTableColumns(val);
  },
);

getMenuTreeselect();
</script>
