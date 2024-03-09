/**
 * 数组操作工具
 */
export class ArrayOps {
  /**
   * 交换数组下标
   * @param arr 数组
   * @param index1 下标1
   * @param index2 下标2
   */
  static swapIndex(arr: Array<any>, index1: number, index2: number) {
    const temp = arr[index1];
    arr[index1] = arr[index2];
    arr[index2] = temp;
  }

  /**
   * 对原数组删除元素
   * @param arr 数组
   * @param removeData 删除的元素
   */
  static deleteElement<T>(arr: Array<T>, removeData: T | Array<T>) {
    if (arr?.length > 0 && removeData) {
      const removeSet = new Set(Array.isArray(removeData) ? removeData : [removeData]);
      for (let i = 0; i < arr.length; i++) {
        if (removeSet.has(arr[i])) {
          // 使用splice方法删除当前元素
          arr.splice(i, 1);

          // 因为splice会改变数组，我们需要减少索引来补偿删除的元素
          i--;
        }
      }
    }
    return arr;
  }

  /**
   * 快速删除。如果数组或需要删除的数据为空，则原样返回原数组
   * @param arr 数组
   * @param removeData 删除的元素
   */
  static fastDeleteElement<T>(arr: Array<T>, removeData: T | Array<T>) {
    if (arr?.length > 0 && removeData) {
      // JS中Set是有序的
      const set = new Set(arr ?? []);
      if (Array.isArray(removeData)) {
        removeData.forEach((value) => {
          set.delete(value);
        });
      } else {
        set.delete(removeData);
      }
      return Array.from(set);
    }
    return arr;
  }

  /**
   * 将数组转为Map。在JS中Map是有序的
   * @param arr 数组
   */
  static toMap<T>(arr: Array<T>): Map<number, T> {
    const map = new Map<number, T>();
    arr.forEach((value, index) => {
      map.set(index, value);
    });
    return map;
  }

  /**
   * 将数组转为对象。对象可能是无序的键值对的集合，因此建议属性不要作为顺序依据
   * @param arr 数组
   */
  static toObject<T>(arr: Array<T>): Record<number, T> {
    const obj: Record<number, T> = {};
    arr.forEach((value, index) => {
      obj[index] = value;
    });
    return obj;
  }
}
