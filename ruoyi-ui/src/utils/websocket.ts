/**
 * @module initWebSocket 初始化
 * @module websocketonopen 连接成功
 * @module websocketonerror 连接失败
 * @module websocketclose 断开连接
 * @module resetHeart 重置心跳
 * @module sendSocketHeart 心跳发送
 * @module reconnect 重连
 * @module sendMsg 发送数据
 * @module websocketonmessage 接收数据
 * @module test 测试收到消息传递
 * @description socket 通信
 * @param {any} url socket地址
 * @param {any} websocket websocket 实例
 * @param {any} heartTime 心跳定时器实例
 * @param {number} socketHeart 心跳次数
 * @param {number} HeartTimeOut 心跳超时时间
 * @param {number} socketError 错误次数
 */
import { NotifyPlugin } from 'tdesign-vue-next';

import { useNoticeStore, useUserStore } from '@/store';

const clientId = import.meta.env.VITE_CLIENT_ID;
let socketUrl = ''; // socket地址
let websocket: WebSocket = null; // websocket 实例
let heartTime: any = null; // 心跳定时器实例
// let socketHeart = 0; // 心跳次数
const HeartTimeOut = 10000; // 心跳超时时间 10000 = 10s
let socketError = 0; // 错误次数
let isClose = true;

// 初始化socket
export function initWebSocket(url: string) {
  if (import.meta.env.VITE_APP_WEBSOCKET === 'false') {
    return null;
  }
  socketUrl = url;
  isClose = false;
  // 初始化 websocket
  websocket = new WebSocket(`${url}?Authorization=Bearer ${useUserStore().token}&clientId=${clientId}`);
  websocketOnOpen();
  websocketOnMessage();
  websocketOnError();
  websocketClose();
  sendSocketHeart();
  return websocket;
}

// socket 连接成功
function websocketOnOpen() {
  websocket.onopen = () => {
    console.debug('连接 websocket 成功', websocket.url.substring(0, websocket.url.indexOf('?')));
    resetHeart();
  };
}

// socket 连接失败
function websocketOnError() {
  websocket.onerror = (e: any) => {
    console.debug('连接 websocket 失败', e);
  };
}

// socket 断开链接
function websocketClose() {
  websocket.onclose = (e: any) => {
    console.debug('断开连接', e);
  };
}

// socket 重置心跳
function resetHeart() {
  // socketHeart = 0;
  socketError = 0;
  clearInterval(heartTime);
  sendSocketHeart();
}

// socket心跳发送
function sendSocketHeart() {
  heartTime = setInterval(() => {
    // 如果连接正常则发送心跳
    if (websocket.readyState === websocket.OPEN) {
      // if (socketHeart <= 30) {
      // websocket.send(
      //   JSON.stringify({
      //     type: 'ping',
      //   }),
      // );
      // socketHeart += 1;
    } else if (!isClose) {
      // 重连
      reconnect();
    }
  }, HeartTimeOut);
}

// socket重连
function reconnect() {
  clearInterval(heartTime);
  if (socketError <= 50) {
    socketError++;
    // 5秒后重连
    setTimeout(() => {
      initWebSocket(socketUrl);
      console.debug('socket重连', socketError);
    }, 5000);
  } else {
    console.log('重试次数已用完');
  }
}

// socket 发送数据
export function sendMsg(data: any) {
  websocket.send(data);
}

// 关闭websocket
export function closeWebsocket() {
  isClose = true;
  websocket?.close();
  console.debug('socket关闭');
}

// socket 接收数据
function websocketOnMessage() {
  websocket.onmessage = (e: MessageEvent) => {
    console.log(e);
    if (e.data.indexOf('heartbeat') > 0) {
      console.debug('websocket: heartbeat');
      resetHeart();
    }
    if (e.data.indexOf('ping') > 0) {
      console.debug('websocket: ping');
      return;
    }
    useNoticeStore().addNotice({
      message: e.data,
      read: false,
      time: new Date().toLocaleString(),
    });
    NotifyPlugin('success', {
      title: '消息',
      content: e.data,
      duration: 5000,
      placement: 'bottom-right',
      closeBtn: true,
    });
    // eslint-disable-next-line consistent-return
    return e.data;
  };
}
