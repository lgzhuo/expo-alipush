import { requireNativeViewManager } from 'expo-modules-core';
import * as React from 'react';

import { AliyunPushViewProps } from './AliyunPush.types';

const NativeView: React.ComponentType<AliyunPushViewProps> =
  requireNativeViewManager('AliyunPush');

export default function AliyunPushView(props: AliyunPushViewProps) {
  return <NativeView {...props} />;
}
