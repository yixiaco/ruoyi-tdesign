import { ref, toRefs } from 'vue';
// @ts-ignore
import useDictStore from '@/store/modules/dict';
import { getDicts } from '@/api/system/dict/data';

export interface DictModel {
  label: string;
  value: string;
  elTagType?: 'primary' | 'warning' | 'danger' | 'default' | 'success' | '';
  elTagClass?: string;
}

/**
 * 获取字典数据
 */
export function useDict(...args) {
  const res = ref<Record<string, Array<DictModel>>>({});
  return (() => {
    args.forEach((dictType) => {
      res.value[dictType] = [];
      const dicts = useDictStore().getDict(dictType);
      if (dicts) {
        res.value[dictType] = dicts;
      } else {
        getDicts(dictType).then((resp) => {
          res.value[dictType] = resp.data.map<DictModel>((p) => ({
            label: p.dictLabel,
            value: p.dictValue,
            elTagType: p.listClass,
            elTagClass: p.cssClass,
          }));
          useDictStore().setDict(dictType, res.value[dictType]);
        });
      }
    });
    return toRefs(res.value);
  })();
}
