import { request } from '@/utils/request';
import { R } from '@/api/model/resultModel';

// 发送测试邮件
export function sendTestMail(toMail) {
  return request.post<R<void>>({
    url: '/mail/sendTestMail',
    params: { toMail },
  });
}
