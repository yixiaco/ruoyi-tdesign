import {
  DialogInstance,
  DialogOptions,
  DialogPlugin,
  Form,
  FormItem,
  Input,
  LoadingInstance,
  LoadingPlugin,
  MessagePlugin,
  NotifyPlugin,
  ValidateResultContext,
} from 'tdesign-vue-next';
import { TNode } from 'tdesign-vue-next/es/common';
import { MessageInfoOptions, MessageInstance } from 'tdesign-vue-next/es/message/type';
import { h, reactive, ref } from 'vue';

let loadingInstance: LoadingInstance;

export default {
  // 消息提示
  msg(message: string | MessageInfoOptions, duration?: number) {
    return MessagePlugin.info(message, duration);
  },
  // 错误消息
  msgError(message: string | MessageInfoOptions, duration?: number) {
    return MessagePlugin.error(message, duration);
  },
  // 成功消息
  msgSuccess(message: string | MessageInfoOptions, duration?: number) {
    return MessagePlugin.success(message, duration);
  },
  // 警告消息
  msgWarning(message: string | MessageInfoOptions, duration?: number) {
    return MessagePlugin.warning(message, duration);
  },
  // 消息类型的加载中
  msgLoading(message: string | MessageInfoOptions, duration?: number) {
    return MessagePlugin.loading(message, duration);
  },
  // 关闭消息
  msgClose(options: Promise<MessageInstance>) {
    MessagePlugin.close(options);
  },
  // 关闭所有消息
  msgCloseAll() {
    MessagePlugin.closeAll();
  },
  // 弹出提示
  alert(content: DialogOptions | string) {
    let instance: DialogInstance;
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
    let instance: DialogInstance;
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
    let instance: DialogInstance;
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
    let instance: DialogInstance;
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
  notify(content: any) {
    return NotifyPlugin.info({
      content,
    });
  },
  // 错误通知
  notifyError(content: any) {
    return NotifyPlugin.error({
      content,
    });
  },
  // 成功通知
  notifySuccess(content: any) {
    return NotifyPlugin.success({
      content,
    });
  },
  // 警告通知
  notifyWarning(content: any) {
    return NotifyPlugin.warning({
      content,
    });
  },
  // 确认窗体
  confirm(content: string, onConfirm: Function, onClose?: Function) {
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
      theme: 'default',
      onClose: () => {
        if (onClose != null) {
          onClose();
        }
        instance.destroy();
      },
    });
    return instance;
  },
  // 提交内容
  prompt(
    tip: string,
    title: string,
    props: {
      confirmButtonText?: string;
      onConfirm?: (value: string) => {};
      theme?: 'default' | 'info' | 'warning' | 'danger' | 'success' | 'primary';
      cancelButtonText?: string | null;
      closeOnClickModal?: boolean;
      inputErrorMessage?: string;
      inputPattern?: RegExp;
    },
  ) {
    const btn = reactive({
      content: props.confirmButtonText,
      loading: false,
      onClick: async () => {
        formRef.value.validate().then(async (result: ValidateResultContext<FormData> | boolean) => {
          if (result === true) {
            btn.loading = true;
            try {
              await props?.onConfirm(form.input);
              instance.destroy();
            } finally {
              btn.loading = false;
            }
          } else if (props.inputErrorMessage) {
            await this.msgError(props.inputErrorMessage);
          }
        });
      },
    });
    const form = reactive({
      input: undefined,
    });
    const formRef = ref(null);
    const instance = DialogPlugin.confirm({
      // @ts-ignore
      theme: props.theme || 'warning',
      header: title || '系统提示',
      confirmBtn: btn,
      cancelBtn: props.cancelButtonText,
      closeOnOverlayClick: props.closeOnClickModal,
      body: () =>
        h(
          // @ts-ignore
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
              'onUpdate:value': (value: any) => {
                form.input = value;
              },
            }),
          ),
        ),
    });
    return instance;
  },
  // 打开遮罩层
  loading(content: string | TNode) {
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
