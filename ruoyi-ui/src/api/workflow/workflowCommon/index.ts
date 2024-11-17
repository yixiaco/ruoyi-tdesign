import type { RouterJumpVo } from '@/api/workflow/workflowCommon/types';

export function useRouterJump() {
  const router = useRouter();
  const { proxy } = getCurrentInstance();
  return (routerJumpVo: RouterJumpVo) => {
    if (
      routerJumpVo.wfNodeConfigVo &&
      routerJumpVo.wfNodeConfigVo.formType === 'static' &&
      routerJumpVo.wfNodeConfigVo.wfFormManageVo
    ) {
      router.push({
        path: `${routerJumpVo.wfNodeConfigVo.wfFormManageVo.router}`,
        query: {
          id: routerJumpVo.businessKey,
          type: routerJumpVo.type,
          taskId: routerJumpVo.taskId,
        },
      });
    } else if (
      routerJumpVo.wfNodeConfigVo &&
      routerJumpVo.wfNodeConfigVo.formType === 'dynamic' &&
      routerJumpVo.wfNodeConfigVo.wfFormManageVo
    ) {
      // proxy.$tab.closePage(proxy.$route);
      router.push({
        path: `${routerJumpVo.wfNodeConfigVo.wfFormManageVo.router}`,
        query: {
          id: routerJumpVo.businessKey,
          type: routerJumpVo.type,
          taskId: routerJumpVo.taskId,
        },
      });
    } else {
      proxy?.$modal.msgError('请到模型配置菜单！');
    }
  };
}
