<template>
  <div>
    <t-dialog v-model:visible="visible" :header="title" width="600px" attach="body">
      <t-form label-width="100px">
        <t-form-item label="小时">
          <t-radio-group v-model="hourValue" variant="default-filled" @change="hourChange">
            <t-radio-button label="4" value="4" />
            <t-radio-button label="8" value="8" />
            <t-radio-button label="12" value="12" />
            <t-radio-button label="24" value="24" />
            <t-radio-button label="自定义" value="自定义" />
          </t-radio-group>
          <t-input-number
            v-show="hourValue === '自定义'"
            v-model="customHourValue"
            class="ml-2!"
            :min="1"
            @change="customHourValueChange"
          />
        </t-form-item>
        <t-form-item label="天">
          <t-radio-group v-model="dayValue" variant="default-filled" @change="dayChange">
            <t-radio-button label="1" value="1" />
            <t-radio-button label="2" value="2" />
            <t-radio-button label="3" value="3" />
            <t-radio-button label="4" value="4" />
            <t-radio-button label="自定义" value="自定义" />
          </t-radio-group>
          <t-input-number
            v-show="dayValue === '自定义'"
            v-model="customDayValue"
            class="ml-2!"
            :min="1"
            @change="customDayValueChange"
          />
        </t-form-item>
        <t-form-item label="周">
          <t-radio-group v-model="weekValue" variant="default-filled" @change="weekChange">
            <t-radio-button label="1" value="1" />
            <t-radio-button label="2" value="2" />
            <t-radio-button label="3" value="3" />
            <t-radio-button label="4" value="4" />
            <t-radio-button label="自定义" value="自定义" />
          </t-radio-group>
          <t-input-number
            v-show="weekValue === '自定义'"
            v-model="customWeekValue"
            class="ml-2!"
            :min="1"
            @change="customWeekValueChange"
          />
        </t-form-item>
        <t-form-item label="月">
          <t-radio-group v-model="monthValue" variant="default-filled" @change="monthChange">
            <t-radio-button label="1" value="1" />
            <t-radio-button label="2" value="2" />
            <t-radio-button label="3" value="3" />
            <t-radio-button label="4" value="4" />
            <t-radio-button label="自定义" value="自定义" />
          </t-radio-group>
          <t-input-number
            v-show="monthValue === '自定义'"
            v-model="customMonthValue"
            class="ml-2!"
            :min="1"
            @change="customMonthValueChange"
          />
        </t-form-item>
      </t-form>

      <template #footer>
        <div>
          <t-button variant="outline" @click="closeDialog">取消</t-button>
          <t-button theme="primary" @click="confirm">确定</t-button>
        </div>
      </template>
    </t-dialog>
  </div>
</template>

<script setup lang="ts">
import type { RadioValue } from 'tdesign-vue-next';

import useDialog from '@/hooks/useDialog';

interface PropType {
  modelValue?: string;
  data?: string;
}
const prop = withDefaults(defineProps<PropType>(), {
  modelValue: '',
  data: '',
});
const emit = defineEmits(['update:modelValue', 'confirmCallBack']);

const { title, visible, openDialog, closeDialog } = useDialog({
  title: '设置任务到期时间',
});
const formValue = ref();
const valueType = ref();

const hourValue = ref('');
const dayValue = ref('');
const weekValue = ref('');
const monthValue = ref('');

const customHourValue = ref(1);
const customDayValue = ref(1);
const customWeekValue = ref(1);
const customMonthValue = ref(1);

const hourValueConst = ['4', '8', '12', '24'];
const dayAndWeekAndMonthValueConst = ['1', '2', '3', '4'];

