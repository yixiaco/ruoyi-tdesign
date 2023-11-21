import { defineStore } from 'pinia';
import { computed, reactive } from 'vue';

import type { NoticeItem } from '@/types/interface';

export const useNoticeStore = defineStore('notice', () => {
  const state = reactive({
    notices: [] as NoticeItem[],
  });

  function addNotice(notice: NoticeItem) {
    state.notices.push(notice);
  }

  function removeNotice(notice: NoticeItem) {
    state.notices.splice(state.notices.indexOf(notice), 1);
  }

  // 实现全部已读
  function readAll() {
    state.notices.forEach((item) => {
      item.read = true;
    });
  }

  const unreadMsg = computed(() => {
    return state.notices.filter((item: NoticeItem) => !item.read);
  });

  const readMsg = computed(() => {
    return state.notices.filter((item: NoticeItem) => item.read);
  });

  function clearNotice() {
    state.notices = [];
  }

  return {
    state,
    unreadMsg,
    readMsg,
    addNotice,
    removeNotice,
    readAll,
    clearNotice,
  };
});
