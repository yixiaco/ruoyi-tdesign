import type { R, TableDataInfo } from '@/api/model/resultModel';
import type { SysUserVo } from '@/api/system/model/userModel';
import type { TaskVo } from '@/api/workflow/task/types';
import { request } from '@/utils/request';

/**
 * 分页查询工作流选择加签人员
 * @param query
 */
export const getPageByAddMultiInstance = (query: object) => {
  return request.get<TableDataInfo<SysUserVo>>({
    url: '/workflow/user/getPageByAddMultiInstance',
    params: query,
  });
};

/**
 * 查询工作流选择减签人员
 * @param taskId
 */
export const getListByDeleteMultiInstance = (taskId: string) => {
  return request.get<R<TaskVo[]>>({
    url: `/workflow/user/getListByDeleteMultiInstance/${taskId}`,
  });
};

/**
 * 按照用户id查询用户
 * @param userIdList
 */
export const getUserListByIds = (userIdList: any[]) => {
  return request.get<R<SysUserVo[]>>({
    url: `/workflow/user/getUserListByIds/${userIdList}`,
  });
};

/**
 * 分页查询用户
 * @param query
 */
export const getPageByUserList = (query: object) => {
  return request.get<TableDataInfo<SysUserVo>>({
    url: '/workflow/user/getPageByUserList',
    params: query,
  });
};
