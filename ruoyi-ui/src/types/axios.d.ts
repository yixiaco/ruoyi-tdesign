import { AxiosRequestConfig } from 'axios';

export interface RequestOptions {
  apiUrl?: string;
  isJoinPrefix?: boolean;
  urlPrefix?: string;
  joinParamsToUrl?: boolean;
  formatDate?: boolean;
  isTransformResponse?: boolean;
  isReturnNativeResponse?: boolean;
  ignoreRepeatRequest?: boolean;
  joinTime?: boolean;
  withToken?: boolean;
  retry?: {
    count: number;
    delay: number;
  };
  repeatSubmit?: boolean;
}

export interface Result<T = any> {
  msg: string;
  code: number;
  data: T;
}

export interface AxiosRequestConfigRetry extends AxiosRequestConfig {
  retryCount?: number;
}
