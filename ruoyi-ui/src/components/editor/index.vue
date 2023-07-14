<template>
  <div>
    <!--  文档:https://www.tiny.cloud/docs/tinymce/6/-->
    <editor ref="editorRef" :api-key="apiKey" :init="conf" v-bind="$attrs" />
  </div>
</template>

<script lang="ts" setup>
import 'tinymce/tinymce';
import 'tinymce/models/dom';
import 'tinymce/icons/default';
import 'tinymce/themes/silver/theme';
// Skins
import 'tinymce/skins/ui/oxide/skin.min.css';
import 'tinymce/skins/ui/oxide/content.min.css';
// Plugins 组件根据init中调用情况自行加载
import 'tinymce/plugins/preview';
import 'tinymce/plugins/importcss';
import 'tinymce/plugins/searchreplace';
import 'tinymce/plugins/autolink';
import 'tinymce/plugins/autosave';
import 'tinymce/plugins/save';
import 'tinymce/plugins/directionality';
import 'tinymce/plugins/code';
import 'tinymce/plugins/visualblocks';
import 'tinymce/plugins/visualchars';
import 'tinymce/plugins/fullscreen';
import 'tinymce/plugins/image';
import 'tinymce/plugins/link';
import 'tinymce/plugins/media';
import 'tinymce/plugins/codesample';
import 'tinymce/plugins/table';
import 'tinymce/plugins/charmap';
import 'tinymce/plugins/pagebreak';
import 'tinymce/plugins/nonbreaking';
import 'tinymce/plugins/anchor';
import 'tinymce/plugins/insertdatetime';
import 'tinymce/plugins/advlist';
import 'tinymce/plugins/lists';
import 'tinymce/plugins/wordcount';
import 'tinymce/plugins/help';
import 'tinymce/plugins/quickbars';

// import 'tinymce/plugins/emoticons';
import Editor from '@tinymce/tinymce-vue';
import type { RawEditorOptions } from 'tinymce';
import { computed, onMounted, ref, watch } from 'vue';

import { uploader } from '@/api/system/oss';
import { useSettingStore } from '@/store';

const apiKey = '';
const settingStore = useSettingStore();
const useDarkMode = computed(() => settingStore.displayMode === 'dark');
// 图片上传逻辑
const uploadHandler: RawEditorOptions['images_upload_handler'] = (blobInfo): Promise<string> => {
  const file = blobInfo.blob();
  const formData = new FormData();
  formData.append('file', file);
  return uploader(formData).then((res) => res.data.url);
};
const editorRef = ref<any>(null);
watch(useDarkMode, () => {
  const reConfig = {
    skin_url: useDarkMode.value ? '/skins/ui/oxide-dark' : '/skins/ui/oxide',
    content_css: useDarkMode.value ? '/skins/content/dark/content.min.css' : '/skins/content/default/content.min.css',
  };
  // @ts-ignore
  editorRef.value.rerender(reConfig);
  removeTinyWarning();
});
// 重新渲染时会再次出现提醒，因此单独抽离出来
function removeTinyWarning() {
  // 当观察到变动时执行的回调函数
  function callback(mutationsList: any, observer: any) {
    for (const mutation of mutationsList) {
      if (mutation.type === 'childList') {
        const dcm = document.querySelector('.tox-notification__dismiss') as HTMLButtonElement;
        if (dcm) {
          dcm.click();
          observer.disconnect();
          return;
        }
      }
    }
  }
  if (!apiKey) {
    // 选择需要观察变动的节点
    const targetNode = document.getElementsByTagName('body')[0];
    // 观察器的配置（需要观察什么变动）
    const config = { childList: true, subtree: true };
    // 创建一个观察器实例并传入回调函数
    const bodyObserver = new MutationObserver(callback);
    // 以上述配置开始观察目标节点
    bodyObserver.observe(targetNode, config);
  }
}
onMounted(() => {
  removeTinyWarning();
});
// 配置项
const conf: RawEditorOptions = {
  promotion: false,
  language_url: '/langs/zh-Hans.js',
  language: 'zh-Hans',
  plugins:
    'preview importcss searchreplace autolink autosave save directionality code visualblocks visualchars fullscreen image link media codesample table charmap pagebreak nonbreaking anchor insertdatetime advlist lists wordcount help charmap quickbars',
  editimage_cors_hosts: ['picsum.photos'],
  menubar: 'file edit view insert format tools table help',
  toolbar:
    'undo redo | bold italic underline strikethrough | fontfamily fontsize blocks | alignleft aligncenter alignright alignjustify | outdent indent |  numlist bullist | forecolor backcolor removeformat | pagebreak | charmap | fullscreen  preview save print | insertfile image media link anchor codesample | ltr rtl',
  autosave_ask_before_unload: true,
  autosave_interval: '30s',
  autosave_prefix: '{path}{query}-{id}-',
  autosave_restore_when_empty: false,
  autosave_retention: '2m',
  image_advtab: true,
  image_uploadtab: true,
  images_file_types: 'jpg,jpeg,png,bmp',
  images_upload_handler: uploadHandler,
  // images_upload_url: 'http://localhost:3000/upload/album',
  file_picker_callback: (callback: any, _value: any, meta: any) => {
    // Provide file and text for the link dialog
    const fileName = meta.fileName;
    if (meta.filetype === 'file') {
      callback(fileName, { text: 'My text' });
    }
    // Provide image and alt text for the image dialog
    if (meta.filetype === 'image') {
      const fileName = 'myimage.jpg';
      callback(fileName, { alt: 'My alt text' });
    }
    // Provide alternative source and posted for the media dialog
    if (meta.filetype === 'media') {
      callback(fileName, { source2: 'alt.ogg', poster: 'image.jpg' });
    }
  },
  importcss_append: true,
  height: 600,
  image_caption: true,
  quickbars_selection_toolbar: 'bold italic | quicklink h2 h3 blockquote quickimage quicktable',
  noneditable_class: 'mceNonEditable',
  toolbar_mode: 'sliding',
  contextmenu: 'link image table',
  skin_url: useDarkMode.value ? '/skins/ui/oxide-dark' : '/skins/ui/oxide',
  content_css: useDarkMode.value ? '/skins/content/dark/content.min.css' : '/skins/content/default/content.min.css',
  content_style: 'body { font-family:Helvetica,Arial,sans-serif; font-size:16px }',
  // skin: useDarkMode.value ? oxideDark : oxide,
  // content_css: useDarkMode.value ? 'dark' : 'default',
  // skin_url: '/skins/ui/oxide',
};
</script>
