// 获取常用时间
import type { ConfigType, OpUnitType, QUnitType } from 'dayjs';
import dayjs from 'dayjs';

export const LAST_7_DAYS = [
  dayjs().subtract(7, 'day').format('YYYY-MM-DD'),
  dayjs().subtract(1, 'day').format('YYYY-MM-DD'),
];

export const LAST_30_DAYS = [
  dayjs().subtract(30, 'day').format('YYYY-MM-DD'),
  dayjs().subtract(1, 'day').format('YYYY-MM-DD'),
];

/**
 * 返回指定单位下两个日期时间之间的差异。
 * @param date1 时间1
 * @param date2 时间2
 * @param unit 单位
 * @param float 小数位
 */
export function dateDiff(date1: ConfigType, date2: ConfigType, unit?: QUnitType | OpUnitType, float?: boolean) {
  return dayjs(date1).diff(date2, unit, float);
}

/**
 * 返回指定单位下两个日期时间之间的差异。
 * @param date1 时间1
 * @param date2 时间2
 * @param unit 单位
 * @param float 小数位
 */
export function dateDiffAbs(date1: ConfigType, date2: ConfigType, unit?: QUnitType | OpUnitType, float?: boolean) {
  return Math.abs(dayjs(date1).diff(date2, unit, float));
}

/**
 * 时间格式化
 * @param date 时间
 * @param format 格式化格式
 */
export function dateFormat(date: ConfigType, format: string) {
  return dayjs(date).format(format);
}
