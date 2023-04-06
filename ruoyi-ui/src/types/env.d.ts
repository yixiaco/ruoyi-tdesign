export interface ImportMetaEnv {
  /** 打包路径 根据项目不同按需配置 */
  readonly VITE_BASE_URL: string;
  /** 页面标题 */
  readonly VITE_APP_TITLE: string;
  /** 企业名称 */
  readonly VITE_APP_COMPANY_NAME: string;
  /** 应用访问路径 例如使用前缀 /admin/ */
  readonly VITE_APP_CONTEXT_PATH: string;
  /** Admin监控地址 */
  readonly VITE_APP_MONITRO_ADMIN: string;
  /** Job监控地址 */
  readonly VITE_APP_XXL_JOB_ADMIN: string;
  /** 若依管理系统/生产环境 api前缀 */
  readonly VITE_APP_BASE_API: string;
  /** 是否在打包时开启压缩，支持 gzip 和 brotli */
  readonly VITE_BUILD_COMPRESS: string;
}
