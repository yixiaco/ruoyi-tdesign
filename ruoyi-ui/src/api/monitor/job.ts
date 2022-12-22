import { request } from '@/utils/request';

// 查询定时任务调度列表
export function listJob(query) {
  return request.get({
    url: '/monitor/job/list',
    params: query,
  });
}

// 查询定时任务调度详细
export function getJob(jobId) {
  return request.get({
    url: `/monitor/job/${jobId}`,
  });
}

// 新增定时任务调度
export function addJob(data) {
  return request.post({
    url: '/monitor/job',
    data,
  });
}

// 修改定时任务调度
export function updateJob(data) {
  return request.put({
    url: '/monitor/job',
    data,
  });
}

// 删除定时任务调度
export function delJob(jobId) {
  return request.delete({
    url: `/monitor/job/${jobId}`,
  });
}

// 任务状态修改
export function changeJobStatus(jobId, status) {
  const data = {
    jobId,
    status,
  };
  return request.put({
    url: '/monitor/job/changeStatus',
    data,
  });
}

// 定时任务立即执行一次
export function runJob(jobId, jobGroup) {
  const data = {
    jobId,
    jobGroup,
  };
  return request.put({
    url: '/monitor/job/run',
    data,
  });
}
