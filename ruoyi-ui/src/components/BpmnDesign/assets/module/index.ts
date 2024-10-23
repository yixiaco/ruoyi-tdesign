// 翻译模块
import type { ModuleDeclaration } from 'didi';

import CustomContextPadProvider from './ContextPad/CustomContextPadProvider';
import CustomPaletteProvider from './Palette/CustomPaletteProvider';
import CustomRenderer from './Renderer/CustomRenderer';
import TranslationModule from './Translate';

const Module: ModuleDeclaration[] = [
  {
    __init__: ['customPaletteProvider', 'customContextPadProvider', 'customRenderer'],
    customPaletteProvider: ['type', CustomPaletteProvider],
    customRenderer: ['type', CustomRenderer],
    customContextPadProvider: ['type', CustomContextPadProvider],
  },
  TranslationModule,
];
export default Module;
