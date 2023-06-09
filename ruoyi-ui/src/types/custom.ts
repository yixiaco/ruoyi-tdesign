import auth from '@/plugins/auth';
import cache from '@/plugins/cache';
import $download from '@/plugins/download';
import modal from '@/plugins/modal';

declare module 'vue' {
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

export {};
