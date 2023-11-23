import merge from 'lodash/merge';
import componentsLocale from 'tdesign-vue-next/es/locale/en_US';

import components from './components';
import layout from './layout';
import pages from './pages';

export default {
  lang: 'English',
  layout,
  pages,
  components,
  constants: {
    switchLanguage: 'the language switch was successful',
  },
  componentsLocale: merge({}, componentsLocale, {
    // 可以在此处定义更多自定义配置，具体可配置内容参看 API 文档
    // https://tdesign.tencent.com/vue-next/config?tab=api
    // pagination: {
    //   jumpTo: 'xxx'
    // },
  }),
};
