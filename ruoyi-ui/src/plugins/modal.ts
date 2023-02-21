import {
  DialogPlugin,
  MessagePlugin,
  NotifyPlugin,
  LoadingPlugin,
  Form,
  FormItem,
  Input,
  DialogOptions,
} from 'tdesign-vue-next';
import { h, reactive, ref } from 'vue';

let loadingInstance;

export default {
  // 消息提示
  msg(content, duration?: number) {
    return MessagePlugin.info(content, duration);
  },
  // 错误消息
  msgError(content, duration?: number) {
    return MessagePlugin.error(content, duration);
  },
  // 成功消息
  msgSuccess(content, duration?: number) {
    return MessagePlugin.success(content, duration);
  },
  // 警告消息
  msgWarning(content, duration?: number) {
    return MessagePlugin.warning(content, duration);
  },
  // 弹出提示
  alert(content: DialogOptions | string) {
    let instance;
    if (content instanceof Object) {
      instance = DialogPlugin.alert({
        onConfirm: () => {
          instance.destroy();
        },
        ...content,
      });
    } else {
      instance = DialogPlugin.alert({
        header: '系统提示',
        body: content,
        onConfirm: () => {
          instance.destroy();
        },
      });
    }
    return instance;
  },
  // 错误提示
  alertError(content: DialogOptions | string) {
    let instance;
    if (content instanceof Object) {
      instance = DialogPlugin.alert({
        theme: 'danger',
        onConfirm: () => {
          instance.destroy();
        },
        ...content,
      });
    } else {
      instance = DialogPlugin.alert({
        header: '系统提示',
        body: content,
        theme: 'danger',
        onConfirm: () => {
          instance.destroy();
        },
      });
    }
    return instance;
  },
  // 成功提示
  alertSuccess(content: DialogOptions | string) {
    let instance;
    if (content instanceof Object) {
      instance = DialogPlugin.alert({
        theme: 'success',
        onConfirm: () => {
          instance.destroy();
        },
        ...content,
      });
    } else {
      instance = DialogPlugin.alert({
        header: '系统提示',
        body: content,
        theme: 'success',
        onConfirm: () => {
          instance.destroy();
        },
      });
    }
    return instance;
  },
  // 警告提示
  alertWarning(content: DialogOptions | string) {
    let instance;
    if (content instanceof Object) {
      instance = DialogPlugin.alert({
        theme: 'warning',
        onConfirm: () => {
          instance.destroy();
        },
        ...content,
      });
    } else {
      instance = DialogPlugin.alert({
        header: '系统提示',
        body: content,
        theme: 'warning',
        onConfirm: () => {
          instance.destroy();
        },
      });
    }
    return instance;
  },
  // 通知提示
  notify(content) {
    return NotifyPlugin.info({
      content,
    });
  },
  // 错误通知
  notifyError(content) {
    return NotifyPlugin.error({
      content,
    });
  },
  // 成功通知
  notifySuccess(content) {
    return NotifyPlugin.success({
      content,
    });
  },
  // 警告通知
  notifyWarning(content) {
    return NotifyPlugin.warning({
      content,
    });
  },
  // 确认窗体
  confirm(content, onConfirm: Function, onCancel?: Function) {
    const btn = reactive({
      content: '确定',
      loading: false,
      onClick: async () => {
        btn.loading = true;
        try {
          await onConfirm();
          instance.destroy();
        } finally {
          btn.loading = false;
        }
      },
    });
    const instance = DialogPlugin.confirm({
      header: '系统提示',
      body: content,
      confirmBtn: btn,
      cancelBtn: '取消',
      theme: 'warning',
      onCancel: () => onCancel && onCancel(),
    });
    return instance;
  },
  // 提交内容
  prompt(tip, title, props) {
    const btn = reactive({
      content: props.confirmButtonText,
      loading: false,
      onClick: async () => {
        formRef.value.validate().then(async () => {
          btn.loading = true;
          try {
            await props?.onConfirm({ value: form.input });
            instance.destroy();
          } finally {
            btn.loading = false;
          }
        });
      },
    });
    const form = reactive({
      input: undefined,
    });
    const formRef = ref(null);
    const instance = DialogPlugin.confirm({
      theme: props.theme || 'warning',
      header: title || '系统提示',
      confirmBtn: btn,
      cancelBtn: props.cancelButtonText,
      closeOnOverlayClick: props.closeOnClickModal,
      body: () =>
        h(
          Form,
          {
            data: form,
            ref: formRef,
            labelAlign: 'top',
            rules: {
              input: [
                { required: true, message: props.inputErrorMessage, trigger: 'blur' },
                { pattern: props.inputPattern, message: props.inputErrorMessage, trigger: 'change' },
              ],
            },
          },
          h(
            FormItem,
            {
              label: tip,
              name: 'input',
            },
            h(Input, {
              value: form.input,
              'onUpdate:value': (value) => {
                form.input = value;
              },
            }),
          ),
        ),
    });
    return instance;
  },
  // 打开遮罩层
  loading(content) {
    loadingInstance = LoadingPlugin({
      text: content,
      attach: 'body',
      fullscreen: true,
    });
  },
  // 关闭遮罩层
  closeLoading() {
    loadingInstance.hide();
  },
};
