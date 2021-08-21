import React, { useEffect, useRef } from 'react';

import { StyleSheet, Text, TouchableOpacity, View } from 'react-native';
import AudioNotification from 'react-native-audio-notification';

export default function App() {
  const notRef = useRef<any>();
  useEffect(() => {
    const not = AudioNotification({
      title: 'hello bin',
      description: 'aaaaaa',
      cover:
        'https://cos.haxibiao.com/storage/app-haxibiao/images60d9f18cbe7f4.png',
      is_play: true,
      is_like: true,
    });
    notRef.current = not;
    not?.subscribe('onClickLike', (event: any) => {
      console.log('用户点击了喜欢', event);
    });

    not?.subscribe('onClickLast', (event: any) => {
      notRef.current.setAudioConfig({
        title: '这是上一首歌啦！',
        description: '好听吧？',
        cover: 'https://cos.haxibiao.com/storage/avatar/avatar-2.jpg',
      });
      notRef.current?.update();
      console.log('用户点击了上一首', event);
    });

    not?.subscribe('onClickPlay', (event: any) => {
      console.log('用户点击了播放/暂停', event);
    });

    not?.subscribe('onClickNext', (event: any) => {
      console.log('用户点击了下一首', event);
    });

    not?.subscribe('onClickClose', (event: any) => {
      console.log('用户点击了关闭', event);
      notRef.current?.cancel();
    });
  }, []);

  return (
    <View style={styles.container}>
      <TouchableOpacity
        style={styles.button}
        onPress={() => {
          notRef.current?.notify();
        }}
      >
        <Text style={{ color: '#FFF' }}>推送通知</Text>
      </TouchableOpacity>

      <TouchableOpacity
        style={styles.button}
        onPress={() => {
          notRef.current?.cancel();
        }}
      >
        <Text style={{ color: '#FFF' }}>关闭通知</Text>
      </TouchableOpacity>

      <TouchableOpacity
        style={styles.button}
        onPress={() => {
          notRef.current.setAudioConfig({
            title: '修改成功啦！！！',
            description: '你看这是已经修改好了的',
            cover:
              'https://cos.haxibiao.com/storage/app-haxibiao/images61206e53659ce.png',
          });
          notRef.current?.update();
        }}
      >
        <Text style={{ color: '#FFF' }}>修改通知</Text>
      </TouchableOpacity>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
  box: {
    width: 60,
    height: 60,
    marginVertical: 20,
  },
  button: {
    paddingHorizontal: 35,
    paddingVertical: 15,
    borderRadius: 100,
    backgroundColor: '#F76',
    marginBottom: 15,
  },
});
