import { NativeModules, NativeEventEmitter } from 'react-native';

export interface CALLBACK_EVENT_TYPE {
  onClickLike: string; // 点击了喜欢
  onClickLast: string; // 点击了上一首
  onClickPlay: string; // 点击了播放
  onClickNext: string; // 点击了下一首
  onClickClose: string; // 点击了关闭
}

export interface AUDIO_PROPS_TYPE {
  title: string;
  cover: string;
  description: string;
  is_like?: boolean;
  is_play?: boolean;
}

const listenerCache: any = {};

export default (config: AUDIO_PROPS_TYPE) => {
  const { RNAudioNotification } = NativeModules;
  if (!RNAudioNotification) return;
  const eventEmitter = new NativeEventEmitter(RNAudioNotification);
  RNAudioNotification.initAudioNotification({ ...config });

  const setAudioConfig = (new_config: AUDIO_PROPS_TYPE) =>
    RNAudioNotification.setAudioConfig({ ...new_config });
  const notify = () => RNAudioNotification.toNotify();
  const cancel = () => RNAudioNotification.toCancel();
  const update = () => RNAudioNotification.toUpdate();

  return {
    notify,
    cancel,
    update,
    setAudioConfig,
    subscribe: (
      type: keyof CALLBACK_EVENT_TYPE,
      callback: (event: any) => void
    ) => {
      if (listenerCache[type]) {
        listenerCache[type].remove();
      }
      return (listenerCache[type] = eventEmitter.addListener(
        type,
        (event: any) => {
          callback(event);
        }
      ));
    },
  };
};
