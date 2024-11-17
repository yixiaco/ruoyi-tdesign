import type EventBus from 'diagram-js/lib/core/EventBus';
import BaseRenderer from 'diagram-js/lib/draw/BaseRenderer';
import type { Shape } from 'diagram-js/lib/model/Types';
import { attr as svgAttr } from 'tiny-svg';

const HIGH_PRIORITY = 1500;
export default class CustomRenderer extends BaseRenderer {
  bpmnRenderer: BaseRenderer;

  modeling: any;

  constructor(eventBus: EventBus, bpmnRenderer: BaseRenderer, modeling: any) {
    super(eventBus, HIGH_PRIORITY);
    this.bpmnRenderer = bpmnRenderer;
    this.modeling = modeling;
  }

  canRender(element: any) {
    // ignore labels
    return !element.labelTarget;
  }

  /**
   * 自定义节点图形
   * @param {*} parentNode 当前元素的svgNode
   * @param {*} element
   * @returns
   */
  drawShape(parentNode: SVGElement, element: Shape) {
    const shape = this.bpmnRenderer.drawShape(parentNode, element);
    const { type } = element;
    // 开始 填充绿色
    if (type === 'bpmn:StartEvent') {
      svgAttr(shape, { fill: '#77DF6D' });
      return shape;
    }
    if (type === 'bpmn:EndEvent') {
      svgAttr(shape, { fill: '#EE7B77' });
      return shape;
    }
    if (type === 'bpmn:UserTask') {
      svgAttr(shape, { fill: '#A9C4F8' });
      return shape;
    }
    return shape;
  }

  getShapePath(shape: Shape) {
    return this.bpmnRenderer.getShapePath(shape);
  }
}
// @ts-ignore
CustomRenderer.$inject = ['eventBus', 'bpmnRenderer'];
