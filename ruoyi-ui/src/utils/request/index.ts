// axios配置  可自行根据项目进行更改，只需更改该文件即可，其他文件可以不动
import isString from 'lodash/isString';
import merge from 'lodash/merge';
import { saveAs } from 'file-saver';
import { DialogPlugin, LoadingPlugin, MessagePlugin, NotifyPlugin } from 'tdesign-vue-next';
import type { InternalAxiosRequestConfig } from 'axios';
import type { AxiosTransform, CreateAxiosOptions } from './AxiosTransform';
import { VAxios } from './Axios';
import { joinTimestamp, formatRequestDate, setObjToUrlParams } from './utils';
import { ContentTypeEnum } from '@/constants';
import { getToken } from '@/utils/auth';
// @ts-ignore
import errorCode from '@/utils/errorCode';
import { tansParams, blobValidate } from '@/utils/ruoyi';
import cache from '@/plugins/cache';
import { useUserStore } from '@/store/modules/user';

// const env = import.meta.env.MODE || 'development';
// 是否显示重新登录
export const isRelogin = { show: false };

// 如果是mock模式 或 没启用直连代理 就不配置host 会走本地Mock拦截 或 Vite 代理
// const host = env === 'mock' || !proxy.isRequestProxy ? '' : proxy[env].host;

// 数据处理，方便区分多种处理方式
const transform: AxiosTransform = {
  // 处理请求数据。如果数据不是预期格式，可直接抛出错误
  transformRequestHook: (res, options) => {
    const { isTransformResponse, isReturnNativeResponse } = options;

    // 如果204无内容直接返回
    if (res.status === 204) {
      return res;
    }

    // 是否返回原生响应头 比如：需要获取响应头时使用该属性
    if (isReturnNativeResponse) {
      return res;
    }
    // 不进行任何处理，直接返回
    // 用于页面代码可能需要直接获取code，data，message这些信息时开启
    if (!isTransformResponse) {
      return res.data;
    }

    let errorMessage;
    // 错误的时候返回
    const { data } = res;
    if (!data) {
      errorMessage = '请求接口错误';
      MessagePlugin.error(errorMessage);
      throw new Error(errorMessage);
    }

    //  这里 code为 后台统一的字段，需要在 types.ts内修改为项目自己的接口返回格式
    // 未设置状态码则默认成功状态
    const code = res.data.code || 200;
    // 获取错误信息
    let msg = errorCode[code] || res.data.msg || errorCode.default;

    // 二进制数据则直接返回
    if (res.request.responseType === 'blob' || res.request.responseType === 'arraybuffer') {
      return res.data;
    }

    if (code === 401) {
      if (!isRelogin.show) {
        isRelogin.show = true;
        DialogPlugin.confirm({
          header: '系统提示',
          body: '登录状态已过期，您可以继续留在该页面，或者重新登录',
          cancelBtn: '取消',
          confirmBtn: '重新登录',
          theme: 'warning',
          onConfirm: () => {
            isRelogin.show = false;
            useUserStore()
              .logout()
              .then(() => {
                window.location.href = `${import.meta.env.VITE_APP_CONTEXT_PATH}index`;
              });
          },
          onCancel: () => {
            isRelogin.show = false;
          },
        });
        return res.data;
      }
      msg = '无效的会话，或者会话已过期，请重新登录。';
      MessagePlugin.error(msg);
      throw new Error(msg);
    } else if (code === 500) {
      MessagePlugin.error(msg);
      throw new Error(msg);
    } else if (code === 601) {
      MessagePlugin.warning(msg);
      throw new Error(msg);
    } else if (code !== 200) {
      NotifyPlugin.error({ title: msg });
      throw new Error('error');
    } else {
      return res.data;
    }

    // 这里逻辑可以根据项目进行修改
    // const hasSuccess = data && code === 0;
    // if (hasSuccess) {
    //   return data.data;
    // }
    //
    // errorMessage = `请求接口错误, 错误码: ${code}`;
    // MessagePlugin.error(errorMessage);
    // throw new Error(errorMessage);
  },

  // 请求前处理配置
  beforeRequestHook: (config, options) => {
    const { apiUrl, isJoinPrefix, urlPrefix, joinParamsToUrl, formatDate, joinTime = true } = options;

    // 添加接口前缀
    if (isJoinPrefix && urlPrefix && isString(urlPrefix)) {
      config.url = `${urlPrefix}${config.url}`;
    }

    // 将baseUrl拼接
    if (apiUrl && isString(apiUrl)) {
      config.url = `${apiUrl}${config.url}`;
    }
    const params = config.params || {};
    const data = config.data || false;

    if (formatDate && data && !isString(data)) {
      formatRequestDate(data);
    }
    if (config.method?.toUpperCase() === 'GET' || config.method?.toUpperCase() === 'DELETE') {
      if (!isString(params)) {
        // 给 get 请求加上时间戳参数，避免从缓存中拿数据。
        config.params = Object.assign(params || {}, joinTimestamp(joinTime, false));
      } else {
        let url = `${config.url}?${tansParams(config.params)}`;
        url = url.slice(0, -1);
        // 兼容restful风格
        config.url = `${url}${joinTimestamp(joinTime, true)}`;
        config.params = undefined;
      }
    } else if (!isString(params)) {
      if (formatDate) {
        formatRequestDate(params);
      }
      if (
        Reflect.has(config, 'data') &&
        config.data &&
        (Object.keys(config.data).length > 0 || data instanceof FormData)
      ) {
        config.data = data;
        config.params = params;
      } else {
        // 非GET请求如果没有提供data，则将params视为data
        config.data = params;
        // config.params = undefined;
      }
      if (joinParamsToUrl) {
        config.url = setObjToUrlParams(config.url as string, { ...config.params, ...config.data });
      }
    } else {
      // 兼容restful风格
      config.url += params;
      config.params = undefined;
    }
    return config;
  },

  // 请求拦截器处理
  requestInterceptors: (config, options) => {
    // 请求之前处理config
    const token = getToken();
    if (token && (config as Recordable)?.requestOptions?.withToken !== false) {
      // jwt token
      (config as Recordable).headers.Authorization = options.authenticationScheme
        ? `${options.authenticationScheme} ${token}`
        : token;
    }
    // 是否需要防止数据重复提交
    const isRepeatSubmit = (config as Recordable)?.requestOptions?.repeatSubmit === false;
    if (!isRepeatSubmit && (config.method.toUpperCase() === 'POST' || config.method.toUpperCase() === 'PUT')) {
      const requestObj = {
        url: config.url,
        data: typeof config.data === 'object' ? JSON.stringify(config.data) : config.data,
        time: new Date().getTime(),
      };
      const sessionObj = cache.session.getJSON('sessionObj');
      if (sessionObj === undefined || sessionObj === null || sessionObj === '') {
        cache.session.setJSON('sessionObj', requestObj);
      } else {
        const sUrl = sessionObj.url; // 请求地址
        const sData = sessionObj.data; // 请求数据
        const sTime = sessionObj.time; // 请求时间
        const interval = 1000; // 间隔时间(ms)，小于此时间视为重复提交
        if (sData === requestObj.data && requestObj.time - sTime < interval && sUrl === requestObj.url) {
          const message = '数据正在处理，请勿重复提交';
          console.warn(`[${sUrl}]: ${message}`);
          throw new Error(message);
        }
        cache.session.setJSON('sessionObj', requestObj);
      }
    }
    return config as InternalAxiosRequestConfig;
  },

  // 响应拦截器处理
  responseInterceptors: (res) => {
    return res;
  },

  // 响应错误处理
  responseInterceptorsCatch: (error: any) => {
    const { config } = error;
    console.log(`err${error}`);
    let { message } = error;
    if (message === 'Network Error') {
      message = '后端接口连接异常';
    } else if (message.includes('timeout')) {
      message = '系统接口请求超时';
    } else if (message.includes('Request failed with status code')) {
      message = `系统接口${message.substring(message.length - 3)}异常`;
    }
    if (!config || !config.requestOptions.retry) {
      MessagePlugin.error(message, 5000);
      return Promise.reject(error);
    }

    config.retryCount = config.retryCount || 0;

    if (config.retryCount >= config.requestOptions.retry.count) {
      MessagePlugin.error(message, 5000);
      return Promise.reject(error);
    }

    config.retryCount += 1;

    const backoff = new Promise((resolve) => {
      setTimeout(() => {
        resolve(config);
      }, config.requestOptions.retry.delay || 1);
    });
    // config.headers = { ...config.headers, 'Content-Type': 'application/json;charset=UTF-8' };
    return backoff.then((config) => request.request(config));
  },
};

