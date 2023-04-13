import axios from 'axios';
import { saveAs } from 'file-saver';
import { LoadingPlugin, MessagePlugin } from 'tdesign-vue-next';

import { getToken } from '@/utils/auth';
import errorCode from '@/utils/errorCode';
import { blobValidate } from '@/utils/ruoyi';

const baseURL = import.meta.env.VITE_APP_BASE_API;
let downloadLoadingInstance;

export default {
  oss(ossId) {
    const url = `${baseURL}/system/oss/download/${ossId}`;
    downloadLoadingInstance = LoadingPlugin({
      text: '正在下载数据，请稍候',
    });
    axios({
      method: 'get',
      url,
      responseType: 'blob',
      headers: { Authorization: `Bearer ${getToken()}` },
    })
      .then((res) => {
        const isBlob = blobValidate(res.data);
        if (isBlob) {
          const blob = new Blob([res.data], { type: 'application/octet-stream' });
          this.saveAs(blob, decodeURI(res.headers['download-filename']));
        } else {
          this.printErrMsg(res.data);
        }
        downloadLoadingInstance.hide();
      })
      .catch((r) => {
        console.error(r);
        MessagePlugin.error('下载文件出现错误，请联系管理员！');
        downloadLoadingInstance.hide();
      });
  },
  zip(url, name) {
    url = baseURL + url;
    axios({
      method: 'get',
      url,
      responseType: 'blob',
      headers: {
        Authorization: `Bearer ${getToken()}`,
        datasource: localStorage.getItem('dataName'),
      },
    }).then((res) => {
      const isBlob = blobValidate(res.data);
      if (isBlob) {
        const blob = new Blob([res.data], { type: 'application/zip' });
        this.saveAs(blob, name);
      } else {
        this.printErrMsg(res.data);
      }
    });
  },
  saveAs(text, name, opts) {
    saveAs(text, name, opts);
  },
  async printErrMsg(data) {
    const resText = await data.text();
    const rspObj = JSON.parse(resText);
    const errMsg = errorCode[rspObj.code] || rspObj.msg || errorCode.default;
    MessagePlugin.error(errMsg);
  },
};
