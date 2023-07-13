<template>
  <div>
    <!--  文档:https://www.tiny.cloud/docs/tinymce/6/-->
    <editor ref="editorRef" :api-key="apiKey" :initial-value="value" :init="conf" v-bind="$attrs" />
  </div>
</template>

<script lang="ts" setup>
import Editor from '@tinymce/tinymce-vue';
import type { RawEditorOptions } from 'tinymce';
import { ref } from 'vue';

import { SysOssUploadVo } from '@/api/system/model/ossModel';
import { uploader } from '@/api/system/oss';

const apiKey = '';
const value = ref('');
const useDarkMode = window.matchMedia('(prefers-color-scheme: dark)').matches;

// 图片上传逻辑
const uploadHandler: RawEditorOptions['images_upload_handler'] = (blobInfo): Promise<string> => {
  const file = blobInfo.blob();
  const formData = new FormData();
  formData.append('file', file);
  return uploader(formData).then((res: SysOssUploadVo) => res.data.url);
};

// 配置项
const conf: RawEditorOptions = {
  language: 'zh_CN',
  plugins:
    'preview importcss searchreplace autolink autosave save directionality code visualblocks visualchars fullscreen image link media codesample table charmap pagebreak nonbreaking anchor insertdatetime advlist lists wordcount help charmap quickbars emoticons',
  editimage_cors_hosts: ['picsum.photos'],
  menubar: 'file edit view insert format tools table help',
  toolbar:
    'undo redo | bold italic underline strikethrough | fontfamily fontsize blocks | alignleft aligncenter alignright alignjustify | outdent indent |  numlist bullist | forecolor backcolor removeformat | pagebreak | charmap emoticons | fullscreen  preview save print | insertfile image media link anchor codesample | ltr rtl',
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
  skin: useDarkMode ? 'oxide-dark' : 'oxide',
  content_css: useDarkMode ? 'dark' : 'default',
  content_style: 'body { font-family:Helvetica,Arial,sans-serif; font-size:16px }',
};
</script>
