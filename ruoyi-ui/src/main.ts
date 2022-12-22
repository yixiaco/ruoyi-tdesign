import { createApp } from 'vue';

import HljsVuePlugin from '@highlightjs/vue-plugin';
import { useDict } from '@/utils/dict';

// 注册指令
import plugins from './plugins'; // plugins
import { download } from '@/utils/request';
import directive from './directive'; // directive
import { parseTime, resetForm, addDateRange, handleTree, selectDictLabel, selectDictLabels } from '@/utils/ruoyi';
import { getConfigKey, updateConfigByKey } from '@/api/system/config';

// 字典标签组件
import DictTag from '@/components/dict-tag/index.vue';
// 文件上传组件
import FileUpload from '@/components/file-upload/index.vue';
// 图片上传组件
import ImageUpload from '@/components/image-upload/index.vue';
// 图片预览组件
import ImagePreview from '@/components/image-preview/index.vue';

import { store } from './store';
import router from './router';
import '@/style/index.less';
import './permission';
import App from './App.vue';

const app = createApp(App);

// 全局方法挂载
app.config.globalProperties.useDict = useDict;
app.config.globalProperties.getConfigKey = getConfigKey;
app.config.globalProperties.updateConfigByKey = updateConfigByKey;
app.config.globalProperties.download = download;
app.config.globalProperties.parseTime = parseTime;
app.config.globalProperties.resetForm = resetForm;
app.config.globalProperties.handleTree = handleTree;
app.config.globalProperties.addDateRange = addDateRange;
app.config.globalProperties.selectDictLabel = selectDictLabel;
app.config.globalProperties.selectDictLabels = selectDictLabels;

// 全局组件挂载
app.component('DictTag', DictTag);
app.component('FileUpload', FileUpload);
app.component('ImageUpload', ImageUpload);
app.component('ImagePreview', ImagePreview);

app.use(store);
app.use(router);
app.use(plugins);
app.use(HljsVuePlugin);

directive(app);

app.mount('#app');
