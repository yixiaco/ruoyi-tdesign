<template>
  <div class="containers">
    <div class="app-containers">
      <t-layout class="h-full">
        <t-content>
          <t-layout style="align-items: stretch; height: 100%">
            <t-header>
              <div class="process-toolbar">
                <t-space break-line :size="10">
                  <t-button size="small" theme="primary" @click="saveXml">保 存</t-button>
                  <t-dropdown size="small" placement="bottom" :popup-props="{ showArrow: true }">
                    <t-button size="small" theme="primary"> 预 览 </t-button>
                    <template #dropdown>
                      <t-dropdown-menu>
                        <t-dropdown-item icon="Document" @click="previewXML">XML预览</t-dropdown-item>
                        <t-dropdown-item icon="View" @click="previewSVG"> SVG预览</t-dropdown-item>
                      </t-dropdown-menu>
                    </template>
                  </t-dropdown>

                  <t-dropdown size="small" placement="bottom" :popup-props="{ showArrow: true }">
                    <t-button size="small" theme="primary"> 下 载 </t-button>
                    <template #dropdown>
                      <t-dropdown-menu>
                        <t-dropdown-item icon="Download" @click="downloadXML">下载XML</t-dropdown-item>
                        <t-dropdown-item icon="Download" @click="downloadSVG">下载SVG</t-dropdown-item>
                      </t-dropdown-menu>
                    </template>
                  </t-dropdown>
                  <t-tooltip content="新建" placement="bottom">
                    <t-button size="small" variant="outline" @click="newDiagram">
                      <template #icon>
                        <add-circle-icon />
                      </template>
                    </t-button>
                  </t-tooltip>
                  <t-tooltip content="自适应屏幕" placement="bottom">
                    <t-button size="small" variant="outline" @click="fitViewport">
                      <template #icon>
                        <drag-move-icon />
                      </template>
                    </t-button>
                  </t-tooltip>
                  <t-tooltip content="放大" placement="bottom">
                    <t-button size="small" variant="outline" @click="zoomViewport(true)">
                      <template #icon>
                        <zoom-in-icon />
                      </template>
                    </t-button>
                  </t-tooltip>
                  <t-tooltip content="缩小" placement="bottom">
                    <t-button size="small" variant="outline" @click="zoomViewport(false)">
                      <template #icon>
                        <zoom-out-icon />
                      </template>
                    </t-button>
                  </t-tooltip>
                  <t-tooltip content="后退" placement="bottom">
                    <t-button size="small" variant="outline" @click="bpmnModeler.get('commandStack').undo()">
                      <template #icon>
                        <arrow-left-icon />
                      </template>
                    </t-button>
                  </t-tooltip>
                  <t-tooltip content="前进" placement="bottom">
                    <t-button size="small" variant="outline" @click="bpmnModeler.get('commandStack').redo()">
                      <template #icon>
                        <arrow-right-icon />
                      </template>
                    </t-button>
                  </t-tooltip>
                </t-space>
              </div>
            </t-header>
            <t-content>
              <div ref="canvas" class="canvas" />
            </t-content>
          </t-layout>
        </t-content>
        <t-aside>
          <div :class="{ 'process-panel': true, hide: panelFlag }">
            <div class="process-panel-bar" @click="panelBarClick">
              <div class="open-bar">
                <t-link type="default" hover="color">
                  <caret-left-small-icon v-if="panelFlag" size="26" class-name="open-bar" />
                  <caret-right-small-icon v-else size="26" class-name="open-bar" />
                </t-link>
              </div>
            </div>
            <transition enter-active-class="animate__animated animate__fadeIn">
              <div v-show="showPanel" v-if="bpmnModeler" class="panel-content">
                <property-panel :modeler="bpmnModeler" />
              </div>
            </transition>
          </div>
        </t-aside>
      </t-layout>
    </div>
  </div>
  <div>
    <t-dialog v-model="perviewXMLShow" header="XML预览" width="80%" attach="body">
      <preview-code :code="xmlStr" language="XML" />
    </t-dialog>
  </div>
  <div>
    <t-dialog v-model="perviewSVGShow" header="SVG预览" width="80%" attach="body">
      <div style="text-align: center" v-html="svgData" />
    </t-dialog>
  </div>
</template>

<script lang="ts" setup>
import {
  AddCircleIcon,
  ArrowLeftIcon,
  ArrowRightIcon,
  CaretLeftSmallIcon,
  CaretRightSmallIcon,
  DragMoveIcon,
  ZoomInIcon,
  ZoomOutIcon,
} from 'tdesign-icons-vue-next';

