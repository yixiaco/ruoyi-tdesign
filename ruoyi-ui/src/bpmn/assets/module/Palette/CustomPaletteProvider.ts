import type BpmnFactory from 'bpmn-js/lib/features/modeling/BpmnFactory';
import type ElementFactory from 'bpmn-js/lib/features/modeling/ElementFactory';
import PaletteProvider from 'bpmn-js/lib/features/palette/PaletteProvider';
import type Create from 'diagram-js/lib/features/create/Create';
import type GlobalConnect from 'diagram-js/lib/features/global-connect/GlobalConnect';
import type HandTool from 'diagram-js/lib/features/hand-tool/HandTool';
import type LassoTool from 'diagram-js/lib/features/lasso-tool/LassoTool';
import type Palette from 'diagram-js/lib/features/palette/Palette';
import type SpaceTool from 'diagram-js/lib/features/space-tool/SpaceTool';
import type { Shape } from 'diagram-js/lib/model/Types';
import { assign } from 'min-dash';

import modeler from '@/store/modules/modeler';

// @Description: 增强左侧面板
class CustomPaletteProvider extends PaletteProvider {
  private readonly palette: Palette;

  private readonly create: Create;

  private readonly elementFactory: ElementFactory;

  private readonly spaceTool: SpaceTool;

  private readonly lassoTool: LassoTool;

  private readonly handTool: HandTool;

  private readonly globalConnect: GlobalConnect;

  private readonly translate: any;

  constructor(
    palette: Palette,
    create: Create,
    elementFactory: ElementFactory,
    spaceTool: SpaceTool,
    lassoTool: LassoTool,
    handTool: HandTool,
    globalConnect: GlobalConnect,
    translate: any,
  ) {
    super(palette, create, elementFactory, spaceTool, lassoTool, handTool, globalConnect, translate);
    this.palette = palette;
    this.create = create;
    this.elementFactory = elementFactory;
    this.spaceTool = spaceTool;
    this.lassoTool = lassoTool;
    this.handTool = handTool;
    this.globalConnect = globalConnect;
    this.translate = translate;
  }

  getPaletteEntries() {
    const actions = {};
    const create = this.create;
    const elementFactory = this.elementFactory;
    const translate = this.translate;

    function createAction(type: string, group: string, className: string, title: string, options?: object) {
      function createListener(event: Shape) {
        const shape = elementFactory.createShape(assign({ type }, options));
        if (options) {
          shape.businessObject.di ??= {};
          shape.businessObject.di.isExpanded = (options as { [key: string]: any }).isExpanded;
        }
        create.start(event, shape, null);
      }
      const shortType = type.replace(/^bpmn:/, '');
      return {
        group,
        className,
        title: title || translate('Create {type}', { type: shortType }),
        action: {
          dragstart: createListener,
          click: createListener,
        },
      };
    }

    function createMultiInstanceUserTask(event: Shape) {
      const bpmnFactory: BpmnFactory | undefined = modeler().getBpmnFactory();
      // 创建一个 bpmn:UserTask
      const userTask = bpmnFactory.create('bpmn:UserTask', {
        // name: '多实例用户任务', // 在画板中显示字段
        isForCompensation: false,
      });
      // 将多实例属性分配给 bpmn:UserTask 的 loopCharacteristics
      userTask.loopCharacteristics = bpmnFactory.create('bpmn:MultiInstanceLoopCharacteristics');
      const customUserTask = elementFactory.createShape({
        type: 'bpmn:UserTask',
        businessObject: userTask, // 分配创建的 userTask 到 businessObject
      });
      create.start(event, customUserTask, {});
    }

    assign(actions, {
      'create.parallel-gateway': createAction(
        'bpmn:ParallelGateway',
        'gateway',
        'bpmn-icon-gateway-parallel',
        '并行网关',
      ),
      'create.event-base-gateway': createAction(
        'bpmn:EventBasedGateway',
        'gateway',
        'bpmn-icon-gateway-eventbased',
        '事件网关',
      ),
      // 分组线
      'gateway-separator': {
        group: 'gateway',
        separator: true,
      },
      'create.user-task': createAction('bpmn:UserTask', 'activity', 'bpmn-icon-user-task', '创建用户任务'),
      'create.multi-instance-user-task': {
        group: 'activity',
        type: 'bpmn:UserTask',
        className: 'bpmn-icon-user task-multi-instance',
        title: '创建多实例用户任务',
        action: {
          click: createMultiInstanceUserTask,
          dragstart: createMultiInstanceUserTask,
        },
      },
      'task-separator': {
        group: 'activity',
        separator: true,
      },
    });
    return actions;
  }
}

CustomPaletteProvider.$inject = [
  'palette',
  'create',
  'elementFactory',
  'spaceTool',
  'lassoTool',
  'handTool',
  'globalConnect',
  'translate',
];

export default CustomPaletteProvider;