const initValue = () => {
  formValue.value = prop.data;
  if (prop.data) {
    const lastStr = prop.data.substring(prop.data.length - 1);
    if (lastStr === 'H') {
      const hourValueValue = prop.data.substring(2, prop.data.length - 1);
      if (hourValueConst.includes(hourValueValue)) {
        hourValue.value = hourValueValue;
      } else {
        hourValue.value = '自定义';
        customHourValue.value = Number(hourValueValue);
      }
    }
    const dayAndWeekAndMonthValue = prop.data.substring(1, prop.data.length - 1);
    if (lastStr === 'D') {
      if (dayAndWeekAndMonthValueConst.includes(dayAndWeekAndMonthValue)) {
        dayValue.value = dayAndWeekAndMonthValue;
      } else {
        dayValue.value = '自定义';
        customDayValue.value = Number(dayAndWeekAndMonthValue);
      }
    }
    if (lastStr === 'W') {
      if (dayAndWeekAndMonthValueConst.includes(dayAndWeekAndMonthValue)) {
        weekValue.value = dayAndWeekAndMonthValue;
      } else {
        weekValue.value = '自定义';
        customWeekValue.value = Number(dayAndWeekAndMonthValue);
      }
    }
    if (lastStr === 'M') {
      if (dayAndWeekAndMonthValueConst.includes(dayAndWeekAndMonthValue)) {
        monthValue.value = dayAndWeekAndMonthValue;
      } else {
        monthValue.value = '自定义';
        customMonthValue.value = Number(dayAndWeekAndMonthValue);
      }
    }
  }
};

const confirm = () => {
  emit('update:modelValue', formValue.value);
  emit('confirmCallBack', formValue.value);
  closeDialog();
};

const customHourValueChange = (customHourValue: number | string) => {
  formValue.value = `PT${customHourValue}H`;

  dayValue.value = '';
  weekValue.value = '';
  monthValue.value = '';
  customDayValue.value = 1;
  customWeekValue.value = 1;
  customMonthValue.value = 1;
};
const customDayValueChange = (customDayValue: number | string) => {
  formValue.value = `P${customDayValue}D`;
  hourValue.value = '';
  weekValue.value = '';
  monthValue.value = '';

  customHourValue.value = 1;
  customWeekValue.value = 1;
  customMonthValue.value = 1;
};

const customWeekValueChange = (customWeekValue: number | string) => {
  formValue.value = `P${customWeekValue}W`;
  hourValue.value = '';
  dayValue.value = '';
  monthValue.value = '';

  customHourValue.value = 1;
  customDayValue.value = 1;
  customMonthValue.value = 1;
};

const customMonthValueChange = (customMonthValue: number | string) => {
  formValue.value = `P${customMonthValue}M`;
  hourValue.value = '';
  dayValue.value = '';
  weekValue.value = '';

  customHourValue.value = 1;
  customDayValue.value = 1;
  customWeekValue.value = 1;
};

const hourChange = (hourValue: RadioValue) => {
  if (hourValue === '自定义') {
    formValue.value = `PT${customHourValue.value}H`;
  } else {
    formValue.value = `PT${hourValue}H`;
  }

  dayValue.value = '';
  weekValue.value = '';
  monthValue.value = '';
  customDayValue.value = 1;
  customWeekValue.value = 1;
  customMonthValue.value = 1;
};
const dayChange = (dayValue: RadioValue) => {
  if (dayValue === '自定义') {
    formValue.value = `P${customDayValue.value}D`;
  } else {
    formValue.value = `P${dayValue}D`;
  }

  hourValue.value = '';
  weekValue.value = '';
  monthValue.value = '';

  customHourValue.value = 1;
  customWeekValue.value = 1;
  customMonthValue.value = 1;
};
const weekChange = (weekValue: RadioValue) => {
  if (weekValue === '自定义') {
    formValue.value = `P${customWeekValue.value}W`;
  } else {
    formValue.value = `P${weekValue}W`;
  }

  hourValue.value = '';
  dayValue.value = '';
  monthValue.value = '';

  customHourValue.value = 1;
  customDayValue.value = 1;
  customMonthValue.value = 1;
};
const monthChange = (monthValue: RadioValue) => {
  if (monthValue === '自定义') {
    formValue.value = `P${customMonthValue.value}M`;
  } else {
    formValue.value = `P${monthValue}M`;
  }

  hourValue.value = '';
  dayValue.value = '';
  weekValue.value = '';

  customHourValue.value = 1;
  customDayValue.value = 1;
  customWeekValue.value = 1;
};

watch(
  () => visible.value,
  () => {
    if (visible.value) {
      initValue();
    }
  },
);

defineExpose({
  openDialog,
  closeDialog,
});
</script>