defineOptions({
  name: 'BpmnDesign',
});
import 'bpmn-js/dist/assets/diagram-js.css';
import 'bpmn-js/dist/assets/bpmn-font/css/bpmn.css';
import 'bpmn-js/dist/assets/bpmn-font/css/bpmn-codes.css';
import 'bpmn-js/dist/assets/bpmn-font/css/bpmn-embedded.css';
import './assets/style/index.less';

import type { Canvas, Modeler } from 'bpmn';
import BpmnModeler from 'bpmn-js/lib/Modeler';

import defaultXML from '@/components/BpmnDesign/assets/defaultXML';
import flowableModdle from '@/components/BpmnDesign/assets/moddle/flowable';
import useDialog from '@/hooks/useDialog';
import useModelerStore from '@/store/modules/modeler';

import Modules from './assets/module/index';
import PropertyPanel from './panel/index.vue';

const emit = defineEmits(['closeCallBack', 'saveCallBack']);

const { openDialog, closeDialog } = useDialog({
  title: '编辑流程',
});
const modelerStore = useModelerStore();

const { proxy } = getCurrentInstance();

const panelFlag = ref(false);
const showPanel = ref(true);
const canvas = ref<HTMLDivElement>();
const panel = ref<HTMLDivElement>();
const bpmnModeler = ref<Modeler>();
const zoom = ref(1);
const perviewXMLShow = ref(false);
const perviewSVGShow = ref(false);
const xmlStr = ref('');
const svgData = ref('');

const panelBarClick = () => {
  // 延迟执行，否则会导致面板收起时，属性面板不显示
  panelFlag.value = !panelFlag.value;
  setTimeout(() => {
    showPanel.value = !panelFlag.value;
  }, 100);
};

/**
 * 初始化Canvas
 */
const initCanvas = () => {
  bpmnModeler.value = new BpmnModeler({
    container: canvas.value,
    // 键盘
    keyboard: {
      bindTo: window, // 或者window，注意与外部表单的键盘监听事件是否冲突
    },
    propertiesPanel: {
      parent: panel.value,
    },
    additionalModules: Modules,
    moddleExtensions: {
      flowable: flowableModdle,
    },
  });
};

/**
 * 初始化Model
 */
const initModel = () => {
  if (modelerStore.getModeler()) {
    modelerStore.getModeler().destroy();
    modelerStore.setModeler(undefined);
  }
  modelerStore.setModeler(bpmnModeler.value);
};

/**
 * 新建
 */
const newDiagram = async () => {
  proxy?.$modal.confirm('是否确认新建', () => {
    initDiagram();
  });
};

/**
 * 初始化
 */
const initDiagram = (xml?: string) => {
  if (!xml) xml = defaultXML;
  bpmnModeler.value.importXML(xml);
};

/**
 * 自适应屏幕
 */
const fitViewport = () => {
  zoom.value = bpmnModeler.value.get<Canvas>('canvas').zoom('fit-viewport');
  const bbox = document.querySelector<SVGGElement>('.app-containers .viewport').getBBox();
  const currentViewBox = bpmnModeler.value.get<Canvas>('canvas').viewbox();
  const elementMid = {
    x: bbox.x + bbox.width / 2 - 65,
    y: bbox.y + bbox.height / 2,
  };
  bpmnModeler.value.get<Canvas>('canvas').viewbox({
    x: elementMid.x - currentViewBox.width / 2,
    y: elementMid.y - currentViewBox.height / 2,
    width: currentViewBox.width,
    height: currentViewBox.height,
  });
  zoom.value = (bbox.width / currentViewBox.width) * 1.8;
};
/**
 * 放大或者缩小
 * @param zoomIn true 放大 | false 缩小
 */
const zoomViewport = (zoomIn = true) => {
  zoom.value = bpmnModeler.value.get<Canvas>('canvas').zoom();
  zoom.value += zoomIn ? 0.1 : -0.1;
  bpmnModeler.value.get<Canvas>('canvas').zoom(zoom.value);
};

/**
 * 下载XML
 */
const downloadXML = async () => {
  try {
    const { xml } = await bpmnModeler.value.saveXML({ format: true });
    downloadFile(`${getProcessElement().name}.bpmn20.xml`, xml, 'application/xml');
  } catch (e) {
    proxy?.$modal.msgError(e);
  }
};

/**
 * 下载SVG
 */
const downloadSVG = async () => {
  try {
    const { svg } = await bpmnModeler.value.saveSVG();
    downloadFile(getProcessElement().name, svg, 'image/svg+xml');
  } catch (e) {
    proxy?.$modal.msgError(e);
  }
};

/**
 * XML预览
 */
const previewXML = async () => {
  try {
    const { xml } = await bpmnModeler.value.saveXML({ format: true });
    xmlStr.value = xml;
    perviewXMLShow.value = true;
  } catch (e) {
    proxy?.$modal.msgError(e);
  }
};

/**
 * SVG预览
 */
