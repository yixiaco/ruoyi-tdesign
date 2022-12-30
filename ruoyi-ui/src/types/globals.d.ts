// 通用声明

// Vue
declare module '*.vue' {
  import { DefineComponent } from 'vue';

  const component: DefineComponent<{}, {}, any>;
  export default component;
}

declare module 'vue' {
  import auth from '@/plugins/auth';
  import cache from '@/plugins/cache';
  import modal from '@/plugins/modal';
  import $download from '@/plugins/download';

  interface ComponentCustomProperties {
    useDict: typeof import('@/utils/dict')['useDict'];
    getConfigKey: typeof import('@/api/system/config')['getConfigKey'];
    updateConfigByKey: typeof import('@/api/system/config')['updateConfigByKey'];
    download: typeof import('@/utils/request')['download'];
    parseTime: typeof import('@/utils/ruoyi')['parseTime'];
    resetForm: typeof import('@/utils/ruoyi')['resetForm'];
    bytesToSize: typeof import('@/utils/ruoyi')['bytesToSize'];
    handleTree: typeof import('@/utils/ruoyi')['handleTree'];
    addDateRange: typeof import('@/utils/ruoyi')['addDateRange'];
    selectDictLabel: typeof import('@/utils/ruoyi')['selectDictLabel'];
    selectDictLabels: typeof import('@/utils/ruoyi')['selectDictLabels'];
    $auth: typeof auth;
    $cache: typeof cache;
    $modal: typeof modal;
    $download: typeof $download;
    $dialog: typeof import('tdesign-vue-next')['DialogPlugin'];
  }
}

declare type ClassName = { [className: string]: any } | ClassName[] | string;

declare module '*.svg' {
  const CONTENT: string;
  export default CONTENT;
}

declare type Recordable<T = any> = Record<string, T>;

export {};