function createAxios(opt?: Partial<CreateAxiosOptions>) {
  return new VAxios(
    merge(
      <CreateAxiosOptions>{
        // https://developer.mozilla.org/en-US/docs/Web/HTTP/Authentication#authentication_schemes
        // 例如: authenticationScheme: 'Bearer'
        authenticationScheme: 'Bearer',
        // 超时
        timeout: 30 * 1000,
        // 携带Cookie
        withCredentials: true,
        // 头信息
        headers: {
          'Content-Type': ContentTypeEnum.Json,
          'Content-Language': 'zh_CN',
        },
        baseURL: import.meta.env.VITE_APP_BASE_API,
        // 数据处理方式
        transform,
        // 配置项，下面的选项都可以在独立的接口请求中覆盖
        requestOptions: {
          // 接口地址
          // apiUrl: host,
          apiUrl: '',
          // 是否自动添加接口前缀
          isJoinPrefix: true,
          // 接口前缀
          // 例如: https://www.baidu.com/api
          // urlPrefix: '/api'
          urlPrefix: '',
          // 是否返回原生响应头 比如：需要获取响应头时使用该属性
          isReturnNativeResponse: false,
          // 需要对返回数据进行处理
          isTransformResponse: true,
          // post请求的时候添加参数到url
          joinParamsToUrl: false,
          // 格式化提交参数时间
          formatDate: true,
          // 是否加入时间戳
          joinTime: true,
          // 忽略重复请求
          ignoreRepeatRequest: true,
          // 是否携带token
          withToken: true,
          // 重试
          retry: {
            count: 0,
            delay: 1000,
          },
          // 防止数据重复提交
          repeatSubmit: true,
        },
      },
      opt || {},
    ),
  );
}
export const request = createAxios();

let downloadLoadingInstance;
// 通用下载方法
export function download(url, params, filename, config?) {
  downloadLoadingInstance = LoadingPlugin({ text: '正在下载数据，请稍候' /* , background: 'rgba(0, 0, 0, 0.7)' */ });
  return request
    .post({
      url,
      params,
      transformRequest: [
        (params) => {
          return tansParams(params);
        },
      ],
      headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
      responseType: 'blob',
      ...config,
    })
    .then(async (data) => {
      const isLogin = await blobValidate(data);
      if (isLogin) {
        const blob = new Blob([data]);
        saveAs(blob, filename);
      } else {
        const resText = await data.text();
        const rspObj = JSON.parse(resText);
        const errMsg = errorCode[rspObj.code] || rspObj.msg || errorCode.default;
        MessagePlugin.error(errMsg);
      }
      downloadLoadingInstance.hide();
    })
    .catch((r) => {
      console.error(r);
      MessagePlugin.error('下载文件出现错误，请联系管理员！');
      downloadLoadingInstance.hide();
    });
}
