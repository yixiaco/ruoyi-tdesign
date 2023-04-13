import { R } from '@/api/model/resultModel';
import { RouterVo } from '@/api/model/routeModel';
import { request } from '@/utils/request';

// 获取路由
export const getRouters = () => {
  return request.get<R<Array<RouterVo>>>({
    url: '/system/menu/getRouters',
  });
};
