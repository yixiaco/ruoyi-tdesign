import ContextPadProvider from 'bpmn-js/lib/features/context-pad/ContextPadProvider';
import type BpmnFactory from 'bpmn-js/lib/features/modeling/BpmnFactory';
import type ElementFactory from 'bpmn-js/lib/features/modeling/ElementFactory';
import type Modeling from 'bpmn-js/lib/features/modeling/Modeling.js';
import type Canvas from 'diagram-js/lib/core/Canvas';
import type EventBus from 'diagram-js/lib/core/EventBus';
import type Connect from 'diagram-js/lib/features/connect/Connect';
import type ContextPad from 'diagram-js/lib/features/context-pad/ContextPad';
import type Create from 'diagram-js/lib/features/create/Create';
import type PopupMenu from 'diagram-js/lib/features/popup-menu/PopupMenu';
import type Rules from 'diagram-js/lib/features/rules/Rules';
import type { Element, Shape } from 'diagram-js/lib/model/Types';
import type { Injector } from 'didi';

import modeler from '@/store/modules/modeler';

// @Description: 增强元素连线事件

class CustomContextPadProvider extends ContextPadProvider {
  private contextPad: ContextPad;

  private modeling: Modeling;

  private elementFactory: ElementFactory;

  private autoPlace: any;

  private connect: Connect;

  private create: Create;

  private popupMenu: PopupMenu;

  private canvas: Canvas;

  private rules: Rules;

  constructor(
    config: any,
    injector: Injector,
    eventBus: EventBus,
    contextPad: ContextPad,
    modeling: Modeling,
    elementFactory: ElementFactory,
    connect: Connect,
    create: Create,
    popupMenu: PopupMenu,
    canvas: Canvas,
    rules: Rules,
    translate: any,
  ) {
    // @ts-ignore
    super(
      config,
      // @ts-ignore
      injector,
      eventBus,
      contextPad,
      modeling,
      elementFactory,
      connect,
      create,
      popupMenu,
      canvas,
      rules,
      translate,
    );

    this.contextPad = contextPad;
    this.modeling = modeling;
    this.elementFactory = elementFactory;
    this.connect = connect;
    this.create = create;
    this.popupMenu = popupMenu;
    this.canvas = canvas;
    this.rules = rules;

    this.autoPlace = injector.get('autoPlace', false);
  }

  getContextPadEntries(_: Element) {
    const actions: Record<string, any> = {};

    const appendUserTask = (event: Event, element: Shape) => {
      const shape = this.elementFactory.createShape({ type: 'bpmn:UserTask' });
      this.create.start(event, shape, {
        source: element,
      });
    };

    const appendMultiInstanceUserTask = (event: Event, element: Shape) => {
      const store = modeler();
      const bpmnFactory = store.getModeler().get('bpmnFactory') as BpmnFactory;
      const businessObject = bpmnFactory.create('bpmn:UserTask', {
        // name: '多实例用户任务',
        isForCompensation: false,
      });
      businessObject.loopCharacteristics = bpmnFactory.create('bpmn:MultiInstanceLoopCharacteristics');
      // 创建 Shape
      const shape = this.elementFactory.createShape({
        type: 'bpmn:UserTask',
        businessObject,
      });
      this.create.start(event, shape, { source: element });
    };

    const appendTask = this.autoPlace
      ? (event: Event, element: Shape) => {
          const bpmnFactory: BpmnFactory | undefined = modeler().getModeler().get('bpmnFactory');
          const businessObject = bpmnFactory.create('bpmn:UserTask', {
            // name: '多实例用户任务',// 右键创建显示
            isForCompensation: false,
          });

          // 创建多实例属性并分配给用户任务的 loopCharacteristics
          businessObject.loopCharacteristics = bpmnFactory.create('bpmn:MultiInstanceLoopCharacteristics');

          // 创建 Shape
          const shape = this.elementFactory.createShape({
            type: 'bpmn:UserTask',
            businessObject,
          });

          this.autoPlace.append(element, shape);
        }
      : appendMultiInstanceUserTask;

    const append = this.autoPlace
      ? (event: Event, element: Shape) => {
          const shape = this.elementFactory.createShape({ type: 'bpmn:UserTask' });
          this.autoPlace.append(element, shape);
        }
      : appendUserTask;

    // // 添加创建用户任务按钮
    actions['append.append-user-task'] = {
      group: 'model',
      className: 'bpmn-icon-user-task',
      title: '用户任务',
      action: {
        dragstart: appendUserTask,
        click: append,
      },
    };

    // 添加创建多实例用户任务按钮
    actions['append.append-multi-instance-user-task'] = {
      group: 'model',
      className: 'bpmn-icon-user', // 你可以使用多实例用户任务的图标  bpmn-icon-user   bpmn-icon-user-task
      title: '多实例用户任务',
      action: {
        dragstart: appendMultiInstanceUserTask,
        click: appendTask,
      },
    };

    return actions;
  }
}

export default CustomContextPadProvider;
