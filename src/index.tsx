import { NativeModules } from 'react-native';

type AudioNotificationType = {
  multiply(a: number, b: number): Promise<number>;
};

const { AudioNotification } = NativeModules;

export default AudioNotification as AudioNotificationType;
