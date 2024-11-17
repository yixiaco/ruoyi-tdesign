import type { BaseEntity, BasePageQuery } from '@/api/model/resultModel';

export interface LeaveVo {
  id: string | number;
  leaveType: string;
  startDate: string;
  endDate: string;
  leaveDays: number;
  remark: string;
  status?: string;
}

export interface LeaveForm extends BaseEntity {
  id?: string | number;
  leaveType?: string;
  startDate?: string;
  endDate?: string;
  leaveDays?: number;
  remark?: string;
  status?: string;
}

export interface LeaveQuery extends BasePageQuery {
  startLeaveDays?: number;
  endLeaveDays?: number;
}
