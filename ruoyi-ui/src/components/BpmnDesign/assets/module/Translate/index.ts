import zh from '@/components/BpmnDesign/assets/lang/zh';

const customTranslate = (template: string, replacements: any) => {
  replacements = replacements || {};
  template = zh[template] || template;
  return template.replace(/{([^}]+)}/g, (_: any, key: any) => {
    return replacements[key] || `{${key}}`;
  });
};

export const translateModule = {
  translate: ['value', customTranslate],
};

export default translateModule;
