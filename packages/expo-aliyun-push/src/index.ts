import { NativeModulesProxy, EventEmitter, Subscription } from 'expo-modules-core';

// Import the native module. On web, it will be resolved to AliyunPush.web.ts
// and on native platforms to AliyunPush.ts
import AliyunPushModule from './AliyunPushModule';
import AliyunPushView from './AliyunPushView';
import { ChangeEventPayload, AliyunPushViewProps } from './AliyunPush.types';

// Get the native constant value.
export const PI = AliyunPushModule.PI;

export function hello(): string {
  return AliyunPushModule.hello();
}

export async function setValueAsync(value: string) {
  return await AliyunPushModule.setValueAsync(value);
}

const emitter = new EventEmitter(AliyunPushModule ?? NativeModulesProxy.AliyunPush);

export function addChangeListener(listener: (event: ChangeEventPayload) => void): Subscription {
  return emitter.addListener<ChangeEventPayload>('onChange', listener);
}

export { AliyunPushView, AliyunPushViewProps, ChangeEventPayload };