const previewSVG = async () => {
  try {
    const { svg } = await bpmnModeler.value.saveSVG();
    svgData.value = svg;
    perviewSVGShow.value = true;
  } catch (e) {
    proxy?.$modal.msgError(e);
  }
};

const curNodeInfo = reactive({
  curType: '', // 任务类型 用户任务
  curNode: '',
  expValue: '', // 多用户和部门角色实现
});

const downloadFile = (fileName: string, data: any, type: string) => {
  const a = document.createElement('a');
  const url = window.URL.createObjectURL(new Blob([data], { type }));
  a.href = url;
  a.download = fileName;
  a.click();
  window.URL.revokeObjectURL(url);
};

const getProcessElement = () => {
  const rootElements = bpmnModeler.value?.getDefinitions().rootElements;
  for (let i = 0; i < rootElements.length; i++) {
    if (rootElements[i].$type === 'bpmn:Process') return rootElements[i];
  }
};

const getProcess = () => {
  const element = getProcessElement();
  return {
    id: element.id,
    name: element.name,
  };
};

const saveXml = async () => {
  const { xml } = await bpmnModeler.value.saveXML({ format: true });
  const { svg } = await bpmnModeler.value.saveSVG();
  const process = getProcess();
  const data = {
    xml,
    svg,
    key: process.id,
    name: process.name,
  };
  emit('saveCallBack', data);
};

const open = (xml?: string) => {
  openDialog();
  nextTick(() => {
    initDiagram(xml);
  });
};
const close = () => {
  closeDialog();
};

onMounted(() => {
  nextTick(() => {
    initCanvas();
    initModel();
  });
});

/**
 * 对外暴露子组件方法
 */
defineExpose({
  initDiagram,
  saveXml,
  open,
  close,
});
</script>

<style lang="less" scoped>
.containers {
  height: 100%;
  .app-containers {
    width: 100%;
    height: 100%;
    .canvas {
      width: 100%;
      height: 100%;
      background: url('data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iNDAiIGhlaWdodD0iNDAiIHhtbG5zPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwL3N2ZyI+PGRlZnM+PHBhdHRlcm4gaWQ9ImEiIHdpZHRoPSI0MCIgaGVpZ2h0PSI0MCIgcGF0dGVyblVuaXRzPSJ1c2VyU3BhY2VPblVzZSI+PHBhdGggZD0iTTAgMTBoNDBNMTAgMHY0ME0wIDIwaDQwTTIwIDB2NDBNMCAzMGg0ME0zMCAwdjQwIiBmaWxsPSJub25lIiBzdHJva2U9IiNlMGUwZTAiIG9wYWNpdHk9Ii4yIi8+PHBhdGggZD0iTTQwIDBIMHY0MCIgZmlsbD0ibm9uZSIgc3Ryb2tlPSIjZTBlMGUwIi8+PC9wYXR0ZXJuPjwvZGVmcz48cmVjdCB3aWR0aD0iMTAwJSIgaGVpZ2h0PSIxMDAlIiBmaWxsPSJ1cmwoI2EpIi8+PC9zdmc+');
    }
    .t-layout__header {
      height: 35px;
      padding: 0;
    }

    .process-panel {
      transition: width 0.25s ease-in;
      .process-panel-bar {
        width: 34px;
        height: 40px;
        .open-bar {
          width: 34px;
          line-height: 40px;
        }
      }
      // 收起面板样式
      &.hide {
        width: 34px;
        overflow: hidden;
        padding: 0;
        .process-panel-bar {
          width: 34px;
          height: 100%;
          box-sizing: border-box;
          display: block;
          text-align: left;
          line-height: 34px;
        }
        .process-panel-bar:hover {
          background-color: #f5f7fa;
        }
      }
    }
  }
}
pre {
  margin: 0;
  height: 100%;
  max-height: calc(80vh - 32px);
  overflow-x: hidden;
  overflow-y: auto;
  :deep(.hljs) {
    word-break: break-word;
    white-space: pre-wrap;
    padding: 0.5em;
  }
}

.open-bar {
  font-size: 20px;
  cursor: pointer;
  text-align: center;
}
.process-panel {
  box-sizing: border-box;
  padding: 0 8px 0 8px;
  border-left: 1px solid #eeeeee;
  box-shadow: #cccccc 0 0 8px;
  max-height: 100%;
  width: 480px;
  height: calc(100vh - 80px);
  //:deep(.t-collapse) {
  //  height: calc(100vh - 162px);
  //  overflow: auto;
  //}
}

// 任务栏 透明度
//:deep(.djs-palette) {
//  opacity: 0.3;
//  transition: all 1s;
//}
//
//:deep(.djs-palette:hover) {
//  opacity: 1;
//  transition: all 1s;
//}
</style>
