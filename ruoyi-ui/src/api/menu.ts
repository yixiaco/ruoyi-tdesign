import { request } from '@/utils/request';
import { RouterVo } from '@/api/model/routeModel';
import { R } from '@/api/model/resultModel';

// 获取路由
export const getRouters = () => {
  return request.get<R<Array<RouterVo>>>({
    url: '/getRouters',
  });
};
